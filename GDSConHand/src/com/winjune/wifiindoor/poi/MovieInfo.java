package com.winjune.wifiindoor.poi;

import java.util.ArrayList;

import com.winjune.wifiindoor.event.EventTime;

public class MovieInfo {
	private int id;
	private int theaterId; // POI id
	
	private String generalDesc;
	private String priceInfo;
	private String posterUrl;
	private ArrayList<EventTime> todaySchedule;
	private ArrayList<EventTime> tomorrowSchedule;
	
	public MovieInfo(int id, String generalDesc, String price, String posterUrl) {
		this.id = id;
		this.generalDesc = generalDesc;
		this.priceInfo = price;
		this.posterUrl = posterUrl;
	}
	
	public String getGeneralDesc(){
		return generalDesc;
	}
	
	public String getPriceInfo(){
		return priceInfo;
	}
	
	public String getPosterUrl(){
		return posterUrl;
	}
	
	public ArrayList<EventTime> getTodaySchedule(){
		return todaySchedule;
	}

	public ArrayList<EventTime> getTomorrowSchedule(){
		return tomorrowSchedule;
	}
}
