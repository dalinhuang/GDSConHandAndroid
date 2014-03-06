package com.winjune.wifiindoor.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;
import com.winjune.wifiindoor.version.ApkVersionManager;
import com.winjune.wifiindoor.version.ISoftwareVersions;
import com.winjune.wifiindoor.version.SoftwareVersionData;

public class StartupActivity extends FragmentActivity {
	           
	@Override
    protected void onPause(){
		
		Util.setEnergySave(true);
		
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		Util.setEnergySave(true);
				
	    Util.cancelToast();
	    
	    super.onDestroy();
	}
	
	@Override
	protected void onStop(){    
				    
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
		
		if (ApkVersionManager.isApkUpdatePending()) {
			ApkVersionManager.doNewVersionUpdate(this);
		}		
		
		Util.setCurrentForegroundActivity(this); 
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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

    }
	
	// Called in the onPostExecute of AsyncTask
	private void jumpToRightEntry() {
        
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
		
		Util.connetcToServer(this);
		
		// Check latest version
		ApkVersionManager.CheckVersionUpgrade(this);
		
	}
}
