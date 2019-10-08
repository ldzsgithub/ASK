package com.jy23.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.Com;
import com.jy23.entity.RespEntry;
import com.jy23.server.ComServer;
import com.jy23.util.Constant;
import com.jy23.util.L;
import com.jy23.util.ValidateKit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("com")
public class ComController {
	@Autowired
	private ComServer comServer;
	@ResponseBody
	@RequestMapping("findSelective")
	public Object findComSelective(String comName, String comBautrate, String comEnable,Integer comState,
			String comJyw, String comTzw, String comSjw, String comLkz, Integer comMode, Integer comPort,Integer hostId) {
		try {
			Com com = new Com();
			com.setComName(comName);
			com.setComBautrate(comBautrate);
			com.setComEnable(comEnable);
			com.setComState(comState);
			com.setComJyw(comJyw);
			com.setComTzw(comTzw);
			com.setComSjw(comSjw);
			com.setComLkz(comLkz);
			com.setComMode(comMode);
			com.setComPort(comPort);
			com.setHostId(hostId);
			List<Com> list = comServer.findComSelective(com);
			return new RespEntry("查询成功", list);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}
	@RequestMapping("toSave")
	public ModelAndView toSave(Integer hostId) {
		try {
			Com com = new Com();
			com.setHostId(hostId);
			List<Com> list = comServer.findComSelective(com);
			ModelAndView modelAndView = new ModelAndView("com/comManager");
			modelAndView.addObject(Constant.FLAG_PAGE, Constant.FLAG_PAGE_UPDATE);
			modelAndView.addObject("com", list.get(0));
			return modelAndView;
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			throw null;
		}
	}

	@RequestMapping("toUpdate")
	public ModelAndView toUpdate(Integer hostId) {
		try {
			ModelAndView modelAndView = new ModelAndView("com/comManager");
			modelAndView.addObject(Constant.FLAG_PAGE, Constant.FLAG_PAGE_ADD);
			Com com = new Com();
			com.setHostId(hostId);
			modelAndView.addObject("com", com);
			return modelAndView;
		} catch (Exception e) {
			log.info("查询异常：{}", new RespEntry("查询异常", 100, e.getMessage()).toString());
			throw null;
		}
	}

	@ResponseBody
	@RequestMapping("save")
	public Object insertSelective(Integer hostId,String comName, String comBautrate, String comEnable,
			Integer comState, String comJyw, String comTzw, String comSjw, String comLkz, Integer comMode, Integer comPort,Integer comCollectiontime) {
		try {
			Com com = new Com();
			com.setHostId(hostId);
			com.setComName(comName);
			com.setComBautrate(comBautrate);
			com.setComEnable(comEnable);
			com.setComState(comState);
			com.setComJyw(comJyw);
			com.setComTzw(comTzw);
			com.setComSjw(comSjw);
			com.setComLkz(comLkz);
			com.setComMode(comMode);
			com.setComPort(comPort);
			com.setComCollectiontime(comCollectiontime);
			boolean b = comServer.insertSelective(com);
			if (b) {
				return new RespEntry("添加 成功");
			} else {
				return new RespEntry("添加失败", 101);
			}
		} catch (Exception e) {
			return new RespEntry("异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("update")
	public Object updateSelective(Integer comId, String comName, String comBautrate, String comEnable,
			Integer comState, String comJyw, String comTzw, String comSjw, String comLkz, Integer comMode, Integer comPort,Integer comCollectiontime) {
		try {
			Com com = new Com();
			com.setComId(comId);
			com.setComName(comName);
			com.setComBautrate(comBautrate);
			com.setComEnable(comEnable);
			com.setComState(comState);
			com.setComJyw(comJyw);
			com.setComTzw(comTzw);
			com.setComSjw(comSjw);
			com.setComLkz(comLkz);
			com.setComMode(comMode);
			com.setComPort(comPort);
			com.setComCollectiontime(comCollectiontime);
			L.e(com);
			boolean b = comServer.updateSelective(com);
			if (b) {
				return new RespEntry("修改 成功");
			} else {
				return new RespEntry("修改失败", 101);
			}
		} catch (Exception e) {
			return new RespEntry("修改异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("deleteById")
	public Object deleteByPrimaryKey(Integer comId) {
		try {
			boolean b = comServer.deleteByPrimaryKey(comId);
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
	public Object findByPrimaryKey(Integer comId) {
		try {
			Com com = comServer.findByPrimaryKey(comId);
			return new RespEntry("查询 成功", com);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("findSelectivePage")
	public Object findComSelectivePage(String comName, String comBautrate, String comEnable, Integer comState,
			String comJyw, String comTzw, String comSjw, String comLkz, Integer comMode, Integer comPort, Integer pageSize, Integer pageNo) {
		try {
			Com com = new Com();
			com.setComName(comName);
			com.setComBautrate(comBautrate);
			com.setComEnable(comEnable);
			com.setComState(comState);
			com.setComJyw(comJyw);
			com.setComTzw(comTzw);
			com.setComSjw(comSjw);
			com.setComLkz(comLkz);
			com.setComMode(comMode);
			com.setComPort(comPort);
			List<Com> list = comServer.findComSelectivePage(com, pageSize, pageNo);
			return new RespEntry("查询成功", list);
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}

	@ResponseBody
	@RequestMapping("findByHostId")
	public Object findByHostAddress(Integer hostId) {
		try {
			Com com = new Com();
			com.setHostId(hostId);
			List<Com> list = comServer.findComSelective(com);
			return new RespEntry("查询成功", ValidateKit.isEmpty(list) ? null : list.get(0));
		} catch (Exception e) {
			return new RespEntry("查询异常", 100, e.getMessage());
		}
	}

}
