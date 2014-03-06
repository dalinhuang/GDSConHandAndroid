/**
 * @(#)WipsException.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.common.webservice.core.error;

/**
 * @author ezhipin
 * 
 */
public class WebException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1721090463886447530L;

	public WebException(String message) {
		super(message);
	}
}
