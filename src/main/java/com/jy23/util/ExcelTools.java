package com.jy23.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * <dependency>
 * <groupId>org.apache.poi</groupId>
 * <artifactId>poi</artifactId>
 * <version>3.17</version>
 * </dependency>
 * <p>
 * <dependency>
 * <groupId>org.apache.poi</groupId>
 * <artifactId>poi-ooxml</artifactId>
 * <version>3.17</version>
 * </dependency>
 */
public class ExcelTools {
	private final static String excel2003L = ".xls";    //2003- 版本的excel
	private final static String excel2007U = ".xlsx";   //2007+ 版本的excel

	public HSSFWorkbook createExcel(List<String> titleList, List<List<String>> data) {
		//创建excel工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		//创建工作表sheet
		HSSFSheet sheet = workbook.createSheet();

		if (titleList != null && titleList.size() > 0) {
			//创建第一行
			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < titleList.size(); i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(titleList.get(i));
			}
		}

		int index = (titleList != null && titleList.size() > 0 ? 1 : 0);

		if (data != null && data.size() > 0) {
			for (int i = 0, rowSize = data.size() ; i < rowSize; i++) {
				List<String> temp = data.get(i);
				HSSFRow row = sheet.createRow(i+index);
				for (int j = 0, colSize = data.get(i).size(); j < colSize; j++) {
					HSSFCell cell = row.createCell(j);
					cell.setCellValue(temp.get(j));
				}
			}
		}
		return workbook;
	}

	public static List<List<String>> parse(File file) throws Exception {
		ExcelTools tools = new ExcelTools();
		List<List<String>> bankListByExcel = tools.getBankListByExcel(new FileInputStream(file), file.getName());
		return bankListByExcel;
	}

	/**
	 * 描述：根据文件后缀，自适应上传文件的版本
	 */
	public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
		Workbook wb = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (excel2003L.equals(fileType)) {
			wb = new HSSFWorkbook(inStr);  //2003-
		} else if (excel2007U.equals(fileType)) {
			wb = new XSSFWorkbook(inStr);  //2007+
		} else {
			throw new Exception("解析的文件格式有误！");
		}
		return wb;
	}

	/**
	 * 描述：获取IO流中的数据，组装成List<List<Object>>对象
	 *
	 * @param in,fileName
	 * @return
	 */
	private List<List<String>> getBankListByExcel(InputStream in, String fileName) throws Exception {
		List<List<String>> list = null;

		//创建Excel工作薄
		Workbook work = this.getWorkbook(in, fileName);
		if (null == work) {
			throw new Exception("创建Excel工作薄为空！");
		}
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;

		list = new ArrayList<List<String>>();
		//遍历Excel中所有的sheet
		int sheetSize = work.getNumberOfSheets();
		System.out.println("sheet 数目：" + sheetSize);
		for (int i = 0; i < sheetSize; i++) {
			sheet = work.getSheetAt(i);
			if (sheet == null) {
				continue;
			}

			int aa = sheet.getFirstRowNum();
			int bb = sheet.getLastRowNum();
			//遍历当前sheet中的所有行
			for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
				row = sheet.getRow(j);
				if (row == null) {
					continue;
				}

				boolean isEmptyRow = true;
				//过滤
				for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
					cell = row.getCell(y);
					if (this.getCellValue(cell) != null && !this.getCellValue(cell).equals("")) {
						isEmptyRow = false;
						break;
					}
				}

				if (!isEmptyRow) {
					//遍历所有的列
					List<String> li = new ArrayList<String>();
					for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
						cell = row.getCell(y);
						li.add(this.getCellValue(cell));
					}
					list.add(li);
				}
			}
		}
		work.close();
		return list;
	}

	/**
	 * 描述：对表格中数值进行格式化
	 */
	private String getCellValue(Cell cell) {
		String value = null;
		DecimalFormat df = new DecimalFormat("0");  //格式化number String字符
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");  //日期格式化
		DecimalFormat df2 = new DecimalFormat("0.00");  //格式化数字  

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if ("General".equals(cell.getCellStyle().getDataFormatString())) {
				value = df.format(cell.getNumericCellValue());
			} else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
				value = sdf.format(cell.getDateCellValue());
			} else {
				value = df2.format(cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = cell.getBooleanCellValue() + "";
			break;
		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;
		default:
			break;
		}
		return value;
	}
}
