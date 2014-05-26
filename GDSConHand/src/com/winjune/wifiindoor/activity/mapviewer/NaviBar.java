package com.winjune.wifiindoor.activity.mapviewer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.util.Log;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.lib.map.MapDataR;
import com.winjune.wifiindoor.lib.map.NaviNodeR;
import com.winjune.wifiindoor.map.MapManager;
import com.winjune.wifiindoor.navi.NaviContext;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.Util;

enum StepType{
	Start,
	StartMidle,
	EndMiddle,
	End
}

public class NaviBar {
			
	public static void showNaviResulOnMap(final MapViewerActivity mapViewer, final NaviContext context) {
					
		final ArrayList<NaviNodeR> naviNodes = context.naviRoute;	
				
		//Draw dotted route
		float startX=-1, startY=-1, stopX=-1, stopY=-1;
		int firstNodeofCurrentMap=-1;
		int lastNodeofCurrentMap=-1;
				
		for (int i = 0; i < naviNodes.size(); i++) {
			NaviNodeR naviNode = naviNodes.get(i);
		
			if (Util.getRuntimeIndoorMap().getMapId() == naviNode.getMapId()){
				if (firstNodeofCurrentMap == -1) {
					firstNodeofCurrentMap = i;
					//First node
					startX = Util.longitudeX2MapX(naviNode.getPlaceX());
					startY = Util.latitudeY2MapY(naviNode.getPlaceY());
				} else	{
					if (i == naviNodes.size()-1) {
						lastNodeofCurrentMap = i;
					}
					stopX = Util.longitudeX2MapX(naviNode.getPlaceX());
					stopY = Util.latitudeY2MapY(naviNode.getPlaceY());							
					drawDottedLine(startX,startY,stopX,stopY,15,15,8,mapViewer.mainScene.getChildByIndex(Constants.LAYER_ROUTE),
							mapViewer.getVertexBufferObjectManager());
					startX = stopX;
					startY = stopY;									
				}
			} else {
				// switch to another map
				if ((firstNodeofCurrentMap != -1) && (lastNodeofCurrentMap == -1)){
					lastNodeofCurrentMap = i-1;
					break;
				}
			}
		}
		
		//Show the start and stop images
		if (lastNodeofCurrentMap > firstNodeofCurrentMap) {
			
			// start point
			if (firstNodeofCurrentMap == 0) {
				showRouteHint(mapViewer, context, firstNodeofCurrentMap,StepType.Start);				
			} else {
				showRouteHint(mapViewer, context, firstNodeofCurrentMap, StepType.StartMidle);
			}
							
			// end point
			if (lastNodeofCurrentMap  == (naviNodes.size() -1)) {
				showRouteHint(mapViewer, context, lastNodeofCurrentMap, StepType.End);
			} else {					
				showRouteHint(mapViewer, context, lastNodeofCurrentMap, StepType.EndMiddle);				
			}
			
		}		
	}
	
	private static void showRouteHint(final MapViewerActivity mapViewer, final NaviContext context, int stepIdx, final StepType type){
		//Load the start and stop images
		final int resId;
		String hintText = null; 
		MapDataR mapData = null;
		final NaviNodeR step = context.naviRoute.get(stepIdx);
		
		// set the corresponding icon and hint text
		switch (type) {
			case Start:
				resId = R.drawable.route_start;
				break;
			case StartMidle:
				resId = R.drawable.route_middle;
				int fromMapId = context.naviRoute.get(stepIdx-1).getMapId();	
				mapData = MapManager.getMapById(fromMapId);
				hintText = "从"+	mapData.getLabel();				
				break;
			case EndMiddle:
				resId = R.drawable.route_middle;
				int toMapId = context.naviRoute.get(stepIdx+1).getMapId();	
				mapData = MapManager.getMapById(toMapId);
				hintText = "到"+	mapData.getLabel();				
				break;
			default:// StepType.End
				resId = R.drawable.route_end;
				break;						
		}
		
		
		
		ITextureRegion mTextureRegion = null;
		try {
			ITexture mTexture = new BitmapTexture(mapViewer.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return mapViewer.getResources().openRawResource(resId);
				}
			});
			
		
			mTexture.load();
			mTextureRegion = TextureRegionFactory.extractFromTexture(mTexture);
						
		} catch (IOException e) {
			Debug.e(e);
		}		
		final MapDataR finalMapData = 	mapData;		
		final String finalHint = hintText;
		int mapX = Util.longitudeX2MapX(step.getPlaceX());
		int mapY = Util.latitudeY2MapY(step.getPlaceY());			
		Sprite iconSprite = new Sprite(mapX - mTextureRegion.getWidth() / 2, 
						  				mapY - mTextureRegion.getHeight(), 
						  				mTextureRegion, 
										mapViewer.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {				
				if (finalHint != null){
					mapViewer.runOnUiThread(new Runnable() {
						@Override
	 					public void run() {
							
							mapViewer.switchRuntimeMap(finalMapData);
							
							showNaviResulOnMap(mapViewer, context);
			            }        
			        });   							
					
					return true;
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};			
		
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_ROUTE).attachChild(iconSprite);
		
		if (hintText != null){
			// configure the touch area
			mapViewer.mainScene.registerTouchArea(iconSprite);

			// Use text to show the end point or go up/down stairs
			// according to the navi node information.		
			Text text = new Text(mapX,
								 mapX, 
								 mapViewer.mFontLabel, 
								 hintText,
								 64,
								 mapViewer.getVertexBufferObjectManager());
			text.setScale(0.5f);
			float textHeight = text.getHeight();
			float textWidth  = text.getWidth();
			text.setPosition(mapX-textWidth/2, mapY-textHeight-mTextureRegion.getHeight());
	
			// setup flash effect
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
			mapViewer.mainScene.getChildByIndex(Constants.LAYER_ROUTE).attachChild(text);
		}
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
