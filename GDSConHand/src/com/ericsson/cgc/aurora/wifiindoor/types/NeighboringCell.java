/**
 * @(#)NeighboringCell.java
 * Jun 4, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.types;

/**
 * @author ezhipin
 * 
 */
public class NeighboringCell {
	private int lac;
	private int cid;
	private int psc;
	private int rssi;
	private int networkType;

	/**
	 * @return the cid
	 */
	public int getCid() {
		return cid;
	}

	/**
	 * @return the lac
	 */
	public int getLac() {
		return lac;
	}

	/**
	 * @return the networkType
	 */
	public int getNetworkType() {
		return networkType;
	}

	/**
	 * @return the psc
	 */
	public int getPsc() {
		return psc;
	}

	/**
	 * @return the rssi
	 */
	public int getRssi() {
		return rssi;
	}

	/**
	 * @param cid
	 *            the cid to set
	 */
	public void setCid(int cid) {
		this.cid = cid;
	}

	/**
	 * @param lac
	 *            the lac to set
	 */
	public void setLac(int lac) {
		this.lac = lac;
	}

	/**
	 * @param networkType
	 *            the networkType to set
	 */
	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	/**
	 * @param psc
	 *            the psc to set
	 */
	public void setPsc(int psc) {
		this.psc = psc;
	}

	/**
	 * @param rssi
	 *            the rssi to set
	 */
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

}
