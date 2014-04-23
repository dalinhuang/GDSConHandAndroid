package com.winjune.wifiindoor.poi;

import java.util.ArrayList;

public class MovieInfo {
	private int id;
	private int theaterId; // POI id
	
	private String generalDesc;
	private String priceInfo;
	private String iconUrl; // for icon display in theatre activity
	private String posterUrl; // for display in movie activity and social share
	private ArrayList<ScheduleTime> todaySchedule;
	private ArrayList<ScheduleTime> tomorrowSchedule;
	
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
	
	public ArrayList<ScheduleTime> getTodaySchedule(){
		return todaySchedule;
	}

	public ArrayList<ScheduleTime> getTomorrowSchedule(){
		return tomorrowSchedule;
	}
}
