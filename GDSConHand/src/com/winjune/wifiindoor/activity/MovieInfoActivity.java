package com.winjune.wifiindoor.activity;

import java.util.ArrayList;
import java.util.Calendar;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.adapter.ScheduleTimeList;
import com.winjune.wifiindoor.poi.EventManager;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.TheatreInfo;
import com.winjune.wifiindoor.poi.MovieInfo;
import com.winjune.wifiindoor.util.Util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import android.widget.Toast;

public class MovieInfoActivity extends Activity {
	
	public static String BUNDLE_KEY_POI_ID = "POI_ID";
	public static String BUNDLE_KEY_MOVIE_IDX = "MOVIE_IDX";	
	
	private int poiId;
	private int movieIdx;
	private TheatreInfo poi;
	private MovieInfo movie;
	
	private boolean introMode = true; // default is to show schedule
	//private boolean todaySchedule = true; // default is to show today's schedule
	
	private AlarmManager mAlarmMgr;
	private int mMinutesAhead;
	
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
		
		mAlarmMgr = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		mMinutesAhead = 5; //By default the remind schedule is 5 minutes
		
		setContentView(R.layout.activity_movie_info);
		contentViewSwitch();
				
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
	
	public void actionClick(View v){
		contentViewSwitch();
	}
	
	public void contentViewSwitch(){
		ViewGroup mainLayout = (ViewGroup) findViewById(R.id.movie_main_layout);	
		LayoutInflater vi = getLayoutInflater();  		
		
		if (introMode) { // current mode is intro mode, switch to schedule mode								
			View introLayout = findViewById(R.id.intro_layout);			
			if (introLayout != null)
				mainLayout.removeView(introLayout);	
		
			vi.inflate(R.layout.layout_schedule_list, (ViewGroup) mainLayout);
			
			// next action is introduction mode
			TextView txtAction = (TextView)findViewById(R.id.title_btn_action);
			txtAction.setText("简介");			
			introMode = false;
			
		} else {			
			View scheduleLayout = findViewById(R.id.movie_schedule_layout);			
			if (scheduleLayout != null)
				mainLayout.removeView(scheduleLayout);	
		
			vi.inflate(R.layout.template_intro, (ViewGroup) mainLayout);
			
			// next action is schedule mode
			TextView txtAction = (TextView)findViewById(R.id.title_btn_action);
			txtAction.setText("排期");			
			introMode = true;						
		}
		
		refreshMovieInfo();
	}
		
	
	public void refreshMovieInfo(){
		
		TextView titleText = (TextView)findViewById(R.id.title_text);
		titleText.setText(movie.name);
		
		if (introMode) {								
			View introLayout = findViewById(R.id.intro_layout);		

			TextView movieLabel = (TextView)findViewById(R.id.text_label);
			movieLabel.setText(movie.name);	

			TextView movieInfo = (TextView)findViewById(R.id.text_detail);
			movieInfo.setText(movie.getGeneralDesc());
		} else { // currently it is schedule mode
		
			ListView lv = (ListView)findViewById(R.id.schedule_list);
		
			ScheduleTimeList ada = new ScheduleTimeList(this, R.layout.list_event_by_time, poi.getMovieByIdx(movieIdx).getTodaySchedule());
		
			lv.setAdapter(ada);
		
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				int startHour = movie.getTodaySchedule().get(position).fromHour;
				int startMinute = movie.getTodaySchedule().get(position).fromMin;
							
				//onSetAlarmClick(startHour, startMinute, arg1, position);
				onSetAlarmClick(19, 0, arg1, position);
				}				
			});
		}
	}
	
private void onSetAlarmClick(final int startHour, final int startMinute, final View v, final int timeIndex){
		
		Calendar currentTime = Calendar.getInstance();
		
		if ((currentTime.get(Calendar.HOUR_OF_DAY) > startHour) ||
				((currentTime.get(Calendar.HOUR_OF_DAY) == startHour) && 
				 (currentTime.get(Calendar.MINUTE) > startMinute))){
			Util.showToast(MovieInfoActivity.this, "剧场已开始！", Toast.LENGTH_SHORT);
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(MovieInfoActivity.this);
		builder.setTitle("请选择提前提醒的时间")
		.setSingleChoiceItems(R.array.alarm_time, 0, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which){
					case 0:
						mMinutesAhead = 5;
						break;
					case 1:
						mMinutesAhead = 10;
						break;
					case 2:
						mMinutesAhead = 15;
						break;
					case 3:
						mMinutesAhead = 30;
						break;
					case 4:
						mMinutesAhead = 60;
						break;
					case 5:
						mMinutesAhead = 1;
						break;
					default:
						break;
				}
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				int alarmHour; 
				int alarmMinute;
				
				Calendar currentTime = Calendar.getInstance();
					
				if (startMinute < mMinutesAhead){
					alarmHour = startHour - 1;
					alarmMinute = startMinute + 60 - mMinutesAhead;
				}
				else{
					alarmHour = startHour;
					alarmMinute = startMinute - mMinutesAhead;
				}
				
				currentTime.set(Calendar.HOUR_OF_DAY, alarmHour);
				currentTime.set(Calendar.MINUTE, alarmMinute);
				
				Intent intent = new Intent(MovieInfoActivity.this, AlarmActivity.class);
				intent.putExtra(AlarmActivity.BUNDLE_KEY_EVENT_TITLE, movie.name);
				intent.putExtra(AlarmActivity.BUDDLE_KEY_ALARM_INFO, mMinutesAhead);
				
				//Different request codes to distinguish distinct PendingIntents
				int requestCode = (alarmHour + alarmMinute) * mMinutesAhead; 
				PendingIntent pi = PendingIntent.getActivity(MovieInfoActivity.this, requestCode, intent, 0);
				
				mAlarmMgr.set(AlarmManager.RTC, currentTime.getTimeInMillis(), pi);
				
				movie.getTodaySchedule().get(timeIndex).setAlarmStatus(true);
				
				TextView tv = (TextView) v.findViewById(R.id.schedule_text_remind);
				tv.setText(R.string.alarm_added);
				tv.setTextColor(Color.RED);
				
				//Util.showToast(PlayhouseInfoActivity.this, "设置提醒成功！", Toast.LENGTH_SHORT);

				dialog.dismiss();
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		})
		.show();
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
