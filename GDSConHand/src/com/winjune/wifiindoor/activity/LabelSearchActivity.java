package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class LabelSearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_search);
	}
	
	
	public void searchClick(View v){
		onBackPressed();
	}


}
