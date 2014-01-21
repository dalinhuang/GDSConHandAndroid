package com.ericsson.cgc.aurora.wifiindoor.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.text.TextUtils;
import android.util.Log;

import com.ericsson.cgc.aurora.wifiindoor.util.NfcConstants;
import com.ericsson.cgc.aurora.wifiindoor.util.WifiIpsSettings;

public class NfcInfoManager {
	private static final boolean DEBUG = WifiIpsSettings.DEBUG;
	protected static final String TAG = NfcInfoManager.class.getSimpleName();
	
	private NfcAdapter nfcAdapter;
	
	public NfcInfoManager(Context paramContext){
		nfcAdapter = NfcAdapter.getDefaultAdapter(paramContext);
	}
	
	/**
	 * bytesToHexString
	 * 
	 * @param src
	 * @return
	 * @param
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString().toUpperCase();
	}
	
	public NfcAdapter getNfcAdapter(){
		return nfcAdapter;
	}
	
	public String getTagId(Intent intent){
		Tag tag = parseTagFromIntent(intent);
		
		if (tag == null) {
			if (DEBUG)
				Log.d(TAG, "no tag is parsed from intent");
			
			return null;
		}

		String rfidFullString = processTag(tag);
		if ((rfidFullString == null) || TextUtils.isEmpty(rfidFullString)) {
			if (DEBUG)
				Log.d(TAG, "No RFID data is found from tag.");

			return null;
		}

		return rfidFullString.substring(0,
				NfcConstants.RFID_STRING_MAX_LENGTH);
	}
	
	public boolean isNfcEmbeded() {
		if (nfcAdapter==null){
			return false;
		}
		
		return true;
	}
	
	public boolean isNfcEnabled(){
		if (!isNfcEmbeded()) {
			return false;
		}
		
		return nfcAdapter.isEnabled();
	}

	private Tag parseTagFromIntent(Intent intent) {
		if (DEBUG)
			Log.d(TAG, "parseNfcIntent()");

		Tag tag;

		String action = intent.getAction();

		if (!action.equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
			if (DEBUG)
				Log.d(TAG, "invalid action.");
			return null;
		}

		tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

		if ((tag == null) || (tag.getTechList().length == 0)) {
			if (DEBUG)
				Log.d(TAG, "TAG is null or doesn't include any tech.");

			return null;
		}

		if (DEBUG) {
			Log.d(TAG, "Support below techs.");
			for (String tech : tag.getTechList()) {
				Log.d(TAG, tech);
			}
		}

		if (DEBUG)
			Log.d(TAG, "mTag.getId() == " + bytesToHexString(tag.getId()));

		return tag;
	}
	
	/**
	 * Parses the NDEF Message from the intent
	 * 
	 * 
	 */
	public String processTag(Tag tag) {
		if (DEBUG)
			Log.d(TAG, "processTag()");

		// Get an instance of the Mifare classic card from this TAG
		// intent
		MifareClassic mfc = MifareClassic.get(tag);

		try {
			// get the number of sectors this card has..and loop
			// thru these sectors
			int sectorCount = mfc.getSectorCount();
			int blockCount = mfc.getBlockCount();
			int size = mfc.getSize();

			String metaInfo = "";
			if (DEBUG) {
				// get tag type
				int tagType = mfc.getType();
				String tagTypeString = "";

				switch (tagType) {
				case MifareClassic.TYPE_CLASSIC:
					tagTypeString = NfcConstants.TAG_TYPE_CLASSIC;
					break;
				case MifareClassic.TYPE_PLUS:
					tagTypeString = NfcConstants.TAG_TYPE_PLUS;
					break;
				case MifareClassic.TYPE_PRO:
					tagTypeString = NfcConstants.TAG_TYPE_PRO;
					break;
				case MifareClassic.TYPE_UNKNOWN:
					tagTypeString = NfcConstants.TAG_TYPE_UNKNOWN;
					break;
				}

				metaInfo = "Tag type: " + tagTypeString + ", Sector Count: "
						+ sectorCount + ", Block Count: " + blockCount
						+ ", Size: " + size + " bytes.";

				Log.d(TAG, metaInfo);
			}

			// MifareClassicCard mifareClassicCard = readTagPayload(mfc);

			// read data
			String data = readDataFromTag(mfc, NfcConstants.RFID_START_BLOCK,
					NfcConstants.RFID_BLOCK_COUNT);

			return data;

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "IO with card fails.");

			return null;
		}
	}
	
	/**
	 * readDataFromTag
	 * 
	 * @param mfc
	 * @param startBlock
	 * @param readcount
	 * @return
	 * @param
	 * @return
	 */
	public String readDataFromTag(MifareClassic mfc, int startBlock, int readcount) {
		String metaInfo = "";
		boolean auth;
		try {
			mfc.connect();
			for (int j = startBlock; j < startBlock + readcount; j++) {
				// Authenticate a sector with key A.
				auth = mfc.authenticateSectorWithKeyA(j
						/ NfcConstants.MIFARE_SECTOR_BLOCK_COUNT,
						NfcConstants.RFID_KEY);
				if (auth) {
					byte[] data = mfc.readBlock(j);
					String dataString = new String(data, "US-ASCII");
					metaInfo += dataString + "\n";
				}
			}
			mfc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return metaInfo;
	}
}
