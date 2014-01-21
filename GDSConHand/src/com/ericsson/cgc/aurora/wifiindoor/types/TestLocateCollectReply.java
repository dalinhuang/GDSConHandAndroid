package com.ericsson.cgc.aurora.wifiindoor.types;

import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

public class TestLocateCollectReply implements IType {
	private Location origLocation;
	private LocationSet locations;
	private long timestamp1; // App Send
	private long timestamp2; // Server Receive
	private long timestamp3; // Server Send
	private int reTest; // re-test indicator
	
	public TestLocateCollectReply() {
		
	}
	
	public TestLocateCollectReply(Location origLocation, LocationSet locations, long timestamp1, long timestamp2, long timestamp3, int reTest) {
		setOrigLocation(origLocation);
		setLocations(locations);
		setTimestamp1(timestamp1);
		setTimestamp2(timestamp2);
		setTimestamp3(timestamp3);
		setReTest(reTest);
	}

	public LocationSet getLocations() {
		return locations;
	}


	public Location getOrigLocation() {
		return origLocation;
	}


	public int getReTest() {
		return reTest;
	}


	public long getTimestamp1() {
		return timestamp1;
	}


	public long getTimestamp2() {
		return timestamp2;
	}


	public long getTimestamp3() {
		return timestamp3;
	}	

	public void setLocations(LocationSet locations) {
		this.locations = locations;
	}

	public void setOrigLocation(Location origLocation) {
		this.origLocation = origLocation;
	}

	public void setReTest(int reTest) {
		this.reTest = reTest;
	}

	public void setTimestamp1(long timestamp1) {
		this.timestamp1 = timestamp1;
	}

	public void setTimestamp2(long timestamp2) {
		this.timestamp2 = timestamp2;
	}

	public void setTimestamp3(long timestamp3) {
		this.timestamp3 = timestamp3;
	}
}
