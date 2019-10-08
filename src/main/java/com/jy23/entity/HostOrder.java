package com.jy23.entity;

import lombok.Data;

@Data
public class HostOrder {
	private Integer orderId;
	private Integer hostAddress;
	private Integer functionCode;
	private Integer registerHeader;
	private Integer registerNumber;
	private String registerCard;
	
	private Integer hostId;
}
