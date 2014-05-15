package com.winjune.wifiindoor.webservice.types;

import java.util.ArrayList;

import com.winjune.common.webservice.core.types.IType;

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

}
