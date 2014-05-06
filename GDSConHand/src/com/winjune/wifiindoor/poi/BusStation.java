package com.winjune.wifiindoor.poi;

import java.util.ArrayList;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.poiviewer.BusStationInfoActivity;
import com.winjune.wifiindoor.activity.poiviewer.POINormalViewerActivity;
import com.winjune.wifiindoor.activity.poiviewer.TheatreInfoActivity;
import com.winjune.wifiindoor.lib.poi.BusLineR;
import com.winjune.wifiindoor.lib.poi.PlaceOfInterestR;

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

@SuppressWarnings("serial")
public class BusStation extends PlaceOfInterest{
				
	private ArrayList<BusLineR> busLines;
	
	public BusStation(PlaceOfInterestR poi){
		super(poi);
		
		busLines = new ArrayList<BusLineR>();		
	}
		
	public void addBusLine(BusLineR aBusLine) {		
		busLines.add(aBusLine);
	}
	
	public BusLineR getBusLine(int index) {
		return busLines.get(index);
	}
	
	public String getGeneralDesc(){
		String info = "";
		boolean dividerNotNeeded = true; 
		
		for (BusLineR line:busLines) {
			if (dividerNotNeeded) { 
				dividerNotNeeded = false;
				info += line.lineName;
			}else {
				info += ";" + line.lineName;
			}
															
		}
		
		return info;
	}
	
	public ArrayList<BusLineR> getBusLines() {				
		return busLines;
	}
	
	public OnClickListener getBtnDetailClickListener(){
		return getBtn1ClickListener();	
	}
	
	public String getBtn1Label(){		
		return "查线路";
	}
	
	public OnClickListener getBtn1ClickListener(){
		OnClickListener mBtnListener = new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        Intent i = new Intent(v.getContext(), BusStationInfoActivity.class); 

				Bundle mBundle = new Bundle(); 
				mBundle.putInt(TheatreInfoActivity.BUNDLE_KEY_POI_ID, BusStation.this.id);
				i.putExtras(mBundle); 	
				
		        v.getContext().startActivity(i);				
			}
		};
		
		return mBtnListener;			
	}	
		
}
