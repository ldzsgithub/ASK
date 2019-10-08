package com.jy23.server;

import java.util.List;

import com.jy23.entity.AskAlarmRecords;
import com.jy23.entity.HistoryDataApp;
import com.jy23.entity.HistoryDataWeb;

public interface HistoryServer {

    List<HistoryDataApp> findHistoryCharApp(String tableName, String time, Integer hostId, Integer probeId);

    List<HistoryDataWeb> findHistoryCharWeb(String tableName, String time, Integer hostId, Integer probeId);

    List<HistoryDataWeb> toHistoryProbeAlarm(String tableName, String time, Integer hostId, Integer probeId);

    List<AskAlarmRecords> findByMonth(String tableName, String columName, Integer hostId);

	void updateHistory(String hostAddress,Integer hostId);
}
