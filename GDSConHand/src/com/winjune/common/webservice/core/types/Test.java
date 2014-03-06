/**
 * @(#)Test.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.common.webservice.core.types;


/**
 * @author ezhipin
 * 
 */
public class Test implements IType {
	private String mName;
	private String mNote;

	/**
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		mName = name;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return mNote;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		mNote = note;
	}

}
