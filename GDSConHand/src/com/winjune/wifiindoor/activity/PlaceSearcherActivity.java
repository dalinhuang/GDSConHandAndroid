package com.winjune.wifiindoor.activity;

import java.util.ArrayList;

import org.andengine.entity.scene.menu.MenuScene;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.id;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.map.FieldInfo;
import com.winjune.wifiindoor.map.MapInfo;
import com.winjune.wifiindoor.map.MapSearchInfo;
import com.winjune.wifiindoor.map.SearchFieldInfo;
import com.winjune.wifiindoor.mapviewer.LabelBar;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PlaceSearcherActivity extends Activity {

	private int maxPlacesPerRow = 4;
	private int maxPlaces = 5;
	
	@Override
	protected void onResume() {
		Util.setEnergySave(false);
		Util.setCurrentForegroundActivity(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Util.setEnergySave(true);
		Util.setCurrentForegroundActivity(null);
		super.onPause();
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.place_searcher);

        String[] locationList = new String[0];
        ArrayList <String> locationArray = new ArrayList<String>();
		ArrayList<FieldInfo> fieldInfos = LabelBar.getMapInfo().getFields();
		if (fieldInfos != null) {

			for (FieldInfo fieldInfo : fieldInfos) {
				if (fieldInfo != null) {
					locationArray.add(fieldInfo.getInfo());
				}
			}
		}

        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.search_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
                android.R.layout.simple_dropdown_item_1line, locationArray.toArray(locationList));
        textView.setThreshold(1); 
        textView.setAdapter(adapter);
        
        TableLayout layout =(TableLayout) this.findViewById(R.id.tablelayout);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        TableRow.LayoutParams params = new TableRow.LayoutParams(
          (int) ((displayMetrics.widthPixels-layout.getPaddingLeft()-layout.getPaddingRight())
                 * (1f/maxPlacesPerRow)), TableRow.LayoutParams.MATCH_PARENT);

        int rows = (int) Math.ceil((double) maxPlaces/(double)maxPlacesPerRow);
		for (int i = 0; i < rows; i++) {
			if ((locationArray.size() == 0) || (locationArray.size() < (i * maxPlacesPerRow))) {
				break;
			}
			TableRow tableRow = new TableRow(this);
			for (int j = 0; j < maxPlacesPerRow; j++) {
				int currentIndex = j + i * maxPlacesPerRow;
				if (locationArray.size() < currentIndex
						|| currentIndex >= maxPlaces) {
					break;
				}
				Button button = new Button(this);
				button.setText(locationArray.get(currentIndex));
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String searchString = ((Button) view).getText()
								.toString();
						saveSearchResults(searchString);
					}
				});
				button.setLayoutParams(params);
				tableRow.addView(button);
			}
			layout.addView(tableRow);
		}
		this.setContentView(layout);
	}

	/** Called when the user clicks the Search button */
	public void placeSearch(View view) {
		// Do something in response to button
		AutoCompleteTextView searchTextView = (AutoCompleteTextView) findViewById(R.id.search_text);
		String searchString = searchTextView.getText().toString();
		saveSearchResults(searchString);
	}

	private void saveSearchResults(String searchString) {
		ArrayList<FieldInfo> fieldInfos = LabelBar.getMapInfo().getFields();
		ArrayList<SearchFieldInfo> searchFieldInfos = new ArrayList<SearchFieldInfo>();
		MapSearchInfo mapSearchInfo = new MapSearchInfo();
		boolean find = false;
		if (fieldInfos != null && !searchString.trim().isEmpty()) {

			for (FieldInfo fieldInfo : fieldInfos) {
				if (fieldInfo != null) {
					if (fieldInfo.getInfo().indexOf(searchString, 0) != -1)
					{
						SearchFieldInfo searchFieldInfo = new SearchFieldInfo();
						searchFieldInfo.setX(fieldInfo.getX());
						searchFieldInfo.setY(fieldInfo.getY());
						searchFieldInfos.add(searchFieldInfo);
						find = true;
					}
						
				}
			}
			
			if (find)
			{
			   mapSearchInfo.setSearchFields(searchFieldInfos);
			   mapSearchInfo.setId(LabelBar.getMapInfo().getId());
			   mapSearchInfo.setVersionCode(LabelBar.getMapInfo().getVersionCode());
			   mapSearchInfo.toXML();
			}
			else
			{
				AlertDialog alertDialog = new AlertDialog.Builder(this).setMessage(
						"没有找到"+searchString.trim()).setPositiveButton("确定", null).create();
				alertDialog.show();
				return;
			}
		}
		this.setResult(RESULT_OK);
		this.finish();
	}

}
