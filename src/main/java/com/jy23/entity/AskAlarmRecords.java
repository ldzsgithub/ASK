package com.jy23.entity ;

import java.lang.Double;
import java.util.Date;
import java.lang.String;
import java.lang.Integer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class AskAlarmRecords implements Serializable{
	private static final long serialVersionUID = 1L;
	
    private Integer probeBh ;//;
    private String probeName ;//;
    private Double alarmValue ;//;
    private String alarmMc ;//;
    private Integer hostId ;//;
    private String hostName ;//;
    private Integer departmentId ;//;
    private String departmentName ;//;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date alarmTime ;//;
}
