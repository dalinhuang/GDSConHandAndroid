package com.winjune.wifiindoor.activity;

import java.util.List;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.R.menu;
import com.winjune.wifiindoor.activity.PlayhouseInfoActivity.PlayhouseTimeList;
import com.winjune.wifiindoor.dummy.DummyContent;

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
import android.widget.ListView;
import android.widget.TextView;

public class TheatreInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_info);
		
		ListView lv = (ListView)findViewById(R.id.movie_list);
		
		MovieList ada = new MovieList(this, R.layout.list_event_by_time, DummyContent.ITEMS);
		
		lv.setAdapter(ada);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(TheatreInfoActivity.this, MovieInfoActivity.class); 
				startActivity(i);					
			}
			
		});		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.movie_info, menu);
		return true;
	}

	public class MovieList extends ArrayAdapter<DummyContent.DummyItem> {

		private int resourceId;  
		private Context context;
		 
		public MovieList(Context context, int resource, List<DummyContent.DummyItem> items) {
			super(context, resource, items);
			this.context = context;
			this.resourceId = resource;
			// TODO Auto-generated constructor stub
		}
		
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent){  
	        LayoutInflater vi = LayoutInflater.from(context);  
 
			View view=vi.inflate(R.layout.list_event_by_time, null);
			TextView timeAndPlace = (TextView)view.findViewById(R.id.textView2);
			//timeAndPlace.setText("test test test test");
				        
	        return view;  
	    }  
	}
}
