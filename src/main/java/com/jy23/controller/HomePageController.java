package com.jy23.controller;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.HomePageP2;
import com.jy23.entity.Probe;
import com.jy23.entity.RespEntry;
import com.jy23.server.HomePageServer;
import com.jy23.server.ProbeServer;
import com.jy23.server.UsersServer;

@Controller
@RequestMapping("homePage")
public class HomePageController {
	@Autowired
	private HomePageServer homePageServer;
	@RequestMapping("index")
	public String index() {
		return "homePage/index";
	}
	
	@RequestMapping("p1")
	public String p1() {
		return "homePage/p1";
	}
	/*
	 * 氧气1
	 * 可燃气2
	 * 一氧化碳0
	 */
	@RequestMapping("pp1")
	@ResponseBody
	public RespEntry pp1() {
		int [] a=new int[3];
		a[0]=homePageServer.countByProbeType(1);
		a[1]=homePageServer.countByProbeType(2);
		a[2]=homePageServer.countByProbeType(0);
		return new RespEntry("",a);
	}
	
	@RequestMapping("p2")
	public String p2() {
		return "homePage/p2";
	}
	/*
	 * 查询部门名称，柱状图x轴
	 */
	@RequestMapping("pp2")
	@ResponseBody
	public RespEntry pp2() {
		List<String> list=homePageServer.selectDeptName();
		String [] a=new String[list.size()];
		for(int i=0;i<list.size();i++) {
			a[i]=list.get(i);
		}
		return new RespEntry("",a);
	}
	
	@RequestMapping("ppp2")
	@ResponseBody
	public RespEntry ppp2() {
		List<HomePageP2> list=homePageServer.selectDeptData();
		return new RespEntry("",list);
	}
	
	@RequestMapping("p3")
	public String p3() {
		return "homePage/p3";
	}
	
	@RequestMapping("p4")
	public String p4() {
		return "homePage/p4";
	}
	
	@RequestMapping("pp4")
	@ResponseBody
	public RespEntry pp4() {
		int data[] = new int[7];
		data[0] = homePageServer.selectTrouble(7);
		data[1] = homePageServer.selectTrouble(6);
		data[2] = homePageServer.selectTrouble(5);
		data[3] = homePageServer.selectTrouble(4);
		data[4] = homePageServer.selectTrouble(3);
		data[5] = homePageServer.selectTrouble(5);
		data[6] = homePageServer.selectTrouble(1);
		return new RespEntry("",data);
	}
	
	@RequestMapping("p5")
	public String p5() {
		return "homePage/p5";
	}
	
	@RequestMapping("pp5")
	@ResponseBody
	public RespEntry pp5() {
		int a = homePageServer.countAlarmProbe();
		int z = homePageServer.countTolalProbe();
		DecimalFormat df=new DecimalFormat("0.00");
		return new RespEntry("",df.format((float)a/z));
	}
	
	@Autowired
	private UsersServer usersServer;
	@Autowired
	private ProbeServer probeServer;
	@RequestMapping("p7")
	public ModelAndView p7() {
		ModelAndView modelAndView = new ModelAndView("homePage/p7");
		List<Probe> list =probeServer.realTimeAlarmChar(null);
		Collections.sort(list, new Comparator<Probe>() {
            @Override
            public int compare(Probe o1, Probe o2) {
            	if(o1.getProbeType()==o2.getProbeType())
            		return o2.getRealtimeValue().compareTo(o1.getRealtimeValue());
            	if(o1.getProbeType()==2)
            		return 1;
            	if(o2.getProbeType()==2)
            		return -1;
                return o2.getRealtimeValue().compareTo(o1.getRealtimeValue());
            }
        });
		for(int i=0;i<list.size();i++) {
			list.get(i).setUsers(usersServer.findUserById(list.get(i).getProbeManager()));
		}
		modelAndView.addObject("probes", list);
		return modelAndView;
	}
	
}
