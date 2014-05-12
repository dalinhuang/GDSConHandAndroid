package com.winjune.wifiindoor.activity;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.lib.poi.PoiOfflineData;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;
import com.winjune.wifiindoor.version.ApkVersionManager;
import com.winjune.wifiindoor.version.ISoftwareVersions;
import com.winjune.wifiindoor.version.SoftwareVersionData;
import com.winjune.wifiindoor.webservice.IpsWebService;

public class StartupActivity extends Activity implements OnInitListener{
	           
    private static SharedPreferences prefs;
    private static boolean isFirstStartup;
    
	private TextToSpeech AutoGuideTTS = null;
	private boolean isTTSSupported = false;
	
	@Override
    protected void onPause(){
		
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		
				
	    Util.cancelToast();
	    
	    AutoGuideTTS.shutdown();
	    
	    super.onDestroy();
	}
	
	@Override
	protected void onStop(){    
				    
	    Util.cancelToast();
	    
	    super.onStop();
	}
	
	@Override
    protected void onResume(){
		super.onResume();
		
		// ??? IpsWebService may not start
		IpsWebService.setActivity(this);

		if (ApkVersionManager.isApkUpdatePending()) {
			ApkVersionManager.doNewVersionUpdate(this);
		}		
		
		Util.setCurrentForegroundActivity(this); 
	}
	
	@Override
	 public void onInit(int status) { 		   
	       if(status == TextToSpeech.SUCCESS){  
	           // we use Chinese      	       	   
	    	  if (AutoGuideTTS.getLanguage() == null){
	    		  Util.showLongToast (this, R.string.tts_language_unsupported);
	    		  return;
	    	  }
	    	   
	          int result =  AutoGuideTTS.isLanguageAvailable(Locale.CHINA);
	          
	          
	          if((result == TextToSpeech.LANG_COUNTRY_AVAILABLE) |  
	             (result == TextToSpeech.LANG_AVAILABLE)) {   
	        	  AutoGuideTTS.setLanguage(Locale.CHINA);
	        	  
	        	  isTTSSupported = true;
	          } else {
	        	  
	        	  Util.showLongToast (this, R.string.tts_language_unsupported); 
	          }                     
	       } 	       	      
	      
	   }

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_startup_entry);
        
        if (AutoGuideTTS == null) {        	
			AutoGuideTTS = new TextToSpeech(this, this);
		}
        
        Util.initApp(this);
        
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		isFirstStartup = prefs.getBoolean("firstStartup", false); // disable the guide pages as they are not so necessary
		//isFirstStartup = true;
        
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
				
				if (isFirstStartup) {
                	Intent intent = new Intent(StartupActivity.this, GuideActivity.class);
                	startActivity(intent);
                    finish();
                    
                    Editor editor = prefs.edit();
                    // For test only - show the guide activity every time
                    editor.putBoolean("firstStartup", false); // disable GuideActivity after the 1st time
                    editor.commit();
                }
                else {
    				jumpToRightEntry();
                }

            }
        	
        }.execute(new Void[]{}); 

    }
	
	// Called in the onPostExecute of AsyncTask
	private void jumpToRightEntry() {
		  if (isTTSSupported)
		   {
		      Util.setTTSSupported(true);
		   }
		   else
		   {
			   Util.setTTSSupported(false);			   
		   } 
        
		// Default is a public version
		if (SoftwareVersionData.VERSION_NAME == null) {
        	SoftwareVersionData.VERSION_NAME = ISoftwareVersions.PUBLIC_VERSION_NAME;
        }
        
		//GDSC customized version
        if (SoftwareVersionData.VERSION_NAME.trim().equalsIgnoreCase(ISoftwareVersions.GDSC_VERSION_NAME)) {
        	Intent intent_map_locator = new Intent(StartupActivity.this, MapLocatorActivity.class);
			Bundle mBundle = new Bundle(); 
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR);
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP, ISoftwareVersions.GDSC_MAP_ID);
			intent_map_locator.putExtras(mBundle); 
    		startActivity(intent_map_locator);
        	finish();
        	return;
        }
        
        //Public Version

        // Enter Google Map
        if  (VisualParameters.GOOGLE_MAP_EMBEDDED)  {               
        	if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) == ConnectionResult.SUCCESS) {
        		Log.e("StartupActivity", "Enter Google Map!");
        		Intent normal_entry = new Intent(StartupActivity.this, GMapEntryActivity.class);
        		startActivity(normal_entry);
        		finish();   		
        		return;
        	}
        }

        if (VisualParameters.MENU_ENTRY_NEEDED) {
        	Log.i("GMapEntryActivity", "No Entry is needed!");
    		Intent normal_entry = new Intent(StartupActivity.this, MenuEntryActivity.class);
    		startActivity(normal_entry);
    		finish();   		
    		return;            	
        }
        
        // By default, entry Map Selector Activity        
    	Intent intent_map_selector = new Intent(StartupActivity.this, MapSelectorActivity.class);
		Bundle mBundle = new Bundle(); 
		mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR);
		intent_map_selector.putExtras(mBundle); 
		startActivity(intent_map_selector);
		
		finish();
    	return;        
    
	}
		
	// Called in AsyncTask when the APP starts up. It should only include the time consuming tasks.
	private void appStartUp() {
		
		IpsWebService.startWebService(StartupActivity.this);
				
		// Check latest version
		ApkVersionManager.CheckVersionUpgrade(StartupActivity.this);
		
		Util.appPrepare(StartupActivity.this, IndoorMapData.CONFIG_FILE_PATH, PoiOfflineData.buslineTableName);
		Util.appPrepare(StartupActivity.this, IndoorMapData.CONFIG_FILE_PATH, PoiOfflineData.festivalTableName);
		Util.appPrepare(StartupActivity.this, IndoorMapData.CONFIG_FILE_PATH, PoiOfflineData.movieTableName);
		Util.appPrepare(StartupActivity.this, IndoorMapData.CONFIG_FILE_PATH, PoiOfflineData.playhouseTableName);
		Util.appPrepare(StartupActivity.this, IndoorMapData.CONFIG_FILE_PATH, PoiOfflineData.poiTableName);
		Util.appPrepare(StartupActivity.this, IndoorMapData.CONFIG_FILE_PATH, PoiOfflineData.restaurantTableName);
		

/*		Util.downFile(StartupActivity.this,
				Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, PoiOfflineData.buslineTableName),
				IndoorMapData.CONFIG_FILE_PATH,
				PoiOfflineData.buslineTableName,                     		
				false,      // Open after download
				"",
				false, //useHandler
				false);// Use Thread	

		Util.downFile(StartupActivity.this,
				Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, PoiOfflineData.festivalTableName),
				IndoorMapData.CONFIG_FILE_PATH,
				PoiOfflineData.festivalTableName,                     		
				false,      // Open after download
				"",
				false, //useHandler
				false);// Use Thread	

		Util.downFile(StartupActivity.this,
				Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, PoiOfflineData.movieTableName),
				IndoorMapData.CONFIG_FILE_PATH,
				PoiOfflineData.movieTableName,                     		
				false,      // Open after download
				"",
				false, //useHandler
				false);// Use Thread	
		Util.downFile(StartupActivity.this,
				Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, PoiOfflineData.playhouseTableName),
				IndoorMapData.CONFIG_FILE_PATH,
				PoiOfflineData.playhouseTableName,                     		
				false,      // Open after download
				"",
				false, //useHandler
				false);// Use Thread	
		Util.downFile(StartupActivity.this,
				Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, PoiOfflineData.poiTableName),
				IndoorMapData.CONFIG_FILE_PATH,
				PoiOfflineData.poiTableName,                     		
				false,      // Open after download
				"",
				false, //useHandler
				false);// Use Thread	
		Util.downFile(StartupActivity.this,
				Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, PoiOfflineData.restaurantTableName),
				IndoorMapData.CONFIG_FILE_PATH,
				PoiOfflineData.restaurantTableName,
				false,      // Open after download
				"",
				false, //useHandler
				false);// Use Thread	

        */
        POIManager.loadOfflineData();
	}
}
