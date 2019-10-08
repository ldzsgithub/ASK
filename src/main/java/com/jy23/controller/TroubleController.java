package com.jy23.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jy23.entity.RespEntry;
import com.jy23.entity.Trouble;
import com.jy23.server.TroubleServer;
import com.jy23.server.UsersServer;

@Controller
@RequestMapping("trouble")
public class TroubleController {
	@Autowired
	private TroubleServer troubleServer;
	@Autowired
	private UsersServer usersServer;
	/*
	 * 全部问题
	 * troubleTitle为查询关键字
	 */
	@RequestMapping("toAllTrouble")
	public ModelAndView toAllTrouble(String troubleTitle) {
		ModelAndView modelAndView = new ModelAndView("trouble/troubleList");
		Trouble trouble =new Trouble();
		trouble.setTroubleTitle(troubleTitle);
		trouble.setStatus(0);
		List<Trouble>troubles=troubleServer.findAllTrouble(trouble);
		for(int i=0;i<troubles.size();i++) {
			troubles.get(i).setMajorUser(usersServer.findUserById(troubles.get(i).getMajor()));
			troubles.get(i).setSubmitterUser(usersServer.findUserById(troubles.get(i).getSubmitter()));
		}
		modelAndView.addObject("trouble", troubles);
		modelAndView.addObject("troubleTitle", troubleTitle);
		return modelAndView;
	}
	/*
	 * 我的问题
	 */
	@RequestMapping("toMyTrouble")
	public ModelAndView toMyTrouble(String id) {
		ModelAndView modelAndView = new ModelAndView("trouble/myTroubleList");
		if(id==null) return modelAndView;
		Trouble trouble =new Trouble();
		trouble.setStatus(0);
		trouble.setMajor(id);
		List<Trouble>troubles=troubleServer.findAllTrouble(trouble);
		for(int i=0;i<troubles.size();i++) {
			troubles.get(i).setSubmitterUser(usersServer.findUserById(troubles.get(i).getSubmitter()));
		}
		modelAndView.addObject("trouble", troubles);
		modelAndView.addObject("id", id);
		return modelAndView;
	}
	/*
	 * 我的提交
	 */
	@RequestMapping("toMySubmit")
	public ModelAndView toMySubmit(String id) {
		ModelAndView modelAndView = new ModelAndView("trouble/mySubmitList");
		if(id==null) return modelAndView;
		Trouble trouble =new Trouble();
		trouble.setStatus(0);
		trouble.setSubmitter(id);
		List<Trouble>troubles=troubleServer.findAllTrouble(trouble);
		for(int i=0;i<troubles.size();i++) {
			troubles.get(i).setMajorUser(usersServer.findUserById(troubles.get(i).getMajor()));
		}
		modelAndView.addObject("trouble", troubles);
		modelAndView.addObject("id", id);
		return modelAndView;
	}
	/*
	 * 新增trouble页面
	 */
	@RequestMapping("/toTroubleAdd")
	public ModelAndView toTroubleAdd(String troubleTitle,String troubleContent,Integer major,Integer submitter,Integer term,Integer troubleLevel) {
		ModelAndView modelAndView = new ModelAndView("trouble/troubleManageAdd");
		modelAndView.addObject("user", usersServer.findAllUsers(null));
		Trouble trouble=new Trouble();
		trouble.setTroubleLevel(1);
		modelAndView.addObject("trouble",trouble);
		return modelAndView;
	}
	/*
	 * 查看页面
	 * flag 0查看1新增2修改3提交
	 */
	@RequestMapping("/pageJump")
	public ModelAndView pageJump(Integer id,String flag) {
		ModelAndView modelAndView = new ModelAndView("trouble/troubleManage");
		modelAndView.addObject("flag", flag);
		modelAndView.addObject("user", usersServer.findAllUsers(null));
		Trouble trouble=new Trouble();
		trouble=troubleServer.findTroubleById(id);
		trouble.setMajorUser(usersServer.findUserById(trouble.getMajor()));
		trouble.setSubmitterUser(usersServer.findUserById(trouble.getSubmitter()));
		modelAndView.addObject("trouble", trouble);
		return modelAndView;
	}
	/*
	 * 新增trouble
	 */
	@RequestMapping("/troubleAdd")
	@ResponseBody
	public RespEntry troubleAdd(String troubleTitle,String troubleContent,String major,String submitter,Integer term,Integer troubleLevel) {
		try {
			Trouble trouble=new Trouble();
			trouble.setTroubleTitle(troubleTitle);
			trouble.setTroubleContent(troubleContent);
			trouble.setTroubleLevel(troubleLevel);
			trouble.setStatus(0);
			trouble.setMajor(major);
			trouble.setSubmitter(submitter);
			Calendar now = Calendar.getInstance(); 
			trouble.setCreatTime(now.getTime());
			now.add(Calendar.DAY_OF_MONTH, term);
			trouble.setTermTime(now.getTime());
			troubleServer.insertTrouble(trouble);
			return new RespEntry("提交成功");
		} catch (Exception e) {
			return new RespEntry("失败",1);
		}
	}
	/*
	 * 修改
	 */
	@RequestMapping("/update")
	@ResponseBody
	public RespEntry update(Integer id,String troubleTitle,String troubleContent,String major,String termTime,Integer troubleLevel) {
		try {
			Trouble trouble=new Trouble();
			trouble.setId(id);
			trouble.setTroubleTitle(troubleTitle);
			trouble.setTroubleContent(troubleContent);
			trouble.setTroubleLevel(troubleLevel);
			trouble.setMajor(major);
			trouble.setTermTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(termTime));
			troubleServer.updateTrouble(trouble);
			return new RespEntry("提交成功");
		} catch (Exception e) {
			return new RespEntry("失败",1);
		}
	}
	/*
	 * 解决问题
	 */
	@RequestMapping("/solve")
	@ResponseBody
	public RespEntry solve(Integer id,String troubleProcess,Integer flag,
			String troubleTitle,String troubleContent,String major,String newMajor,Integer troubleLevel) {
		try {
			Trouble trouble=new Trouble();
			trouble.setId(id);
			trouble.setTroubleProcess(troubleProcess);
			trouble.setSolveTime(new Date());
			trouble.setStatus(-1);
			if(flag==32) {
				Trouble newTrouble=new Trouble();
				newTrouble.setStatus(0);
				newTrouble.setTroubleTitle(troubleTitle);
				newTrouble.setTroubleContent(troubleContent+"\n- - - - - - - - - - - - - - - - - - - - - - - - - -\n"+troubleProcess);
				newTrouble.setTroubleLevel(troubleLevel);
				newTrouble.setSubmitter(major);
				newTrouble.setMajor(newMajor);
				newTrouble.setCreatTime(new Date());
				newTrouble.setTermTime(newTrouble.getCreatTime());
				troubleServer.insertTrouble(newTrouble);
				trouble.setStatus(newTrouble.getId());
			}
			troubleServer.updateTrouble(trouble);
			return new RespEntry("提交成功");
		} catch (Exception e) {
			return new RespEntry("失败",1);
		}
	}
	/*
	 * 历史问题
	 */
	@RequestMapping("/toHistoryTrouble")
	public ModelAndView toHistoryTrouble(String troubleTitle) {
		ModelAndView modelAndView = new ModelAndView("trouble/troubleHistory");
		Trouble trouble =new Trouble();
		trouble.setTroubleTitle(troubleTitle);
		trouble.setStatus(-1);
		List<Trouble>troubles=troubleServer.findAllTrouble(trouble);
		for(int i=0;i<troubles.size();i++) {
			troubles.get(i).setMajorUser(usersServer.findUserById(troubles.get(i).getMajor()));
			troubles.get(i).setSubmitterUser(usersServer.findUserById(troubles.get(i).getSubmitter()));
		}
		modelAndView.addObject("trouble", troubles);
		modelAndView.addObject("troubleTitle", troubleTitle);
		return modelAndView;
	}
	/*
	 * 问题数据
	 */
	@RequestMapping("/toTroubleData")
	public ModelAndView toTroubleData(String troubleTitle) {
		ModelAndView modelAndView = new ModelAndView("trouble/troubleData");
		modelAndView.addObject("sTime", new Date());
		modelAndView.addObject("users", usersServer.findAllUsers(null));
		return modelAndView;
	}
	/*
	 * 问题数据 查询
	 * 遗留未解决，遗留已解决，遗留交付上级，已解决，未解决，交付上级
	 */
	@RequestMapping("/queryTroubleData")
	@ResponseBody
	public RespEntry queryTroubleData(Integer id,String sTime) {
		try {
			String nTime=null;
			if(sTime!=null) {
				int mon=Integer.parseInt(sTime.substring(5));
				String year=sTime.substring(0,4);
				if(mon==12) {
					year=""+(Integer.parseInt(year)+1);
					nTime=year+"-01";
				} else {
					mon+=1;
					nTime=year+"-"+("0"+mon).substring(("0"+mon).length()-2);
				}
				sTime+="-01 00:00:00";
				nTime+="-01 00:00:00";
			}
			if(id==-1) id=null;
			int[]data=new int[6];
			data[0]=troubleServer.counta(id,sTime,nTime);
			data[1]=troubleServer.countb(id,sTime,nTime);
			data[2]=troubleServer.countc(id,sTime,nTime);
			data[3]=troubleServer.countd(id,sTime,nTime);
			data[4]=troubleServer.counte(id,sTime,nTime);
			data[5]=troubleServer.countf(id,sTime,nTime);
			return new RespEntry("成功",data);
		} catch (Exception e) {
			return new RespEntry("查询失败",1);
		}
	}
}
