package com.ericsson.cgc.aurora.wifiindoor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.ericsson.cgc.aurora.wifiindoor.map.InterestPlace;
import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;

public class InterestPlaceViewerActivity extends Activity {
	private TextView textInfo;
	
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
        
        setContentView(R.layout.interest_place_viewer);
        
        textInfo = (TextView) findViewById(R.id.textInfo);
          
        Bundle bundle = getIntent().getExtras();
		InterestPlace place = (InterestPlace) bundle.getSerializable(IndoorMapData.BUNDLE_KEY_INTEREST_PLACE_INSTANCE);
		
		if (place != null) {
			textInfo.setText(place.getInfo());
		}
    }
    
    @Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}