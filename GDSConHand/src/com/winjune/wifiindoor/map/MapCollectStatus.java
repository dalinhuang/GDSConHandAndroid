package com.winjune.wifiindoor.map;

import java.io.Serializable;
import java.util.ArrayList;

public class MapCollectStatus implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6037011358526642457L;

	private int mapId;
	private int versionCode;
	private ArrayList<CellCollectStatus> cells;
	
	public ArrayList<CellCollectStatus> getCells() {
		return cells;
	}

	public void setCells(ArrayList<CellCollectStatus> cells) {
		this.cells = cells;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int id) {
		this.mapId = id;
	}
}
