package com.winjune.wifiindoor.poi;

import java.io.Serializable;
import java.util.ArrayList;

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
import com.winjune.wifiindoor.lib.poi.MovieInfoR;
import com.winjune.wifiindoor.lib.poi.PlaceOfInterestR;
import com.winjune.wifiindoor.util.Constants;

public class TheatreInfo extends PlaceOfInterest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6940701012727934560L;
	private ArrayList<MovieInfoR> movies;
	
	public TheatreInfo(PlaceOfInterestR poi){
		super(poi);

		movies = new ArrayList<MovieInfoR>();
	}
	
	public void addMovie(MovieInfoR movie){
		movies.add(movie);
	}

	public ArrayList<MovieInfoR> getMovies() {				
		return movies;
	}
	
	public MovieInfoR getMovieByIdx(int idx){
		return movies.get(idx);
	}
	
	public int getMovieNum(){
		return movies.size();
	}
	
	public String getContextBtn1Label(){		
		return "今日排期";
	}
	
	public OnClickListener getContextBtn1ClickListener(){
		OnClickListener mBtnListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(v.getContext(), TheatreInfoActivity.class); 

				Bundle mBundle = new Bundle(); 
				mBundle.putInt(Constants.BUNDLE_KEY_POI_ID, TheatreInfo.this.id);
				i.putExtras(mBundle); 	
				
		        v.getContext().startActivity(i);				
			}
		};
		
		return mBtnListener;			
	}	
}
