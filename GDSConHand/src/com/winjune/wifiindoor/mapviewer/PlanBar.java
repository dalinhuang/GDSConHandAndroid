package com.winjune.wifiindoor.mapviewer;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.andengine.entity.primitive.Rectangle;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

import com.google.gson.Gson;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.transport.MsgConstants;
import com.winjune.wifiindoor.webservice.types.CollectInfo;
import com.winjune.wifiindoor.webservice.types.Location;
import com.winjune.wifiindoor.webservice.types.LocationSet;
import com.winjune.wifiindoor.webservice.types.NfcLocation;
import com.winjune.wifiindoor.webservice.types.TestLocateCollectReply;
import com.winjune.wifiindoor.webservice.types.TestLocateCollectRequest;
import com.winjune.wifiindoor.wifi.WifiFingerPrint;

public class PlanBar {

	// send the NFC tagId + [MapID,X,Y] to server, so the Fine Location against
	// this NFC can be stored/updated
	public static void editNfcQrTagInMap(MapViewerActivity mapViewer, String tagId) {
		// send Nfc/Qr Locate messsage to server
		NfcLocation nfcLoc = new NfcLocation(tagId,
				Util.getRuntimeIndoorMap().getMapId(), 
				mapViewer.mTargetColNo, mapViewer.mTargetRowNo, 
				Util.getRuntimeIndoorMap().getVersionCode());

		//Util.showShortToast(this, R.string.store_nfc_info_into_map);
		MapHUD.updateHinText(mapViewer, R.string.store_nfc_info_into_map);

		try {
			Gson gson = new Gson();
			String json = gson.toJson(nfcLoc);
			JSONObject data = new JSONObject(json);

			if (Util.sendToServer(mapViewer, MsgConstants.MT_EDIT_NFC_QR, data)) {
				//Util.showShortToast(this, R.string.nfc_info_stored);
				MapHUD.updateHinText(mapViewer, R.string.nfc_info_stored);
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			//Util.showToast(MapViewerActivity.this, "NFC02 " + ex.toString(), Toast.LENGTH_LONG);
			ex.printStackTrace();
			MapHUD.updateHinText(mapViewer, "NFC: 002 ERROR: " + ex.getMessage());
		}
	}
	
	public static void addNfcQrLocation(MapViewerActivity mapViewer) {
		// Not return when no NFC, since we can support Camera for QR Code
		if (!Util.getNfcInfoManager().isNfcEmbeded()) {
			//Util.showLongToast(this, R.string.no_nfc_embeded);
			MapHUD.updateHinText(mapViewer, R.string.no_nfc_embeded);
		} else {
			if (!Util.getNfcInfoManager().isNfcEnabled()) {
				//Util.showLongToast(this, R.string.no_nfc_enabled);
				MapHUD.updateHinText(mapViewer, R.string.no_nfc_enabled);
			}
		}

		if (mapViewer.mNfcEditState == IndoorMapData.NFC_EDIT_STATE_SCANNING) {
			// Cancel both last one and this one
			mapViewer.mNfcEditState = IndoorMapData.NFC_EDIT_STATE_NULL;
			//Util.showLongToast(this, R.string.last_nfc_scan_ongoing_failure);
			MapHUD.updateHinText(mapViewer, R.string.last_nfc_scan_ongoing_failure);
			return;
		}

		mapViewer.mNfcEditState = IndoorMapData.NFC_EDIT_STATE_SCANNING;
		//Util.showLongToast(this, R.string.start_scan_nfc);
		MapHUD.updateHinText(mapViewer, R.string.start_scan_nfc);
	}	
	
	public static void collectFingerprint(final MapViewerActivity mapViewer, final boolean silent) {
		if (mapViewer.DEBUG)
			Log.d(mapViewer.TAG, "Start collectFingerprint");

		if (!Util.getWifiInfoManager().isWifiEmbeded()) {
			if (!silent)
				//Util.showLongToast(this, R.string.no_wifi_embeded);
				MapHUD.updateHinText(mapViewer, R.string.no_wifi_embeded);
			return;
		}

		if (!Util.getWifiInfoManager().isWifiEnabled()) {
			if (!silent)
				//Util.showLongToast(this, R.string.no_wifi_enabled);
				MapHUD.updateHinText(mapViewer, R.string.no_wifi_enabled);
			return;
		}
		
		if (!silent)
			//Util.showShortToast(this, R.string.collecting);
			MapHUD.updateHinText(mapViewer, R.string.collecting);
		
		if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER) {
			if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {			
				CollectInfo collect = new CollectInfo();
				Location location = new Location(Util.getRuntimeIndoorMap().getMapId(), 
						mapViewer.mTargetColNo, mapViewer.mTargetRowNo, 
						Util.getRuntimeIndoorMap().getVersionCode());
				collect.setLocation(location);
	
				WifiFingerPrint fingnerPrint = Util.getWifiInfoManager().mergeSamples();
				fingnerPrint.log();
				collect.setWifiFingerPrint(fingnerPrint);
	
				try {
					Gson gson = new Gson();
					String json = gson.toJson(collect);
					JSONObject data = new JSONObject(json);
	
					if (Util.sendToServer(mapViewer, MsgConstants.MT_COLLECT, data)) {
						// For test purpose, display the [x,y]
						if (!silent) 
							MapHUD.updateHinText(mapViewer, mapViewer.getResources().getString(R.string.collected)
									+ " ["
									+ mapViewer.mTargetColNo
									+ ","
									+ mapViewer.mTargetRowNo + "]");
							/*
							Util.showToast(this,
									getResources().getString(R.string.collected)
											+ " ["
											+ x
											+ ","
											+ y + "]",
									Toast.LENGTH_LONG);
									*/
	
						Log.e("COLLECT", "Send MT_COLLECT to Server: TRUE");
						
						addCollectedFlag(mapViewer, mapViewer.mTargetColNo, mapViewer.mTargetRowNo); // add and show the flags
					} else {
						// All errors should be handled in the sendToServer
						// method
					}
	
				} catch (Exception ex) {
					//Util.showToast(this, "102 " + ex.toString(), Toast.LENGTH_LONG);
					ex.printStackTrace();
					MapHUD.updateHinText(mapViewer, "COLLECT: 102 ERROR: " + ex.getMessage());
				}
				
				return;
			} // if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
		} // if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER) {
		
		
		// No enough buffered data or buffer not used, use a thread
		
		// To pass the x, y to our new Thread
		if ((mapViewer.currentCollectingX != -1) || (mapViewer.currentCollectingX != -1)) {
			if (!silent)
				//Util.showShortToast(this, R.string.collect_ongoing_failure);
				MapHUD.updateHinText(mapViewer, R.string.collect_ongoing_failure);
			return;
		}

		if (LocateBar.isCollectingOnGoing(mapViewer)) {
			if (!silent)
				MapHUD.updateHinText(mapViewer, R.string.another_collect_ongoing);
				//Util.showShortToast(this, R.string.another_collect_ongoing);
			return;
		}

		LocateBar.setCollectingOnGoing(mapViewer, true);

		mapViewer.currentCollectingX = mapViewer.mTargetColNo;
		mapViewer.currentCollectingY = mapViewer.mTargetRowNo;

		new Thread() {
			public void run() {
				CollectInfo collect = new CollectInfo();
				Location location = new Location(Util.getRuntimeIndoorMap().getMapId(), 
						mapViewer.currentCollectingX, mapViewer.currentCollectingY, 
						Util.getRuntimeIndoorMap().getVersionCode());
				collect.setLocation(location);

				WifiFingerPrint fingnerPrint = new WifiFingerPrint(IndoorMapData.REQUEST_COLLECT);
				fingnerPrint.log();

				LocateBar.setCollectingOnGoing(mapViewer, false);

				collect.setWifiFingerPrint(fingnerPrint);

				// JSONObject data = new JSONObject();

				try {
					Gson gson = new Gson();
					String json = gson.toJson(collect);
					JSONObject data = new JSONObject(json);

					if (Util.sendToServer(mapViewer, MsgConstants.MT_COLLECT, data)) {
						// For test purpose, display the [x,y]
						if (!silent)
							MapHUD.updateHinText(mapViewer, mapViewer.getResources().getString(R.string.collected)
									+ " ["
									+ mapViewer.currentCollectingX
									+ ","
									+ mapViewer.currentCollectingY + "]");
							/*Util.showToast(MapViewerActivity.this,
									getResources()
											.getString(R.string.collected)
											+ " ["
											+ currentCollectingX
											+ ","
											+ currentCollectingY + "]",
									Toast.LENGTH_LONG);*/
						addCollectedFlag(mapViewer, mapViewer.currentCollectingX, mapViewer.currentCollectingY); //add and show the flags
					} else {
						// All errors should be handled in the sendToServer
						// method
					}

				} catch (Exception ex) {
					//Util.showToast(MapViewerActivity.this,"002 " + ex.toString(), Toast.LENGTH_LONG);
					ex.printStackTrace();
					MapHUD.updateHinText(mapViewer, "COLLECT: 002 ERROR: " + ex.getMessage());
				}

				// We are ready for next collecting on new position
				mapViewer.currentCollectingX = -1;
				mapViewer.currentCollectingY = -1;

				if (mapViewer.DEBUG)
					Log.d(mapViewer.TAG, "End collectFingerprint Thread");
			}
		}.start();
	}

	// Add collected flag to the cell whose fingerprint has been collected. 
	public static void addCollectedFlag(final MapViewerActivity mapViewer, final int colNo, final int rowNo) {
		
		mapViewer.runOnUpdateThread(new Runnable() {
			public void run() {
				int cellPixel = Util.getRuntimeIndoorMap().getCellPixel();
				float pX = colNo * cellPixel;
				float pY = rowNo * cellPixel;
				
				Rectangle flag = Library.genFlag(mapViewer, pX, pY);
				
				flag.setPosition(pX, pY); //It might be dummy code, put here for test
				
				mapViewer.mainScene.getChildByIndex(Constants.LAYER_FLAG).attachChild(flag);
				
				if (mapViewer.collectedFlags == null) {
					
					mapViewer.collectedFlags = new ArrayList<Rectangle>();
				}
				
				mapViewer.collectedFlags.add(flag);
			}

		});
	}
	
	public static void testLocate(final MapViewerActivity mapViewer) {
		if (!Util.getWifiInfoManager().isWifiEmbeded()) {
			//Util.showLongToast(this, R.string.no_wifi_embeded);
			MapHUD.updateHinText(mapViewer, R.string.no_wifi_embeded);
			return;
		}

		if (!Util.getWifiInfoManager().isWifiEnabled()) {
			//Util.showLongToast(this, R.string.no_wifi_enabled);
			MapHUD.updateHinText(mapViewer, R.string.no_wifi_enabled);
			return;
		}
		
		//Util.showShortToast(this, R.string.locate_collecting);
		MapHUD.updateHinText(mapViewer, R.string.locate_collecting);
		
		if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR) {
			if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
				try {
					TestLocateCollectRequest testPosition = new TestLocateCollectRequest();
					testPosition.setLocation(new Location(Util.getRuntimeIndoorMap().getMapId(), 
							mapViewer.mTargetColNo, mapViewer.mTargetRowNo, 
							Util.getRuntimeIndoorMap().getVersionCode()));
					WifiFingerPrint fingnerPrint = Util.getWifiInfoManager().mergeSamples();
					fingnerPrint.log();
					
					testPosition.setFignerPrint(fingnerPrint);
					testPosition.setTimestamp(System.currentTimeMillis());
					try {
						Gson gson = new Gson();
						String json = gson.toJson(testPosition);
						JSONObject data = new JSONObject(json);
	
						if (Util.sendToServer(mapViewer, MsgConstants.MT_LOCATE_TEST, data)) {
							//Util.showShortToast(this, R.string.locate_collected);
							MapHUD.updateHinText(mapViewer, R.string.locate_collected);
						} else {
							// All errors should be handled in the sendToServer
							// method
						}
					} catch (Exception ex) {
						//Util.showToast(this, "LOCATE_TEST:104 " + ex.toString(), Toast.LENGTH_LONG);
						ex.printStackTrace();
						MapHUD.updateHinText(mapViewer, "LOCATE_TEST:104 ERROR " + ex.getMessage());
						mapViewer.finish();
						return;
					}
				} catch (Exception e) {
					//Util.showToast(this, "LOCATE_TEST:103 " + e.toString(), Toast.LENGTH_LONG);
					e.printStackTrace();
					MapHUD.updateHinText(mapViewer, "LOCATE_TEST:103 ERROR " + e.getMessage());
					mapViewer.finish();
					return;
				}
				
				return;
			} //if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
		} //if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR) {
			
		// No enough buffered data or buffer not used, use a thread
		
		if (LocateBar.isCollectingOnGoing(mapViewer)) {
			//Util.showShortToast(this, R.string.another_collect_ongoing);
			MapHUD.updateHinText(mapViewer, R.string.another_collect_ongoing);
			return;
		}
		
		// To pass the x, y to our new Thread
		if ((mapViewer.currentCollectingX != -1) || (mapViewer.currentCollectingX != -1)) {
			//Util.showShortToast(this, R.string.collect_ongoing_failure);
			MapHUD.updateHinText(mapViewer, R.string.collect_ongoing_failure);
			return;
		}
		
		LocateBar.setCollectingOnGoing(mapViewer, true);
		
		mapViewer.currentCollectingX = mapViewer.mTargetColNo;
		mapViewer.currentCollectingY = mapViewer.mTargetRowNo;

		new Thread() {
			public void run() {
				try {
					TestLocateCollectRequest testPosition = new TestLocateCollectRequest();
					testPosition.setLocation(new Location(Util.getRuntimeIndoorMap().getMapId(), 
							mapViewer.currentCollectingX, mapViewer.currentCollectingY, 
							Util.getRuntimeIndoorMap().getVersionCode()));
					WifiFingerPrint fingnerPrint = new WifiFingerPrint(IndoorMapData.REQUEST_LOCATE);
					fingnerPrint.log();
					testPosition.setFignerPrint(fingnerPrint);
					testPosition.setTimestamp(System.currentTimeMillis());

					LocateBar.setCollectingOnGoing(mapViewer, false);

					try {
						// data.put("req", IndoorMapData.REQUEST_LOCATE);
						// data.put("fingerprint", fingerPrint);
						Gson gson = new Gson();
						String json = gson.toJson(testPosition);
						JSONObject data = new JSONObject(json);

						if (Util.sendToServer(mapViewer, MsgConstants.MT_LOCATE_TEST, data)) {
							/*
							 * if (waitForLocation()) { // do nothing } else {
							 * // All errors should be handled in the
							 * waitForLocation // method }
							 */
							//Util.showShortToast(MapViewerActivity.this, R.string.locate_collected);
							MapHUD.updateHinText(mapViewer, R.string.locate_collected);
						} else {
							// All errors should be handled in the sendToServer
							// method
						}
					} catch (Exception ex) {
						//Util.showToast(MapViewerActivity.this, "004 " + ex.toString(), Toast.LENGTH_LONG);
						ex.printStackTrace();
						MapHUD.updateHinText(mapViewer, "TEST_LOC004 ERROR: " + ex.getMessage());
					}
				} catch (Exception e) {
					LocateBar.setCollectingOnGoing(mapViewer, false);
					
					//Util.showToast(MapViewerActivity.this,"003 " + e.toString(), Toast.LENGTH_LONG);
					e.printStackTrace();
					MapHUD.updateHinText(mapViewer, "TEST_LOC003 ERROR: " + e.getMessage());
				}
				
				// We are ready for next collecting on new position
				mapViewer.currentCollectingX = -1;
				mapViewer.currentCollectingY = -1;

				if (mapViewer.DEBUG)
					Log.d(mapViewer.TAG, "End MT_LOCATE_TEST Thread");
			}
		}.start();
	}
	
	public static void testCollect(final MapViewerActivity mapViewer) {
		if (!Util.getWifiInfoManager().isWifiEmbeded()) {
			//Util.showLongToast(this, R.string.no_wifi_embeded);
			MapHUD.updateHinText(mapViewer, R.string.no_wifi_embeded);
			return;
		}

		if (!Util.getWifiInfoManager().isWifiEnabled()) {
			//Util.showLongToast(this, R.string.no_wifi_enabled);
			MapHUD.updateHinText(mapViewer, R.string.no_wifi_enabled);
			return;
		}
		
		//Util.showShortToast(this, R.string.collecting);
		MapHUD.updateHinText(mapViewer, R.string.collecting);
		
		if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER) {
			if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
				TestLocateCollectRequest testPosition = new TestLocateCollectRequest();
				testPosition.setLocation(new Location(Util.getRuntimeIndoorMap().getMapId(), 
						mapViewer.mTargetColNo, mapViewer.mTargetRowNo, 
						Util.getRuntimeIndoorMap().getVersionCode()));
				
				WifiFingerPrint fingnerPrint = Util.getWifiInfoManager().mergeSamples();
				fingnerPrint.log();
				testPosition.setFignerPrint(fingnerPrint);
				testPosition.setTimestamp(System.currentTimeMillis());
	
				try {
					Gson gson = new Gson();
					String json = gson.toJson(testPosition);
					JSONObject data = new JSONObject(json);
	
					if (Util.sendToServer(mapViewer, MsgConstants.MT_COLLECT, data)) {
						// For test purpose, display the [x,y]
						MapHUD.updateHinText(mapViewer, mapViewer.getResources().getString(R.string.collected)
								+ " ["
								+ mapViewer.mTargetColNo
								+ ","
								+ mapViewer.mTargetRowNo + "]");
					} else {
						// All errors should be handled in the sendToServer
						// method
					}
	
				} catch (Exception ex) {
					//Util.showToast(this, "102 " + ex.toString(), Toast.LENGTH_LONG);
					ex.printStackTrace();
					MapHUD.updateHinText(mapViewer, "TEST_COLLECT: 102 ERROR: " + ex.getMessage());
				}
				
				return;
			} // if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
		} // if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER) {
		
		
		// No enough buffered data or buffer not used, use a thread
		
		// To pass the x, y to our new Thread
		if ((mapViewer.currentCollectingX != -1) || (mapViewer.currentCollectingX != -1)) {
			//Util.showShortToast(this, R.string.collect_ongoing_failure);
			MapHUD.updateHinText(mapViewer, R.string.collect_ongoing_failure);
			return;
		}

		if (LocateBar.isCollectingOnGoing(mapViewer)) {
			MapHUD.updateHinText(mapViewer, R.string.another_collect_ongoing);
			//Util.showShortToast(this, R.string.another_collect_ongoing);
			return;
		}

		LocateBar.setCollectingOnGoing(mapViewer, true);

		mapViewer.currentCollectingX = mapViewer.mTargetColNo;
		mapViewer.currentCollectingY = mapViewer.mTargetRowNo;

		new Thread() {
			public void run() {
				TestLocateCollectRequest testPosition = new TestLocateCollectRequest();
				testPosition.setLocation(new Location(Util.getRuntimeIndoorMap().getMapId(), 
						mapViewer.currentCollectingX, mapViewer.currentCollectingY, 
						Util.getRuntimeIndoorMap().getVersionCode()));
				WifiFingerPrint fingnerPrint = new WifiFingerPrint(IndoorMapData.REQUEST_COLLECT);
				fingnerPrint.log();
				testPosition.setFignerPrint(fingnerPrint);
				testPosition.setTimestamp(System.currentTimeMillis());

				LocateBar.setCollectingOnGoing(mapViewer, false);

				try {
					Gson gson = new Gson();
					String json = gson.toJson(testPosition);
					JSONObject data = new JSONObject(json);

					if (Util.sendToServer(mapViewer, MsgConstants.MT_COLLECT_TEST, data)) {
						// For test purpose, display the [x,y]
						MapHUD.updateHinText(mapViewer, mapViewer.getResources().getString(R.string.collected)
								+ " ["
								+ mapViewer.currentCollectingX
								+ ","
								+ mapViewer.currentCollectingY + "]");
					} else {
						// All errors should be handled in the sendToServer
						// method
					}

				} catch (Exception ex) {
					//Util.showToast(MapViewerActivity.this,"002 " + ex.toString(), Toast.LENGTH_LONG);
					ex.printStackTrace();
					MapHUD.updateHinText(mapViewer, "TEST_COLLECT: 002 ERROR: " + ex.getMessage());
				}

				// We are ready for next collecting on new position
				mapViewer.currentCollectingX = -1;
				mapViewer.currentCollectingY = -1;

				if (mapViewer.DEBUG)
					Log.d(mapViewer.TAG, "End testCollect Thread");
			}
		}.start();
	}	
	@SuppressLint("SimpleDateFormat")
	public static void showTestResult(MapViewerActivity mapViewer, TestLocateCollectReply testLocation, Location balanceLocation) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mapViewer);
		
		String message = "";		
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		if (testLocation.getReTest() == 1) {
			if (mapViewer.mMode == IndoorMapData.MAP_MODE_TEST_COLLECT) {
				message += "第一次在该点采集指纹！无法参考此测试结果！\n";
				message += "如有必要请多次采集数据并查看测试结果！\n";	
			} else {
				if (mapViewer.mMode == IndoorMapData.MAP_MODE_TEST_LOCATE) {
					message += "该点指纹未采集！已自动保存指纹！无法参考此测试结果！\n";
					message += "请稍后重新测试该点！如有必要请删除并重新采集该点数据！\n";
				} else {
					message += "模式已被切换！\n";
				}
			}	
		}
		
		Location origLocation = testLocation.getOrigLocation();
		
		if (origLocation.getMapId() == balanceLocation.getMapId()){
			message += "实际地点：[" + origLocation.getX() + "," + origLocation.getY() + "]\n";
			message += "定位地点：[" + balanceLocation.getX() + "," + balanceLocation.getY() + "]\n";
		} else {
			message += "定位地点和实际地点不在同一张地图！\n";
		}		
		
		float timeInterval1 = (currentTime - testLocation.getTimestamp1()) / 1000f;
		message += "总耗时间：" + timeInterval1 + "s\n";
		float timeInterval2 = (testLocation.getTimestamp3() - testLocation.getTimestamp2()) / 1000f;
		message += "定位耗时：" + timeInterval2 + "s\n";
		float timeInterval3 = timeInterval1 - timeInterval2;
		message += "网络耗时：" + timeInterval3 + "s";
		
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("测试结果 @ " + sdf.format(new Date(currentTime)));
		builder.setMessage(message);
		
		builder.setPositiveButton("我知道了", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});

		builder.create();
		builder.show();
	}
	
	
	public static void handleTestReply(MapViewerActivity mapViewer, TestLocateCollectReply testLocation) {
		updateLocation(mapViewer, testLocation);
	}	
	
	public static void updateLocation(MapViewerActivity mapViewer, TestLocateCollectReply testLocation) {
		LocationSet locationSet = testLocation.getLocations();
		
		Location banlanceLocation = locationSet.balanceLocation();

		LocateBar.updateLocation(mapViewer, banlanceLocation);
		
		if (banlanceLocation.getMapId() == Util.getRuntimeIndoorMap().getMapId()) {
			LocateBar.updateTrack(mapViewer, locationSet.getLocations());
		}
	
		showTestResult(mapViewer, testLocation, banlanceLocation);
	}		
	
	public static void deleteFingerprint(MapViewerActivity mapViewer) {
		Location location = new Location(Util.getRuntimeIndoorMap().getMapId(), mapViewer.mTargetColNo, mapViewer.mTargetRowNo, 
										Util.getRuntimeIndoorMap().getVersionCode());
		
		try {
			Gson gson = new Gson();
			String json = gson.toJson(location);
			JSONObject data = new JSONObject(json);

			if (Util.sendToServer(mapViewer, MsgConstants.MT_DELETE_FINGERPRINT, data)) {
				//Util.showShortToast(this, R.string.delete_fingerprint_at_this_location);
				MapHUD.updateHinText(mapViewer, mapViewer.getResources().getString(R.string.delete_fingerprint_at_this_location) 
						+ " @[" + mapViewer.mTargetColNo + "," + mapViewer.mTargetRowNo + "]");
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			//Util.showToast(MapViewerActivity.this, "DEL_FP01 " + ex.toString(), Toast.LENGTH_LONG);
			ex.printStackTrace();
			MapHUD.updateHinText(mapViewer, "DEL_FP01 ERROR: " + ex.getMessage());
		}
	}		
}
