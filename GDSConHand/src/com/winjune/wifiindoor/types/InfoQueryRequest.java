package com.winjune.wifiindoor.types;

import java.util.ArrayList;

public class InfoQueryRequest {
	ArrayList<Location> locations;
	
	/**
	 * @return the location
	 */
	public ArrayList<Location> getLocations() {
		return locations;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}
}
