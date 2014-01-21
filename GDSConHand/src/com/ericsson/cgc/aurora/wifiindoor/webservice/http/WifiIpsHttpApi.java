/**
 * @(#)WipsHttpApi.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice.http;

import java.io.IOException;

import org.apache.http.auth.AuthScope;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.ericsson.cgc.aurora.wifiindoor.ads.AdGroup;
import com.ericsson.cgc.aurora.wifiindoor.ads.AdvertiseInfoReplyJsonParser;
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
import com.ericsson.cgc.aurora.wifiindoor.util.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsCredentialsException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.ApkVersionJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.BuildingManagerReplyJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.IndoorMapReplyJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.LocationJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.LocationSetJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.MapInfoReplyJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.MapManagerReplyJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.NaviInfoReplyJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.NullJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.QueryInfoJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.TestJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.TestLocationJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.xml.TestXmlParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.Test;

/**
 * @author ezhipin
 * 
 */
public class WifiIpsHttpApi {
	@SuppressWarnings("unused")
	private static final String TAG = "WifiIpsHttpApi";
	@SuppressWarnings("unused")
	private static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private final String mApiBaseUrl;
	@SuppressWarnings("unused")
	private final AuthScope mAuthScope;

	private IHttpApi mHttpApi;

	private NullJsonParser mNullJsonParser = new NullJsonParser();

	public WifiIpsHttpApi(String domain, String clientVersion) {
		mApiBaseUrl = WifiIpsSettings.URL_PREFIX + domain;
		mAuthScope = new AuthScope(domain, 80);

		mHttpApi = new HttpApiWithNoAuth(clientVersion);
	}

	public void collect(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_COLLECT), json);
		mHttpApi.doHttpRequest(httpPost, mNullJsonParser);
	}

	public void collectNfc(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_NFC_COLLECT), json);
		mHttpApi.doHttpRequest(httpPost, mNullJsonParser);
	}

	public TestLocateCollectReply collectTest(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_COLLECT_TEST), json);
		return (TestLocateCollectReply) mHttpApi.doHttpRequest(httpPost,
				new TestLocationJsonParser());
	}

	public void deleteFingerprint(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_DELETE_FINGERPRINT), json);
		mHttpApi.doHttpRequest(httpPost, mNullJsonParser);
	}
	
	private String fullUrl(String url) {
		return mApiBaseUrl + url;
	}
	
	public LocationSet locate(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_LOCATE), json);
		return (LocationSet) mHttpApi.doHttpRequest(httpPost,
				new LocationSetJsonParser());
	}

	public Location locateBaseOnNfc(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_LOCATE_BASE_NFC), json);
		return (Location) mHttpApi.doHttpRequest(httpPost,
				new LocationJsonParser());
	}

	public TestLocateCollectReply locateTest(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_LOCATE_TEST), json);
		return (TestLocateCollectReply) mHttpApi.doHttpRequest(httpPost,
				new TestLocationJsonParser());
	}

	public QueryInfo query(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_QUERY), json);
		return (QueryInfo) mHttpApi.doHttpRequest(httpPost,
				new QueryInfoJsonParser());
	}

	public AdGroup queryAdvertiseInfo(JSONObject json) throws WifiIpsException,
            WifiIpsCredentialsException, WifiIpsError, IOException {
        HttpPost httpPost = mHttpApi.createHttpPost(
                 fullUrl(WifiIpsSettings.URL_API_QUERY_ADVERTISE_INFO), json);
        return (AdGroup) mHttpApi.doHttpRequest(httpPost,
                 new AdvertiseInfoReplyJsonParser());
   }
	
	public ApkVersionReply queryApkVersion(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_QUERY_APK_VERSION), json);
		return (ApkVersionReply) mHttpApi.doHttpRequest(httpPost,
				new ApkVersionJsonParser());
	}
	
	public BuildingManagerReply queryBuilding(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_QUERY_BUILDING), json);
		return (BuildingManagerReply) mHttpApi.doHttpRequest(httpPost,
				new BuildingManagerReplyJsonParser());
	}
	
	public IndoorMapReply queryMap(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_DOWNLOAD_MAP), json);
		return (IndoorMapReply) mHttpApi.doHttpRequest(httpPost,
				new IndoorMapReplyJsonParser());
	}
	
	public MapInfoReply queryMapInfo(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_QUERY_MAP_INFO), json);
		return (MapInfoReply) mHttpApi.doHttpRequest(httpPost,
				new MapInfoReplyJsonParser());
	}
	
	public MapManagerReply queryMaps(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_QUERY_MAP_LIST), json);
		return (MapManagerReply) mHttpApi.doHttpRequest(httpPost,
				new MapManagerReplyJsonParser());
	}
	
	public NaviInfoReply queryNaviInfo(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_QUERY_NAVI_INFO), json);
		return (NaviInfoReply) mHttpApi.doHttpRequest(httpPost,
				new NaviInfoReplyJsonParser());
	}
	
	public Test testGet(String parameter) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpGet httpGet = mHttpApi.createHttpGet(
				fullUrl(WifiIpsSettings.URL_API_TEST), new BasicNameValuePair(
						"parameter", parameter));
		return (Test) mHttpApi.doHttpRequest(httpGet, new TestXmlParser());
	}
	
	public Test testPostJson(JSONObject json) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_TEST), json);
		return (Test) mHttpApi.doHttpRequest(httpPost, new TestJsonParser());
	}	

	public Test testPostXml(String parameter) throws WifiIpsException,
			WifiIpsCredentialsException, WifiIpsError, IOException {
		HttpPost httpPost = mHttpApi.createHttpPost(
				fullUrl(WifiIpsSettings.URL_API_TEST), new BasicNameValuePair(
						"parameter", parameter));
		return (Test) mHttpApi.doHttpRequest(httpPost, new TestXmlParser());
	}
}
