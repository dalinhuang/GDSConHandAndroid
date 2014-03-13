package com.winjune.wifiindoor.util;

import com.winjune.wifiindoor.R;

public final class IndoorMapData {
	// Request Type send to Server, used to package the Fingerprint
	public static final int REQUEST_LOCATE = 0;
	public static final int REQUEST_COLLECT = 1;
	
	// Indoor Map mode
	public static final int MAP_MODE_VIEW = 0;
	public static final int MAP_MODE_EDIT = 1;
	public static final int MAP_MODE_EDIT_TAG = 2;
	public static final int MAP_MODE_DELETE_FINGERPRINT = 3;
	public static final int MAP_MODE_TEST_LOCATE = 4;
	public static final int MAP_MODE_TEST_COLLECT = 5;
	public static final int MAP_MODE_MAX=6;
	
	// NFC state
	public static final int NFC_EDIT_STATE_NULL = 0;
	public static final int NFC_EDIT_STATE_SCANNING = 1;
	public static final int NFC_EDIT_STATE_FINISH = 2;
	
	// Map Related Constants
	public static final String BUNDLE_KEY_MAP_INSTANCE = "MAP";
	public static final String BUNDLE_KEY_LOCATION_COL = "COLNO";
	public static final String BUNDLE_KEY_LOCATION_ROW = "ROWNO";
	public static final String BUNDLE_KEY_LOCATION_MAP = "MAPID";
	public static final String BUNDLE_KEY_LOCATION_MAP_VERSION = "MAP_VERSION";
	public static final String BUNDLE_KEY_REQ_FROM = "FROM";
	public static final String BUNDLE_KEY_BUILDING_MAPS = "BUILDING_MAPS";
	public static final String BUNDLE_KEY_MAP_INFO = "MAP_INFO";
	public static final String BUNDLE_KEY_LOCATION_INFO = "LOCATION_INFO";
	public static final String BUNDLE_KEY_NAVI_RESULT = "NAVI_RESULT";
	public static final String BUNDLE_KEY_INTEREST_PLACE_INSTANCE = "INTEREST_PLACE_INS";
	public static final String BUNDLE_KEY_INTEREST_PLACE_NO = "INTEREST_PLACE_NO";
	
	public static final int BUNDLE_VALUE_REQ_FROM_LOCATOR = 0;
	public static final int BUNDLE_VALUE_REQ_FROM_SELECTOR = 1;
	public static final int BUNDLE_VALUE_REQ_FROM_VIEWER = 2;
	public static final int BUNDLE_VALUE_REQ_FROM_BUILDING = 3;
	
	public static final int BUNDLE_VAL_INTEREST_REQ_FROM_TOUCH  = 1;
	public static final int BUNDLE_VAL_INTEREST_REQ_FROM_INPUT  = 2;
	
	public static final int MAP_FILE_DOWNLOADING = 0;
	public static final int MAP_FILE_UPDATED = 1;
	public static final int MAP_FILE_OUTDATED = 2;
	
	public static final int ATTR_NUMBER_PER_MAP_PIECE = 5; // left, top, right, bottom, name
	public static final int MAP_PIECE_ATTR_LEFT = 0;
	public static final int MAP_PIECE_ATTR_TOP = 1;
	public static final int MAP_PIECE_ATTR_WIDTH = 2;
	public static final int MAP_PIECE_ATTR_HEIGHT = 3;
	public static final int MAP_PIECE_ATTR_NAME = 4;
	
	public static final String CONFIG_FILE_PATH = "";
	public static final String APK_FILE_PATH_LOCAL = "apks/";
	public static final String MAP_FILE_PATH_LOCAL = "maps/";
	public static final String IMG_FILE_PATH_LOCAL = "img/";
	public static final String AUDIO_FILE_PATH_LOCAL = "audio/";
	public static final String APK_FILE_PATH_REMOTE = APK_FILE_PATH_LOCAL;
	public static final String MAP_FILE_PATH_REMOTE = MAP_FILE_PATH_LOCAL;
	public static final String IMG_FILE_PATH_REMOTE = IMG_FILE_PATH_LOCAL;
	public static final String AUDIO_FILE_PATH_REMOTE = AUDIO_FILE_PATH_LOCAL;
		
	public static final String CONFIG_FILE_NAME = "config.properties";	
	public static final String MAP_XML_NAME = "map.xml";
	public static final String MAP_MANAGER_FILE_NAME = "map_manager.xml";
	public static final String BUILDING_MANAGER_FILE_NAME = "building_manager.xml";
	public static final String MAP_INFO_FILE_NAME = "map_info.xml";
	public static final String MAP_SEARCH_INFO_FILE_NAME = "map_search_info.xml";
	public static final String NAVI_INFO_FILE_NAME = "navigator.xml";
	public static final String INTEREST_PLACE_INFO_FILE_NAME = "interest_places.xml";
	
	// Put all Map Sanity checking return codes here
	public static final int MAP_SANITY_RC_OK = 255;
	public static final int MAP_SANITY_RC_NO_INIT_MAP_DATA = 0;
	public static final int MAP_SANITY_RC_NO_MAP_FIELD_DATA = 1;
	public static final int[] MAP_SANITY_RC_ERROR_DESC = {
		R.string.map_sanity_rc_no_init_map_data,
		R.string.map_sanity_rc_no_map_field_data
		};
	
	// Put all file checking return codes here
	public static final int FILE_RC_OK = 255;
	public static final int FILE_RC_FILE_NOT_FOUND = 0;
	public static final int FILE_RC_IO_ERROR = 1;
	
	// Is this MAP CELL can be arrived
	public static final int UNKNOWN = -1;
	public static final int NO_WAY = 0;
	public static final int INNER_ROOM = 1;
	public static final int NEAR_DOOR = 2;
	public static final int PUBLIC_PATH = 3;		
	public static final int NEAR_ENTRY = 4;
	public static final int NEAR_EXIT = 5;
	
	// Some parms to control the Fingerprint collection
	public static int MAX_FINGERPRINTS_FOR_LOCATE = 20; // 20 groups
	public static long MAX_WAIT_MS_FOR_LOCATE = 6000;  // 6s
	public static int MAX_FINGERPRINTS_FOR_COLLECT = 20; 
	public static long MAX_WAIT_MS_FOR_COLLECT = 8000; // 8s
	public static int MIN_DBM_COUNT_IN = -105; // Not count in the weak signal below this value
	public static int MAX_DBM_COUNT_IN = -40; // Not count in the Strong signal above this value
	public static int MIN_AP_COUNT_IN = 6; // but at least report 6 APs
	
	public static long PERIODIC_LOCATE_INTERVAL = 120000; // Auto Locate every 2 minute
	public static long PERIODIC_WIFI_SCAN_INTERVAL = 1000; // Auto WIFI SCAN every 1 sec	
	public static long PERIODIC_WIFI_CAPTURE_INTERVAL = 1000; // Try to auto capture and save the WIFI information every 1 sec
	public static boolean PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR = true; // Keep capture the WIFI Fingerprint in background so we do not need to waiting a long time before locating/collecting
	public static boolean PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER = false; // Keep capture the WIFI Fingerprint in background so we do not need to waiting a long time before locating/collecting
	public static int MIN_WIFI_SAMPLES_BUFFERED = 3; // Start to locating at least with 3 samples
	public static long MAX_AGE_FOR_WIFI_SAMPLES = 10000; // Only use the buffers no older than 10s
	public static int MAX_WIFI_SAMPLES_BUFFERED = (int) (MAX_AGE_FOR_WIFI_SAMPLES / PERIODIC_WIFI_CAPTURE_INTERVAL); // Store Max to 100 samples in background
	
	
	public static final int HANDLER_STARTING_REQUEST = 101;
	public static final int HANDLER_FINISHING_REQUEST = 102;
	public static final int HANDLER_RESPONSE_RECEIVED = 103;
	public static final int HANDLER_ERROR_REPORTED = 104;
	
}
