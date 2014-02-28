package com.winjune.wifiindoor.navi;

import java.io.Serializable;

public class NaviNode implements Serializable {
	private static final long serialVersionUID = 6013104644590875612L;
	
	private int id;
	private int nameId;
	private int mapId;
	private int x;
	private int y;
	private String name;	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getNameId() {
		return nameId;
	}

	public void setNameId(int id) {
		this.nameId = id;
	}	
		
	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
