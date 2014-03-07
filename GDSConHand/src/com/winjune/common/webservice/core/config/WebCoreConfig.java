/**
 * @(#)IWebCoreSettings.java
 * Oct 30, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.common.webservice.core.config;

/**
 * @author ezhipin
 * 
 */
public class WebCoreConfig {
	// running in DEBUG mode or RUN mode
	public static boolean DEBUG = true;
	
	public static int CONNECTION_TIMEOUT = 30000; // 30s
	public static int SOCKET_TIMEOUT = 30000; // 30s
}
