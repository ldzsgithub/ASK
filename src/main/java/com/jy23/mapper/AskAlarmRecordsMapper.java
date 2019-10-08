package com.jy23.mapper;

import java.lang.Long;
import java.lang.String;
import java.lang.Integer;

import org.apache.ibatis.annotations.Param;

import com.jy23.entity.AskAlarmRecords;

import java.util.List;

public interface AskAlarmRecordsMapper {

    int insertSelective(AskAlarmRecords askAlarmRecords);

    int updateSelective(AskAlarmRecords askAlarmRecords);

    int deleteByPrimaryKey(@Param("xh") Long xh);

    AskAlarmRecords findByPrimaryKey(@Param("xh") Long xh);

    List<AskAlarmRecords> findSelective(AskAlarmRecords askAlarmRecords);

    int count(AskAlarmRecords askAlarmRecords);

    List<AskAlarmRecords> findSelectiveByTime(
            @Param("departmentId") Integer departmentId,
            @Param("hostId") Integer hostId,
            @Param("probeBh") Integer probeBh,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("startAlarmValue") Integer startAlarmValue,
            @Param("endAlarmValue") Integer endAlarmValue
    );

}
