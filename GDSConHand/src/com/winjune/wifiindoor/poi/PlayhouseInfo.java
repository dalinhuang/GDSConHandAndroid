package com.winjune.wifiindoor.poi;

import java.util.ArrayList;

public class PlayhouseInfo extends PlaceOfInterest{

	private int hallId;
	
	private ArrayList<ScheduleTime> normalDayTime;
	private ArrayList<ScheduleTime> weekendTime;
	private ArrayList<ScheduleTime> festivalTime;
	
	public PlayhouseInfo(String title) {
		super();
		
		this.label = title;
	
		normalDayTime = new ArrayList<ScheduleTime>();
		weekendTime = new ArrayList<ScheduleTime>();
		festivalTime = new ArrayList<ScheduleTime>();		
	}
	
	
	public void setPlace(int placeX, int placeY, int placeNo){
		this.hallId = placeNo;
		this.placeX = placeX;
		this.placeY = placeY;
	}
	
	public int getPlaceNo(){
		return hallId;
	}
	
	public String getTitle(){
		return label;
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

	public void addNormalDayTimes(String schedulesStr){
		ScheduleTime.addScheduleList(schedulesStr, normalDayTime);
	}
	
	public void addWeekendTimes(String schedulesStr){
		ScheduleTime.addScheduleList(schedulesStr, weekendTime);
	}
	
	public void addFestivalTimes(String schedulesStr){
		ScheduleTime.addScheduleList(schedulesStr, festivalTime);
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
