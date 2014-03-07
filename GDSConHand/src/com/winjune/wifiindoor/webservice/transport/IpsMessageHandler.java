package com.winjune.wifiindoor.webservice.transport;

import com.winjune.common.webservice.core.transport.ResponseBlockingQueue;
import com.winjune.common.webservice.core.types.IType;
import com.winjune.wifiindoor.activity.GMapEntryActivity;
import com.winjune.wifiindoor.activity.MapLocatorActivity;
import com.winjune.wifiindoor.activity.MapSelectorActivity;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.activity.MenuEntryActivity;
import com.winjune.wifiindoor.ads.AdGroup;
import com.winjune.wifiindoor.mapviewer.AdBanner;
import com.winjune.wifiindoor.mapviewer.InfoBanner;
import com.winjune.wifiindoor.mapviewer.InterestPlaceBar;
import com.winjune.wifiindoor.mapviewer.LabelBar;
import com.winjune.wifiindoor.mapviewer.LocateBar;
import com.winjune.wifiindoor.mapviewer.NaviBar;
import com.winjune.wifiindoor.mapviewer.PlanBar;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.version.ApkVersionManager;
import com.winjune.wifiindoor.webservice.transport.IpsTransportServiceListener;
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
	private static IpsTransportServiceThread mTransportServiceThread;

	public static void setActivity(Activity activity) {
		Log.e("IpsMessageHandler", activity.toString());
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
				locator.connectionFailed();
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
			
			if (activity instanceof MenuEntryActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				MenuEntryActivity entry = (MenuEntryActivity) activity;
				entry.updateLocation(locations);

				return;
			}

			if (activity instanceof MapViewerActivity) {
				//Log.e("IpsMessageHandler", "viewer.updateLocation");
				MapViewerActivity viewer = (MapViewerActivity) activity;
				LocateBar.updateLocation(viewer, locations);

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
			
			if (activity instanceof MenuEntryActivity) {
				//Log.e("IpsMessageHandler", "entry.updateLocation");
				MenuEntryActivity entry = (MenuEntryActivity) activity;
				entry.updateLocation(location);

				return;
			}

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
				LabelBar.setMapInfo(mapInfo.toMapInfo());
				LabelBar.showMapInfo(viewer1, true);

				return;
			}
			
			return;
		}
		
		if (object instanceof NaviInfoReply) {
			NaviInfoReply naviInfo = (NaviInfoReply) object;			
			
			if (activity instanceof MapViewerActivity) {
				MapViewerActivity viewer1 = (MapViewerActivity) activity;
				
				NaviBar.setNaviInfo(viewer1, naviInfo.toNaviInfo());

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
		
		if (object instanceof InterestPlacesInfoReply) {
			InterestPlacesInfoReply interestPlacesInfo = (InterestPlacesInfoReply) object;			
			
			if (activity instanceof MapViewerActivity) {
				MapViewerActivity viewer1 = (MapViewerActivity) activity;
				
				InterestPlaceBar.showInterestPlacesInfo(viewer1, interestPlacesInfo.toInterestPlacesInfo(), true);

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
}
