package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.parsers.json.AbstractJsonParser;
import com.winjune.common.webservice.core.parsers.json.JsonPullParser;
import com.winjune.wifiindoor.webservice.types.TestLocateCollectReply;

/**
 * @author haleyshi
 * 
 */
public class TestLocationJsonParser extends AbstractJsonParser<TestLocateCollectReply> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.winjune.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected TestLocateCollectReply parseInner(JsonPullParser parser) throws IOException,
			WebError, WebParseException {
		Gson gson = new Gson();
		TestLocateCollectReply location = gson.fromJson(parser.getContent(), TestLocateCollectReply.class);
		return location;
	}

}

