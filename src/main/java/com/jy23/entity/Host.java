package com.jy23.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Host {
	private Integer hostId;			//主机id
	private Integer hostAddress;	//主机地址
	private String hostName;		//主机名称
	private String hostPosition;	//主机位置
	private String hostRemarks;		//备注
	private Integer hostEnable;		//启用状态
	private Integer companyId;		//公司id
	private Integer departmentId;	//部门id
	private Integer transStatus;		//主机通讯状态
	private Integer numberChannels;		//通道数量
	private String registerCard;		//物联网号码
	private Integer hostCollectiontime;		//采集间隔
	//Alarm_Status
	//Card_Socket
	private Integer orderId;
}
