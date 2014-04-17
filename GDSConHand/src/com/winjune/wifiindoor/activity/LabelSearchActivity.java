package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class LabelSearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_search);
	}
	
	public void backClick(View v) {
	  	onBackPressed();    	
	}  	
	
	public void searchClick(View v){
		onBackPressed();
	}

	public void restaurantClick(View v){
        Intent i = new Intent(this, RestaurantInfoActivity.class); 
		startActivity(i);				
	}
	
	public void busStationClick(View v){
        Intent i = new Intent(this, BusStationInfoActivity.class); 
		startActivity(i);				
	}
	
	public void moreClick(View v){
        Intent i = new Intent(this, ShortcutEntryActivity.class); 
		startActivity(i);				
	}
}
