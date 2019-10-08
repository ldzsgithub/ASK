package com.jy23.server ;


import java.lang.String;
import java.lang.Integer;

import com.jy23.entity.AskAlarmRecords;

import java.util.List;

/**
 * 
 */
public interface AskAlarmRecordsServer {
	boolean insertSelective(AskAlarmRecords askAlarmRecords);

	boolean updateSelective(AskAlarmRecords askAlarmRecords);

	boolean deleteByPrimaryKey(java.lang.Long xh);

	AskAlarmRecords findByPrimaryKey(java.lang.Long xh);

	List<AskAlarmRecords> findSelective(AskAlarmRecords askAlarmRecords);

	List<AskAlarmRecords> findSelectivePage(AskAlarmRecords askAlarmRecords ,Integer pageSize ,Integer pageNo);

	int count(AskAlarmRecords askAlarmRecords);

    List<AskAlarmRecords> findSelectiveByTime(AskAlarmRecords askAlarm, String startTime, String endTime,Integer startAlarmValue,Integer endAlarmValue);

}
