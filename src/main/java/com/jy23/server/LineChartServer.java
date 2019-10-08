package com.jy23.server;

import java.util.List;

import com.jy23.entity.LineChart;
import com.jy23.entity.Probe;

public interface LineChartServer {
	List<LineChart> findLineChart(String tableName,Integer hostId,String sDay);

	List<Probe> findProbeName(Integer hostId);
}
