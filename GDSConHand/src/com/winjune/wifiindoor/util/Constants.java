package com.winjune.wifiindoor.util;

public final class Constants {
	public static int LAYER_INDEX = 0;
	public static final int LAYER_BACKGROUND = LAYER_INDEX++;
	public static final int LAYER_MAP = LAYER_INDEX++;
	public static final int LAYER_FLAG = LAYER_INDEX++;
	public static final int LAYER_USER = LAYER_INDEX++;
	public static final int LAYER_SEARCH = LAYER_INDEX++;
	public static final int LAYER_ROUTE = LAYER_INDEX++;

	public static final int LOCATION_USER = 0;
	public static final int TARGET_USER = 1;
	public static final int TRACK_USER = 2;
	
	public static final int MAX_TRACK_USERS = 30;
	
	
	public static final String ActionLocate = "ActionLocate";
	public static final String ActionSearch = "ActionSearch";
	public static final String ActionRoute = "ActionRoute";
	
	public static final String BUNDLE_KEY_POI_ID = "POI_ID";
	public static final String BUNDLE_RESULT_SEARCH_CONTEXT = "RESULT_SEARCH_CONTEXT";
	public static final String BUNDLE_LOCATION_CONTEXT = "LOCATION_CONTEXT";
	public static final String BUNDLE_KEY_NAVI_CONTEXT = "NAVI_CONTEXT";
		
	
}
