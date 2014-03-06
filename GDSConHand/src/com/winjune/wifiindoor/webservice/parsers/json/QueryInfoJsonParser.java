/**
 * @(#)TestJsonParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.parsers.json.AbstractJsonParser;
import com.winjune.common.webservice.core.parsers.json.JsonPullParser;
import com.winjune.wifiindoor.webservice.types.QueryInfo;

/**
 * @author ezhipin
 * 
 */
public class QueryInfoJsonParser extends AbstractJsonParser<QueryInfo> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.winjune.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected QueryInfo parseInner(JsonPullParser parser) throws IOException,
			WebError, WebParseException {
		Gson gson = new Gson();
		QueryInfo queryInfo = gson.fromJson(parser.getContent(), QueryInfo.class);
		return queryInfo;
	}

}
