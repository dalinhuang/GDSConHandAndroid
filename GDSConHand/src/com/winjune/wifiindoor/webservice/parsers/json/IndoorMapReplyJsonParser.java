package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.wifiindoor.types.IndoorMapReply;
import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;

public class IndoorMapReplyJsonParser extends AbstractJsonParser<IndoorMapReply> {
	@Override
	protected IndoorMapReply parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		IndoorMapReply map = gson.fromJson(parser.getContent(), IndoorMapReply.class);
		return map;
	}
}