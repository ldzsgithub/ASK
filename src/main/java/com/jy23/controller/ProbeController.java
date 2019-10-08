package com.jy23.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.Host;
import com.jy23.entity.Probe;
import com.jy23.entity.ProbeCheck;
import com.jy23.entity.RespEntry;
import com.jy23.server.HostServer;
import com.jy23.server.ProbeCheckServer;
import com.jy23.server.ProbeServer;
import com.jy23.server.UsersServer;
import com.jy23.util.Constant;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("probe")
@Slf4j
public class ProbeController {

	@Autowired
	private ProbeServer probeServer;
	@Autowired
	private HostServer hostServer;
	@Autowired
	private ProbeCheckServer probeCheckServer;
	@Autowired
	private UsersServer usersServer;
	
	@RequestMapping("findSelectivePageWeb")
	public ModelAndView findSelectivePageWeb(@RequestParam(value = "hostId", defaultValue = "-1") Integer hostId,String authority,Probe probe,
			@RequestParam(value = "pageSize", defaultValue = Constant.DEFAULT_PAGE_SIZE) Integer pageSize,
			@RequestParam(value = "pageNo", defaultValue = Constant.DEFAULT_PAGE_NO) Integer pageNo) {
		try {
			ModelAndView modelAndView = new ModelAndView("probe/probeList");
			List<Host>hosts=new ArrayList<>();
			if(authority!=null&&authority!="") {
				Host host=new Host();
				String a[]=authority.split(",");
				for(int i=0;i<a.length;i++) {
					host.setDepartmentId(Integer.parseInt(a[i]));
					hosts.addAll(hostServer.findHostSelective(host));
				}
			}
			modelAndView.addObject("hosts", hosts);
			List<Probe>list=new ArrayList<>();
			int count=0;
			int pages=0;
			if(hostId==-1) {
				if(authority!=null&&authority!="") {
					String a[]=authority.split(",");
					int qx[]=new int[a.length];
					for(int i=0;i<a.length;i++) {
						count+=probeServer.countProbeByQx(Integer.parseInt(a[i]),probe.getProbeName());
						qx[i]=Integer.parseInt(a[i]);
					}
					pages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
					list=probeServer.selectProbeByQx(pageSize, pageNo, qx,probe.getProbeName());
				}
			} else {
				count = probeServer.count(probe);
				pages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
				list= probeServer.findSelectivePage(pageSize, pageNo, probe);
			}
			for(int i=0;i<list.size();i++) {
				list.get(i).setUsers(usersServer.findUserById(list.get(i).getProbeManager()));
			}
			modelAndView.addObject("list", list);
			modelAndView.addObject("pages", pages);
			modelAndView.addObject("pageSize", pageSize);
			modelAndView.addObject("pageNo", pageNo);
			modelAndView.addObject("probe", probe);
			modelAndView.addObject("authority", authority);
			return modelAndView;
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return null;
		}
	}
	
	@RequestMapping("pageJump")
	public ModelAndView pageJump(Boolean isAdd, Integer id,String authority) {
		try {
			ModelAndView modelAndView = new ModelAndView("probe/probeManager");
			Probe probe = new Probe();
			if (isAdd) {
				probe.setProbeEnable(0);
				//probe.setHighValue(100.0);
				//probe.setLowValue(24.0);
				modelAndView.addObject(Constant.FLAG_PAGE, Constant.FLAG_PAGE_ADD);
			} else {
				if (id != null && id > 0) {
					probe = probeServer.findByPrimaryKey(id);
				}
				modelAndView.addObject(Constant.FLAG_PAGE, Constant.FLAG_PAGE_UPDATE);
			}
			List<Host>hosts=new ArrayList<>();
			Host host=new Host();
			String a[]=authority.split(",");
			for(int i=0;i<a.length;i++) {
				host.setDepartmentId(Integer.parseInt(a[i]));
				hosts.addAll(hostServer.findHostSelective(host));
			}
			modelAndView.addObject("hosts", hosts);
			modelAndView.addObject("probe", probe);
			return modelAndView;
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return null;
		}
	}

	@ResponseBody
	@RequestMapping("save")
	public RespEntry insertSelective(Integer hostId, Integer probeBh, String probeName, String probePosition,String probeRemarks, 
			 String lat, String lng, Double highValue, Double lowValue,Integer probeType,Integer checkMonth,String probeManager) {
		try {
			if(hostId==-1) return new RespEntry("请选择主机",1);
			if(probeBh==null) return new RespEntry("请选择编号",1);
			Probe probeBhCheck = new Probe();
			probeBhCheck.setHostId(hostId);
			probeBhCheck.setProbeBh(probeBh);
			List<Probe> probeBhChecks=probeServer.findProbeSelective(probeBhCheck);
			if(probeBhChecks.size()!=0) return new RespEntry("编号已存在",1);
			if(probeManager=="-1"||probeManager==null) return new RespEntry("请选择负责人",1);
			if(probeName.trim()=="") return new RespEntry("探头名称不能为空",1);
			if(probePosition.trim()=="") return new RespEntry("探头位置不能为空",1);
			Probe probe = new Probe();
			probe.setHostId(hostId);
			probe.setHostAddress(hostServer.findByPrimaryKey(hostId).getHostAddress());
			probe.setProbeBh(probeBh);
			probe.setProbeName(probeName);
			probe.setProbePosition(probePosition);
			probe.setProbeRemarks(probeRemarks);
			probe.setLat(lat);
			probe.setLng(lng);
			probe.setLowValue(lowValue);
			probe.setHighValue(highValue);
			probe.setProbeEnable(0);
			probe.setProbeType(probeType);
			probe.setProbeManager(probeManager);
			probeServer.insertSelective(probe);
			
			ProbeCheck probeCheck=new ProbeCheck();
			probeCheck.setProbeId(probe.getProbeId());
			Calendar calendar = Calendar.getInstance();
			probeCheck.setCreatDate(calendar.getTime());
			calendar.add(Calendar.MONTH,checkMonth);
			probeCheck.setCheckDate(calendar.getTime());
			probeCheckServer.insertProbeCheck(probeCheck);
			
			hostServer.addRegisterNumber(hostId);
			return new RespEntry("添加成功",0);
		} catch (Exception e) {
			return new RespEntry("添加异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("update")
	public Object updateSelective(Integer hostId,Integer probeId, Integer probeBh, String probeName, String probePosition,String probeRemarks, 
			 String lat, String lng, Double highValue, Double lowValue,Integer probeType,String probeManager) {
		try {
			if(hostId==-1) return new RespEntry("请选择主机",1);
			if(probeBh==null) return new RespEntry("请选择编号",1);
			if(probeManager=="-1"||probeManager==null) return new RespEntry("请选择负责人",1);
			if(probeName.trim()=="") return new RespEntry("探头名称不能为空",1);
			if(probePosition.trim()=="") return new RespEntry("探头位置不能为空",1);
			Probe probe = new Probe();
			probe.setHostId(hostId);
			probe.setHostAddress(hostServer.findByPrimaryKey(hostId).getHostAddress());
			probe.setProbeId(probeId);
			probe.setProbeBh(probeBh);
			probe.setProbeName(probeName);
			probe.setProbePosition(probePosition);
			probe.setProbeRemarks(probeRemarks);
			probe.setLat(lat);
			probe.setLng(lng);
			probe.setLowValue(lowValue);
			probe.setHighValue(highValue);
			probe.setProbeType(probeType);
			probe.setProbeManager(probeManager);
			probeServer.updateSelective(probe);
			return new RespEntry("修改成功",0);
		} catch (Exception e) {
			return new RespEntry("修改异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("deleteById")
	public Object deleteByPrimaryKey(Integer probeId) {
		try {
			hostServer.reduceRegisterNumber(probeId);
			probeCheckServer.deleteByHostId(probeId);
			boolean b = probeServer.deleteByPrimaryKey(probeId);
			if (b) {
				return new RespEntry("删除 成功");
			} else {
				return new RespEntry("删除失败", 101);
			}
		} catch (Exception e) {
			return new RespEntry("删除异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("findById")
	public Object findByPrimaryKey(Integer probeId) {
		try {
			Probe probe = probeServer.findByPrimaryKey(probeId);
			return new RespEntry("查询 成功", probe);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("findSelective")
	public Object findProbeSelective(Integer hostId, Integer probeBh, String probeName, String probePosition,String probeRemarks, 
			 String lat, String lng, Double highValue, Double lowValue) {
		try {
			Probe probe = new Probe();
			probe.setHostId(hostId);
			probe.setProbeBh(probeBh);
			probe.setProbeName(probeName);
			probe.setProbePosition(probePosition);
			probe.setProbeRemarks(probeRemarks);
			probe.setLat(lat);
			probe.setLng(lng);
			probe.setLowValue(lowValue);
			probe.setHighValue(highValue);
			List<Probe> list = probeServer.findProbeSelective(probe);
			return new RespEntry("查询成功", list);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("selectCanUseBH")
	public Object selectCanUseBH(Integer hostId) {
		try {
			Probe probe = new Probe();
			probe.setHostId(hostId);
			List<Probe> jcdList = probeServer.findProbeSelective(probe);                                                                                                                             
			int[]jcd=new int[hostServer.findByPrimaryKey(hostId).getNumberChannels()+1];
			for(int i = 0; i < jcdList.size(); i++) {
				jcd[jcdList.get(i).getProbeBh()]=1;
			}
			for(int i=1;i<jcd.length;i++) {
				if(jcd[i]==0)
					return new RespEntry("查询成功",0,i);
			}
			return new RespEntry("当前主机下气体检测点已满", 100, null);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}


	@ResponseBody
	@RequestMapping("selectRealTimeAll")
	public Object selectRealTimeAll() {
		try {
			List<Probe> list = probeServer.selectRealTimeAll();
			return new RespEntry("查询成功", list);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("countAlarm")
	public Object countAlarm() {
		try {
			int num = probeServer.countAlarm();
			return new RespEntry("查询成功", num + "");
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}

	@RequestMapping("toRealTimeCharWeb")
	public String toRealTime() {
		return "chart/realTimeData";
	}

	@ResponseBody
	@RequestMapping("haveAlarm")
	public RespEntry haveAlarm() {
		try {
			boolean b = probeServer.haveAlarm();
			return new RespEntry("ok",b);
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return null;
		}
	}

	@ResponseBody
	@RequestMapping("findSelectiveWeb")
	public RespEntry findSelectiveWeb(Integer hostId,Integer departmentId,String authority) {
		try {
			if (authority==""||authority==null)return new RespEntry("当前用户没有权限", 1);
			List<Probe> list=new ArrayList<>();
			if (hostId != null && hostId == -1) {
				hostId = null;
			}
			if (departmentId != null && departmentId == -1) {
				String a[]=authority.split(",");
				for(int i=0;i<a.length;i++) {
					list.addAll(probeServer.selectProbeByHIdAndPid(hostId,Integer.parseInt(a[i])));
				}
				return new RespEntry("ok", list);
			}
			list = probeServer.selectProbeByHIdAndPid(hostId,departmentId);
			return new RespEntry("ok", list);
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return new RespEntry("查询失败", 1);
		}
	}
	
	@ResponseBody
	@RequestMapping("findByArea")
	public RespEntry findByArea(Integer hostId,Integer departmentId,String authority) {
		try {
			if (authority==""||authority==null)return new RespEntry("当前用户没有权限", 1);
			List<Probe> list=new ArrayList<>();
			if (hostId != null && hostId == -1) {
				hostId = null;
			}
			if (departmentId != null && departmentId == -1) {
				String a[]=authority.split(",");
				for(int i=0;i<a.length;i++) {
					list.addAll(probeServer.selectProbeByHIdAndPid(hostId,Integer.parseInt(a[i])));
				}
				return new RespEntry("ok", list);
			} else {
				list = probeServer.selectByArea(hostId,departmentId);
				return new RespEntry("ok", list);
			}
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return new RespEntry("查询失败", 1);
		}
	}

	@RequestMapping("toProbeMapWeb")
	public ModelAndView toProbeMapWeb() {
		try {
			ModelAndView modelAndView = new ModelAndView("map/BMapAlarm");
			return modelAndView;
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return null;
		}
	}
	
	@RequestMapping("toCustomMap")
	public ModelAndView toCustomMap() {
		try {
			ModelAndView modelAndView = new ModelAndView("map/CustomMapAlarm");
			return modelAndView;
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return null;
		}
	}

	@ResponseBody
	@RequestMapping("alarm")
	public RespEntry alarm( ) {
		try {
			List<Probe> list = probeServer.selectRealTimeAll();
			return new RespEntry("查询成功", list);
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return null;
		}
	}

	@RequestMapping("toRealTimeAlarmCharWeb")
	public ModelAndView toRealTimeAlarmCharWeb(String authority) {
		try {
			ModelAndView modelAndView = new ModelAndView("chart/probeRealTimeAlarmList");
			if(authority==null||authority=="") return modelAndView;
			modelAndView.addObject("flag", 1);
			List<Probe> list =new ArrayList<>();
			String a[]=authority.split(",");
			for(int i=0;i<a.length;i++) {
				list.addAll(probeServer.realTimeAlarmChar(Integer.parseInt(a[i])));
			}
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
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return null;
		}
	}
	
}
