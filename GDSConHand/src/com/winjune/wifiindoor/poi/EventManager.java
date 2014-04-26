package com.winjune.wifiindoor.poi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import android.annotation.SuppressLint;

public class EventManager extends POIManager{
	private static ArrayList<Date> festivalDays;
	
	//private static ArrayList<PlayhouseInfo> eventItems;
	
	private static boolean initialed = false;
	
	
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
	
	public static Integer[] getHallsWithPlayhouse(){		
		HashSet<Integer>  hallSet = new HashSet<Integer> ();
  
        for (PlaceOfInterest et:POIList){        	
       		if (et.poiType == POIType.Playhouse)
       			hallSet.add(et.hallId);
        }
        	
        Integer[] placesGroup = hallSet.toArray(new Integer[0]);        
        return placesGroup;
	}		
	
	public ArrayList<PlayhouseInfo> getTodayEventListByHall(int placeNo ){
		ArrayList<PlayhouseInfo> todayEvent = new ArrayList<PlayhouseInfo>();
		
        for (PlaceOfInterest et:POIList){        	
       		if (et.poiType == POIType.Playhouse) {
       			if (et.getHall() == placeNo) {
       				todayEvent.add((PlayhouseInfo)et);
       			}
        	}
        }
		
		return todayEvent;
	}
	
	public static ArrayList<PlayhouseInfo> getTodayEventListByTime(int fromHour, int toHour ){
		ArrayList<PlayhouseInfo> todayEvent = new ArrayList<PlayhouseInfo>();
		ArrayList<ScheduleTime> timeList;
		
		for (PlaceOfInterest ei:POIList) {
			if (ei.poiType == POIType.Playhouse) {
			
				timeList = ((PlayhouseInfo)ei).getTodaySchedule();
				for (ScheduleTime et: timeList){				
					
					//some event only has start time								
					if ((et.fromHour >= fromHour) && (et.fromHour < toHour)) {						
						todayEvent.add((PlayhouseInfo)ei);
						break;
					} 				
				}
			}
		}
		
		return todayEvent;
	}
	
		
	public static ArrayList <PlayhouseInfo> getEventListByAlarm(){
		
		ArrayList<PlayhouseInfo> events = new ArrayList<PlayhouseInfo>();
		ArrayList<ScheduleTime> timeList;
		
		for (PlaceOfInterest ei:POIList){
			
			if (ei.poiType == POIType.Playhouse) {
				timeList = ((PlayhouseInfo)ei).getEventAlarmTimeOfToday();
			
				if (!timeList.isEmpty()){
					events.add((PlayhouseInfo)ei);
				}
			}
		}
		
		return events;
	}
}
