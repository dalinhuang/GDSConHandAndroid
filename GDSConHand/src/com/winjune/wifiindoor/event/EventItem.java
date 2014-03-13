package com.winjune.wifiindoor.event;

import java.util.ArrayList;

class EventTime {
	public int fromHour;
	public int fromMin;
	public int toHour;
	public int toMin;
	
	public EventTime (int fromHour, int fromMin, int toHour, int toMin) {
		this.fromHour = fromHour;
		this.fromMin = fromMin;
		this.toHour = toHour;
		this.toMin = toMin;		
	}
}

public class EventItem {
	private String title;
	private int placeX;
	private int placeY;
	private int placeNo;	
	private ArrayList<EventTime> normalDayTime;
	private ArrayList<EventTime> weekendTime;
	private ArrayList<EventTime> festivalTime;
	private String desc;
	
	public EventItem(String title) {
		this.title = title;
		
		normalDayTime = new ArrayList<EventTime>();
		weekendTime = new ArrayList<EventTime>();
		festivalTime = new ArrayList<EventTime>();
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
	
	public void addNormalDayTime(int fromHour, int fromMin, int toHour, int toMin){
		EventTime eventTime = new EventTime(fromHour, fromMin, toHour, toMin);
		
		this.normalDayTime.add(eventTime);		
	}
	
	public void addWeekendTime(int fromHour, int fromMin, int toHour, int toMin){
				
		EventTime eventTime = new EventTime(fromHour, fromMin, toHour, toMin);
		
		this.weekendTime.add(eventTime);				
	}
	
	public void adFestivalTime(int fromHour, int fromMin, int toHour, int toMin){
				
		EventTime eventTime = new EventTime(fromHour, fromMin, toHour, toMin);
		
		this.festivalTime.add(eventTime);				
	}
	
	public void addWeekdayTime(int fromHour, int fromMin, int toHour, int toMin){
		
		EventTime eventTime = new EventTime(fromHour, fromMin, toHour, toMin);
		
		this.normalDayTime.add(eventTime);		
		this.weekendTime.add(eventTime);				
	}
	
	public void addHolidayTime(int fromHour, int fromMin, int toHour, int toMin){
		
		EventTime eventTime = new EventTime(fromHour, fromMin, toHour, toMin);
		
		this.weekendTime.add(eventTime);
		this.festivalTime.add(eventTime);				
	}
}
