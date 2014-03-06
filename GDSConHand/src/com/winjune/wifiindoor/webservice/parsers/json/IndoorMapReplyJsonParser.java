package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.parsers.json.AbstractJsonParser;
import com.winjune.common.webservice.core.parsers.json.JsonPullParser;
import com.winjune.wifiindoor.webservice.types.IndoorMapReply;

public class IndoorMapReplyJsonParser extends AbstractJsonParser<IndoorMapReply> {
	@Override
	protected IndoorMapReply parseInner(JsonPullParser parser) throws IOException,
			WebError, WebParseException {
		Gson gson = new Gson();
		IndoorMapReply map = gson.fromJson(parser.getContent(), IndoorMapReply.class);
		return map;
	}
}