package com.winjune.wifiindoor.poi;

import java.util.ArrayList;
import java.util.HashSet;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.poiviewer.BusStationInfoActivity;
import com.winjune.wifiindoor.activity.poiviewer.RestaurantInfoActivity;
import com.winjune.wifiindoor.activity.poiviewer.TheatreInfoActivity;
import com.winjune.wifiindoor.lib.poi.POIType;
import com.winjune.wifiindoor.lib.poi.PlaceOfInterestR;
import com.winjune.wifiindoor.lib.poi.RestaurantInfoR;

public class RestaurantInfo extends PlaceOfInterest{

	private ArrayList<RestaurantInfoR> menu = new ArrayList<RestaurantInfoR>();
	
	public RestaurantInfo(PlaceOfInterestR poiR) {
		// TODO Auto-generated constructor stub
		super(poiR);
	}
	
	public void addMenuItem(RestaurantInfoR menuItem) {
		menu.add(menuItem);
	}

	public void addMenuItem(String category, String name, String iconUrl, String price){
		RestaurantInfoR item = new RestaurantInfoR(id, category, name, iconUrl, price);		
		menu.add(item);		
	}

	public ArrayList<String> getCategoryList(){
		HashSet<String> categorySet = new HashSet<String> (); 				
		
		for (RestaurantInfoR item: menu)
			categorySet.add(item.category);
		
		String[] categoryArray = categorySet.toArray(new String[0]);
		
		ArrayList<String> list = new ArrayList<String>();
		
		for (String cat: categoryArray)
			list.add(cat);
		
		return list;
	}
	
	public ArrayList<RestaurantInfoR> getMenuByCategory(String category) {
		ArrayList<RestaurantInfoR> menuCat = new ArrayList<RestaurantInfoR>();
		
		for (RestaurantInfoR item:menu){
			if (category.equals(item.category))
				menuCat.add(item);							
		}
			
		return menuCat;
	}
	
	public OnClickListener getBtnDetailClickListener(){
		return getBtn1ClickListener();	
	}
	
	public String getBtn1Label(){		
		return "今日菜单";
	}
	
	public OnClickListener getBtn1ClickListener(){
		OnClickListener mBtnListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(v.getContext(), RestaurantInfoActivity.class); 

				Bundle mBundle = new Bundle(); 
				mBundle.putInt(TheatreInfoActivity.BUNDLE_KEY_POI_ID, RestaurantInfo.this.id);
				i.putExtras(mBundle); 	
				
		        v.getContext().startActivity(i);				
			}
		};
		
		return mBtnListener;			
	}			
}
