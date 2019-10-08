package com.jy23.server;

import java.util.List;

import com.jy23.entity.ProbeCheck;

public interface ProbeCheckServer {

	List<ProbeCheck> findAllProbeCheck(ProbeCheck probeCheck);

	void insertProbeCheck(ProbeCheck probeCheck);

	void updateProbeCheck(ProbeCheck probeCheck);

	void deleteByHostId(Integer probeId);

}
