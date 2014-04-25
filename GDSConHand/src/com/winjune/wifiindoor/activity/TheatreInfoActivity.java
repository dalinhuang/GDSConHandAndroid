package com.winjune.wifiindoor.activity;

import java.util.List;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.R.menu;
import com.winjune.wifiindoor.adapter.ScheduleTimeList;
import com.winjune.wifiindoor.poi.BusStation;
import com.winjune.wifiindoor.poi.MovieInfo;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.TheatreInfo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TheatreInfoActivity extends PoiBaseActivity {
	public static String BUNDLE_KEY_POI_ID = "POI_ID";	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle mBundle = getIntent().getExtras();
		poiId = mBundle.getInt(BUNDLE_KEY_POI_ID);			
		
		setContentView(R.layout.activity_theatre_info);
		
		ListView lv = (ListView)findViewById(R.id.movie_list);
		
		poi = (TheatreInfo)POIManager.getPOIbyId(poiId);
		
		UpdateTitleInfo();
		
		UpdateDesc();
		
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
	
	public void backClick(View v){
		onBackPressed();
	}

	public class MovieList extends ArrayAdapter<MovieInfo> {
		private Context context;
		private List<MovieInfo> items;
		 
		public MovieList(Context context, int resource, List<MovieInfo> items) {
			super(context, resource, items);
			this.context = context;
			this.items = items;
		}
		
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent){  
	        LayoutInflater vi = LayoutInflater.from(context);  
 
			View view=vi.inflate(R.layout.list_event_by_time, null);
			
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
