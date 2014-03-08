package com.winjune.wifiindoor.activity;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.TabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;   
import android.view.Window;
import android.widget.ListView;
import android.app.AlertDialog;

import java.util.ArrayList;   
import java.util.HashMap; 

import org.json.JSONObject;

import com.winjune.wifiindoor.R;
import com.google.gson.Gson;
import com.winjune.wifiindoor.R.drawable;
import com.winjune.wifiindoor.R.id;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.R.string;
import com.winjune.wifiindoor.map.MapManager;
import com.winjune.wifiindoor.map.MapManagerItem;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.MapManagerReply;
import com.winjune.wifiindoor.webservice.types.VersionOrMapIdRequest;

import android.widget.AdapterView;   
import android.widget.SimpleAdapter; 
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;  

/**
 * @author haleyshi
 *
 */
@SuppressWarnings("deprecation")
public class MapSelectorActivity extends TabActivity{
	private Resources resources;
	private MapManager mapManager;
	private MapManager mapManagerFiltered;
	private ListView updatedListView;
	private ListView outdatedListView;
	private ListView downloadingListView;
	private Bundle bundle;
	
	private int[] mapsInBuilding = null;
	private boolean aBuildingOnly = false;
	
	// Load Map Manager file for the general information
	private void setView(ListView list_view, final int category){
        MapManagerItem map_manager_item = null;
        
        //Generate Arrays, add data for ListView
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();   
        for(int i=0; i<mapManagerFiltered.getMapManagerItemNumber(category); i++)   
        {   
            HashMap<String, Object> map = new HashMap<String, Object>(); 
            
            map_manager_item = mapManagerFiltered.getMapManagerItemByIndex(category, i);

            switch (category){           
            	case IndoorMapData.MAP_FILE_UPDATED:
            		map.put("ItemImage", R.drawable.updated);//Picture Resource ID
            		break;
            	case IndoorMapData.MAP_FILE_OUTDATED:
            		map.put("ItemImage", R.drawable.outdated);//Picture Resource ID
            		break;
            	case IndoorMapData.MAP_FILE_DOWNLOADING:
            		map.put("ItemImage", R.drawable.downloading);//Picture Resource ID
            		break;
            	default:
            }
                    
            map.put("ItemTitle", map_manager_item.getTitle());   
            map.put("ItemText", 
            		getResources().getString(R.string.map_size) + map_manager_item.getRows() + " * " + map_manager_item.getColumns() 
            		+ "    " + getResources().getString(R.string.version) + map_manager_item.getVersion());   
            listItem.add(map);   
        }  
        
        //Generate Adaptor's Item
        SimpleAdapter listItemAdapter = new SimpleAdapter(
        	this, listItem, R.layout.map_list_items,       
            new String[] {"ItemImage", "ItemTitle", "ItemText"},    
            new int[] {R.id.ItemImage, R.id.ItemTitle, R.id.ItemText}   
        );  
          
        list_view.setAdapter(listItemAdapter);   
        list_view.setOnItemClickListener(new OnItemClickListener() {    
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) { 
            	
            	MapManagerItem map_manager_item = mapManagerFiltered.getMapManagerItemByIndex(category, arg2);
            	
            	switch (category){           
	            	case IndoorMapData.MAP_FILE_UPDATED:
	            		
	            		Intent intent_map_locator = new Intent(MapSelectorActivity.this, MapLocatorActivity.class);
	    				Bundle mBundle = new Bundle(); 
	    				mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR);
	    				mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP, map_manager_item.getMapId());
	    				intent_map_locator.putExtras(mBundle); 
	            		startActivity(intent_map_locator);
	            		
	            		// Finish this Activity
	            		finish();
	            		
	            		break;
	            	case IndoorMapData.MAP_FILE_OUTDATED:
	            		AlertDialog alertDialog = new AlertDialog.Builder(arg1.getContext()).create();
	                    alertDialog.setTitle(getResources().getString(R.string.oops));
	                    alertDialog.setMessage(getResources().getString(R.string.map_outdated));
	                    alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
	                       public void onClick(DialogInterface dialog, int which) {
	                          //Do nothing
	                       }
	                    });
	                    
	                    alertDialog.setIcon(R.drawable.warning);
	                    alertDialog.show();
	                    
	            		break;
	            	case IndoorMapData.MAP_FILE_DOWNLOADING:
	            		
	            		break;
	            	default:
	            }          	
            	 
            }   
        });
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Util.setEnergySave(false);
		
		IpsWebService.setActivity(this);
		IpsWebService.activateWebService();
		
		
		Util.setCurrentForegroundActivity(this);
		
		System.gc();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Util.setEnergySave(true);
		
		Util.setCurrentForegroundActivity(null);
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        
        // Start the Ips Message Handler Thread if it has not been started yet.
        IpsWebService.setActivity(this);
        IpsWebService.activateWebService();
        
        // Hidden Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        resources = getResources();
        
        bundle = getIntent().getExtras();
        
        switch (bundle.getInt(IndoorMapData.BUNDLE_KEY_REQ_FROM)) {
        	case IndoorMapData.BUNDLE_VALUE_REQ_FROM_BUILDING:
        		mapsInBuilding = bundle.getIntArray(IndoorMapData.BUNDLE_KEY_BUILDING_MAPS);
        		aBuildingOnly = true;
        		break;
        	case IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR:
        		break;
        	default:        			
        }
        
        // Read from File for the map items
        mapManager = new MapManager();
        
        boolean queried = false;
        if (!mapManager.loadMapManager(this, resources)) {
        	mapManager.setVersionCode(-1);
        	queryMaps(-1);
        	queried = true;
        }
        
        // filter
        if (aBuildingOnly) {
        	mapManagerFiltered = mapManager.getSubMapManager(mapsInBuilding);
        } else {
        	mapManagerFiltered = mapManager;
        }

        //Add Tabs
        TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.map_chooser, tabHost.getTabContentView(), true);
        tabHost.addTab(tabHost.newTabSpec(resources.getString(R.string.map_selector_updated)).setIndicator(resources.getString(R.string.map_selector_updated), resources.getDrawable(R.drawable.updated)).setContent(R.id.updatedListView));
        tabHost.addTab(tabHost.newTabSpec(resources.getString(R.string.map_selector_outdated)).setIndicator(resources.getString(R.string.map_selector_outdated), resources.getDrawable(R.drawable.outdated)).setContent(R.id.outdatedListView));
        tabHost.addTab(tabHost.newTabSpec(resources.getString(R.string.map_selector_downloading)).setIndicator(resources.getString(R.string.map_selector_downloading), resources.getDrawable(R.drawable.downloading)).setContent(R.id.downloadingListView));
        
        // Set Content View
        setContentView(tabHost);
        //setTitle(getResources().getString(R.string.map_selector) + " - " + getResources().getString(R.string.map_selector_campaign)); 
        
        //Define ListView per Tab
        updatedListView = (ListView) findViewById(R.id.updatedListView); 
        outdatedListView = (ListView) findViewById(R.id.outdatedListView);
        downloadingListView = (ListView) findViewById(R.id.downloadingListView);

        setView(updatedListView, IndoorMapData.MAP_FILE_UPDATED);
        setView(outdatedListView, IndoorMapData.MAP_FILE_OUTDATED);
        setView(downloadingListView, IndoorMapData.MAP_FILE_DOWNLOADING);
        
        if (!queried) {
        	queryMaps(mapManager.getVersionCode());
        }
    }

	@Override
    public void onDestroy(){
		if (updatedListView != null)
			updatedListView.setVisibility(View.GONE);
		
		if (outdatedListView != null)
			outdatedListView.setVisibility(View.GONE);
		
		if (downloadingListView != null)
			downloadingListView.setVisibility(View.GONE);
		
    	super.onDestroy();
    }
	
    private void queryMaps(int versionCode) {
    	VersionOrMapIdRequest version = new VersionOrMapIdRequest();
		version.setCode(versionCode);
		
		try {		
			Gson gson = new Gson();
			String json = gson.toJson(version);
			JSONObject data = new JSONObject(json);

			if (IpsWebService.sendMessage(this, IpsMsgConstants.MT_MAP_LIST_QUERY, data)) {
				
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			Util.showToast(this, "GET MAP LIST ERROR: " + ex.getMessage(), Toast.LENGTH_LONG);
			ex.printStackTrace();
		}
	}
    
    public void handleMapListReply(MapManagerReply managerReply) {
    	Log.e("handleMapListReply", "handleMapListReply");
    	
    	if (managerReply == null) {
			return;
		}

		if (mapManager == null) {	
			Log.e("handleMapListReply", "handleMapListReply1.1");
			mapManager = new MapManager();
		} else {
			Log.e("handleMapListReply", "handleMapListReply1.2");
			if (managerReply.getVersionCode() == mapManager.getVersionCode()) {
				return;
			}
		}
		
		Log.e("handleMapListReply", "handleMapListReply1.3");
		mapManager.mergeMaps(managerReply);
		mapManager.setVersionCode(managerReply.getVersionCode());
		
		mapManager.toXML();
		
		// filter
        if (aBuildingOnly) {
        	mapManagerFiltered = mapManager.getSubMapManager(mapsInBuilding);
        }

		setView(updatedListView, IndoorMapData.MAP_FILE_UPDATED);
    }
}


