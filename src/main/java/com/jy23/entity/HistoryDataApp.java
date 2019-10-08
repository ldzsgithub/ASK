package com.jy23.entity;


import java.util.Date;

public class HistoryDataApp {
	private Integer hostAddr;//
	private Float numValue;//
	private Date historyDate;//

	public Integer getHostAddr() {
		return hostAddr;
	}

	public void setHostAddr(Integer hostAddr) {
		this.hostAddr = hostAddr;
	}

	public Float getNumValue() {
		return numValue;
	}

	public void setNumValue(Float numValue) {
		this.numValue = numValue;
	}

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}
}
