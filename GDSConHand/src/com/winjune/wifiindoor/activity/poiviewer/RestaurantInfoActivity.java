package com.winjune.wifiindoor.activity.poiviewer;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.RestaurantInfo;
import com.winjune.wifiindoor.util.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * An activity representing a list of MenuItems. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link RestaurantMenuDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link RestaurantMenuListFragment} and the item details (if present) is a
 * {@link RestaurantMenuDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link RestaurantMenuListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class RestaurantInfoActivity extends FragmentActivity implements
	RestaurantMenuListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	
	public static String BUNDLE_KEY_POI_ID = "POI_ID";
	
	private boolean mTwoPane;
	
	private int poiId;
	private RestaurantInfo poi;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_info);
		
		Bundle mBundle = getIntent().getExtras();
		poiId = mBundle.getInt(BUNDLE_KEY_POI_ID);			
		
		poi = (RestaurantInfo)POIManager.getPOIbyId(poiId);
		TextView titleText = (TextView)findViewById(R.id.title_text);
		titleText.setText(poi.label);

		if (findViewById(R.id.menuitem_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			RestaurantMenuListFragment fragment = (RestaurantMenuListFragment) getSupportFragmentManager()
			.findFragmentById(R.id.menuitem_list);
			
			fragment.setActivateOnItemClick(true);
			
			int position = 0;
			ListView curList = fragment.getListView();
			curList.requestFocusFromTouch();
			curList.setSelection(position);
			curList.performItemClick(curList.getAdapter().getView(position, null, null), position, position);

		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link RestaurantMenuListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putInt(RestaurantMenuDetailFragment.BUNDLE_KEY_POI_ID, poiId);
			arguments.putString(RestaurantMenuDetailFragment.MENU_CATEGORY_IDX, id);
			
			RestaurantMenuDetailFragment fragment = new RestaurantMenuDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.menuitem_detail_container, fragment).commit();

		} 
	}
	
	public void backClick(View v) {
    	onBackPressed();    	
	}
	
	public void onMapClick(View v){
		Intent data = new Intent(this, MapViewerActivity.class);
		data.setAction(Constants.ActionLocate);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(Constants.BUNDLE_LOCATION_CONTEXT, poi);
		data.putExtras(mBundle);
		
		startActivity(data);
		
		finish();
	}
	
}
