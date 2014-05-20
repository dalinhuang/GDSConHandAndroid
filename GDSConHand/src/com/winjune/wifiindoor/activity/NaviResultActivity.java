package com.winjune.wifiindoor.activity;


import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.lib.map.NaviNodeR;
import com.winjune.wifiindoor.navi.NaviContext;
import com.winjune.wifiindoor.util.Constants;

public class NaviResultActivity extends Activity{
	
	NaviContext mContext = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navi_result);
		
		TextView titleText = (TextView)findViewById(R.id.title_text);
		if (titleText == null)
			return;
		
		titleText.setText("路线详细");
		
		Bundle bundle = getIntent().getExtras();
		mContext = (NaviContext)bundle.getSerializable(Constants.BUNDLE_KEY_NAVI_CONTEXT);		

		TextView searchText = (TextView)findViewById(R.id.search_text);
		if (searchText == null)
			return;
		
		searchText.setText(mContext.text);
		
		ArrayList<String> stepNameList = new ArrayList<String>();
		for (NaviNodeR step:mContext.naviRoute){
			stepNameList.add(step.getLabel());
		}
		
		ListView lv = (ListView)findViewById(R.id.route_step_list);
		
		ArrayAdapter<String> stepsAda = new ArrayAdapter<String>(this, R.layout.list_route, stepNameList);
		lv.setAdapter(stepsAda);
				
	}
	
	public void backClick(View v) {
	  	onBackPressed();    	
	}  			
	
	public void onMapClick(View v){
		Intent resultI = new Intent(this, MapViewerActivity.class);
		
		resultI.setAction(Constants.ActionRoute);

		Bundle mBundle = new Bundle(); 
		mBundle.putSerializable(Constants.BUNDLE_KEY_NAVI_CONTEXT, mContext);
		resultI.putExtras(mBundle);
		
		startActivity(resultI);		
		
		finish();
	}
}
