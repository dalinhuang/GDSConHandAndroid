package com.winjune.wifiindoor.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.adapter.HistoryDataList;
import com.winjune.wifiindoor.adapter.SearchResultList;
import com.winjune.wifiindoor.navi.NaviContext;
import com.winjune.wifiindoor.navi.Navigator;
import com.winjune.wifiindoor.navi.NaviHistory;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.poi.SearchContext;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.Util;

public class RouteMainActivity extends Activity {
	public static final int END_REQUEST_CODE = 0;
	public static final int START_REQUEST_CODE = 1;

	private TextView startPointText = null;
	private TextView endPointText = null;
	private ListView lv = null;
	
	private String searchText = "";
	private boolean startInput = false;
	private boolean endInput = false;
	private PlaceOfInterest startPoi = null;
	private PlaceOfInterest endPoi = null;
	
	private NaviHistory history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_main);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)	{
			int endPoiId = bundle.getInt(Constants.BUNDLE_KEY_POI_ID);
			if (endPoiId != 0)
				endPoi = POIManager.getPOIbyId(endPoiId);
		}

		history = new NaviHistory();
		history.loadCachedData();

		startPointText = (TextView) findViewById(R.id.input_start_point);
		endPointText = (TextView) findViewById(R.id.input_end_point);
		
		if (endPoi != null)
			endPointText.setText(endPoi.getLabel());
		else
			endPoi = new PlaceOfInterest();
		
		// setup history info
		lv = (ListView) findViewById(R.id.route_history_list);

		final HistoryDataList historyAda = new HistoryDataList(this,
				R.layout.list_history, history.getHistory());

		lv.setAdapter(historyAda);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				searchText = historyAda.getRecord(position);
				String startEndStrs[] = searchText.split(" - ");
				

				ArrayList<PlaceOfInterest> endOptions = POIManager.searchPOIsbyLabel(startEndStrs[1]);
				
				if (endOptions.size() == 1){
					endPoi = endOptions.get(0);
					endInput = true;
				} else if (endOptions.size() > 1) {
					showPlaceOptionsDialog(endOptions, false);
				}
							
				ArrayList<PlaceOfInterest> startOptions = POIManager.searchPOIsbyLabel(startEndStrs[0]);
				
				if (startOptions.size() == 1){
					startPoi = startOptions.get(0);
					startInput = true;
				} else if (startOptions.size() > 1) {
					showPlaceOptionsDialog(startOptions, true);
				}
														
				
				if (startInput && endInput){			
					performSearch();
				}
			}
		});

	}

	public void searchClick(View v) {
		String startText = startPointText.getText().toString();
		String endText = endPointText.getText().toString();

		if (startText.equals("输入起点. . .")) {
			AlertDialog alertDialog = new AlertDialog.Builder(this,
					AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setMessage("请输入起点")
					.setPositiveButton("确定", null).create();
			alertDialog.show();
			return;
		}

		if (endText.equals("输入终点. . .")) {
			AlertDialog alertDialog = new AlertDialog.Builder(this,
					AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setMessage("请输入终点")
					.setPositiveButton("确定", null).create();
			alertDialog.show();
			return;
		}	

		if (startText.equals("我的位置")) {
			startInput = true;
			startPoi = new PlaceOfInterest();
		}		
		
		searchText = startText + " - " + endText;
		
		performSearch();

	}

	public void transitClick(View v) {
		String startPointTextString = startPointText.getText().toString();
		String endPointTextString = endPointText.getText().toString();

		PlaceOfInterest tempPoi = startPoi;
		boolean tempInput = startInput;

		startPoi = endPoi;
		startInput = endInput;
		endPoi = tempPoi;
		endInput = tempInput;

		if (startPoi != null) {
			startPointText.setText(startPoi.label);
		}
		else if (startPointTextString.equals("我的位置")){
			startPointText.setText("输入起点. . .");
		}
		else {
			startPointText.setText("我的位置");
		}
		
		if (endPoi != null) {
			endPointText.setText(endPoi.label);
		}
		else if (endPointTextString.equals("我的位置")){
			endPointText.setText("输入终点. . .");
		}
		else {
			endPointText.setText("我的位置");
		}
			

	}

	private void performSearch() {
		if (searchText.isEmpty())
			return;		
		
		if ((!startInput) | (!endInput))
			return;

		history.addRecord(searchText);

		NaviContext context = new NaviContext(searchText);
				
		context.naviRoute = Navigator.go(startPoi.id, endPoi.id);	
		
		if (context.naviRoute == null){
			Util.showToast(this, "No route", Toast.LENGTH_LONG);
			return;
		}			

		Intent resultI = new Intent(this, NaviResultActivity.class);

		Bundle mBundle = new Bundle(); 
		mBundle.putSerializable(Constants.BUNDLE_KEY_NAVI_CONTEXT, context);
		resultI.putExtras(mBundle);
		startActivity(resultI);
		
		finish();
	}

	public void clearHistoryClick(View v) {
		history.clearRecords();

		final HistoryDataList historyAda = new HistoryDataList(this,
				R.layout.list_history, history.getHistory());

		lv.setAdapter(historyAda);
	}

	public void backClick(View v) {
		onBackPressed();
	}

	public void jumpToInputEndPointClick(View v) {
		Intent i = new Intent(this, RouteInputActivity.class);
		startActivityForResult(i, END_REQUEST_CODE);
	}

	public void jumpToInputStartPointClick(View v) {
		Intent i = new Intent(this, RouteInputActivity.class);
		startActivityForResult(i, START_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case END_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();

				SearchContext mContext = (SearchContext) bundle
						.getSerializable(RouteInputActivity.RESULT_SEARCH_CONTEXT);
				endPointText.setText(mContext.searchText);

				endPoi = mContext.poiResults.get(mContext.currentFocusIdx);
				endPointText.setText(endPoi.label);
				endInput = true;
			}
			break;
		case START_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();

				SearchContext mContext = (SearchContext) bundle
						.getSerializable(RouteInputActivity.RESULT_SEARCH_CONTEXT);

				if (mContext.searchText != null
						&& mContext.searchText.equals("我的位置")) {
					startPointText.setText("我的位置");
					startInput = true;
					startPoi = new PlaceOfInterest();
				} else {
					startPoi = mContext.poiResults
							.get(mContext.currentFocusIdx);
					startPointText.setText(startPoi.label);
					startInput = true;
				}

			}

			break;
		default:
		}
	}
	
	private void showPlaceOptionsDialog(final ArrayList<PlaceOfInterest> poiOptions, final boolean isStart) {
		SearchContext context = new SearchContext("");
		context.poiResults = poiOptions;
		
		SearchResultList resultsAda = new SearchResultList(this,
				R.layout.list_search_result, context);

		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		if (isStart)
			builder.setTitle("请选择您的起点:");
		else
			builder.setTitle("请选择您的终点:");
		
		builder.setAdapter(resultsAda, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {			
				if (isStart) {
					RouteMainActivity.this.startPoi = poiOptions.get(position);
					startInput = true;
					
					if (startInput && endInput)
						performSearch();
					
				}
				else {
					RouteMainActivity.this.endPoi = poiOptions.get(position);
					endInput = true;
					
					if (startInput && endInput)
						performSearch();
				}
			}											

		});

		builder.show();
	}	

}
