/**
 * @(#)IJsonParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.common.webservice.core.parsers.json;

import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.parsers.IParser;
import com.winjune.common.webservice.core.types.IType;



/**
 * @author ezhipin
 * 
 */
public interface IJsonParser<T extends IType> extends IParser<T> {
	public abstract T parse(JsonPullParser parser) throws WebError, WebParseException;
}
