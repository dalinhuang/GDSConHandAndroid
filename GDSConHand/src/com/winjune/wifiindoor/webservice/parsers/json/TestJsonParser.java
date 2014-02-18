/**
 * @(#)TestJsonParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;
import com.winjune.wifiindoor.webservice.types.Test;

/**
 * @author ezhipin
 * 
 */
public class TestJsonParser extends AbstractJsonParser<Test> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected Test parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {

		// JSONObject jsonObject = new JSONObject(parser.getContent());

		Gson gson = new Gson();
		Test test = gson.fromJson(parser.getContent(), Test.class);
		return test;
	}

}
