package com.winjune.wifiindoor.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.lib.poi.ScheduleTime;


public class ScheduleTimeList extends ArrayAdapter<ScheduleTime> {

	private int resourceId;  
	private Context context;
	private ArrayList<ScheduleTime> mEventTimesOfToday;
	 
	public ScheduleTimeList(Context context, int resource, ArrayList<ScheduleTime> items) {
		super(context, resource, items);
		this.context = context;
		this.resourceId = resource;
		this.mEventTimesOfToday = items;
		// TODO Auto-generated constructor stub
	}
	
    @Override  
    public View getView(int position, View convertView, ViewGroup parent){  
    	View view = convertView;
    	
    	if (view == null) {
    		LayoutInflater vi = LayoutInflater.from(context);  
    		view=vi.inflate(R.layout.list_schedule_time, parent, false);
    	}
		
		boolean playStarted = false;
		Calendar currentTime = Calendar.getInstance();
		int startHour = mEventTimesOfToday.get(position).fromHour;
		int startMin = mEventTimesOfToday.get(position).fromMin;
		
		if ((currentTime.get(Calendar.HOUR_OF_DAY) > startHour) ||
				((currentTime.get(Calendar.HOUR_OF_DAY) == startHour) && 
				 (currentTime.get(Calendar.MINUTE) > startMin))){
			playStarted = true;
		}
		
		TextView scheduleTxt = (TextView) view.findViewById(R.id.schedule_time_text);
		scheduleTxt.setText(mEventTimesOfToday.get(position).toString());
				
		if (playStarted){
			scheduleTxt.setTextColor(Color.GRAY);		
		}
		
		TextView remind = (TextView) view.findViewById(R.id.schedule_text_remind);
		if (mEventTimesOfToday.get(position).getAlarmStatus()){
			remind.setText(R.string.alarm_added);
			remind.setTextColor(Color.RED);
		}
		else{
			if (playStarted){
				remind.setVisibility(View.INVISIBLE);
			}
		}
			        
        return view;  
    }   		
}