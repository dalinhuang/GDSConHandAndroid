/**
 * @(#)ITransportService.java
 * Oct 30, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.common.webservice.core.transport;

import org.json.JSONObject;

/**
 * @author ezhipin
 * 
 */
public interface IService {
	public void service(int requestCode, JSONObject requestPayload);
}
