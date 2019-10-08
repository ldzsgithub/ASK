package com.jy23.mapper;

import java.lang.Integer;

import org.apache.ibatis.annotations.Param;

import com.jy23.entity.Probe;

import java.util.List;

public interface ProbeMapper {
    void insertSelective(Probe probe);

    void updateSelective(Probe probe);

    int deleteByPrimaryKey(Integer probeId);

    Probe findByPrimaryKey(@Param("probeId") Integer probeId);

    List<Probe> findProbeSelective(Probe probe);

    List<Probe> selectRealTimeAll();

    Integer countAlarm();

    Integer count(Probe probe);

    List<Probe> findProbeByHostId(@Param("hostId") Integer hostId);

    List<Probe> selectProbeByDid(@Param("department_id") Integer departmentId);

    List<Probe> realTimeAlarmChar(@Param("department_id") Integer departmentId);

    Integer haveAlarm();

	int countProbeByQx(Integer departmentId,String probeName);
	
	List<Probe> selectProbeByQx(int[] qx, String probeName);

	List<Probe> findByArea(@Param("hostId") Integer hostId);
}
