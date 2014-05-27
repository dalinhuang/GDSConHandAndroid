package com.winjune.wifiindoor.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

@SuppressWarnings("unused")
public class VisualParameters {

	// Cancel these definition for flexible per Map defined CELL_WIDTH=CELL_HEIGHT
		
	public static float CONTROL_BUTTON_ALPHA = 0.5f;
	
	public static float USER_PIN_ALPHA = 0.7f;
	public static float MAP_PIC_ALPHA = 0.7f;
	public static float MAP_FONT_ALPHA = 0.5f;
	public static float AD_PIC_ALPHA = 0.7f;
	
	public static int FONT_CHAR_ABS_WIDTH_MAPINFO = 16;
	public static int FONT_CHAR_WIDTH_MENU = 40;
	public static int FONT_CHAR_WIDTH_HINTS = 24;
	
	// Left Margins for ADS 
	public static int BOTTOM_SPACE_FOR_ADS_PORTRAIT = 0; //120;  
	public static int RIGHT_SPACE_FOR_ADS_LANDSCAPE = 0; //120;
	
	//SOC for planning/debug mode
	public static boolean PLANNING_MODE_ENABLED = false;
	
	public static boolean ZOOM_SWITCH_ENABLED = false;
	
	// SOC for ADS
	public static boolean ADS_ENABLED = false;
	
	public static int DEFAULT_MAP_ID = 15;
	
	private static boolean initialed = false;


	public static void initial(Activity activity) {

		if (initialed) {
			return;
		}

		initialed = true;
	}
}
