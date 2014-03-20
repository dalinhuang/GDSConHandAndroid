package com.winjune.wifiindoor.mapviewer;

import java.util.ArrayList;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.json.JSONObject;

import android.widget.Toast;

import com.google.gson.Gson;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.map.CellCollectStatus;
import com.winjune.wifiindoor.map.InterestPlace;
import com.winjune.wifiindoor.map.MapCollectStatus;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.VersionOrMapIdRequest;

public class CollectedFlag {

	
	public static void loadCollectedFlag(final MapViewerActivity mapViewer) {
		
		int mapid = Util.getRuntimeIndoorMap().getMapId();
		
		downloadCollectedFlag(mapViewer, mapid);
		
	}
	
	public static void downloadCollectedFlag(MapViewerActivity mapViewer, int mapId) {
		VersionOrMapIdRequest id = new VersionOrMapIdRequest();
		id.setCode(mapId);

		try {
			
			Gson gson = new Gson();
			String json = gson.toJson(id);
			JSONObject data = new JSONObject(json);

			if (IpsWebService.sendMessage(mapViewer, IpsMsgConstants.MT_COLLECTED_FLAG_QUERY, data)) {
				
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			Util.showToast(mapViewer, "GET COLLECTED FLAGS ERROR: " + ex.getMessage(), Toast.LENGTH_LONG);
			ex.printStackTrace();
		}
	}
	
	public static void showCollectedFlag(final MapViewerActivity mapViewer, MapCollectStatus flags) {
		
		if (flags == null) {
			return;
		}
		
		// we only display the collected flags in planning mode
		if (!VisualParameters.PLANNING_MODE_ENABLED)
			return;
		
		
		// Clear old collected flags info
		if (mapViewer.collectedFlags == null) {
			mapViewer.collectedFlags = new ArrayList<Rectangle>();
		} else {
			for (Rectangle flag:mapViewer.collectedFlags) {
				if (flag != null) {
					mapViewer.mainScene.getChildByIndex(Constants.LAYER_FLAG).detachChild(flag);
				}
			}
			mapViewer.collectedFlags.clear();
		}
		
		// Show the new collected flags info
		ArrayList<CellCollectStatus> cells = flags.getCells();
		
		if (cells == null) {
			return;
		}
		
		for (CellCollectStatus cell : cells) {
			if (cell != null) {
				// X and Y = -1 mean the guide audio 
				if ((cell.getX() != -1)&& (cell.getY() != -1)) {  
					addCollectedFlag(mapViewer, cell.getX(), cell.getY());
				}
			}
		}
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
}
