/**
 * @(#)OutgoingMessageQueue.java
 * Apr 4, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.webservice;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.json.JSONObject;

/**
 * @author ezhipin
 * 
 */
public class OutgoingMessageQueue {
	private static final int MAX_SIZE = 200;
	private static BlockingQueue<JSONObject> mQueue = new ArrayBlockingQueue<JSONObject>(
			MAX_SIZE);

	private static Object mAccessLock = new Object();

	public static boolean offer(JSONObject data) {
		synchronized (mAccessLock) {
			return mQueue.offer(data);
		}
	}

	public static JSONObject take() {
		try {
			synchronized (mAccessLock) {
				return mQueue.take();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isEmpty() {
		return mQueue.isEmpty();

	}
}
