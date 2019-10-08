package com.jy23.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jy23.entity.LineChart;
import com.jy23.entity.Probe;

public interface LineChartMapper {
	List<LineChart> findLineChart(@Param("tableName")String tableName,
								@Param("hostId")Integer hostId,
								@Param("sDay")String sDay);

	List<Probe> findProbeName(Integer hostId);
}
