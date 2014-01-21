package com.ericsson.cgc.aurora.wifiindoor.ads;

import java.io.IOException;

import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsParseException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.AbstractJsonParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json.JsonPullParser;
import com.google.gson.Gson;

public class AdvertiseInfoReplyJsonParser extends AbstractJsonParser<AdGroup> {
	
	@Override
	protected AdGroup parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		AdGroup info = gson.fromJson(parser.getContent(), AdGroup.class);
		return info;
	}

}
