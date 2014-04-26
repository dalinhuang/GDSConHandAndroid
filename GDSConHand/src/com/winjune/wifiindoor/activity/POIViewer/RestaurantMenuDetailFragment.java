package com.winjune.wifiindoor.activity.POIViewer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.id;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.RestaurantInfo;

/**
 * A fragment representing a single MenuItem detail screen. This fragment is
 * either contained in a {@link RestaurantInfoActivity} in two-pane mode (on
 * tablets) or a {@link RestaurantMenuDetailActivity} on handsets.
 */
public class RestaurantMenuDetailFragment extends Fragment {
	public static String BUNDLE_KEY_POI_ID = "POI_ID";
	public static String MENU_CATEGORY_IDX = "MENU_CATEGORY_IDX";


	/**
	 * The dummy content this fragment is presenting.
	 */
	private int poiId;	
	private RestaurantInfo poi;
	private String category;
	private ArrayList<RestaurantInfo.MenuItem> categoryMenu;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RestaurantMenuDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle arguments = getArguments();
		poiId = arguments.getInt(BUNDLE_KEY_POI_ID);		
		category = arguments.getString(MENU_CATEGORY_IDX);
		
		poi = (RestaurantInfo)POIManager.getPOIbyId(poiId);

		categoryMenu = poi.getMenuByCategory(category);	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_restaurant_menu_detail,
				container, false);
		
		ListView lv = (ListView)rootView.findViewById(R.id.menu_item_list);
		
		MenuItemList ada = new MenuItemList(getActivity(), R.layout.list_menu_item, categoryMenu);
		
		lv.setAdapter(ada);		
		
		return rootView;
	}
	
	public class MenuItemList extends ArrayAdapter<RestaurantInfo.MenuItem> {
		private Context context;
		private List<RestaurantInfo.MenuItem> items; 
			
		public MenuItemList(Context context, int resource, List<RestaurantInfo.MenuItem> items) {
			super(context, resource, items);
			this.context = context;
			this.items = items;
			// TODO Auto-generated constructor stub
		}
			
		@Override  
		public View getView(int position, View convertView, ViewGroup parent){  
			LayoutInflater vi = LayoutInflater.from(context);  
	 
			View view=vi.inflate(R.layout.list_menu_item, null);
			
			TextView itemName = (TextView)view.findViewById(R.id.menu_item_name);
			itemName.setText(items.get(position).name);
			
			TextView itemPrice = (TextView)view.findViewById(R.id.menu_item_price);
			itemPrice.setText(items.get(position).priceInfo);			
					        
		    return view;  
		}   		
	}		
}
