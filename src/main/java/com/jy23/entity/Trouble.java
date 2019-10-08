package com.jy23.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Trouble {
	private Integer id;
	private String troubleTitle;
	private String troubleContent;
	private String major;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date creatTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date solveTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date termTime;				//期限
	private Integer status;				//-1处理完毕 0处理中 >0流转id
	private Integer troubleLevel;		//1普通 2紧急 3非常紧急
	private String submitter;			//问题提交人
	private String troubleProcess;
	
	private Users majorUser;
	private Users submitterUser;
}
