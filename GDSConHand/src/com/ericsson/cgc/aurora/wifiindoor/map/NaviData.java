package com.ericsson.cgc.aurora.wifiindoor.map;

import java.io.Serializable;
public class NaviData implements Serializable {
	private static final long serialVersionUID = -3834597256377907103L;
	
	private int from;
	private int to;
	private float distance;
	private String info;
	
	public float getDistance() {
		return distance;
	}
	
	public int getFrom() {
		return from;
	}

	public String getInfo() {
		return info;
	}

	public int getTo() {
		return to;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setTo(int to) {
		this.to = to;
	}
}
