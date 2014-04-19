package com.winjune.wifiindoor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.winjune.wifiindoor.R;

public class RouteMainctivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_main);
    }
    
    public void backClick(View v) {
    	onBackPressed();    	
    }   
    
    public void jumpToInputEndPointClick(View v){		
		Intent i = new Intent(this, RouteInputActivity.class); 
		startActivity(i);	
	}
}