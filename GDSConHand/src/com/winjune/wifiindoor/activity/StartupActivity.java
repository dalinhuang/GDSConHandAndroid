package com.winjune.wifiindoor.activity;

import java.util.ArrayList;
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

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.lib.map.MapDataR;
import com.winjune.wifiindoor.lib.poi.PoiOfflineData;
import com.winjune.wifiindoor.map.MapManager;
import com.winjune.wifiindoor.navi.Navigator;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.version.ApkVersionManager;
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
        
		final long startTime = System.currentTimeMillis();

        
        if (AutoGuideTTS == null) {        	
			AutoGuideTTS = new TextToSpeech(this, this);
		}
        
        Util.initApp(this);
        
        IpsWebService.startWebService(StartupActivity.this);
                        
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
        		
        		appStartUp();
        		
        		long completeTime = System.currentTimeMillis();
        		long runTime = completeTime - startTime;
        		
        		if (runTime < 1500) {
        			
        			long sleepTime = 1500 - runTime;
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
                	Util.setTTSSupported(isTTSSupported);
               		                                   
                	startMapViewer();
       		
                	finish();
                }

            }
        	
        }.execute(new Void[]{}); 

    }
	
		
	// Called in AsyncTask when the APP starts up. It should only include the time consuming tasks.
	private void appStartUp() {
		
		// IpsWebService.startWebService(StartupActivity.this);
				
		// Check latest version
		ApkVersionManager.CheckVersionUpgrade(StartupActivity.this);
		
		// Copy all the necessary files from assets if necessary.
		ArrayList<String> fileNames = new ArrayList<String>();
		String fullPath = Util.getFilePath(IndoorMapData.CONFIG_FILE_PATH);
		fileNames.add(fullPath+MapManager.mapTableName);
		fileNames.add(fullPath+PoiOfflineData.buslineTableName + PoiOfflineData.jsonFileExtension);
		fileNames.add(fullPath+PoiOfflineData.festivalTableName + PoiOfflineData.jsonFileExtension);
		fileNames.add(fullPath+PoiOfflineData.movieTableName + PoiOfflineData.jsonFileExtension);
		fileNames.add(fullPath+PoiOfflineData.playhouseTableName + PoiOfflineData.jsonFileExtension);
		fileNames.add(fullPath+PoiOfflineData.poiTableName + PoiOfflineData.jsonFileExtension);
		fileNames.add(fullPath+PoiOfflineData.restaurantTableName + PoiOfflineData.jsonFileExtension);
		fileNames.add(fullPath+"version_table.json");		
		fileNames.add(fullPath+ Navigator.NaviNodeTableName);		
		fileNames.add(fullPath+Navigator.NaviPathTableName);		
		Util.appFilesPrepare(StartupActivity.this, fileNames);
		/*
		Util.downFile(StartupActivity.this,
				Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, MapManager.mapTableName),
				IndoorMapData.CONFIG_FILE_PATH,
				MapManager.mapTableName,                     		
				false,      // Open after download
				"",
				false, //useHandler
				false);// Use Thread			
		
		Util.downFile(StartupActivity.this,
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
				
		*/		
		Util.downFile(StartupActivity.this,
				Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, PoiOfflineData.poiTableName + PoiOfflineData.jsonFileExtension),
				IndoorMapData.CONFIG_FILE_PATH,
				PoiOfflineData.poiTableName + PoiOfflineData.jsonFileExtension,                     		
				false,      // Open after download
				"",
				false, //useHandler
				false);// Use Thread	
		
		/*
		Util.downFile(StartupActivity.this,
				Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, PoiOfflineData.restaurantTableName),
				IndoorMapData.CONFIG_FILE_PATH,
				PoiOfflineData.restaurantTableName,
				false,      // Open after download
				"",
				false, //useHandler
				false);// Use Thread	        
		*/
		Util.downFile(StartupActivity.this,
				Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, Navigator.NaviNodeTableName),
				IndoorMapData.CONFIG_FILE_PATH,
				Navigator.NaviNodeTableName,                     		
				false,      // Open after download
				"",
				false, //useHandler
				false);// Use Thread		
		
		Util.downFile(StartupActivity.this,
				Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, Navigator.NaviPathTableName),
				IndoorMapData.CONFIG_FILE_PATH,
				Navigator.NaviPathTableName,                     		
				false,      // Open after download
				"",
				false, //useHandler
				false);// Use Thread		
	
		
		MapManager.loadOfflineData(fullPath);		
		
        POIManager.loadOfflineData(fullPath);
        
        Navigator.loadOfflineData(fullPath);
        
        startMapViewer();
        
        finish();
	}
	
	private void startMapViewer() {
		Intent i = new Intent(StartupActivity.this, MapViewerActivity.class); 
		startActivity(i);		
	}	
	
}
