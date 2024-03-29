package com.winjune.wifiindoor.activity.poiviewer;

import java.util.List;

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
import com.winjune.wifiindoor.lib.poi.BusLineR;
import com.winjune.wifiindoor.poi.BusStation;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.util.Constants;

public class BusStationInfoActivity extends POIBaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle mBundle = getIntent().getExtras();
		poiId = mBundle.getInt(Constants.BUNDLE_KEY_POI_ID);
		poi = POIManager.getPOIbyId(poiId);
		
		setContentView(R.layout.activity_bus_station_info);
	
		ListView lv = (ListView)findViewById(R.id.bus_line_list);	

		updateTitleInfo();		
		
		BusLineList ada = new BusLineList(this, R.layout.list_event_by_time, ((BusStation)poi).getBusLines());
		
		lv.setAdapter(ada);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(BusStationInfoActivity.this, BusLineInfoActivity.class);

				Bundle mBundle = new Bundle(); 
				mBundle.putInt(BusLineInfoActivity.BUNDLE_KEY_POI_ID, poi.id);
				mBundle.putSerializable(BusLineInfoActivity.BUNDLE_KEY_BUS_LINE_IDX, position);
				
				i.putExtras(mBundle); 
				
			    startActivity(i);   				
			}			
		});		
	}
	  
	 public class BusLineList extends ArrayAdapter<BusLineR> {

			private Context context;
			private List<BusLineR> items;
			 
			public BusLineList(Context context, int resource, List<BusLineR> items) {
				super(context, resource, items);
				this.context = context;
				this.items = items;
				// TODO Auto-generated constructor stub
			}
			
		    @Override  
		    public View getView(int position, View convertView, ViewGroup parent){  	
		    	View view = convertView;
		    	
		    	if (view ==  null){
		    		LayoutInflater vi = LayoutInflater.from(context);  
		    		view=vi.inflate(R.layout.list_bus_lines, parent, false);
		    	}
				
				BusLineR aBL = items.get(position);
				TextView lineName = (TextView)view.findViewById(R.id.bus_line_name);
				lineName.setText(aBL.lineName);
				
				TextView lineText = (TextView)view.findViewById(R.id.bus_line_text);
				lineText.setText(aBL.getStartEndInfo());
					        
		        return view;  
		    }   		
		}


}
