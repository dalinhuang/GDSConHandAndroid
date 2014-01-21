/**
 * @(#)CollectInfo.java
 * Jun 4, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.types;


/**
 * @author ezhipin
 * 
 */
public class CollectInfo {
	private Location location;
	private WifiFingerPrint wifiFingerPrint;

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the wifiFingerPrint
	 */
	public WifiFingerPrint getWifiFingerPrint() {
		return wifiFingerPrint;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * @param wifiFingerPrint
	 *            the wifiFingerPrint to set
	 */
	public void setWifiFingerPrint(WifiFingerPrint wifiFingerPrint) {
		this.wifiFingerPrint = wifiFingerPrint;
	}

}
