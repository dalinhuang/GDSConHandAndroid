package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.parsers.json.AbstractJsonParser;
import com.winjune.common.webservice.core.parsers.json.JsonPullParser;
import com.winjune.wifiindoor.webservice.types.MapManagerReply;

public class MapManagerReplyJsonParser extends AbstractJsonParser<MapManagerReply> {
	@Override
	protected MapManagerReply parseInner(JsonPullParser parser) throws IOException,
			WebError, WebParseException {
		Gson gson = new Gson();
		MapManagerReply manager = gson.fromJson(parser.getContent(), MapManagerReply.class);
		return manager;
	}
}
