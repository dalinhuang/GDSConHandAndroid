package com.winjune.wifiindoor.poi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.winjune.wifiindoor.activity.poiviewer.PlayhouseInfoActivity;
import com.winjune.wifiindoor.activity.poiviewer.TheatreInfoActivity;
import com.winjune.wifiindoor.lib.poi.PlaceOfInterestR;
import com.winjune.wifiindoor.lib.poi.PlayhouseInfoR;
import com.winjune.wifiindoor.lib.poi.ScheduleTime;

@SuppressWarnings("serial")
public class PlayhouseInfo extends PlaceOfInterest{
	
	private ArrayList<ScheduleTime> normalDayTime;
	private ArrayList<ScheduleTime> weekendTime;
	private ArrayList<ScheduleTime> festivalTime;
	
	public PlayhouseInfo(PlaceOfInterestR poi) {
		super(poi);
					
		normalDayTime = new ArrayList<ScheduleTime>();
		weekendTime = new ArrayList<ScheduleTime>();
		festivalTime = new ArrayList<ScheduleTime>();		
	}
	
	public void loadOfflineData(PlayhouseInfoR schedule){
		normalDayTime = schedule.getNormalDayTime();
		weekendTime = schedule.getWeekendTime();
		festivalTime = schedule.getFestivalTime();
	}
	
	public String getTodayScheduleInfo() {				
		       
        ArrayList<ScheduleTime> timeList = getTodaySchedule();
       		
		if (timeList == null)
			return null;
		
		String eventInfo = "";
		
		for ( ScheduleTime et: timeList){
			eventInfo += et.toString() + "  ";
		}
		
		return eventInfo;
	}	
	
	public ArrayList <ScheduleTime> getEventAlarmTimeOfToday(){
		
		ArrayList<ScheduleTime> timeList = getTodaySchedule();
		ArrayList<ScheduleTime> alarmList = new ArrayList<ScheduleTime>();
		
		for (ScheduleTime et: timeList){
			if (et.getAlarmStatus()){
				alarmList.add(et);
			}
		}
		
		return alarmList;
	}	
	
	public ArrayList<ScheduleTime> getTodaySchedule() {
		
		
		Date dt = new Date(0);;		
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0 )	dayOfWeek = 0;
        
        if (EventManager.isFestivalDay(dt)) {
        	return festivalTime;
        } else if ((dayOfWeek == 0) || (dayOfWeek == 6))  {
        	return weekendTime;        	
        }
        
        return normalDayTime;
  				
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
		
	public OnClickListener getBtnDetailClickListener(){
		return getContextBtn1ClickListener();	
	}
	
	public String getContextBtn1Label(){		
		return "今日排期";
	}
	
	public OnClickListener getContextBtn1ClickListener(){
		OnClickListener mBtnListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(v.getContext(), PlayhouseInfoActivity.class); 

				Bundle mBundle = new Bundle(); 
				mBundle.putInt(TheatreInfoActivity.BUNDLE_KEY_POI_ID, PlayhouseInfo.this.id);
				i.putExtras(mBundle); 	
				
		        v.getContext().startActivity(i);				
			}
		};
		
		return mBtnListener;			
	}		
}
