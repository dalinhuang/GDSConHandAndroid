package com.winjune.wifiindoor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.winjune.wifiindoor.R;

public class RouteFirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_first_ui);
    }
    
    public void backClick(View v) {
    	onBackPressed();    	
    }   
    
    public void jumpToInputEndPointClick(View v){		
		Intent i = new Intent(this, RoutePointInputActivity.class); 
		startActivity(i);	
	}
}
