package com.winjune.wifiindoor.webservice.transport;

import java.io.IOException;

import org.json.JSONObject;

import com.winjune.common.webservice.core.error.WebCredentialsException;
import com.winjune.common.webservice.core.error.WebError;
import com.winjune.common.webservice.core.error.WebException;
import com.winjune.common.webservice.core.transport.ResponseBlockingQueue;
import com.winjune.common.webservice.core.types.IType;
import com.winjune.common.webservice.core.types.Test;
import com.winjune.wifiindoor.activity.MapLocatorActivity;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.activity.mapviewer.AdBanner;
import com.winjune.wifiindoor.activity.mapviewer.CollectedFlag;
import com.winjune.wifiindoor.activity.mapviewer.InfoBanner;
import com.winjune.wifiindoor.activity.mapviewer.LocateBar;
import com.winjune.wifiindoor.activity.mapviewer.NaviBar;
import com.winjune.wifiindoor.activity.mapviewer.PlanBar;
import com.winjune.wifiindoor.ads.AdGroup;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.version.ApkVersionManager;
import com.winjune.wifiindoor.webservice.http.IpsHttpApi;
import com.winjune.wifiindoor.webservice.transport.IpsTransportServiceListener;
import com.winjune.wifiindoor.webservice.types.ApkVersionReply;
import com.winjune.wifiindoor.webservice.types.BuildingManagerReply;
import com.winjune.wifiindoor.webservice.types.CollectStatusReply;
import com.winjune.wifiindoor.webservice.types.IndoorMapReply;
import com.winjune.wifiindoor.webservice.types.Location;
import com.winjune.wifiindoor.webservice.types.LocationSet;
import com.winjune.wifiindoor.webservice.types.MapManagerReply;
import com.winjune.wifiindoor.webservice.types.QueryInfo;
import com.winjune.wifiindoor.webservice.types.TestLocateCollectReply;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class IpsMessageHandler {
	private static Activity activity;
	public static IpsHttpApi mWifiIpsHttpApi;
	private static IpsTransportServiceThread mTransportServiceThread;

	public static boolean initialize(String domain, String clientVersion) {
		
		mWifiIpsHttpApi = new IpsHttpApi(domain);
		
		if (mWifiIpsHttpApi != null)
			return mWifiIpsHttpApi.initIpsHttpClient(domain, clientVersion);		

		return false;
	}
	
	public static boolean reInitialize(String domain, String clientVersion) {
		IpsMessageHandler.mWifiIpsHttpApi = null;
		return initialize(domain, clientVersion);
	}
	
	public static void setActivity(Activity activity) {
		Log.i("IpsMessageHandler", activity.toString());
		IpsMessageHandler.activity = activity;
	}

	@SuppressLint("HandlerLeak")
	private static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case IndoorMapData.HANDLER_STARTING_REQUEST:
				handleStartingRequest();
				break;
			case IndoorMapData.HANDLER_FINISHING_REQUEST:
				handleFinishingRequest();
				break;
			case IndoorMapData.HANDLER_RESPONSE_RECEIVED:
				handleResponseReceived();
				break;
			case IndoorMapData.HANDLER_ERROR_REPORTED:
				Bundle bundle = msg.getData();
				String error = bundle.getString("error");
				handleErrorReported(error);
				break;
			default:
				break;
			}
		}
	};

	private static void handleStartingRequest() {
		// showProgressDialog();
	}

	private static void handleFinishingRequest() {
		// dismissProgressDialog();
	}

	private static void handleResponseReceived() {
		IType object = ResponseBlockingQueue.take();

		handleMessage(object);
	}

	private static void handleErrorReported(String error) {
		if ((error != null) && !TextUtils.isEmpty(error)) {
			Util.showToast(activity, error, Toast.LENGTH_LONG);
		}

		if (activity instanceof MapLocatorActivity) {
				MapLocatorActivity locator = (MapLocatorActivity) activity;
				locator.enterDefaultMap();
		}
	}

	private static void handleMessage(IType object) {
		if (object == null) {
			return;
		}
		
		// TODO: sometimes the message is from Activity A but can be handled by Activity B if:
		// 1. both activities can handle this message Type
		// 2. Request was sent by A
		// 3. B came into Foreground
		
		// TODO: sometimes the message is handled in background when:
		// 1. the current foreground Activity B is not register itself into this IpsMessageHandler
		// 2. The background Activity A is still registered and the Request was sent from A 
		
		if (object instanceof Location) {
			Location location = (Location) object;
			
			if (activity instanceof MapViewerActivity) {
				//Log.e("IpsMessageHandler", "viewer.updateLocation");
				MapViewerActivity viewer = (MapViewerActivity) activity;
				LocateBar.updateLocation(viewer, location);

				return;
			}
			
			return;
		} 
		
		if (object instanceof QueryInfo) {
			QueryInfo queryInfo = (QueryInfo) object;			
			
			if (activity instanceof MapViewerActivity) {
				//Log.e("IpsMessageHandler", "viewer.updateLocation");
				MapViewerActivity viewer1 = (MapViewerActivity) activity;
				
				InfoBanner.showInfo(viewer1, queryInfo);

				return;
			}
			
			return;
		}
		
		if (object instanceof TestLocateCollectReply) {
			TestLocateCollectReply testLocation = (TestLocateCollectReply) object;			
			
			if (activity instanceof MapViewerActivity) {
				//Log.e("IpsMessageHandler", "viewer.updateLocation");
				MapViewerActivity viewer = (MapViewerActivity) activity;
				PlanBar.handleTestReply(viewer, testLocation);

				return;
			}
			
			return;
		}  
		
		if (object instanceof ApkVersionReply) {
			ApkVersionReply version = (ApkVersionReply) object;
			

			ApkVersionManager.handleApkVersionReply(activity, version);
						
			return;
		} 
		
		if (object instanceof BuildingManagerReply) {
			BuildingManagerReply manager = (BuildingManagerReply) object;
			
			return;
		}
		
		if (object instanceof MapManagerReply) {
			MapManagerReply manager = (MapManagerReply) object;
			
			if (activity instanceof MapLocatorActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				MapLocatorActivity locator = (MapLocatorActivity) activity;
				locator.handleMapListReply(manager);

				return;
			}			
			
			return;
		} 		
				
		if (object instanceof IndoorMapReply) {
			IndoorMapReply map = (IndoorMapReply) object;
			
			if (activity instanceof MapLocatorActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				MapLocatorActivity locator = (MapLocatorActivity) activity;
				locator.handleMapReply(map);

				return;
			}
			
			return;
		}
					
		if (object instanceof AdGroup) {
			AdGroup advertiseList = (AdGroup) object;			
			
			if (activity instanceof MapViewerActivity) {
				//Log.e("IpsMessageHandler", "viewer.updateLocation");
				MapViewerActivity viewer1 = (MapViewerActivity) activity;
				
				AdBanner.checkAndDownloadAd(viewer1, advertiseList);

				return;
			}
			
			return;
		}
	
		
		if (object instanceof CollectStatusReply) {
			CollectStatusReply collectStatus = (CollectStatusReply) object;			
			
			if (activity instanceof MapViewerActivity) {
				MapViewerActivity viewer1 = (MapViewerActivity) activity;
				
				CollectedFlag.showCollectedFlag(viewer1, collectStatus.toMapCollectStatus());

				return;
			}
			
			return;
		}

	}
			

	public static void startTransportServiceThread() {
		Log.e("IpsMessageHandler", "Start IpsMessageHandler!");
		
		if (mTransportServiceThread == null) {
			IpsTransportServiceListener listener = new IpsTransportServiceListener(mHandler);
			mTransportServiceThread = new IpsTransportServiceThread(listener,
					activity.getApplicationContext());
			mTransportServiceThread.setName("TransportService");

			mTransportServiceThread.start();

			Log.e("IpsMessageHandler", "IpsMessageHandler Started!");

		} else {

			// Returns true if the receiver has already been started and
			// still runs code (hasn't died yet). Returns false either if
			// the receiver hasn't been started yet or if it has already
			// started and run to completion and died.
			//
			// Only start thread when it haven't started.
			if (!mTransportServiceThread.isAlive()) {
				if (mTransportServiceThread.isThreadRunToCompletion()) {
					// can not start the old thread again
					// just new a same thread
					IpsTransportServiceListener listener = new IpsTransportServiceListener(mHandler);
					mTransportServiceThread = new IpsTransportServiceThread(
							listener, activity.getApplicationContext());
					mTransportServiceThread.setName("TransportService");
					mTransportServiceThread.start();
					Log.e("IpsMessageHandler",
							"IpsMessageHandler ReStarted from Complete!");
				} else {
					mTransportServiceThread.start();
					Log.e("IpsMessageHandler", "IpsMessageHandler ReStarted!");
				}
			} else {
				Log.e("IpsMessageHandler", "IpsMessageHandler Already Started!");
			}
		}
	}
	
	@V1
	public static Test testPostXml(String parameter) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.testPostXml(parameter);
	}

	/*
	 * 
	 */
	@V1
	public static Test testPostJson(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.testPostJson(json);
	}

	/*
	 * collect, no response
	 */
	@V1
	public static LocationSet locate(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.locate(json);
	}

	/*
	 * locate
	 */
	@V1
	public static void collect(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		mWifiIpsHttpApi.collect(json);
	}
	
	/*
	 * query
	 */
	@V1
	public static QueryInfo query(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.query(json);
	}
	
	/*
	 * collectNfc
	 */
	@V1
	public static void collectNfc(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		mWifiIpsHttpApi.collectNfc(json);
	}

	/*
	 * locateBaseOnNfc
	 */
	@V1
	public static Location locateBaseOnNfc(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.locateBaseOnNfc(json);
	}
	
	/*
	 * deleteFingerprint, no response
	 */
	@V1
	public static void deleteFingerprint(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		mWifiIpsHttpApi.deleteFingerprint(json);
	}
	
	/*
	 * locate_test
	 */
	@V1
	public static TestLocateCollectReply locateTest(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.locateTest(json);
	}
	
	/*
	 * collect_test
	 */
	@V1
	public static TestLocateCollectReply collectTest(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.collectTest(json);
	}
	
	/*
	 * update Apk
	 */
	@V1
	public static ApkVersionReply queryApkVersion(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryApkVersion(json);
	}
	
	/*
	 * update Buildings
	 */
	@V1
	public static BuildingManagerReply queryBuilding(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryBuilding(json);
	}
	
	/*
	 * update mapList
	 */
	@V1
	public static MapManagerReply queryMaps(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryMaps(json);
	}
	
	/*
	 * update Map
	 */
	@V1
	public static IndoorMapReply queryMap(JSONObject json) throws WebException,
			WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
			throw new WebException("IPS HTTP API is null!");			
		}
		
		return mWifiIpsHttpApi.queryMap(json);
	}
		
	/*
	 * update advertise Info
	 */
	@V1
	public static  AdGroup queryAdvertiseInfo(JSONObject json) throws WebException,
	        WebCredentialsException, WebError, IOException {

        if (mWifiIpsHttpApi == null) {			
	        throw new WebException("IPS HTTP API is null!");			
        }

        return mWifiIpsHttpApi.queryAdvertiseInfo(json);
    }
	
	/*
	 * update collected flags
	 */
	@V1
	public static CollectStatusReply queryCollectStatus(JSONObject json) throws WebException,
		WebCredentialsException, WebError, IOException {
		
		if (mWifiIpsHttpApi == null) {			
	        throw new WebException("IPS HTTP API is null!");			
        }

        return mWifiIpsHttpApi.queryCollectStatus(json);
	}
	
	/**
	 * This api is supported in the V1 API
	 */
	@interface V1 {
	}
	
	
}
