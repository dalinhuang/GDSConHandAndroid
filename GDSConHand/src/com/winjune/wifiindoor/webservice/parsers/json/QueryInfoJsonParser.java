/**
 * @(#)TestJsonParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.wifiindoor.types.QueryInfo;
import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;

/**
 * @author ezhipin
 * 
 */
public class QueryInfoJsonParser extends AbstractJsonParser<QueryInfo> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected QueryInfo parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		QueryInfo queryInfo = gson.fromJson(parser.getContent(), QueryInfo.class);
		return queryInfo;
	}

}
