/**
 * @(#)HttpApiWithNoAuth.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice.http;

import java.io.IOException;

import org.apache.http.client.methods.HttpRequestBase;

import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsCredentialsException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsParseException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.xml.IXmlParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;




/**
 * @author ezhipin
 *
 */
public class HttpApiWithNoAuth extends AbstractHttpApi {

    public HttpApiWithNoAuth(String clientVersion) {
        super(clientVersion);
    }

    public IType doHttpRequest(HttpRequestBase httpRequest,
            IXmlParser<? extends IType> parser) throws WifiIpsCredentialsException,
            WifiIpsParseException, WifiIpsException, IOException {
        return doHttpRequest(httpRequest, parser);
    }
}
