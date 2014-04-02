package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class RoutePointInputActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_point_input);
    }
    
    public void backClick(View v) {
    	onBackPressed();    	
    }    
}
