package com.ericsson.cgc.aurora.wifiindoor;

import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoPusherActivity extends Activity {
	private TextView mapInfo;
	private TextView locationInfo;
	
	@Override
	protected void onResume() {
		super.onResume();
		Util.setEnergySave(false);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Util.setEnergySave(true);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.info_pusher);
        
        // Define Buttons and bind the listeners
        mapInfo = (TextView) findViewById(R.id.mapInfo);
        locationInfo = (TextView) findViewById(R.id.locationInfo);
        
        Bundle bundle = getIntent().getExtras();
		String info1 = bundle.getString(IndoorMapData.BUNDLE_KEY_MAP_INFO);
		String info2 = bundle.getString(IndoorMapData.BUNDLE_KEY_LOCATION_INFO);
		
		if (info1 != null) {
			mapInfo.setText(info1);
		}
		
		if (info2 != null) {
			locationInfo.setText(info2);
		}
		
    }
    
    @Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
