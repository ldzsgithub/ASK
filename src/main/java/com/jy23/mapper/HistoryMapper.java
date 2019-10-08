package com.jy23.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jy23.entity.AskAlarmRecords;
import com.jy23.entity.History;
import com.jy23.entity.HistoryDataApp;
import com.jy23.entity.HistoryDataWeb;

public interface HistoryMapper {
    int insertSelective(History history);

    int updateSelective(History history);

    History findByhostAddr(@Param("host_addr") Integer hostAddr);


    List<History> findHistorySelective(History history);

    List<HistoryDataApp> findHistoryCharApp(@Param("tableName") String tableName,
                                            @Param("startTime") String startTime,
                                            @Param("endTime") String endTime,
                                            @Param("hostId") Integer hostId,
                                            @Param("columName") String columName);

    List<HistoryDataWeb> findHistoryCharWeb(@Param("tableName") String tableName,
                                            @Param("startTime") String startTime,
                                            @Param("endTime") String endTime,
                                            @Param("hostId") Integer hostId,
                                            @Param("columName") String columName);

    List<HistoryDataWeb> toHistoryProbeAlarm(@Param("tableName") String tableName,
                                             @Param("startTime") String startTime,
                                             @Param("endTime") String endTime,
                                             @Param("hostId") Integer hostId,
                                             @Param("columName") String columName,
                                             @Param("lowVal") Integer lowVal);

    List<AskAlarmRecords> findByMonth(@Param("tableName") String tableName,
    									@Param("columName") String columName,
    									@Param("hostId") Integer hostId);

	void insertHistory(@Param("hostAddress")Integer hostAddress,
							@Param("hostId") Integer hostId);
	
	void updateHistory(@Param("hostAddress")String hostAddress,
			@Param("hostId") Integer hostId);
	
	void deleteHistory(@Param("hostId") Integer hostId);
}
