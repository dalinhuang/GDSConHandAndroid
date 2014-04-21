package com.winjune.wifiindoor.poi;

import java.io.Serializable;

public class SearchFieldInfo implements Serializable {
	private static final long serialVersionUID = -4968185882242111254L;
	
	private String searchText;
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
	
	public String getText() {
		return searchText;
	}

	public void setText(String txt) {
		this.searchText = txt;
	}

}
