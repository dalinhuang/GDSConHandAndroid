/**
 * @(#)HttpApiWithNoAuth.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.common.webservice.core.http;

import java.io.IOException;

import org.apache.http.client.methods.HttpRequestBase;

import com.winjune.common.webservice.core.error.WebCredentialsException;
import com.winjune.common.webservice.core.error.WebException;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.parsers.xml.IXmlParser;
import com.winjune.common.webservice.core.types.IType;




/**
 * @author ezhipin
 *
 */
public class HttpApiWithNoAuth extends AbstractHttpApi {

    public HttpApiWithNoAuth(String clientVersion) {
        super(clientVersion);
    }

    public IType doHttpRequest(HttpRequestBase httpRequest,
            IXmlParser<? extends IType> parser) throws WebCredentialsException,
            WebParseException, WebException, IOException {
        return doHttpRequest(httpRequest, parser);
    }
}
