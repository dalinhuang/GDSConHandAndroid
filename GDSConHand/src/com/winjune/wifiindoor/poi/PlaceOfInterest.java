package com.winjune.wifiindoor.poi;

import android.app.ActionBar.LayoutParams;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winjune.wifiindoor.R;

enum POIType {
	Normal,
	BusStation,
	Theatre,
	Playhouse,
	Restaurant	
}

enum ContextMenuRequestFrom {
	Location,
	Selection
}

public class PlaceOfInterest {
	
	private static int ID_GENERATOR = 0;
	
	public POIType poiType;
	
	public int id;
	
	public int hallId;

	
	public int ttsNo;
	
	public int mapId;
	
	public int placeX;
	public int placeY;

	private String 	iconUrl;
	public String 	audioUrl;
	public String 	webURL;

	public String label;
	protected String generalDesc;
	public String detailedDesc;
		
	
	public boolean shareble;
	public boolean reachable;
	public boolean readable;
	
	public float scale;
	public float alpha;
	public float rotation;
	public float minZoomFactor;
	public float maxZoomFactor;
	
	
	public PlaceOfInterest(POIType poiType){
		this.poiType = poiType;
		this.id = ID_GENERATOR ++;
		
		this.ttsNo = 0;
		this.mapId = 0;
		this.placeX = 0;
		this.placeY = 0;
			
		this.iconUrl = "";		
		this.label = "";

		this.generalDesc = "";
		this.detailedDesc = "";
		
		this.webURL = "";
		
		this.shareble = false;
		
		this.reachable = true;
		
		this.readable = false;
		
		this.scale = 0f;
		this.alpha = 0f;
		this.rotation = 0f;
		this.maxZoomFactor = 0.5f;
		this.maxZoomFactor = 3f;							
	}
	
	public PlaceOfInterest(){
		this(POIType.Normal);
	}
	
	public PlaceOfInterest(String label){
		this(POIType.Normal);
		this.label = label;
	}
			
	public String getGeneralDesc(){
		return generalDesc;
	}

	public int getHall(){
		return hallId;
	}
	
	public String getLabel(){
		return label;
	}	

	public int getX(){
		return placeX;
	}
	
	public void setX(int x){
		this.placeX = x;
	}
	
	public int getY(){
		return placeY;
	}
	
	public void setY(int y){
		this.placeY = y;
	}
	
	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getMinZoomFactor() {
		return minZoomFactor;
	}

	public void setMinZoomFactor(float minZoomFactor) {
		this.minZoomFactor = minZoomFactor;
	}

	public float getMaxZoomFactor() {
		return maxZoomFactor;
	}

	public void setMaxZoomFactor(float maxZoomFactor) {
		this.maxZoomFactor = maxZoomFactor;
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
        //detailBtn.setOnClickListener(getOnClickListener());
        
        View btn1 = (View) pop.getContentView().findViewById(R.id.button1);
		TextView btn1_label = (TextView) pop.getContentView().findViewById(R.id.button1_label);
		btn1_label.setText("分享");
		btn1.setOnClickListener(new OnClickListener() {
			@Override
		    public void onClick(View v) {
			}
		});

        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);		
	}	
}
