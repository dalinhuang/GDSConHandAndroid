package com.winjune.wifiindoor.poi;

import java.util.ArrayList;

import com.winjune.wifiindoor.map.FieldInfo;
import com.winjune.wifiindoor.map.MapInfo;

public class POIManager {
	
	public static ArrayList<PlaceOfInterest> POIList = new ArrayList<PlaceOfInterest>();
	
	
	
	public static PlaceOfInterest findNearestPOI(int mapId, int placeX, int PlaceY) {
	
		return POIList.get(0);
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
	

	
	
	public static PlaceOfInterest getPOI(int poiId) {
		
		return POIList.get(0);
	}	
	
	public static void addSamples(){
		BusStation aBusStation = new BusStation();
		BusLine aBusLine;
		aBusLine = new BusLine( aBusStation.id, // busStation POI id;
								"801路",
								"9:00", 
								"17:00",
								"票价:2元,月票通用",
								"大学城科学中心总站;广大公寓;广大生活区;广大;大学城枢纽站;华师;体育中心总站");
		
		aBusStation.addBusLine(aBusLine);

		aBusLine = new BusLine( aBusStation.id, // busStation POI id;
				"802路",
				"9:00", 
				"17:00",
				"票价:2元,月票通用",
				"大学城科学中心总站;广大公寓;广大生活区;广大;大学城枢纽站;华师;恒宝广场总站");

		aBusStation.addBusLine(aBusLine);	
		
		aBusLine = new BusLine( aBusStation.id, // busStation POI id;
				"803路",
				"9:00", 
				"17:00",
				"票价:4元,月票通用",
				"大学城科学中心总站;广大公寓;广大生活区;广大;大学城枢纽站;华师;黄沙大道总站");

		aBusStation.addBusLine(aBusLine);		
		
		
		
		POIList.add(aBusStation);
		
		
		TheatreInfo aTheatre = new TheatreInfo();
		POIList.add(aTheatre);		
					
	}
}
