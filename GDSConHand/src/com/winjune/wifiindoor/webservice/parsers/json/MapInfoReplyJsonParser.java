package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.wifiindoor.types.MapInfoReply;
import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;

/**
 * @author haleyshi
 * 
 */
public class MapInfoReplyJsonParser extends AbstractJsonParser<MapInfoReply> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected MapInfoReply parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		MapInfoReply info = gson.fromJson(parser.getContent(), MapInfoReply.class);
		return info;
	}

}