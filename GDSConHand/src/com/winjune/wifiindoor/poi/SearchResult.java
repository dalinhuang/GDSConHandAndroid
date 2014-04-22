package com.winjune.wifiindoor.poi;

import java.io.Serializable;

public class SearchResult implements Serializable {
	private static final long serialVersionUID = -4968185882242111254L;
	
	private int x;
	private int y;

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
}
