package com.ericsson.cgc.aurora.wifiindoor.types;

import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

public class NaviDataReply implements IType {
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
