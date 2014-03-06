/**
 * @(#)TestJsonParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.parsers.json;

import java.io.IOException;









import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.parsers.json.AbstractJsonParser;
import com.winjune.common.webservice.core.parsers.json.JsonPullParser;
import com.winjune.common.webservice.core.types.IType;

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
			WebError, WebParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
