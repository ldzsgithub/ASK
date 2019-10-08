package com.jy23.mapper ;

import com.jy23.entity.ProbeCheck;

import java.util.List;

public interface ProbeCheckMapper {

	List<ProbeCheck> findAllProbeCheck(ProbeCheck probeCheck);

	void insertProbeCheck(ProbeCheck probeCheck);

	void updateProbeCheck(ProbeCheck probeCheck);

	void deleteByHostId(Integer probeId);

}
