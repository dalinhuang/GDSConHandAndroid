/**
 * @(#)IXmlParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.parsers.xml;

import org.xmlpull.v1.XmlPullParser;

import com.winjune.wifiindoor.webservice.error.WifiIpsError;
import com.winjune.wifiindoor.webservice.error.WifiIpsParseException;
import com.winjune.wifiindoor.webservice.parsers.IParser;
import com.winjune.wifiindoor.webservice.types.IType;



/**
 * @author ezhipin
 * 
 */
public interface IXmlParser<T extends IType> extends IParser<T> {
	public abstract T parse(XmlPullParser parser) throws WifiIpsError,
			WifiIpsParseException;
}
