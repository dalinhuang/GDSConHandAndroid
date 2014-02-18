package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.wifiindoor.types.MapManagerReply;
import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;

public class MapManagerReplyJsonParser extends AbstractJsonParser<MapManagerReply> {
	@Override
	protected MapManagerReply parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		MapManagerReply manager = gson.fromJson(parser.getContent(), MapManagerReply.class);
		return manager;
	}
}
