package com.jy23.server;

import java.util.List;

import com.jy23.entity.Trouble;

public interface TroubleServer {

	List<Trouble> findAllTrouble(Trouble trouble);

	Trouble findTroubleById(Integer id);

	int insertTrouble(Trouble trouble);

	void updateTrouble(Trouble trouble);

	int counta(Integer id, String sTime, String nTime);

	int countb(Integer id, String sTime, String nTime);

	int countc(Integer id, String sTime, String nTime);

	int countd(Integer id, String sTime, String nTime);

	int counte(Integer id, String sTime, String nTime);

	int countf(Integer id, String sTime, String nTime);

}
