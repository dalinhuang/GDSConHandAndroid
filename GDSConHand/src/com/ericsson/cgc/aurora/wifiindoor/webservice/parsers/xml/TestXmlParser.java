/**
 * @(#)TestXmlParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.xml;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.ericsson.cgc.aurora.wifiindoor.util.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsParseException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.Test;

import android.util.Log;


/**
 * @author ezhipin
 * 
 */
public class TestXmlParser extends AbstractXmlParser<Test> {
	public static final String TAG = "TestXmlParser";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wips.webservice.parsers.xml.AbstractXmlParser
	 * #parseInner(org.xmlpull.v1.XmlPullParser)
	 */
	@Override
	protected Test parseInner(XmlPullParser parser) throws IOException,
			XmlPullParserException, WifiIpsError, WifiIpsParseException {
		parser.require(XmlPullParser.START_TAG, null, null);

		Test test = new Test();

		while (parser.nextTag() == XmlPullParser.START_TAG) {
			String name = parser.getName();
			if ("name".equals(name)) {
				test.setName(parser.nextText());

			} else if ("note".equals(name)) {
				test.setNote(parser.nextText());

			} else {
				// Consume something we don't understand.
				if (DEBUG)
					Log.d(TAG, "Found tag that we don't recognize: " + name);
				skipSubTree(parser);
			}
		}

		return test;
	}

}
