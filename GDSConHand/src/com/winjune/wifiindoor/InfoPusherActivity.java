package com.winjune.wifiindoor;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class InfoPusherActivity extends Activity {
	private TextView mapInfo;
	private TextView locationInfo;
	
	@Override
	protected void onResume() {
		Util.setEnergySave(false);
		Util.setCurrentForegroundActivity(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Util.setEnergySave(true);
		Util.setCurrentForegroundActivity(null);
		super.onPause();
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
}
