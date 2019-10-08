package com.jy23.server;

import java.util.List;

import com.jy23.entity.Host;
import com.jy23.entity.HostOrder;
import com.jy23.exception.MsgException;

public interface HostServer {

	int count(Host host);

	List<Host> findHostSelectivePage(Host host, Integer pageSize, Integer pageNo);

	Host findByPrimaryKey(Integer id);

	boolean insertSelective(Host host) throws MsgException;

	boolean updateSelective(Host host) throws MsgException;
	
	boolean deleteByPrimaryKey(Integer hostId ) throws MsgException;

	List<Host> findHostSelective(Host host);

	List<Host> selectHostByQx(Integer pageSize, Integer pageNo, int[] qx,String hostName);

	void updateHostOrder(HostOrder hostOrder);

	void deleteHostOrder(Integer hostId);

	void addRegisterNumber(Integer hostId);
	
	void reduceRegisterNumber(Integer probeId);

}
