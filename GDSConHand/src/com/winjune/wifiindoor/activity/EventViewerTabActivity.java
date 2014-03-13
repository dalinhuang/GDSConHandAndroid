package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class EventViewerTabActivity extends TabActivity{
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		Resources myResources = getResources();

	    //setContentView(R.layout.event_viewer);

	    TabHost.TabSpec spec;
	    
	    Intent intent;

	    TabHost tabHost = getTabHost();
	    
	    spec = tabHost.newTabSpec(myResources.getString(R.string.list_by_time));
	    spec.setIndicator(myResources.getString(R.string.list_by_time), myResources.getDrawable(R.drawable.list_by_time));	  
	    myResources.getDrawable(R.drawable.list_by_time).setAlpha(150);
	    intent = new Intent(this, EventViewerListByTimeActivity.class);
	    spec.setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent(this, EventViewerListByPlaceActivity.class);
	  
	    spec = tabHost.newTabSpec(myResources.getString(R.string.list_by_place));
	    spec.setIndicator(myResources.getString(R.string.list_by_place), myResources.getDrawable(R.drawable.list_by_place));
	    myResources.getDrawable(R.drawable.list_by_place).setAlpha(150);
	    spec.setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	    
	    setContentView(tabHost);
	}
}
