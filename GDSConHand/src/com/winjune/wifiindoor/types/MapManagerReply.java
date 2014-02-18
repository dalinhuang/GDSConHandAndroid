package com.winjune.wifiindoor.types;

import java.util.ArrayList;

import com.winjune.wifiindoor.map.MapManagerItem;
import com.winjune.wifiindoor.webservice.types.IType;

public class MapManagerReply implements IType {
	private int versionCode;
	private ArrayList<MapManagerItemReply> mapItems;
	
	public int getVersionCode() {
		return versionCode;
	}
	
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public ArrayList<MapManagerItemReply> getMapItems() {
		return mapItems;
	}

	public void setMapItems(ArrayList<MapManagerItemReply> mapItems) {
		this.mapItems = mapItems;
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
