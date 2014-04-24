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
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.BusStationInfoActivity.BusLineList;
import com.winjune.wifiindoor.dummy.DummyContent;
import com.winjune.wifiindoor.map.IndoorMap;
import com.winjune.wifiindoor.poi.BusLine;
import com.winjune.wifiindoor.poi.BusStation;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.util.IndoorMapData;

public class BusLineInfoActivity extends Activity {
	
	public static String BUNDLE_KEY_POI_ID = "POI_ID";
	public static String BUNDLE_KEY_BUS_LINE_IDX = "LINE_IDX";
	
	private int poiId;
	private int lineIdx;	
	private BusStation poi;
	private BusLine busline;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle mBundle = getIntent().getExtras();
		poiId = mBundle.getInt(BUNDLE_KEY_POI_ID);
		 lineIdx = mBundle.getInt(BUNDLE_KEY_BUS_LINE_IDX);

		poi = (BusStation)POIManager.getPOIbyId(poiId);
		busline = poi.getBusLine(lineIdx);		
		
		setContentView(R.layout.activity_bus_line_info);
		
		TextView titleText = (TextView)findViewById(R.id.title_text);
		titleText.setText(busline.lineName);
		
		TextView lineInfo = (TextView)findViewById(R.id.bus_line_info);
		lineInfo.setText(busline.lineName + busline.getStartEndInfo());
					
		ListView lv = (ListView)findViewById(R.id.bus_stop_list);
				
		BusStopList ada = new BusStopList(this, R.layout.list_bus_stops, busline.getStopList());
		
		lv.setAdapter(ada);				
	}
	
	
	public void backClick(View v) {
	    	onBackPressed();    	
	}  
	 
	public class BusStopList extends ArrayAdapter<String> {
		private Context context;
		private List<String> items; 
			
		public BusStopList(Context context, int resource, List<String> items) {
			super(context, resource, items);
			this.context = context;
			this.items = items;
			// TODO Auto-generated constructor stub
		}
			
		@Override  
		public View getView(int position, View convertView, ViewGroup parent){  
			LayoutInflater vi = LayoutInflater.from(context);  
	 
			View view=vi.inflate(R.layout.list_bus_stops, null);
			
			TextView stopNo = (TextView)view.findViewById(R.id.bus_stop_no);
			String stopNoText = ""+ (position+1);
			stopNo.setText(stopNoText);
			
			TextView stopName = (TextView)view.findViewById(R.id.bus_stop_name);
			stopName.setText(items.get(position));			
					        
		    return view;  
		}   		
	}	

}
