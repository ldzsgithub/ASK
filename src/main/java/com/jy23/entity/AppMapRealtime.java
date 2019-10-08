package com.jy23.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AppMapRealtime implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 792163867126819624L;
	
	private Float latitude;					//纬度
    private Float longitude;				//精度
    private int lever = 0;					//精度[0,正常;1,低报;2,高爆]
    private List<Probe> realtimeList;

    private String latlngKey;

    public int getLever() {
        return lever;
    }

    public void setLever(int lever) {
        this.lever = lever;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getLatlngKey() {
        return latlngKey;
    }

    public void setLatlngKey(String latlngKey) {
        this.latlngKey = latlngKey;
    }

    public AppMapRealtime(Float latitude, Float longitude, String latlngKey) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.latlngKey = latlngKey;
        realtimeList = new ArrayList<>();
    }

    public List<Probe> getRealtimeList() {
        return realtimeList;
    }

    public void setRealtimeList(List<Probe> realtimeList) {
        this.realtimeList = realtimeList;
    }
}
