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
import com.winjune.wifiindoor.util.Util;

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
				mBundle.putInt(POINormalViewerActivity.BUNDLE_KEY_POI_ID, PlaceOfInterest.this.id);
				i.putExtras(mBundle); 	
				
		        v.getContext().startActivity(i);				
			}
		};
		
		return mBtnListener;			
	}
	
	public String getContextBtn1Label(){
		String label = "分享";
		return label;
	}
	
	public OnClickListener getContextBtn1ClickListener(){
		OnClickListener mBtnListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(v.getContext(), POINormalViewerActivity.class); 

				Bundle mBundle = new Bundle(); 
				mBundle.putInt(POINormalViewerActivity.BUNDLE_KEY_POI_ID, PlaceOfInterest.this.id);
				i.putExtras(mBundle); 	
				
		        v.getContext().startActivity(i);				
			}
		};
		
		return mBtnListener;			
	}	
	
	public String getContextBtn2Label(){
		String label = "到这去";
		return label;	
	}
	
	public OnClickListener getContextBtn2ClickListener(){
		OnClickListener mBtnListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(v.getContext(), POINormalViewerActivity.class); 

				Bundle mBundle = new Bundle(); 
				mBundle.putInt(POINormalViewerActivity.BUNDLE_KEY_POI_ID, PlaceOfInterest.this.id);
				i.putExtras(mBundle); 	
				
		        v.getContext().startActivity(i);				
			}
		};
		
		return mBtnListener;			
	}	
	
	public String getContentBtn1Label(){
		return getContextBtn1Label();
	}
	
	public OnClickListener getContentBtn1ClickListener(){
		return getContextBtn1ClickListener();
	}	
	
	public String getContentBtn2Label(){
		return getContextBtn2Label();	
	}
	
	public OnClickListener getContentBtn2ClickListener(){
		return getContextBtn2ClickListener();			
	}			
	
	public void setupContentButton(View v){
        View btn1 = v.findViewById(R.id.button1);
		TextView btn1_label = (TextView)v.findViewById(R.id.button1_label);
		btn1_label.setText(getContextBtn1Label());
		btn1.setOnClickListener(getContextBtn1ClickListener());
		
        View btn2 = v.findViewById(R.id.button2);
		TextView btn2_label = (TextView)v.findViewById(R.id.button2_label);
		btn2_label.setText(getContextBtn2Label());
		btn2.setOnClickListener(getContextBtn2ClickListener());				
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
		btn1_label.setText(getContextBtn1Label());
		btn1.setOnClickListener(getContextBtn1ClickListener());
		
        View btn2 = (View) pop.getContentView().findViewById(R.id.button2);
		TextView btn2_label = (TextView) pop.getContentView().findViewById(R.id.button2_label);
		btn2_label.setText(getContextBtn2Label());
		btn2.setOnClickListener(getContextBtn2ClickListener());				

        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);		
	}

	public int getMapX() {
		return super.getPlaceX()*Util.getRuntimeIndoorMap().getMapWidth()/Util.getRuntimeIndoorMap().getMaxLongitude();
	}

	public int getMapY() {
		return super.getPlaceY()*Util.getRuntimeIndoorMap().getMapHeight()/Util.getRuntimeIndoorMap().getMaxLatitude();
	}
}
