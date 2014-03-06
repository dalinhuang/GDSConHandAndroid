/**
 * @(#)TestJsonParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.common.webservice.core.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.types.Test;

/**
 * @author ezhipin
 * 
 */
public class TestJsonParser extends AbstractJsonParser<Test> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.winjune.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected Test parseInner(JsonPullParser parser) throws IOException,
			WebError, WebParseException {

		// JSONObject jsonObject = new JSONObject(parser.getContent());

		Gson gson = new Gson();
		Test test = gson.fromJson(parser.getContent(), Test.class);
		return test;
	}

}
