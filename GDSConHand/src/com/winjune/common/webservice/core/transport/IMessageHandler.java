/**
 * @(#)MessageHandler.java
 * Oct 31, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.common.webservice.core.transport;

/**
 * @author ezhipin
 * 
 */
public interface IMessageHandler {
	public void handleStartingRequest();

	public void handleFinishingRequest();

	public void handleResponseReceived();

	public void handleErrorReported(String error);

}
