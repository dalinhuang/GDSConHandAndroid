package com.ericsson.cgc.aurora.wifiindoor.types;

import android.net.wifi.ScanResult;

public class MyWifiInfo {
	private String mac;
	private int dbm;

	public MyWifiInfo(ScanResult scanresult) {
		if (scanresult == null){
			setMac("");
			dbm = 0;
		} else {
			if (scanresult.BSSID == null){
				setMac("");
			} else {
				setMac(scanresult.BSSID);
			}
			
			dbm = scanresult.level;
		}
	}
	
	public MyWifiInfo(String s, int i) {	
		if (s == null){
			setMac("");
		} else {
			setMac(s);
		}
		
		dbm = i;
	}

	public int getDbm() {
		return dbm;
	}

	public String getMac() {
		return mac;
	}
	
	public void setDbm(int dbm) {
		this.dbm = dbm;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
}
