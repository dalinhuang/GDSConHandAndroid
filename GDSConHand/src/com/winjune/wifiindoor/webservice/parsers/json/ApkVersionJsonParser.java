/**
 * @(#)ApkVersionJsonParser.java
 */
package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.parsers.json.AbstractJsonParser;
import com.winjune.common.webservice.core.parsers.json.JsonPullParser;
import com.winjune.wifiindoor.webservice.types.ApkVersionReply;

/**
 * @author haleyshi
 * 
 */
public class ApkVersionJsonParser extends AbstractJsonParser<ApkVersionReply> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.winjune.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected ApkVersionReply parseInner(JsonPullParser parser) throws IOException,
			WebError, WebParseException {
		Gson gson = new Gson();
		ApkVersionReply version = gson.fromJson(parser.getContent(), ApkVersionReply.class);
		return version;
	}

}
