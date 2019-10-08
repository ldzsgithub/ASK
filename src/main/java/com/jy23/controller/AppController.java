package com.jy23.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("app")
public class AppController {
	
	@RequestMapping("update")
	public ModelAndView update() {
		ModelAndView modelAndView = new ModelAndView("app/update");
		return modelAndView;
	}
}
