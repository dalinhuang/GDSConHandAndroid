/**
 * @(#)MainTransportServiceListener.java
 * Jun 3, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiindoor.webservice.TransportServiceThread.TransportServiceListener;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

/**
 * @author ezhipin
 * 
 */
public class MainTransportServiceListener implements TransportServiceListener {
	public static final String TAG = "MainTransportServiceListener";
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	private Handler mHandler;

	public MainTransportServiceListener(Handler handler) {
		mHandler = handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wifiindoor.webservice.TransportServiceThread.
	 * TransportServiceListener#onError(java.lang.String)
	 */
	@Override
	public void onError(String error) {
		// TODO Auto-generated method stub
		if (mHandler != null) {
			Message message = mHandler
					.obtainMessage(IndoorMapData.HANDLER_ERROR_REPORTED);
			Bundle bundle = new Bundle();
			bundle.putString("error", error);
			message.setData(bundle);

			message.sendToTarget();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wifiindoor.webservice.TransportServiceThread.
	 * TransportServiceListener#onFinishingRequest()
	 */
	@Override
	public void onFinishingRequest() {
		// TODO Auto-generated method stub

		if (mHandler != null)
			mHandler.obtainMessage(IndoorMapData.HANDLER_FINISHING_REQUEST)
					.sendToTarget();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wifiindoor.webservice.TransportServiceThread.
	 * TransportServiceListener
	 * #onResponseReceived(com.ericsson.cgc.aurora.wifiindoor
	 * .webservice.types.IType)
	 */
	@Override
	public void onResponseReceived(IType object) {
		// TODO Auto-generated method stub
		if (mHandler != null) {
			ResponseBlockingQueue.put(object);

			mHandler.obtainMessage(IndoorMapData.HANDLER_RESPONSE_RECEIVED)
					.sendToTarget();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ericsson.cgc.aurora.wifiindoor.webservice.TransportServiceThread.
	 * TransportServiceListener#onStartingRequest()
	 */
	@Override
	public void onStartingRequest() {
		// TODO Auto-generated method stub

		if (mHandler != null)
			mHandler.obtainMessage(IndoorMapData.HANDLER_STARTING_REQUEST)
					.sendToTarget();
	}

}