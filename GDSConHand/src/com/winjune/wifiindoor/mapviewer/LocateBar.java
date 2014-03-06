package com.winjune.wifiindoor.mapviewer;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.andengine.entity.primitive.Rectangle;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

import com.google.gson.Gson;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.MsgConstants;
import com.winjune.wifiindoor.webservice.types.CollectInfo;
import com.winjune.wifiindoor.webservice.types.Location;
import com.winjune.wifiindoor.webservice.types.LocationSet;
import com.winjune.wifiindoor.webservice.types.NfcLocation;
import com.winjune.wifiindoor.webservice.types.TestLocateCollectReply;
import com.winjune.wifiindoor.webservice.types.TestLocateCollectRequest;
import com.winjune.wifiindoor.wifi.WifiFingerPrint;

public class LocateBar {

	public static void setCurrentLocation(MapViewerActivity mapViewer) {
		updateLocation(mapViewer,
						Util.getRuntimeIndoorMap().getMapId(), 
						Util.getRuntimeIndoorMap().getVersionCode(), 
						mapViewer.mTargetColNo, 
						mapViewer.mTargetRowNo);
	}
	
	public static boolean isCollectingOnGoing(MapViewerActivity mapViewer) {
		return mapViewer.collectingOnGoing;
	}

	public static void setCollectingOnGoing(MapViewerActivity mapViewer, boolean collectingOnGoing) {
		mapViewer.collectingOnGoing = collectingOnGoing;
	}		
	
	public static void locateMe(final MapViewerActivity mapViewer, boolean periodic) {

		if (mapViewer.DEBUG)
			Log.d(mapViewer.TAG, "Start LocateMe");

		// Not run periodic locating when on Edit Mode 
		if (periodic && (mapViewer.mMode != IndoorMapData.MAP_MODE_VIEW)) {
			return;
		}

		if (periodic && isCollectingOnGoing(mapViewer)) {
			return;
		}

		if (isCollectingOnGoing(mapViewer)) {
			//Util.showShortToast(this, R.string.another_collect_ongoing);
			MapHUD.updateHinText(mapViewer, R.string.another_collect_ongoing);
			return;
		}

		if (!Util.getWifiInfoManager().isWifiEmbeded()) {
			//Util.showLongToast(this, R.string.no_wifi_embeded);
			MapHUD.updateHinText(mapViewer, R.string.no_wifi_embeded);
			return;
		}

		if (!Util.getWifiInfoManager().isWifiEnabled()) {
			//Util.showLongToast(this, R.string.no_wifi_enabled);
			MapHUD.updateHinText(mapViewer, R.string.no_wifi_enabled);
			return;
		}
		
		mapViewer.periodicLoacting = periodic;		
		//Util.showShortToast(this, R.string.locate_collecting);
		MapHUD.updateHinText(mapViewer, R.string.locate_collecting);
		
		if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR) {
			if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
				try {
					WifiFingerPrint fingnerPrint = Util.getWifiInfoManager().mergeSamples();				
					fingnerPrint.log();
					try {
						Gson gson = new Gson();
						String json = gson.toJson(fingnerPrint);
						JSONObject data = new JSONObject(json);
	
						if (Util.sendToServer(mapViewer, MsgConstants.MT_LOCATE, data)) {
							//Util.showShortToast(this, R.string.locate_collected);
							MapHUD.updateHinText(mapViewer, R.string.locate_collected);
						} else {
							// All errors should be handled in the sendToServer
							// method
						}
					} catch (Exception ex) {
						//Util.showToast(this, "LOCATE:104 " + ex.toString(), Toast.LENGTH_LONG);
						ex.printStackTrace();
						MapHUD.updateHinText(mapViewer, "LOCATE:104 ERROR: " + ex.getMessage());
						mapViewer.finish();
						return;
					}
				} catch (Exception e) {
					//Util.showToast(this, "LOCATE:103 " + e.toString(), Toast.LENGTH_LONG);
					e.printStackTrace();
					MapHUD.updateHinText(mapViewer, "LOCATE:103 ERROR: " + e.getMessage());
					mapViewer.finish();
					return;
				}
				
				return;
			} //if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
		} //if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR) {
			
		// No enough buffered data or buffer not used, use a thread
		
		setCollectingOnGoing(mapViewer, true);

		new Thread() {
			public void run() {
				try {
					WifiFingerPrint fingnerPrint = new WifiFingerPrint(IndoorMapData.REQUEST_LOCATE);
					fingnerPrint.log();
					
					setCollectingOnGoing(mapViewer, false);

					try {
						// data.put("req", IndoorMapData.REQUEST_LOCATE);
						// data.put("fingerprint", fingerPrint);
						Gson gson = new Gson();
						String json = gson.toJson(fingnerPrint);
						JSONObject data = new JSONObject(json);

						if (Util.sendToServer(mapViewer, MsgConstants.MT_LOCATE, data)) {
							//Util.showShortToast(MapViewerActivity.this, R.string.locate_collected);
							MapHUD.updateHinText(mapViewer, R.string.locate_collected);
						} else {
							// All errors should be handled in the sendToServer
							// method
						}
					} catch (Exception ex) {
						//Util.showToast(MapViewerActivity.this, "004 " + ex.toString(), Toast.LENGTH_LONG);
						MapHUD.updateHinText(mapViewer, "LOCATE: 004 ERROR: " + ex.getMessage());
					}
				} catch (Exception e) {
					setCollectingOnGoing(mapViewer, false);
					
					//Util.showToast(MapViewerActivity.this, "003 " + e.toString(), Toast.LENGTH_LONG);
					MapHUD.updateHinText(mapViewer, "LOCATE: 003 ERROR: " + e.getMessage());
				}

				if (mapViewer.DEBUG)
					Log.d(mapViewer.TAG, "End LocateMe Thread");
			}
		}.start();
	}

	
	public static void startPeriodicLocateMeThread(final MapViewerActivity mapViewer) {
		if (mapViewer.mPeriodicLocateMeThread == null) {

			// Locate Me Periodically
			mapViewer.mPeriodicLocateMeThread = new Thread() {
				public void run() {
					while (true) { // Run forever
						if (!mapViewer.periodicLocateMeOn) {
							break; // Stop Thread on pause
						}
						
						try {
							sleep(IndoorMapData.PERIODIC_LOCATE_INTERVAL);
						} catch (InterruptedException e) {
							continue;
						}

						if (!mapViewer.periodicLocateMeOn) {
							break; // Stop Thread on pause
						}
						
						long currentTime = System.currentTimeMillis();
						
						if (currentTime-mapViewer.lastManualLocateTime<IndoorMapData.PERIODIC_LOCATE_INTERVAL) {
							// No Periodically Location Update if some manual update happens inner this interval
							continue;
						}	
							
						locateMe(mapViewer, true); // Periodic Locating
					}
				}
			};

			if (mapViewer.DEBUG)
				Log.d(mapViewer.TAG, "PeriodicLocateMeThread starts.");

			mapViewer.mPeriodicLocateMeThread.start();
		} else {
			if (mapViewer.DEBUG)
				Log.d(mapViewer.TAG, "PeriodicLocateMeThread already starts.");
		}
	}
	

	public static void updateTrack(MapViewerActivity mapViewer, ArrayList<Location> locations) {
		// Sanity Check

		if (locations == null) {
			return;
		}
				
		int idx = 0;
		
		for (Location location:locations) {
			int mapId = location.getMapId();
			int version = location.getMapVersion();
			int colNo = location.getX();
			int rowNo = location.getY();
			
			if (mapViewer.DEBUG)
				Log.e("updateTrack", "mapId="+mapId+",colNo="+colNo+",rowNo="+rowNo);
			
			if ((mapId == -1) || (rowNo == -1) || (colNo == -1)) {
				// Not display the track
				continue;
			}

			if ( (mapId == Util.getRuntimeIndoorMap().getMapId()) && (version == Util.getRuntimeIndoorMap().getVersionCode()) ) {
				// Inner same Map with same Version
				
				// Out of bound
				if ((rowNo >= Util.getRuntimeIndoorMap().getRowNum()) || (colNo >= Util.getRuntimeIndoorMap().getColNum())) {
					continue;
				}
				
				mapViewer.graphicListener.locate(Util.getRuntimeIndoorMap(), colNo, rowNo, Constants.LAYER_USER, idx);
				idx++;
			} else {
				// ignore if mapId or version changed
				continue;
			}
		}
		
	}
	
	public static boolean updateLocation(MapViewerActivity mapViewer,int mapId, int mapVersion, int colNo, int rowNo) {
		if (mapViewer.DEBUG)
			Log.e("updateLocation", "mapId="+mapId+",mapVersion="+mapVersion+",colNo="+colNo+",rowNo="+rowNo);

		if ((mapId == -1) || (rowNo == -1) || (colNo == -1)) {
			//Util.showLongToast(this, R.string.no_match_location);
			MapHUD.updateHinText(mapViewer, R.string.no_match_location);

			if (mapViewer.DEBUG)
				Log.d(mapViewer.TAG, "End updateLocation: Fail");
			return false;
		}

		if ( (mapId == Util.getRuntimeIndoorMap().getMapId()) && (mapVersion == Util.getRuntimeIndoorMap().getVersionCode()) ) {
			// Inner same Map with same version
			//Util.showShortToast(this, R.string.located);
			MapHUD.updateHinText(mapViewer, R.string.located);
			
			// Out of bound
			if ((rowNo >= Util.getRuntimeIndoorMap().getRowNum()) || (colNo >= Util.getRuntimeIndoorMap().getColNum())) {
				//Util.showLongToast(this, R.string.map_out_of_date);
				MapHUD.updateHinText(mapViewer, R.string.out_of_map_bound);
				return false;
			}

			mapViewer.graphicListener.locate(Util.getRuntimeIndoorMap(), colNo, rowNo, Constants.LOCATION_USER, 0);

			MapDrawer.setCameraCenterTo(mapViewer, colNo, rowNo, false); // x,y
			
			// Set last known good location
			mapViewer.naviMyPlaceX = colNo;
			mapViewer.naviMyPlaceY = rowNo;

			// Show Location based News
			InfoBanner.infoMe(mapViewer, colNo, rowNo);
		} else {
			// Not loading new map
			// Hoare: disable to load new map in map viewer by location button
			MapHUD.updateHinText(mapViewer, R.string.no_match_location);

			if (mapViewer.DEBUG)
				Log.d(mapViewer.TAG, "End updateLocation: Fail");
			return false;
			
			/*
			if (periodicLoacting || (mMode != IndoorMapData.MAP_MODE_VIEW)){
				
				// Not Load new Map automatically when in EditMode or Periodic
				// Location Update if not the same MapId
				// If only version changes, load/upgrade new map in all scenarios
				if (mapId != Util.getRuntimeIndoorMap().getMapId()) {
					MapHUD.updateHinText(this, R.string.location_not_in_this_map);
	
					// Not display the user
					mainScene.getChildByIndex(Constants.LAYER_USER).detachChild(
							Util.getRuntimeIndoorMap().getUser().getSprite());
					
					return true;
				}
			} 
			
			// loading new map
			MapHUD.updateHinText(this, R.string.loading_new_map);

			Intent intent_locate_map = new Intent(MapViewerActivity.this,
					MapLocatorActivity.class);
			// Bundle bundle = getIntent().getExtras();
			Bundle mBundle = new Bundle();
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM,
					IndoorMapData.BUNDLE_VALUE_REQ_FROM_VIEWER);
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP, mapId);
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP_VERSION, mapVersion);
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_COL, colNo);
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_ROW, rowNo);
			intent_locate_map.putExtras(mBundle);
			startActivity(intent_locate_map);

			finish();
			
			System.gc();
			try {
				finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			*/
		}

		return true;
	}

	public static void updateLocation(MapViewerActivity mapViewer, Location location) {
		// Not display the user		
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_USER).detachChild(Util.getRuntimeIndoorMap().getUser().getSprite());
		for (int i=0; i<Util.getRuntimeIndoorMap().getTracksNum();i++){
			mapViewer.mainScene.getChildByIndex(Constants.LAYER_USER).detachChild(Util.getRuntimeIndoorMap().getTrack(i).getSprite());
		}
		
		updateLocation(mapViewer, location.getMapId(), location.getMapVersion(), location.getX(), location.getY());
	}
	
	public static void updateLocation(MapViewerActivity mapViewer, LocationSet locationSet) {
		Location banlanceLocation = locationSet.balanceLocation();

		updateLocation(mapViewer, banlanceLocation);
		
		if (banlanceLocation.getMapId() == Util.getRuntimeIndoorMap().getMapId()) {
			updateTrack(mapViewer, locationSet.getLocations());
		}
        
		// TODO: The server side is not ready, comment this line to avoid the 404 Not Found error
		//mAdvertisement.getAds(banlanceLocation);
	}

	


	
}
