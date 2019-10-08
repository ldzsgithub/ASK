package com.jy23.serverImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jy23.entity.Trouble;
import com.jy23.mapper.TroubleMapper;
import com.jy23.server.TroubleServer;

@Service("troubleServer")
public class TroubleServerImpl implements TroubleServer{
	@Autowired
	private TroubleMapper troubleMapper;
	
	@Override
	public List<Trouble> findAllTrouble(Trouble trouble) {
		return troubleMapper.findAllTrouble(trouble);
	}

	@Override
	public Trouble findTroubleById(Integer id) {
		return troubleMapper.findTroubleById(id);
	}

	@Override
	public int insertTrouble(Trouble trouble) {
		return troubleMapper.insertTrouble(trouble);
	}

	@Override
	public void updateTrouble(Trouble trouble) {
		troubleMapper.updateTrouble(trouble);
	}

	@Override
	public int counta(Integer id, String sTime, String nTime) {
		return troubleMapper.counta(id,sTime,nTime);
	}

	@Override
	public int countb(Integer id, String sTime, String nTime) {
		return troubleMapper.countb(id,sTime,nTime);
	}

	@Override
	public int countc(Integer id, String sTime, String nTime) {
		return troubleMapper.countc(id,sTime,nTime);
	}

	@Override
	public int countd(Integer id, String sTime, String nTime) {
		return troubleMapper.countd(id,sTime,nTime);
	}

	@Override
	public int counte(Integer id, String sTime, String nTime) {
		return troubleMapper.counte(id,sTime,nTime);
	}

	@Override
	public int countf(Integer id, String sTime, String nTime) {
		return troubleMapper.countf(id,sTime,nTime);
	}

}
