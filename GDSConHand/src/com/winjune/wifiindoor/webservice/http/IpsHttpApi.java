/**
 * @(#)WipsHttpApi.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.http;

import java.io.IOException;

import org.apache.http.auth.AuthScope;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.winjune.common.webservice.core.error.WebCredentialsException;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebException;
import com.winjune.common.webservice.core.http.HttpApiWithNoAuth;
import com.winjune.common.webservice.core.http.IHttpApi;
import com.winjune.common.webservice.core.parsers.json.TestJsonParser;
import com.winjune.common.webservice.core.parsers.xml.TestXmlParser;
import com.winjune.common.webservice.core.types.Test;
import com.winjune.wifiindoor.ads.AdGroup;
import com.winjune.wifiindoor.ads.AdvertiseInfoReplyJsonParser;
import com.winjune.wifiindoor.util.WifiIpsSettings;
import com.winjune.wifiindoor.webservice.parsers.json.ApkVersionJsonParser;
import com.winjune.wifiindoor.webservice.parsers.json.BuildingManagerReplyJsonParser;
import com.winjune.wifiindoor.webservice.parsers.json.CollectStatusReplyJsonParser;
import com.winjune.wifiindoor.webservice.parsers.json.LocationJsonParser;
import com.winjune.wifiindoor.webservice.parsers.json.LocationSetJsonParser;
import com.winjune.wifiindoor.webservice.parsers.json.NullJsonParser;
import com.winjune.wifiindoor.webservice.parsers.json.QueryInfoJsonParser;
import com.winjune.wifiindoor.webservice.parsers.json.TestLocationJsonParser;
import com.winjune.wifiindoor.webservice.types.ApkVersionReply;
import com.winjune.wifiindoor.webservice.types.BuildingManagerReply;
import com.winjune.wifiindoor.webservice.types.CollectStatusReply;
import com.winjune.wifiindoor.webservice.types.Location;
import com.winjune.wifiindoor.webservice.types.LocationSet;
import com.winjune.wifiindoor.webservice.types.QueryInfo;
import com.winjune.wifiindoor.webservice.types.TestLocateCollectReply;

/**
 * @author ezhipin
 * 
 */
public class IpsHttpApi {
	@SuppressWarnings("unused")
	private static final String TAG = "IpsHttpApi";
	@SuppressWarnings("unused")
	private static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private final String mApiBaseUrl;
	private AuthScope mAuthScope;

	private IHttpApi mHttpApi;

	private NullJsonParser mNullJsonParser = new NullJsonParser();

	public IpsHttpApi(String domain) {
		mApiBaseUrl = WifiIpsSettings.URL_PREFIX + domain;		
	}
	
	public boolean initIpsHttpClient(String domain, String clientVersion) {
		
		try	{
			mAuthScope = new AuthScope(domain, 80);
			mHttpApi = new HttpApiWithNoAuth(clientVersion);
		} catch (Exception e) {			
			return false;
		}	
		
		if ((mAuthScope != null) && (mHttpApi != null))
			return true;
		else
			return false;		
		
	}

	public Test testPostXml(String parameter) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_TEST), new BasicNameValuePair(
						"parameter", parameter));
		return (Test) mHttpApi.doHttpRequest(httpPost, new TestXmlParser());
	}

	public Test testPostJson(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_TEST), json);
		return (Test) mHttpApi.doHttpRequest(httpPost, new TestJsonParser());
	}

	public Test testGet(String parameter) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpGet httpGet = mHttpApi.createHttpGet(
				fullUrl(WifiIpsSettings.URL_API_TEST), new BasicNameValuePair(
						"parameter", parameter));
		return (Test) mHttpApi.doHttpRequest(httpGet, new TestXmlParser());
	}

	public LocationSet locate(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_LOCATE), json);
		return (LocationSet) mHttpApi.doHttpRequest(httpPost,
				new LocationSetJsonParser());
	}
	
	public TestLocateCollectReply locateTest(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_LOCATE_TEST), json);
		return (TestLocateCollectReply) mHttpApi.doHttpRequest(httpPost,
				new TestLocationJsonParser());
	}
	
	public TestLocateCollectReply collectTest(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_COLLECT_TEST), json);
		return (TestLocateCollectReply) mHttpApi.doHttpRequest(httpPost,
				new TestLocationJsonParser());
	}

	public void collect(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_COLLECT), json);
		mHttpApi.doHttpRequest(httpPost, mNullJsonParser);
	}

	public QueryInfo query(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_QUERY), json);
		return (QueryInfo) mHttpApi.doHttpRequest(httpPost,
				new QueryInfoJsonParser());
	}

	public void collectNfc(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_NFC_COLLECT), json);
		mHttpApi.doHttpRequest(httpPost, mNullJsonParser);
	}

	public Location locateBaseOnNfc(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_LOCATE_BASE_NFC), json);
		return (Location) mHttpApi.doHttpRequest(httpPost,
				new LocationJsonParser());
	}
	
	public void deleteFingerprint(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_DELETE_FINGERPRINT), json);
		mHttpApi.doHttpRequest(httpPost, mNullJsonParser);
	}
	
	public ApkVersionReply queryApkVersion(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_QUERY_APK_VERSION), json);
		return (ApkVersionReply) mHttpApi.doHttpRequest(httpPost,
				new ApkVersionJsonParser());
	}
	
	public BuildingManagerReply queryBuilding(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_QUERY_BUILDING), json);
		return (BuildingManagerReply) mHttpApi.doHttpRequest(httpPost,
				new BuildingManagerReplyJsonParser());
	}
		
	public AdGroup queryAdvertiseInfo(JSONObject json) throws WebException,
            WebCredentialsException, WebError, IOException {
        HttpPost httpPost = mHttpApi.createHttpPost(
                 fullUrl(WifiIpsSettings.URL_API_QUERY_ADVERTISE_INFO), json);
        return (AdGroup) mHttpApi.doHttpRequest(httpPost,
                 new AdvertiseInfoReplyJsonParser());
	}
	
	public CollectStatusReply queryCollectStatus(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_QUERY_COLLECT_STATUS), json);
		return (CollectStatusReply) mHttpApi.doHttpRequest(httpPost,
				new CollectStatusReplyJsonParser());
	}

	private String fullUrl(String url) {
		return mApiBaseUrl + url;
	}

}
