package com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.ericsson.cgc.aurora.wifiindoor.types.MapManagerReply;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsParseException;
import com.google.gson.Gson;

public class MapManagerReplyJsonParser extends AbstractJsonParser<MapManagerReply> {
	@Override
	protected MapManagerReply parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		MapManagerReply manager = gson.fromJson(parser.getContent(), MapManagerReply.class);
		return manager;
	}
}
