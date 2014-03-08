/**
 * @(#)IpsTransportServiceThread.java
 * Jun 3, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.webservice.transport;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.winjune.common.webservice.core.error.WebCredentialsException;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebException;
import com.winjune.common.webservice.core.transport.OutgoingMessageQueue;
import com.winjune.common.webservice.core.types.IType;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.ads.AdGroup;
import com.winjune.wifiindoor.util.WifiIpsSettings;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.ApkVersionReply;
import com.winjune.wifiindoor.webservice.types.BuildingManagerReply;
import com.winjune.wifiindoor.webservice.types.IndoorMapReply;
import com.winjune.wifiindoor.webservice.types.InterestPlacesInfoReply;
import com.winjune.wifiindoor.webservice.types.Location;
import com.winjune.wifiindoor.webservice.types.LocationSet;
import com.winjune.wifiindoor.webservice.types.MapInfoReply;
import com.winjune.wifiindoor.webservice.types.MapManagerReply;
import com.winjune.wifiindoor.webservice.types.NaviInfoReply;
import com.winjune.wifiindoor.webservice.types.QueryInfo;
import com.winjune.wifiindoor.webservice.types.TestLocateCollectReply;

/**
 * @author ezhipin
 * 
 */
public class IpsTransportServiceThread extends Thread {
	public static final String TAG = "IpsTransportServiceThread";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private TransportServiceListener mTransportServiceListener;
	private Context paramContext;
	
	private boolean mIsThreadRunToCompletion;

	public IpsTransportServiceThread(TransportServiceListener listener, Context paramContext) {
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

		try {
			switch (requestCode) {
			case IpsMsgConstants.MT_COLLECT:
				mTransportServiceListener.onStartingRequest();
				IpsMessageHandler.collect(requestPayload);
				mTransportServiceListener.onFinishingRequest();

				break;
			
			case IpsMsgConstants.MT_LOCATE:
				mTransportServiceListener.onStartingRequest();
				LocationSet locations = IpsMessageHandler.locate(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(locations);

				break;
				
			case IpsMsgConstants.MT_INFO_QUERY:
				mTransportServiceListener.onStartingRequest();
				QueryInfo info = IpsMessageHandler.query(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(info);

				break;	
				
			case IpsMsgConstants.MT_EDIT_NFC_QR:
				mTransportServiceListener.onStartingRequest();			
				IpsMessageHandler.collectNfc(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				
				break;
				
			case IpsMsgConstants.MT_LOCATE_FROM_NFC_QR:
				mTransportServiceListener.onStartingRequest();
				Location nfcLocation = IpsMessageHandler.locateBaseOnNfc(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(nfcLocation);
				
				break;
				
			case IpsMsgConstants.MT_DELETE_FINGERPRINT:
				mTransportServiceListener.onStartingRequest();
				IpsMessageHandler.deleteFingerprint(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				
				break;	
			
			case IpsMsgConstants.MT_LOCATE_TEST:
				mTransportServiceListener.onStartingRequest();
				TestLocateCollectReply testLocation = IpsMessageHandler.locateTest(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(testLocation);

				break;	
			
			case IpsMsgConstants.MT_COLLECT_TEST:
				mTransportServiceListener.onStartingRequest();
				TestLocateCollectReply testLocation2 = IpsMessageHandler.collectTest(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(testLocation2);

				break;	
								
			case IpsMsgConstants.MT_APK_VERSION_QUERY:
				mTransportServiceListener.onStartingRequest();
				ApkVersionReply version = IpsMessageHandler.queryApkVersion(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(version);

				break;	
			
			case IpsMsgConstants.MT_BUILDING_QUERY:
				mTransportServiceListener.onStartingRequest();
				BuildingManagerReply buildingManager = IpsMessageHandler.queryBuilding(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(buildingManager);
				break;
				
			case IpsMsgConstants.MT_MAP_LIST_QUERY:
				mTransportServiceListener.onStartingRequest();
				MapManagerReply mapManager = IpsMessageHandler.queryMaps(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(mapManager);
				break;
				
			case IpsMsgConstants.MT_MAP_QUERY:
				mTransportServiceListener.onStartingRequest();
				IndoorMapReply indoorMap = IpsMessageHandler.queryMap(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(indoorMap);
				break;
			
			case IpsMsgConstants.MT_MAP_INFO_QUERY:
				mTransportServiceListener.onStartingRequest();
				MapInfoReply mapInfo = IpsMessageHandler.queryMapInfo(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(mapInfo);
				break;
				
			case IpsMsgConstants.MT_NAVI_INFO_QUERY:
				mTransportServiceListener.onStartingRequest();
				NaviInfoReply naviInfo = IpsMessageHandler.queryNaviInfo(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(naviInfo);
				break;
				
			case IpsMsgConstants.MT_AD_INFO_QUERY:
				mTransportServiceListener.onStartingRequest();
				AdGroup advertiseList = IpsMessageHandler.queryAdvertiseInfo(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(advertiseList);
				break;		
				
			case IpsMsgConstants.MT_INTEREST_PLACES_QUERY:
				mTransportServiceListener.onStartingRequest();
				InterestPlacesInfoReply interestPlacesInfo = IpsMessageHandler.queryInterestPlacesInfo(requestPayload);
				mTransportServiceListener.onFinishingRequest();
				mTransportServiceListener.onResponseReceived(interestPlacesInfo);
				break;	
				
			default:
				mTransportServiceListener
						.onError("Do not support request code " + requestCode
								+ ".");

				break;
			}
		} catch (WebCredentialsException e) {
			// TODO Auto-generated catch block
			mTransportServiceListener.onError("051 " + e.toString());
			e.printStackTrace();
		} catch (WebError e) {
			// TODO Auto-generated catch block
			mTransportServiceListener.onError("052 " + e.toString());
			e.printStackTrace();
		} catch (WebException e) {
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
