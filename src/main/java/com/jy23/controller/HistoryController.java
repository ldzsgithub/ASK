package com.jy23.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.AskAlarmRecords;
import com.jy23.entity.Department;
import com.jy23.entity.HistoryDataApp;
import com.jy23.entity.HistoryDataWeb;
import com.jy23.entity.Host;
import com.jy23.entity.Probe;
import com.jy23.entity.RespEntry;
import com.jy23.server.DepartmentServer;
import com.jy23.server.HistoryServer;
import com.jy23.server.HostServer;
import com.jy23.server.ProbeServer;
import com.jy23.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("history")
@Slf4j
public class HistoryController {

    @Autowired
    private HistoryServer historyServer;
    @Autowired
    private HostServer hostServer;
    @Autowired
    private ProbeServer probeServer;


    @ResponseBody
    @RequestMapping("findHistoryCharApp")
    public Object findHistoryCharApp(Integer probeId, Integer hostId, String time) {
        try {
            String tableName = "";
            String[] split = time.split("-");
            tableName = split[0] + split[1];
//            if (StringUtil.isEntry(tableName)) {
//                return new RespEntry("年月字段不能为空", 106);
//            }
            List<HistoryDataApp> list = historyServer.findHistoryCharApp(tableName, time, hostId, probeId);
            return new RespEntry("查询成功", list);
        } catch (Exception e) {
            return new RespEntry("查询异常", 100, e.getMessage());
        }
    }

    /**
     * 根据检测点id获取检测点编号，然后再去历史表进行查询
     *
     */
    @ResponseBody
    @RequestMapping("findHistoryCharWeb")
    public Object findHistoryCharWeb(Integer probeId, Integer hostId, String time) {
        try {
            String tableName = "";//对传入的时间进行解析
            String[] split = time.split("-");
            tableName = split[0] + split[1];
//            if (StringUtil.isEntry(tableName)) {
//                return new RespEntry("年月字段不能为空", 106);
//            }
            List<HistoryDataWeb> list = historyServer.findHistoryCharWeb(tableName, time, hostId, probeId);
            if (list ==null || list.size()==0){

                List<String> temp = TimeUtils.oneDayAllMinute(time);
                list = new ArrayList<>(1440);
                for (int i = 0,size=temp.size(); i < size; i++) {
                    list.add(new HistoryDataWeb(0F,temp.get(i)));
                }
                log.info("填充数据，非数据库内的数据,共填充{}条",temp.size());
            }
            return new RespEntry("查询成功", list);
        } catch (Exception e) {
            return new RespEntry("查询异常", 100, e.getMessage());
        }
    }

    @Autowired
    private DepartmentServer departmentServer ;

    @RequestMapping("toHistoryCharWeb")
    public ModelAndView toHistoryCharWeb() {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("chart/historyData");
            List<Department> departmentList = departmentServer.findDepartmentSelective(null);
            modelAndView.addObject("departmentList", departmentList);
            return modelAndView;
        } catch (Exception e) {
            log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
            return null;
        }
    }

    @ResponseBody
    @RequestMapping("toHistoryProbeAlarm")
    public ModelAndView toHistoryProbeAlarm(Integer probeId, Integer hostId, String time) {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("chart/historyProbeAlarm");
            String tableName = "";
            String[] split = time.split("-");
            tableName = split[0] + split[1];
//            if (StringUtil.isEntry(tableName)) {
//                return null;
//            }
            List<HistoryDataWeb> list = historyServer.toHistoryProbeAlarm(tableName, time, hostId, probeId);
            if (list !=null &&list.size()>0){
                Host host = hostServer.findByPrimaryKey(hostId);
                Department department = departmentServer.findByPrimaryKey(host.getDepartmentId());
                Probe probe = probeServer.findByPrimaryKey(probeId);
                modelAndView.addObject("list",list);
                modelAndView.addObject("hostName",host.getHostName());
                modelAndView.addObject("departmentName",department.getDepartmentName());
                modelAndView.addObject("probeName",probe.getProbeName());
            }
            return modelAndView;
        } catch (Exception e) {
            log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
            return null;
        }
    }

    /*
     *  实时数据-clickimage-搜索
     */
    @RequestMapping("toRealTimeProbeHistory")
    public ModelAndView toRealTimeProbeHistory(Integer hostId,Integer probeId, String time) {
        try {
            ModelAndView modelAndView = new ModelAndView("chart/realTimeProbeHistory2");
            modelAndView.addObject("probeId",probeId);
            modelAndView.addObject("hostId", hostId);
            modelAndView.addObject("time",time);
            if(time ==null || time.equals("")){
            	return modelAndView;
            }
            String tableName=time.replace("-","");
            Probe probe = probeServer.findByPrimaryKey(probeId);
            String columName="A"+probe.getProbeBh();
            Host host = hostServer.findByPrimaryKey(hostId);
            Department department = departmentServer.findByPrimaryKey(host.getDepartmentId());
            modelAndView.addObject("hostName",host.getHostName());
            modelAndView.addObject("departmentName",department.getDepartmentName());
            modelAndView.addObject("probeName",probe.getProbeName());
            
            List<AskAlarmRecords> list = historyServer.findByMonth(tableName, columName, hostId);
            modelAndView.addObject("alarmList",list);
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
            return null;
        }
    }
    
}

