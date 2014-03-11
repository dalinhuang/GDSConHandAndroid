package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class EventViewerTabActivity extends TabActivity{
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

	    setContentView(R.layout.event_viewer);

	    TabHost.TabSpec spec;
	    
	    Intent intent;

	    TabHost tabHost = getTabHost();
	    
	    spec = tabHost.newTabSpec("List by Time");
	    spec.setIndicator("List by Time");
	    intent = new Intent(this, EventViewerListByTimeActivity.class);
	    spec.setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent(this, EventViewerListByTimeActivity.class);
	  
	    spec = tabHost.newTabSpec("List by Place");
	    spec.setIndicator("List by Place");
	    spec.setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(1);
	}
}
