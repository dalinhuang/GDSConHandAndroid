package com.ericsson.cgc.aurora.wifiindoor.network;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.ericsson.cgc.aurora.wifiindoor.types.MyWifiInfo;
import com.ericsson.cgc.aurora.wifiindoor.types.WifiFingerPrint;
import com.ericsson.cgc.aurora.wifiindoor.types.WifiFingerPrintSample;
import com.ericsson.cgc.aurora.wifiindoor.util.CoolDown;
import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;

public class WifiInfoManager {
	private WifiManager wifiManager;
	private long previous = 0;
	private long previous_previous = 0;
	private boolean quitFromSignalChanged = false;
	private boolean refreshOnGoing = false;
	
	private ArrayList<WifiFingerPrintSample> savedSamples;
	
	public WifiInfoManager(Context paramContext){
		this.wifiManager = (WifiManager) paramContext.getSystemService(Context.WIFI_SERVICE);
	}
	
	public ArrayList<MyWifiInfo> dump() {
		if (!isWifiEnabled()) {
			return null;
		}
		
		ArrayList<MyWifiInfo> lsAllWIFI = new ArrayList<MyWifiInfo>();		
		
		List<ScanResult> lsScanResult = this.wifiManager.getScanResults();
		
		// Store the AP from Scan result but not connected one since there are not the same in one scan.
		if (lsScanResult!=null){		
			for (ScanResult result : lsScanResult) {
				MyWifiInfo scanWIFI = new MyWifiInfo(result);				
				lsAllWIFI.add(scanWIFI);
			}
		}
		
		return lsAllWIFI;
	}
	
	private ArrayList<MyWifiInfo> filterAPs(ArrayList<MyWifiInfo> neighborApsSample) {
		// Filter the result
		ArrayList<MyWifiInfo> neighborApsOfLeftSample = new ArrayList<MyWifiInfo>();
		ArrayList<MyWifiInfo> neighborApsOfFilteredSample = new ArrayList<MyWifiInfo>();
		
		for (MyWifiInfo wifiInfo : neighborApsSample) {
			boolean validSignal = true;
			 int dbm = wifiInfo.getDbm();
				
			// Set if one of the sample for this AP has weak/strong signal
			if  ( (dbm < IndoorMapData.MIN_DBM_COUNT_IN) || (dbm > IndoorMapData.MAX_DBM_COUNT_IN) ) {
				validSignal = false;
			}
			
			// Only copy the valid signal that not too strong or too weak
			if (validSignal) {
				neighborApsOfLeftSample.add(wifiInfo);				
			} else {
				neighborApsOfFilteredSample.add(wifiInfo);
			}
		}
		
		// Add more APs if needed
		int currentSampleSize = neighborApsOfLeftSample.size();
		for (int i=currentSampleSize; i<IndoorMapData.MIN_AP_COUNT_IN; i++) {
			// Add 1 best sample that marked as weak signal
			if (neighborApsOfFilteredSample.size() == 0){
				//Log.e("Wifi Sample", "Only "+currentSampleSize+" samples!");
				break;
			}
			
			int idx = getBestSample(neighborApsOfFilteredSample);
			
			// Sanity check
			if ((idx <0) || (idx >= neighborApsOfFilteredSample.size())) {
				break;
			}
			
			neighborApsOfLeftSample.add(neighborApsOfFilteredSample.get(idx));
			neighborApsOfFilteredSample.remove(idx);
		}
		
		return neighborApsOfLeftSample;
	}
	
	private int getBestSample(ArrayList<MyWifiInfo> sample) {
		int best1 = -1;
		int best2 = -1;
		int bestDbm1 = -200;
		int bestDbm2 = 0;
		
		if (sample == null) {
			return -1;
		}
		
		for (MyWifiInfo wifi : sample) {
			int dbm = wifi.getDbm();
			
			if ( dbm > IndoorMapData.MIN_DBM_COUNT_IN) {
				// Update the best strong
				if (dbm < bestDbm2) {
					bestDbm2 = dbm;
					best2 = sample.indexOf(wifi);
				}
			} else {
				// Update the best weak
				if (dbm > bestDbm1) {
					bestDbm1 = dbm;
					best1 = sample.indexOf(wifi);
				}
			}		
		}
		
		if (bestDbm1 == -200) {
			return best2;
		}
		
		if (bestDbm2 == 0) {
			return best1;
		}
		
		if ((bestDbm1 - IndoorMapData.MIN_DBM_COUNT_IN) > (IndoorMapData.MAX_DBM_COUNT_IN - bestDbm2) ) {
			return best2;
		}
		
		return best1;
	}

	public String getMyMac(){
		if (wifiManager != null){
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			if (wifiInfo != null) {
				String mac = wifiInfo.getMacAddress();
				if ( mac != null){
					return mac;
				}
			}
		}

		return "";
	}
	
	private ArrayList<MyWifiInfo> getWifiApListOneSample() {		
		refresh();
		
		if (!quitFromSignalChanged){ // Impossible
			return null;
		}
		
		return dump();
	}
	
	private ArrayList<MyWifiInfo> getWifiApListOneSample(CoolDown coolDown) {		
		refresh(coolDown);
		
		if (!quitFromSignalChanged){ // Max timer timeout
			return null;
		}
		
		return dump();
	}
	
	
	private ArrayList<MyWifiInfo> getWifiApListOneSampleWithoutRefresh() {		
		return dump();
	}
	
	public ArrayList<WifiFingerPrintSample> getWifiFingerPrintList(int times, long timeout) {
		if (!isWifiEnabled()) {
			return null;  
		}
		
		// Init wait conditions
		CoolDown coolDown = new CoolDown(timeout);		
		coolDown.checkValidity();		
		
		ArrayList<WifiFingerPrintSample> wifiFingerPrintList = new ArrayList<WifiFingerPrintSample>();
		
		for (int i = 0; i < times; i++) {
			// Capture data again
			ArrayList<MyWifiInfo> neighborApsOfNthSample = getWifiApListOneSample(coolDown);

			if (neighborApsOfNthSample != null) {
				WifiFingerPrintSample wifiFingerPrintSample = new WifiFingerPrintSample(filterAPs(neighborApsOfNthSample), System.currentTimeMillis());
				wifiFingerPrintList.add(wifiFingerPrintSample);
			}	
			
			if (!quitFromSignalChanged){ // Max timer timeout
				break;
			}
		}
		
		if (wifiFingerPrintList.isEmpty()) {
			Log.e("Wifi Sample", "null, capture 1 sample anyway");
			WifiFingerPrintSample wifiFingerPrintSample = getWifiFingerPrintOneSampleWithoutRefresh();	
			
			if (wifiFingerPrintSample == null) {
				Log.e("Wifi Sample", "still null");
				return null;
			}
			
			wifiFingerPrintList.add(wifiFingerPrintSample);
		} 

		return wifiFingerPrintList;
	}
	
	public WifiFingerPrintSample getWifiFingerPrintOneSample() {
		if (!isWifiEnabled()) {
			return null;  
		}
		
		ArrayList<MyWifiInfo> neighborApsOfFirstSample = getWifiApListOneSample();		
		
		if (neighborApsOfFirstSample != null) {
			return new WifiFingerPrintSample(filterAPs(neighborApsOfFirstSample), System.currentTimeMillis());
		}		

		return null;
	}
	
	public WifiFingerPrintSample getWifiFingerPrintOneSampleWithoutRefresh() {
		if (!isWifiEnabled()) {
			return null;  
		}
		
		ArrayList<MyWifiInfo> neighborApsOfFirstSample = getWifiApListOneSampleWithoutRefresh();		
		
		if (neighborApsOfFirstSample != null) {
			return new WifiFingerPrintSample(filterAPs(neighborApsOfFirstSample), System.currentTimeMillis());
		}		

		return null;
	}
	
	public boolean hasEnoughSavedSamples(){
		if (savedSamples == null) {
			return false;
		}
		
		// Not edit anything in savedSamples to avoid the concurrent write issue
		
		long currentTime = System.currentTimeMillis();
		int num = 0;
		
		for (WifiFingerPrintSample sample : savedSamples) {
			if ((currentTime - sample.getBirthday()) > IndoorMapData.MAX_AGE_FOR_WIFI_SAMPLES) {
				continue;
			}
			
			num++;
		}
		
		return (num >= IndoorMapData.MIN_WIFI_SAMPLES_BUFFERED);
	}
	
	public boolean hasFreshSavedSamples(){
		if (savedSamples == null) {
			return false;
		}
		
		// Not edit anything in savedSamples to avoid the concurrent write issue
		
		long currentTime = System.currentTimeMillis();
		int num = 0;
		
		for (WifiFingerPrintSample sample : savedSamples) {
			if ((currentTime - sample.getBirthday()) > IndoorMapData.MAX_AGE_FOR_WIFI_SAMPLES) {
				continue;
			}
			
			num++;
		}
		
		return (num != 0);
	}
	
	public boolean hasSavedSamples(){
		if (savedSamples == null) {
			return false;
		}
		
		return (savedSamples.size() != 0);
	}
	
	public boolean isConnectedTo(android.net.wifi.WifiInfo wifiConnection){
		if (wifiManager==null){
			return false;
		}
		
		if (!isWifiEnabled()) {
			return false;
		}
		
		if (wifiConnection.getNetworkId()==-1){
			return false;
		}
		
		return true;
	}
	
	private boolean isSignalChanged() {
		previous = 0;		
		List<ScanResult> lsScanResult = wifiManager.getScanResults();
		// previous dbm sum
		if (lsScanResult!=null){	
			for (ScanResult result : lsScanResult) {
				previous += (result.level * result.level);
			}
		}
	
		quitFromSignalChanged = (previous_previous != previous);
		
		return quitFromSignalChanged;
	}

	public boolean isWifiEmbeded(){
		return (wifiManager != null);
	}
	
	public boolean isWifiEnabled(){
		if (wifiManager==null){
			return false;
		}
		
		if (!isWifiEmbeded()) {
			return false;
		}
		
		if (wifiManager.isWifiEnabled()){
			return true;
		} else {
			// Try to enable the WIFI if it is not enabled
			return wifiManager.setWifiEnabled(true);
		}
	}
	
	public WifiFingerPrint mergeSamples() {
		if (savedSamples == null) {
			return null;
		}
		
		if (savedSamples.size() == 0) {
			return null;
		}
		
		// WARNING: Not edit anything in savedSamples to avoid the concurrent write issue
		
		ArrayList<WifiFingerPrintSample> samples = new ArrayList<WifiFingerPrintSample>();
		long currentTime = System.currentTimeMillis();
		
		
		for (WifiFingerPrintSample sample : savedSamples) {
			if ((currentTime - sample.getBirthday()) > IndoorMapData.MAX_AGE_FOR_WIFI_SAMPLES) {
				continue;
			}			
			
			samples.add(sample);
		}
		
		
		if (samples.isEmpty()) {
			Log.e("Wifi Sample", "null, capture 1 sample anyway");
			WifiFingerPrintSample wifiFingerPrintSample = getWifiFingerPrintOneSampleWithoutRefresh();	
			
			if (wifiFingerPrintSample == null) {
				Log.e("Wifi Sample", "still null");
				return null;
			}
			
			samples.add(wifiFingerPrintSample);
		} 
		
		return new WifiFingerPrint(samples);
	}
	
	// Refresh the WIFI AP data, this should be invoked every time before get the current data.
	public void refresh(){
		while (refreshOnGoing) {
			
		}		
		refreshOnGoing = true;
		
		quitFromSignalChanged = false; // No more retry in the invoker

		previous_previous = 0;		
		List<ScanResult> lsScanResult = wifiManager.getScanResults();		
		// previous dbm sum
		if (lsScanResult!=null){		
			for (ScanResult result : lsScanResult) {
				previous_previous += (result.level * result.level);
			}
		}
		
		wifiManager.startScan();
		
		// Wait for Scan over (get new data)
		while (!isSignalChanged()) {
			// Do nothing
		}
		
		refreshOnGoing = false;
	}
	
	// Refresh the WIFI AP data, this should be invoked every time before get the current data.
	public void refresh(CoolDown coolDown){
		
		while (refreshOnGoing) {
			
		}		
		refreshOnGoing = true;
		
		quitFromSignalChanged = false; // No more retry in the invoker

		previous_previous = 0;		
		List<ScanResult> lsScanResult = wifiManager.getScanResults();		
		// previous dbm sum
		if (lsScanResult!=null){		
			for (ScanResult result : lsScanResult) {
				previous_previous += (result.level * result.level);
			}
		}
		
		wifiManager.startScan();
		
		// Wait for Scan over (get new data or timeout)
		while (!coolDown.checkValidity() && !isSignalChanged()) {
			// Do nothing
		}
		
		refreshOnGoing = false;
	}
	
	public void saveWifiInBackground() {
		if (!isWifiEnabled()) {
			savedSamples = null; // Empty the saved Fingerprints
			return;  
		}	
		
		if (!IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR && !IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER) {
			// Just Scan
			wifiManager.startScan();
			return;
		}
		
		// Store a sample
		WifiFingerPrintSample oneSample = getWifiFingerPrintOneSample();
		
		if (oneSample != null) {
			storeCurrentSample(oneSample);
		}
		
		return;
	}

	private void storeCurrentSample(WifiFingerPrintSample fingerPrint) {
		if (fingerPrint == null) {
			return;
		}
		
		if (savedSamples == null){
			savedSamples = new ArrayList<WifiFingerPrintSample>();			
		} 
		
		if (savedSamples.size() >= IndoorMapData.MAX_WIFI_SAMPLES_BUFFERED) {
			savedSamples.remove(0); // Override the oldest one			
		}
		
		savedSamples.add(fingerPrint);
	}

	public WifiManager wifiManager(){
		return this.wifiManager;
	}
}
