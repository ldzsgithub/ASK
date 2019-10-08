package com.jy23.entity;


import com.fasterxml.jackson.annotation.JsonFormat;

public class HistoryDataWeb {
    private Float numValue;//

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String historyDate;//
    private Integer alarm;//1,低报;2,高报

    public Integer getAlarm() {
        return alarm;
    }

    public void setAlarm(Integer alarm) {
        this.alarm = alarm;
    }

    public HistoryDataWeb() {
    }

    public HistoryDataWeb(Float numValue, String historyDate) {

        this.numValue = numValue;
        this.historyDate = historyDate;
    }

    public Float getNumValue() {
        return numValue;
    }

    public void setNumValue(Float numValue) {
        this.numValue = numValue;
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
    }
}
