package com.winjune.wifiindoor.activity.poiviewer;

import java.util.ArrayList;
import java.util.Calendar;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.lib.poi.ScheduleTime;
import com.winjune.wifiindoor.poi.PlayhouseInfo;
import com.winjune.wifiindoor.poi.EventManager;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.adapter.ScheduleTimeList;

import android.os.Bundle;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayhouseInfoActivity extends POIBaseActivity {
	
	private AlarmManager mAlarmMgr;
	private int mMinutesAhead;
	private ArrayList<ScheduleTime> mEventTimesOfToday;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playhouse_info);
		Bundle bundle = getIntent().getExtras();
		poiId = bundle.getInt(Constants.BUNDLE_KEY_POI_ID);
		
		poi = POIManager.getPOIbyId(poiId);
		
		updateTitleInfo();
		
		mEventTimesOfToday =((PlayhouseInfo)poi).getTodaySchedule();
		
		ListView lv = (ListView)findViewById(R.id.schedule_list);
		
		ScheduleTimeList ada = new ScheduleTimeList(this, R.layout.list_schedule_time, mEventTimesOfToday);
		
		lv.setAdapter(ada);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
					int startHour = mEventTimesOfToday.get(arg2).fromHour;
					int startMinute = mEventTimesOfToday.get(arg2).fromMin;
					
					onSetAlarmClick(19, 0, arg1, arg2);

			}
			
		});
		
		mAlarmMgr = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		
		mMinutesAhead = 5; //By default the alarm time is 5 minutes ahead
	}
	
	public void introClick(View v){
        Intent i = new Intent(this, POINormalViewerActivity.class); 

		Bundle mBundle = new Bundle(); 
		mBundle.putInt(Constants.BUNDLE_KEY_POI_ID, poiId);
		i.putExtras(mBundle); 	
		
        v.getContext().startActivity(i);			
	}
	
	public void onSetAlarmClick(final int startHour, final int startMinute, final View v, final int timeIndex){
		
		Calendar currentTime = Calendar.getInstance();
		
		if ((currentTime.get(Calendar.HOUR_OF_DAY) > startHour) ||
				((currentTime.get(Calendar.HOUR_OF_DAY) == startHour) && 
				 (currentTime.get(Calendar.MINUTE) > startMinute))){
			Util.showToast(PlayhouseInfoActivity.this, "剧场已开始！", Toast.LENGTH_SHORT);
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(PlayhouseInfoActivity.this);
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
				
				Intent intent = new Intent(PlayhouseInfoActivity.this, AlarmActivity.class);
				intent.putExtra(AlarmActivity.BUNDLE_KEY_EVENT_TITLE, poi.label);
				intent.putExtra(AlarmActivity.BUDDLE_KEY_ALARM_INFO, mMinutesAhead);
				
				//Different request codes to distinguish distinct PendingIntents
				int requestCode = (alarmHour + alarmMinute) * mMinutesAhead; 
				PendingIntent pi = PendingIntent.getActivity(PlayhouseInfoActivity.this, requestCode, intent, 0);
				
				mAlarmMgr.set(AlarmManager.RTC, currentTime.getTimeInMillis(), pi);
				
				((PlayhouseInfo)poi).getTodaySchedule().get(timeIndex).setAlarmStatus(true);
				
				TextView tv = (TextView) v.findViewById(R.id.schedule_text_remind);
				tv.setText(R.string.alarm_added);
				tv.setTextColor(Color.RED);
				tv.setVisibility(View.VISIBLE);
				
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
}
