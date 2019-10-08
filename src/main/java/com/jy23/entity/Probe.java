package com.jy23.entity ;

import java.lang.String;
import java.lang.Integer;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Probe {
	
	private Integer probeId;		//
	private Integer probeBh;		//
	private String probeName;		//
	private String probePosition;	//
	private String probeRemarks;	//
	private Integer probeType;		//一氧化碳0 氧气1 可燃气2
	private Integer probeEnable;	//
	private Integer hostAddress;
	private Integer hostId;			//
	private String hostName;		//实时数据分组展示
	private Double lowValue;		//
	private Double highValue;		//
	private Double realtimeValue;	//
	private String realtimeState;	//
	private String lat;				//
	private String lng;				//
	private String customLat;
	private String customLng;
	
	private Double bjzt;			//
	private String bjztMc;			//
	private Double bjztOld;			//
	private Integer sfbj;			//
	private Integer cxsj;
	
	private String department_name;	//
	private String host_name;		//
	
	private String probeManager;
	private Users users;
}
