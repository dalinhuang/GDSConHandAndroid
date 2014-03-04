package com.winjune.wifiindoor.webservice;

import com.winjune.wifiindoor.EntryActivity;
import com.winjune.wifiindoor.GMapEntryActivity;
import com.winjune.wifiindoor.MapLocatorActivity;
import com.winjune.wifiindoor.MapSelectorActivity;
import com.winjune.wifiindoor.MapViewerActivity;
import com.winjune.wifiindoor.ads.AdGroup;
import com.winjune.wifiindoor.mapviewer.AdBanner;
import com.winjune.wifiindoor.mapviewer.InfoBanner;
import com.winjune.wifiindoor.mapviewer.InterestPlaceBar;
import com.winjune.wifiindoor.mapviewer.LabelBar;
import com.winjune.wifiindoor.mapviewer.NaviBar;
import com.winjune.wifiindoor.types.ApkVersionReply;
import com.winjune.wifiindoor.types.BuildingManagerReply;
import com.winjune.wifiindoor.types.IndoorMapReply;
import com.winjune.wifiindoor.types.InterestPlacesInfoReply;
import com.winjune.wifiindoor.types.Location;
import com.winjune.wifiindoor.types.LocationSet;
import com.winjune.wifiindoor.types.MapInfoReply;
import com.winjune.wifiindoor.types.MapManagerReply;
import com.winjune.wifiindoor.types.NaviInfoReply;
import com.winjune.wifiindoor.types.QueryInfo;
import com.winjune.wifiindoor.types.TestLocateCollectReply;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.MainTransportServiceListener;
import com.winjune.wifiindoor.webservice.types.IType;

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
				
				LabelBar.showMapInfo(viewer1, mapInfo.toMapInfo(), true);

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
