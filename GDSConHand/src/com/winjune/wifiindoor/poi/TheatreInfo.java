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
import com.winjune.wifiindoor.activity.poiviewer.TheatreInfoActivity;
import com.winjune.wifiindoor.lib.poi.MovieInfoR;
import com.winjune.wifiindoor.lib.poi.PlaceOfInterestR;

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
	
	public class POIOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	        Intent i = new Intent(v.getContext(), TheatreInfoActivity.class); 

			Bundle mBundle = new Bundle(); 
			mBundle.putInt(TheatreInfoActivity.BUNDLE_KEY_POI_ID, TheatreInfo.this.id);
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
		btn1_label.setText("分享");
	
	    View btn2 = (View) pop.getContentView().findViewById(R.id.button2);	    
		TextView btn2_label = (TextView) pop.getContentView().findViewById(R.id.button2_label);
		btn2_label.setText("今日排期");				
		btn2.setOnClickListener(getOnClickListener());

        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);		
	}	
}
