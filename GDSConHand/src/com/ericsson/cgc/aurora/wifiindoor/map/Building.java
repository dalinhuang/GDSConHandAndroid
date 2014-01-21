package com.ericsson.cgc.aurora.wifiindoor.map;

import java.io.Serializable;

public class Building implements Serializable{
	private static final long serialVersionUID = -6298128825404411780L;
	
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
	
	public String getMaps() {
		return maps;
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

	public void setMaps(String maps) {
		this.maps = maps;
	}

	public void setName(String name) {
		this.name = name;
	}
}
