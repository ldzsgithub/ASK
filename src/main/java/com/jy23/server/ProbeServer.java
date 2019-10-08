package com.jy23.server;

import java.util.List;

import com.jy23.entity.Probe;

public interface ProbeServer {
	void insertSelective(Probe probe);

	void updateSelective(Probe probe);

	boolean deleteByPrimaryKey(Integer probeId );

	Probe findByPrimaryKey(Integer probeId );

	List<Probe> findProbeSelective(Probe probe);

    List<Probe> selectRealTimeAll();

	Integer countAlarm();

	List<Probe>  findSelectivePage(Integer pageSize, Integer pageNo, Probe probe);

	int count(Probe probe);

    List<Probe> findProbeByHostId(Integer hostId);

    List<Probe> selectProbeByHIdAndPid(Integer hostId, Integer departmentId);
    
    List<Probe> selectByArea(Integer hostId, Integer departmentId);

	List<Probe> realTimeAlarmChar(Integer departmentId);

    boolean haveAlarm();

	int countProbeByQx(Integer departmentId,String probeName);

	List<Probe> selectProbeByQx(Integer pageSize, Integer pageNo, int[] qx, String probeName);
}
