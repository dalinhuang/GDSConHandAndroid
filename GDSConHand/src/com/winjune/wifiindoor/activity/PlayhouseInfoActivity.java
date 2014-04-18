package com.winjune.wifiindoor.activity;

import java.util.Calendar;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.dummy.DummyContent;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.adapter.ScheduleTimeList;

import android.os.Bundle;
import android.app.Activity;
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

public class PlayhouseInfoActivity extends Activity {

	private AlarmManager mAlarmMgr;
	private int mMinutesAhead;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playhouse_info);
		
		ListView lv = (ListView)findViewById(R.id.playhouse_schedule_list);
		
		ScheduleTimeList ada = new ScheduleTimeList(this, R.layout.list_event_by_time, DummyContent.ITEMS);
		
		lv.setAdapter(ada);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		mAlarmMgr = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		
		mMinutesAhead = 5; //By default the alarm time is 5 minutes ahead
	}
	

	
	public void backClick(View v){
		onBackPressed();
	}
	
	public void onSetAlarmClick(View v){
		
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
				int startHour = 22; // TBD: this should be got from the adpater or the textView
				int startMinute = 33; // TBD: this should be got from the adpater or the textView
				
				Calendar currentTime = Calendar.getInstance();
				
				if ((currentTime.get(Calendar.HOUR_OF_DAY) > startHour) ||
						((currentTime.get(Calendar.HOUR_OF_DAY) == startHour) && 
						 (currentTime.get(Calendar.MINUTE) > startMinute))){
					Util.showToast(PlayhouseInfoActivity.this, "剧场已开始！", Toast.LENGTH_SHORT);
					return;
				}
				
				Intent intent = new Intent(PlayhouseInfoActivity.this, AlarmActivity.class);
				PendingIntent pi = PendingIntent.getActivity(PlayhouseInfoActivity.this, 0, intent, 0);
				
				if (startMinute < mMinutesAhead){
					startHour = startHour - 1;
					startMinute = startMinute + 60 - mMinutesAhead;
				}
				else{
					startMinute = startMinute - mMinutesAhead;
				}
				
				currentTime.set(Calendar.HOUR_OF_DAY, startHour);
				currentTime.set(Calendar.MINUTE, startMinute);
				
				mAlarmMgr.set(AlarmManager.RTC, currentTime.getTimeInMillis(), pi);
				
				TextView tv = (TextView) findViewById(R.id.schedule_text_remind);
				tv.setText(R.string.alarm_set);
				tv.setTextColor(Color.RED);
				
				Util.showToast(PlayhouseInfoActivity.this, "设置提醒成功！", Toast.LENGTH_SHORT);

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
