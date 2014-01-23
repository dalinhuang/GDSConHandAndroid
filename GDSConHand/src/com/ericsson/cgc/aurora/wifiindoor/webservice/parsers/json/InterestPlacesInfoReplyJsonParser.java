package com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.ericsson.cgc.aurora.wifiindoor.types.InterestPlacesInfoReply;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsParseException;
import com.google.gson.Gson;

/**
 * @author haleyshi
 * 
 */
public class InterestPlacesInfoReplyJsonParser extends AbstractJsonParser<InterestPlacesInfoReply> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wips.webservice.parsers.json.AbstractJsonParser
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