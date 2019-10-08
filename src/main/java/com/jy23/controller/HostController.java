package com.jy23.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.Area;
import com.jy23.entity.Department;
import com.jy23.entity.Host;
import com.jy23.entity.HostOrder;
import com.jy23.entity.RespEntry;
import com.jy23.exception.MsgException;
import com.jy23.server.DepartmentServer;
import com.jy23.server.HistoryServer;
import com.jy23.server.HostServer;
import com.jy23.util.Constant;
import com.jy23.util.ResultStatusCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("host")
public class HostController {

	@Autowired
	private HostServer hostServer;
	@Autowired
	private DepartmentServer departmentServer;
	@RequestMapping("/findSelectivePageWeb")
	public ModelAndView findSelectivePageWeb(Integer hostAddress, String hostName, String hostPosition, String hostRemarks, 
			Integer hostEnable, Integer companyId, Integer departmentId, Integer numberChannels,String authority,
			@RequestParam(value = "pageSize", defaultValue = Constant.DEFAULT_PAGE_SIZE) Integer pageSize,
			@RequestParam(value = "pageNo", defaultValue = Constant.DEFAULT_PAGE_NO) Integer pageNo) {
		try {
			ModelAndView modelAndView = new ModelAndView("host/hostList");
			Host host = new Host();
			host.setHostAddress(hostAddress);
			host.setHostName(hostName);
			host.setHostPosition(hostPosition);
			host.setHostRemarks(hostRemarks);
			host.setHostEnable(hostEnable);
			host.setCompanyId(companyId);
			host.setNumberChannels(numberChannels);
			int count = 0;
			List<Host> list = new ArrayList<>();
			if(authority!=null&&authority!="") {
				String a[]=authority.split(",");
				int qx[] = new int[a.length];
				for(int i=0;i<a.length;i++) {
					host.setDepartmentId(Integer.parseInt(a[i]));
					count+=hostServer.count(host);
					qx[i]=Integer.parseInt(a[i]);
				}
				list=hostServer.selectHostByQx(pageSize, pageNo,qx,hostName);
			} 
			int pages = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
			modelAndView.addObject("list", list);
			modelAndView.addObject("pages", pages);
			modelAndView.addObject("pageSize", pageSize);
			modelAndView.addObject("pageNo", pageNo);
			modelAndView.addObject("host", host);
			modelAndView.addObject("authority", authority);
			return modelAndView;
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return null;
		}
	}

	@RequestMapping("/pageJump")
	public ModelAndView pageJump(Boolean isAdd, Integer id, String authority) {
		try {
			ModelAndView modelAndView = new ModelAndView("host/hostManager");
			Host host=new Host();
			if (isAdd) {
				modelAndView.addObject(Constant.FLAG_PAGE, Constant.FLAG_PAGE_ADD);
				host = new Host();
				host.setHostEnable(0);
				host.setNumberChannels(16);
				host.setTransStatus(0);
				host.setHostCollectiontime(3000);
			} else {
				modelAndView.addObject(Constant.FLAG_PAGE, Constant.FLAG_PAGE_UPDATE);
				if (id != null && id > 0) {
					host = hostServer.findByPrimaryKey(id);
				}
			}
			List<Department>dept=new ArrayList<>();
			Department department=new Department();
			String a[]=authority.split(",");
			for(int i=0;i<a.length;i++) {
				department.setDepartmentId(Integer.parseInt(a[i]));
				dept.addAll(departmentServer.findDepartmentSelective(department));
			}
			modelAndView.addObject("department", dept);
			modelAndView.addObject("host", host);
			return modelAndView;
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			return null;
		}
	}

	@ResponseBody
	@RequestMapping("/save")
	public Object insertSelective(String hostAddress,String hostName,String hostPosition,
			String hostRemarks, Integer hostEnable, Integer companyId,Integer departmentId,Integer numberChannels,Integer transStatus,
			HttpServletResponse httpServletResponse, String registerCard, Integer hostCollectiontime) {
		try {
			if(departmentId==-1)return new RespEntry("请选择部门名称", 101);
			if(hostAddress.trim()==null)return new RespEntry("主机地址不能为空", 101);
			if(!(Pattern.compile("[0-9]*")).matcher(String.valueOf(hostAddress)).matches())return new RespEntry("主机地址必须为数字", 101);
			if(hostName.trim()=="")return new RespEntry("主机名称不能为空", 101);
			if(hostPosition.trim()=="")return new RespEntry("主机位置不能为空", 101);
			Host host = new Host();
			host.setHostAddress(Integer.parseInt(hostAddress));
			host.setHostName(hostName);
			host.setHostPosition(hostPosition);
			host.setHostRemarks(hostRemarks);
			host.setHostEnable(hostEnable);
			host.setCompanyId(1);
			host.setDepartmentId(departmentId);
			host.setNumberChannels(numberChannels);
			host.setRegisterCard(registerCard);
			host.setHostCollectiontime(hostCollectiontime);
			host.setTransStatus(transStatus);
			boolean b = hostServer.insertSelective(host);
			if (b) {
				return new RespEntry("添加 成功");
			} else {
				return new RespEntry("添加失败", 101);
			}
		} catch (MsgException e) {
			return new RespEntry(e.getErrorMessage(), e.getErrorCode());
		} catch (Exception e) {
			return new RespEntry("异常", 100, e.getMessage());
		}
	}
	
	@Autowired
	private HistoryServer historyServer;
	
	@ResponseBody
	@RequestMapping("/update")
	public Object updateSelective(Integer hostId, String hostAddress, String hostName, String hostPosition,
			String hostRemarks, Integer hostEnable, Integer companyId, Integer departmentId, Integer numberChannels,
			String registerCard, Integer hostCollectiontime ,Integer transStatus) {
		try {
			if(departmentId==-1)return new RespEntry("请选择部门名称", 101);
			if(hostAddress.trim()==null)return new RespEntry("主机地址不能为空", 101);
			if(!(Pattern.compile("[0-9]*")).matcher(String.valueOf(hostAddress)).matches())return new RespEntry("主机地址必须为数字", 101);
			if(hostName.trim()=="")return new RespEntry("主机名称不能为空", 101);
			if(hostPosition.trim()=="")return new RespEntry("主机位置不能为空", 101);
			HostOrder hostOrder = new HostOrder();
			hostOrder.setHostAddress(Integer.parseInt(hostAddress));
			hostOrder.setRegisterCard(registerCard);
			hostOrder.setHostId(hostId);
			hostServer.updateHostOrder(hostOrder);
			historyServer.updateHistory(hostAddress,hostId);
			Host host = new Host();
			host.setHostId(hostId);
			host.setHostAddress(Integer.parseInt(hostAddress));
			host.setHostName(hostName);
			host.setHostPosition(hostPosition);
			host.setHostRemarks(hostRemarks);
			host.setHostEnable(hostEnable);
			host.setCompanyId(companyId);
			host.setDepartmentId(departmentId);
			host.setNumberChannels(numberChannels);
			host.setRegisterCard(registerCard);
			host.setHostCollectiontime(hostCollectiontime);
			host.setTransStatus(transStatus);
			boolean b = hostServer.updateSelective(host);
			if (b) {
				return new RespEntry("修改 成功");
			} else {
				return new RespEntry("修改失败", 101);
			}
		} catch(NumberFormatException e) {
			return new RespEntry("主机地址必须为数字", 101);
		} catch (Exception e) {
			return new RespEntry("修改异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("/deleteById")
	public Object deleteByPrimaryKey(Integer hostId) {
		try {
			boolean b = hostServer.deleteByPrimaryKey(hostId);
			if (b) {
				return new RespEntry("删除 成功");
			} else {
				return new RespEntry("删除失败", 101);
			}
		} catch (MsgException e) {
			return new RespEntry(ResultStatusCode.ERROR_CANNOT_BE_DELETED);
		} catch (Exception e) {
			return new RespEntry("删除异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("findSelective")
	public Object findHostSelective(Integer hostAddress, String hostName, String hostPosition, String hostRemarks,
			Integer hostEnable, Integer companyId, Integer departmentId, Integer numberChannels) {
		try {
			Host host = new Host();
			host.setHostAddress(hostAddress);
			host.setHostName(hostName);
			host.setHostPosition(hostPosition);
			host.setHostRemarks(hostRemarks);
			host.setHostEnable(hostEnable);
			host.setCompanyId(companyId);
			host.setDepartmentId(departmentId);
			host.setNumberChannels(numberChannels);
			List<Host> list = hostServer.findHostSelective(host);
			return new RespEntry("查询成功", list);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping("findArea")
	public Object findArea(Integer departmentId) {
		try {
			int a = departmentServer.countArea(departmentId);
			List<Host>list = new ArrayList<Host>();
			if(a>0) {
				List<Area> lst = departmentServer.findArea(departmentId);
				for (int i=0;i<lst.size();i++) {
					Host host = new Host();
					host.setHostId(lst.get(i).getId());
					host.setHostName(lst.get(i).getAreaName());
					list.add(host);
				}
			} else {
				Host host = new Host();
				host.setDepartmentId(departmentId);
				list = hostServer.findHostSelective(host);
			}
			return new RespEntry("查询成功", list);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}
}
