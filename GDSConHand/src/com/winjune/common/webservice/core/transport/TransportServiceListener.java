/**
 * 
 */
package com.winjune.common.webservice.core.transport;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.winjune.common.webservice.core.config.WebCoreConfig;
import com.winjune.common.webservice.core.types.IType;

/**
 * 
 */
public class TransportServiceListener implements ITransportServiceListener {
	public static final String TAG = TransportServiceListener.class.toString();
	public static final boolean DEBUG = WebCoreConfig.DEBUG;

	private Handler handler;

	public TransportServiceListener(Handler handler) {
		this.handler = handler;
	}

	/*
	 *
	 */
	@Override
	public void onStartingRequest() {
		// TODO Auto-generated method stub

		if (this.handler != null) {
			this.handler.obtainMessage(HandlerConstants.HANDLER_STARTING_REQUEST)
					.sendToTarget();
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

		if (this.handler != null) {
			this.handler.obtainMessage(HandlerConstants.HANDLER_FINISHING_REQUEST)
					.sendToTarget();
		}
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
		if (this.handler != null) {
			ResponseBlockingQueue.put(object);

			this.handler.obtainMessage(HandlerConstants.HANDLER_RESPONSE_RECEIVED)
					.sendToTarget();
		}
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
		if (this.handler != null) {
			Message message = this.handler
					.obtainMessage(HandlerConstants.HANDLER_ERROR_REPORTED);
			Bundle bundle = new Bundle();
			bundle.putString("error", error);
			message.setData(bundle);

			message.sendToTarget();
		}
	}

}
