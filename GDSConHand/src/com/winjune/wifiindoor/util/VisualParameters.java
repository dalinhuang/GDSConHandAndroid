package com.winjune.wifiindoor.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

@SuppressWarnings("unused")
public class VisualParameters {

	// Cancel these definition for flexible per Map defined CELL_WIDTH=CELL_HEIGHT
	//public static int CELL_WIDTH = 30; 
	//public static int CELL_HEIGHT = CELL_WIDTH;
		
	public static float CONTROL_BUTTON_ALPHA = 0.5f;
	
	public static float USER_PIN_ALPHA = 0.7f;
	public static float MAP_PIC_ALPHA = 0.7f;
	public static float MAP_FONT_ALPHA = 0.5f;
	public static float AD_PIC_ALPHA = 0.7f;
	
	public static int FONT_CHAR_ABS_WIDTH_MAPINFO = 16;
	public static int FONT_CHAR_WIDTH_MENU = 40;
	public static int FONT_CHAR_WIDTH_HINTS = 16;
	
	// Left Margins for ADS 
	public static int BOTTOM_SPACE_FOR_ADS_PORTRAIT = 0; //120;  
	public static int RIGHT_SPACE_FOR_ADS_LANDSCAPE = 0; //120;
	
	//SOC for planning/debug mode
	public static boolean PLANNING_MODE_ENABLED = false;
	
	public static boolean ZOOM_SWITCH_ENABLED = false;
	
	// SOC for ADS
	public static boolean ADS_ENABLED = false;
	// SOC for BANNER
	public static boolean BANNERS_ENABLED = false;
	
	// SOC for background lines
	public static boolean BACKGROUND_LINES_NEEDED = true;
	
	//public static int BOTTOM_SPACE_FOR_ADS_PORTRAIT = 120;
	//public static int BOTTOM_SPACE_FOR_TABHOST_BAR = 60;
	//public static int RIGHT_SPACE_FOR_ADS_LANDSCAPE = BOTTOM_SPACE_FOR_ADS_PORTRAIT;

	private static boolean initialed = false;

	private static float density = 1.0f;

	public static void initial(Activity activity) {

		if (initialed) {
			return;
		}

		initialed = true;
		
		/*
		Display display = activity.getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		
		display.getMetrics(outMetrics);
		
		int cameraWidth = outMetrics.widthPixels;
		int cameraHeight = outMetrics.heightPixels;
		
		density = Math.min(cameraWidth, cameraHeight) / 480;

		if (density <= 0) {
			density = 1;
		}

		//Log.d("VisualParameters", "density:" + density);

		if (density != 1) {
			Field[] fields = VisualParameters.class.getFields();

			for (Field field : fields) {
				if (Modifier.isStatic(field.getModifiers())
						&& field.getType().equals(int.class)) {
					try {
						int ori = field.getInt(null);
						field.setInt(null, (int) (ori * density));
						//Log.d("VisualParameters", "change " + field.getName() + " from " + ori
						//		+ " to " + field.getInt(null));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		*/
	}
}
