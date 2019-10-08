package com.jy23.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.Department;
import com.jy23.entity.RespEntry;
import com.jy23.entity.Users;
import com.jy23.server.DepartmentServer;
import com.jy23.server.HostServer;
import com.jy23.server.UsersServer;

@Controller
@RequestMapping("userQx")
public class UserQxController {
	
	@Autowired
	UsersServer usersServer;
	@Autowired
	DepartmentServer departmentServer;
	@Autowired
	HostServer hostServer;
	/*
	 * 权限管理
	 */
	@RequestMapping("findAll")
    public ModelAndView findAllUsers() {
    	ModelAndView modelAndView = new ModelAndView();
    	List<Users> list = usersServer.findAllUsers(null);
    	modelAndView.addObject("list", list);
    	modelAndView.setViewName("userqx/userqxManage");
    	return modelAndView;
    }
	/*
	 * 权限管理-修改角色-修改
	 */
	@RequestMapping("update")
    @ResponseBody
	public String update(String id,Integer sel,String authority) {
		Users user=new Users();
		user.setId(id);
		user.setLoginflag(sel);
		if(sel==1) {
			List<Department>department=departmentServer.findDepartmentSelective(null);
			authority="";
			for(int i=0;i<department.size();i++) {
				authority+=department.get(i).getDepartmentId()+",";
			}
		}
		user.setAuthority(authority);
		usersServer.updateQx(user);
    	return "<script>window.parent.closeLayer(\"修改成功\");</script>";
	}
	/*
	 * 权限管理-修改角色
	 */
	@RequestMapping("updateQx")
    @ResponseBody
	public ModelAndView updateQx(String id) {
		ModelAndView modelAndView = new ModelAndView("userqx/userqxUpdate");
		modelAndView.addObject("id", id);
		Users users=usersServer.findUserById(id);
		List<Department>department=departmentServer.findDepartmentSelective(null);
		modelAndView.addObject("department", department);
		modelAndView.addObject("users", users);
    	return modelAndView;
	}
	/*
	 * 权限管理-修改部门
	 */
	@RequestMapping("updateDeptQx")
	@ResponseBody
	public ModelAndView updateDeptQx(String id,String authority) {
		ModelAndView modelAndView = new ModelAndView("userqx/userDept");
		modelAndView.addObject("id", id);
		Users users=usersServer.findUserById(id);
		List<Department>department=departmentServer.findDepartmentSelective(null);
		modelAndView.addObject("department", department);
		modelAndView.addObject("users", users);
    	return modelAndView;
	}
	/*
	 * 权限管理-修改部门-修改
	 */
	@RequestMapping("updateDeptSub")
	@ResponseBody
	public String updateQx(String id,String authority) {
		Users user=new Users();
		user.setId(id);
		user.setAuthority(authority);
		usersServer.updateQx(user);
		return "<script>window.parent.closeLayer(\"修改成功\");</script>";
	}
	/*
	 * 部门权限
	 */
	@RequestMapping("selectAuthority")
	@ResponseBody
	public RespEntry selectAuthority(String authority) {
		if(authority==""||authority==null) return new RespEntry("当前用户没有权限",1);
		String[]a=authority.split(",");
		List<Department>dept=new ArrayList<>();
		for(int i=0;i<a.length;i++) {
			dept.add(usersServer.selectAuthority(Integer.parseInt(a[i])));
		}
		return new RespEntry("",0,dept);
	}
	/*
	 * 根据主机id查询部门，再查询部门下的负责人
	 */
	@RequestMapping("selectUser")
	@ResponseBody
	public RespEntry selectAuthority(Integer hostId) {
		List<Users>users=usersServer.findUsersByAuthority(""+hostServer.findByPrimaryKey(hostId).getDepartmentId());
		return new RespEntry("",0,users);
	}
	
}
