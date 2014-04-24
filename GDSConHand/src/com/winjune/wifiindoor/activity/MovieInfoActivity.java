package com.winjune.wifiindoor.activity;

import java.util.ArrayList;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.adapter.ScheduleTimeList;
import com.winjune.wifiindoor.dummy.DummyContent;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.TheatreInfo;
import com.winjune.wifiindoor.poi.MovieInfo;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MovieInfoActivity extends Activity {
	
	public static String BUNDLE_KEY_POI_ID = "POI_ID";
	public static String BUNDLE_KEY_MOVIE_IDX = "MOVIE_IDX";	
	
	private int poiId;
	private int movieIdx;
	private TheatreInfo poi;
	private MovieInfo movie;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle mBundle = getIntent().getExtras();
		poiId = mBundle.getInt(BUNDLE_KEY_POI_ID);
		movieIdx = mBundle.getInt(BUNDLE_KEY_MOVIE_IDX);	
		
		poi = (TheatreInfo)POIManager.getPOIbyId(poiId);
		movie = poi.getMovieByIdx(movieIdx);
		
		setContentView(R.layout.activity_movie_info);
		attachScheduleView();
		refreshMovieInfo();
				
		final Gallery gallery = (Gallery) findViewById(R.id.gallery);		
		MovieGalleryAdapter galleryAda = new MovieGalleryAdapter();

		gallery.setAdapter(galleryAda);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				movieIdx = position;
				movie = poi.getMovieByIdx(movieIdx);
				refreshMovieInfo();				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent){
			}
		});
			
	}
	
	public void backClick(View v){
		onBackPressed();
	}
	
	public void attachScheduleView(){	
		
		View mainLayout = findViewById(R.id.movie_main_layout);
		
		LayoutInflater vi = getLayoutInflater();  		
		vi.inflate(R.layout.layout_movie_schedule, (ViewGroup) mainLayout);		
	}
	
	
		
	
	public void refreshMovieInfo(){
		
		TextView titleText = (TextView)findViewById(R.id.title_text);
		titleText.setText(movie.name);			

		//TextView movieLabel = (TextView)findViewById(R.id.text_label);
		//movieLabel.setText(movie.name);	

		//TextView movieInfo = (TextView)findViewById(R.id.text_detail);
		//movieInfo.setText(movie.getGeneralDesc());			
		
		ListView lv = (ListView)findViewById(R.id.schedule_list);
		
		ScheduleTimeList ada = new ScheduleTimeList(this, R.layout.list_event_by_time, poi.getMovieByIdx(movieIdx).getTodaySchedule());
		
		lv.setAdapter(ada);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});		
	}
	
	public class MovieGalleryAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return poi.getMovieNum();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return poi.getMovieByIdx(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// 创建一个ImageView
			ImageView imageView = new ImageView(MovieInfoActivity.this);
			imageView.setImageResource(R.drawable.movie_hobbit);
			// 设置ImageView的缩放类型
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(150, 200));
			TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
			imageView.setBackgroundResource(typedArray.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0));
			
			return imageView;
		}
		
	}
}
