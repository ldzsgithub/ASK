package com.jy23.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.LineChart;
import com.jy23.entity.Probe;
import com.jy23.entity.RespEntry;
import com.jy23.server.LineChartServer;

@Controller
@RequestMapping("lineChart")
public class LineChartController {

    @RequestMapping("toLineChart")
    public ModelAndView toLineChart() {
        ModelAndView modelAndView = new ModelAndView("chart/lineChart");
        modelAndView.addObject("sTime", new Date());
        return modelAndView;
    }
    
    @Autowired
    private LineChartServer lineChartServer;
    @RequestMapping("queryName")
    @ResponseBody
    public RespEntry queryName(Integer hostId) {
    try {
        List<Probe>list=lineChartServer.findProbeName(hostId);
        String []str=new String[16];
        for(int i=0;i<list.size();i++) {
        	str[list.get(i).getProbeBh()-1]=list.get(i).getProbeName();
        }
        return new RespEntry("查询成功", str);
    }
    catch (Exception e) {
    	e.printStackTrace();
    	return new RespEntry("查询失败", 1);
	}
    }
    @RequestMapping("query")
    @ResponseBody
    public RespEntry query(Integer departmentId,Integer hostId,String sTime) {
    	try {
        String tableName=sTime.replace("-","").substring(0, 6);
        String sDay=sTime.replace("-","").substring(6, 8);
        List<LineChart>list=lineChartServer.findLineChart(tableName, hostId, sDay);
        Double[][]data=new Double[16][24];
        for(int i=0;i<list.size();i++) {
        	data[0][Integer.parseInt(list.get(i).getH())]=list.get(i).getA1()==null||list.get(i).getA1()<=0?null:list.get(i).getA1();
        	data[1][Integer.parseInt(list.get(i).getH())]=list.get(i).getA2()==null||list.get(i).getA2()<=0?null:list.get(i).getA2();
        	data[2][Integer.parseInt(list.get(i).getH())]=list.get(i).getA3()==null||list.get(i).getA3()<=0?null:list.get(i).getA3();
        	data[3][Integer.parseInt(list.get(i).getH())]=list.get(i).getA4()==null||list.get(i).getA4()<=0?null:list.get(i).getA4();
        	data[4][Integer.parseInt(list.get(i).getH())]=list.get(i).getA5()==null||list.get(i).getA5()<=0?null:list.get(i).getA5();
        	data[5][Integer.parseInt(list.get(i).getH())]=list.get(i).getA6()==null||list.get(i).getA6()<=0?null:list.get(i).getA6();
        	data[6][Integer.parseInt(list.get(i).getH())]=list.get(i).getA7()==null||list.get(i).getA7()<=0?null:list.get(i).getA7();
        	data[7][Integer.parseInt(list.get(i).getH())]=list.get(i).getA8()==null||list.get(i).getA8()<=0?null:list.get(i).getA8();
        	data[8][Integer.parseInt(list.get(i).getH())]=list.get(i).getA9()==null||list.get(i).getA9()<=0?null:list.get(i).getA9();
        	data[9][Integer.parseInt(list.get(i).getH())]=list.get(i).getA10()==null||list.get(i).getA10()<=0?null:list.get(i).getA10();
        	data[10][Integer.parseInt(list.get(i).getH())]=list.get(i).getA11()==null||list.get(i).getA11()<=0?null:list.get(i).getA11();
        	data[11][Integer.parseInt(list.get(i).getH())]=list.get(i).getA12()==null||list.get(i).getA12()<=0?null:list.get(i).getA12();
        	data[12][Integer.parseInt(list.get(i).getH())]=list.get(i).getA13()==null||list.get(i).getA13()<=0?null:list.get(i).getA13();
        	data[13][Integer.parseInt(list.get(i).getH())]=list.get(i).getA14()==null||list.get(i).getA14()<=0?null:list.get(i).getA14();
        	data[14][Integer.parseInt(list.get(i).getH())]=list.get(i).getA15()==null||list.get(i).getA15()<=0?null:list.get(i).getA15();
        	data[15][Integer.parseInt(list.get(i).getH())]=list.get(i).getA16()==null||list.get(i).getA16()<=0?null:list.get(i).getA16();
        }
        return new RespEntry("查询成功", data);
    }
    catch (Exception e) {
    	e.printStackTrace();
    	return new RespEntry("查询失败", 1);
	}
    }
}
