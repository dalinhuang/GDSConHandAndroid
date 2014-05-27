package com.winjune.wifiindoor.poi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;

import com.winjune.wifiindoor.activity.poiviewer.BusStationInfoActivity;
import com.winjune.wifiindoor.activity.poiviewer.POINormalViewerActivity;
import com.winjune.wifiindoor.activity.poiviewer.PlayhouseInfoActivity;
import com.winjune.wifiindoor.activity.poiviewer.RestaurantInfoActivity;
import com.winjune.wifiindoor.activity.poiviewer.TheatreInfoActivity;
import com.winjune.wifiindoor.lib.poi.*;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;

public class POIManager {
	private static int idGenerator = 1000;

//	public static Map<POIType, Class> alias = new HashMap<POIType, Class>();
	public static FestivalT festivalData;
	public static ArrayList<PlaceOfInterest> POIList = new ArrayList<PlaceOfInterest>();
	
	
	public static void loadOfflineData(String path){
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
		
		PoiOfflineData offlineData = new PoiOfflineData(path);
		
		offlineData.fromXML();
		
		festivalData = offlineData.festivalTable;
				
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
					poi = new PlaceOfInterest(poiR);
					break;
				}
			}
			
			POIList.add(poi);						
		}
		
		// load busLine info			
		for (BusLineR aBusline: offlineData.buslineTable.getBusLines()) {
			if (getPOIbyId(aBusline.poiId) instanceof BusStation)
			{
				((BusStation)getPOIbyId(aBusline.poiId)).addBusLine(aBusline);
			}
			else
			{
				Log.e("POI", "Wrong BusStation POI with id:"+aBusline.poiId);
			}
		}
		
		// load restaurant info
		for (RestaurantInfoR menuItem: offlineData.restaurantTable.getMenus()) {
			if (getPOIbyId(menuItem.poiId) instanceof RestaurantInfo)
			{
				((RestaurantInfo)getPOIbyId(menuItem.poiId)).addMenuItem(menuItem);
			}
			else
			{
				Log.e("POI", "Wrong RestaurantInfo POI with id:"+menuItem.poiId);
			}
		}		
		
		// load movie info
		for (MovieInfoR aMovie: offlineData.movieTable.getMovies()) {
			if (getPOIbyId(aMovie.poiId) instanceof TheatreInfo)
			{
				((TheatreInfo)getPOIbyId(aMovie.poiId)).addMovie(aMovie);
			}
			else
			{
				Log.e("POI", "Wrong TheatreInfo POI with id:"+aMovie.poiId);
			}
		}			
		
		// load playhouse info
		for (PlayhouseInfoR mSchedule: offlineData.playhouseTable.getSchedules()) {
			if (getPOIbyId(mSchedule.poiId) instanceof PlayhouseInfo)
			{
				((PlayhouseInfo)getPOIbyId(mSchedule.poiId)).loadOfflineData(mSchedule);
			}
			else
			{
				Log.e("POI", "Wrong PlayhouseInfo POI with id:"+mSchedule.poiId);
			}
		}			
		
		
	}

	public static Class getPOIClass(POIType poiType) {
		
		switch (poiType){		
			case BusStation:{
				return  BusStation.class;				
			}
			case Theatre:{
				return TheatreInfo.class;				
			}
			case Restaurant:{
				return RestaurantInfo.class;				
			}
			case Playhouse: {
				return PlayhouseInfo.class;				
			}
			default: {
				return PlaceOfInterest.class;				
			}
		}
	}
	
	public static Class getPOIViewerClass(POIType poiType){
		
		switch (poiType){		
			case BusStation:{
				return  BusStationInfoActivity.class;				
			}
			case Theatre:{
				return TheatreInfoActivity.class;				
			}
			case Restaurant:{
				return RestaurantInfoActivity.class;				
			}
			case Playhouse: {
				return PlayhouseInfoActivity.class;				
			}
			default: {
				return POINormalViewerActivity.class;				
			}
		}		
		
	}
	
	public static PlaceOfInterest getNearestPOI(int mapId, int placeX, int placeY) {
		PlaceOfInterest nearestPoi = null;		
		double shortestDist = Double.MAX_VALUE;
		
		
		for (PlaceOfInterest aPOI:POIList) {
        	if (aPOI.mapId == mapId) {        		  
        		double distX =  Math.abs(aPOI.getPlaceX() - placeX);
        		double distY =  Math.abs(aPOI.getPlaceY() - placeY);
        		double mDist = distX*distX + distY*distY;
        		
        		if ( mDist < shortestDist) {
        			nearestPoi = aPOI;
        			shortestDist = mDist;
        		}        		        		 
        	}        	
		}					
		
		if (nearestPoi != null){
			// caculate the real distance based on px
			double realDist =  Math.sqrt(shortestDist) * Util.getRuntimeIndoorMap().getMapWidth()
							/Util.getRuntimeIndoorMap().getMaxLongitude();
			
			// 
			if (realDist > VisualParameters.POI_DISTANCE_THRESHOLD)
				nearestPoi = null; 			
		}
			
		return nearestPoi;
	}
		
	public static String[] buildAutoCompleteText(){
        String[] labelArray = new String[0];
        // use Set to avoid duplicated record
        Set<String> labelList = new HashSet<String>();

        for (PlaceOfInterest aPOI:POIList) {
        	if ((aPOI.label != null) && !(aPOI.label.isEmpty()))        		
        		labelList.add(aPOI.label);				
		}		
        
        labelArray = labelList.toArray(labelArray);
        
        return labelArray;
	}
	
	public static ArrayList<PlaceOfInterest> searchPOIsbyLabel(String text) {
		
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
	
	public static PlaceOfInterest getPOIbyLabel(String text) {
			

		for (PlaceOfInterest poi: POIList) {
			
			if (poi.label != null) {
				if (poi.label.equals(text)) {
					return poi;
				}
			}
		}
		
		return null;
	}	
	
	public static PlaceOfInterest getPOIbyId(int poiId) {		
		for (PlaceOfInterest poi: POIList) {
			if (poi.id == poiId) {
				return poi;
			}
		}
		
		return null;
	}	
	
	public static PlaceOfInterest getPOIbyTtsNo(int ttsNo) {
		for (PlaceOfInterest poi: POIList) {
			if (poi.ttsNo == ttsNo) {
				return poi;
			}
		}
		
		return null;
	}
	
		
	public static ArrayList<MovieInfoR> getMovieListByAlarm(){
		 
		ArrayList<MovieInfoR> movieList = new ArrayList<MovieInfoR>();
		
		for (PlaceOfInterest poi : POIList){
			
			if (poi.getPoiType() == POIType.Theatre){
				
				TheatreInfo ti = (TheatreInfo) poi;
				ArrayList<MovieInfoR> movies = ti.getMovies();
				
				for (MovieInfoR mi : movies){
					
					if (mi.hasMovieAlarmToday()){
						movieList.add(mi);
					}
				}
			}
		}
		
		return movieList;
	}
	
	public static String getHallLabel(int hallId){
		
		return getPOIbyId(hallId).label;
	}	
		
}
