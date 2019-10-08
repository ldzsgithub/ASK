package com.jy23.serverImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.jy23.entity.Com;
import com.jy23.entity.Host;
import com.jy23.entity.HostOrder;
import com.jy23.entity.Probe;
import com.jy23.exception.MsgException;
import com.jy23.mapper.ComMapper;
import com.jy23.mapper.HistoryMapper;
import com.jy23.mapper.HostMapper;
import com.jy23.mapper.HostOrderMapper;
import com.jy23.mapper.ProbeMapper;
import com.jy23.server.HostServer;
import com.jy23.util.ResultStatusCode;
import com.jy23.util.ValidateKit;

@Service("hostServer")
public class HostServerImpl implements HostServer{
	@Autowired
	private HostMapper hostMapper;
	@Autowired
	private ComMapper comMapper;
	@Autowired
	private ProbeMapper probeMapper;

	@Override
	public int count(Host host) {
		Integer count = hostMapper.count(host);
		return count == null ? 0:count;
	}

	@Override
	public List<Host> findHostSelectivePage(Host host, Integer pageSize, Integer pageNo) {
		PageHelper.startPage(pageNo, pageSize);
		List<Host> list = hostMapper.findHostSelective(host);
		return list;
	}

	@Override
	public Host findByPrimaryKey(Integer id) {
		return hostMapper.findByPrimaryKey(id);
	}
	
	@Autowired
	private HostOrderMapper hostOrderMapper;
	
	@Autowired
	private HistoryMapper historyMapper;
	
	@Override
	public boolean insertSelective(Host host) throws MsgException {
		Host hostQuery = new Host();
		hostQuery.setHostAddress(host.getHostAddress());
		List<Host> list = hostMapper.findHostSelective(hostQuery);
		if (list != null && list.size() > 0) {
			throw new MsgException(ResultStatusCode.ERROR_HOST_ADDRESS_EXISTS);
		}
		HostOrder hostOrder = new HostOrder();
		hostOrder.setHostAddress(host.getHostAddress());
		hostOrder.setFunctionCode(3);
		hostOrder.setRegisterHeader(0);
		hostOrder.setRegisterNumber(0);
		hostOrder.setRegisterCard(host.getRegisterCard());
		hostOrderMapper.insertHostOrder(hostOrder);
		
		host.setOrderId(hostOrder.getOrderId());
		hostMapper.insertSelective(host);
		
		historyMapper.insertHistory(host.getHostAddress(),host.getHostId());
//		Com com = new Com();
//		com.setHostId(host.getHostId());
//		com.setComName("COM1");
//		com.setComEnable("0");
//		com.setComState(1);
//		com.setComJyw("none");
//		com.setComTzw("1");
//		com.setComSjw("8");
//		com.setComLkz("Custom");
//		com.setComMode(0);
//		com.setComBautrate("9600");
//		com.setComCollectiontime(1000);
//		com.setComPort(0);
//		comMapper.insertSelective(com);
		return true;
	}

	@Override
	public boolean updateSelective(Host host) throws MsgException {
		hostMapper.updateSelective(host);
		Com comQuery = new Com();
		comQuery.setHostId(host.getHostId());
		List<Com> comList = comMapper.findSelective(comQuery);
		if(ValidateKit.isEmpty(comList)){
			return true;
		}
		if(comList.get(0).getHostId().intValue() != host.getHostId()){
			comList.get(0).setHostId(host.getHostId());
			comMapper.updateSelective(comList.get(0));
		}
		return true;
	}

	@Override
	public boolean deleteByPrimaryKey(Integer hostId) throws MsgException {
		Probe probeQuery = new Probe();
        probeQuery.setHostId(hostId);
        List<Probe> probeList = probeMapper.findProbeSelective(probeQuery);
        if (probeList != null && probeList.size() > 0) {
            throw new MsgException(ResultStatusCode.ERROR_CANNOT_BE_DELETED);
        }
        hostOrderMapper.deleteHostOrder(hostId);
        
        hostMapper.deleteByPrimaryKey(hostId);
        
        historyMapper.deleteHistory(hostId);
        
        Com comQuery = new Com();
        comQuery.setHostId(hostId);
        List<Com> comList = comMapper.findSelective(comQuery);
        if(!ValidateKit.isEmpty(comList)){
            comMapper.deleteByPrimaryKey(comList.get(0).getComId());
        }
        return true ;
	}
	
	@Override
    public List<Host> findHostSelective(Host host) {
        List<Host> list = hostMapper.findHostSelective(host);
        return list == null ? new ArrayList<Host>() : list;
    }

	@Override
	public List<Host> selectHostByQx(Integer pageSize, Integer pageNo, int[] qx,String hostName) {
		PageHelper.startPage(pageNo, pageSize);
		List<Host> list = hostMapper.selectHostByQx(qx,hostName);
		return list;
	}
	
	@Override
	public void updateHostOrder(HostOrder hostOrder) {
		hostOrderMapper.updateHostOrder(hostOrder);
	}

	@Override
	public void deleteHostOrder(Integer hostId) {
		hostOrderMapper.deleteHostOrder(hostId);
	}

	@Override
	public void addRegisterNumber(Integer hostId) {
		hostOrderMapper.addRegisterNumber(hostId);
	}

	@Override
	public void reduceRegisterNumber(Integer probeId) {
		hostOrderMapper.reduceRegisterNumber(probeId);
	}
}
