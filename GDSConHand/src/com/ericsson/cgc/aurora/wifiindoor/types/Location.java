/**
 * @(#)Test.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.types;

import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

/**
 * @author ezhipin, haleyshi
 * 
 */
public class Location implements IType {
	private int mapId;
	private int mapVersion;
	private int x;
	private int y;
	
	public Location() {
		
	}
	
	public Location(int mapId, int x, int y, int mapVersion) {
		this.mapId = mapId;
		this.x = x;
		this.y = y;
		this.mapVersion = mapVersion;
	}
	
	/**
	 * @return the mapId
	 */
	public int getMapId() {
		return mapId;
	}
	public int getMapVersion() {
		return mapVersion;
	}
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param mapId the mapId to set
	 */
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	public void setMapVersion(int mapVersion) {
		this.mapVersion = mapVersion;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "Location [MapId=" + mapId + ", X=" + x + ", Y=" + y + "]";
	}
}
