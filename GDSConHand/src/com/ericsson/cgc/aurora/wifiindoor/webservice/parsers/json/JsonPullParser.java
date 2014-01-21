/**
 * @(#)JsonPullParser.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice.parsers.json;

import java.io.InputStream;

/**
 * @author ezhipin
 *
 */
public class JsonPullParser {
	private String content;
	
	public String getContent() {
		return content;
	}
	
	public void setInput(InputStream is, String input) {
		
	}
	
	public void setInput(String reader) {
		content = reader;
	}
}
