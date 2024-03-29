package com.winjune.wifiindoor.activity.poiviewer;

import java.util.List;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.lib.poi.MovieInfoR;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.poi.TheatreInfo;
import com.winjune.wifiindoor.util.Constants;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TheatreInfoActivity extends POIBaseActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle mBundle = getIntent().getExtras();
		poiId = mBundle.getInt(Constants.BUNDLE_KEY_POI_ID);			
		
		setContentView(R.layout.activity_theatre_info);
		
		ListView lv = (ListView)findViewById(R.id.movie_list);
		
		poi = (TheatreInfo)POIManager.getPOIbyId(poiId);
		
		updateTitleInfo();
			
		MovieList ada = new MovieList(this, R.layout.list_event_by_time, ((TheatreInfo)poi).getMovies());
		
		lv.setAdapter(ada);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(TheatreInfoActivity.this, MovieInfoActivity.class); 
		        
		    	Bundle mBundle = new Bundle(); 
				mBundle.putInt(MovieInfoActivity.BUNDLE_KEY_POI_ID, poi.id);
				mBundle.putInt(MovieInfoActivity.BUNDLE_KEY_MOVIE_IDX, position);
				i.putExtras(mBundle); 	
				
				startActivity(i);					
			}
			
		});						
	}
	
	public void introClick(View v){
        Intent i = new Intent(this, POINormalViewerActivity.class); 

		Bundle mBundle = new Bundle(); 
		mBundle.putInt(Constants.BUNDLE_KEY_POI_ID, poiId);
		i.putExtras(mBundle); 	
		
        v.getContext().startActivity(i);			
	}
	
	public class MovieList extends ArrayAdapter<MovieInfoR> {
		private Context context;
		private List<MovieInfoR> items;
		 
		public MovieList(Context context, int resource, List<MovieInfoR> items) {
			super(context, resource, items);
			this.context = context;
			this.items = items;
		}
		
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent){  
	    	View view = convertView;
	    	
	    	if (view == null){	    	
	    		LayoutInflater vi = LayoutInflater.from(context);   
	    		view=vi.inflate(R.layout.list_event_by_time, parent, false);
	    	}
			
			ImageView icon = (ImageView)view.findViewById(R.id.list_event_icon);
			icon.setImageResource(R.drawable.movie_hobbit_thumbnail);
			
			TextView movieTitle = (TextView)view.findViewById(R.id.event_title);
			movieTitle.setText(items.get(position).name);
			
			TextView movieSchedule = (TextView)view.findViewById(R.id.event_schedule);
			movieSchedule.setText(items.get(position).getTodayScheduleStr());			
				        
	        return view;  
	    }  
	}
}
