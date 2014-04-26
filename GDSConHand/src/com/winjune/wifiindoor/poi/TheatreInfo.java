package com.winjune.wifiindoor.poi;

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
import com.winjune.wifiindoor.activity.POIViewer.TheatreInfoActivity;

public class TheatreInfo extends PlaceOfInterest{
	
	private ArrayList<MovieInfo> movies;
	
	public TheatreInfo(){
		super(POIType.Theatre);
		this.label = "巨幕影院";
		this.detailedDesc = "3D巨幕影院屏幕宽29米、高22米，是目前亚洲地区最大的巨幕影院，可容纳610名观众同时体验。其放映设备采用当今世界上技术含量最高、画格最大的70毫米15齿孔IMAX立体放映系统，它使用的70毫米15齿孔胶片有效画幅是普通35毫米胶片的10倍，它独特的“波形环状”输片设计，把每一个画格牢牢地吸附在片门之上，使画面清晰稳定。";
		movies = new ArrayList<MovieInfo>();
	}
	
	public void addMovie(MovieInfo movie){
		movies.add(movie);
	}

	public ArrayList<MovieInfo> getMovies() {				
		return movies;
	}
	
	public MovieInfo getMovieByIdx(int idx){
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
