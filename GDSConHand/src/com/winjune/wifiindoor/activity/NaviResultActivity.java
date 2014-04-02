package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.id;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NaviResultActivity extends Activity {
	private TextView naviInfo;
	private Button endButton;
	
	@Override
	protected void onResume() {
		super.onResume();
		Util.setEnergySave(false);
		Util.setCurrentForegroundActivity(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Util.setEnergySave(true);
		Util.setCurrentForegroundActivity(null);
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_navigator_result);
        
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
        		NaviResultActivity.this.finish();
        	}
        });
		
    }
    
    @Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
