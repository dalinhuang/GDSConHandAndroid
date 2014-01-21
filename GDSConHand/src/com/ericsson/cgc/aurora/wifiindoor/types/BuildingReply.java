package com.ericsson.cgc.aurora.wifiindoor.types;

import com.ericsson.cgc.aurora.wifiindoor.map.Building;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

public class BuildingReply implements IType {
	private int id;
	private String category;
	private String name;
	private double latitude;
	private double longitude;
	private String maps;
	
	public String getCategory() {
		return category;
	}
	
	public int getId() {
		return id;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public String getName() {
		return name;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public void setName(String name) {
		this.name = name;
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
