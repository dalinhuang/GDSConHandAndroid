package com.winjune.wifiindoor.webservice.types;

import com.winjune.wifiindoor.util.Util;

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

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	
	public String getMyWifiMac() {
		return myWifiMac;
	}

	public void setMyWifiMac(String myWifiMac) {
		this.myWifiMac = myWifiMac;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
}
