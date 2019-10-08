package com.jy23.serverImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jy23.entity.ProbeCheck;
import com.jy23.mapper.ProbeCheckMapper;
import com.jy23.server.ProbeCheckServer;

@Service("probeCheckServer")
public class ProbeCheckServerImpl implements ProbeCheckServer{
	@Autowired
	private ProbeCheckMapper probeCheckMapper;
	@Override
	public List<ProbeCheck> findAllProbeCheck(ProbeCheck probeCheck) {
		return probeCheckMapper.findAllProbeCheck(probeCheck);
	}
	@Override
	public void insertProbeCheck(ProbeCheck probeCheck) {
		probeCheckMapper.insertProbeCheck(probeCheck);
	}
	@Override
	public void updateProbeCheck(ProbeCheck probeCheck) {
		probeCheckMapper.updateProbeCheck(probeCheck);
	}
	@Override
	public void deleteByHostId(Integer probeId) {
		probeCheckMapper.deleteByHostId(probeId);
	}

}
