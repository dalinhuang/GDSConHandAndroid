/**
 * @(#)TransportServiceThread.java
 * Jun 3, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.ericsson.cgc.aurora.wifiindoor.R;
import com.ericsson.cgc.aurora.wifiindoor.ads.AdGroup;
import com.ericsson.cgc.aurora.wifiindoor.types.ApkVersionReply;
import com.ericsson.cgc.aurora.wifiindoor.types.BuildingManagerReply;
import com.ericsson.cgc.aurora.wifiindoor.types.IndoorMapReply;
import com.ericsson.cgc.aurora.wifiindoor.types.InterestPlacesInfoReply;
import com.ericsson.cgc.aurora.wifiindoor.types.Location;
import com.ericsson.cgc.aurora.wifiindoor.types.LocationSet;
import com.ericsson.cgc.aurora.wifiindoor.types.MapInfoReply;
import com.ericsson.cgc.aurora.wifiindoor.types.MapManagerReply;
import com.ericsson.cgc.aurora.wifiindoor.types.NaviInfoReply;
import com.ericsson.cgc.aurora.wifiindoor.types.QueryInfo;
import com.ericsson.cgc.aurora.wifiindoor.types.TestLocateCollectReply;
import com.ericsson.cgc.aurora.wifiindoor.util.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsCredentialsException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsError;
import com.ericsson.cgc.aurora.wifiindoor.webservice.error.WifiIpsException;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

/**
 * @author ezhipin
 * 
 */
public class TransportServiceThread extends Thread {
	public static final String TAG = "TransportServiceThread";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private TransportServiceListener mTransportServiceListener;
	private Context paramContext;
	
	private boolean mIsThreadRunToCompletion;

	public TransportServiceThread(TransportServiceListener listener, Context paramContext) {
		mTransportServiceListener = listener;
		mIsThreadRunToCompletion = false;
		this.paramContext = paramContext;
	}

	@Override
	public void run() {
		Looper.prepare();
		
		while (true) {
			while (OutgoingMessageQueue.isEmpty()) {
				try {
					// sleep 200ms
					Thread.sleep(200);
				} catch (InterruptedException e) {
					Log.e(TAG, "OutgoingMessageQueue is empty.", e);
				}
			}

			JSONObject json = OutgoingMessageQueue.take();

			try {
				int requestCode = json.getInt("RequestCode");
				JSONObject requestPayload = json
						.getJSONObject("RequestPayload");
				processRequest(requestCode, requestPayload);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void processRequest(int requestCode, JSONObject requestPayload) {
		if (DEBUG)
			Log.d(TAG, "processRequest()");

		WebService webServiceInstance = WebService.getInstance();
		if (webServiceInstance == null) {
			if (DEBUG)
				Log.d(TAG, "WebService instance is null.");

			mTransportServiceListener.onError("WebService instance is null.");
		
			return;
		}

		try {
			switch (requestCode) {
			case MsgConstants.MT_COLLECT:
				mTransportServiceListener.onStartingRequest();
				webServiceInstance.collect(requestPayload);
				mTransportServiceListener.onFinishingRequest();

				break;
			
			case MsgConstants.MT_LOCATE:
				mTransportServiceListener.onStartingRequest();
				LocationSet locations = webServiceInstance.locate(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(locations);

				break;
				
			case MsgConstants.MT_INFO_QUERY:
				mTransportServiceListener.onStartingRequest();
				QueryInfo info = webServiceInstance.query(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(info);

				break;	
				
			case MsgConstants.MT_EDIT_NFC_QR:
				mTransportServiceListener.onStartingRequest();			
				webServiceInstance.collectNfc(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				
				break;
				
			case MsgConstants.MT_LOCATE_FROM_NFC_QR:
				mTransportServiceListener.onStartingRequest();
				Location nfcLocation = webServiceInstance.locateBaseOnNfc(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(nfcLocation);
				
				break;
				
			case MsgConstants.MT_DELETE_FINGERPRINT:
				mTransportServiceListener.onStartingRequest();
				webServiceInstance.deleteFingerprint(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				
				break;	
			
			case MsgConstants.MT_LOCATE_TEST:
				mTransportServiceListener.onStartingRequest();
				TestLocateCollectReply testLocation = webServiceInstance.locateTest(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(testLocation);

				break;	
			
			case MsgConstants.MT_COLLECT_TEST:
				mTransportServiceListener.onStartingRequest();
				TestLocateCollectReply testLocation2 = webServiceInstance.collectTest(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(testLocation2);

				break;	
								
			case MsgConstants.MT_APK_VERSION_QUERY:
				mTransportServiceListener.onStartingRequest();
				ApkVersionReply version = webServiceInstance.queryApkVersion(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(version);

				break;	
			
			case MsgConstants.MT_BUILDING_QUERY:
				mTransportServiceListener.onStartingRequest();
				BuildingManagerReply buildingManager = webServiceInstance.queryBuilding(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(buildingManager);
				break;
				
			case MsgConstants.MT_MAP_LIST_QUERY:
				mTransportServiceListener.onStartingRequest();
				MapManagerReply mapManager = webServiceInstance.queryMaps(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(mapManager);
				break;
				
			case MsgConstants.MT_MAP_QUERY:
				mTransportServiceListener.onStartingRequest();
				IndoorMapReply indoorMap = webServiceInstance.queryMap(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(indoorMap);
				break;
			
			case MsgConstants.MT_MAP_INFO_QUERY:
				mTransportServiceListener.onStartingRequest();
				MapInfoReply mapInfo = webServiceInstance.queryMapInfo(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(mapInfo);
				break;
				
			case MsgConstants.MT_NAVI_INFO_QUERY:
				mTransportServiceListener.onStartingRequest();
				NaviInfoReply naviInfo = webServiceInstance.queryNaviInfo(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(naviInfo);
				break;
				
			case MsgConstants.MT_AD_INFO_QUERY:
				mTransportServiceListener.onStartingRequest();
				AdGroup advertiseList = webServiceInstance.queryAdvertiseInfo(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(advertiseList);
				break;		
				
			case MsgConstants.MT_INTEREST_PLACES_QUERY:
				mTransportServiceListener.onStartingRequest();
				InterestPlacesInfoReply interestPlacesInfo = webServiceInstance.queryInterestPlacesInfo(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(interestPlacesInfo);
				break;	
				
			default:
				mTransportServiceListener
						.onError("Do not support request code " + requestCode
								+ ".");

				break;
			}
		} catch (WifiIpsCredentialsException e) {
			// TODO Auto-generated catch block
			mTransportServiceListener.onError("051 " + e.toString());
			e.printStackTrace();
		} catch (WifiIpsError e) {
			// TODO Auto-generated catch block
			mTransportServiceListener.onError("052 " + e.toString());
			e.printStackTrace();
		} catch (WifiIpsException e) {
			// TODO Auto-generated catch block
			mTransportServiceListener.onError("053 " + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			mTransportServiceListener.onError(paramContext.getString(R.string.server_connect_timeout));
			e.printStackTrace();
		}
	}

	public boolean isThreadRunToCompletion() {
		return mIsThreadRunToCompletion;
	}
	
	public static interface TransportServiceListener {
		public void onStartingRequest();

		public void onFinishingRequest();

		public void onResponseReceived(IType object);

		public void onError(String error);
	}
}
