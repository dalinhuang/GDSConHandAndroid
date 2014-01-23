/**
 * @(#)MsgConstants.java
 * Jun 3, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice;

/**
 * @author ezhipin
 * 
 */
public class MsgConstants {
	/*
	 * Message between APP and web server
	 */
	public static final int MT_LOCATE = 1;
	public static final int MT_COLLECT = 2;
	public static final int MT_INFO_QUERY = 3;
	public static final int MT_LOCATE_FROM_NFC_QR = 4;
	public static final int MT_EDIT_NFC_QR = 5;
	public static final int MT_DELETE_FINGERPRINT = 6;
	public static final int MT_LOCATE_TEST = 7;
	public static final int MT_COLLECT_TEST = 8;
	public static final int MT_APK_VERSION_QUERY = 9;
	public static final int MT_BUILDING_QUERY = 10;
	public static final int MT_MAP_LIST_QUERY = 11;
	public static final int MT_MAP_QUERY = 12;
	public static final int MT_MAP_INFO_QUERY = 13;
	public static final int MT_NAVI_INFO_QUERY = 14;
	public static final int MT_AD_INFO_QUERY = 15;
	public static final int MT_INTEREST_PLACES_QUERY = 16;
}
