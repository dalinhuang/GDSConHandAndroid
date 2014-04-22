package com.winjune.wifiindoor.poi;

import java.util.ArrayList;

public class BusLine {
	private int stationId; //POI id
		
	private String lineName;
	private String startTime;
	private String endTime;
	private String priceInfo;
	
	private ArrayList<String> stopList;
	
	public BusLine(int id, String name, String startTime, String endTime, String price, String stopInfo) {
		this.stationId = id;
		this.lineName = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.priceInfo = price;
		
		String[] stops = stopInfo.split(";");
		
		stopList = new ArrayList<String>();
		
		for (int i=0; i<stops.length; i++)
			stopList.add(stops[i]);
	}
	
	public String getLineName(){
		return lineName;
	}
	
	public ArrayList<String>  getStopList(){
		return stopList;
	}
	
	public String getStartTime(){
		return startTime;
	}
	
	public String getEndTime(){
		return endTime;
	}
	
	public String getPriceInfo(){
		return priceInfo;
	}
	
	public String getStartEndInfo(){
		
		// normally the stop number should be bigger than 1.
		if (stopList.size() > 1) {
			
			String info = "(";
			info += stopList.get(0);
			info += "-";
			info += stopList.get(stopList.size()-1);
			info +=")";
			
			return info;
		}
		
		return null;
	}
	
}
