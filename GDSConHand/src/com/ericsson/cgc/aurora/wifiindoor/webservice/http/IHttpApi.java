/**
 * @(#)IHttpApi.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice.http;

import java.io.IOException;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONObject;

import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsCredentialsException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsParseException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.IJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.xml.IXmlParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;



/**
 * @author ezhipin
 *
 */
public interface IHttpApi {
    abstract public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs);

    abstract public HttpPost createHttpPost(String url, JSONObject json);
    
    abstract public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs);

    abstract public IType doHttpRequest(HttpRequestBase httpRequest,
            IJsonParser<? extends IType> parser) throws WifiIpsCredentialsException,
            WifiIpsParseException, WifiIpsException, IOException;
    
    abstract public IType doHttpRequest(HttpRequestBase httpRequest,
            IXmlParser<? extends IType> parser) throws WifiIpsCredentialsException,
            WifiIpsParseException, WifiIpsException, IOException;
}
