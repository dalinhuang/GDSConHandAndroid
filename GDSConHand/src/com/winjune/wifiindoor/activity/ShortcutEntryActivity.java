package com.winjune.wifiindoor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.winjune.wifiindoor.R;

public class ShortcutEntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut_entry);
    }
    
    public void jumpToBusMapClick(View v){		
  		Intent i = new Intent(this, BusMapActivity.class); 
  		startActivity(i);	
  	}
    
    public void jumpToRestaurantMenu(View v){		
  		Intent i = new Intent(this, RestaurantInfoActivity.class); 
  		startActivity(i);	
  	}
    
    public void backClick(View v) {
    	onBackPressed();    	
    }    
    
	public void restaurantClick(View v){
        Intent i = new Intent(this, RestaurantInfoActivity.class); 
		startActivity(i);				
	}
	
	public void iMaxTheatreClick(View v){
        Intent i = new Intent(this, TheatreInfoActivity.class); 
		startActivity(i);				
	}
}
