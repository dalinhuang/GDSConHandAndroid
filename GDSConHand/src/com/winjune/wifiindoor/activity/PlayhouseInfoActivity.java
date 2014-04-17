package com.winjune.wifiindoor.activity;

import java.util.Calendar;
import java.util.List;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.dummy.DummyContent;
import com.winjune.wifiindoor.util.Util;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayhouseInfoActivity extends Activity {

	private AlarmManager mAlarmMgr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playhouse_info);
		
		ListView lv = (ListView)findViewById(R.id.playhouse_schedule_list);
		
		PlayhouseTimeList ada = new PlayhouseTimeList(this, R.layout.list_event_by_time, DummyContent.ITEMS);
		
		lv.setAdapter(ada);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		mAlarmMgr = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
	}
	
	public class PlayhouseTimeList extends ArrayAdapter<DummyContent.DummyItem> {

		private int resourceId;  
		private Context context;
		 
		public PlayhouseTimeList(Context context, int resource, List<DummyContent.DummyItem> items) {
			super(context, resource, items);
			this.context = context;
			this.resourceId = resource;
			// TODO Auto-generated constructor stub
		}
		
	    @Override  
	    public View getView(int position, View convertView, ViewGroup parent){  
	        LayoutInflater vi = LayoutInflater.from(context);  
 
			View view=vi.inflate(R.layout.list_playhouse_schedule, null);
			//timeAndPlace.setText("test test test test");
				        
	        return view;  
	    }   		
	}
	
	public void backClick(View v){
		onBackPressed();
	}
	
	public void onSetAlarmClick(View v){
		
		Calendar currentTime = Calendar.getInstance();
		
		int startHour = 16; // TBD: this should be got from the adpater or the textView
		int startMinute = 56; // TBD: this should be got from the adpater or the textView
		int minutesAhead = 1; //TBD: this should be chosen by the user
		
		if (currentTime.get(Calendar.HOUR_OF_DAY) > startHour){
			Util.showToast(PlayhouseInfoActivity.this, "剧场已开始！", Toast.LENGTH_SHORT);
			return;
		}
		
		Intent intent = new Intent(PlayhouseInfoActivity.this, AlarmActivity.class);
		PendingIntent pi = PendingIntent.getActivity(PlayhouseInfoActivity.this, 0, intent, 0);
		
		if (startMinute < minutesAhead){
			startHour = startHour - 1;
			startMinute = startMinute + 60 - minutesAhead;
		}
		else{
			startMinute = startMinute - minutesAhead;
		}
		
		currentTime.set(Calendar.HOUR_OF_DAY, startHour);
		currentTime.set(Calendar.MINUTE, startMinute);
		
		mAlarmMgr.set(AlarmManager.RTC, currentTime.getTimeInMillis(), pi);
		
		Util.showToast(PlayhouseInfoActivity.this, "设置提醒成功！", Toast.LENGTH_SHORT);
	}
}
