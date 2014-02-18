/**
 * @(#)HttpApiWithNoAuth.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.http;

import java.io.IOException;

import org.apache.http.client.methods.HttpRequestBase;

import com.winjune.wifiindoor.webservice.error.WifiIpsCredentialsException;
import com.winjune.wifiindoor.webservice.error.WifiIpsException;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;
import com.winjune.wifiindoor.webservice.parsers.xml.IXmlParser;
import com.winjune.wifiindoor.webservice.types.IType;




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
