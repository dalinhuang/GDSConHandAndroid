package com.ericsson.cgc.aurora.wifiindoor.types;

public class TestLocateCollectRequest {
	private Location location;
	private WifiFingerPrint fignerPrint;
	private long timestamp;

	public WifiFingerPrint getFignerPrint() {
		return fignerPrint;
	}

	public Location getLocation() {
		return location;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setFignerPrint(WifiFingerPrint fignerPrint) {
		this.fignerPrint = fignerPrint;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
