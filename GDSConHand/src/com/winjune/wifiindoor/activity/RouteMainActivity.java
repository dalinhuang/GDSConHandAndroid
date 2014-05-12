package com.winjune.wifiindoor.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.adapter.HistoryDataList;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.poi.RouteSearchHistory;
import com.winjune.wifiindoor.poi.SearchContext;

public class RouteMainActivity extends Activity {
	public static final int TERMINATION_REQUEST_CODE = 0;
	public static final int START_REQUEST_CODE = 1;
	public static String IS_START_POINT = "START_POINT";

	private TextView startPointText = null;
	private TextView endPointText = null;
	private ListView lv = null;
	private PlaceOfInterest startPoi = null;
	private PlaceOfInterest endPoi = null;

	private RouteSearchHistory history;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_main);

		history = new RouteSearchHistory();
		history.loadCachedData();

		startPointText = (TextView) findViewById(R.id.input_start_point);
		endPointText = (TextView) findViewById(R.id.input_end_point);

		// setup history info
		lv = (ListView) findViewById(R.id.route_history_list);

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

		performSearch(startText + " - " + endText);

	}

	public void transitClick(View v) {
		String startPointTextString = startPointText.getText().toString();
		String endPointTextString = endPointText.getText().toString();

		PlaceOfInterest tempPoi = null;

		tempPoi = startPoi;
		startPoi = endPoi;
		endPoi = tempPoi;

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

	private void performSearch(String text) {
		if (text.isEmpty())
			return;

		history.addRecord(text);

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
		startActivityForResult(i, TERMINATION_REQUEST_CODE);
		i.putExtra(IS_START_POINT, false);
	}

	public void jumpToInputStartPointClick(View v) {
		Intent i = new Intent(this, RouteInputActivity.class);
		i.putExtra(IS_START_POINT, true);
		startActivityForResult(i, START_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case TERMINATION_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();

				SearchContext mContext = (SearchContext) bundle
						.getSerializable(RouteInputActivity.RESULT_SEARCH_CONTEXT);
				endPointText.setText(mContext.searchText);

				endPoi = mContext.poiResults.get(mContext.currentFocusIdx);
				endPointText.setText(endPoi.label);

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
				} else {
					startPoi = mContext.poiResults
							.get(mContext.currentFocusIdx);
					startPointText.setText(startPoi.label);
				}

			}

			break;
		default:
		}
	}

}
