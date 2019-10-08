package com.jy23.mapper;

import com.jy23.entity.HostOrder;

public interface HostOrderMapper {

	int insertHostOrder(HostOrder hostOrder);

	void updateHostOrder(HostOrder hostOrder);

	void deleteHostOrder(Integer hostId);
	
	void addRegisterNumber(Integer hostId);

	void reduceRegisterNumber(Integer probeId);

}
