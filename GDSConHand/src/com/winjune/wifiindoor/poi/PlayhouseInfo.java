package com.winjune.wifiindoor.poi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class PlayhouseInfo extends PlaceOfInterest{
	
	private ArrayList<ScheduleTime> normalDayTime;
	private ArrayList<ScheduleTime> weekendTime;
	private ArrayList<ScheduleTime> festivalTime;
	
	public PlayhouseInfo(String title) {
		super(POIType.Playhouse);
		
		this.label = title;
	
		normalDayTime = new ArrayList<ScheduleTime>();
		weekendTime = new ArrayList<ScheduleTime>();
		festivalTime = new ArrayList<ScheduleTime>();		
	}
	
	public ArrayList<ScheduleTime> getTodaySchedule() {
		
		
		Date dt = new Date(0);;		
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0 )	dayOfWeek = 0;
        
        ArrayList<ScheduleTime> timeList;
        if (EventManager.isFestivalDay(dt)) {
        	return festivalTime;
        } else if ((dayOfWeek == 0) || (dayOfWeek == 6))  {
        	return weekendTime;        	
        }
        
        return normalDayTime;
  				
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
