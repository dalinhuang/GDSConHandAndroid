package com.ericsson.cgc.aurora.wifiindoor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;

public class NavigatorActivity extends Activity {
	private TextView naviInfo;
	private Button endButton;
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.navigator_result);
        
        // Define Buttons and bind the listeners
        naviInfo = (TextView) findViewById(R.id.naviInfo);
        endButton = (Button) findViewById(R.id.endButton);
        
        Bundle bundle = getIntent().getExtras();
		String naviStr = bundle.getString(IndoorMapData.BUNDLE_KEY_NAVI_RESULT);
		
		if (naviStr != null) {
			naviInfo.setText(naviStr);
		}
		
		endButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View view){        		
        		NavigatorActivity.this.finish();
        	}
        });
		
    }
	
    @Override
	protected void onPause() {
		super.onPause();
		Util.setEnergySave(true);
	}
    
    @Override
	protected void onResume() {
		super.onResume();
		Util.setEnergySave(false);
	}
}
