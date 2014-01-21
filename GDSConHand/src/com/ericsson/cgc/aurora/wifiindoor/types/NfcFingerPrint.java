package com.ericsson.cgc.aurora.wifiindoor.types;

import com.ericsson.cgc.aurora.wifiindoor.util.Util;

public class NfcFingerPrint {
	private String accountName;
	private String myWifiMac;
	private String deviceName;
	private String tagId;
	
	public NfcFingerPrint(String tagId) {
		setAccountName(Util.getAccountName());
		setTagId(tagId);
		setMyWifiMac(Util.getWifiInfoManager().getMyMac());
		setDeviceName(Util.getDeviceName());
	}

	public String getAccountName() {
		return accountName;
	}

	public String getDeviceName() {
		return deviceName;
	}
	
	public String getMyWifiMac() {
		return myWifiMac;
	}

	public String getTagId() {
		return tagId;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public void setMyWifiMac(String myWifiMac) {
		this.myWifiMac = myWifiMac;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
}
