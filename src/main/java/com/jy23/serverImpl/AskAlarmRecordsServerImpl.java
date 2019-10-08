package com.jy23.serverImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.jy23.entity.AskAlarmRecords;
import com.jy23.mapper.AskAlarmRecordsMapper;
import com.jy23.server.AskAlarmRecordsServer;

import java.util.ArrayList;
import java.util.List;

@Service("askAlarmRecordsServer")
public class AskAlarmRecordsServerImpl implements AskAlarmRecordsServer {
	@Autowired
	private AskAlarmRecordsMapper askAlarmRecordsMapper;

	@Override
	public boolean insertSelective(AskAlarmRecords askAlarmRecords){
		int num = askAlarmRecordsMapper.insertSelective(askAlarmRecords);
		return num > 0;
	}

	@Override
	public boolean updateSelective(AskAlarmRecords askAlarmRecords){
		int num = askAlarmRecordsMapper.updateSelective(askAlarmRecords);
		return num > 0;
	}

	@Override
	public boolean deleteByPrimaryKey(Long xh){
		int num = askAlarmRecordsMapper.deleteByPrimaryKey(xh);
		return num > 0;
	}

	@Override
	public AskAlarmRecords findByPrimaryKey(Long xh){
		AskAlarmRecords askAlarmRecords = askAlarmRecordsMapper.findByPrimaryKey(xh);
		return askAlarmRecords;
	}

	@Override
	public List<AskAlarmRecords> findSelective(AskAlarmRecords askAlarmRecords ){
		List<AskAlarmRecords> list = askAlarmRecordsMapper.findSelective(askAlarmRecords);
		return list == null ? new ArrayList<AskAlarmRecords>() : list;
	}

	@Override
	public List<AskAlarmRecords> findSelectivePage(AskAlarmRecords askAlarmRecords ,Integer pageSize ,Integer pageNo){
		PageHelper.startPage(pageNo, pageSize);
		List<AskAlarmRecords> list = askAlarmRecordsMapper.findSelective(askAlarmRecords);
		return list == null ? new ArrayList<AskAlarmRecords>() : list;
	}

	@Override
	public int count( AskAlarmRecords askAlarmRecords){
		Integer count = askAlarmRecordsMapper.count(askAlarmRecords);
		return count == null ? 0 : count.intValue();
	}

	@Override
	public List<AskAlarmRecords> findSelectiveByTime(AskAlarmRecords askAlarmRecords, String startTime, String endTime,Integer startAlarmValue,
			Integer endAlarmValue) {
		List<AskAlarmRecords> list = askAlarmRecordsMapper.findSelectiveByTime(askAlarmRecords.getDepartmentId(),askAlarmRecords.getHostId(),
				askAlarmRecords.getProbeBh(), startTime, endTime, startAlarmValue, endAlarmValue);
		return list == null ? new ArrayList<AskAlarmRecords>() : list;
	}

}
