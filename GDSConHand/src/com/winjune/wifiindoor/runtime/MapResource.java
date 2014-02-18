package com.winjune.wifiindoor.runtime;

import java.io.Serializable;

public class MapResource implements Serializable {
	private static final long serialVersionUID = -8419515552600629444L;
	
	private int left;
	private int top;
	private int width;
	private int height;
	private String name;
	
	public int getLeft() {
		return left;
	}
	
	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
