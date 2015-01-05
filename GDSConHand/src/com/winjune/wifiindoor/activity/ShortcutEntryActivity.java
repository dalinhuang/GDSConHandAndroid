package com.winjune.wifiindoor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.poiviewer.POIBaseActivity;
import com.winjune.wifiindoor.activity.poiviewer.RestaurantInfoActivity;
import com.winjune.wifiindoor.activity.poiviewer.TheatreInfoActivity;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.util.Constants;

public class ShortcutEntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut_entry);
    }
        
    public void backClick(View v) {
    	onBackPressed();    	
    }    
    
	public void shortcutClick(View v){
		String txt = ((TextView)v).getText().toString();
		
		PlaceOfInterest poi = POIManager.getPOIbyLabel(txt); 
		if (poi == null) {
			Log.e("LabelSearch", "POI not found");
			return;
		}
		
		Class mViewerClass = POIManager.getPOIViewerClass(poi.getPoiType());
		
		Intent intent_poi = new Intent(this, mViewerClass);		
		Bundle mBundle = new Bundle(); 
		mBundle.putInt(Constants.BUNDLE_KEY_POI_ID, poi.id);
		intent_poi.putExtras(mBundle); 		
		
		startActivity(intent_poi);
	}
}
