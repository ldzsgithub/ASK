package com.jy23.serverImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jy23.entity.Com;
import com.jy23.mapper.ComMapper;
import com.jy23.server.ComServer;

@Service("comServer")
public class ComServerImpl implements ComServer{
	@Autowired
	private ComMapper comMapper;

	@Override
	public boolean insertSelective(Com com){
		int num = comMapper.insertSelective(com);
		return num > 0;
	}

	@Override
	public boolean updateSelective(Com com){
		int num = comMapper.updateSelective(com);
		return num > 0;
	}

	@Override
	public boolean deleteByPrimaryKey(Integer comId){
		int num = comMapper.deleteByPrimaryKey(comId);
		return num > 0;
	}

	@Override
	public Com findByPrimaryKey(Integer comId){
		Com com = comMapper.findByPrimaryKey(comId);
		return com;
	}

	@Override
	public List<Com> findComSelective( Com com ){ 
		List<Com> list = comMapper.findSelective(com);
		return list == null ? new ArrayList<Com>() : list;
	}
	@Override
	public List<Com> findComSelectivePage( Com com ,Integer pageSize ,Integer pageNo){
		PageHelper.startPage(pageNo, pageSize);
		List<Com> list = comMapper.findSelective(com);
		return list == null ? new ArrayList<Com>() : list;
	}

}
