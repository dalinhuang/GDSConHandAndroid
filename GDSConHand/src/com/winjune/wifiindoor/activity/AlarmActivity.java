package com.winjune.wifiindoor.activity;

import java.util.ArrayList;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.R.menu;
import com.winjune.wifiindoor.poi.EventItem;
import com.winjune.wifiindoor.poi.EventManager;
import com.winjune.wifiindoor.poi.ScheduleTime;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AlarmActivity extends Activity {

	private ArrayList<EventItem> mEventItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		
		mEventItems = EventManager.getEventListByAlarm();
		
		ListView lv = (ListView) findViewById(R.id.eventListByAlarm);
		lv.setAdapter(new EventListByAlarm(AlarmActivity.this,
				R.layout.list_event_by_time, mEventItems));
		
		int minutes = getIntent().getIntExtra(EventManager.Key_Event_Alarm_Time, -1);
		
		if (minutes != -1){
			new AlertDialog.Builder(AlarmActivity.this)
			.setTitle("提醒").setMessage("您关注的活动还有"+Integer.toString(minutes)+"分钟就要开始了！")
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
	
	public class EventListByAlarm extends ArrayAdapter<EventItem> {

		private int resourceId;  
		private Context context;
		private ArrayList<EventItem> events;
		 
		public EventListByAlarm(Context context, int resource, ArrayList<EventItem> items) {
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
			title.setText(events.get(position).getTitle());
			
			TextView tv = (TextView)view.findViewById(R.id.event_schedule);
			String times = "已关注时间：";
			EventItem ei = events.get(position);
			ArrayList<ScheduleTime> timeList = EventManager.getEventAlarmTimeOfToday(ei);
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
