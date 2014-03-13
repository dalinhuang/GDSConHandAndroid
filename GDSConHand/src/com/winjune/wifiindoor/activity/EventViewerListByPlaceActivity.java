package com.winjune.wifiindoor.activity;


import java.util.ArrayList;
import java.util.Set;

import com.winjune.wifiindoor.R;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.winjune.wifiindoor.event.*;

public class EventViewerListByPlaceActivity extends ExpandableListActivity{
		private EventManager eventManager;
		
	
	  	@Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState); 
	        
	        eventManager = new EventManager();
	          
	        setContentView(R.layout.event_list);  
	          
	        PlaceAdapter ada=new PlaceAdapter(); //Adapter1的定义下面，自定义视图是由它实现的  
	        setListAdapter((ExpandableListAdapter) ada);   
	    }  
	  	    
	    public class PlaceAdapter extends BaseExpandableListAdapter {   
		      
	    	private int[] placesNoGroup;
	    	private ArrayList<ArrayList<EventItem>>  eventsListChild;
	        
	        
	        public PlaceAdapter() {
	        	placesNoGroup = eventManager.getEventPlaces();
	        	eventsListChild = new ArrayList<ArrayList<EventItem>>();
	        	
	        	for (int i=0; i<placesNoGroup.length; i++) {
		        	ArrayList<EventItem> eventsToday;			        	
		        	
	        		eventsToday = eventManager.getTodayEventListByPlace(placesNoGroup[i]);
	        		eventsListChild.add(eventsToday);	        		
	        	}	        	
	        }
	          
	        @Override  
	        public Object getChild(int groupPosition, int childPosition) {
	        	ArrayList<EventItem> eventsToday;
	        	
	        	eventsToday = eventsListChild.get(groupPosition);
	        	
	            return eventsToday.get(childPosition); 
	        }  
	      
	        @Override  
	        public long getChildId(int groupPosition, int childPosition) {  
	            return childPosition; //  
	        }  
	      
	        @Override  
	        public int getChildrenCount(int groupPosition) { 
	        	ArrayList<EventItem> eventsToday;
	        	
	        	eventsToday = eventsListChild.get(groupPosition);
	        	
	        	if (eventsToday != null) {	        	
	        		return eventsToday.size(); 
	        	}
	        	
	        	return 0;	        	
	        }  
	      
	        @Override  
	        public View getChildView(int groupPosition, int childPosition,  boolean isLastChild, View convertView, ViewGroup parent) {  
	            LayoutInflater inflate=LayoutInflater.from(EventViewerListByPlaceActivity.this);  
	            View view=inflate.inflate(R.layout.event_list_child, null); //用childlayout这个layout作为条目的视图  
	              	              
	            TextView name=(TextView)view.findViewById(R.id.EventTitle); //childlayout有一个用于显示名字的视图  
	        	
	            ArrayList<EventItem> eventsToday;	  
	        	
	        	eventsToday = eventsListChild.get(groupPosition);
	        	
	        	EventItem thisEvent = eventsToday.get(childPosition);
	        		            
	        	if (thisEvent != null)
	        		name.setText(thisEvent.getTitle());

	            String eventInfo = eventManager.getEventTimeInfo(thisEvent);
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
	              
	            LayoutInflater inflate=LayoutInflater.from(EventViewerListByPlaceActivity.this);  
	            View view=inflate.inflate(R.layout.event_list_group, null);    
	              
	            TextView groupName=(TextView)view.findViewById(R.id.GroupTitle);
	            String groupTitle = eventManager.getPlaceLabel(placesNoGroup[groupPosition]);
	            groupName.setText(groupTitle);  
	              
	            TextView groupCount=(TextView)view.findViewById(R.id.EventCount);  
	            groupCount.setText(": "+ eventsListChild.get(groupPosition).size());  
	              
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
