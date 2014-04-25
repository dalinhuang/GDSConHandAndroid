package com.winjune.wifiindoor.poi;

import java.util.ArrayList;

public class ScheduleTime {
	public int fromHour;
	public int fromMin;
	public int toHour;
	public int toMin;
	
	private boolean alarmAdded;
	
	public static int OPEN_HOUR 	= 9; // open time clock
	public static int OPEN_MIN 		= 0;	
	public static int CLOSE_HOUR 	= 17; // close time clock
	public static int CLOSE_MIN 	= 0;
	
	public ScheduleTime(){
		this.fromHour = 0;
		this.fromMin = 0;
		this.toHour = 0;
		this.toMin = 0;	
	}
	
	// supported format: 10:00 - 11:00;	
	public void fromString(String scheduleStr){
		
		String[] splittedTime = scheduleStr.trim().split("-");		
		
		String[] fromTimeStr = splittedTime[0].trim().split(":");		
		if (fromTimeStr.length !=2)
			return;
		
		this.fromHour = Integer.parseInt(fromTimeStr[0].trim());
		this.fromMin = Integer.parseInt(fromTimeStr[1].trim());
		
		if (splittedTime.length > 1) {
			String[] toTimeStr = splittedTime[1].trim().split(":");
			
			if (toTimeStr.length !=2)
				return;
			
			this.toHour = Integer.parseInt(toTimeStr[0].trim());
			this.toMin = Integer.parseInt(toTimeStr[1].trim());
		}
				
	}
	
	public static void addScheduleList(String schedulesStr, ArrayList<ScheduleTime> scheduleList){
		String[] schedules = schedulesStr.split(";");
						
		for (String scheduleStr: schedules) {
			ScheduleTime scheduleItem = new ScheduleTime();
			scheduleItem.fromString(scheduleStr);
			scheduleList.add(scheduleItem);
		}						
	}	
	
	public ScheduleTime (int fromHour, int fromMin, int toHour, int toMin) {		
		this();

		if ((fromHour < 0) || (fromHour > 23))
			return;
		if ((toHour < 0) || (toHour > 23))
			return;
		if ((fromMin < 0) || (fromMin > 59))
			return;
		if ((toMin < 0) || (toMin > 59))
			return;		
		
		this.fromHour = fromHour;
		this.fromMin = fromMin;
		this.toHour = toHour;
		this.toMin = toMin;		
		
		alarmAdded = false;
		
		return;
	}
		
	public String toString() {
		String tmpStr ="";
		tmpStr += fromHour + ":";		
		if ( fromMin <10)
			tmpStr += "0" + fromMin;
		else
			tmpStr += fromMin;

		// toHour == 0 means no end time
		// so we don't display it
		if (toHour > 0){
			tmpStr += " - ";
			tmpStr += toHour + ":";
			if ( toMin <10)
				tmpStr = tmpStr + "0" + toMin;
			else
				tmpStr += toMin;
		}
			
		return tmpStr;
	}
	
	public String getStartTime(){
		String tmpStr = "";
		
		tmpStr += fromHour + ":";		
		if ( fromMin <10)
			tmpStr += "0" + fromMin;
		else
			tmpStr += fromMin;
		
		return tmpStr;
	}
	
	public String getEndTime(){
		String tmpStr = "";
		
		if (toHour > 0){
			tmpStr += toHour + ":";
			if ( toMin <10)
				tmpStr += "0" + toMin;
			else
				tmpStr += toMin;
		}
		
		return tmpStr;
	}
	
	public boolean getAlarmStatus(){
		return alarmAdded;
	}
	
	public void setAlarmStatus(boolean status){
		alarmAdded = status;
	}
}
