package com.winjune.wifiindoor.types;

import com.winjune.wifiindoor.map.Building;
import com.winjune.wifiindoor.webservice.types.IType;

public class BuildingReply implements IType {
	private int id;
	private String category;
	private String name;
	private double latitude;
	private double longitude;
	private String maps;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public Building toBuilding() {
		Building building = new Building();
		building.setId(id);
		building.setCategory(category);
		building.setLatitude(latitude);
		building.setLongitude(longitude);
		building.setName(name);
		building.setMaps(maps);
		
		return building;
	}
}
