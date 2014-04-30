package com.winjune.wifiindoor.poi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import com.winjune.wifiindoor.lib.poi.FestivalT;
import com.winjune.wifiindoor.lib.poi.POIType;
import com.winjune.wifiindoor.lib.poi.ScheduleTime;

import android.annotation.SuppressLint;

public class EventManager extends POIManager{
		
	public static boolean isFestivalDay(Date dt) {			
		return festivalData.isFestivalDay(dt);
	}
	
	public static Integer[] getHallsWithPlayhouse(){		
		HashSet<Integer>  hallSet = new HashSet<Integer> ();
  
        for (PlaceOfInterest et:POIList){        	
       		if (et.getPoiType() == POIType.Playhouse)
       			hallSet.add(et.hallId);
        }
        	
        Integer[] placesGroup = hallSet.toArray(new Integer[0]);        
        return placesGroup;
	}		
	
	public static ArrayList<PlayhouseInfo> getTodayEventListByHall(int placeNo ){
		ArrayList<PlayhouseInfo> todayEvent = new ArrayList<PlayhouseInfo>();
		
        for (PlaceOfInterest et:POIList){        	
       		if (et.getPoiType() == POIType.Playhouse) {
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
			if (ei.getPoiType() == POIType.Playhouse) {
			
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
			
			if (ei.getPoiType() == POIType.Playhouse) {
				timeList = ((PlayhouseInfo)ei).getEventAlarmTimeOfToday();
			
				if (!timeList.isEmpty()){
					events.add((PlayhouseInfo)ei);
				}
			}
		}
		
		return events;
	}
}
