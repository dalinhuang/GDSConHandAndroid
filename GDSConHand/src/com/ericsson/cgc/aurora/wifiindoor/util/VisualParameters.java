package com.ericsson.cgc.aurora.wifiindoor.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import android.app.Activity;

public class VisualParameters {

	// Cancel these definition for flexible per Map defined CELL_WIDTH=CELL_HEIGHT
	//public static int CELL_WIDTH = 30; 
	//public static int CELL_HEIGHT = CELL_WIDTH;
		
	public static float CONTROL_BUTTON_ALPHA = 0.5f;
	
	public static float USER_PIN_ALPHA = 0.7f;
	public static float MAP_PIC_ALPHA = 0.7f;
	public static float MAP_FONT_ALPHA = 0.5f;
	public static float AD_PIC_ALPHA = 0.7f;
	
	public static int FONT_CHAR_WIDTH_MAPINFO = 12;
	public static int FONT_CHAR_WIDTH_MENU = 40;
	public static int FONT_CHAR_WIDTH_HINTS = 16;
	
	// Left Margins for ADS 
	public static int BOTTOM_SPACE_FOR_ADS_PORTRAIT = 120;  
	public static int RIGHT_SPACE_FOR_ADS_LANDSCAPE = 120;
	
	//public static int BOTTOM_SPACE_FOR_ADS_PORTRAIT = 120;
	//public static int BOTTOM_SPACE_FOR_TABHOST_BAR = 60;
	//public static int RIGHT_SPACE_FOR_ADS_LANDSCAPE = BOTTOM_SPACE_FOR_ADS_PORTRAIT;

	private static boolean initialed = false;

	public static float density = 1.0f;

	public static void initial(Activity activity) {

		if (initialed) {
			return;
		}

		initialed = true;
		
		//cancel the density (hardcode to 1.5f) so support the data be easy re-used between different devices/screens
		// DENSITY_HIGH 240 (dpi)
		// DENSITY_MEDIUM 160
		// DENSITY_LOW 120
		// DENSITY_DEFAULT 160
		//DisplayMetrics dm = new DisplayMetrics();
		//activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		//density = dm.density;			
		//density = 1.5f;

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
	}
}
