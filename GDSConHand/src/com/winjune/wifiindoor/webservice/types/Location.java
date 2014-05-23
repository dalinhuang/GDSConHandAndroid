/**
 * @(#)Test.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.types;

import com.winjune.common.webservice.core.types.IType;

/**
 * @author ezhipin, haleyshi
 * 
 */
public class Location implements IType {
	private int mapId;
	private int x;
	private int y;
	
	public Location(int mapId, int x, int y) {
		this.mapId = mapId;
		this.x = x;
		this.y = y;
	}
	
	public Location() {
		
	}
	
	/**
	 * @return the mapId
	 */
	public int getMapId() {
		return mapId;
	}
	/**
	 * @param mapId the mapId to set
	 */
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
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
