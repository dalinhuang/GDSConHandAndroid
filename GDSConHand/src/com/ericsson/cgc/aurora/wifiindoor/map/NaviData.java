package com.ericsson.cgc.aurora.wifiindoor.map;

import java.io.Serializable;
public class NaviData implements Serializable {
	private static final long serialVersionUID = -3834597256377907103L;
	
	private int from;
	private int to;
	private float distance;
	private String info;
	
	public int getFrom() {
		return from;
	}
	
	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
