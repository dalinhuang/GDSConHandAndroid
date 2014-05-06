package com.winjune.wifiindoor.activity.mapviewer;

import java.io.FileInputStream;
import java.io.InputStream;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.activity.NaviResultActivity;
import com.winjune.wifiindoor.navi.NaviInfo;
import com.winjune.wifiindoor.navi.NaviPath;
import com.winjune.wifiindoor.navi.Navigator;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.VersionOrMapIdRequest;

public class NaviBar {
	
	public static void showNaviBar(final MapViewerActivity mapViewer) {	
		mapViewer.runOnUiThread(new Runnable() {
			  public void run() {
				    NaviBar.createNaviBar(mapViewer);	
			  }
		});
	}
	
	public static void loadNaviInfo(MapViewerActivity mapViewer) {
		
		mapViewer.myNavigator = new Navigator();
		
		boolean updateNeeded = true; //Hoare: update every time regardless map versionn, for test only

		try {
			NaviInfo naviInfo = new NaviInfo();

			InputStream map_file_is = new FileInputStream(Util.getNaviInfoFilePathName(""+Util.getRuntimeIndoorMap().getMapId()));
			
			naviInfo.fromXML(map_file_is);
			// file has already been closed
			//map_file_is.close();
			
			// For Files in SD Card but not
			//load_map_rc = designMap.fromXML(IndoorMapData.map_file_path + map_file_name);
			
			if (naviInfo.getVersionCode() != Util.getRuntimeIndoorMap().getVersionCode()) {
				updateNeeded = true;
			}
			
			// load cached navi info first regardless 
			mapViewer.myNavigator.init(naviInfo, mapViewer.getResources().getString(R.string.navi_meter));
			
		} catch (Exception e) {
			updateNeeded = true;
		}
		
		if (updateNeeded) {
			// Hoare: harcode map_id 1 as the GDSC map
			int mapid = Util.getRuntimeIndoorMap().getMapId();
			
			if (mapid == 1 ) {
				mapid = 2;
			}				
			
			downloadNaviInfo(mapViewer, mapid);
		} 					
	}
	
	private static void downloadNaviInfo(MapViewerActivity mapViewer, int mapId) {
		VersionOrMapIdRequest id = new VersionOrMapIdRequest();
		id.setCode(mapId);

		try {
			
			Gson gson = new Gson();
			String json = gson.toJson(id);
			JSONObject data = new JSONObject(json);

			if (IpsWebService.sendMessage(mapViewer, IpsMsgConstants.MT_NAVI_INFO_QUERY, data)) {
				
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			Util.showToast(mapViewer, "GET NAVI INFO ERROR: " + ex.getMessage(), Toast.LENGTH_LONG);
			ex.printStackTrace();
		}
	}	
	
	
	public static void createNaviBar(final MapViewerActivity mapViewer) {	
				    
		AlertDialog.Builder builder = new AlertDialog.Builder(mapViewer);
					
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(R.string.navi_from_to);
					
		Spinner sipnnerFrom;
		Spinner sipnnerTo;
		ArrayAdapter<String> adapter;
		String[] spinnerNames;
					
					
		if ((mapViewer.myNavigator.naviInfo == null) || (mapViewer.myNavigator.naviInfo.getNodes() == null) 
			|| (mapViewer.myNavigator.naviInfo.getNodes().isEmpty())) {
			builder.setMessage(R.string.navi_no_node);
		} else {
			LayoutInflater inflater = mapViewer.getLayoutInflater();
			View layout = inflater.inflate(R.layout.dialog_navigator_input, (ViewGroup) mapViewer.findViewById(R.id.navi));
			builder.setView(layout);
						
			sipnnerFrom =  (Spinner) layout.findViewById(R.id.from_list);
			sipnnerTo =  (Spinner) layout.findViewById(R.id.to_list);
					
						
			spinnerNames = mapViewer.myNavigator.getNodeSpinnerNames();
			if (spinnerNames == null) {
				builder.setMessage(R.string.navi_no_node);	
			} else {
				spinnerNames[0] = mapViewer.getResources().getString(R.string.navi_my_place);						
						
				adapter = new ArrayAdapter<String>(mapViewer, android.R.layout.simple_spinner_item, spinnerNames);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						
				if (sipnnerFrom != null) {				
					sipnnerFrom.setAdapter(adapter);							
							
					if (mapViewer.naviFromNode != -1) {
						sipnnerFrom.setSelection(mapViewer.naviFromNode);
					}
							
							
					sipnnerFrom.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							mapViewer.naviFromNode = arg2;
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							mapViewer.naviFromNode = -1;
						}
					});			
				} else { //sipnnerFrom != null
					builder.setMessage(R.string.navi_no_node);		
				}
									
				if (sipnnerTo != null) {
				
					sipnnerTo.setAdapter(adapter);
								
					if (mapViewer.naviToNode != -1) {
						sipnnerTo.setSelection(mapViewer.naviToNode);
					}
								
					sipnnerTo.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
	
						@Override
						public void onItemSelected(AdapterView<?> arg0,
												View arg1, int arg2, long arg3) {
							mapViewer.naviToNode = arg2;
						}
	
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							mapViewer.naviToNode = -1;
						}
					});
				} else {// (sipnnerTo != null)
					builder.setMessage(R.string.navi_no_node);
				}
			}// (spinnerNames == null)
		}				   
					
		builder.setPositiveButton(R.string.navi_go, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if ((mapViewer.naviFromNode == -1) || (mapViewer.naviToNode == -1) 
					|| (mapViewer.naviFromNode == mapViewer.naviToNode)) {
					Util.showLongToast(mapViewer, R.string.navi_node_select_wrong);
				} else {
					goNavigator(mapViewer);
				}
			}
		});

		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mapViewer.naviFromNode = -1;
				mapViewer.naviToNode = -1;
			}
		});
					
		builder.create();
		builder.show();			
	}
	
	// TODO: how to display the navi. result?
	private static void  goNavigator(MapViewerActivity mapViewer) {
		String naviStr = "";
		
		if (((mapViewer.naviMyPlaceX == -1) || (mapViewer.naviMyPlaceY == -1)) 
			&& ((mapViewer.naviFromNode == 0) || (mapViewer.naviToNode == 0))) {
			naviStr = mapViewer.getResources().getString(R.string.navi_my_place_unknown);
			showNavigatorViewer(mapViewer, naviStr);
			return;
		}
		
		int fromNode = getNaviNodeIdFromSpinnerId(mapViewer, mapViewer.naviFromNode);
		int toNode = getNaviNodeIdFromSpinnerId(mapViewer, mapViewer.naviToNode);

		if ((fromNode == -1) || (toNode == -1)) {
			naviStr = mapViewer.getResources().getString(R.string.navi_failed_no_data);
			showNavigatorViewer(mapViewer, naviStr);
			return;
		} 
		
		if ((mapViewer.myNavigator.naviInfo == null) || (mapViewer.myNavigator.naviInfo.getNodes() == null) 
			|| (mapViewer.myNavigator.naviInfo.getPaths() == null)) {
			naviStr = mapViewer.getResources().getString(R.string.navi_failed_no_data);
			showNavigatorViewer(mapViewer, naviStr);
			return;
		}
		
		NaviPath bestRoute = mapViewer.myNavigator.getShortestPath(fromNode, toNode);
		
		if (bestRoute == null) {
			naviStr = mapViewer.getResources().getString(R.string.navi_failed_no_route);
			showNavigatorViewer(mapViewer, naviStr);
			return;
		}
		
		naviStr += mapViewer.myNavigator.getSpinnerName(mapViewer.naviFromNode) + " ->->->->-> " +
					mapViewer.myNavigator.getSpinnerName(mapViewer.naviToNode) + "\n";
		
		naviStr += mapViewer.getResources().getString(R.string.navi_total_distance) + bestRoute.getDist() 
					+ mapViewer.getResources().getString(R.string.navi_meter);
		naviStr += "\n\n";
		
		if (mapViewer.naviFromNode == 0) {
			naviStr += mapViewer.getResources().getString(R.string.navi_my_place) + " ->-> " 
						+ mapViewer.myNavigator.getSpinnerName(mapViewer.naviFromNode) + "\n";
		}

		
		naviStr += bestRoute.getPathDesc();
		if (mapViewer.naviToNode == 0) {
			naviStr += mapViewer.myNavigator.getNodeName(fromNode) + " ->-> " 
					   + mapViewer.getResources().getString(R.string.navi_my_place) + "\n";
		}		
		
		naviStr += mapViewer.getResources().getString(R.string.navi_over) + "\n";		
		
		showNavigatorViewer(mapViewer, naviStr);
	}

	private static void showNavigatorViewer(MapViewerActivity mapViewer, String naviStr) {
		Intent intent_navigator = new Intent(mapViewer, NaviResultActivity.class); 
		Bundle mBundle = new Bundle(); 
		
		mBundle.putString(IndoorMapData.BUNDLE_KEY_NAVI_RESULT, naviStr);
		intent_navigator.putExtras(mBundle); 
		mapViewer.startActivity(intent_navigator);
	}
	
	private static int getNaviNodeIdFromSpinnerId (MapViewerActivity mapViewer, int spinnerId) {
		if ((mapViewer.myNavigator.naviInfo == null) || (mapViewer.myNavigator.naviInfo.getNodes() == null)) {
			return -1;
		}
		
		if (spinnerId == 0) { // My Place
			return mapViewer.myNavigator.getNearestNaviNode(mapViewer.naviMyPlaceX, mapViewer.naviMyPlaceY);
		}		
				
		return mapViewer.myNavigator.getNodeIdBySpinnerIdx(spinnerId);
	}		
	
	// Handle the reply Message for Navi. Info update
	public static void setNaviInfo(MapViewerActivity mapViewer, NaviInfo naviInfo) {
		if (naviInfo == null) {
			return;
		}			
			
		mapViewer.myNavigator.init(naviInfo, mapViewer.getResources().getString(R.string.navi_meter));
		
		// Store into file
		naviInfo.toXML();
	}	
}
