package com.jy23.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.AskAlarmRecords;
import com.jy23.entity.Probe;
import com.jy23.entity.RespEntry;
import com.jy23.server.AskAlarmRecordsServer;
import com.jy23.server.ProbeServer;
import com.jy23.util.Constant;
import com.jy23.util.ExcelTools;
import com.jy23.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("askAlarmRecords")
@Slf4j
public class AskAlarmRecordsController {

	@Autowired
	private AskAlarmRecordsServer askAlarmRecordsServer;
	@Autowired
	private ProbeServer probeServer;
	
	/*
	 * 探头报警历史
	 */
	@RequestMapping("toProbeAlarm")
	public ModelAndView toProbeAlarm() {
		ModelAndView modelAndView=new ModelAndView("chart/probeAlarmForm");
		Calendar now = Calendar.getInstance(); 
		Date end=now.getTime();
		modelAndView.addObject("endTime",end);
		now.add(Calendar.DAY_OF_MONTH, -1);
		Date start=now.getTime();
		modelAndView.addObject("startTime",start);
		return modelAndView;
	}
	
	/*
	 * 探头报警历史-查询
	 */
	@RequestMapping("probeAlarm")
	public ModelAndView findSelectiveByTime(Integer hostId, Integer departmentId, Integer probeBh, String startTime, String endTime,
			Integer startAlarmValue, Integer endAlarmValue,String authority) {
		try {
			ModelAndView modelAndView = new ModelAndView("chart/probeAlarm");
			AskAlarmRecords askAlarm = new AskAlarmRecords();
			askAlarm.setHostId(hostId != null && hostId != -1 ? hostId : null);
			askAlarm.setProbeBh(probeBh != null && probeBh != -1 ? probeBh : null);
			List<AskAlarmRecords> list =new ArrayList<>();
			if (departmentId != null && departmentId == -1) {
				String a[]=authority.split(",");
				for(int i=0;i<a.length;i++) {
					askAlarm.setDepartmentId(Integer.parseInt(a[i]));
					list.addAll(askAlarmRecordsServer.findSelectiveByTime(askAlarm, startTime, endTime, startAlarmValue, endAlarmValue));
				}
			} else {
				askAlarm.setDepartmentId(departmentId);
				list = askAlarmRecordsServer.findSelectiveByTime(askAlarm, startTime, endTime, startAlarmValue, endAlarmValue);
			}
			modelAndView.addObject("alarmList", list);
			if(list==null||list.size()==0)return null;
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * 导出excel
	 */
	@RequestMapping("probeAlarmExcel")
	public void probeAlarmExcel(Integer hostId, Integer departmentId,Integer probeBh, String startTime, String endTime, Integer startAlarmValue,
			Integer endAlarmValue,HttpServletRequest request,HttpServletResponse response) {
		try {
			AskAlarmRecords askAlarm = new AskAlarmRecords();
			askAlarm.setDepartmentId(departmentId != null && departmentId != -1 ? departmentId : null);
			askAlarm.setHostId(hostId != null && hostId != -1 ? hostId : null);
			askAlarm.setProbeBh(probeBh != null && probeBh != -1 ? probeBh : null);
			List<AskAlarmRecords> list = askAlarmRecordsServer.findSelectiveByTime(askAlarm, startTime, endTime, startAlarmValue, endAlarmValue);
			List<String> titleList = new ArrayList<>();
			titleList.add("序号");
			titleList.add("工厂名称");
			titleList.add("主机名称");
			titleList.add("检测点名称");
			titleList.add("报警名称");
			titleList.add("报警数值");
			titleList.add("报警日期");
			List<List<String>> data = new ArrayList<>();
			List<String> temp = null;
			for (int i = 0, size = list.size(); i < size; i++) {
				temp = new ArrayList<>();
				temp.add(String.valueOf(i + 1));
				temp.add(list.get(i).getDepartmentName());
				temp.add(list.get(i).getHostName());
				temp.add(list.get(i).getProbeName());
				temp.add(list.get(i).getAlarmMc());
				temp.add(String.valueOf(list.get(i).getAlarmValue()));
				temp.add(TimeUtils.Y_M_D_H_M_S(list.get(i).getAlarmTime()));
				data.add(temp);
			}
			ExcelTools tools = new ExcelTools();
			HSSFWorkbook workbook = tools.createExcel(titleList, data);
			String fileName ="探头报警历史"+".xls";
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1") );
			
			File file = new File("D:\\"+fileName);
		    file.createNewFile();
			FileOutputStream stream = FileUtils.openOutputStream(file);
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			workbook.write(stream);
			stream.close();
			return ;
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}
	}
	
	@ResponseBody
	@RequestMapping("save")
	public Object insertSelective(Integer probeBh, String probeName, Double alarmValue, String alarmMc, Integer hostId, String hostName,
			Integer departmentId, String departmentName, Date alarmTime) {
		try {
			AskAlarmRecords askAlarmRecords = new AskAlarmRecords();
			askAlarmRecords.setProbeBh(probeBh);
			askAlarmRecords.setProbeName(probeName);
			askAlarmRecords.setAlarmValue(alarmValue);
			askAlarmRecords.setAlarmMc(alarmMc);
			askAlarmRecords.setHostId(hostId);
			askAlarmRecords.setHostName(hostName);
			askAlarmRecords.setDepartmentId(departmentId);
			askAlarmRecords.setDepartmentName(departmentName);
			askAlarmRecords.setAlarmTime(alarmTime);
			boolean b = askAlarmRecordsServer.insertSelective(askAlarmRecords);
			if (b) {
				return new RespEntry("添加成功");
			} else {
				return new RespEntry("添加失败", 101);
			}
		} catch (Exception e) {
			return new RespEntry("异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("update")
	public Object updateSelective(Integer probeBh, String probeName, Double alarmValue, String alarmMc, Integer hostId, String hostName, Integer departmentId, String departmentName, Date alarmTime) {
		try {
			AskAlarmRecords askAlarmRecords = new AskAlarmRecords();
			askAlarmRecords.setProbeBh(probeBh);
			askAlarmRecords.setProbeName(probeName);
			askAlarmRecords.setAlarmValue(alarmValue);
			askAlarmRecords.setAlarmMc(alarmMc);
			askAlarmRecords.setHostId(hostId);
			askAlarmRecords.setHostName(hostName);
			askAlarmRecords.setDepartmentId(departmentId);
			askAlarmRecords.setDepartmentName(departmentName);
			askAlarmRecords.setAlarmTime(alarmTime);
			boolean b = askAlarmRecordsServer.updateSelective(askAlarmRecords);
			if (b) {
				return new RespEntry("修改成功");
			} else {
				return new RespEntry("修改失败", 101);
			}
		} catch (Exception e) {
			return new RespEntry("修改异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("deleteByPrimaryKey")
	public Object deleteByPrimaryKey(Long xh) {
		try {
			boolean b = askAlarmRecordsServer.deleteByPrimaryKey(xh);
			if (b) {
				return new RespEntry("删除成功");
			} else {
				return new RespEntry("删除失败", 101);
			}
		} catch (Exception e) {
			return new RespEntry("删除异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("findByPrimaryKey")
	public Object findByPrimaryKey(Long xh) {
		try {
			AskAlarmRecords askAlarmRecords = askAlarmRecordsServer.findByPrimaryKey(xh);
			return new RespEntry("查询成功", askAlarmRecords);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("findSelective")
	public Object findSelective(Integer probeBh, String probeName, Double alarmValue, String alarmMc, Integer hostId, String hostName, Integer departmentId, String departmentName, Date alarmTime) {
		try {
			AskAlarmRecords askAlarmRecords = new AskAlarmRecords();
			askAlarmRecords.setProbeBh(probeBh);
			askAlarmRecords.setProbeName(probeName);
			askAlarmRecords.setAlarmValue(alarmValue);
			askAlarmRecords.setAlarmMc(alarmMc);
			askAlarmRecords.setHostId(hostId);
			askAlarmRecords.setHostName(hostName);
			askAlarmRecords.setDepartmentId(departmentId);
			askAlarmRecords.setDepartmentName(departmentName);
			askAlarmRecords.setAlarmTime(alarmTime);
			List<AskAlarmRecords> list = askAlarmRecordsServer.findSelective(askAlarmRecords);
			return new RespEntry("查询成功", list);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("findSelectivePage")
	public Object findSelectivePage(@RequestParam(value = "pageSize", defaultValue = Constant.DEFAULT_PAGE_SIZE) Integer pageSize,
			@RequestParam(value = "pageNo", defaultValue = Constant.DEFAULT_PAGE_NO) Integer pageNo,Integer probeBh, String probeName, 
			Double alarmValue, String alarmMc, Integer hostId, String hostName, Integer departmentId, String departmentName, Date alarmTime) {
		try {
			AskAlarmRecords askAlarmRecords = new AskAlarmRecords();
			askAlarmRecords.setProbeBh(probeBh);
			askAlarmRecords.setProbeName(probeName);
			askAlarmRecords.setAlarmValue(alarmValue);
			askAlarmRecords.setAlarmMc(alarmMc);
			askAlarmRecords.setHostId(hostId);
			askAlarmRecords.setHostName(hostName);
			askAlarmRecords.setDepartmentId(departmentId);
			askAlarmRecords.setDepartmentName(departmentName);
			askAlarmRecords.setAlarmTime(alarmTime);
			List<AskAlarmRecords> list = askAlarmRecordsServer.findSelectivePage(askAlarmRecords, pageSize, pageNo);
			return new RespEntry("查询成功", list);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}

	@RequestMapping("findSelectivePageWeb")
	public Object findSelectivePageWeb(@RequestParam(value = "pageSize", defaultValue = Constant.DEFAULT_PAGE_SIZE) Integer pageSize,
			@RequestParam(value = "pageNo", defaultValue = Constant.DEFAULT_PAGE_NO) Integer pageNo,Integer probeBh, String probeName,
			Double alarmValue, String alarmMc, Integer hostId, String hostName, Integer departmentId, String departmentName, Date alarmTime) {
		try {
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("askAlarmRecords/askAlarmRecordsList");
			AskAlarmRecords queryAskAlarmRecords = new AskAlarmRecords();
			queryAskAlarmRecords.setProbeBh(probeBh);
			queryAskAlarmRecords.setProbeName(probeName);
			queryAskAlarmRecords.setAlarmValue(alarmValue);
			queryAskAlarmRecords.setAlarmMc(alarmMc);
			queryAskAlarmRecords.setHostId(hostId);
			queryAskAlarmRecords.setHostName(hostName);
			queryAskAlarmRecords.setDepartmentId(departmentId);
			queryAskAlarmRecords.setDepartmentName(departmentName);
			queryAskAlarmRecords.setAlarmTime(alarmTime);
			int count = askAlarmRecordsServer.count(queryAskAlarmRecords);
			int pages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
			modelAndView.addObject("pages", pages);
			modelAndView.addObject("pageSize", pageSize);
			modelAndView.addObject("pageNo", pageNo);
			List<AskAlarmRecords> list = askAlarmRecordsServer.findSelectivePage(queryAskAlarmRecords, pageSize, pageNo);
			modelAndView.addObject("list", list);
			return modelAndView;
		} catch (Exception e) {
			return null;
		}
	}


	@RequestMapping("pageJump")
	public ModelAndView pageJump(@RequestParam(defaultValue = "true") Boolean isAdd, Long xh) {
		try {
			ModelAndView modelAndView = new ModelAndView();
			AskAlarmRecords askAlarmRecords = null;
			if (isAdd) {
				askAlarmRecords = new AskAlarmRecords();
				modelAndView.addObject(Constant.FLAG_PAGE, Constant.FLAG_PAGE_ADD);
			} else {
				if (xh != null && xh > 0) {
					askAlarmRecords = askAlarmRecordsServer.findByPrimaryKey(xh);
				}
				modelAndView.addObject(Constant.FLAG_PAGE, Constant.FLAG_PAGE_UPDATE);
			}
			modelAndView.setViewName("askAlarmRecords/askAlarmRecordsManager");
			modelAndView.addObject("askAlarmRecords", askAlarmRecords);
			return modelAndView;
		} catch (Exception e) {
			return null;
		}
	}
	/*
	 * 实时数据 -点击图片
	 * 第一次点击图面进入页面时，查询展示当日记录
	 */
	@RequestMapping("toRealTimeProbeHistory")
	public ModelAndView toRealTimeProbeHistory(Integer hostId, Integer probeId) {
		try {
			ModelAndView modelAndView = new ModelAndView("chart/realTimeProbeHistory");
			modelAndView.addObject("hostId", hostId);
			modelAndView.addObject("probeId", probeId);
			String t = TimeUtils.Y_M_D(new Date());
			String startTime = t + " 00:00:00";
			String endTime = t + " 23:59:59";
			Probe probe = probeServer.findByPrimaryKey(probeId);
			AskAlarmRecords askAlarm = new AskAlarmRecords();
			askAlarm.setHostId(hostId != null && hostId != -1 ? hostId : null);
			askAlarm.setProbeBh(probe.getProbeBh() != null && probe.getProbeBh() != -1 ? probe.getProbeBh() : null);
            modelAndView.addObject("probeName",probe.getProbeName());
			List<AskAlarmRecords> list = askAlarmRecordsServer.findSelectiveByTime(askAlarm, startTime, endTime, null, null);
			if(list.size()==0) {
				return modelAndView;
			}
			modelAndView.addObject("hostName",list.get(0).getHostName());
			modelAndView.addObject("departmentName",list.get(0).getDepartmentName());
			modelAndView.addObject("alarmList", list);
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return null;
		}
	}

	
}

