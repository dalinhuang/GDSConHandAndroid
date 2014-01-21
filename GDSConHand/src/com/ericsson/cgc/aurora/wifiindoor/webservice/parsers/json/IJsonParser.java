/**
 * @(#)IJsonParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json;

import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsParseException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.IParser;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;



/**
 * @author ezhipin
 * 
 */
public interface IJsonParser<T extends IType> extends IParser<T> {
	public abstract T parse(JsonPullParser parser) throws WifiIpsError, WifiIpsParseException;
}
