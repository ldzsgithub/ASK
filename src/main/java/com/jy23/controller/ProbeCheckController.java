package com.jy23.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.Host;
import com.jy23.entity.ProbeCheck;
import com.jy23.entity.RespEntry;
import com.jy23.server.HostServer;
import com.jy23.server.ProbeCheckServer;
import com.jy23.server.UsersServer;

@Controller
@RequestMapping("probeCheck")
public class ProbeCheckController {
	@Autowired
	private ProbeCheckServer probeCheckServer;
	@Autowired
	private UsersServer usersServer;
	@Autowired
	private HostServer hostServer;
	
	@RequestMapping("toProbeCheckList")
	public ModelAndView toProbeCheckList(Integer hostId,String probeName,String authority) {
        ModelAndView modelAndView = new ModelAndView("probe/probeCheckList");
        modelAndView.addObject("authority",authority);
        if(authority==null||authority=="") return modelAndView;
        List<Host>hosts = new ArrayList<>();
        Host host = new Host();
        String a[]=authority.split(",");
		for(int i=0;i<a.length;i++) {
			host.setDepartmentId(Integer.parseInt(a[i]));
			hosts.addAll(hostServer.findHostSelective(host));
		}
        List<ProbeCheck>list = new ArrayList<>();
        ProbeCheck probeCheck=new ProbeCheck();
        probeCheck.setProbeName(probeName);
        if(hostId==null||hostId==-1) {
        	probeCheck.setHostId(null);
        	for(int i=0;i<a.length;i++) {
        		probeCheck.setDepartmentId(Integer.parseInt(a[i]));
    			list.addAll(probeCheckServer.findAllProbeCheck(probeCheck));
    		}
        } else {
        	probeCheck.setHostId(hostId);
            list=probeCheckServer.findAllProbeCheck(probeCheck);
        }
        for(ProbeCheck p:list) {
        	p.setUser(usersServer.findUserById(p.getProbeManager()));
        }
        Collections.sort(list,new Comparator<ProbeCheck>() {
			@Override
			public int compare(ProbeCheck o1, ProbeCheck o2) {
				return o1.getCheckDate().compareTo(o2.getCheckDate());
			}
        });
        modelAndView.addObject("hostId",hostId);
        modelAndView.addObject("probeName",probeName);
        modelAndView.addObject("probeCheck",list);
        modelAndView.addObject("hosts",hosts);
        return modelAndView;
    }
	
	@RequestMapping("updateProbeCheck")
	@ResponseBody    
	public RespEntry updateProbeCheck(Integer probeId,String sTime) {
		try {
			ProbeCheck probeCheck=new ProbeCheck();
			probeCheck.setProbeId(probeId);
			probeCheck.setCreatDate(new Date());
			probeCheck.setCheckDate(new SimpleDateFormat("yyyy-MM-dd").parse(sTime));
			probeCheckServer.updateProbeCheck(probeCheck);
			return new RespEntry("修改成功");
		} catch (Exception e) {
			return new RespEntry("修改失败",1);
		}
	}
	@RequestMapping("toUpdateProbeCheck")
	public ModelAndView toUpdateProbeCheck(Integer probeId) {
		ModelAndView modelAndView = new ModelAndView("probe/probeCheckUpdate");
		modelAndView.addObject("probeId", probeId);
		return modelAndView;
	}
}
