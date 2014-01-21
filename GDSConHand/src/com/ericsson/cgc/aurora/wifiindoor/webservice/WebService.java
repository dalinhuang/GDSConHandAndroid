/**
 * @(#)WipsWebService.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice;

import java.io.IOException;

import org.json.JSONObject;

import com.ericsson.cgc.aurora.wifiindoor.ads.AdGroup;
import com.ericsson.cgc.aurora.wifiindoor.types.ApkVersionReply;
import com.ericsson.cgc.aurora.wifiindoor.types.BuildingManagerReply;
import com.ericsson.cgc.aurora.wifiindoor.types.IndoorMapReply;
import com.ericsson.cgc.aurora.wifiindoor.types.Location;
import com.ericsson.cgc.aurora.wifiindoor.types.LocationSet;
import com.ericsson.cgc.aurora.wifiindoor.types.MapInfoReply;
import com.ericsson.cgc.aurora.wifiindoor.types.MapManagerReply;
import com.ericsson.cgc.aurora.wifiindoor.types.NaviInfoReply;
import com.ericsson.cgc.aurora.wifiindoor.types.QueryInfo;
import com.ericsson.cgc.aurora.wifiindoor.types.TestLocateCollectReply;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsCredentialsException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.http.WifiIpsHttpApi;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.Test;

/**
 * @author ezhipin
 * 
 */
public class WebService {
	/**
	 * This api is supported in the V1 API
	 */
	@interface V1 {
	}

	private static WebService mSingletonInstance = null;

	public static WebService getInstance() {
		if (mSingletonInstance == null)
			mSingletonInstance = new WebService();

		return mSingletonInstance;
	}

	private WifiIpsHttpApi mWifiIpsHttpApi;

	private WebService() {

	}
	
	/*
	 * locate
	 */
	@V1
	public void collect(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		mWifiIpsHttpApi.collect(json);
	}
	
	/*public void setProxy(){
		WebProxy myProxy = new WebProxy("192.168.0.3:8080",true);
		//myProxy.Credentials = new NetworkCredential("username","password","domainname");
		myService.Proxy = myProxy;

	}*/

	/*
	 * collectNfc
	 */
	@V1
	public void collectNfc(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		mWifiIpsHttpApi.collectNfc(json);
	}

	/*
	 * collect_test
	 */
	@V1
	public TestLocateCollectReply collectTest(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.collectTest(json);
	}

	/*
	 * deleteFingerprint, no response
	 */
	@V1
	public void deleteFingerprint(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		mWifiIpsHttpApi.deleteFingerprint(json);
	}

	public boolean initialize(String domain, String clientVersion) {
		mWifiIpsHttpApi = new WifiIpsHttpApi(domain, clientVersion);

		return true;
	}
	
	/*
	 * collect, no response
	 */
	@V1
	public LocationSet locate(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.locate(json);
	}
	
	/*
	 * locateBaseOnNfc
	 */
	@V1
	public Location locateBaseOnNfc(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.locateBaseOnNfc(json);
	}

	/*
	 * locate_test
	 */
	@V1
	public TestLocateCollectReply locateTest(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.locateTest(json);
	}
	
	/*
	 * query
	 */
	@V1
	public QueryInfo query(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.query(json);
	}
	
	/*
	 * update advertise Info
	 */
	@V1
	public AdGroup queryAdvertiseInfo(JSONObject json) throws WifiIpsException,
	        WifiIpsCredentialsException, WifiIpsError, IOException {

        if (mWifiIpsHttpApi == null) {			
	        throw new WifiIpsException("IPS HTTP API is null!");			
        }

        return mWifiIpsHttpApi.queryAdvertiseInfo(json);
    }
	
	/*
	 * update Apk
	 */
	@V1
	public ApkVersionReply queryApkVersion(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryApkVersion(json);
	}
	
	/*
	 * update Buildings
	 */
	@V1
	public BuildingManagerReply queryBuilding(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryBuilding(json);
	}
	
	/*
	 * update Map
	 */
	@V1
	public IndoorMapReply queryMap(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryMap(json);
	}
	
	/*
	 * update Map Info
	 */
	@V1
	public MapInfoReply queryMapInfo(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryMapInfo(json);
	}
	
	/*
	 * update mapList
	 */
	@V1
	public MapManagerReply queryMaps(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryMaps(json);
	}
	
	/*
	 * update Navi Info
	 */
	@V1
	public NaviInfoReply queryNaviInfo(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryNaviInfo(json);
	}
	
	public boolean reInitialize(String domain, String clientVersion) {
		mWifiIpsHttpApi = null;
		return initialize(domain, clientVersion);
	}
	
	
	/*
	 * 
	 */
	@V1
	public Test testPostJson(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.testPostJson(json);
	}	
		
	
	/*
	 * 
	 */
	@V1
	public Test testPostXml(String parameter) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.testPostXml(parameter);
	}
}
