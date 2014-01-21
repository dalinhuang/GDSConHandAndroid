package com.ericsson.cgc.aurora.wifiindoor.map;

import java.io.Serializable;

public class NaviNode implements Serializable {
	private static final long serialVersionUID = 6013104644590875612L;
	
	private int id;
	private int mapId;
	private int x;
	private int y;
	private String name;
	
	public int getId() {
		return id;
	}

	public int getMapId() {
		return mapId;
	}
	
	public String getName() {
		return name;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
