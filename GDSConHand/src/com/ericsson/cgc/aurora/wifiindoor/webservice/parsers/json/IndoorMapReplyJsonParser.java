package com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.ericsson.cgc.aurora.wifiindoor.types.IndoorMapReply;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsParseException;
import com.google.gson.Gson;

public class IndoorMapReplyJsonParser extends AbstractJsonParser<IndoorMapReply> {
	@Override
	protected IndoorMapReply parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		IndoorMapReply map = gson.fromJson(parser.getContent(), IndoorMapReply.class);
		return map;
	}
}