package com.ericsson.cgc.aurora.wifiindoor.types;

import java.util.ArrayList;

import com.ericsson.cgc.aurora.wifiindoor.map.MapManagerItem;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

public class MapManagerReply implements IType {
	private int versionCode;
	private ArrayList<MapManagerItemReply> mapItems;
	
	public ArrayList<MapManagerItemReply> getMapItems() {
		return mapItems;
	}
	
	public int getVersionCode() {
		return versionCode;
	}

	public void setMapItems(ArrayList<MapManagerItemReply> mapItems) {
		this.mapItems = mapItems;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public ArrayList<MapManagerItem> toMapItems() {
		if (mapItems == null) {
			return null;
		}
		
		ArrayList<MapManagerItem> items2 = new ArrayList<MapManagerItem>();
		
		for (MapManagerItemReply item:mapItems) {
			items2.add(item.toMapItem());
		}
		
		return items2;
	}
	
}
