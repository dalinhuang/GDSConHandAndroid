package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.poiviewer.POIBaseActivity;
import com.winjune.wifiindoor.activity.poiviewer.POITtsPlayerActivity;
import com.winjune.wifiindoor.adapter.HistoryDataList;
import com.winjune.wifiindoor.lib.poi.POIType;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.poi.SearchContext;
import com.winjune.wifiindoor.poi.SearchHistory;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.adapter.SearchResultList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

public class LabelSearchActivity extends Activity {
	
	private static int TTS_DIGIT_NUM = 4;
	private SearchHistory history;
	private ListView lv = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_label_search);
		
		history = new SearchHistory();
		history.loadCachedData();
		
		// setup auto-complete  
        final String[] labelArray = POIManager.buildAutoCompleteText();
	    AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.search_text_input);
	    ArrayAdapter<String> autoCompleteAda = new ArrayAdapter<String>(this, 
	                android.R.layout.simple_dropdown_item_1line, labelArray);
	    textView.setThreshold(1); 
	    textView.setAdapter(autoCompleteAda);	
	    textView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				
				TextView tv = (TextView)v;

				// TODO Auto-generated method stub
				performSearch(tv.getText().toString());
			}
	    	
	    }); 
	    
	    //setup history info
		lv = (ListView)findViewById(R.id.search_history_list);
		
		final HistoryDataList historyAda = new HistoryDataList(this, 
					R.layout.list_history, history.getHistory());
		
		lv.setAdapter(historyAda);	   
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				performSearch(historyAda.getRecord(position));
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
			mBundle.putInt(Constants.BUNDLE_KEY_POI_ID, poi.id);
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
		
		performSearch(text);
	}
	
	private void performSearch(String text) {
		if (text.isEmpty())
			return;
				
		if (isInputTtsNum(text)) {
			
			int ttsNum = Integer.parseInt(text);
			
			playTtsAudio(ttsNum);
			
			return;
		}		
		
		SearchContext mContext = new SearchContext(text);
		mContext.poiResults = POIManager.searchPOIsbyLabel(text);
		
		if (mContext.poiResults.size() == 0) {
			AlertDialog alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
					.setMessage("找不到相关位置")
					.setPositiveButton("确定", null)
					.create();
			alertDialog.show();
			return;
		}
		
		history.addRecord(text);
		
		// by default, the first is the focus one
		mContext.currentFocusIdx = 0;
		mContext.searchText = text;
		
		if (mContext.poiResults.size() == 1)
			showResult(mContext);
		else
			showResultDialog(mContext);
	}
	
	
	private void showResultDialog(final SearchContext mContext) {
		
		SearchResultList resultsAda = new SearchResultList(this, R.layout.list_search_result, mContext);

		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);  
		builder.setTitle("您要找的是:");  		
		builder.setAdapter(resultsAda, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				// TODO Auto-generated method stub
				mContext.currentFocusIdx = position;
				showResultsOnMap(mContext);
			}
			
		});			
		
		builder.show();  		
	}
	
	private void showResult(final SearchContext mContext) {		
		PlaceOfInterest poi = mContext.poiResults.get(mContext.currentFocusIdx);
		
		if ((poi.getPoiType() == POIType.BusStation) |
			(poi.getPoiType() == POIType.Restaurant) ) {
								
			Class mViewerClass = POIManager.getPOIViewerClass(poi.getPoiType());						
			Intent intent_poi = new Intent(this, mViewerClass);
			
			Bundle mBundle = new Bundle(); 
			mBundle.putInt(Constants.BUNDLE_KEY_POI_ID, poi.id);
			intent_poi.putExtras(mBundle); 		
			
			startActivity(intent_poi);
			return;
		} else {
			showResultsOnMap(mContext);
		}
		
	}	
	
	private void showResultsOnMap(SearchContext mContext){
		Intent data = new Intent(this, MapViewerActivity.class);
		data.setAction(Constants.ActionSearch);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(Constants.BUNDLE_RESULT_SEARCH_CONTEXT, mContext);
		data.putExtras(mBundle);

		startActivity(data);
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		View v = findViewById(R.id.search_text_input);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		
		finish();
	}
		
	public void clearHistoryClick(View v) {
		history.clearRecords();
		
		final HistoryDataList historyAda = new HistoryDataList(this, 
				R.layout.list_history, history.getHistory());
	
	    lv.setAdapter(historyAda);	 
	}
	


	public void shortcutClick(View v){
		String txt = ((TextView)v).getText().toString();
		
		performSearch(txt);
	}
		
	public void moreClick(View v){
        Intent i = new Intent(this, ShortcutEntryActivity.class); 
		startActivity(i);				
	}	
}
