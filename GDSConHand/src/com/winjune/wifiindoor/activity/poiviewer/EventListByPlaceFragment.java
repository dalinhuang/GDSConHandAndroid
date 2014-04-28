package com.winjune.wifiindoor.activity.poiviewer;


import java.util.ArrayList;

import com.winjune.wifiindoor.R;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlayhouseInfo;
import com.winjune.wifiindoor.poi.EventManager;

public class EventListByPlaceFragment extends Fragment {
		
	private EventManager eventManager;
	private ArrayList<ArrayList<PlayhouseInfo>>  eventsListChild;
		
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle  savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_event_list, null);

		return v;
	}
	
	@Override  
	public void onActivityCreated(Bundle savedInstanceState) {  
	  		super.onActivityCreated(savedInstanceState);
	  		
	        eventManager = new EventManager();
	        
	        ExpandableListView lv = (ExpandableListView) getActivity().findViewById(R.id.event_list_by_place);
	        // Set up our adapter
	        
	        PlaceAdapter ada=new PlaceAdapter(); //Adapter1的定义下面，自定义视图是由它实现的  
	        
	        
	        lv.setAdapter((ExpandableListAdapter) ada); 
	        
	        lv.setOnChildClickListener((new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView arg0, View arg1,
						int groupId, int childId, long arg4) {
					// TODO Auto-generated method stub
								
					Intent i = new Intent(getActivity(), PlayhouseInfoActivity.class);
					Bundle bundle = new Bundle();
					
					bundle.putInt(PlayhouseInfoActivity.BUNDLE_KEY_POI_ID, eventsListChild.get(groupId).get(childId).id);
					i.putExtras(bundle);
					startActivity(i);					
					return true;
				}
	        	
	        }));
	        
	 }  
	  	    
	 public class PlaceAdapter extends BaseExpandableListAdapter {   
		      
	    	private Integer[] placesNoGroup;       
	        
	        public PlaceAdapter() {
	        	placesNoGroup = EventManager.getHallsWithPlayhouse();
	        	eventsListChild = new ArrayList<ArrayList<PlayhouseInfo>>();
	        	
	        	for (int i=0; i<placesNoGroup.length; i++) {
		        	ArrayList<PlayhouseInfo> eventsToday;			        	
		        	
	        		eventsToday = eventManager.getTodayEventListByHall(placesNoGroup[i]);
	        		eventsListChild.add(eventsToday);	        		
	        	}	        	
	        }
	          
	        @Override  
	        public Object getChild(int groupPosition, int childPosition) {
	        	ArrayList<PlayhouseInfo> eventsToday;
	        	
	        	eventsToday = eventsListChild.get(groupPosition);
	        	
	            return eventsToday.get(childPosition); 
	        }  
	      
	        @Override  
	        public long getChildId(int groupPosition, int childPosition) {  
	            return childPosition; //  
	        }  
	      
	        @Override  
	        public int getChildrenCount(int groupPosition) { 
	        	ArrayList<PlayhouseInfo> eventsToday;
	        	
	        	eventsToday = eventsListChild.get(groupPosition);
	        	
	        	if (eventsToday != null) {	        	
	        		return eventsToday.size(); 
	        	}
	        	
	        	return 0;	        	
	        }  
	      
	        @Override  
	        public View getChildView(int groupPosition, int childPosition,  boolean isLastChild, View convertView, ViewGroup parent) {  
	            LayoutInflater inflate=LayoutInflater.from(getActivity());  
	            View view=inflate.inflate(R.layout.list_event_child_by_place, null); //用childlayout这个layout作为条目的视图  
	              	              
	            TextView name=(TextView)view.findViewById(R.id.EventTitle); //childlayout有一个用于显示名字的视图  
	        	
	            ArrayList<PlayhouseInfo> eventsToday;	  
	        	
	        	eventsToday = eventsListChild.get(groupPosition);
	        	
	        	PlayhouseInfo thisEvent = eventsToday.get(childPosition);
	        		            
	        	if (thisEvent != null)
	        		name.setText(thisEvent.getLabel());

	            String eventInfo = getResources().getString(R.string.time);  
	            eventInfo += ": "+ thisEvent.getTodayScheduleInfo();
	            TextView description=(TextView)view.findViewById(R.id.EventInfo); //childlayout有一个用于显示描述的视图，在name视图的下面，  
	            description.setTextKeepState(eventInfo);  //这里只是简单的把它的数据设为description  
	              	              
	            return view;  
	        }  
	      
	        @Override  
	        public Object getGroup(int groupPosition) {  	        	
	            return placesNoGroup[groupPosition];  
	        }  
	      
	        @Override  
	        public int getGroupCount() {  
	            return placesNoGroup.length;  
	        }  
	      
	        @Override  
	        public long getGroupId(int groupPosition) {  
	            return groupPosition;  
	        }  
	      
	        //父列表中的某一项的View  
			@Override  
	        public View getGroupView(int groupPosition, boolean isExpanded,  View convertView, ViewGroup parent) {  
	              
	            LayoutInflater inflate=LayoutInflater.from(getActivity());  
	            View view=inflate.inflate(R.layout.list_event_group_by_place, null);    
	              
	            TextView groupName=(TextView)view.findViewById(R.id.GroupTitle);
	            String groupTitle = POIManager.getHallLabel(placesNoGroup[groupPosition]);
	            groupName.setText(groupTitle);  
	              
	            TextView groupCount=(TextView)view.findViewById(R.id.EventCount);  
	            groupCount.setText(": "+ eventsListChild.get(groupPosition).size());  
	            
	            TextView prompt=(TextView)view.findViewById(R.id.EventPlaceExpand);
	            if (isExpanded) {
	            	view.setBackgroundColor(getResources().getColor(R.color.white));	            	
	            	prompt.setText("收起");
	            } else {
	            	view.setBackgroundColor(getResources().getColor(R.color.grey_light_light));
	            	prompt.setText("展开");
	            }
	            
	            	              
	            return view;  
	        }  
	      
	        @Override  
	        public boolean hasStableIds() {  
	            return true;  
	        }  
	      
	        @Override  
	        public boolean isChildSelectable(int groupPosition, int childPosition) {  
	            return true;  
	        }  
	          
	    }  
	    
}
