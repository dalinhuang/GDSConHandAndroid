package com.winjune.wifiindoor.navi;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.color;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.activity.NaviResultActivity;
import com.winjune.wifiindoor.activity.mapviewer.NaviBar;
import com.winjune.wifiindoor.lib.map.MapDataR;
import com.winjune.wifiindoor.lib.map.NaviNodeR;
import com.winjune.wifiindoor.map.MapManager;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.Util;

public class NaviContext implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1905197474553939983L;
	
	public String text;
	public ArrayList<NaviNodeR> naviRoute;
	
	private ArrayList<String> stepNames = null;
	private ArrayList<Integer> mapIds = null;
	
	public NaviContext(String text){
		this.text = text;
		naviRoute = new ArrayList<NaviNodeR>();
	}
	
	public void showContextMenu(final MapViewerActivity mapViewer){
		LayoutInflater inflater = LayoutInflater.from(mapViewer); 
		View view = inflater.inflate(R.layout.popup_navi_context, null); 
		
		final PopupWindow pop = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false); 		  				
		pop.setBackgroundDrawable(new BitmapDrawable()); 
        pop.setOutsideTouchable(true); 
        pop.setFocusable(true);

        TextView labelText = (TextView) pop.getContentView().findViewById(R.id.text_label);                
        labelText.setText("路线概述");
        
        View detailBtn = (View) pop.getContentView().findViewById(R.id.btn_detail);
        detailBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				Intent resultI = new Intent(mapViewer, NaviResultActivity.class);

				Bundle mBundle = new Bundle(); 
				mBundle.putSerializable(Constants.BUNDLE_KEY_NAVI_CONTEXT, NaviContext.this);
				resultI.putExtras(mBundle);
				mapViewer.startActivity(resultI);				
			}
        	
        });        
        
        if (stepNames == null)
        	stepNames = getStepNames();
        
        if (mapIds == null)
        	mapIds = getMapIds();
              
        final ListView lv = (ListView) pop.getContentView().findViewById(R.id.route_map_list);
		ArrayAdapter<String> stepsAda = new RouteMapList(mapViewer, R.layout.list_route_map);		
		lv.setAdapter(stepsAda);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
				// TODO Auto-generated method stub
				if (position < mapIds.size()){
					int mMapId = mapIds.get(position);
					if (mMapId != Util.getRuntimeIndoorMap().getMapId()){
						
						final MapDataR mapData = MapManager.getMapById(mMapId);
						mapViewer.switchRuntimeMap(mapData);

						mapViewer.refreshMapLabel(mapData.getLabel());
						NaviBar.showNaviResulOnMap(mapViewer, NaviContext.this);
					}
					
				}
				
			}
		});

        pop.showAtLocation(mapViewer.getCurrentFocus(), Gravity.BOTTOM, 0, 0);		        
        
	}	
	
	private ArrayList<Integer> getMapIds(){
		ArrayList<Integer> mapIds = new ArrayList<Integer>();

		int currentMapId 	= naviRoute.get(0).getMapId();
		mapIds.add(currentMapId);
				
		for (int i = 0; i < naviRoute.size(); i++) {
			int mMapId = naviRoute.get(i).getMapId();
			
			if (mMapId != currentMapId) {
				currentMapId = mMapId;
				mapIds.add(currentMapId);
			}
		}		
		
		return mapIds;		
	}
	
	private ArrayList<String> getStepNames(){ 
		ArrayList<String> stepNames = new ArrayList<String>();

		int currentMapId 	= naviRoute.get(0).getMapId();
		int firstNodeOfMap	=  0;
		int lastNodeOfMap	= -1;
				
		for (int i = 1; i < naviRoute.size(); i++) {
			int mMapId = naviRoute.get(i).getMapId();
			
			if (mMapId != currentMapId) {
				String stepName = MapManager.getMapById(currentMapId).getLabel()+": ";
				
				stepName += naviRoute.get(firstNodeOfMap).getLabel();
				
				// last node
				if (lastNodeOfMap != -1)
					stepName += " - " + naviRoute.get(lastNodeOfMap).getLabel();
				
				stepNames.add(stepName);
								
				// jump to the next map
				currentMapId = mMapId;
				firstNodeOfMap = i;
			} else {							
				lastNodeOfMap = i;
			}
		}

		String stepName = MapManager.getMapById(currentMapId).getLabel()+": ";
		
		stepName += naviRoute.get(firstNodeOfMap).getLabel();
		
		// last node
		if (lastNodeOfMap != -1)
			stepName += " - "  + naviRoute.get(lastNodeOfMap).getLabel();
		
		stepNames.add(stepName);		
		
		
		return stepNames;
	}
	
	
	public class RouteMapList extends ArrayAdapter<String> {
		private Context context;
			
		public RouteMapList(Context context, int resource) {
			super(context, resource, stepNames);
			this.context = context;
			// TODO Auto-generated constructor stub
		}
			
		@Override  
		public View getView(int position, View convertView, ViewGroup parent){  
			View view = convertView;
			
			if (view == null) {
				LayoutInflater vi = LayoutInflater.from(context);  	 
				view=vi.inflate(R.layout.list_route_map, parent, false);
			}
			
			TextView stepTV = (TextView)view.findViewById(R.id.route_map_label);
			
			stepTV.setText(stepNames.get(position));			

			return view;  
		}   		
	}		

}
