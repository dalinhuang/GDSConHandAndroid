package com.winjune.wifiindoor.activity;

import java.util.ArrayList;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.R.menu;
import com.winjune.wifiindoor.poi.EventManager;
import com.winjune.wifiindoor.poi.MovieInfo;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlayhouseInfo;
import com.winjune.wifiindoor.poi.ScheduleTime;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AlarmActivity extends Activity {
	
	public static String BUNDLE_KEY_EVENT_TITLE = "BUNDLE_KEY_EVENT_TITLE";
	public final static String BUDDLE_KEY_ALARM_INFO = "BUDDLE_KEY_ALARM_INFO";	

	private ArrayList<EventInfo> mEventInfos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		
		mEventInfos = new ArrayList<EventInfo>();
		
		//Fill in the playhouse information
		ArrayList<PlayhouseInfo> eventItems = EventManager.getEventListByAlarm();
		for (PlayhouseInfo ei: eventItems){
			EventInfo eventInfo = new EventInfo();
			
			eventInfo.setEventTitle(ei.getLabel());
			eventInfo.setSchedules(ei.getEventAlarmTimeOfToday());
			
			mEventInfos.add(eventInfo);
		}
		
		//Fill in the movie information
		ArrayList<MovieInfo> movies = POIManager.getMovieListByAlarm();
		for (MovieInfo mi: movies){
			EventInfo eventInfo = new EventInfo();
			
			eventInfo.setEventTitle(mi.name);
			eventInfo.setSchedules(mi.getTodayScheduleByAlarm());
			
			mEventInfos.add(eventInfo);
		}
		
		ListView lv = (ListView) findViewById(R.id.eventListByAlarm);
		lv.setAdapter(new EventListByAlarm(AlarmActivity.this,
				R.layout.list_event_by_time, mEventInfos));
		
		Intent intent = getIntent();
		String title = intent.getStringExtra(BUNDLE_KEY_EVENT_TITLE);
		int minutes = intent.getIntExtra(BUDDLE_KEY_ALARM_INFO, -1);
		
		if (minutes != -1){
			new AlertDialog.Builder(AlarmActivity.this)
			.setTitle("提醒").setMessage("您关注的活动"+title+"还有"+Integer.toString(minutes)+"分钟就要开始了！")
			.setPositiveButton("确定", new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//AlarmActivity.this.finish();
					dialog.dismiss();
				}
			
			}).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm, menu);
		return true;
	}
	
	private class EventInfo {
		private String mEventTitle;
		private ArrayList<ScheduleTime> mSchedules;
		
		public EventInfo(){
			mEventTitle = "";
			mSchedules = null;
		}
		
		public String getEventTitle(){
			return mEventTitle;
		}
		
		public void setEventTitle(String title){
			mEventTitle = title;
		}
		
		public ArrayList<ScheduleTime> getSchedules(){
			return mSchedules;
		}
		
		public void setSchedules(ArrayList<ScheduleTime> schedules){
			mSchedules = schedules;
		}
	}
	
	public class EventListByAlarm extends ArrayAdapter<EventInfo> {

		private int resourceId;  
		private Context context;
		private ArrayList<EventInfo> events;
		 
		public EventListByAlarm(Context context, int resource, ArrayList<EventInfo> items) {
			super(context, resource, items);
			this.context = context;
			this.resourceId = resource;
			events = items;
			// TODO Auto-generated constructor stub
		}
		
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent){  
	        LayoutInflater vi = LayoutInflater.from(context);  
 
			View view=vi.inflate(R.layout.list_event_by_time, null);
			
			TextView title = (TextView) view.findViewById(R.id.event_title);
			title.setText(events.get(position).getEventTitle());
			
			TextView tv = (TextView)view.findViewById(R.id.event_schedule);
			String times = "已关注时间：";
			EventInfo ei = events.get(position);
			ArrayList<ScheduleTime> timeList = ei.getSchedules();
			for (ScheduleTime et : timeList){
				times += et.getStartTime();
			}
			tv.setText(times);
				        
	        return view;  
	    }
	}
	
	public void backClick(View v){
		onBackPressed();
	}
}
