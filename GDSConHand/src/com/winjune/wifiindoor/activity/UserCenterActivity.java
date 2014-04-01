package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class UserCenterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
	}
	
	public void backClick(View v){
		onBackPressed();
	}	
	
	public void shareMyPosBarClick(View v){
		
	}

	public void settingBarClick(View v){
        Intent i = new Intent(this, SettingActivity.class); 
		startActivity(i);		
	}
	
	public void feedbackBarClick(View v){
		
	}

	public void planningTuningBarClick(View v){
        Intent i = new Intent(this, TunerActivity.class); 
		startActivity(i);				
	}
	
	public void checkUpdateBarClick(View v){
		
	}
	
	public void aboutBarClick(View v){
		
	}
	
	public void exitAppBarClick(View v) {		
 
         setResult(RESULT_FIRST_USER);
         
         finish();
	}
}
