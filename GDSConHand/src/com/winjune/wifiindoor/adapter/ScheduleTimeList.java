package com.winjune.wifiindoor.adapter;

import java.util.List;

import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.dummy.DummyContent;


public class ScheduleTimeList extends ArrayAdapter<DummyContent.DummyItem> {

		private int resourceId;  
		private Context context;
		 
		public ScheduleTimeList(Context context, int resource, List<DummyContent.DummyItem> items) {
			super(context, resource, items);
			this.context = context;
			this.resourceId = resource;
			// TODO Auto-generated constructor stub
		}
		
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent){  
	        LayoutInflater vi = LayoutInflater.from(context);  
 
			View view=vi.inflate(R.layout.list_schedule, null);
			//timeAndPlace.setText("test test test test");
				        
	        return view;  
	    }   		
	}