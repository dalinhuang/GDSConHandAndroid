package com.winjune.wifiindoor.activity;

import java.io.FileInputStream;
import java.io.InputStream;

import org.json.JSONObject;

import com.winjune.wifiindoor.R;
import com.google.gson.Gson;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.IndoorMapReply;
import com.winjune.wifiindoor.webservice.types.MapManagerReply;
import com.winjune.wifiindoor.webservice.types.VersionOrMapIdRequest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MapLocatorActivity extends Activity {
	
	
	
	private boolean mapDownloadOngoing = false;

	
	@Override
	protected void onResume() {
		super.onResume();
		
		System.gc();

		IpsWebService.setActivity(this);
		IpsWebService.activateWebService();
		
		Util.setCurrentForegroundActivity(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Util.setCurrentForegroundActivity(null);
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_locator);

        					
        // Start the Ips Message Handler Thread if it has not been started yet.
        IpsWebService.setActivity(this);
        IpsWebService.activateWebService();		    
        
        // default map is 2
        openMapViewer(2);
					
    }
  
	public void enterDefaultMap(){
		if (mapDownloadOngoing){
			// when hit here, map download fails due to connection or server issue.
			// So default map needs to be loaded.
			Util.showLongToast(this, R.string.server_unreachable);
			
			IndoorMapReply indoorMap = new IndoorMapReply();
			try {
				InputStream map_file_is = getAssets().open("defaultmap.xml");
			
				indoorMap.fromXML(map_file_is);
				Util.setIsDefaultMap(true);
				Log.i("MapLocatorActivity", "Download map failure, use the default map instead");
			} catch (Exception exception) {
				Log.e("MapLocatorActivity", "Default map is not exist!");
				return;
			}
			enterMapViewer(indoorMap);
		} 
	}
	
	private void enterMapViewer(IndoorMapReply indoorMap) {
		Intent intent_locate_map = new Intent(MapLocatorActivity.this, MapViewerActivity.class); 
		Bundle mBundle = new Bundle(); 
		mBundle.putSerializable(IndoorMapData.BUNDLE_KEY_MAP_INSTANCE, indoorMap);
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR);
        intent_locate_map.putExtras(mBundle); 
		startActivity(intent_locate_map);
			
		finish();
	}

	private void openMapViewer(int mapId) {				
		try {
			IndoorMapReply indoorMap = new IndoorMapReply();
			
			InputStream map_file_is = new FileInputStream(Util.getMapFilePathName(""+mapId));
			
			indoorMap.fromXML(map_file_is);
			
			enterMapViewer(indoorMap);
			
		} catch (Exception e) {
			
			// start to download the map
			if (!downloadMap(mapId)) { 							
				//no cached map file and download map files failed
				// use default map							
				enterDefaultMap();			
			}
	   }									
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

			if (IpsWebService.sendMessage(this, IpsMsgConstants.MT_MAP_QUERY, data)) {
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
		if (indoorMapReply == null)
			return;		
		
		// Store XML file
		indoorMapReply.toXML();
		
		// go to Map Viewer
		enterMapViewer(indoorMapReply);
	}
}
