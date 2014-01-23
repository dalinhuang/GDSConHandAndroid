/**
 * @(#)AbstractParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.ericsson.cgc.aurora.wifiindoor.util.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsParseException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

/**
 * @author ezhipin
 * 
 */
public abstract class AbstractXmlParser<T extends IType> implements
		IXmlParser<T> {
	public static final String TAG = "AbstractXmlParser";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private static XmlPullParserFactory sFactory;
	static {
		try {
			sFactory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException e) {
			throw new IllegalStateException("Could not create a factory");
		}
	}

	abstract protected T parseInner(final XmlPullParser parser)
			throws IOException, XmlPullParserException, WifiIpsError,
			WifiIpsParseException;

	public final T parse(XmlPullParser parser) throws WifiIpsParseException,
			WifiIpsError {
		try {
			if (parser.getEventType() == XmlPullParser.START_DOCUMENT) {
				parser.nextTag();
				if (parser.getName().equals("error")) {
					throw new WifiIpsError(parser.nextText());
				}
			}
			return parseInner(parser);
		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
			throw new WifiIpsParseException(e.getMessage());
		} catch (XmlPullParserException e) {
			Log.e(TAG, "XmlPullParserException", e);
			throw new WifiIpsParseException(e.getMessage());
		}
	}

	public static final XmlPullParser createXmlPullParser(InputStream is) {
		XmlPullParser parser;
		try {
			parser = sFactory.newPullParser();
			if (DEBUG) {
				StringBuffer sb = new StringBuffer();
				if (DEBUG) {
					while (true) {
						final int ch = is.read();
						if (ch < 0) {
							break;
						} else {
							sb.append((char) ch);
						}
					}
					is.close();
					
					if (DEBUG)
						Log.d(TAG, sb.toString());
				}
				parser.setInput(new StringReader(sb.toString()));
			} else {
				parser.setInput(is, null);
			}
		} catch (XmlPullParserException e) {
			throw new IllegalArgumentException();
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}
		return parser;
	}

	public static void skipSubTree(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, null);
		int level = 1;
		while (level > 0) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.END_TAG) {
				--level;
			} else if (eventType == XmlPullParser.START_TAG) {
				++level;
			}
		}
	}

}
