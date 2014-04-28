package com.winjune.wifiindoor.activity;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.poiviewer.BusStationInfoActivity;
import com.winjune.wifiindoor.activity.poiviewer.POITtsPlayerActivity;
import com.winjune.wifiindoor.activity.poiviewer.RestaurantInfoActivity;
import com.winjune.wifiindoor.adapter.HistoryDataList;
import com.winjune.wifiindoor.map.InterestPlace;
import com.winjune.wifiindoor.map.InterestPlacesInfo;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.poi.SearchHistory;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

public class LabelSearchActivity extends Activity {
	
	private static int TTS_DIGIT_NUM = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_search);
		
		// setup auto-complete  
        String[] labelArray = POIManager.buildAutoCompleteText();
	    AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.search_text_input);
	    ArrayAdapter<String> autoCompleteAda = new ArrayAdapter<String>(this, 
	                android.R.layout.simple_dropdown_item_1line, labelArray);
	    textView.setThreshold(1); 
	    textView.setAdapter(autoCompleteAda);	
	    
	    //setup history info
		ListView lv = (ListView)findViewById(R.id.search_history_list);
		
		HistoryDataList historyAda = new HistoryDataList(this, 
					R.layout.list_history, SearchHistory.getHistory());
		
		lv.setAdapter(historyAda);	   
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});		
	}
	
	public void backClick(View v) {
	  	onBackPressed();    	
	}  		
	
	private boolean isInputTtsNum(String text) {
		
		if (text == null)
			return false;
				
		if (text.length() != TTS_DIGIT_NUM) 
			return false;
			
		for (int i=0; i<text.length(); i++) {
			if (!Character.isDigit(text.charAt(i)))
				return false;
		}
				
		return true;		
	}
	
	public void playTtsAudio(int ttsNo) {
		PlaceOfInterest poi = POIManager.getPOIbyTtsNo(ttsNo);
		
		if (poi != null){									
			Intent intent_poi = new Intent(this, POITtsPlayerActivity.class);

			Bundle mBundle = new Bundle(); 
			mBundle.putInt(POITtsPlayerActivity.BUNDLE_KEY_POI_ID, poi.id);
			intent_poi.putExtras(mBundle); 
			startActivity(intent_poi);
		}									

	}
	
	public void searchClick(View v){		
		AutoCompleteTextView searchTextView = (AutoCompleteTextView) findViewById(R.id.search_text_input);
		
		String inputText = searchTextView.getText().toString();		
		
		String text = inputText.trim();
		
		if (text.isEmpty())
			return;
		
		SearchHistory.addHistoryRecord(inputText);		

		
		if (isInputTtsNum(text)) {
			
			int ttsNum = Integer.parseInt(text);
			
			playTtsAudio(ttsNum);	

		}		
	}
	
	
	
	public void clearHistoryClick(View v) {
		SearchHistory.clearHistory();
	}

	public void restaurantClick(View v){
        Intent i = new Intent(this, RestaurantInfoActivity.class); 
		startActivity(i);				
	}
	
	public void busStationClick(View v){
        Intent i = new Intent(this, BusStationInfoActivity.class); 
		startActivity(i);				
	}
	
	public void moreClick(View v){
        Intent i = new Intent(this, ShortcutEntryActivity.class); 
		startActivity(i);				
	}
	
		
}
