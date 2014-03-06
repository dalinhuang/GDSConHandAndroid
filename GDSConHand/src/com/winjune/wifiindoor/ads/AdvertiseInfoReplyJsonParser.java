package com.winjune.wifiindoor.ads;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.parsers.json.AbstractJsonParser;
import com.winjune.common.webservice.core.parsers.json.JsonPullParser;

public class AdvertiseInfoReplyJsonParser extends AbstractJsonParser<AdGroup> {
	
	@Override
	protected AdGroup parseInner(JsonPullParser parser) throws IOException,
			WebError, WebParseException {
		Gson gson = new Gson();
		AdGroup info = gson.fromJson(parser.getContent(), AdGroup.class);
		return info;
	}

}
