package com.winjune.wifiindoor.navi;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.lib.map.NaviNodeR;

public class NaviContext implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1905197474553939983L;
	
	public String text;
	public ArrayList<NaviNodeR> naviRoute;
	
	public NaviContext(String text){
		this.text = text;
		naviRoute = new ArrayList<NaviNodeR>();
	}
	
	public void showContextMenu(View v){
		LayoutInflater inflater = LayoutInflater.from(v.getContext()); 
		View view = inflater.inflate(R.layout.popup_context_menu, null); 
		
		final PopupWindow pop = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false); 		  				
		pop.setBackgroundDrawable(new BitmapDrawable()); 
        pop.setOutsideTouchable(true); 
        pop.setFocusable(true);

        TextView labelText = (TextView) pop.getContentView().findViewById(R.id.text_label);                
        labelText.setText("路线概述");
        
        TextView generalText = (TextView) pop.getContentView().findViewById(R.id.text_general);
        generalText.setText(this.text); 		

        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);		
	}	

}
