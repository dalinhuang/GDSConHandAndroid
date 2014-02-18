package com.winjune.wifiindoor.ads;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;
import com.winjune.wifiindoor.webservice.parsers.json.AbstractJsonParser;
import com.winjune.wifiindoor.webservice.parsers.json.JsonPullParser;

public class AdvertiseInfoReplyJsonParser extends AbstractJsonParser<AdGroup> {
	
	@Override
	protected AdGroup parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		AdGroup info = gson.fromJson(parser.getContent(), AdGroup.class);
		return info;
	}

}
