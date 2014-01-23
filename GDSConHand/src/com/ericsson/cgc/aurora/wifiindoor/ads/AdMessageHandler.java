package com.ericsson.cgc.aurora.wifiindoor.ads;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.ericsson.cgc.aurora.wifiindoor.util.AdData;
import com.ericsson.cgc.aurora.wifiindoor.webservice.ResponseBlockingQueue;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

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
