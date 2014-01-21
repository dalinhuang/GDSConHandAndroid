package com.ericsson.cgc.aurora.wifiindoor.types;

import java.util.ArrayList;

public class LocationQueryInfo {
	private Location mLocation;
	private ArrayList<String> mMessages;

	public LocationQueryInfo(Location location, ArrayList<String> messages) {
		mLocation = location;
		mMessages = messages;
	}

	public Location getLocation() {
		return mLocation;
	}

	public ArrayList<String> getMessages() {
		return mMessages;
	}

	public void setLocation(Location location) {
		this.mLocation = location;
	}

	public void setMessages(ArrayList<String> messages) {
		this.mMessages = messages;
	}

}
