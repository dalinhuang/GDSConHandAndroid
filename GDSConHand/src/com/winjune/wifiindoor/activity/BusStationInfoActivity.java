package com.winjune.wifiindoor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.winjune.wifiindoor.R;

public class BusStationInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_station_info);
	}

	 public void backClick(View v) {
	    	onBackPressed();    	
	    } 
	 
 
	 public void enterBusstop202(View v) {
		 Intent i = new Intent(this, BusLineInfoActivity.class);
	     startActivity(i);   	
	    }   
	 



}
