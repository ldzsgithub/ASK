package com.jy23.serverImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jy23.entity.AskAlarmRecords;
import com.jy23.entity.HistoryDataApp;
import com.jy23.entity.HistoryDataWeb;
import com.jy23.entity.Probe;
import com.jy23.mapper.HistoryMapper;
import com.jy23.mapper.ProbeMapper;
import com.jy23.server.HistoryServer;

@Service("historyServer")
public class HistoryServerImpl implements HistoryServer {
    @Autowired
    private HistoryMapper historyMapper;
    @Autowired
    private ProbeMapper probeMapper;

    @Override
    public List<HistoryDataApp> findHistoryCharApp(String tableName, String time, Integer hostId, Integer probeId) {
        Probe probe = probeMapper.findByPrimaryKey(probeId);
        String columName = "a" + probe.getProbeBh();
        String startTime = time + " 00:00:00";
        String endTime = time + " 23:59:59";
        return historyMapper.findHistoryCharApp(tableName, startTime, endTime, hostId, columName);
    }

    @Override
    public List<HistoryDataWeb> findHistoryCharWeb(String tableName, String time, Integer hostId, Integer probeId) {
        Probe probe = probeMapper.findByPrimaryKey(probeId);
        String columName = "a" + probe.getProbeBh();
        String startTime = time + " 00:00:00";
        String endTime = time + " 23:59:59";
        return historyMapper.findHistoryCharWeb(tableName, startTime, endTime, hostId, columName);
    }

    @Override
    public List<HistoryDataWeb> toHistoryProbeAlarm(String tableName, String time, Integer hostId, Integer probeId) {
        Probe probe = probeMapper.findByPrimaryKey(probeId);
        String columName = "a" + probe.getProbeBh();
        String startTime = time + " 00:00:00";
        String endTime = time + " 23:59:59";

        int lowVal = probe.getLowValue().intValue();
        int highVal = probe.getHighValue().intValue();

        List<HistoryDataWeb> list = historyMapper.toHistoryProbeAlarm(tableName, startTime, endTime, hostId, columName, lowVal);
        if (list != null && list.size() > 0) {
            for (HistoryDataWeb h : list) {
                if (h.getNumValue() >= lowVal && h.getNumValue() < highVal) {
                    h.setAlarm(1);
                } else {
                    h.setAlarm(2);
                }
            }
        }
        return list;
    }

    @Override
    public List<AskAlarmRecords> findByMonth(String tableName,String  columName, Integer hostId) {
        return historyMapper.findByMonth(tableName, columName, hostId);
    }

	@Override
	public void updateHistory(String hostAddress,Integer hostId) {
		historyMapper.updateHistory(hostAddress, hostId);
	}
}
