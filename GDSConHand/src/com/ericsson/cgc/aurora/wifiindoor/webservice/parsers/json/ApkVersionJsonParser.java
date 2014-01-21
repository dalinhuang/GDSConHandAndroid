/**
 * @(#)ApkVersionJsonParser.java
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.ericsson.cgc.aurora.wifiindoor.types.ApkVersionReply;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsParseException;
import com.google.gson.Gson;

/**
 * @author haleyshi
 * 
 */
public class ApkVersionJsonParser extends AbstractJsonParser<ApkVersionReply> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected ApkVersionReply parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		ApkVersionReply version = gson.fromJson(parser.getContent(), ApkVersionReply.class);
		return version;
	}

}
