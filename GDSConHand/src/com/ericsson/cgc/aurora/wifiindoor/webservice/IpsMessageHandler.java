package com.ericsson.cgc.aurora.wifiindoor.webservice;

import com.ericsson.cgc.aurora.wifiindoor.EntryActivity;
import com.ericsson.cgc.aurora.wifiindoor.GMapEntryActivity;
import com.ericsson.cgc.aurora.wifiindoor.MapLocatorActivity;
import com.ericsson.cgc.aurora.wifiindoor.MapSelectorActivity;
import com.ericsson.cgc.aurora.wifiindoor.MapViewerActivity;
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
import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;
import com.ericsson.cgc.aurora.wifiindoor.webservice.MainTransportServiceListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class IpsMessageHandler {
	private Activity activity;
	private TransportServiceThread mTransportServiceThread;

	public void setActivity(Activity activity) {
		Log.e("IpsMessageHandler", activity.toString());
		this.activity = activity;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
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

	private void handleStartingRequest() {
		// showProgressDialog();
	}

	private void handleFinishingRequest() {
		// dismissProgressDialog();
	}

	private void handleResponseReceived() {
		IType object = ResponseBlockingQueue.take();

		handleMessage(object);
	}

	private void handleErrorReported(String error) {
		if ((error != null) && !TextUtils.isEmpty(error)) {
			Util.showToast(activity, error, Toast.LENGTH_LONG);
		}

		if (activity instanceof MapLocatorActivity) {
				MapLocatorActivity locator = (MapLocatorActivity) activity;
				locator.connectionFailed();
		}
	}

	private void handleMessage(IType object) {
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

		if (object instanceof LocationSet) {
			LocationSet locations = (LocationSet) object;

			if (activity instanceof MapLocatorActivity) {
				//Log.e("IpsMessageHandler", "locator.updateLocation");
				MapLocatorActivity locator = (MapLocatorActivity) activity;
				locator.updateLocation(locations);

				return;
			}
			
			if (activity instanceof GMapEntryActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				GMapEntryActivity entry = (GMapEntryActivity) activity;
				entry.updateLocation(locations);

				return;
			}
			
			if (activity instanceof EntryActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				EntryActivity entry = (EntryActivity) activity;
				entry.updateLocation(locations);

				return;
			}

			if (activity instanceof MapViewerActivity) {
				//Log.e("IpsMessageHandler", "viewer.updateLocation");
				MapViewerActivity viewer = (MapViewerActivity) activity;
				viewer.updateLocation(locations);

				return;
			}
			
			return;
		} 
		
		if (object instanceof Location) {
			Location location = (Location) object;
			
			if (activity instanceof GMapEntryActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				GMapEntryActivity entry = (GMapEntryActivity) activity;
				entry.updateLocation(location);

				return;
			}
			
			if (activity instanceof EntryActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				EntryActivity entry = (EntryActivity) activity;
				entry.updateLocation(location);

				return;
			}

			if (activity instanceof MapViewerActivity) {
				//Log.e("IpsMessageHandler", "viewer.updateLocation");
				MapViewerActivity viewer = (MapViewerActivity) activity;
				viewer.updateLocation(location);

				return;
			}
			
			return;
		} 
		
		if (object instanceof QueryInfo) {
			QueryInfo queryInfo = (QueryInfo) object;			
			
			if (activity instanceof MapViewerActivity) {
				//Log.e("IpsMessageHandler", "viewer.updateLocation");
				MapViewerActivity viewer1 = (MapViewerActivity) activity;
				
				viewer1.showInfo(queryInfo);

				return;
			}
			
			return;
		}
		
		if (object instanceof TestLocateCollectReply) {
			TestLocateCollectReply testLocation = (TestLocateCollectReply) object;			
			
			if (activity instanceof MapViewerActivity) {
				//Log.e("IpsMessageHandler", "viewer.updateLocation");
				MapViewerActivity viewer = (MapViewerActivity) activity;
				viewer.handleTestReply(testLocation);

				return;
			}
			
			return;
		}
		
		if (object instanceof ApkVersionReply) {
			ApkVersionReply version = (ApkVersionReply) object;
			
			if (activity instanceof GMapEntryActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				GMapEntryActivity entry = (GMapEntryActivity) activity;
				entry.handleApkVersionReply(version);

				return;
			}
			
			if (activity instanceof EntryActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				EntryActivity entry = (EntryActivity) activity;
				entry.handleApkVersionReply(version);

				return;
			}
			
			return;
		} 
		
		if (object instanceof BuildingManagerReply) {
			BuildingManagerReply manager = (BuildingManagerReply) object;
			
			if (activity instanceof GMapEntryActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				GMapEntryActivity entry = (GMapEntryActivity) activity;
				entry.handleBuildingReply(manager);

				return;
			}
			
			return;
		} 
		
		if (object instanceof MapManagerReply) {
			MapManagerReply manager = (MapManagerReply) object;
			
			if (activity instanceof MapSelectorActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				MapSelectorActivity selector = (MapSelectorActivity) activity;
				selector.handleMapListReply(manager);

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
		
		if (object instanceof MapInfoReply) {
			MapInfoReply mapInfo = (MapInfoReply) object;			
			
			if (activity instanceof MapViewerActivity) {
				MapViewerActivity viewer1 = (MapViewerActivity) activity;
				
				viewer1.showMapInfo(mapInfo.toMapInfo(), true);

				return;
			}
			
			return;
		}
		
		if (object instanceof NaviInfoReply) {
			NaviInfoReply naviInfo = (NaviInfoReply) object;			
			
			if (activity instanceof MapViewerActivity) {
				MapViewerActivity viewer1 = (MapViewerActivity) activity;
				
				viewer1.setNaviInfo(naviInfo.toNaviInfo());

				return;
			}
			
			return;
		}
		
		if (object instanceof AdGroup) {
			AdGroup advertiseList = (AdGroup) object;			
			
			if (activity instanceof MapViewerActivity) {
				//Log.e("IpsMessageHandler", "viewer.updateLocation");
				MapViewerActivity viewer1 = (MapViewerActivity) activity;
				
				viewer1.checkAndDownloadAd(advertiseList);

				return;
			}
			
			return;
		}
		
		if (object instanceof InterestPlacesInfoReply) {
			InterestPlacesInfoReply interestPlacesInfo = (InterestPlacesInfoReply) object;			
			
			if (activity instanceof MapViewerActivity) {
				MapViewerActivity viewer1 = (MapViewerActivity) activity;
				
				viewer1.showInterestPlacesInfo(interestPlacesInfo.toInterestPlacesInfo(), true);

				return;
			}
			
			return;
		}

	}

	public void startTransportServiceThread() {
		Log.e("IpsMessageHandler", "Start IpsMessageHandler!");
		if (mTransportServiceThread == null) {
			MainTransportServiceListener listener = new MainTransportServiceListener(mHandler);
			mTransportServiceThread = new TransportServiceThread(listener,
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
					MainTransportServiceListener listener = new MainTransportServiceListener(mHandler);
					mTransportServiceThread = new TransportServiceThread(
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
}
