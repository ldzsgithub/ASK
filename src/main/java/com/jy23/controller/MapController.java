package com.jy23.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("map")
public class MapController {
	/*
	 * 百度地图获取 map/BMapGetPoint
	 * 自定义地图 map/CustomMapGetPoint
	 */
    @RequestMapping("toBdMap")
    public ModelAndView toBdMap() {
        ModelAndView modelAndView = new ModelAndView("map/CustomMapGetPoint");
        return modelAndView;
    }
}

