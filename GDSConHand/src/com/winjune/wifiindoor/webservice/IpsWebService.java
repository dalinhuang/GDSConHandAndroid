/**
 * @(#)WipsWebService.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.winjune.common.webservice.core.error.WebCredentialsException;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebException;
import com.winjune.common.webservice.core.transport.OutgoingMessageQueue;
import com.winjune.common.webservice.core.types.Test;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.ads.AdGroup;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.WifiIpsSettings;
import com.winjune.wifiindoor.version.ApkVersionManager;
import com.winjune.wifiindoor.webservice.http.IpsHttpApi;
import com.winjune.wifiindoor.webservice.transport.IpsMessageHandler;
import com.winjune.wifiindoor.webservice.types.ApkVersionReply;
import com.winjune.wifiindoor.webservice.types.BuildingManagerReply;
import com.winjune.wifiindoor.webservice.types.IndoorMapReply;
import com.winjune.wifiindoor.webservice.types.InterestPlacesInfoReply;
import com.winjune.wifiindoor.webservice.types.Location;
import com.winjune.wifiindoor.webservice.types.LocationSet;
import com.winjune.wifiindoor.webservice.types.MapInfoReply;
import com.winjune.wifiindoor.webservice.types.MapManagerReply;
import com.winjune.wifiindoor.webservice.types.NaviInfoReply;
import com.winjune.wifiindoor.webservice.types.QueryInfo;
import com.winjune.wifiindoor.webservice.types.TestLocateCollectReply;

/**
 * @author ezhipin
 * 
 */
public class IpsWebService {
	private static IpsWebService mSingletonInstance = null;
	private static boolean httpConnectionEstablished = false;
	private static boolean serverReachable = false;
	
	private IpsHttpApi mWifiIpsHttpApi;

	private IpsWebService() {

	}

	public static IpsWebService getInstance() {
		if (mSingletonInstance == null) {
			
			mSingletonInstance = new IpsWebService();			
						
		}

		return mSingletonInstance;
	}
	
	public static void setActivity(Activity activity) {
		
		IpsMessageHandler.setActivity(activity);
	}	
	
	public static void startTransportServiceThread() {
		IpsMessageHandler.startTransportServiceThread();
	}

	public static void connetcToServer(final Activity activity) {
		int counter = 0;
		 
		while (!WifiIpsSettings.getServerAddress(true)) 
	    { 
	    	if (counter >= 5) { // Max wait for 30 seconds for the network
	    		Util.showLongToast(activity, R.string.wrong_server);
	    		return;
	    	}
			
			counter++;
			try {
				Thread.sleep(500);  // wait 500ms
			} catch (InterruptedException e) {
				continue;
			}
	    }
	    	
		loadWebService();
		
		
		// Start the Ips Message Handler Thread if it has not been started yet.
		IpsMessageHandler.startTransportServiceThread();
		
		setHttpConnectionEstablished(true);
		
		//Hoare: bypass ping check since it doesn't work in some mobiles
		setServerReachable(true);
		//setServerReachable(WifiIpsSettings.isPingable());
				
		return;
	}
	
	public static boolean sendToServer(Activity activity, int requestCode, JSONObject data) {
		
		Log.e("Request", "Code: " + requestCode + ", Data" + data.toString());
		
		if (!Util.getNetworkInfoManager().isConnected()) {
			Util.showLongToast(activity, R.string.no_data_connection);
			return false;
		}		
		
		if (!isServerReachable()) {
			Util.showLongToast(activity, R.string.server_unreachable);
			return false;
		}
		
		if (!isHttpConnectionEstablished()) {
			Util.showLongToast(activity, R.string.no_http_connection);
			return false;
		}
				
		JSONObject json = new JSONObject();
		try {
			json.put("RequestCode", requestCode);
			json.put("RequestPayload", data);
			if (isHttpConnectionEstablished()) {
				OutgoingMessageQueue.offer(json);
			} else {
				final JSONObject json1 = json;
				
				// Use a Thread to wait for 1 more minute if the HTTP connection is not ready
				new Thread() {
					public void run() {
						for (int counter=0;counter<60;counter++) { // Run 60 * 1 s
							if (isHttpConnectionEstablished()) {
								OutgoingMessageQueue.offer(json1);
								break;
							}
							
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}.start();
			}

			return true;
		} catch (JSONException e) {
			Util.showToast(activity, "031 " + e.toString(), Toast.LENGTH_LONG);
		}
		

		return false;
	}
	
	public static boolean isServerReachable() {
		return serverReachable;
	}

	public static void setServerReachable(boolean serverReachable) {
		IpsWebService.serverReachable = serverReachable;
	}			
	
	public static boolean isHttpConnectionEstablished() {
		return httpConnectionEstablished;
	}

	public static void setHttpConnectionEstablished(
			boolean httpConnectionEstablished) {
		IpsWebService.httpConnectionEstablished = httpConnectionEstablished;
	}	
	
	private static void loadWebService() {
		IpsWebService instance = IpsWebService.getInstance();

		if (instance == null) {
			return;
		}

		instance.initialize(WifiIpsSettings.SERVER, ApkVersionManager.getApkVersionName());
	}	
	
	public boolean initialize(String domain, String clientVersion) {
		mWifiIpsHttpApi = new IpsHttpApi(domain, clientVersion);

		return true;
	}
	
	public boolean reInitialize(String domain, String clientVersion) {
		mWifiIpsHttpApi = null;
		return initialize(domain, clientVersion);
	}
	
	/*public void setProxy(){
		WebProxy myProxy = new WebProxy("192.168.0.3:8080",true);
		//myProxy.Credentials = new NetworkCredential("username","password","domainname");
		myService.Proxy = myProxy;

	}*/

	/*
	 * 
	 */
	@V1
	public Test testPostXml(String parameter) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.testPostXml(parameter);
	}

	/*
	 * 
	 */
	@V1
	public Test testPostJson(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.testPostJson(json);
	}

	/*
	 * collect, no response
	 */
	@V1
	public LocationSet locate(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.locate(json);
	}

	/*
	 * locate
	 */
	@V1
	public void collect(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		mWifiIpsHttpApi.collect(json);
	}
	
	/*
	 * query
	 */
	@V1
	public QueryInfo query(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.query(json);
	}
	
	/*
	 * collectNfc
	 */
	@V1
	public void collectNfc(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		mWifiIpsHttpApi.collectNfc(json);
	}

	/*
	 * locateBaseOnNfc
	 */
	@V1
	public Location locateBaseOnNfc(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.locateBaseOnNfc(json);
	}
	
	/*
	 * deleteFingerprint, no response
	 */
	@V1
	public void deleteFingerprint(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		mWifiIpsHttpApi.deleteFingerprint(json);
	}
	
	/*
	 * locate_test
	 */
	@V1
	public TestLocateCollectReply locateTest(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.locateTest(json);
	}
	
	/*
	 * collect_test
	 */
	@V1
	public TestLocateCollectReply collectTest(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.collectTest(json);
	}
	
	/*
	 * update Apk
	 */
	@V1
	public ApkVersionReply queryApkVersion(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryApkVersion(json);
	}
	
	/*
	 * update Buildings
	 */
	@V1
	public BuildingManagerReply queryBuilding(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryBuilding(json);
	}
	
	/*
	 * update mapList
	 */
	@V1
	public MapManagerReply queryMaps(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryMaps(json);
	}
	
	/*
	 * update Map
	 */
	@V1
	public IndoorMapReply queryMap(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryMap(json);
	}
	
	/*
	 * update Map Info
	 */
	@V1
	public MapInfoReply queryMapInfo(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryMapInfo(json);
	}
	
	/*
	 * update Navi Info
	 */
	@V1
	public NaviInfoReply queryNaviInfo(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryNaviInfo(json);
	}
	
	
	/*
	 * update advertise Info
	 */
	@V1
	public AdGroup queryAdvertiseInfo(JSONObject json) throws WebException,
	        WebCredentialsException, WebError, IOException {

        if (mWifiIpsHttpApi == null) {			
	        throw new WebException("IPS HTTP API is null!");			
        }

        return mWifiIpsHttpApi.queryAdvertiseInfo(json);
    }
	
	/*
	 * update Interest Places Info
	 */
	@V1
	public InterestPlacesInfoReply queryInterestPlacesInfo(JSONObject json)  throws WebException,
    		WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
	        throw new WebException("IPS HTTP API is null!");			
        }

        return mWifiIpsHttpApi.queryInterestPlacesInfo(json);
	}
	
	/**
	 * This api is supported in the V1 API
	 */
	@interface V1 {
	}

}
