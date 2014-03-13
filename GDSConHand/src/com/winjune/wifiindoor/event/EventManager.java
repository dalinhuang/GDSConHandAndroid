package com.winjune.wifiindoor.event;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.util.Log;

public class EventManager {
	private ArrayList<Date> holidays;
	private Map<Integer, String> placesMap;
	private ArrayList<EventItem> eventItems;
	public static int MaxPlaceNo = 5;
	
	public EventManager(){
		
		holidays = new ArrayList<Date>();
		
		placesMap = new HashMap<Integer, String>();
		
		placesMap.put(1, "试验与发现馆");
		placesMap.put(2, "儿童天地馆");
		placesMap.put(3, "交通世界馆");
		
		eventItems = new ArrayList<EventItem>();
		
		EventItem eventItem1	= new EventItem("电磁舞台");
		eventItem1.setPlace(0,0, 1);
		eventItem1.addNormalDayTime(10, 20, 0, 0);
		eventItem1.addNormalDayTime(14, 30, 0, 0);
		eventItem1.addHolidayTime(10, 30, 0, 0);
		eventItem1.addHolidayTime(12, 0, 0, 0);
		eventItem1.addHolidayTime(15, 30, 0, 0);	
		eventItems.add(eventItem1);
	
		EventItem eventItem2	= new EventItem("基因剧场");
		eventItem2.setPlace(0,0, 1);
		eventItem2.addNormalDayTime(11, 0, 0, 0);
		eventItem2.addNormalDayTime(14, 0, 0, 0);
		eventItem2.addHolidayTime(11, 0, 0, 0);
		eventItem2.addHolidayTime(14, 0, 0, 0);
		eventItem2.addHolidayTime(14, 30, 0, 0);	
		eventItems.add(eventItem2);
		
		EventItem eventItem3	= new EventItem("启蒙剧场");
		eventItem3.setPlace(0,0, 2);	
		eventItem3.addHolidayTime(14, 30, 0, 0);	
		eventItems.add(eventItem3);
		
		EventItem eventItem4	= new EventItem("磁悬浮技术");
		eventItem4.setPlace(0,0, 3);
		eventItem4.addWeekdayTime(10, 30, 12, 0);
		eventItem4.addWeekdayTime(14, 0, 15, 30);
		eventItem4.adFestivalTime(10, 0, 12, 0);
		eventItem4.adFestivalTime(14, 0, 16, 0);	
		eventItems.add(eventItem4);	
		
	}
	
	private void addHoliday(String strDt) { 
	    // 2012-02-24  
	    Date dt = java.sql.Date.valueOf(strDt);
	    
	    holidays.add(dt);		
	}

		
	private boolean isHoliday(Date dt) {
		
		for (int i=0; i<holidays.size(); i++) {
			Date tmpDt = holidays.get(i); 
			
			if (tmpDt == dt) return true; 
		}			
		
		return false;
	}
	
	public int[] getTodayEventPlaces(){		
		int[] places = new int[MaxPlaceNo];;

    	for (int j=0; j<MaxPlaceNo; j++) {
    		places[j] = -1;
    	}
    	
		Date dt = new Date(0);;		
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0 )	dayOfWeek = 0;
        
        
        int addedNum = 0;
        for (int i=0; i<eventItems.size();i++){
        	EventItem et = eventItems.get(i);
       		
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
	
	public String getPlaceLabel(int placeNo){
		
		return placesMap.get(placeNo);		
	}
	
	public ArrayList<EventItem> getTodayEventListByPlace(int placeNo ){
		ArrayList<EventItem> todayEvent = new ArrayList<EventItem>();
		
		for (int i=0; i<eventItems.size();i++) {
			if (eventItems.get(i).getPlaceNo() == placeNo) {
				todayEvent.add(eventItems.get(i));
			}
		}
		
		return todayEvent;
	}
	
	

}
