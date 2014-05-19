package com.winjune.wifiindoor.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.adapter.SearchResultList;
import com.winjune.wifiindoor.adapter.HistoryDataList;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.SearchContext;
import com.winjune.wifiindoor.poi.SearchHistory;

public class RouteInputActivity extends Activity {
	private SearchHistory history;
	public static String RESULT_SEARCH_CONTEXT = "POINT_INPUT_CONTEXT";
	
	private ListView lv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_input);

		history = new SearchHistory();
		history.loadCachedData();

		// setup auto-complete
		String[] labelArray = POIManager.buildAutoCompleteText();
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

		// setup history info
		lv = (ListView) findViewById(R.id.search_history_list);

		final HistoryDataList historyAda = new HistoryDataList(this,
				R.layout.list_history, history.getHistory());
		
		lv.setAdapter(historyAda);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				performSearch(historyAda.getRecord(position));
			}
		});
	}

	public void backClick(View v) {
		onBackPressed();
	}

	public void searchClick(View v) {
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

		SearchContext mContext = new SearchContext(text);
		
		if (text.equals("我的位置")) {
			mContext.searchText = "我的位置";
			returnToRouteMain(mContext);			
			return;
		}
		
		mContext.poiResults = POIManager.searchPOIsbyLabel(text);
		
		if (mContext.poiResults.size() == 0) {
			AlertDialog alertDialog = new AlertDialog.Builder(this,
					AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
					.setMessage("找不到相关位置").setPositiveButton("确定", null)
					.create();
			alertDialog.show();
			return;
		}

		history.addRecord(text);
		

		// by default, the first is the focus one
		mContext.currentFocusIdx = 0;

		if (mContext.poiResults.size() == 1) {

			returnToRouteMain(mContext);
		} else
			showResultDialog(mContext);
		
		
	}

	private void showResultDialog(final SearchContext mContext) {

		SearchResultList resultsAda = new SearchResultList(this,
				R.layout.list_search_result, mContext);

		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setTitle("您要找的是:");
		builder.setAdapter(resultsAda, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				
				// remove all unselected items 
				for (int i=0; i<mContext.poiResults.size(); i++){
					if (position != i)
						mContext.poiResults.remove(i);
				}
								
				returnToRouteMain(mContext);
			}

		});

		builder.show();
	}

	private void returnToRouteMain(SearchContext mContext) {
		Intent data = new Intent();
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(RESULT_SEARCH_CONTEXT, mContext);
		data.putExtras(mBundle);

		setResult(RESULT_OK, data);

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
}
