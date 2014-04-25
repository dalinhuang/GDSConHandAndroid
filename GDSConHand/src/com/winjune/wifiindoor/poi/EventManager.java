package com.winjune.wifiindoor.poi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import android.annotation.SuppressLint;
import android.util.Log;

public class EventManager {
	private static ArrayList<Date> festivalDays;
	
	//private static Map<Integer, String> placesMap; 
	private static ArrayList<PlayhouseInfo> eventItems;
	public static int MaxPlaceNo = 8;
	
	private static boolean initialed = false;
	
	public static int PANNEL_TIME_STEP  = 1; // the time based event show, interval
			
	public final static String Key_Event_Title = "Key_Event_Title";
	public final static String Key_Event_Alarm_Time = "Key_Event_Alarm_Time";
	
	@SuppressLint("UseSparseArrays")
	//public EventManager(){
	public static void initial(){
	
		if (initialed){
			return;
		}
		
		initialed = true;
		
		festivalDays = new ArrayList<Date>();
		addFestivalDay("2014-01-01");
		addFestivalDay("2014-04-05");//清明
		addFestivalDay("2014-05-01");
		addFestivalDay("2014-06-02"); //端午
		addFestivalDay("2014-09-08");
		addFestivalDay("2014-10-01");
		addFestivalDay("2014-10-02");
		addFestivalDay("2014-10-03");		
		addFestivalDay("2014-10-04");
		addFestivalDay("2014-10-05");
		addFestivalDay("2014-10-06");
		addFestivalDay("2014-10-07");					
	}
	
	private static void addFestivalDay(String strDt) { 
	    // 2012-02-24  
	    Date dt = java.sql.Date.valueOf(strDt);
	    
	    festivalDays.add(dt);		
	}

		
	public static boolean isFestivalDay(Date dt) {
		
		for (Date tmpDt:festivalDays) {						
			if (tmpDt == dt) return true; 
		}			
		
		return false;
	}
	
/*	public int[] getHallsWithPlayhouse(){		
		int[] places = new int[MaxPlaceNo];;

    	for (int j=0; j<MaxPlaceNo; j++) {
    		places[j] = -1;
    	}       
        
        int addedNum = 0;
        for (PlayhouseInfo et:eventItems){        	
       		
        	boolean alreadyAdded = false;
        	for (int j=0; j<addedNum; j++) {        		
        		if (places[j] == et.getPlaceNo()){ 
        			alreadyAdded = true;
        			break; 
        		}
        	}
        	
        	if (!alreadyAdded){
        		places[addedNum] = et.getPlaceNo();
        		addedNum ++;
        	}
        }
		
        if (addedNum > 0) {        	
        	
        	int[] placesGroup = Arrays.copyOf(places, addedNum);        
        	return placesGroup;
        }
        
        return null;
	}	
*/	
	private static PlayhouseInfo getEventByTitle(String title){
		
		for (PlayhouseInfo et : eventItems){
			
			if (et.getTitle().equals(title)){
				return et;
			}
		}
		
		return null;
	}
	
	public static ArrayList<ScheduleTime> getEventTodayTime(String title){
		
		PlayhouseInfo et = getEventByTitle(title);
		
		if (et != null){
			return getEventTodayTime(et);
		}
		
		return null;
	}

	private static ArrayList<ScheduleTime> getEventTodayTime(PlayhouseInfo et) {
		
		
		Date dt = new Date(0);;		
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0 )	dayOfWeek = 0;
        
        ArrayList<ScheduleTime> timeList;
        if (isFestivalDay(dt)) {
        	timeList = et.getFestivalTime();
        } else if ((dayOfWeek == 0) || (dayOfWeek == 6))  {
        	timeList = et.getWeekendTime();        	
        } else {
        	timeList = et.getNormalDayTime();
        }
        
        return timeList;
       				
	}
	
	public String getEventTimeInfo(PlayhouseInfo ei) {				
		if (ei == null)
			return null;		
		       
        ArrayList<ScheduleTime> timeList = getEventTodayTime(ei);
       		
		if (timeList == null)
			return null;
		
		String eventInfo = "";
		
		for ( ScheduleTime et: timeList){
			eventInfo += et.toString() + "  ";
		}
		
		return eventInfo;
	}
	
	public ArrayList<PlayhouseInfo> getTodayEventListByPlace(int placeNo ){
		ArrayList<PlayhouseInfo> todayEvent = new ArrayList<PlayhouseInfo>();
		
		for (PlayhouseInfo ei:eventItems) {
			if (ei.getPlaceNo() == placeNo) {
				todayEvent.add(ei);
			}
		}
		
		return todayEvent;
	}
	
	public static ArrayList<PlayhouseInfo> getTodayEventListByTime(int fromHour, int toHour ){
		ArrayList<PlayhouseInfo> todayEvent = new ArrayList<PlayhouseInfo>();
		ArrayList<ScheduleTime> timeList;
		
		for (PlayhouseInfo ei:eventItems) {
			
			timeList = getEventTodayTime(ei);
			for (ScheduleTime et: timeList){				
				
				//some event only has start time								
				if ((et.fromHour >= fromHour) && (et.fromHour < toHour)) {						
					todayEvent.add(ei);
					break;
				} 				
			}
		}
		
		return todayEvent;
	}
	
	public static ArrayList <ScheduleTime> getEventAlarmTimeOfToday(PlayhouseInfo ei){
		
		ArrayList<ScheduleTime> timeList = getEventTodayTime(ei);
		ArrayList<ScheduleTime> alarmList = new ArrayList<ScheduleTime>();
		
		for (ScheduleTime et: timeList){
			if (et.getAlarmStatus()){
				alarmList.add(et);
			}
		}
		
		return alarmList;
	}
		
	public static ArrayList <PlayhouseInfo> getEventListByAlarm(){
		
		ArrayList<PlayhouseInfo> events = new ArrayList<PlayhouseInfo>();
		ArrayList<ScheduleTime> timeList;
		
		for (PlayhouseInfo ei:eventItems){
			timeList = getEventAlarmTimeOfToday(ei);
			
			if (!timeList.isEmpty()){
				events.add(ei);
			}
		}
		
		return events;
	}

}
