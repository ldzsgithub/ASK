package com.jy23.controller;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.RespEntry;
import com.jy23.entity.Users;
import com.jy23.server.UsersServer;


@Controller
@RequestMapping("users")
public class UsersController {

    @Autowired
    private UsersServer usersServer;
    /*
     * 登录校验
     */
    @ResponseBody
    @RequestMapping("loginWeb")
    public RespEntry loginWeb(String users,String password) {
    	if(users.trim()=="")return new RespEntry("用户名不能为空",103);
    	if(password.trim()=="")return new RespEntry("密码不能为空",103);
    	Users user=new Users();
    	user.setName(users);
    	user.setPassword(password);
    	int a=usersServer.checkLogin(user);
    	if(a==1)return new RespEntry("登录成功");
    	return new RespEntry("用户名或密码错误",103);
    }
    /*
     * 注销
     */
    @RequestMapping("loginOut")
    public String loginOut(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().removeAttribute("userId");
        return "redirect:/";
    }
    /*
     * 注册
     */
    @RequestMapping("usersRegister")
	public String register() {
		return "user/userRegister";
	}
    
    @RequestMapping("registerUsers")
    @ResponseBody
	public RespEntry register(String users) {
    	if(users.trim()=="")return new RespEntry("用户名不能为空",100);
    	int a=usersServer.registerCheckUsers(users);
    	if(a!=0)return new RespEntry("用户名已存在",100);
    	return new RespEntry("此用户名可以使用");
	}
    @RequestMapping("registerPassword")
    @ResponseBody
	public RespEntry registerPassword(String password) {
    	if(password.trim()=="")return new RespEntry("密码不能为空",100);
		return new RespEntry("ok");
	}
    @RequestMapping("registerRepassword")
    @ResponseBody
	public RespEntry registerRepassword(String password,String repassword) {
    	if(password.equals(repassword))return new RespEntry("密码一致");
		return new RespEntry("两次输入密码不一致",100);
	}
    @RequestMapping("registerAll")
    @ResponseBody
	public RespEntry registerAll(String users,String password,String repassword,String phoneNumber) {
    	if(users.trim()=="") return new RespEntry("用户名不能为空",100);
    	int a=usersServer.registerCheckUsers(users);
    	if(a!=0) return new RespEntry("用户名已存在",100);
    	if(password.trim()=="") return new RespEntry("密码不能为空",100);
    	if(!password.equals(repassword)) return new RespEntry("两次输入密码不一致",100);
    	if(phoneNumber.trim()=="") return new RespEntry("请输入手机号码",100);
    	if(!(Pattern.compile("[0-9]*")).matcher(String.valueOf(phoneNumber)).matches()) return new RespEntry("请输入正确的手机号码", 101);
		return new RespEntry("ok");
	}
    @RequestMapping("registerUpdate")
    @ResponseBody
	public RespEntry registerUpdate(String password,String repassword) {
    	if(password.trim()=="") return new RespEntry("密码不能为空",100);
    	if(!password.equals(repassword)) return new RespEntry("两次输入密码不一致",100);
		return new RespEntry("ok");
	}
    
    @RequestMapping("save")
    @ResponseBody
	public String save(String users,String password,String phoneNumber) {
    	Users user=new Users();
    	user.setName(users);
    	user.setPassword(password);
    	user.setPhoneNumber(phoneNumber);
    	user.setLoginflag(3);
    	user.setId(UUID.randomUUID().toString().replaceAll("-",""));
    	usersServer.insertUsers(user);
		return "<script>window.parent.closeLayer(\"注册成功\");</script>";
	}
    
    @RequestMapping("update")
    @ResponseBody
	public String update(String id,String password) {
    	Users user=new Users();
    	user.setPassword(password);
    	user.setId(id);
    	usersServer.updateUsers(user);
    	return "<script>window.parent.closeLayer(\"修改成功\");</script>";
	}
    
    @RequestMapping("findAllUsers")
    public ModelAndView findAllUsers() {
    	ModelAndView modelAndView = new ModelAndView();
    	List<Users> list = usersServer.findAllUsers(null);
    	modelAndView.addObject("list", list);
    	modelAndView.setViewName("user/userManage");
    	return modelAndView;
    }
    
    @RequestMapping("updateUsers")
    public ModelAndView updateUsers(String id) {
    	ModelAndView modelAndView = new ModelAndView();
    	Users user =new Users();
    	user.setId(id);
    	List<Users> list = usersServer.findAllUsers(user);
    	modelAndView.addObject("users", list.get(0).getName());
    	modelAndView.addObject("id",list.get(0).getId());
    	modelAndView.setViewName("user/userUpdate");
    	return modelAndView;
    }
    @RequestMapping("indexUpdateUsers")
    public ModelAndView indexUpdateUsers(String users) {
    	ModelAndView modelAndView = new ModelAndView();
    	Users user =new Users();
    	user.setName(users);
    	List<Users> list = usersServer.findAllUsers(user);
    	modelAndView.addObject("users", list.get(0).getName());
    	modelAndView.addObject("id",list.get(0).getId());
    	modelAndView.setViewName("user/userUpdate");
    	return modelAndView;
    }
    @RequestMapping("deleteUsers")
    @ResponseBody
    public RespEntry deleteUsers(String id) {
    	usersServer.deleteUsers(id);
    	return new RespEntry("删除成功");
    }
    @RequestMapping("userInfo")
    public ModelAndView userInfo(String id) {
    	ModelAndView modelAndView = new ModelAndView("user/userInfo");
    	modelAndView.addObject("users", usersServer.findUserById(id));
    	return modelAndView;
    }
    
}
