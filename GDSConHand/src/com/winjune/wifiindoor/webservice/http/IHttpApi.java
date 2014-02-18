/**
 * @(#)IHttpApi.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.http;

import java.io.IOException;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONObject;

import com.winjune.wifiindoor.webservice.error.WifiIpsCredentialsException;
import com.winjune.wifiindoor.webservice.error.WifiIpsException;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;
import com.winjune.wifiindoor.webservice.parsers.json.IJsonParser;
import com.winjune.wifiindoor.webservice.parsers.xml.IXmlParser;
import com.winjune.wifiindoor.webservice.types.IType;



/**
 * @author ezhipin
 *
 */
public interface IHttpApi {
    abstract public IType doHttpRequest(HttpRequestBase httpRequest,
            IXmlParser<? extends IType> parser) throws WifiIpsCredentialsException,
            WifiIpsParseException, WifiIpsException, IOException;

    abstract public IType doHttpRequest(HttpRequestBase httpRequest,
            IJsonParser<? extends IType> parser) throws WifiIpsCredentialsException,
            WifiIpsParseException, WifiIpsException, IOException;
    
    abstract public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs);

    abstract public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs);
    
    abstract public HttpPost createHttpPost(String url, JSONObject json);
}
