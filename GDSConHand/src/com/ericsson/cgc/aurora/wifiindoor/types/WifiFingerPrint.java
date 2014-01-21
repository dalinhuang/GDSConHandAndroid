/**
 * @(#)WifiFingerPrint.java
 * Jun 4, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.types;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ericsson.cgc.aurora.wifiindoor.network.WifiInfoManager;
import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;

/**
 * @author ezhipin, haleyshi
 * 
 */
public class WifiFingerPrint {
	private String accountName;
	private String myWifiMac;
	private String deviceName;
	private ArrayList<WifiFingerPrintSample> samples;

	public WifiFingerPrint(ArrayList<WifiFingerPrintSample> samples) {
		setAccountName(Util.getAccountName());
		setMyWifiMac(Util.getWifiInfoManager().getMyMac());
		setDeviceName(Util.getDeviceName());
		
		setSamples(samples);
	}
	
	public WifiFingerPrint(int req) {
		setAccountName(Util.getAccountName());
		setMyWifiMac(Util.getWifiInfoManager().getMyMac());
		setDeviceName(Util.getDeviceName());
		
		captureSamples(Util.getWifiInfoManager(), req);
	}

	private void captureSamples(WifiInfoManager wifiInfoManager, int req) {
		// Capture multiple samples
		int times = 1;
		long timeout = 1000;

		if (req == IndoorMapData.REQUEST_LOCATE) {
			times = IndoorMapData.MAX_FINGERPRINTS_FOR_LOCATE;
			timeout = IndoorMapData.MAX_WAIT_MS_FOR_LOCATE;
		} else {
			if (req == IndoorMapData.REQUEST_COLLECT) {
				times = IndoorMapData.MAX_FINGERPRINTS_FOR_COLLECT;
				timeout = IndoorMapData.MAX_WAIT_MS_FOR_COLLECT;
			}
		}
		
		setSamples(wifiInfoManager.getWifiFingerPrintList(times, timeout));
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

	public ArrayList<WifiFingerPrintSample> getSamples() {
		return samples;
	}

	@SuppressLint("SimpleDateFormat")
	public void log() {
		if (samples == null) {
			Log.e("Fingerprint", "null");
			return;
		}
		
		if (samples.isEmpty()) {
			Log.e("Fingerprint", "No Sample");
			return;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		Log.e("Fingerprint", this.toString() + "@" + sdf.format(new Date(System.currentTimeMillis())));
		
		for (WifiFingerPrintSample sample:samples) {
			if (sample.getNeighboringAPs() == null) {
				Log.e("--Sample", "null");
				continue;
			}
			
			if (sample.getNeighboringAPs().isEmpty()) {
				Log.e("--Sample", "No APs");
				continue;
			}
		
			Log.e("--Sample", sample.toString() + "@" + sdf.format(new Date(sample.getBirthday())));
			
			for (MyWifiInfo ap:sample.getNeighboringAPs()) {
				Log.e("----AP", ap.getMac()+","+ap.getDbm());
			}			
		}
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
	
	public void setSamples(ArrayList<WifiFingerPrintSample> samples) {
		this.samples = samples;
	}
}
