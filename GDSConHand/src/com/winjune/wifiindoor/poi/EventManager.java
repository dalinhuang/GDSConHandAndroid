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
	private static Map<Integer, String> placesMap;
	private static ArrayList<EventItem> eventItems;
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
		
		placesMap = new HashMap<Integer, String>();
		
		placesMap.put(1, "试验与发现馆");
		placesMap.put(2, "儿童天地馆");
		placesMap.put(3, "交通世界馆");
		placesMap.put(4, "数码世界馆");
		placesMap.put(5, "飞天之梦馆");
		placesMap.put(6, "绿色家园馆");
		placesMap.put(7, "人与健康馆");
		placesMap.put(8, "感知与思维馆");
		
		
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
		eventItem4.addFestivalTime(10, 0, 12, 0);
		eventItem4.addFestivalTime(14, 0, 16, 0);	
		eventItems.add(eventItem4);	

		EventItem eventItem5	= new EventItem("模拟汽车工厂");
		eventItem5.setPlace(0,0, 3);
		eventItem5.addAllTime(9, 30, 12, 0);
		eventItem5.addAllTime(13, 0, 16, 30);			
		eventItems.add(eventItem5);	

		EventItem eventItem6	= new EventItem("机器人搏击");
		eventItem6.setPlace(0,0, 4);
		eventItem6.addAllTime(10, 45, 0, 0);
		eventItem6.addAllTime(11, 45, 0, 0);
		eventItem6.addAllTime(14, 45, 0, 0);
		eventItem6.addAllTime(15, 45, 0, 0);	
		eventItem6.addFestivalTime(13, 45, 0, 0);
		eventItems.add(eventItem6);	

		EventItem eventItem7	= new EventItem("太空生活表演");
		eventItem7.setPlace(0,0, 5);
		eventItem7.addAllTime(10, 30, 0, 0);
		eventItem7.addNormalDayTime(14, 20, 0, 0);
		eventItem7.addFestivalTime(13, 0, 0, 0);
		eventItem7.addFestivalTime(15, 0, 0, 0);	
		eventItems.add(eventItem7);	

		EventItem eventItem8	= new EventItem("航天发射指挥控制中心");
		eventItem8.setPlace(0,0, 5);
		eventItem8.addNormalDayTime(10, 45, 0, 0);
		eventItem8.addNormalDayTime(13, 0, 0, 0);
		eventItem8.addNormalDayTime(14, 0, 0, 0);
		eventItem8.addHolidayTime(11, 0, 0, 0);
		eventItem8.addHolidayTime(13, 30, 0, 0);
		eventItem8.addHolidayTime(15, 0, 16, 0);	
		eventItems.add(eventItem8);	

		EventItem eventItem9	= new EventItem("飞行模拟剧场");
		eventItem9.setPlace(0,0, 5);
		eventItem9.addAllTime(10, 15, 0, 0);
		eventItem9.addAllTime(11, 15, 0, 0);
		eventItem9.addAllTime(14, 0, 0, 0);
		eventItem9.addAllTime(14, 45, 0, 0);	
		eventItem9.addAllTime(15, 30, 0, 0);
		eventItems.add(eventItem9);	

		EventItem eventItem10	= new EventItem("台风体验");
		eventItem10.setPlace(0,0, 6);
		eventItem10.addAllTime(10, 0, 10, 30);
		eventItem10.addAllTime(10, 50, 11, 30);
		eventItem10.addAllTime(12, 50, 13, 30);
		eventItem10.addAllTime(13, 50, 14, 30);	
		eventItem10.addAllTime(14, 50, 15, 30);
		eventItems.add(eventItem10);	
		
		EventItem eventItem11	= new EventItem("地球物体剧场");
		eventItem11.setPlace(0,0, 6);
		eventItem11.addAllTime(10, 45, 0, 0);
		eventItem11.addAllTime(11, 35, 0, 0);
		eventItem11.addAllTime(13, 35, 0, 0);
		eventItem11.addAllTime(14, 35, 0, 0);	
		eventItem11.addAllTime(15, 35, 0, 0);
		eventItems.add(eventItem11);	
		
		EventItem eventItem12	= new EventItem("虚拟人体漫游");
		eventItem12.setPlace(0,0, 7);
		eventItem12.addAllTime(9, 50, 0, 0);
		eventItem12.addAllTime(10, 30, 0, 0);
		eventItem12.addAllTime(11, 10, 0, 0);
		eventItem12.addAllTime(11, 50, 0, 0);	
		eventItem12.addAllTime(13, 40, 0, 0);
		eventItem12.addAllTime(14, 20, 0, 0);
		eventItem12.addAllTime(15, 0, 0, 0);
		eventItem12.addAllTime(15, 40, 0, 0);
		eventItem7.addFestivalTime(12, 30, 0, 0);
		eventItem7.addFestivalTime(13, 0, 0, 0);
		eventItem7.addFestivalTime(16, 20, 0, 0);
		eventItems.add(eventItem12);	
		
		EventItem eventItem13	= new EventItem("感知线索剧场");
		eventItem13.setPlace(0,0, 8);
		eventItem13.addAllTime(9, 30, 10, 0);
		eventItem13.addAllTime(10, 0, 10, 30);
		eventItem13.addAllTime(10, 30, 11, 0);
		eventItem13.addAllTime(11, 0, 11, 30);	
		eventItem13.addAllTime(11, 30, 12, 0);
		eventItem13.addAllTime(13, 30, 14, 0);
		eventItem13.addAllTime(14, 0, 14, 30);
		eventItem13.addAllTime(14, 30, 15, 0);
		eventItem13.addAllTime(15, 0, 15, 30);
		eventItem13.addAllTime(15, 30, 16, 0);
		eventItem13.addFestivalTime(12, 0, 12, 30);
		eventItem13.addFestivalTime(12, 30, 13, 30);
		eventItem13.addFestivalTime(16, 0, 16, 30);		
		eventItems.add(eventItem13);
						
	}
	
	private static void addFestivalDay(String strDt) { 
	    // 2012-02-24  
	    Date dt = java.sql.Date.valueOf(strDt);
	    
	    festivalDays.add(dt);		
	}

		
	private static boolean isFestivalDay(Date dt) {
		
		for (Date tmpDt:festivalDays) {						
			if (tmpDt == dt) return true; 
		}			
		
		return false;
	}
	
	public static String getPlace(int placeNo){
		
		return placesMap.get(placeNo);
	}
	
	public int[] getEventPlaces(){		
		int[] places = new int[MaxPlaceNo];;

    	for (int j=0; j<MaxPlaceNo; j++) {
    		places[j] = -1;
    	}       
        
        int addedNum = 0;
        for (EventItem et:eventItems){        	
       		
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
	
	private static EventItem getEventByTitle(String title){
		
		for (EventItem et : eventItems){
			
			if (et.getTitle().equals(title)){
				return et;
			}
		}
		
		return null;
	}
	
	public static ArrayList<ScheduleTime> getEventTodayTime(String title){
		
		EventItem et = getEventByTitle(title);
		
		if (et != null){
			return getEventTodayTime(et);
		}
		
		return null;
	}

	private static ArrayList<ScheduleTime> getEventTodayTime(EventItem et) {
		
		
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
	
	public String getEventTimeInfo(EventItem ei) {				
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
	
	public ArrayList<EventItem> getTodayEventListByPlace(int placeNo ){
		ArrayList<EventItem> todayEvent = new ArrayList<EventItem>();
		
		for (EventItem ei:eventItems) {
			if (ei.getPlaceNo() == placeNo) {
				todayEvent.add(ei);
			}
		}
		
		return todayEvent;
	}
	
	public static ArrayList<EventItem> getTodayEventListByTime(int fromHour, int toHour ){
		ArrayList<EventItem> todayEvent = new ArrayList<EventItem>();
		ArrayList<ScheduleTime> timeList;
		
		for (EventItem ei:eventItems) {
			
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
	
	public static ArrayList <ScheduleTime> getEventAlarmTimeOfToday(EventItem ei){
		
		ArrayList<ScheduleTime> timeList = getEventTodayTime(ei);
		ArrayList<ScheduleTime> alarmList = new ArrayList<ScheduleTime>();
		
		for (ScheduleTime et: timeList){
			if (et.getAlarmStatus()){
				alarmList.add(et);
			}
		}
		
		return alarmList;
	}
		
	public static ArrayList <EventItem> getEventListByAlarm(){
		
		ArrayList<EventItem> events = new ArrayList<EventItem>();
		ArrayList<ScheduleTime> timeList;
		
		for (EventItem ei:eventItems){
			timeList = getEventAlarmTimeOfToday(ei);
			
			if (!timeList.isEmpty()){
				events.add(ei);
			}
		}
		
		return events;
	}

}
