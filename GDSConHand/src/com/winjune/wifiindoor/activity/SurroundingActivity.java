package com.winjune.wifiindoor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.winjune.wifiindoor.R;

public class SurroundingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surrounding);
    }
    
    public void jumpToBusMapClick(View v){		
  		Intent i = new Intent(this, BusMapActivity.class); 
  		startActivity(i);	
  	}
    
    public void jumpToRestaurantMenu(View v){		
  		Intent i = new Intent(this, MenuItemListActivity.class); 
  		startActivity(i);	
  	}
    
    public void jumpToMovieTheatre3D(View v){		
  		Intent i = new Intent(this, MovieInfoActivity.class); 
  		startActivity(i);	
  	}

    public void backClick(View v) {
    	onBackPressed();    	
    }    
}
