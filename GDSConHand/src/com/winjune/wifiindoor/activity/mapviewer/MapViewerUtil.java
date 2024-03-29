package com.winjune.wifiindoor.activity.mapviewer;

import java.util.ArrayList;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.ScreenCapture;
import org.andengine.entity.util.ScreenCapture.IScreenCaptureCallback;

import android.content.res.Configuration;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.GraphicMapListener;
import com.winjune.wifiindoor.drawing.SoundMapListener;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.drawing.graphic.model.LocationSprite;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.ShareUtil;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;

public class MapViewerUtil {
	

	public static void initData(MapViewerActivity mapViewer) {

		mapViewer.backgroundSprite = null;
		
		mapViewer.currentCollectingX = -1;
		mapViewer.currentCollectingY = -1;
		LocateBar.setCollectingOnGoing(mapViewer, false);
		mapViewer.mMode = IndoorMapData.MAP_MODE_VIEW;
		mapViewer.mNfcEditState = IndoorMapData.NFC_EDIT_STATE_NULL;
		mapViewer.mTargetColNo = -1;
		mapViewer.mTargetRowNo = -1;
		mapViewer.periodicLocateMeOn = true;
		mapViewer.periodicLoacting = false;
				
		mapViewer.lastManualLocateTime = System.currentTimeMillis();
		mapViewer.lastBackTime = mapViewer.lastManualLocateTime - 6000;
		
		mapViewer.reDrawPending = false;
		mapViewer.reDrawOn = true;
		mapViewer.reDrawOngoing = false;

		mapViewer.infoQueryToast = Toast.makeText(mapViewer,
				mapViewer.getResources().getString(R.string.no_latest_info),
				Toast.LENGTH_LONG);
		mapViewer.infoQueryToast.setMargin(0, 0);
		mapViewer.infoQueryToast.setGravity(Gravity.TOP, 0, 0);
		
		mapViewer.continuousCollectStartTime = 0;
		mapViewer.continuousCollectStopTime = 0;
		mapViewer.mContStartRowNo = -1;
		mapViewer.mContStartColNo = -1;
		mapViewer.mContStarted = false;
		
		
		// Get the display		
		mapViewer.mOrientation = mapViewer.getResources().getConfiguration().orientation;	
		
		int CONTROL_BUTTON_NUMBER = Library.CONTROL_BUTTON_NUMBER;
		mapViewer.CONTROL_BUTTON_WIDTH = 30;
		mapViewer.CONTROL_BUTTON_MARGIN = 10;
		
		// Ensure the ICON is not too small on large screen
		int MIN_VALUE = Math.max(60, Math.round(Math.min(Util.getCameraWidth(), Util.getCameraHeight())/10));

		if (mapViewer.mOrientation == Configuration.ORIENTATION_PORTRAIT) {			
			mapViewer.BOTTOM_SPACE += VisualParameters.BOTTOM_SPACE_FOR_ADS_PORTRAIT;
			mapViewer.CONTROL_BUTTON_WIDTH = mapViewer.CONTROL_BUTTON_HEIGHT 
					= Math.min(MIN_VALUE, Math.round((Util.getCameraWidth() - VisualParameters.BOTTOM_SPACE_FOR_ADS_PORTRAIT) / CONTROL_BUTTON_NUMBER / 1.5f));
			// Here use 3f to let the control tab layout on the top of height
			mapViewer.CONTROL_BUTTON_MARGIN = Math.min(MIN_VALUE, Math.round ((Util.getCameraHeight() - VisualParameters.BOTTOM_SPACE_FOR_ADS_PORTRAIT - mapViewer.CONTROL_BUTTON_HEIGHT * CONTROL_BUTTON_NUMBER) / CONTROL_BUTTON_NUMBER / 3f)); 
		} else if (mapViewer.mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			// remove the right margin
			// RIGHT_SPACE += VisualParameters.RIGHT_SPACE_FOR_ADS_LANDSCAPE;
			mapViewer.CONTROL_BUTTON_WIDTH = mapViewer.CONTROL_BUTTON_HEIGHT 
					= Math.min(MIN_VALUE, Math.round((Util.getCameraHeight()) / CONTROL_BUTTON_NUMBER / 1.5f));
			mapViewer.CONTROL_BUTTON_MARGIN = Math.min(MIN_VALUE, Math.round ((Util.getCameraHeight() - mapViewer.CONTROL_BUTTON_HEIGHT * CONTROL_BUTTON_NUMBER) / CONTROL_BUTTON_NUMBER / 3f));// Here use 3f to let the control tab layout on the top of height
		}		
				
		mapViewer.poiLabels = new ArrayList<Text>();
		mapViewer.poiIcons = new ArrayList<Sprite>();;
		mapViewer.focusPlace = null;
		mapViewer.locationPlaces = new ArrayList<LocationSprite>();
		mapViewer.collectedFlags = new ArrayList<Rectangle>(); 
		mapViewer.naviLines = new ArrayList<Line>();
		mapViewer.naviIcons = new ArrayList<Sprite>();
		mapViewer.naviHints = new ArrayList<Text>();
	}	
	
	public static void resetCameraBounds(MapViewerActivity mapViewer, int mapWidth, int mapHeight) {
		int totalWidth, totalHeight;

		totalWidth = mapWidth + mapViewer.LEFT_SPACE + mapViewer.RIGHT_SPACE;
		totalHeight = mapHeight + mapViewer.TOP_SPACE + mapViewer.BOTTOM_SPACE;

		// To align with the Camera
		if (totalWidth < Util.getCameraWidth()) 
			totalWidth = Util.getCameraWidth();

		if (totalHeight < Util.getCameraHeight())
			totalHeight = Util.getCameraHeight();
		
		mapViewer.mCamera.setBounds(0, 0, totalWidth, totalHeight);
	}
	
	public static void initCamera(MapViewerActivity mapViewer, float zoomFactor, int mapWidth, int mapHeight) {
		
		mapViewer.TOP_SPACE = 0;
		mapViewer.BOTTOM_SPACE = 0;
		mapViewer.LEFT_SPACE = 0;
		mapViewer.RIGHT_SPACE = 0;	

		mapViewer.mCamera = new ZoomCamera(0, 0, Util.getCameraWidth(), Util.getCameraHeight());
		
		// Original zoom factor
		mapViewer.mCamera.setZoomFactor(zoomFactor);

		resetCameraBounds(mapViewer, mapWidth,  mapHeight);
		
		mapViewer.mCamera.setBoundsEnabled(true);
		
	}
	
		
	public static ZoomCamera getMCamera(MapViewerActivity mapViewer) {
		return mapViewer.mCamera;
	}

	public static int getMode(MapViewerActivity mapViewer) {
		return mapViewer.mMode;
	}	
	
	public static boolean handleSingleTap(final MapViewerActivity mapViewer, MotionEvent event) {
		float zoomFactor = mapViewer.mCamera.getZoomFactor();
		float centerX = mapViewer.mCamera.getCenterX();
		float centerY = mapViewer.mCamera.getCenterY();
		float width = mapViewer.mCamera.getWidth();
		float height = mapViewer.mCamera.getHeight();
		float x = event.getX() / zoomFactor + centerX - width / 2;
		float y = event.getY() / zoomFactor + centerY - height / 2;
		int longitudeX = (int)(0.5+x*Util.getRuntimeMap().getMaxLongitude()/Util.getRuntimeMap().getMapWidth());
		int latitudeY = (int)(0.5+y*Util.getRuntimeMap().getMaxLatitude()/Util.getRuntimeMap().getMapHeight());
		
		if(VisualParameters.PLANNING_MODE_ENABLED){
	    	MapHUD.updateHinText(mapViewer, "longitudeX:"+longitudeX +" latitudeY:"+latitudeY);
		}
			
		final PlaceOfInterest poi = POIManager.getNearestPOI(Util.getRuntimeMap().getMapId(), longitudeX, latitudeY);
		
		if (poi == null)		
			return false;
		
		// need to clear all location spites first
		SearchBar.clearLocationPlaces(mapViewer);
		
		LocateBar.attachLocationSprite(mapViewer, poi);
		
		mapViewer.runOnUiThread(new Runnable() {    
            public void run() {    
            	poi.showContextMenu(mapViewer.getCurrentFocus());	
            }        
        });    		
		
		return true;		
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

		int colNo = (int) ((x - mapViewer.LEFT_SPACE) / Util.getRuntimeMap().getCellPixel());
		int rowNo = (int) ((y - mapViewer.TOP_SPACE) / Util.getRuntimeMap().getCellPixel());

		// Out of Upper Bound
		if ((colNo >= Util.getRuntimeMap().getColNum())
				|| (rowNo >= Util.getRuntimeMap().getRowNum())) {
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
		mapViewer.graphicListener.locate(Util.getRuntimeMap(), colNo, rowNo, Constants.TARGET_USER, 0);
		MapHUD.updateHinText(mapViewer, mapViewer.getResources().getString(R.string.current_selected_location) + " @[" + colNo + "," + rowNo + "]");
		
		// Set for next Action
		mapViewer.mTargetColNo = colNo;
		mapViewer.mTargetRowNo = rowNo;
	}
	
	public static void exitApp() {				
		
		System.exit(0);		
	}	
	
	public static void addListeners(MapViewerActivity mapViewer){
		// Listeners
		mapViewer.graphicListener = new GraphicMapListener(mapViewer, mapViewer.mainScene);
		mapViewer.graphicListener.setOffsetX(mapViewer.LEFT_SPACE);
		mapViewer.graphicListener.setOffsetY(mapViewer.TOP_SPACE);
		Util.getRuntimeMap().addListener(mapViewer.graphicListener);
		Util.getRuntimeMap().addListener(new SoundMapListener());		
	}	
	
	public static void captureScreenToShare(final MapViewerActivity mapViewer) {
	
		ScreenCapture screenCapture = new ScreenCapture();
		String fileName = Util.getFilePath(IndoorMapData.IMG_FILE_PATH_LOCAL) + "snapshot.png";//
				        
        /* Attaching the ScreenCapture to the end. */
        mapViewer.mainScene.attachChild(screenCapture);
                
        screenCapture.capture(Util.getCameraWidth(), Util.getCameraHeight(), fileName, new IScreenCaptureCallback() {
        	@Override
        	public void onScreenCaptured(final String pFilePath) {
        		Util.showToast(mapViewer,  "Screenshot captured sucessfully!", Toast.LENGTH_SHORT); 
        	}

            @Override
            public void onScreenCaptureFailed(final String pFilePath, final Exception pException) {
            	Util.showToast(mapViewer,  "Failed to capture screenshot!", Toast.LENGTH_SHORT);
            }
        });        
	}	
}
