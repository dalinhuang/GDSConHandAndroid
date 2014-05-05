package com.winjune.wifiindoor.poi;


import java.io.Serializable;
import java.util.ArrayList;

public class SearchContext implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8473227740932225200L;
	public String searchText;		
	public int currentFocusIdx;
	public ArrayList<PlaceOfInterest> poiResults; // we only record the POI id
	
	public SearchContext(String text) {
		currentFocusIdx = 0;
		poiResults = new ArrayList<PlaceOfInterest>();
	}
	
}
