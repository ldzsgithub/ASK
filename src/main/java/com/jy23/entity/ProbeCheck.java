package com.jy23.entity ;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.lang.Integer;
import lombok.Data;

@Data
public class ProbeCheck {
	private Integer id;			//
	private Integer probeId;	//
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date creatDate;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date checkDate;		//
	
	private Users user;
	
	private String probeName;
	private String probePosition;
	private String probeManager;
	
	private Integer hostId;
	private Integer departmentId;
}
