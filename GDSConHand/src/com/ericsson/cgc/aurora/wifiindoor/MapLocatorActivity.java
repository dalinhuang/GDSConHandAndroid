package com.ericsson.cgc.aurora.wifiindoor;

import java.io.FileInputStream;
import java.io.InputStream;

import org.json.JSONObject;

import com.ericsson.cgc.aurora.wifiindoor.map.IndoorMap;
import com.ericsson.cgc.aurora.wifiindoor.types.IndoorMapReply;
import com.ericsson.cgc.aurora.wifiindoor.types.Location;
import com.ericsson.cgc.aurora.wifiindoor.types.LocationSet;
import com.ericsson.cgc.aurora.wifiindoor.types.VersionOrMapIdRequest;
import com.ericsson.cgc.aurora.wifiindoor.types.WifiFingerPrint;
import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;
import com.ericsson.cgc.aurora.wifiindoor.webservice.MsgConstants;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MapLocatorActivity extends Activity {
	private Bundle bundle;
	
	private boolean mapDownloadOngoing = false;
	private boolean withLocation = false;
	private int x;
	private int y;	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		System.gc();

		Util.getIpsMessageHandler().setActivity(this);
		Util.getIpsMessageHandler().startTransportServiceThread();
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_locator);

        bundle = getIntent().getExtras();
		int req = bundle.getInt(IndoorMapData.BUNDLE_KEY_REQ_FROM);
		
		switch (req) {
		
			case IndoorMapData.BUNDLE_VALUE_REQ_FROM_LOCATOR:
				
				Util.showShortToast(this, R.string.locate_current_map);
				
		        // Start the Ips Message Handler Thread if it has not been started yet.
		        Util.getIpsMessageHandler().setActivity(this);
				Util.getIpsMessageHandler().startTransportServiceThread();
		        
		        // Locate me so I know which map I should load.
				locateMe();
				
				break;
			
			case IndoorMapData.BUNDLE_VALUE_REQ_FROM_VIEWER:
				
				Util.showShortToast(this, R.string.locate_current_map);
				
				// Load new map and pass in the new location
				int mapId = bundle.getInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP);
				int mapVersion = bundle.getInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP_VERSION);
				int colNo = bundle.getInt(IndoorMapData.BUNDLE_KEY_LOCATION_COL);
				int rowNo = bundle.getInt(IndoorMapData.BUNDLE_KEY_LOCATION_ROW);
				
				updateLocation(mapId, mapVersion, colNo, rowNo);
				
				break;
				
			case IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR:
				
				Util.showShortToast(this, R.string.load_selected_map);
				
				// Load new map only
				int mapId1 = bundle.getInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP);
				openMapViewer(mapId1);
				
				break;
		
			default:
				finish();
				break;
		}
    }
  
	private void locateMe() {		
		if (!Util.getWifiInfoManager().isWifiEmbeded()) {
			Util.showLongToast(this, R.string.no_wifi_embeded);
			finish();
			return;
		}
		
		if (!Util.getWifiInfoManager().isWifiEnabled()) {
			Util.showLongToast(this, R.string.no_wifi_enabled);
			finish();
			return;
		}
		
		Util.showShortToast(this, R.string.locate_collecting);
		
		if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR) {
			if (Util.getWifiInfoManager().hasFreshSavedSamples()) {  // No need for multiple tuples, try to get into the map but not waiting
				try {
					WifiFingerPrint fignerPrint = Util.getWifiInfoManager().mergeSamples();
					try {
						Gson gson = new Gson();
						String json = gson.toJson(fignerPrint);
						JSONObject data = new JSONObject(json);

						if (Util.sendToServer(this, MsgConstants.MT_LOCATE, data)) {
							Util.showShortToast(this, R.string.locate_collected);;
						} else {
							// All errors should be handled in the sendToServer
							// method
						}
					} catch (Exception ex) {
						Util.showToast(this, "LOCATE:114 " + ex.toString(), Toast.LENGTH_LONG);
						finish();
						return;
					}
				} catch (Exception e) {
					Util.showToast(this, "LOCATE:113 " + e.toString(), Toast.LENGTH_LONG);
					finish();
					return;
				}
				
				return;
			}  // if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
		} // if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR) {
							
		// Use a thread if no enough buffered data or buffer not used
		new Thread(){
			public void run(){
				try {
					WifiFingerPrint fignerPrint = new WifiFingerPrint(IndoorMapData.REQUEST_LOCATE);
					
					try {
						Gson gson = new Gson();
						String json = gson.toJson(fignerPrint);
						JSONObject data = new JSONObject(json);

						if (Util.sendToServer(MapLocatorActivity.this, MsgConstants.MT_LOCATE, data)) {
							Util.showShortToast(MapLocatorActivity.this, R.string.locate_collected);
						} else {
							// All errors should be handled in the sendToServer
							// method
						}
					} catch (Exception ex) {
						Util.showToast(MapLocatorActivity.this, "LOCATE:004 " + ex.toString(), Toast.LENGTH_LONG);
						MapLocatorActivity.this.finish();
						return;
					}
				} catch (Exception e) {
					Util.showToast(MapLocatorActivity.this, "LOCATE:003 " + e.toString(), Toast.LENGTH_LONG);
					MapLocatorActivity.this.finish();
					return;
				}
			}
		}.start();
		
	}
	
	public boolean updateLocation(int mapId, int mapVersion, int colNo, int rowNo) {
		if ((mapId==-1) || (rowNo==-1) || (colNo==-1)) {			
			Util.showLongToast(this, R.string.no_match_location);
			
			finish();

			return false;
		}
		
		openMapViewer(mapId, mapVersion, colNo, rowNo);
		
		return true;
	}
	
	public void updateLocation(LocationSet locationSet) {
		updateLocation(locationSet.balanceLocation());	
	}
	
	private void updateLocation(Location location) {
		updateLocation(location.getMapId(), location.getMapVersion(), location.getX(), location.getY());
	}
	
	public void connectionFailed(){
		finish();
	}
	
	private void startNewIntent(IndoorMap indoorMap) {
		Intent intent_locate_map = new Intent(MapLocatorActivity.this, MapViewerActivity.class); 
		Bundle mBundle = new Bundle(); 
		mBundle.putSerializable(IndoorMapData.BUNDLE_KEY_MAP_INSTANCE, indoorMap);
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR);
        intent_locate_map.putExtras(mBundle); 
		startActivity(intent_locate_map);
			
		finish();
	}

	private void openMapViewer(int mapId) {
		IndoorMap indoorMap = new IndoorMap();
		
		try {
			InputStream map_file_is = new FileInputStream(Util.getMapFilePathName(""+mapId));
			
			indoorMap.fromXML(map_file_is);
			// file has already been closed
			//map_file_is.close();
			
			// For Files in SD Card but not
			//load_map_rc = designMap.fromXML(IndoorMapData.map_file_path + map_file_name);
			
		} catch (Exception e) {
			if (!downloadMap(mapId)) {
				finish();
			}
			withLocation = false;
			return;
		}
		
		startNewIntent(indoorMap);
	}	
	
	private void startNewIntent(IndoorMap indoorMap, int colNo, int rowNo) {
		Intent intent_locate_map = new Intent(MapLocatorActivity.this, MapViewerActivity.class);  
		Bundle mBundle = new Bundle(); 
		mBundle.putSerializable(IndoorMapData.BUNDLE_KEY_MAP_INSTANCE, indoorMap);
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_LOCATOR);
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_COL, colNo);
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_ROW, rowNo);
        intent_locate_map.putExtras(mBundle); 
		startActivity(intent_locate_map);
			
		finish();
	}
	
	private void openMapViewer(int mapId, int mapVersion, int colNo, int rowNo) {
		IndoorMap indoorMap = new IndoorMap();
		boolean updateNeeded = false;

		try {
			InputStream map_file_is = new FileInputStream(Util.getMapFilePathName(""+mapId));
			
			indoorMap.fromXML(map_file_is);
			// file has already been closed
			//map_file_is.close();
			
			// For Files in SD Card but not
			//load_map_rc = designMap.fromXML(IndoorMapData.map_file_path + map_file_name);
			
			if (indoorMap.getVersionCode() != mapVersion) {
				updateNeeded = true;
			}
		} catch (Exception e) {
			updateNeeded = true;
		}
		
		if (updateNeeded) {
			withLocation = true;
			x = colNo;
			y = rowNo;
			if (!downloadMap(mapId)) {
				finish();
			}
			return;
		}
		
		startNewIntent(indoorMap, colNo, rowNo);
	}

	private boolean downloadMap(int mapId) {
		VersionOrMapIdRequest id = new VersionOrMapIdRequest();
		id.setCode(mapId);
		
		mapDownloadOngoing = true;
		
		Util.showShortToast(this, R.string.download_ongoing);
		
		try {
			
			Gson gson = new Gson();
			String json = gson.toJson(id);
			JSONObject data = new JSONObject(json);

			if (Util.sendToServer(this, MsgConstants.MT_MAP_QUERY, data)) {
				return true;
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			mapDownloadOngoing = false;
			Util.showToast(this, "GET MAP ERROR: " + ex.getMessage(), Toast.LENGTH_LONG);
			ex.printStackTrace();
		}
		
		return false;
	}
	
	public void handleMapReply(IndoorMapReply indoorMapReply) {
		if (indoorMapReply == null) {
			return;
		}
		
		// Store XML file
		IndoorMap indoorMap = indoorMapReply.toIndoorMap();
		
		if (indoorMap == null) {
			return;
		}
		
		indoorMap.toXML();
		
		// go to Map Viewer
		if (mapDownloadOngoing && withLocation) {
			startNewIntent(indoorMap, x, y);
		} else {
			if (mapDownloadOngoing) {
				startNewIntent(indoorMap);
			}
		}
	}	
}
