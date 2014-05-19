package com.winjune.wifiindoor.activity;


import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import com.winjune.wifiindoor.R;

public class NaviResultActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navi_result);
			
	}
	
	public void backClick(View v) {
	  	onBackPressed();    	
	}  			
	
	public void onMapClick(View v){
		
	}
}
