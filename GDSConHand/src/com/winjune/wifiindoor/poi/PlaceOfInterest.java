package com.winjune.wifiindoor.poi;

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
import com.winjune.wifiindoor.activity.poiviewer.POINormalViewerActivity;
import com.winjune.wifiindoor.activity.poiviewer.RestaurantInfoActivity;
import com.winjune.wifiindoor.activity.poiviewer.TheatreInfoActivity;
import com.winjune.wifiindoor.lib.poi.PlaceOfInterestR;

enum ContextMenuRequestFrom {
	Location,
	Selection
}

@SuppressWarnings("serial")
public class PlaceOfInterest extends PlaceOfInterestR {
	
	public PlaceOfInterest(){
		super();
	}
	
	public PlaceOfInterest(PlaceOfInterestR poi){
		super(poi);
	}
	
	public OnClickListener getBtnDetailClickListener(){
		OnClickListener mBtnListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(v.getContext(), POINormalViewerActivity.class); 

				Bundle mBundle = new Bundle(); 
				mBundle.putInt(TheatreInfoActivity.BUNDLE_KEY_POI_ID, PlaceOfInterest.this.id);
				i.putExtras(mBundle); 	
				
		        v.getContext().startActivity(i);				
			}
		};
		
		return mBtnListener;			
	}
	
	public String getBtn1Label(){
		String label = "分享";
		return label;
	}
	
	public OnClickListener getBtn1ClickListener(){
		OnClickListener mBtnListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(v.getContext(), POINormalViewerActivity.class); 

				Bundle mBundle = new Bundle(); 
				mBundle.putInt(TheatreInfoActivity.BUNDLE_KEY_POI_ID, PlaceOfInterest.this.id);
				i.putExtras(mBundle); 	
				
		        v.getContext().startActivity(i);				
			}
		};
		
		return mBtnListener;			
	}	
	
	public String getBtn2Label(){
		String label = "到这去";
		return label;	
	}
	
	public OnClickListener getBtn2ClickListener(){
		OnClickListener mBtnListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(v.getContext(), POINormalViewerActivity.class); 

				Bundle mBundle = new Bundle(); 
				mBundle.putInt(TheatreInfoActivity.BUNDLE_KEY_POI_ID, PlaceOfInterest.this.id);
				i.putExtras(mBundle); 	
				
		        v.getContext().startActivity(i);				
			}
		};
		
		return mBtnListener;			
	}		
	
		
	public void showContextMenu(View v){
		LayoutInflater inflater = LayoutInflater.from(v.getContext()); 
		View view = inflater.inflate(R.layout.popup_context_menu2, null); 
		
		final PopupWindow pop = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false); 		  				
		pop.setBackgroundDrawable(new BitmapDrawable()); 
        pop.setOutsideTouchable(true); 
        pop.setFocusable(true);

        TextView labelText = (TextView) pop.getContentView().findViewById(R.id.text_label);                
        labelText.setText(this.label);
        
        TextView generalText = (TextView) pop.getContentView().findViewById(R.id.text_general);
        generalText.setText(getGeneralDesc());         
        
        View detailBtn = (View) pop.getContentView().findViewById(R.id.btn_detail);
        detailBtn.setOnClickListener(getBtnDetailClickListener());
        
        View btn1 = (View) pop.getContentView().findViewById(R.id.button1);
		TextView btn1_label = (TextView) pop.getContentView().findViewById(R.id.button1_label);
		btn1_label.setText(getBtn1Label());
		btn1.setOnClickListener(getBtn1ClickListener());
		
        View btn2 = (View) pop.getContentView().findViewById(R.id.button2);
		TextView btn2_label = (TextView) pop.getContentView().findViewById(R.id.button2_label);
		btn2_label.setText(getBtn2Label());
		btn2.setOnClickListener(getBtn2ClickListener());				

        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);		
	}
}
