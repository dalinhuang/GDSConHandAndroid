package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.wifiindoor.types.TestLocateCollectReply;
import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;

/**
 * @author haleyshi
 * 
 */
public class TestLocationJsonParser extends AbstractJsonParser<TestLocateCollectReply> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected TestLocateCollectReply parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		TestLocateCollectReply location = gson.fromJson(parser.getContent(), TestLocateCollectReply.class);
		return location;
	}

}

