package com.winjune.wifiindoor.poi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventItem {
	private String title;
	private int placeX;
	private int placeY;
	private int placeNo;	
	private ArrayList<ScheduleTime> normalDayTime;
	private ArrayList<ScheduleTime> weekendTime;
	private ArrayList<ScheduleTime> festivalTime;
	private String desc;
	
	public EventItem(String title) {
		this.title = title;
		
		normalDayTime = new ArrayList<ScheduleTime>();
		weekendTime = new ArrayList<ScheduleTime>();
		festivalTime = new ArrayList<ScheduleTime>();
		
	}
	
	
	public void setPlace(int placeX, int placeY, int placeNo){
		this.placeNo = placeNo;
		this.placeX = placeX;
		this.placeY = placeY;
	}
	
	public int getPlaceNo(){
		return placeNo;
	}
	
	public String getTitle(){
		return title;
	}
	
	public ArrayList<ScheduleTime> getNormalDayTime() {
		return normalDayTime;
	}
	
	public ArrayList<ScheduleTime> getWeekendTime() {
		return weekendTime;
	}

	public ArrayList<ScheduleTime> getFestivalTime() {
		return normalDayTime;
	}

	
	public void addNormalDayTime(int fromHour, int fromMin, int toHour, int toMin){
		ScheduleTime eventTime = new ScheduleTime(fromHour, fromMin, toHour, toMin);		
		this.normalDayTime.add(eventTime);		
	}
	
	public void addWeekendTime(int fromHour, int fromMin, int toHour, int toMin){
				
		ScheduleTime eventTime = new ScheduleTime(fromHour, fromMin, toHour, toMin);
		
		this.weekendTime.add(eventTime);				
	}
	
	public void addFestivalTime(int fromHour, int fromMin, int toHour, int toMin){
				
		ScheduleTime eventTime = new ScheduleTime(fromHour, fromMin, toHour, toMin);
		
		this.festivalTime.add(eventTime);				
	}
	
	public void addWeekdayTime(int fromHour, int fromMin, int toHour, int toMin){
		
		ScheduleTime eventTime = new ScheduleTime(fromHour, fromMin, toHour, toMin);
		
		this.normalDayTime.add(eventTime);		
		this.weekendTime.add(eventTime);				
	}
	
	public void addHolidayTime(int fromHour, int fromMin, int toHour, int toMin){
		
		ScheduleTime eventTime = new ScheduleTime(fromHour, fromMin, toHour, toMin);
		
		this.weekendTime.add(eventTime);
		this.festivalTime.add(eventTime);				
	}
	
	public void addAllTime(int fromHour, int fromMin, int toHour, int toMin){
		
		ScheduleTime eventTime = new ScheduleTime(fromHour, fromMin, toHour, toMin);
		
		this.normalDayTime.add(eventTime);				
		this.weekendTime.add(eventTime);
		this.festivalTime.add(eventTime);				
	}
	
}
