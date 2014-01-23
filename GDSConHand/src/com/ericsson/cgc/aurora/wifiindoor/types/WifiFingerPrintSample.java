package com.ericsson.cgc.aurora.wifiindoor.types;

import java.util.ArrayList;

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
