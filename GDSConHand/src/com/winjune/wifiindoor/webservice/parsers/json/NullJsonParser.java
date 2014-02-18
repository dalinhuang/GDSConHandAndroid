/**
 * @(#)TestJsonParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;




import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;
import com.winjune.wifiindoor.webservice.types.IType;

/**
 * @author ezhipin
 * 
 */
public class NullJsonParser extends AbstractJsonParser<IType> {

	/* (non-Javadoc)
	 * @see com.winjune.wifiindoor.webservice.parsers.json.AbstractJsonParser#parseInner(com.winjune.wifiindoor.webservice.parsers.json.JsonPullParser)
	 */
	@Override
	protected IType parseInner(JsonPullParser parser) throws IOException,
			WifiIpsError, WifiIpsParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
