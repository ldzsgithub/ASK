package com.jy23.serverImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.jy23.entity.Probe;
import com.jy23.mapper.DepartmentMapper;
import com.jy23.mapper.ProbeMapper;
import com.jy23.server.ProbeServer;

@Service("probeServer")
public class ProbeServerImpl implements ProbeServer {
    @Autowired
    private ProbeMapper probeMapper;

    @Override
    @Transactional
    public void insertSelective(Probe probe) {
        probeMapper.insertSelective(probe);
    }

    @Override
    public void updateSelective(Probe probe) {
        probeMapper.updateSelective(probe);
    }

    @Override
    public boolean deleteByPrimaryKey(Integer probeId) {
        int num = probeMapper.deleteByPrimaryKey(probeId);
        return num > 0;
    }

    @Override
    public Probe findByPrimaryKey(Integer probeId) {
        return probeMapper.findByPrimaryKey(probeId);
    }

    @Override
    public List<Probe> findProbeSelective(Probe probe) {
        List<Probe> list = probeMapper.findProbeSelective(probe);
        return list == null ? new ArrayList<Probe>() : list;
    }

    @Override
    public List<Probe> selectRealTimeAll() {
        List<Probe> list = probeMapper.selectRealTimeAll();
        return list == null ? new ArrayList<Probe>() : list;
    }

    @Override
    public Integer countAlarm() {
        Integer num = probeMapper.countAlarm();
        return num == null ? 0 : num;
    }

    @Override
    public List<Probe> findSelectivePage(Integer pageSize, Integer pageNo, Probe probe) {
        PageHelper.startPage(pageNo, pageSize);
        List<Probe> list = findProbeSelective(probe);
        return list;
    }

    @Override
    public int count(Probe probe) {
        Integer num = probeMapper.count(probe);
        return num == null ? 0 : num;
    }

    @Override
    public List<Probe> selectProbeByHIdAndPid(Integer hostId, Integer departmentId) {
        List<Probe> list = null;
        if (departmentId == null || departmentId.intValue() == -1) {
            list = probeMapper.findProbeSelective(null);
        } else {
            if (hostId == null || hostId.intValue() == -1) {//主机-全部
                list = probeMapper.selectProbeByDid(departmentId);
            } else {
                Probe queryProbe = new Probe();
                queryProbe.setHostId(hostId);
                list = probeMapper.findProbeSelective(queryProbe);
            }
        }
        return list;
    }
    @Autowired
    private DepartmentMapper departmentMapper;
    @Override
    public List<Probe> selectByArea(Integer hostId, Integer departmentId) {
    	List<Probe> list = null;
    	if (departmentId == null || departmentId.intValue() == -1) {
    		list = probeMapper.findProbeSelective(null);
    	} else {
    		if (hostId == null || hostId.intValue() == -1) {//主机-全部
    			list = probeMapper.selectProbeByDid(departmentId);
    		} else {
    			int a = departmentMapper.countArea(departmentId);
    			if (a > 0) {
    				list = probeMapper.findByArea(hostId);
    			} else {
    				Probe queryProbe = new Probe();
    				queryProbe.setHostId(hostId);
    				list = probeMapper.findProbeSelective(queryProbe);
    			}
    		}
    	}
    	return list;
    }

    @Override
    public List<Probe> realTimeAlarmChar(Integer departmentId) {
        return probeMapper.realTimeAlarmChar(departmentId);
    }

    @Override
    public boolean haveAlarm() {
        PageHelper.startPage(1, 1);
        Integer b = probeMapper.haveAlarm();
        return b == null ? false : true;
    }

    @Override
    public List<Probe> findProbeByHostId(Integer hostId) {
        return probeMapper.findProbeByHostId(hostId);
    }

	@Override
	public int countProbeByQx(Integer departmentId,String probeName) {
		return probeMapper.countProbeByQx(departmentId,probeName);
	}

	@Override
	public List<Probe> selectProbeByQx(Integer pageSize, Integer pageNo, int[] qx, String probeName) {
		PageHelper.startPage(pageNo, pageSize);
		return probeMapper.selectProbeByQx(qx,probeName);
	}
}