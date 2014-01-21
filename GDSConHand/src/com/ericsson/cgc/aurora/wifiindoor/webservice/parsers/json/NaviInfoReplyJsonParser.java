package com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.ericsson.cgc.aurora.wifiindoor.types.NaviInfoReply;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsParseException;
import com.google.gson.Gson;

/**
 * @author haleyshi
 * 
 */
public class NaviInfoReplyJsonParser extends AbstractJsonParser<NaviInfoReply> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected NaviInfoReply parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		NaviInfoReply info = gson.fromJson(parser.getContent(), NaviInfoReply.class);
		return info;
	}

}