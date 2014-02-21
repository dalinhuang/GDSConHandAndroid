package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.wifiindoor.types.InterestPlacesInfoReply;
import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;

/**
 * @author haleyshi
 * 
 */
public class InterestPlacesInfoReplyJsonParser extends AbstractJsonParser<InterestPlacesInfoReply> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.winjune.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected InterestPlacesInfoReply parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		InterestPlacesInfoReply info = gson.fromJson(parser.getContent(), InterestPlacesInfoReply.class);
		return info;
	}

}