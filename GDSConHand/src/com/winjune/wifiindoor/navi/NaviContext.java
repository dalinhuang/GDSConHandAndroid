package com.winjune.wifiindoor.navi;

import java.io.Serializable;
import java.util.ArrayList;

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

}
