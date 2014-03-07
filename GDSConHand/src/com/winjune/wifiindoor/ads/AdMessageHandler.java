package com.winjune.wifiindoor.ads;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.winjune.common.webservice.core.transport.ResponseBlockingQueue;
import com.winjune.common.webservice.core.types.IType;
import com.winjune.wifiindoor.util.AdData;

public class AdMessageHandler {

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AdData.HANDLER_ADVERTISE_REQUEST:
				handleAdvertiseStartingRequest();
				break;
			case AdData.HANDLER_ADVERTISE_RESPONSE:
				handleAdvertiseFinishingRequest();
				break;

			default:
				break;
			}
		}
	};
	
	private void handleAdvertiseFinishingRequest() {
		// dismissProgressDialog();
	}
	
	private void handleAdvertiseStartingRequest() {
		// dismissProgressDialog();
		
		IType object = ResponseBlockingQueue.take();
		handleAdMessage(object);
	}
	
	private void handleAdMessage(IType object) {
		
	}

}
