/**
 * @(#)NfcConstants.java
 * Apr 20, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.util;

/**
 * @author ezhipin
 *
 */
public class NfcConstants {
	public static final String TAG_TYPE_CLASSIC = "TYPE_CLASSIC";
	public static final String TAG_TYPE_PLUS = "TYPE_PLUS";
	public static final String TAG_TYPE_PRO = "TYPE_PRO";
	public static final String TAG_TYPE_UNKNOWN = "TYPE_UNKNOWN";
	
	// MifareClassic.KEY_DEFAULT is FF FF FF FF FF FF
	// We use FF FF FF 11 11 11
	public static final byte[] RFID_KEY = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte)0x11, (byte)0x11, (byte)0x11};
	public static final int RFID_START_BLOCK = 1;
	public static final int RFID_BLOCK_COUNT = 1;
	
	public static final int RFID_STRING_MAX_LENGTH = 12;
	
	
	public static final int MIFARE_SECTOR_BLOCK_COUNT = 4;
}
