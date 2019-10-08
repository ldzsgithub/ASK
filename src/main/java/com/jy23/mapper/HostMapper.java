package com.jy23.mapper;

import java.util.List;

import com.jy23.entity.Host;

public interface HostMapper {
	Integer count(Host host);

	List<Host> findHostSelective(Host host);

	Host findByPrimaryKey(Integer id);

	void insertSelective(Host host);

	void updateSelective(Host host);

	int deleteByPrimaryKey(Integer hostId);

	List<Host> selectHostByQx(int[] qx,String hostName);
}
