package com.winjune.wifiindoor.mapviewer;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.winjune.wifiindoor.activity.EventViewerActivity;
import com.winjune.wifiindoor.activity.InfoPusherActivity;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.runtime.Cell;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.InfoQueryRequest;
import com.winjune.wifiindoor.webservice.types.Location;
import com.winjune.wifiindoor.webservice.types.LocationQueryInfo;
import com.winjune.wifiindoor.webservice.types.QueryInfo;

public class InfoBanner {

	
	public static void showInfo(MapViewerActivity mapViewer) {
		//infoQueryToast.show();
		/*
		Intent intent_pusher = new Intent(mapViewer, InfoPusherActivity.class); 
		Bundle mBundle = new Bundle(); 
		String info1 = Util.getRuntimeIndoorMap().informationsToString();
		String info2 = Util.getRuntimeIndoorMap().informationsToStringForLocations();
		
		mBundle.putString(IndoorMapData.BUNDLE_KEY_MAP_INFO, info1);
		mBundle.putString(IndoorMapData.BUNDLE_KEY_LOCATION_INFO, info2);
		intent_pusher.putExtras(mBundle); 
		mapViewer.startActivity(intent_pusher);
		*/
		
		Intent event_viewer = new Intent(mapViewer, EventViewerActivity.class); 				
		mapViewer.startActivity(event_viewer);		
	}

	public static void showInfo(MapViewerActivity mapViewer, QueryInfo queryInfo) {
		ArrayList<LocationQueryInfo> infos = queryInfo.getInfos();

		if ((infos == null) || (infos.isEmpty())) {
			return;
		}

		String text = "";

		for (LocationQueryInfo info : infos) {
			Location loc = info.getLocation();
			ArrayList<String> messages = info.getMessages();

			int col = loc.getX();
			int row = loc.getY();

			// Not for this map
			if (loc.getMapId() != Util.getRuntimeIndoorMap().getMapId()) {
				continue;
			}

			// No info
			if ((messages == null) || (messages.isEmpty())) {
				continue;
			}

			if ((col == -1) || (row == -1)) {
				// For map overall
				if (Util.getRuntimeIndoorMap().isSameInfo(messages)) {
					continue;
				} else {
					text += Util.getRuntimeIndoorMap().informationsToString(messages);
					Util.getRuntimeIndoorMap().setInfo(messages);
				}
			} else {
				// For cell

				// Out of bound
				if ((col > Util.getRuntimeIndoorMap().getColNum())
						|| (row > Util.getRuntimeIndoorMap().getRowNum()) || (col < 0)
						|| (row < 0)) {
					continue;
				}

				Cell cell = Util.getRuntimeIndoorMap().getCellAt(row, col);

				if (cell.isSameInfo(messages)) {
					continue;
				} else {
					text += cell.informationsToString(messages);
					cell.setInfo(messages);
				}
			}
		}

		if (text.equals("")) {
			return;
		}

		mapViewer.infoQueryToast.setText(text);
		mapViewer.infoQueryToast.show();
	}

	public static void infoMe(MapViewerActivity mapViewer, int colNo, int rowNo) {
		InfoQueryRequest infoQueryReq = new InfoQueryRequest();
		ArrayList<Location> locations = new ArrayList<Location>();

		if (Util.getRuntimeIndoorMap().isRefreshInfoNeeded()) {
			locations.add(new Location(Util.getRuntimeIndoorMap().getMapId(), -1, -1, Util.getRuntimeIndoorMap().getVersionCode()));
		}

		if ((colNo != -1) && (rowNo != -1)) {
			int areaSize = (7 - 1) / 2;

			int fromCol = Math.max(0, colNo - areaSize);
			int fromRow = Math.max(0, rowNo - areaSize);
			int toCol = Math.min(colNo + areaSize,
					Util.getRuntimeIndoorMap().getColNum() - 1);
			int toRow = Math.min(rowNo + areaSize,
					Util.getRuntimeIndoorMap().getRowNum() - 1);

			for (int col = fromCol; col <= toCol; col++) {
				for (int row = fromRow; row <= toRow; row++) {
					Cell cell = Util.getRuntimeIndoorMap().getCellAt(row, col); // y, x:
																		// Cells
																		// has
																		// the
																		// revert
																		// x, y
																		// as
																		// colNo,
																		// rowNo
					if (cell != null) {
						if (cell.isRefreshInfoNeeded()) {
							locations.add(new Location(Util.getRuntimeIndoorMap()
									.getMapId(), col, row, Util.getRuntimeIndoorMap().getVersionCode()));
						}
					}
				}
			}
		}

		if (!locations.isEmpty()) {
			infoQueryReq.setLocations(locations);

			try {
				Gson gson = new Gson();
				String json = gson.toJson(infoQueryReq);
				JSONObject data = new JSONObject(json);

				if (IpsWebService.sendMessage(mapViewer, IpsMsgConstants.MT_INFO_QUERY, data)) {
					
				} else {
					// All errors should be handled in the sendToServer
					// method
				}
			} catch (Exception ex) {
				//Util.showToast(this, "004 " + ex.toString(), Toast.LENGTH_LONG);
				ex.printStackTrace();
				MapHUD.updateHinText(mapViewer, "PUSH_INFO: 004 ERROR: " + ex.getMessage());
			}
		}
	}
}
