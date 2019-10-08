package com.jy23.serverImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jy23.entity.HomePageP2;
import com.jy23.mapper.HomePageMapper;
import com.jy23.server.HomePageServer;

@Service("homePageServer")
public class HomePageSererImpl implements HomePageServer{
	@Autowired
	private HomePageMapper homePageMapper;
	
	@Override
	public int countByProbeType(int probeType) {
		return homePageMapper.countByProbeType(probeType);
	}

	@Override
	public List<String> selectDeptName() {
		List<String> list = homePageMapper.selectDeptName();
		return list==null?new ArrayList<String>():list;
	}

	@Override
	public int countAlarmProbe() {
		return homePageMapper.countAlarmProbe();
	}

	@Override
	public int countTolalProbe() {
		return homePageMapper.countTolalProbe();
	}

	@Override
	public List<HomePageP2> selectDeptData() {
		List<HomePageP2>list = homePageMapper.selectDeptData();
		return list==null?new ArrayList<HomePageP2>():list;
	}

	@Override
	public int selectTrouble(int sDay) {
		return homePageMapper.selectTrouble(sDay);
	}
	
}
