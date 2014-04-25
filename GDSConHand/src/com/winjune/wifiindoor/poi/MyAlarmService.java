package com.winjune.wifiindoor.poi;

import java.util.Calendar;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.AlarmActivity;
import com.winjune.wifiindoor.activity.PlayhouseInfoActivity;
import com.winjune.wifiindoor.util.Util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyAlarmService {
	
	private static AlarmManager mAlarmMgr;
	
	public void init(){
		
	}
	
	
	public void onSetAlarmClick(final Activity activity, final int startHour, final int startMinute, final View v, final int timeIndex){
	
	}
}
