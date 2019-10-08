package com.jy23.serverImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jy23.entity.LineChart;
import com.jy23.entity.Probe;
import com.jy23.mapper.LineChartMapper;
import com.jy23.server.LineChartServer;

@Service("lineChartServer")
public class LineChartServerImpl implements LineChartServer{
	@Autowired
	private LineChartMapper lineChartMapper;

	@Override
	public List<LineChart> findLineChart(String tableName, Integer hostId, String sDay) {
		List<LineChart>list=lineChartMapper.findLineChart(tableName, hostId, sDay);
		return list==null?new ArrayList<LineChart>():list;
	}

	@Override
	public List<Probe> findProbeName(Integer hostId) {
		List<Probe>list=lineChartMapper.findProbeName(hostId);
		return list==null?new ArrayList<Probe>():list;
	}

}
