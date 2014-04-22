package com.winjune.wifiindoor.activity;

import java.util.ArrayList;
import java.util.Calendar;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.PlayhouseInfoActivity.PlayhouseTimeList;
import com.winjune.wifiindoor.adapter.HistoryDataList;
import com.winjune.wifiindoor.event.EventTime;
import com.winjune.wifiindoor.map.FieldInfo;
import com.winjune.wifiindoor.mapviewer.LabelBar;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.SearchHistory;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LabelSearchActivity extends Activity {
	
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
	
	public void searchClick(View v){		
		AutoCompleteTextView searchTextView = (AutoCompleteTextView) findViewById(R.id.search_text_input);
		
		String inputText = searchTextView.getText().toString();		
		
		SearchHistory.addHistoryRecord(inputText);		
				
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
