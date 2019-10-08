package com.jy23.serverImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jy23.mapper.BaseMapper;
import com.jy23.server.BaseServer;

@Service("baseServer")
public class BaseServerImpl implements BaseServer{
	@Autowired
	private BaseMapper baseMapper;
	@Override
	public void creatASKHis(String tableName) {
		baseMapper.creatASKHis(tableName);
	}

}
