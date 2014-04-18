package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class RouteInputActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_input);
    }
    
    public void backClick(View v) {
    	onBackPressed();    	
    }    
}
