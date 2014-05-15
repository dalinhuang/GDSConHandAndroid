package com.winjune.wifiindoor.activity.mapviewer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.activity.NaviResultActivity;
import com.winjune.wifiindoor.navi.NaviInfo;
import com.winjune.wifiindoor.navi.NaviNode;
import com.winjune.wifiindoor.navi.NaviPath;
import com.winjune.wifiindoor.navi.Navigator;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.VersionOrMapIdRequest;

public class NaviBar {
	
	
	public static void loadNaviInfo(MapViewerActivity mapViewer) {
		
		mapViewer.myNavigator = new Navigator();
		
		boolean updateNeeded = true; //Hoare: update every time regardless map versionn, for test only

		try {
			NaviInfo naviInfo = new NaviInfo();

			InputStream map_file_is = new FileInputStream(Util.getNaviInfoFilePathName(""+Util.getRuntimeIndoorMap().getMapId()));
			
			naviInfo.fromXML(map_file_is);
			// file has already been closed
			//map_file_is.close();
			
			// For Files in SD Card but not
			//load_map_rc = designMap.fromXML(IndoorMapData.map_file_path + map_file_name);
			
			if (naviInfo.getVersionCode() != Util.getRuntimeIndoorMap().getVersionCode()) {
				updateNeeded = true;
			}
			
			// load cached navi info first regardless 
			mapViewer.myNavigator.init(naviInfo, mapViewer.getResources().getString(R.string.navi_meter));
			
		} catch (Exception e) {
			updateNeeded = true;
		}
		
		if (updateNeeded) {
			// Hoare: harcode map_id 1 as the GDSC map
			int mapid = Util.getRuntimeIndoorMap().getMapId();
			
			if (mapid == 1 ) {
				mapid = 2;
			}				
			

		} 					
	}
			
	// TODO: how to display the navi. result?
	private static void  goNavigator(MapViewerActivity mapViewer) {
		String naviStr = "";
		
		if (((mapViewer.naviMyPlaceX == -1) || (mapViewer.naviMyPlaceY == -1)) 
			&& ((mapViewer.naviFromNode == 0) || (mapViewer.naviToNode == 0))) {
			naviStr = mapViewer.getResources().getString(R.string.navi_my_place_unknown);
			showNavigatorViewer(mapViewer, naviStr);
			return;
		}
		
		int fromNode = getNaviNodeIdFromSpinnerId(mapViewer, mapViewer.naviFromNode);
		int toNode = getNaviNodeIdFromSpinnerId(mapViewer, mapViewer.naviToNode);

		if ((fromNode == -1) || (toNode == -1)) {
			naviStr = mapViewer.getResources().getString(R.string.navi_failed_no_data);
			showNavigatorViewer(mapViewer, naviStr);
			return;
		} 
		
		if ((mapViewer.myNavigator.naviInfo == null) || (mapViewer.myNavigator.naviInfo.getNodes() == null) 
			|| (mapViewer.myNavigator.naviInfo.getPaths() == null)) {
			naviStr = mapViewer.getResources().getString(R.string.navi_failed_no_data);
			showNavigatorViewer(mapViewer, naviStr);
			return;
		}
		
		NaviPath bestRoute = mapViewer.myNavigator.getShortestPath(fromNode, toNode);
		
		if (bestRoute == null) {
			naviStr = mapViewer.getResources().getString(R.string.navi_failed_no_route);
			showNavigatorViewer(mapViewer, naviStr);
			return;
		}
		
		naviStr += mapViewer.myNavigator.getSpinnerName(mapViewer.naviFromNode) + " ->->->->-> " +
					mapViewer.myNavigator.getSpinnerName(mapViewer.naviToNode) + "\n";
		
		naviStr += mapViewer.getResources().getString(R.string.navi_total_distance) + bestRoute.getDist() 
					+ mapViewer.getResources().getString(R.string.navi_meter);
		naviStr += "\n\n";
		
		if (mapViewer.naviFromNode == 0) {
			naviStr += mapViewer.getResources().getString(R.string.navi_my_place) + " ->-> " 
						+ mapViewer.myNavigator.getSpinnerName(mapViewer.naviFromNode) + "\n";
		}

		
		naviStr += bestRoute.getPathDesc();
		if (mapViewer.naviToNode == 0) {
			naviStr += mapViewer.myNavigator.getNodeName(fromNode) + " ->-> " 
					   + mapViewer.getResources().getString(R.string.navi_my_place) + "\n";
		}		
		
		naviStr += mapViewer.getResources().getString(R.string.navi_over) + "\n";		
		
		showNavigatorViewer(mapViewer, naviStr);
	}

	private static void showNavigatorViewer(MapViewerActivity mapViewer, String naviStr) {
		Intent intent_navigator = new Intent(mapViewer, NaviResultActivity.class); 
		Bundle mBundle = new Bundle(); 
		
		mBundle.putString(IndoorMapData.BUNDLE_KEY_NAVI_RESULT, naviStr);
		intent_navigator.putExtras(mBundle); 
		mapViewer.startActivity(intent_navigator);
	}
	
	private static int getNaviNodeIdFromSpinnerId (MapViewerActivity mapViewer, int spinnerId) {
		if ((mapViewer.myNavigator.naviInfo == null) || (mapViewer.myNavigator.naviInfo.getNodes() == null)) {
			return -1;
		}
		
		if (spinnerId == 0) { // My Place
			return mapViewer.myNavigator.getNearestNaviNode(mapViewer.naviMyPlaceX, mapViewer.naviMyPlaceY);
		}		
				
		return mapViewer.myNavigator.getNodeIdBySpinnerIdx(spinnerId);
	}		
	
	// Handle the reply Message for Navi. Info update
	public static void setNaviInfo(MapViewerActivity mapViewer, NaviInfo naviInfo) {
		if (naviInfo == null) {
			return;
		}			
			
		mapViewer.myNavigator.init(naviInfo, mapViewer.getResources().getString(R.string.navi_meter));
		
		// Store into file
		naviInfo.toXML();
	}	
	
	
	public static void showRouteSprite(final MapViewerActivity mapViewer, final ArrayList<NaviNode> naviNodes) {
		//Load the start and stop images
		ITextureRegion startTextureRegion = null;
		ITextureRegion stopTextureRegion = null;
		try {
			ITexture startTexture = new BitmapTexture(mapViewer.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return mapViewer.getResources().openRawResource(R.drawable.route_result_start_point);
				}
			});
			ITexture stopTexture = new BitmapTexture(mapViewer.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return mapViewer.getResources().openRawResource(R.drawable.route_result_end_point);
				}
			});

			startTexture.load();
			startTextureRegion = TextureRegionFactory.extractFromTexture(startTexture);
			stopTexture.load();
			stopTextureRegion = TextureRegionFactory.extractFromTexture(stopTexture);
		} catch (IOException e) {
			Debug.e(e);
		}
		
		//Testing nodes. Need to remove when real navinode info provided.
		NaviNode naviNode1 = new NaviNode();
		naviNode1.setX(530);
		naviNode1.setY(800);
		naviNode1.setMapId(2);
		naviNodes.add(naviNode1);
		NaviNode naviNode2 = new NaviNode();
		naviNode2.setX(460);
		naviNode2.setY(900);
		naviNode2.setMapId(2);
		naviNodes.add(naviNode2);
		NaviNode naviNode3 = new NaviNode();
		naviNode3.setX(550);
		naviNode3.setY(1030);
		naviNode3.setMapId(2);
		naviNodes.add(naviNode3);
		NaviNode naviNode4 = new NaviNode();
		naviNode4.setX(650);
		naviNode4.setY(1040);
		naviNode4.setMapId(2);
		naviNodes.add(naviNode4);
		NaviNode naviNode5 = new NaviNode();
		naviNode5.setX(720);
		naviNode5.setY(1010);
		naviNode5.setMapId(2);
		naviNodes.add(naviNode5);
		
		//Draw dotted route
		float startX=-1;
		float startY=-1;
		float stopX=-1;
		float stopY=-1;
		int firstNodeofCurrentMap=-1;
		int lastNodeofCurrentMap=-1;
		for (int i = 0; i < naviNodes.size(); i++) {
			NaviNode naviNode = naviNodes.get(i);
			if (Util.getRuntimeIndoorMap().getMapId() == naviNode.getMapId())
			{
				if (firstNodeofCurrentMap == -1)
				{
					firstNodeofCurrentMap = i;
					//First node
					startX = naviNode.getX();
					startY = naviNode.getY();
				}
				else
				{
					if (i == naviNodes.size()-1)
					{
						lastNodeofCurrentMap = i;
					}
					stopX = naviNode.getX();
					stopY = naviNode.getY();							
					drawDottedLine(startX,startY,stopX,stopY,15,15,8,mapViewer.mainScene.getChildByIndex(Constants.LAYER_ROUTE),
							mapViewer.getVertexBufferObjectManager());
					startX = stopX;
					startY = stopY;									
				}
			}
			else
			{
				if ((firstNodeofCurrentMap != -1) && (lastNodeofCurrentMap == -1))
				{
					lastNodeofCurrentMap = i-1;
					//Last node
				}
			}
		}
		
		//Show the start and stop images
		if (lastNodeofCurrentMap > firstNodeofCurrentMap) {
			final Sprite startPoint = new Sprite(naviNodes.get(
					firstNodeofCurrentMap).getX()
					- startTextureRegion.getWidth() / 2, naviNodes.get(
					firstNodeofCurrentMap).getY()
					- startTextureRegion.getHeight(), startTextureRegion,
					mapViewer.getVertexBufferObjectManager());
			mapViewer.mainScene.getChildByIndex(Constants.LAYER_ROUTE)
					.attachChild(startPoint);
			final Sprite stopPoint = new Sprite(naviNodes.get(
					lastNodeofCurrentMap).getX()
					- stopTextureRegion.getWidth() / 2, naviNodes.get(
					lastNodeofCurrentMap).getY()
					- stopTextureRegion.getHeight(), stopTextureRegion,
					mapViewer.getVertexBufferObjectManager());
			mapViewer.mainScene.getChildByIndex(Constants.LAYER_ROUTE)
					.attachChild(stopPoint);
			//TODO: Use text to show the end point or go up/down stairs
			//      according to the navinode information.
			/*
			Text text = new Text(naviNodes.get(lastNodeofCurrentMap).getX(),
					naviNodes.get(lastNodeofCurrentMap).getY(), 
					mapViewer.mFont_mapinfo, 
//					"终",
					"F2",
					100,
					mapViewer.getVertexBufferObjectManager())
			{

				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY)
				
				{
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
						//go to the other floor
					}
					return true;
				}
			};
			text.setScale(0.5f);
			float textHeight = text.getHeight();
			float textWidth  = text.getWidth();
			text.setPosition(stopX-textWidth/2, stopY-textHeight-stopTextureRegion.getHeight()*5/12);

			final LoopEntityModifier entityModifier =
					new LoopEntityModifier(null,
							-1,
							null,
							new SequenceEntityModifier(
									new ScaleModifier(2, 2, 1.5f),
									new ScaleModifier(2, 1.5f, 2),
									new DelayModifier(3f)
							)
					);

			text.registerEntityModifier(entityModifier);
			text.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			mapViewer.mainScene.getChildByIndex(Constants.LAYER_ROUTE)
			.attachChild(text);
			mapViewer.mainScene.registerTouchArea(text);
*/			
		}

//		mapViewer.mainScene.registerTouchArea(placeSprite);
		
	}

	private static void drawDottedLine(float x11, float y11, float x22, float y22,
			float lineLength, float gapLength, float lineWidth, IEntity entity,
			VertexBufferObjectManager vertexBufferObjectManager) {
		float slope;
		double deltaY;
		double deltaX;
		double gapDeltaX;
		double gapDeltaY;
		float direction = 1;
		if (x22 < x11 && x22 > 0) {
			direction = -1;
		}
		
		if (x22 != x11) {
			if (y22 != y11) {
				slope = (y22 - y11) / (x22 - x11);
				deltaY = slope * lineLength
						* Math.sqrt(1 / (slope * slope + 1)) * (direction);
				deltaX = deltaY / slope * (1);
				gapDeltaY = slope * gapLength
						* Math.sqrt(1 / (slope * slope + 1)) * (direction);
				gapDeltaX = gapDeltaY / slope * (1);
			} else {
				deltaX = lineLength;
				deltaY = 0;
				gapDeltaX = gapLength;
				gapDeltaY = 0;
			}
		} else {
			deltaX = 0;
			deltaY = lineLength;
			gapDeltaX = 0;
			gapDeltaY = gapLength;
		}

		double totalLineLength = Math.sqrt((y22 - y11) * (y22 - y11)
				+ (x22 - x11) * (x22 - x11));
		float currentX = x11;
		float currentY = y11;
		int count = (int) (totalLineLength / (gapLength + lineLength));
		float remainLength = (float) (totalLineLength - (gapLength + lineLength)
				* count);
		boolean shortCut = false;
		if (remainLength > 0) {
			count++;
			if (remainLength < lineLength) {
				shortCut = true;
			}
		}
		for (int i = 0; i < count; i++) {
			float newX, newY;
			if (shortCut && i == count - 1) {
				newX = x22;
				newY = y22;
			} else {
				newX = (float) (currentX + deltaX);
				newY = (float) (currentY + deltaY);
			}
			final Line line = new Line(currentX, currentY, newX, newY,
					lineWidth, vertexBufferObjectManager);
			currentX = (float) (newX + gapDeltaX);
			currentY = (float) (newY + gapDeltaY);
			//Draw a blue line
			line.setColor(0, 0, 1);
			entity.attachChild(line);

		}
	}	
}
