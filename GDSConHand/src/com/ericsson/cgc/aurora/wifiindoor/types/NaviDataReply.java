package com.ericsson.cgc.aurora.wifiindoor.types;

import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

public class NaviDataReply implements IType {
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
