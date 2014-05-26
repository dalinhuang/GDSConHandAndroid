package com.winjune.wifiindoor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.map.MapManager;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.poi.SearchContext;

public class SearchResultList extends ArrayAdapter<PlaceOfInterest> {
	private Context context;
	private SearchContext mSearchContext; 
		
	public SearchResultList(Context context, int resource, SearchContext mSearchContext) {
		super(context, resource, mSearchContext.poiResults);
		this.context = context;
		this.mSearchContext = mSearchContext;
		// TODO Auto-generated constructor stub
	}
		
	@Override  
	public View getView(int position, View convertView, ViewGroup parent){  
		View view=convertView;
		
		if (view == null) {
			LayoutInflater vi = LayoutInflater.from(context);   
			view=vi.inflate(R.layout.list_search_result, parent, false);
		}
		
		TextView poiLabelV = (TextView)view.findViewById(R.id.text_label);
		
		String poiLabel = (position +1) +". "+ mSearchContext.poiResults.get(position).label;
		poiLabelV.setText(poiLabel);
		
		TextView placeInfoV = (TextView)view.findViewById(R.id.text_floor_hall);
		int mapId = mSearchContext.poiResults.get(position).mapId;
		String placeInfo = MapManager.getMapById(mapId).getLabel();
		placeInfoV.setText(placeInfo);			
				        
	    return view;  
	}   		
}	