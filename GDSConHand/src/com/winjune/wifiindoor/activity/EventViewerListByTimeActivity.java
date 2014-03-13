package com.winjune.wifiindoor.activity;


import java.util.ArrayList;
import com.winjune.wifiindoor.R;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.winjune.wifiindoor.event.*;

public class EventViewerListByTimeActivity extends ExpandableListActivity{
		private EventManager eventManager;
		
	
	  	@Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState); 
	        
	        eventManager = new EventManager();
	          
	        setContentView(R.layout.event_list);  
	          
	        TimeAdapter ada=new TimeAdapter(); //Adapter1的定义下面，自定义视图是由它实现的  
	        setListAdapter((ExpandableListAdapter) ada);   
	    }  
	  
	    public class TimeAdapter extends BaseExpandableListAdapter {   
	      
	        private EventTime timeListGroup[];   
	    	private ArrayList<ArrayList<EventItem>>  eventsListChild;
  
	    	public TimeAdapter(){
	    		int adjustedCloseHour = EventTime.CLOSE_HOUR;
	    		
	    		if ( EventTime.CLOSE_MIN > 0)
	    			adjustedCloseHour ++;
	    		
	    		// EventManager.PANNEL_TIME_STEP - 1 is used to adjust the num
	    		int timeListNum = (adjustedCloseHour - EventTime.OPEN_HOUR + EventManager.PANNEL_TIME_STEP - 1)/EventManager.PANNEL_TIME_STEP;
	    		
	    		timeListGroup = new EventTime[timeListNum];
	    		eventsListChild = new ArrayList<ArrayList<EventItem>>();
	    		
	    		int fromTime = EventTime.OPEN_HOUR;
	    		int	toTime;
	    		for (int i=0; i<timeListNum; i++) {
	    			toTime = fromTime + EventManager.PANNEL_TIME_STEP;
	    			
	    			timeListGroup[i] = new EventTime(fromTime, 0, toTime, 0);	    			
	    			ArrayList<EventItem> eventsByTime = eventManager.getTodayEventListByTime(fromTime, toTime);	
	    			
	    			eventsListChild.add(eventsByTime);
	    			
	    			fromTime = toTime;	    			
	    		}
	    		
	    	}
	          
	        @Override  
	        public Object getChild(int groupPosition, int childPosition) {
	        	ArrayList<EventItem> eventsByTime;
	        	
	        	eventsByTime = eventsListChild.get(groupPosition);
	        	
	            return eventsByTime.get(childPosition); 
	        }  
	      
	        @Override  
	        public long getChildId(int groupPosition, int childPosition) {  
	            return childPosition; //  
	        }  
	      
	        @Override  
	        public int getChildrenCount(int groupPosition) {  
	        	ArrayList<EventItem> eventsByTime;
	        	
	        	eventsByTime = eventsListChild.get(groupPosition);
	        	
	        	if (eventsByTime != null) {	        	
	        		return eventsByTime.size(); 
	        	}
	        	
	        	return 0;		        
	        }  
	      
	        @Override  
	        public View getChildView(int groupPosition, int childPosition,  boolean isLastChild, View convertView, ViewGroup parent) {  
	            //重点在这里  
	            LayoutInflater inflate=LayoutInflater.from(EventViewerListByTimeActivity.this);  
	            View view=inflate.inflate(R.layout.event_list_child, null); //用childlayout这个layout作为条目的视图  
	            
	            ArrayList<EventItem> eventsByTime;	  
	        	
	            eventsByTime = eventsListChild.get(groupPosition);
	        	
	        	EventItem thisEvent = eventsByTime.get(childPosition);
	        	
	            String eventTitle = "";
	        	// Title + Place
	            if (thisEvent != null) {
	            	int placeNo = thisEvent.getPlaceNo();
	            	
	        		eventTitle = thisEvent.getTitle() +" - " + eventManager.getPlaceLabel(placeNo) ;
	        	}
	            
	       
	            TextView name=(TextView)view.findViewById(R.id.EventTitle); //childlayout有一个用于显示名字的视图  
	            name.setText(eventTitle); //给这个视图数据  
	            
	            String eventInfo = eventManager.getEventTimeInfo(thisEvent);
	            TextView description=(TextView)view.findViewById(R.id.EventInfo); //childlayout有一个用于显示描述的视图，在name视图的下面，  
	            description.setTextKeepState(eventInfo);  //这里只是简单的把它的数据设为description  
	              	              
	            return view;  
	        }  
	      
	        @Override  
	        public Object getGroup(int groupPosition) {  
	            return timeListGroup[groupPosition];  
	        }  
	      
	        @Override  
	        public int getGroupCount() {  
	            return timeListGroup.length;  
	        }  
	      
	        @Override  
	        public long getGroupId(int groupPosition) {  
	            return groupPosition;  
	        }  
	      
	        //父列表中的某一项的View  
	        @Override  
	        public View getGroupView(int groupPosition, boolean isExpanded,  View convertView, ViewGroup parent) {  
	            //这里的处理方法和getChildView()里的类似，不再重复说了  
	              
	            LayoutInflater inflate=LayoutInflater.from(EventViewerListByTimeActivity.this);  
	            View view=inflate.inflate(R.layout.event_list_group, null);    
	            
	            String timeLabel = timeListGroup[groupPosition].toString();
	            TextView groupName=(TextView)view.findViewById(R.id.GroupTitle);  
	            groupName.setText(timeLabel);  
	              
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
