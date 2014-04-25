package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.poi.PlaceOfInterest;

import android.app.Activity;
import android.widget.TextView;

public class PoiBaseActivity extends Activity{	
	public static String BUNDLE_KEY_POI_ID = "POI_ID";
	
	
	protected int poiId;
	protected PlaceOfInterest poi;
	
	protected void UpdateTitleInfo(){
		TextView titleText = (TextView)findViewById(R.id.title_text);
		titleText.setText(poi.label);		
	}
	
	protected void UpdateDesc(){
		TextView info = (TextView)findViewById(R.id.text_detail);
		info.setText(poi.detailedDesc);	
		
	}
}
