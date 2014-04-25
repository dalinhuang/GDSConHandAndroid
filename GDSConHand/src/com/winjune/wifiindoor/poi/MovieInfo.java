package com.winjune.wifiindoor.poi;

import java.util.ArrayList;

public class MovieInfo {
	private int theaterId; // POI id
	
	public String name;
	
	private String generalDesc;
	private String priceInfo;
	private String iconUrl; // for icon display in theater activity
	private String posterUrl; // for display in movie activity and social share
	private ArrayList<ScheduleTime> todaySchedule;
	private ArrayList<ScheduleTime> tomorrowSchedule;
	
	public MovieInfo(int theaterId, String name, String price, String generalDesc) {
		this.theaterId = theaterId;
		this.name = name;
		this.generalDesc = generalDesc;
		this.priceInfo = price;
		
		todaySchedule = new ArrayList<ScheduleTime>();
		tomorrowSchedule = new ArrayList<ScheduleTime>();
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
	
	public String getTodayScheduleStr(){
		String info = "";
		
		for (ScheduleTime item: todaySchedule)
			info += item.toString()+"  ";
		
		return info;
	}
		
	public void addTodaySchedule(String schedulesStr){
		ScheduleTime.addScheduleList(schedulesStr, todaySchedule);
	}
	
	public void addTomorrowSchedule(String schedulesStr){
		ScheduleTime.addScheduleList(schedulesStr, tomorrowSchedule);
	}	
	
	public ArrayList<ScheduleTime> getTodaySchedule(){
		return todaySchedule;
	}

	public ArrayList<ScheduleTime> getTomorrowSchedule(){
		return tomorrowSchedule;
	}
}
