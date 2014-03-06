/**
 * @(#)IXmlParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.common.webservice.core.parsers.xml;

import org.xmlpull.v1.XmlPullParser;

import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.parsers.IParser;
import com.winjune.common.webservice.core.types.IType;



/**
 * @author ezhipin
 * 
 */
public interface IXmlParser<T extends IType> extends IParser<T> {
	public abstract T parse(XmlPullParser parser) throws WebError,
			WebParseException;
}
