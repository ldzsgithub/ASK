package com.jy23.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RealTimeAlarmData {
	private String departmentName;
	private String hostName;
	private String probeName;
	private Double lowValue;
	private Double highValue;
	private Double realtimeValue;

}
