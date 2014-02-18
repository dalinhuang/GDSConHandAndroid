package com.winjune.wifiindoor.types;

import java.util.ArrayList;

import com.winjune.wifiindoor.map.Building;
import com.winjune.wifiindoor.webservice.types.IType;

public class BuildingManagerReply implements IType {
	private int versionCode;
	private ArrayList<BuildingReply> buildings;
	
	public int getVersionCode() {
		return versionCode;
	}
	
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public ArrayList<BuildingReply> getBuildings() {
		return buildings;
	}

	public void setBuildings(ArrayList<BuildingReply> buildings) {
		this.buildings = buildings;
	}
	
	public ArrayList<Building> toBuildings() {
		if (buildings == null) {
			return null;
		}
		
		ArrayList<Building> buildings2 = new ArrayList<Building>();
		
		for (BuildingReply building:buildings) {
			buildings2.add(building.toBuilding());
		}
		
		return buildings2;
	}
}
