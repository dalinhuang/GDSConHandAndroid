package com.winjune.wifiindoor.wifi;

import java.util.ArrayList;

import com.winjune.wifiindoor.webservice.types.MyWifiInfo;

public class WifiFingerPrintSample {
	private ArrayList<MyWifiInfo> neighboringAPs;
	private long birthday;
	
	public WifiFingerPrintSample(
			ArrayList<MyWifiInfo> neighborAps,
			long birthday) {
		setNeighboringAPs(neighborAps);
		setBirthday(birthday);
	}

	public ArrayList<MyWifiInfo> getNeighboringAPs() {
		return neighboringAPs;
	}
	
	public void setNeighboringAPs(ArrayList<MyWifiInfo> neighboringAPs) {
		this.neighboringAPs = neighboringAPs;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

}
