package com.winjune.wifiindoor.poi;

import java.util.ArrayList;

import com.winjune.wifiindoor.map.FieldInfo;
import com.winjune.wifiindoor.map.MapInfo;

public class POIManager {
	
	public static ArrayList<PlaceOfInterest> POIList = new ArrayList<PlaceOfInterest>();
	
	
	
	public static PlaceOfInterest findNearestPOI(int mapId, int placeX, int PlaceY) {
	
		return null;
	}
	
	
	public static String[] buildAutoCompleteText(){
        String[] labelArray = new String[0];
        ArrayList <String> labelList = new ArrayList<String>();

        for (PlaceOfInterest aPOI:POIList) {				
				labelList.add(aPOI.getLabel());				
		}		
        
        labelArray = labelList.toArray(labelArray);
        
        return labelArray;
	}
	
	public static ArrayList<PlaceOfInterest> findPOIbyLabel(String text) {
		
		ArrayList<PlaceOfInterest> matchedPOIs = new ArrayList<PlaceOfInterest>();

		for (PlaceOfInterest poi: POIList) {
			if (poi.getLabel().contains(text)) {
				matchedPOIs.add(poi);
			}
		}
		
		return matchedPOIs;
	}
	
	public static void mapInfo2POI(int mapId, MapInfo mapInfo) {
		PlaceOfInterest aPOI = new  PlaceOfInterest();
		
		for (FieldInfo field: mapInfo.getFields()){
			aPOI.setMapId(mapId);
			aPOI.setX(field.getX());
			aPOI.setY(field.getY());
			aPOI.setLabel(field.getInfo());
			aPOI.setAlpha(field.getAlpha());
			aPOI.setScale(field.getScale());
			aPOI.setRotation(field.getRotation());
			aPOI.setMinZoomFactor(field.getMinZoomFactor());
			aPOI.setMaxZoomFactor(field.getMaxZoomFactor());
			
			POIList.add(aPOI);
		}		
	}
	
	
	public static void addSamples(){
		BusStation aBusStation = new BusStation();
		
		POIList.add(aBusStation);
		
		
		TheatreInfo aTheatre = new TheatreInfo();
		POIList.add(aTheatre);
				
		
	}
}
