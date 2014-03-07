package com.winjune.wifiindoor.webservice.types;

import com.winjune.common.webservice.core.types.IType;

public class FieldInfoReply implements IType {
	private int x;
	private int y;
	private String info;
	private float scale;
	private float alpha;
	private float rotation;
	private float minZoomFactor;
	private float maxZoomFactor;
	
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
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
