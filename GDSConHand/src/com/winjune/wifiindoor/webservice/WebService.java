/**
 * @(#)WipsWebService.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice;

import java.io.IOException;

import org.json.JSONObject;

import com.winjune.common.webservice.core.error.WebCredentialsException;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebException;
import com.winjune.common.webservice.core.types.Test;
import com.winjune.wifiindoor.ads.AdGroup;
import com.winjune.wifiindoor.webservice.http.WifiIpsHttpApi;
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
