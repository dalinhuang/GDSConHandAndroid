package com.winjune.wifiindoor.activity.poiviewer;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.poi.PlaceOfInterest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class POIBaseActivity extends Activity{	
	public static String BUNDLE_KEY_POI_ID = "POI_ID";
	
	
	protected int poiId;
	protected PlaceOfInterest poi;
	
	 public void backClick(View v) {
	    	onBackPressed();    	
	 } 	
	
	protected void updateTitleInfo(){
		TextView titleText = (TextView)findViewById(R.id.title_text);
		if (titleText == null)
			return;
		
		titleText.setText(poi.label);		
	}
	
	protected void updateDesc(){
		TextView info = (TextView)findViewById(R.id.text_detail);
		if (info == null)
			return;
		
		info.setText(poi.detailedDesc);			
	}
	
	public void setupContentButton(){
		View v = findViewById(R.id.button_layout);
		
		if (v != null) {
			poi.setupContentButton(v);			
		}
	}
	
	public void onMapClick(View v){
		Intent data = new Intent(this, MapViewerActivity.class);
		data.setAction(MapViewerActivity.BUNDLE_LOCATION_CONTEXT);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(MapViewerActivity.BUNDLE_LOCATION_CONTEXT, poi);
		data.putExtras(mBundle);
		
		startActivity(data);
		
		finish();
	}
}
