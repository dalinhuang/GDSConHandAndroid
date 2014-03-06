/**
 * @(#)AbstractJsonParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.common.webservice.core.parsers.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebParseException;
import com.winjune.common.webservice.core.types.IType;
import com.winjune.wifiindoor.util.WifiIpsSettings;

import android.util.Log;



/**
 * @author ezhipin
 * 
 */
public abstract class AbstractJsonParser<T extends IType> implements
		IJsonParser<T> {
	public static final String TAG = "AbstractJsonParser";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	abstract protected T parseInner(JsonPullParser parser) throws IOException,
			WebError, WebParseException;

	public final T parse(JsonPullParser parser) throws WebParseException,
			WebError {
		try {
			return parseInner(parser);
		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
			throw new WebParseException(e.getMessage());
		}
	}

	public static final JsonPullParser createJsonPullParser(InputStream is) {
		JsonPullParser parser = new JsonPullParser();
		try {
			
			String replyStr = preParseJsonString(is);

			is.close();

			if (DEBUG) {
				Log.e("Reply", replyStr);
			}

			parser.setInput(replyStr);

		} catch (IOException e) {
			throw new IllegalArgumentException();
		}

		return parser;
	}
	
	private static String preParseJsonString(InputStream is) {
		return preParseJsonString$2(is);
	}
	
	@SuppressWarnings("unused")
	private static String preParseJsonString$3(InputStream is) {
		StringBuffer sb = new StringBuffer();
		
		int a = -1;
		char ch = 0;
		boolean longChar = false;

		while (true) {		
			try {
				a = is.read();
				
				if (a < 0) {
					break;
				} else {
					if (longChar) {
						int high = (int) ch;
						ch = (char) (high << 8 + a);
						sb.append(ch);
						longChar = false;
						continue;
					}
					
					if (a > 127) {
						ch = (char) a;
						longChar = true;
						continue;
					} else {
						longChar = false;
						sb.append((char) a);
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
		return sb.toString();
	}

	private static String preParseJsonString$2(InputStream is) {	
		try {
			if (is == null) {
				return "";
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
						
			String replyStr = br.readLine();
			
			if (replyStr == null) {
				return "";
			}
			
			if (replyStr.isEmpty()) {
				return "";
			}
			
			return new String(replyStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return "";
	}

	@SuppressWarnings("unused")
	private static String preParseJsonString$1(InputStream is) {
		StringBuffer sb = new StringBuffer();

		while (true) {
			int ch;
			try {
				ch = is.read();
				if (ch < 0) {
					break;
				} else {
					sb.append((char) ch);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
		return sb.toString();
	}
}
