package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.winjune.wifiindoor.types.BuildingManagerReply;
import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;

/**
 * @author haleyshi
 * 
 */
public class BuildingManagerReplyJsonParser extends AbstractJsonParser<BuildingManagerReply> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wips.webservice.parsers.json.AbstractJsonParser
	 * #parseInner()
	 */
	@Override
	protected BuildingManagerReply parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		Gson gson = new Gson();
		BuildingManagerReply manager = gson.fromJson(parser.getContent(), BuildingManagerReply.class);
		return manager;
	}

}
