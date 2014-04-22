package com.winjune.wifiindoor.poi;

enum POIType {
	Normal,
	BusStation,
	Theatre,
	Restaurant	
}

public class PlaceOfInterest {
	
	private POIType poiType;
	
	private int id;
	
	private int ttsNo;
	
	private int mapId;
	
	private int placeX;
	private int placeY;

	private boolean visible;
	private String 	iconUrl;

	private String label;
	private String generalDesc;
	private String detailedDesc;
	
	private String descURL;
	
	
	private boolean shareble;
	private boolean reachable;
	private boolean readable;
	
	private float scale;
	private float alpha;
	private float rotation;
	private float minZoomFactor;
	private float maxZoomFactor;
	
	public int getMapId(){
		return mapId;
	}
	
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	
	public int getX() {
		return placeX;
	}
	
	public void setX(int x) {
		this.placeX = x;
	}

	public int getY() {
		return placeY;
	}

	public void setY(int y) {
		this.placeY = y;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String info) {
		this.label = info;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getMinZoomFactor() {
		return minZoomFactor;
	}

	public void setMinZoomFactor(float minZoomFactor) {
		this.minZoomFactor = minZoomFactor;
	}

	public float getMaxZoomFactor() {
		return maxZoomFactor;
	}

	public void setMaxZoomFactor(float maxZoomFactor) {
		this.maxZoomFactor = maxZoomFactor;
	}	
	
}
