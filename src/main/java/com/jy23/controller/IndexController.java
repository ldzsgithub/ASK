package com.jy23.controller;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.Users;
import com.jy23.server.UsersServer;

@Controller
@RequestMapping("/")
public class IndexController {
	@Autowired
	UsersServer usersServer;
	
	@RequestMapping("/")
	public String index() {
		return "login";
	}
	
	@RequestMapping("toWelcome")
	public String welcome() {
		return "welcome";
	}
	
	@RequestMapping(value="/toMain",method=RequestMethod.POST)
    public ModelAndView toMain(String nameId) {
		ModelAndView modelAndView=new ModelAndView("index");
		Users userf =new Users();
		userf.setName(nameId);
		List<Users> user =usersServer.findAllUsers(userf);
		modelAndView.addObject("user", user.get(0));
		return modelAndView;
    }
	
	@Value("${server.port}")
    private String port;
	//@ResponseBody
	@RequestMapping(value="/{a}",method=RequestMethod.GET)
	public String erro(@PathVariable String a) throws UnknownHostException {
        //InetAddress addr = InetAddress.getLocalHost();  
        //String ip=addr.getHostAddress().toString();
		//return "遇到了错误..."+"<a href=\"http://"+ip+":"+port+"\">返回主页</a>";
        return "redirect:/";
	}
	
}
