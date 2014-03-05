package com.winjune.wifiindoor.mapviewer;

import org.andengine.engine.camera.ZoomCamera;

import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;

import com.winjune.wifiindoor.MapViewerActivity;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;

public class MapViewerUtil {
	
	
	public static void initCamera(MapViewerActivity mapViewer) {
		// Get the display
		Display display = mapViewer.getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		
		mapViewer.mOrientation = mapViewer.getResources().getConfiguration().orientation;
		
		display.getMetrics(outMetrics);
		
		mapViewer.cameraWidth = outMetrics.widthPixels;
		mapViewer.cameraHeight = outMetrics.heightPixels;
		
		mapViewer.density = Math.min(mapViewer.cameraWidth, mapViewer.cameraHeight) / 480;

		int CONTROL_BUTTON_NUMBER = Library.CONTROL_BUTTON_NUMBER;
		int TAB_BUTTON_NUMBER = Library.TAB_BUTTON_NUMBER;
		
		mapViewer.CONTROL_BUTTON_WIDTH = 30;
		mapViewer.CONTROL_BUTTON_MARGIN = 10;
		
		// Ensure the ICON is not too small on large screen
		int MIN_VALUE = Math.max(60, Math.round(Math.min(mapViewer.cameraWidth, mapViewer.cameraHeight)/10));

		mapViewer.TAB_BUTTON_WIDTH = mapViewer.TAB_BUTTON_HEIGHT
			= Math.min(MIN_VALUE, Math.round(mapViewer.cameraWidth / TAB_BUTTON_NUMBER / 1.5f));
		// Here use 2 to let the TAB fill the whole width
		mapViewer.TAB_BUTTON_MARGIN = Math.round ((mapViewer.cameraWidth - mapViewer.TAB_BUTTON_WIDTH * TAB_BUTTON_NUMBER) / TAB_BUTTON_NUMBER / 2);  
		
		
		mapViewer.TOP_SPACE = 0;
		mapViewer.BOTTOM_SPACE = 0;
		mapViewer.LEFT_SPACE = 0;
		mapViewer.RIGHT_SPACE = 0;		

		if (mapViewer.mOrientation == Configuration.ORIENTATION_PORTRAIT) {			
			mapViewer.BOTTOM_SPACE += VisualParameters.BOTTOM_SPACE_FOR_ADS_PORTRAIT;
			mapViewer.CONTROL_BUTTON_WIDTH = mapViewer.CONTROL_BUTTON_HEIGHT 
					= Math.min(MIN_VALUE, Math.round((mapViewer.cameraHeight - mapViewer.TAB_BUTTON_HEIGHT - VisualParameters.BOTTOM_SPACE_FOR_ADS_PORTRAIT) / CONTROL_BUTTON_NUMBER / 1.5f));
			// Here use 3f to let the control tab layout on the top of height
			mapViewer.CONTROL_BUTTON_MARGIN = Math.min(MIN_VALUE, Math.round ((mapViewer.cameraHeight - mapViewer.TAB_BUTTON_HEIGHT - VisualParameters.BOTTOM_SPACE_FOR_ADS_PORTRAIT - mapViewer.CONTROL_BUTTON_HEIGHT * CONTROL_BUTTON_NUMBER) / CONTROL_BUTTON_NUMBER / 3f)); 
		} else if (mapViewer.mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			// remove the right margin
			// RIGHT_SPACE += VisualParameters.RIGHT_SPACE_FOR_ADS_LANDSCAPE;
			mapViewer.CONTROL_BUTTON_WIDTH = mapViewer.CONTROL_BUTTON_HEIGHT 
					= Math.min(MIN_VALUE, Math.round((mapViewer.cameraHeight - mapViewer.TAB_BUTTON_HEIGHT) / CONTROL_BUTTON_NUMBER / 1.5f));
			mapViewer.CONTROL_BUTTON_MARGIN = Math.min(MIN_VALUE, Math.round ((mapViewer.cameraHeight - mapViewer.TAB_BUTTON_HEIGHT - mapViewer.CONTROL_BUTTON_HEIGHT * CONTROL_BUTTON_NUMBER) / CONTROL_BUTTON_NUMBER / 3f));// Here use 3f to let the control tab layout on the top of height
		}	
		
		mapViewer.BOTTOM_SPACE += mapViewer.TAB_BUTTON_HEIGHT;
		
		// remove the right margin
		//RIGHT_SPACE += CONTROL_BUTTON_WIDTH;

		mapViewer.mCamera = new ZoomCamera(0, 0, mapViewer.cameraWidth, mapViewer.cameraHeight);
	}
	
	
	public static int getCenterColNo(MapViewerActivity mapViewer){
		int colNo;
		
		float centerX = mapViewer.mCamera.getCenterX();  

		colNo = (int) centerX / Util.getCurrentCellPixel();

		return colNo;	
	}
	
	public static int getCenterRowNo(MapViewerActivity mapViewer){
		int rowNo;
		
		float centerY = mapViewer.mCamera.getCenterY(); 
		
		rowNo = (int) centerY / Util.getCurrentCellPixel();
		
		return rowNo;
	}
	
	public static ZoomCamera getMCamera(MapViewerActivity mapViewer) {
		return mapViewer.mCamera;
	}

	public static int getMode(MapViewerActivity mapViewer) {
		return mapViewer.mMode;
	}	
	
	public static boolean handleTouchEvent(MapViewerActivity mapViewer, MotionEvent event) {

		if (mapViewer.gestureDetector.onTouchEvent(event)) {
			//Log.e("Touch", "gestureDetector.onTouchEvent");
			return true;
		}

		if (mapViewer.zoomGestureDector.onTouchEvent(event)) {
			//Log.e("Touch", "zoomGestureDector.onTouchEvent");
			return true;
		}

		return false;
	}
	
	public static void handleLongPress(MapViewerActivity mapViewer, MotionEvent e) {
		
		//long press is only enable in planning/debug mode
		if (!VisualParameters.PLANNING_MODE_ENABLED) {
			return;
		}
		
		float zoomFactor = mapViewer.mCamera.getZoomFactor();
		float centerX = mapViewer.mCamera.getCenterX();
		float centerY = mapViewer.mCamera.getCenterY();
		float width = mapViewer.mCamera.getWidth();
		float height = mapViewer.mCamera.getHeight();
		float x = e.getX() / zoomFactor + centerX - width / 2;
		float y = e.getY() / zoomFactor + centerY - height / 2;

		// Out of Lower Bound
		if ((x < mapViewer.LEFT_SPACE)
				|| (y < mapViewer.TOP_SPACE)) {
			//Util.showShortToast(this, R.string.out_of_map_bound);
			MapHUD.updateHinText(mapViewer, R.string.out_of_map_bound);
			return;
		}

		int colNo = (int) ((x - mapViewer.LEFT_SPACE) / Util.getRuntimeIndoorMap().getCellPixel());
		int rowNo = (int) ((y - mapViewer.TOP_SPACE) / Util.getRuntimeIndoorMap().getCellPixel());

		// Out of Upper Bound
		if ((colNo >= Util.getRuntimeIndoorMap().getColNum())
				|| (rowNo >= Util.getRuntimeIndoorMap().getRowNum())) {
			//Util.showShortToast(this, R.string.out_of_map_bound);
			MapHUD.updateHinText(mapViewer, R.string.out_of_map_bound);
			return;
		}

		// Vibrate
		if (Util.getVibrator() != null) {			
			// Nexus7 does not has a Vibrator but it get into our codes, let it play sounds
			if (Util.getDeviceName().trim().equalsIgnoreCase("Nexus 7")) {
				// Play sound, repeat 1 time
				mapViewer.medSound.setLoopCount(1);
				mapViewer.medSound.play();
			} else {
				Util.getVibrator().vibrate(500);
			}
		} else {
			// Play sound, repeat 1 time
			mapViewer.medSound.setLoopCount(1);
			mapViewer.medSound.play();
		}
		
		// Put a flag on the chosen cell
		mapViewer.graphicListener.locate(Util.getRuntimeIndoorMap(), colNo, rowNo, Constants.TARGET_USER, 0);
		MapHUD.updateHinText(mapViewer, mapViewer.getResources().getString(R.string.current_selected_location) + " @[" + colNo + "," + rowNo + "]");
		
		// Set for next Action
		mapViewer.mTargetColNo = colNo;
		mapViewer.mTargetRowNo = rowNo;
	}	
	
}
