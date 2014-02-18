/**
 * @(#)AbstractHttpApi.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.util.Log;

import com.winjune.wifiindoor.util.WifiIpsSettings;
import com.winjune.wifiindoor.webservice.error.WifiIpsCredentialsException;
import com.winjune.wifiindoor.webservice.error.WifiIpsException;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;
import com.winjune.wifiindoor.webservice.parsers.json.AbstractJsonParser;
import com.winjune.wifiindoor.webservice.parsers.json.IJsonParser;
import com.winjune.wifiindoor.webservice.parsers.xml.AbstractXmlParser;
import com.winjune.wifiindoor.webservice.parsers.xml.IXmlParser;
import com.winjune.wifiindoor.webservice.types.IType;

/**
 * @author ezhipin
 * 
 */
public abstract class AbstractHttpApi implements IHttpApi {
	public static final String TAG = "AbstractHttpApi";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static final String CLIENT_VERSION_HEADER = "User-Agent";

	private final DefaultHttpClient mHttpClient;
	private final String mClientVersion;

	public AbstractHttpApi(String clientVersion) {
		mHttpClient = createHttpClient();
		mClientVersion = clientVersion;
	}

	public IType doHttpRequest(HttpRequestBase httpRequest,
			IXmlParser<? extends IType> parser)
			throws WifiIpsCredentialsException, WifiIpsParseException, WifiIpsException,
			IOException {
		if (DEBUG)
			Log.d(TAG, "doHttpRequest: " + httpRequest.getURI());

		HttpResponse response = executeHttpRequest(httpRequest);
		if (DEBUG)
			Log.d(TAG, "executed HttpRequest for: "
					+ httpRequest.getURI().toString());

		int statusCode = response.getStatusLine().getStatusCode();
		switch (statusCode) {
		case 200:
			InputStream is = response.getEntity().getContent();
			try {
				return parser.parse(AbstractXmlParser.createXmlPullParser(is));
			} finally {
				is.close();
			}
		case 401:
			response.getEntity().consumeContent();
			if (DEBUG)
				Log.d(TAG, "HTTP Code: 401");
			throw new WifiIpsCredentialsException(response.getStatusLine()
					.toString());
		case 404:
			response.getEntity().consumeContent();
			throw new WifiIpsException(response.getStatusLine().toString());
		case 500:
			response.getEntity().consumeContent();
			if (DEBUG)
				Log.d(TAG, "HTTP Code: 500");
			throw new WifiIpsException("Wips is down. Try again later.");
		default:
			if (DEBUG)
				Log.d(TAG, "Default case for status code reached: "
						+ response.getStatusLine().toString());
			response.getEntity().consumeContent();
			throw new WifiIpsException("Error connecting to WifiIPS Server: "
					+ statusCode + ". Try again later.");
		}
	}
	
	public IType doHttpRequest(HttpRequestBase httpRequest,
			IJsonParser<? extends IType> parser)
			throws WifiIpsCredentialsException, WifiIpsParseException, WifiIpsException,
			IOException {
		if (DEBUG)
			Log.d(TAG, "doHttpRequest: " + httpRequest.getURI());

		HttpResponse response = executeHttpRequest(httpRequest);
		if (DEBUG)
			Log.d(TAG, "executed HttpRequest for: "
					+ httpRequest.getURI().toString());

		int statusCode = response.getStatusLine().getStatusCode();
		switch (statusCode) {
		case 200:
			response.setHeader("Content-type", "application/json;charset=UTF-8");
			response.setHeader("Content-Encoding", "UTF-8");
			
			InputStream is = response.getEntity().getContent();
			
			if (DEBUG) {
				//Log.d(TAG, response.getEntity().getContentType().getName() + ": " + response.getEntity().getContentType().getValue());
				//Log.d(TAG, response.getEntity().getContentEncoding().getName() + ": " + response.getEntity().getContentEncoding().getValue());
			}			
			
			try {
				return parser.parse(AbstractJsonParser.createJsonPullParser(is));
			} finally {
				is.close();
			}
		case 401:
			response.getEntity().consumeContent();
			if (DEBUG)
				Log.d(TAG, "HTTP Code: 401");
			throw new WifiIpsCredentialsException(response.getStatusLine()
					.toString());
		case 404:
			response.getEntity().consumeContent();
			throw new WifiIpsException(response.getStatusLine().toString());
		case 500:
			response.getEntity().consumeContent();
			if (DEBUG)
				Log.d(TAG, "HTTP Code: 500");
			throw new WifiIpsException("Wips is down. Try again later.");
		default:
			if (DEBUG)
				Log.d(TAG, "Default case for status code reached: "
						+ response.getStatusLine().toString());
			response.getEntity().consumeContent();
			throw new WifiIpsException("Error connecting to Server: "
					+ statusCode + ". Try again later.");
		}
	}

	private HttpResponse executeHttpRequest(HttpRequestBase httpRequest)
			throws IOException {
		if (DEBUG)
			Log.d(TAG, "executing HttpRequest for: "
					+ httpRequest.getURI().toString());
		try {
			mHttpClient.getConnectionManager().closeExpiredConnections();
			return mHttpClient.execute(httpRequest);
		} catch (IOException e) {
			httpRequest.abort();
			throw e;
		}
	}

	public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
		if (DEBUG)
			Log.d(TAG, "creating HttpGet for: " + url);

		String query = URLEncodedUtils.format(stripNulls(nameValuePairs),
				HTTP.UTF_8);
		HttpGet httpGet = new HttpGet(url + "?" + query);
		httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);

		if (DEBUG)
			Log.d(TAG, "Created: " + httpGet.getURI());

		return httpGet;
	}

	public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
		if (DEBUG)
			Log.d(TAG, "creating HttpPost for: " + url);

		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setHeader("Accept", "application/xml");
		httpPost.setHeader("Content-type", "application/xml");
		
		httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(
					stripNulls(nameValuePairs), HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			throw new IllegalArgumentException(
					"Unable to encode http parameters.");
		}
		if (DEBUG)
			Log.d(TAG, "Created: " + httpPost);

		return httpPost;
	}

	public HttpPost createHttpPost(String url, JSONObject json) {
		if (DEBUG)
			Log.d(TAG, "creating HttpPost for: " + url);

		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
		httpPost.setHeader("Content-Encoding", "UTF-8");
		
		httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		try {
			httpPost.setEntity(new StringEntity(json.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			throw new IllegalArgumentException(
					"Unable to encode http parameters.");
		}

		if (DEBUG)
			Log.d(TAG, "Created: " + httpPost);

		return httpPost;
	}

	private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < nameValuePairs.length; i++) {
			NameValuePair param = nameValuePairs[i];
			if (param.getValue() != null) {
				if (DEBUG)
					Log.d(TAG, "Param: " + param);
				params.add(param);
			}
		}
		return params;
	}
	
    /**
     * Create a thread-safe client. This client does not do redirecting, to allow us to capture
     * correct "error" codes.
     *
     * @return HttpClient
     */
    public final DefaultHttpClient createHttpClient() {
        // Sets up the http part of the service.
        final SchemeRegistry supportedSchemes = new SchemeRegistry();

        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        supportedSchemes.register(new Scheme("http", sf, 80));

        // Set some client http client parameter defaults.
        final HttpParams httpParams = createHttpParams();
        HttpClientParams.setRedirecting(httpParams, false);

        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams,
                supportedSchemes);
        return new DefaultHttpClient(ccm, httpParams);
    }

    /**
     * Create the default HTTP protocol parameters.
     */
    private final HttpParams createHttpParams() {
        final HttpParams params = new BasicHttpParams();

        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        HttpConnectionParams.setConnectionTimeout(params, WifiIpsSettings.CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, WifiIpsSettings.SOCKET_TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        return params;
    }

}
