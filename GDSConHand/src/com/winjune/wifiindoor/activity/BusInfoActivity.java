package com.winjune.wifiindoor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.winjune.wifiindoor.R;

public class BusInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_info);
	}

	 public void backClick(View v) {
	    	onBackPressed();    	
	    } 
	 
	 public void enterBusstop383(View v) {
		 Intent i = new Intent(this, Bus383InfoActivity.class);
	     startActivity(i);   	
	    } 
	 
	 public void enterBusstop801(View v) {
		 Intent i = new Intent(this, Bus801InfoActivity.class);
	     startActivity(i);   	
	    }  
	 
	 public void enterBusstop202(View v) {
		 Intent i = new Intent(this, Bus202InfoActivity.class);
	     startActivity(i);   	
	    }   
	 
	 public void enterBusstop5(View v) {
		 Intent i = new Intent(this, Bus5InfoActivity.class);
	     startActivity(i);   	
	    }   


}
