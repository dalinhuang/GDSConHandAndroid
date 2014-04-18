package com.winjune.wifiindoor.event;

public class EventTime {
	public int fromHour;
	public int fromMin;
	public int toHour;
	public int toMin;
	
	public static int OPEN_HOUR 	= 9; // open time clock
	public static int OPEN_MIN 		= 0;	
	public static int CLOSE_HOUR 	= 17; // close time clock
	public static int CLOSE_MIN 	= 0;
	
	
	public EventTime (int fromHour, int fromMin, int toHour, int toMin) {
		
		this.fromHour = 0;
		this.fromMin = 0;
		this.toHour = 0;
		this.toMin = 0;	

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
}
