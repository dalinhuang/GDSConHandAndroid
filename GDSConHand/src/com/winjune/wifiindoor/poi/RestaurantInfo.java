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
import com.winjune.wifiindoor.activity.poiviewer.RestaurantInfoActivity;
import com.winjune.wifiindoor.activity.poiviewer.TheatreInfoActivity;

public class RestaurantInfo extends PlaceOfInterest{

	private ArrayList<MenuItem> menu = new ArrayList<MenuItem>();
	
	public RestaurantInfo(){
		super(POIType.Restaurant);
	}
	
	public void addMenuItem(String category, String name, String iconUrl, String price){
		MenuItem item = new MenuItem(category, name, iconUrl, price);		
		menu.add(item);		
	}

	public ArrayList<String> getCategoryList(){
		HashSet<String> categorySet = new HashSet<String> (); 				
		
		for (MenuItem item: menu)
			categorySet.add(item.category);
		
		String[] categoryArray = categorySet.toArray(new String[0]);
		
		ArrayList<String> list = new ArrayList<String>();
		
		for (String cat: categoryArray)
			list.add(cat);
		
		return list;
	}
	
	public ArrayList<MenuItem> getMenuByCategory(String category) {
		ArrayList<MenuItem> menuCat = new ArrayList<MenuItem>();
		
		for (MenuItem item:menu){
			if (category.equals(item.category))
				menuCat.add(item);							
		}
			
		return menuCat;
	}
	
	
	public class POIOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	        Intent i = new Intent(v.getContext(), RestaurantInfoActivity.class); 

			Bundle mBundle = new Bundle(); 
			mBundle.putInt(TheatreInfoActivity.BUNDLE_KEY_POI_ID, RestaurantInfo.this.id);
			i.putExtras(mBundle); 	
			
	        v.getContext().startActivity(i);				
		}			
	}	
	
	public View.OnClickListener getOnClickListener(){
		return new POIOnClickListener();
	}	
	
	public void showContextMenu(View v){
		LayoutInflater inflater = LayoutInflater.from(v.getContext()); 
		View view = inflater.inflate(R.layout.popup_context_menu3, null); 
		
		final PopupWindow pop = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false); 		  				
		pop.setBackgroundDrawable(new BitmapDrawable()); 
        pop.setOutsideTouchable(true); 
        pop.setFocusable(true);

        TextView labelText = (TextView) pop.getContentView().findViewById(R.id.text_label);                
        labelText.setText(this.label);
        
        TextView generalText = (TextView) pop.getContentView().findViewById(R.id.text_general);
        generalText.setText(getGeneralDesc());         
        
      
        View detailBtn = (View) pop.getContentView().findViewById(R.id.btn_detail);
        detailBtn.setOnClickListener(getOnClickListener());
        
        View btn1 = (View) pop.getContentView().findViewById(R.id.button1);
		TextView btn1_label = (TextView) pop.getContentView().findViewById(R.id.button1_label);
		btn1_label.setText("到这去");
	
	    View btn2 = (View) pop.getContentView().findViewById(R.id.button2);	    
		TextView btn2_label = (TextView) pop.getContentView().findViewById(R.id.button2_label);
		btn2_label.setText("今日菜单");				
		btn2.setOnClickListener(getOnClickListener());

        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);		
	}		
		
	public class MenuItem {
		public String  category;
		public String 	priceInfo;
		public String	iconUrl;
		public String  name;
		
		public MenuItem(String category, String name, String iconUrl, String price){
			this.category = category;
			this.name = name;
			this.iconUrl = iconUrl;
			this.priceInfo = price;
		}
	}		
}
