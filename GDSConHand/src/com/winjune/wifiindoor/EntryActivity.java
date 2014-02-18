package com.winjune.wifiindoor;

import java.io.FileInputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ericsson.cgc.aurora.wifiindoor.R;
import com.winjune.wifiindoor.map.IndoorMap;
import com.winjune.wifiindoor.types.ApkVersionReply;
import com.winjune.wifiindoor.types.LocationSet;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

public class EntryActivity extends Activity implements SensorEventListener {
	private OnClickListener on_click_listener0 = null;
	private OnClickListener on_click_listener1 = null;
	private OnClickListener on_click_listener2 = null;
	private OnClickListener on_click_listener3 = null;
	private Button button0;
	private Button button1;
	private Button button2;
	private Button button3;
	
	public static final String PACKAGE_NAME = "com.winjune.wifiindoor";
	   	        
	@Override
	protected void onResume() {
		super.onResume();
		System.gc();
		Util.setEnergySave(false);
		
		// Enable NFC Foreground Dispatch
		Util.enableNfc(this);
		
		// Disable ACCELEROMETER
		Util.enableAcclerometer(this);
		
		Util.setCurrentForegroundActivity(this);
	}
	
	@Override
	protected void onStop(){
	    super.onStop();
	  
	    // Disable NFC Foreground Dispatch
	    Util.disableNfc(this);
			
	    // Disable ACCELEROMETER
	    Util.disableAcclerometer(this);
	    
	    Util.setEnergySave(true);
	    Util.cancelToast();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Util.setEnergySave(true);
		
		// Disable NFC Foreground Dispatch
		Util.disableNfc(this);
		
		// Disable ACCELEROMETER
	    Util.disableAcclerometer(this);
	    
	    Util.setCurrentForegroundActivity(null);
	}
	
	@Override
	protected void onDestroy() {
		Util.setEnergySave(true);
		
		// Disable NFC Foreground Dispatch
		Util.disableNfc(this);
		
		// Disable ACCELEROMETER
	    Util.disableAcclerometer(this);
	    
	    Util.cancelToast();
	 
	    super.onDestroy();
	}
	
	@Override
	public void onNewIntent(Intent intent) {
	    //Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);	
		
		super.onNewIntent(intent);
	    
	    // Ensure there is a network connection
	    if (!Util.isHttpConnectionEstablished()) {
	    	Util.showLongToast(this, R.string.retry_ip);
	    	return;
	    }

		String tagId = Util.getNfcInfoManager().getTagId(intent);
		
		if (tagId == null) {
			return;
		}
	    
	    //String tagId = tagFromIntent.getId().toString();
		Util.nfcQrLocateMe(this, tagId);  
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String tagId = bundle.getString("result");
			
			Log.e("QR", "tagID="+tagId);
				
			Util.nfcQrLocateMe(this, tagId);
		}
	
	}

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Already down in GMapEntryActivity
        //Util.initApp(this);
               
        // Locate the current Map
        on_click_listener0 = new OnClickListener(){
        	public void onClick(View view){        		
        		locateMe();
        	}
        };
        
        //Choose a Map
        on_click_listener1 = new OnClickListener(){
        	public void onClick(View view){        		
        		Intent intent_map_selector = new Intent(EntryActivity.this, MapSelectorActivity.class);
        		Bundle mBundle = new Bundle(); 
				mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR);
				intent_map_selector.putExtras(mBundle); 
        		startActivity(intent_map_selector);
        	}
        };
        
        //QR Scan
        on_click_listener2 = new OnClickListener(){
        	public void onClick(View view){ 
        	   
        		if (!Util.isHttpConnectionEstablished()) {
        			Util.showLongToast(EntryActivity.this, R.string.retry_ip);
        			Util.initApp(EntryActivity.this);
        			return;
        		}
        		
        		Intent openCameraIntent = new Intent(EntryActivity.this, QrScannerActivity.class);
        		startActivityForResult(openCameraIntent, 0);
   	
        	}
        };
        
        //Configuration
        on_click_listener3 = new OnClickListener(){
        	public void onClick(View view){        		        		
        		Intent intent_tuner = new Intent(EntryActivity.this, TunerActivity.class);
        		startActivity(intent_tuner);
        	}
        };
 
        setContentView(R.layout.entry);
        
        // Define Buttons and bind the listeners
        button0 = (Button) findViewById(R.id.locateButton);
        button0.setOnClickListener(on_click_listener0);
        button1 = (Button) findViewById(R.id.selectButton);
        button1.setOnClickListener(on_click_listener1);
        button2 = (Button) findViewById(R.id.qrButton);
        button2.setOnClickListener(on_click_listener2);
        button3 = (Button) findViewById(R.id.configButton);
        button3.setOnClickListener(on_click_listener3);
    }
	
	public void handleApkVersionReply(ApkVersionReply version) {
		Util.setApkVersionChecked(true);
		Util.setApkVersionReply(version);
		
		if (version.getVersionCode() > Util.getApkVersionCode() ) {
			if (!Util.isNetworkConfigPending()) {
				Util.doNewVersionUpdate(this);
			} else {
				Util.setApkUpdatePending(true);
			}	
		} else {
			Util.showShortToast(this, R.string.latest_apk_version);
		}
	}
	
	public void updateLocation(com.winjune.wifiindoor.types.Location location) {
		updateLocation(location.getMapId(), location.getX(), location.getY());
	}
	
	private boolean updateLocation(int mapId, int colNo, int rowNo) {
		if ((rowNo==-1) || (colNo==-1)) {			
			Util.showLongToast(this, R.string.no_match_location);
			return false;
		}
		
		Log.e("LocatorActivity", "Start openMapViewer");
		openMapViewer(mapId, colNo, rowNo);
		
		return true;
	}
	
	public void updateLocation(LocationSet locationSet) {
		updateLocation(locationSet.balanceLocation());	
	}
	
	private void openMapViewer(int mapId, int colNo, int rowNo) {
		IndoorMap indoorMap = new IndoorMap();
		
		try {
			InputStream map_file_is = new FileInputStream(Util.getMapFilePathName(""+mapId));
			
			indoorMap.fromXML(map_file_is);
			// file has already been closed
			//map_file_is.close();
			
			// For Files in SD Card but not
			//load_map_rc = designMap.fromXML(IndoorMapData.map_file_path + map_file_name);
			
		} catch (Exception e) {
			Util.showLongToast(this, R.string.invalid_map);
			
			return;
		}
		
		Intent intent_locate_map = new Intent(EntryActivity.this, MapViewerActivity.class); 
		//Bundle bundle = getIntent().getExtras(); 
		Bundle mBundle = new Bundle(); 
		mBundle.putSerializable(IndoorMapData.BUNDLE_KEY_MAP_INSTANCE, indoorMap);
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_LOCATOR);
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_COL, colNo);
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_ROW, rowNo);
        intent_locate_map.putExtras(mBundle); 
		startActivity(intent_locate_map);		
	}
	
	private void locateMe() {		
		if (!Util.isHttpConnectionEstablished()) {
			Util.showLongToast(EntryActivity.this, R.string.retry_ip);
			Util.initApp(EntryActivity.this);
			return;
		}
		
		Intent intent_map_locator = new Intent(EntryActivity.this, MapLocatorActivity.class);
		Bundle mBundle = new Bundle(); 
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_LOCATOR);
		intent_map_locator.putExtras(mBundle); 
		startActivity(intent_map_locator);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		if (Util.isShakeDetected(event)) {
			locateMe();
		}
	}

}