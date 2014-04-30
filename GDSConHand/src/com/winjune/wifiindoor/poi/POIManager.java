package com.winjune.wifiindoor.poi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.winjune.wifiindoor.lib.poi.*;
import com.winjune.wifiindoor.map.FieldInfo;
import com.winjune.wifiindoor.map.InterestPlace;
import com.winjune.wifiindoor.map.InterestPlacesInfo;
import com.winjune.wifiindoor.map.MapInfo;
import com.winjune.wifiindoor.util.Util;

public class POIManager {
	
//	public static Map<POIType, Class> alias = new HashMap<POIType, Class>();
	
	public static ArrayList<PlaceOfInterest> POIList = new ArrayList<PlaceOfInterest>();
	
	
	public static void loadOfflineData(){
		/*alias.put(POIType.Normal, com.winjune.wifiindoor.poi.PlaceOfInterest.class);
		alias.put(POIType.BusStation, com.winjune.wifiindoor.poi.BusStation.class);
		alias.put(POIType.Playhouse, com.winjune.wifiindoor.poi.PlayhouseInfo.class);		
		try {
			PlaceOfInterest poi = (PlaceOfInterest) alias.get(myType).newInstance();
			
			POIList.add(poi);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/		
		
		PoiOfflineData offlineData = new PoiOfflineData(Util.getFilePath());
		
		offlineData.fromXML();
		
		// load POI data
		for (PlaceOfInterestR poiR: offlineData.poiTable.poiData) {
			PlaceOfInterest poi;
						
			switch (poiR.getPoiType()){
				case BusStation:{
					poi = new BusStation(poiR);
					break;
				}
				case Theatre:{
					poi = new TheatreInfo(poiR);
					break;					
				}
				case Restaurant:{
					poi = new RestaurantInfo(poiR);
					break;
				}
				case Playhouse: {
					poi = new PlayhouseInfo(poiR);
					break;
				}
				default: {
					poi = new TheatreInfo(poiR);
					break;
				}
			}
			
			POIList.add(poi);						
		}
		
		// load busLine info						
	}

	public static PlaceOfInterest findNearestPOI(int mapId, int placeX, int PlaceY) {
	
		return POIList.get(2);
	}
		
	public static String[] buildAutoCompleteText(){
        String[] labelArray = new String[0];
        ArrayList <String> labelList = new ArrayList<String>();

        for (PlaceOfInterest aPOI:POIList) {
        	if ((aPOI.label != null) && !(aPOI.label.isEmpty()))        		
        		labelList.add(aPOI.label);				
		}		
        
        labelArray = labelList.toArray(labelArray);
        
        return labelArray;
	}
	
	public static ArrayList<PlaceOfInterest> findPOIbyLabel(String text) {
		
		ArrayList<PlaceOfInterest> matchedPOIs = new ArrayList<PlaceOfInterest>();

		for (PlaceOfInterest poi: POIList) {
			
			if (poi.label != null) {
				if (poi.label.contains(text)) {
					matchedPOIs.add(poi);
				}
			}
		}
		
		return matchedPOIs;
	}
	
	public static PlaceOfInterest getPOIbyId(int poiId) {				
		return POIList.get(poiId);
	}	
	
	public static PlaceOfInterest getPOIbyTtsNo(int ttsNo) {
		for (PlaceOfInterest poi: POIList) {
			if (poi.ttsNo == ttsNo) {
				return poi;
			}
		}
		
		return null;
	}
	
		
	public static ArrayList<MovieInfo> getMovieListByAlarm(){
		 
		ArrayList<MovieInfo> movieList = new ArrayList<MovieInfo>();
		
		for (PlaceOfInterest poi : POIList){
			
			if (poi.getPoiType() == POIType.Theatre){
				
				TheatreInfo ti = (TheatreInfo) poi;
				ArrayList<MovieInfo> movies = ti.getMovies();
				
				for (MovieInfo mi : movies){
					
					if (mi.hasMovieAlarmToday()){
						movieList.add(mi);
					}
				}
			}
		}
		
		return movieList;
	}
	
	public static String getHallLabel(int hallId){
		
		return POIList.get(hallId).label;
	}	
	
	
	public static void mapinfo2Poi(MapInfo mapInfo){
		ArrayList<FieldInfo> mapInfos = mapInfo.getFields();
		PlaceOfInterest poi;
		
		for (FieldInfo mField: mapInfos ) {
			poi = new PlaceOfInterest();
			poi.alpha = mField.getAlpha();
			poi.label = mField.getInfo();
			poi.placeX = mField.getX();
			poi.placeY = mField.getY();
			poi.rotation = mField.getRotation();
			poi.scale = mField.getScale();
			poi.maxZoomFactor = mField.getMaxZoomFactor();
			poi.minZoomFactor = mField.getMinZoomFactor();
			poi.label = mField.getInfo();
			
			POIList.add(poi);
		}
		
	}
	
	public static void interestPlacesInfo2Poi(InterestPlacesInfo ipInfo){
		ArrayList<InterestPlace> mapInfos = ipInfo.getFields();
		PlaceOfInterest poi;
		
		for (InterestPlace mField: mapInfos ) {
			poi = new PlaceOfInterest();
			poi.detailedDesc = mField.getInfo();
			poi.placeX = mField.getX();
			poi.placeY = mField.getY();
			poi.ttsNo = mField.getSerial();
			poi.audioUrl = mField.getUrlAudio();
			poi.webUrl = mField.getUrlVideo();
			poi.picUrl = mField.getUrlPic();
			
			POIList.add(poi);
		}		
	}
		
}
