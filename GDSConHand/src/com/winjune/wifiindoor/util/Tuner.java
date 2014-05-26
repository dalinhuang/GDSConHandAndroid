package com.winjune.wifiindoor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Tuner {
	private static boolean initialed = false;
	private static Properties properties;
	
	public static void initial(){
		
		if (initialed) {
			return;
		}

		initialed = true;
		
		if (loadConfig()) {
			syncToConfig();
		}
	}
	
	public static void syncToConfig() {
		if (properties == null) {
			return;
		}
		
		String key = null;
		String value = null;
		
		// Part I:
		
		key = "PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR";
		value = properties.getProperty(key);		
		if (value != null) {
			if (value.trim().equalsIgnoreCase("true")){
				IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR = true;
			} else {
				IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR = false;
			}
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR));
		}
		
		key = "PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER";
		value = properties.getProperty(key);		
		if (value != null) {
			if (value.trim().equalsIgnoreCase("true")){
				IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER = true;
			} else {
				IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER = false;
			}
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER));
		}
		
		key = "PERIODIC_WIFI_CAPTURE_INTERVAL";
		value = properties.getProperty(key);		
		if (value != null) {
			IndoorMapData.PERIODIC_WIFI_CAPTURE_INTERVAL = Long.parseLong(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.PERIODIC_WIFI_CAPTURE_INTERVAL));
		}
		
		key = "MAX_AGE_FOR_WIFI_SAMPLES";
		value = properties.getProperty(key);		
		if (value != null) {
			IndoorMapData.MAX_AGE_FOR_WIFI_SAMPLES = Long.parseLong(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.MAX_AGE_FOR_WIFI_SAMPLES));
		}
		
		if (IndoorMapData.PERIODIC_WIFI_CAPTURE_INTERVAL != 0){
			IndoorMapData.MAX_WIFI_SAMPLES_BUFFERED = (int) (IndoorMapData.MAX_AGE_FOR_WIFI_SAMPLES / IndoorMapData.PERIODIC_WIFI_CAPTURE_INTERVAL);
		}
		
		key = "MIN_WIFI_SAMPLES_BUFFERED";
		value = properties.getProperty(key);		
		if (value != null) {
			IndoorMapData.MIN_WIFI_SAMPLES_BUFFERED = Integer.parseInt(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.MIN_WIFI_SAMPLES_BUFFERED));
		}		
		
		// Part II:
		
		key = "PERIODIC_LOCATE_INTERVAL";
		value = properties.getProperty(key);		
		if (value != null) {
			IndoorMapData.PERIODIC_LOCATE_INTERVAL = Long.parseLong(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.PERIODIC_LOCATE_INTERVAL));
		}
		
		key = "PERIODIC_WIFI_SCAN_INTERVAL";
		value = properties.getProperty(key);		
		if (value != null) {
			IndoorMapData.PERIODIC_WIFI_SCAN_INTERVAL = Long.parseLong(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.PERIODIC_WIFI_SCAN_INTERVAL));
		}
		
		// Part III:
		
		key = "MAX_FINGERPRINTS_FOR_LOCATE";
		value = properties.getProperty(key);		
		if (value != null) {
			IndoorMapData.MAX_FINGERPRINTS_FOR_LOCATE = Integer.parseInt(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.MAX_FINGERPRINTS_FOR_LOCATE));
		}
		
		key = "MAX_WAIT_MS_FOR_LOCATE";
		value = properties.getProperty(key);		
		if (value != null) {
			IndoorMapData.MAX_WAIT_MS_FOR_LOCATE = Long.parseLong(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.MAX_WAIT_MS_FOR_LOCATE));
		}
		
		key = "MAX_FINGERPRINTS_FOR_COLLECT";
		value = properties.getProperty(key);		
		if (value != null) {
			IndoorMapData.MAX_FINGERPRINTS_FOR_COLLECT = Integer.parseInt(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.MAX_FINGERPRINTS_FOR_COLLECT));
		}
		
		key = "MAX_WAIT_MS_FOR_COLLECT";
		value = properties.getProperty(key);		
		if (value != null) {
			IndoorMapData.MAX_WAIT_MS_FOR_COLLECT = Long.parseLong(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.MAX_WAIT_MS_FOR_COLLECT));
		}
		
		// Part IV:
		
		key = "MIN_DBM_COUNT_IN";
		value = properties.getProperty(key);		
		if (value != null) {
			IndoorMapData.MIN_DBM_COUNT_IN = Integer.parseInt(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.MIN_DBM_COUNT_IN));
		}
		
		key = "MIN_AP_COUNT_IN";
		value = properties.getProperty(key);		
		if (value != null) {
			IndoorMapData.MIN_AP_COUNT_IN = Integer.parseInt(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(IndoorMapData.MIN_AP_COUNT_IN));
		}
		
		// Part V:
		key = "DEBUG";
		value = properties.getProperty(key);
		if (value != null) {
			if (value.trim().equalsIgnoreCase("true")){
				WifiIpsSettings.DEBUG = true;
			} else {
				WifiIpsSettings.DEBUG = false;
			}
		} else {
			properties.setProperty(key, String.valueOf(WifiIpsSettings.DEBUG));
		}
		
		
		key = "PRIMARY_SERVER";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.PRIMARY_SERVER = value.trim();
		} else {
			properties.setProperty(key, WifiIpsSettings.PRIMARY_SERVER);
		}
		
		key = "SECONDARY_SERVER";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.SECONDARY_SERVER = value.trim();
		} else {
			properties.setProperty(key, WifiIpsSettings.SECONDARY_SERVER);
		}
		
		key = "SERVER_PORT";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.SERVER_PORT = value.trim();
		} else {
			properties.setProperty(key, WifiIpsSettings.SERVER_PORT);
		}
		
		key = "CONNECTION_TIMEOUT";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.CONNECTION_TIMEOUT = Integer.parseInt(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(WifiIpsSettings.CONNECTION_TIMEOUT));
		}
		
		key = "SOCKET_TIMEOUT";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.SOCKET_TIMEOUT = Integer.parseInt(value.trim());
		} else {
			properties.setProperty(key, String.valueOf(WifiIpsSettings.SOCKET_TIMEOUT));
		}
		
		key = "SERVER_SUB_DOMAIN";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.SERVER_SUB_DOMAIN = value.trim();
		} else {
			properties.setProperty(key, WifiIpsSettings.SERVER_SUB_DOMAIN);
		}
		
		key = "URL_PREFIX";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.URL_PREFIX = value.trim();
		} else {
			properties.setProperty(key, WifiIpsSettings.URL_PREFIX);
		}
		
		key = "URL_API_LOCATE";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.URL_API_LOCATE = value.trim();
		} else {
			properties.setProperty(key, WifiIpsSettings.URL_API_LOCATE);
		}
		
		key = "URL_API_COLLECT";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.URL_API_COLLECT = value.trim();
		} else {
			properties.setProperty(key, WifiIpsSettings.URL_API_COLLECT);
		}
		
		key = "URL_API_QUERY";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.URL_API_QUERY = value.trim();
		} else {
			properties.setProperty(key, WifiIpsSettings.URL_API_QUERY);
		}
		
		key = "URL_API_NFC_COLLECT";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.URL_API_NFC_COLLECT = value.trim();
		} else {
			properties.setProperty(key, WifiIpsSettings.URL_API_NFC_COLLECT);
		}
		
		key = "URL_API_LOCATE_BASE_NFC";
		value = properties.getProperty(key);
		if (value != null) {
			WifiIpsSettings.URL_API_LOCATE_BASE_NFC = value.trim();
		} else {
			properties.setProperty(key, WifiIpsSettings.URL_API_LOCATE_BASE_NFC);
		}
		
		// Part VI:

		key = "PLANNING_MODE_ENABLED";
		value = properties.getProperty(key);
		if (value != null) {
			if (value.trim().equalsIgnoreCase("true")){
				VisualParameters.PLANNING_MODE_ENABLED = true;
			} else {
				VisualParameters.PLANNING_MODE_ENABLED = false;
			}
		} else {
			properties.setProperty(key, String.valueOf(VisualParameters.PLANNING_MODE_ENABLED));
		}
		
		key = "ZOOM_SWITCH_ENABLED";
		value = properties.getProperty(key);
		if (value != null) {
			if (value.trim().equalsIgnoreCase("true")){
				VisualParameters.ZOOM_SWITCH_ENABLED = true;
			} else {
				VisualParameters.ZOOM_SWITCH_ENABLED = false;
			}
		} else {
			properties.setProperty(key, String.valueOf(VisualParameters.ZOOM_SWITCH_ENABLED));
		}		
		
		
		key = "ADS_ENABLED";
		value = properties.getProperty(key);
		if (value != null) {
			if (value.trim().equalsIgnoreCase("true")){
				VisualParameters.ADS_ENABLED = true;
			} else {
				VisualParameters.ADS_ENABLED = false;
			}
		} else {
			properties.setProperty(key, String.valueOf(VisualParameters.ADS_ENABLED));
		}
	
		key = "DEFAULT_MAP_id";
		value = properties.getProperty(key);
		if (value != null) {
			VisualParameters.DEFAULT_MAP_ID =  Integer.parseInt(value.trim());
				
		} else {
			properties.setProperty(key, String.valueOf(VisualParameters.DEFAULT_MAP_ID));
		}
					

	}

	//Load Configuration from file
	public static boolean loadConfig() {		
		if (properties == null) {
			properties = new Properties();
		}
		
		File file = Util.openOrCreateFileInPath(IndoorMapData.CONFIG_FILE_PATH, IndoorMapData.CONFIG_FILE_NAME, false);

	    if (file == null) {
	    	return false;
	    }
			
		try {
			FileInputStream s = new FileInputStream(file);
			properties.load(s);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// save Configuration to file
	public static boolean saveConfig() {
		File file = Util.openOrCreateFileInPath(IndoorMapData.CONFIG_FILE_PATH, IndoorMapData.CONFIG_FILE_NAME, false);

	    if (file == null) {
	    	return false;
	    }
			
		try {
			FileOutputStream s = new FileOutputStream(file);	
			properties.store(s, "CONFIGURATION FOR WIFI IPS");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public static Properties getProperties() {
		return properties;
	}

	public static void setProperties(Properties properties) {
		Tuner.properties = properties;
	}
}
