package com.winjune.wifiindoor.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.poiviewer.BusStationInfoActivity;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

public class BusStation extends PlaceOfInterest implements Serializable{
		
	private static final long serialVersionUID = -5969028824407419064L;
	
	private ArrayList<BusLine> busLines;
	
	public BusStation(){
		super();
		
		this.label = "大学城科学中心总站";
		busLines = new ArrayList<BusLine>();		
	}
	
	//Set Alias for the XML serialization
	private void setAlias(XStream xs){
		xs.alias("BusStation", com.winjune.wifiindoor.poi.BusStation.class);
		xs.alias("BusLine", com.winjune.wifiindoor.poi.BusLine.class);
		//Invoke other objects' setAlias methods here
	}	
	
	//Serialize current IndoorMap to XML file
	private boolean toXML(FileOutputStream cacheFile){
		//Serialize this object
		XStream xs = new XStream();
		setAlias(xs);
					
		//Write to the map info file
		try{
			xs.toXML(this, cacheFile);						
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//Set current InoorMap from XML file
	public boolean fromXML(InputStream cacheFile){
		XStream xs = new XStream();
		setAlias(xs);

		try {
			xs.fromXML(cacheFile, this);			
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
		
	
	public void addBusLine(BusLine aBusLine) {		
		busLines.add(aBusLine);
	}
	
	public BusLine getBusLine(int index) {
		return busLines.get(index);
	}
	
	public String getGeneralDesc(){
		String info = "";
		boolean dividerNotNeeded = true; 
		
		for (BusLine line:busLines) {
			if (dividerNotNeeded) { 
				dividerNotNeeded = false;
				info += line.lineName;
			}else {
				info += ";" + line.lineName;
			}
															
		}
		
		return info;
	}
	
	public ArrayList<BusLine> getBusLines() {				
		return busLines;
	}
	
	public View.OnClickListener getOnClickListener(){
		return new POIOnClickListener();
	}
	
	public class POIOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
	        Intent i = new Intent(v.getContext(), BusStationInfoActivity.class); 

			Bundle mBundle = new Bundle(); 
			mBundle.putInt(BusStationInfoActivity.BUNDLE_KEY_POI_ID, BusStation.this.id);
			i.putExtras(mBundle); 	
			
	        v.getContext().startActivity(i);				
		}			
	}	
	
	public void toXML(){
		
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
		btn1.setOnClickListener(new OnClickListener() {
			@Override
		    public void onClick(View v) {
			}
		});

        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);		
	}
}
