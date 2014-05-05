package com.winjune.wifiindoor.mapviewer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.text.Text;
import org.json.JSONObject;

import android.widget.Toast;

import com.google.gson.Gson;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.map.FieldInfo;
import com.winjune.wifiindoor.map.MapInfo;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.VersionOrMapIdRequest;

public class LabelBar {

	private static MapInfo mapInfo = null;
	
	public static void loadMapInfo(MapViewerActivity mapViewer) {
		if (mapInfo == null)
		{
			mapInfo = new MapInfo();
		}
		boolean updateNeeded = false; //Hoare: update every time regardless map versionn, for test only

		try {
			InputStream map_file_is = new FileInputStream(Util.getMapInfoFilePathName(""+Util.getRuntimeIndoorMap().getMapId()));
			
			mapInfo.fromXML(map_file_is);
			// file has already been closed
			//map_file_is.close();
			
			// For Files in SD Card but not
			//load_map_rc = designMap.fromXML(IndoorMapData.map_file_path + map_file_name);
			
			if (mapInfo.getVersionCode() != Util.getRuntimeIndoorMap().getVersionCode()) {
				updateNeeded = true;
			}
		} catch (Exception e) {
			updateNeeded = true;
		}
		
		if (updateNeeded) {
			// Hoare: harcode map_id 1 as the GDSC map
			int mapid = Util.getRuntimeIndoorMap().getMapId();
			
			if (mapid == 1 ) {
				mapid = 2;
			}				
			
			downloadMapInfo(mapViewer, mapid);
			return;
		}
		
		// load mapinfo to POI manager
		POIManager.mapinfo2Poi(mapInfo);
		
		showMapInfo(mapViewer, false);
	}
	
	public static void setMapInfo(MapInfo newMapInfo) {
		if (newMapInfo == null || mapInfo == null) {
			return;
		}
		
		mapInfo.setId(newMapInfo.getId());
		mapInfo.setVersionCode(newMapInfo.getVersionCode());
		
		// Show New Map Info
		ArrayList<FieldInfo> newFieldInfos = newMapInfo.getFields();
		ArrayList<FieldInfo> fieldInfos = mapInfo.getFields();
		
		if (fieldInfos == null) {
			if (newFieldInfos != null)
			{
			    fieldInfos = new ArrayList<FieldInfo>();
			}
		}
		else
		{
			fieldInfos.clear();
		}
		
		if (newFieldInfos == null)
		{
			fieldInfos.clear();
			fieldInfos = null;
		}
		else
		{
			fieldInfos.addAll(newFieldInfos);
			mapInfo.setFields(fieldInfos);
		}
	}
	
	// to be replaced 
	public static void showMapInfo(MapViewerActivity mapViewer, boolean storeNeeded) {
		if (mapInfo == null) {
			return;
		}
				
		showLabelsFromPOI(mapViewer);
		
		return;
		
/*		float currentZoomFactor = mapViewer.mCamera.getZoomFactor();
		
		// Clear old Map info
		if (mapViewer.mapInfos == null) {
			mapViewer.mapInfos = new ArrayList<Text>();
		} else {
			for (Text text:mapViewer.mapInfos) {
				if (text != null) {
					mapViewer.mainScene.detachChild(text);
				}
			}
			mapViewer.mapInfos.clear();
		}
		
		// Show New Map Info
		ArrayList<FieldInfo> fieldInfos = mapInfo.getFields();
		
		if (fieldInfos == null) {
			return;
		}
		
		for (FieldInfo fieldInfo : fieldInfos) {
			if (fieldInfo != null) {
				if ((currentZoomFactor >= fieldInfo.getMinZoomFactor())
				 && (currentZoomFactor <= fieldInfo.getMaxZoomFactor()))
				addTextTag(mapViewer,fieldInfo);
			}
		}
		
		// Store in File, put it here so the info may be re-encoded above in future.
		if (storeNeeded) {
			mapInfo.toXML();
		}*/
	}

	private static void downloadMapInfo(MapViewerActivity mapViewer, int mapId) {
		VersionOrMapIdRequest id = new VersionOrMapIdRequest();
		id.setCode(mapId);

		try {
			
			Gson gson = new Gson();
			String json = gson.toJson(id);
			JSONObject data = new JSONObject(json);

			if (IpsWebService.sendMessage(mapViewer, IpsMsgConstants.MT_MAP_INFO_QUERY, data)) {
				
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			Util.showToast(mapViewer, "GET MAP INFO ERROR: " + ex.getMessage(), Toast.LENGTH_LONG);
			ex.printStackTrace();
		}
	}
	


	private static void addTextTag(MapViewerActivity mapViewer, FieldInfo fieldInfo) {
		float pX = fieldInfo.getX() * Util.getRuntimeIndoorMap().getCellPixel();
		float pY = fieldInfo.getY() * Util.getRuntimeIndoorMap().getCellPixel();
		
		/* Sometimes it cause problem: include: 字符串显示叠加, 闪屏
		Text text = new TickerText(pX, pY, mFont, textStr, 
				new TickerTextOptions(HorizontalAlign.CENTER, 10),  //出现速度
				getVertexBufferObjectManager());
		
		text.registerEntityModifier(
			new SequenceEntityModifier(
				new ParallelEntityModifier(
					new AlphaModifier(1, 0.7f, 0.7f),  //透明度过渡
					new ScaleModifier(1, 0.6f, 1f)   //缩放过渡
				),
				new RotationModifier(1, 0, 0)          //旋转过渡
			)
		);
		text.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		*/
		
		Text text = new Text(pX,
				pY, 
				mapViewer.mFont_mapinfo, 
				fieldInfo.getInfo(),
				100,
				mapViewer.getVertexBufferObjectManager());
		text.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		text.setAlpha(fieldInfo.getAlpha());
		text.setRotation(fieldInfo.getRotation()); // For future use if we need to rotate a angle
		text.setScale(fieldInfo.getScale()); // For future use if we need to display some label with a bigger/smaller scale
		
		mapViewer.mainScene.attachChild(text);
		
		// Store so we can clear them in future if needed
		if (mapViewer.mapInfos == null) {
			mapViewer.mapInfos = new ArrayList<Text>();
		}
		
		mapViewer.mapInfos.add(text);
	}

	public static MapInfo getMapInfo() {
		return mapInfo;
	}	

	private static void addTextTag(MapViewerActivity mapViewer, PlaceOfInterest poi) {
		float pX = poi.getX() * Util.getRuntimeIndoorMap().getCellPixel();
		float pY = poi.getY() * Util.getRuntimeIndoorMap().getCellPixel();
				
		Text text = new Text(pX,
				pY, 
				mapViewer.mFont_mapinfo, 
				poi.label,
				100,
				mapViewer.getVertexBufferObjectManager());
		text.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		text.setAlpha(poi.getAlpha());
		text.setRotation(poi.getRotation()); // For future use if we need to rotate a angle
		text.setScale(poi.getScale()); // For future use if we need to display some label with a bigger/smaller scale
		
		mapViewer.mainScene.attachChild(text);
		
		// Store so we can clear them in future if needed
		if (mapViewer.mapInfos == null) {
			mapViewer.mapInfos = new ArrayList<Text>();
		}
		
		mapViewer.mapInfos.add(text);
	}	
	
	public static void showLabelsFromPOI(MapViewerActivity mapViewer) {
		float currentZoomFactor = mapViewer.mCamera.getZoomFactor();
		
		// Clear old Map info
		if (mapViewer.mapInfos == null) {
			mapViewer.mapInfos = new ArrayList<Text>();
		} else {
			for (Text text:mapViewer.mapInfos) {
				if (text != null) {
					mapViewer.mainScene.detachChild(text);
				}
			}
			mapViewer.mapInfos.clear();
		}
		
		// Show New Map Info

		for (PlaceOfInterest poi: POIManager.POIList) {			
			if ((currentZoomFactor >= poi.minZoomFactor)
				 && (currentZoomFactor <= poi.maxZoomFactor))
				addTextTag(mapViewer,poi);			
		}		
	}	
}
