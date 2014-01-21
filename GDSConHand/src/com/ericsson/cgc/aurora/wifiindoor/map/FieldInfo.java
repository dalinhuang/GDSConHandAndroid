package com.ericsson.cgc.aurora.wifiindoor.map;

import java.io.Serializable;

public class FieldInfo implements Serializable {
	private static final long serialVersionUID = -4968185882242111253L;
	
	private int x;
	private int y;
	private String info;
	private float scale;
	private float alpha;
	private float rotation;
	
	public float getAlpha() {
		return alpha;
	}
	
	public String getInfo() {
		return info;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

}