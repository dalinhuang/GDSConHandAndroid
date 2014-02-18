/**
 * @(#)NeighboringCell.java
 * Jun 4, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.types;

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
	 * @return the lac
	 */
	public int getLac() {
		return lac;
	}

	/**
	 * @param lac
	 *            the lac to set
	 */
	public void setLac(int lac) {
		this.lac = lac;
	}

	/**
	 * @return the cid
	 */
	public int getCid() {
		return cid;
	}

	/**
	 * @param cid
	 *            the cid to set
	 */
	public void setCid(int cid) {
		this.cid = cid;
	}

	/**
	 * @return the psc
	 */
	public int getPsc() {
		return psc;
	}

	/**
	 * @param psc
	 *            the psc to set
	 */
	public void setPsc(int psc) {
		this.psc = psc;
	}

	/**
	 * @return the rssi
	 */
	public int getRssi() {
		return rssi;
	}

	/**
	 * @param rssi
	 *            the rssi to set
	 */
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	/**
	 * @return the networkType
	 */
	public int getNetworkType() {
		return networkType;
	}

	/**
	 * @param networkType
	 *            the networkType to set
	 */
	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

}
