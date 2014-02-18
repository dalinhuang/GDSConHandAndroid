package com.winjune.wifiindoor.types;

public class TestLocateCollectRequest {
	private Location location;
	private WifiFingerPrint fignerPrint;
	private long timestamp;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public WifiFingerPrint getFignerPrint() {
		return fignerPrint;
	}

	public void setFignerPrint(WifiFingerPrint fignerPrint) {
		this.fignerPrint = fignerPrint;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
