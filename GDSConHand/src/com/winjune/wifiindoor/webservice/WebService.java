/**
 * @(#)WipsWebService.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice;

import java.io.IOException;

import org.json.JSONObject;

import com.winjune.wifiindoor.ads.AdGroup;
import com.winjune.wifiindoor.types.ApkVersionReply;
import com.winjune.wifiindoor.types.BuildingManagerReply;
import com.winjune.wifiindoor.types.IndoorMapReply;
import com.winjune.wifiindoor.types.InterestPlacesInfoReply;
import com.winjune.wifiindoor.types.Location;
import com.winjune.wifiindoor.types.LocationSet;
import com.winjune.wifiindoor.types.MapInfoReply;
import com.winjune.wifiindoor.types.MapManagerReply;
import com.winjune.wifiindoor.types.NaviInfoReply;
import com.winjune.wifiindoor.types.QueryInfo;
import com.winjune.wifiindoor.types.TestLocateCollectReply;
import com.winjune.wifiindoor.webservice.error.WifiIpsCredentialsException;
import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsException;
import com.winjune.wifiindoor.webservice.http.WifiIpsHttpApi;
import com.winjune.wifiindoor.webservice.types.Test;

/**
 * @author ezhipin
 * 
 */
public class WebService {
	private static WebService mSingletonInstance = null;

	private WifiIpsHttpApi mWifiIpsHttpApi;

	private WebService() {

	}

	public static WebService getInstance() {
		if (mSingletonInstance == null)
			mSingletonInstance = new WebService();

		return mSingletonInstance;
	}

	public boolean initialize(String domain, String clientVersion) {
		mWifiIpsHttpApi = new WifiIpsHttpApi(domain, clientVersion);

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
	public Test testPostXml(String parameter) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WifiIpsException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.testPostXml(parameter);
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
	 * update Interest Places Info
	 */
	@V1
	public InterestPlacesInfoReply queryInterestPlacesInfo(JSONObject json)  throws WifiIpsException,
    		WifiIpsCredentialsException, WifiIpsError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
	        throw new WifiIpsException("IPS HTTP API is null!");			
        }

        return mWifiIpsHttpApi.queryInterestPlacesInfo(json);
	}
	
	/**
	 * This api is supported in the V1 API
	 */
	@interface V1 {
	}

}
