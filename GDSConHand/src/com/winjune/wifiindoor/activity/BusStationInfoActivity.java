package com.winjune.wifiindoor.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.dummy.DummyContent;

public class BusStationInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_station_info);

		ListView lv = (ListView)findViewById(R.id.bus_line_list);
		
		BusLineList ada = new BusLineList(this, R.layout.list_event_by_time, DummyContent.ITEMS);
		
		lv.setAdapter(ada);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				 Intent i = new Intent(BusStationInfoActivity.this, BusLineInfoActivity.class);
			     startActivity(i);   
				
			}
			
		});
		
	}

	 public void backClick(View v) {
	    	onBackPressed();    	
	    } 
	 
 
	 public void enterBusstop202(View v) {
		 Intent i = new Intent(this, BusLineInfoActivity.class);
	     startActivity(i);   	
	    }   
	 
	 public class BusLineList extends ArrayAdapter<DummyContent.DummyItem> {

			private int resourceId;  
			private Context context;
			 
			public BusLineList(Context context, int resource, List<DummyContent.DummyItem> items) {
				super(context, resource, items);
				this.context = context;
				this.resourceId = resource;
				// TODO Auto-generated constructor stub
			}
			
		    @Override  
		    public View getView(int position, View convertView, ViewGroup parent){  
		        LayoutInflater vi = LayoutInflater.from(context);  
	 
				View view=vi.inflate(R.layout.list_bus_lines, null);
				//timeAndPlace.setText("test test test test");
					        
		        return view;  
		    }   		
		}


}
