package com.winjune.wifiindoor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;

import com.winjune.wifiindoor.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.LocationListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.winjune.wifiindoor.map.Building;
import com.winjune.wifiindoor.map.BuildingManager;
import com.winjune.wifiindoor.map.IndoorMap;
import com.winjune.wifiindoor.types.ApkVersionReply;
import com.winjune.wifiindoor.types.BuildingManagerReply;
import com.winjune.wifiindoor.types.LocationSet;
import com.winjune.wifiindoor.types.VersionOrMapIdRequest;
import com.winjune.wifiindoor.util.ISoftwareVersions;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.SoftwareVersionData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;
import com.winjune.wifiindoor.webservice.MsgConstants;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class GMapEntryActivity extends FragmentActivity implements SensorEventListener {
	   
    private GoogleMap mMap;
    
    private LocationManager locationManager; 
    private String bestProvider;
    private LocationListener myLocationListener;
    
    private BuildingManager buildingManager;
    private ArrayList<Marker> mMarkers;
    private String[] mBuildings;
	
	private Resources resources;
	
	private static final int MENU_ITEM_LOCATE = Menu.FIRST;   
    private static final int MENU_ITEM_SCAN = Menu.FIRST+1;
    private static final int MENU_ITEM_CONFIG = Menu.FIRST+2;
    private static final int MENU_ITEM_EXIT = Menu.FIRST+3;
	
	@Override
    protected void onPause(){

		// Comment out as GMap function is blocked.
		// locationManager.removeUpdates(myLocationListener);
		
		// Disable NFC Foreground Dispatch
		Util.disableNfc(this);
		
		// Disable ACCELEROMETER
		Util.disableAcclerometer(this);
				
		Util.setEnergySave(true);
		
		super.onPause();
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
	protected void onStop(){    
	    // Disable NFC Foreground Dispatch
	    Util.disableNfc(this);
			
	    // Disable ACCELEROMETER
	    Util.disableAcclerometer(this);
	    
	    Util.setEnergySave(true);
	    Util.cancelToast();
	    
	    super.onStop();
	}
	
	@Override
    protected void onResume(){
		super.onResume();
		
		System.gc();
		
		Util.getIpsMessageHandler().setActivity(this);

		Util.setEnergySave(false);
		
		if (Util.isApkUpdatePending()) {
			Util.doNewVersionUpdate(this);
		}
		
		// Comment out as GMap function is blocked
		// locationManager.requestLocationUpdates(bestProvider, 10000l, 30f, myLocationListener); // 10s, 30m
		
		// Enable NFC Foreground Dispatch
		Util.enableNfc(this);
		
		// Enable ACCELEROMETER
		Util.enableAcclerometer(this);
		
		Util.setCurrentForegroundActivity(this); 
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
		
		Util.setCurrentForegroundActivity(null);
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
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.i("GMapEntryActivity", "onCreate");
        
        setContentView(R.layout.gmap_entry);
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.map);
        f.setUserVisibleHint(false); // Set invisible for the GMap fragment
        
        Util.initApp(this);
        
        new AsyncTask<Void, Void, Integer> () {

			@Override
			protected void onPreExecute() {

			}
        	
        	@Override
			protected Integer doInBackground(Void... params) {
				// TODO Auto-generated method stub
        		long startTime = System.currentTimeMillis();
        		appStartUp();
        		long completeTime = System.currentTimeMillis();
        		long runTime = completeTime - startTime;
        		
        		if (runTime < 3000) {
        			
        			long sleepTime = 3000 - runTime;
        			//Hoare: give more time to show background picture
            		try {
    					Thread.sleep(sleepTime);
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
        		}
        		
        		return null;
			}

			@Override
            protected void onPostExecute(Integer result) {
				jumpToRightEntry();

            }
        	
        }.execute(new Void[]{});
        
        // Block the Google Map function so far
        // GMapInit();

    }
	
	// Called in the onPostExecute of AsyncTask
	private void jumpToRightEntry() {
        if (SoftwareVersionData.VERSION_NAME == null) {
        	SoftwareVersionData.VERSION_NAME = ISoftwareVersions.PUBLIC_VERSION_NAME;
        }
        
        if (SoftwareVersionData.VERSION_NAME.trim().equalsIgnoreCase(ISoftwareVersions.GDSC_VERSION_NAME)) {
        	Intent intent_map_locator = new Intent(GMapEntryActivity.this, MapLocatorActivity.class);
			Bundle mBundle = new Bundle(); 
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR);
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP, ISoftwareVersions.GDSC_MAP_ID);
			intent_map_locator.putExtras(mBundle); 
    		startActivity(intent_map_locator);
        	finish();
        	return;
        }
        
        if (!VisualParameters.ENTRY_NEEDED) {
        	Log.i("GMapEntryActivity", "No Entry is needed!");
        	Intent intent_map_selector = new Intent(GMapEntryActivity.this, MapSelectorActivity.class);
    		Bundle mBundle = new Bundle(); 
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR);
			intent_map_selector.putExtras(mBundle); 
    		startActivity(intent_map_selector);
    		finish();
        	return;
        }
        
        if (!VisualParameters.GOOGLE_MAP_EMBEDDED) {
        	Log.e("GMapEntryActivity", "Goolge Play Service Not Available!");
        	Intent normal_entry = new Intent(GMapEntryActivity.this, EntryActivity.class);
    		startActivity(normal_entry);
    		finish();   		
        	return;
        }
        
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) != ConnectionResult.SUCCESS) {
        	Log.e("GMapEntryActivity", "Goolge Play Service Not Available!");
        	Intent normal_entry = new Intent(GMapEntryActivity.this, EntryActivity.class);
    		startActivity(normal_entry);
    		finish();   		
        	return;
        }
        
        return;
	}
	
	// Initial method for Google Map 
	private void GMapInit() {
        
        Log.i("GMapEntryActivity", "Google Map Embedded!");

        resources = getResources();
        
        mMap = ((SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        
        addBuildingMarkers();
        
        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){

			@Override
			public void onInfoWindowClick(Marker marker) {
				int idx = mMarkers.indexOf(marker);
				int[] xBuilding = null;
				
				if ((idx >= 0) && (idx < mBuildings.length)){
					xBuilding = getMapArray(mBuildings[idx]);
				}
				
				if (xBuilding != null){
					Intent building_map_selector = new Intent(GMapEntryActivity.this, MapSelectorActivity.class);
					Bundle mBundle = new Bundle(); 
					mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_BUILDING);
					mBundle.putIntArray(IndoorMapData.BUNDLE_KEY_BUILDING_MAPS, xBuilding);
					building_map_selector.putExtras(mBundle); 
		    		startActivity(building_map_selector);
				}
			}
        	
        });
        
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE); 
        bestProvider = getBestProvider(); 
        updateToNewLocation(getLastKnownLocation());
        
        myLocationListener = new LocationListener(){       	
        	@Override
			public void onLocationChanged(Location location) {
				if (location != null) { 
	                // Go to current location
					updateToNewLocation(location);
	            } 	
			}

			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
        	
        };
        
        locationManager.requestLocationUpdates(bestProvider, 3000l, 8f, myLocationListener);
        
	}
	
	private void addBuildingMarkers() {
		// Read from File for the building items
        buildingManager = new BuildingManager();
        if (!buildingManager.loadBuildingManager(this, resources)) {
        	buildingManager.setVersionCode(-1);
        	queryBuildings(-1);
        	return;
        }
        
        addBuildingMarkers(buildingManager);
        
        queryBuildings(buildingManager.getVersionCode());
	}
	
	private void addBuildingMarkers(BuildingManager buildingManager) {
		if (buildingManager == null) {
			return;
		}
		
		if (buildingManager.getBuildings() == null) {
        	return;
        }
        
        
		if (mMarkers == null) {
			mMarkers = new ArrayList<Marker>(); 
		} else {
			mMarkers.clear();
		}
		
		mBuildings = new String[buildingManager.getItemNumber()];
		
        for (Building building : buildingManager.getBuildings()) {
        	MarkerOptions location = new MarkerOptions()
	    		.position(new LatLng(building.getLatitude(), building.getLongitude()))
	    		.title(building.getCategory())
	    		.snippet(building.getName());

        	Marker marker = mMap.addMarker(location);
  
        	mMarkers.add(marker);
        	mBuildings[mMarkers.indexOf(marker)] = building.getMaps();
        }       
        
        if (mMarkers.size() > 0){
        	mMarkers.get(0).showInfoWindow();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		
		menu.add(0, MENU_ITEM_LOCATE, 0, resources.getString(R.string.menu_locate)).setIcon(android.R.drawable.ic_menu_mylocation);  
		menu.add(0, MENU_ITEM_SCAN, 0, resources.getString(R.string.menu_scan_qr)).setIcon(android.R.drawable.ic_menu_camera);  
		menu.add(0, MENU_ITEM_CONFIG, 0, resources.getString(R.string.menu_config)).setIcon(android.R.drawable.ic_menu_preferences);  
        menu.add(0, MENU_ITEM_EXIT, 0, resources.getString(R.string.menu_exit)).setIcon(android.R.drawable.ic_menu_close_clear_cancel);	
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case MENU_ITEM_LOCATE:
	    	locateMe();
	    	break;
	    case MENU_ITEM_SCAN:
	    	Intent openCameraIntent = new Intent(GMapEntryActivity.this, QrScannerActivity.class);
			startActivityForResult(openCameraIntent, 0);
	    	break;	
	    case MENU_ITEM_CONFIG:
	    	Intent openConfigIntent = new Intent(GMapEntryActivity.this, TunerActivity.class);
			startActivity(openConfigIntent);
	        break;
	    case MENU_ITEM_EXIT:
	    	exitApp();
	        break;
	    }
		
		return true;
	}
	
	private void locateMe() {
		if (!Util.isHttpConnectionEstablished()) {
			Util.showLongToast(this, R.string.retry_ip);
			Util.initApp(this);
			new Thread() {
				public void run() {
					Util.connetcToServer(GMapEntryActivity.this);
				}
			}.start();
			return;
		}
		
		Intent intent_map_locator = new Intent(GMapEntryActivity.this, MapLocatorActivity.class);
		Bundle mBundle = new Bundle(); 
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_LOCATOR);
		intent_map_locator.putExtras(mBundle); 
		startActivity(intent_map_locator);
	}

	private void exitApp() {

		System.exit(0);
	}
	
	// Called in AsyncTask when the APP starts up. It should only include the time consuming tasks.
	private void appStartUp() {
		
		Util.connetcToServer(this);
		
	}
    
    private String getBestProvider(){ 
        Criteria criteria = new Criteria(); 
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); // 锟斤拷锟矫撅拷锟斤拷
        criteria.setAltitudeRequired(false); // 锟斤拷锟斤拷锟角凤拷锟斤拷要锟结供锟斤拷锟斤拷锟斤拷息
        criteria.setBearingRequired(false); // 锟角凤拷锟斤拷要锟斤拷锟斤拷锟斤拷息
        criteria.setCostAllowed(false); // 锟斤拷锟斤拷锟揭碉拷锟斤拷 Provider 锟角凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 锟斤拷锟矫耗碉拷
        
        String provider=locationManager.getBestProvider(criteria, true); 
        
        Log.e("getBestProvider", provider);
        
        // 锟斤拷锟斤拷锟斤拷芊锟斤拷锟�null, 锟斤拷锟斤拷位锟斤拷锟斤拷息锟斤拷锟斤拷未锟斤拷锟斤拷
        return provider; 
    }  

    public Location getLastKnownLocation() { 
		Location ret = null; 
		
		if (bestProvider == null) {
			bestProvider = getBestProvider();
		}
        
		if(bestProvider != null) { 
			ret = locationManager.getLastKnownLocation(bestProvider); 
		} else {
			// Try to get from Network
			ret = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		// 锟斤拷锟斤拷锟斤拷芑岱碉拷锟�null, 锟斤拷示锟斤拷锟秸碉拷前锟侥诧拷询锟斤拷锟斤拷锟睫凤拷锟斤拷取系统锟斤拷锟揭伙拷胃锟斤拷碌牡锟斤拷锟轿伙拷锟斤拷锟较�		
		if (ret == null) {
			Log.e("getLastKnownLocation", "null");
		} else {
			Log.e("getLastKnownLocation", ret.getLongitude()+","+ret.getLatitude());
		}
		
		return ret; 
     } 
	
	private void updateToNewLocation(Location location){
	    if(location == null){
	    	return;
	    }
		//锟斤拷取锟斤拷锟斤拷
		double dLong = location.getLongitude();
		//锟斤拷取纬锟斤拷
		double dLat = location.getLatitude();
		
		//Log.e("updateToNewLocation", dLong+","+dLat);
		
		float zoomLevel = 13f;
		if (mMap.getCameraPosition() != null) {
			zoomLevel = mMap.getCameraPosition().zoom;
		}
	    
	    //锟斤拷锟斤拷影锟斤拷锟狡讹拷锟斤拷指锟斤拷锟侥碉拷锟斤拷位锟斤拷
		CameraPosition cameraPosition = new CameraPosition.Builder()
	        .target(new LatLng(dLat, dLong))              // Sets the center of the map to ZINTUN
	        .zoom(zoomLevel)          // 锟斤拷锟脚憋拷锟斤拷
	        .bearing(0)                // Sets the orientation of the camera to east
	        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
	        .build();                   // Creates a CameraPosition from the builder
	    
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
	
	public void updateLocation(com.winjune.wifiindoor.types.Location location) {
		updateLocation(location.getMapId(), location.getX(), location.getY());
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
		
		Intent intent_locate_map = new Intent(GMapEntryActivity.this, MapViewerActivity.class); 
		//Bundle bundle = getIntent().getExtras(); 
		Bundle mBundle = new Bundle(); 
		mBundle.putSerializable(IndoorMapData.BUNDLE_KEY_MAP_INSTANCE, indoorMap);
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_LOCATOR);
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_COL, colNo);
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_ROW, rowNo);
        intent_locate_map.putExtras(mBundle); 
		startActivity(intent_locate_map);
		
		Log.e("GMapEntryActivity", "Start MapViewerActivity, finish current Locator");
			
	}
	
	private int[] getMapArray(String maps) {
		if (maps == null) {
			return null;
		}
		
		String[] mapStrArray = maps.split(",");
		int[] mapIntArray = new int[mapStrArray.length];
		
		for (int i=0; i<mapStrArray.length; i++) {
			try {
				mapIntArray[i] = Integer.parseInt(mapStrArray[i]);
			} catch (Exception e) {
				e.printStackTrace();
				mapIntArray[i] = -1;
			}
		}
		
		return mapIntArray;
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
	
	private void queryBuildings(int versionCode) {
		VersionOrMapIdRequest version = new VersionOrMapIdRequest();
		version.setCode(versionCode);
		
		try {
			Gson gson = new Gson();
			String json = gson.toJson(version);
			JSONObject data = new JSONObject(json);

			if (Util.sendToServer(this, MsgConstants.MT_BUILDING_QUERY, data)) {
				
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			Util.showToast(this, "GET BUILDING ERROR: " + ex.getMessage(), Toast.LENGTH_LONG);
			ex.printStackTrace();
		}
	}
	
	public void handleBuildingReply(BuildingManagerReply managerReply) {
		if (managerReply == null) {
			return;
		}

		if (buildingManager == null) {	
			buildingManager = new BuildingManager();
		} else {
			if (managerReply.getVersionCode() == buildingManager.getVersionCode()) {
				return;
			}
			mMap.clear();
		}
		
		buildingManager.mergeBuildings(managerReply);
		buildingManager.setVersionCode(managerReply.getVersionCode());
		
		buildingManager.toXML();
		addBuildingMarkers(buildingManager);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (Util.isShakeDetected(event)) {     
		        locateMe();
		}
	}

}
