package com.winjune.wifiindoor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.winjune.wifiindoor.R;

public class Bus202InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_202_info);
	}

	 public void backClick(View v) {
	    	onBackPressed();    	
	    }  
	 
	

}
