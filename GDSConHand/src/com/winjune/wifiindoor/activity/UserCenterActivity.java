package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.activity.*;
import com.winjune.wifiindoor.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class UserCenterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
	}
	
	public void shareMyPosClick(View v){
		
	}

	public void settingClick(View v){
        Intent i = new Intent(this, SettingActivity.class); 
		startActivity(i);	
	}
	
	public void feedbackClick(View v){
		
	}

	public void planningTuningClick(View v){
        Intent i = new Intent(this, TunerActivity.class); 
		startActivity(i);	
	}
	
	public void checkUpdateClick(View v){
		
	}
	
	public void aboutClick(View v){
		
	}
	
	public void exitAppClick(View v) {
		
	}
}
