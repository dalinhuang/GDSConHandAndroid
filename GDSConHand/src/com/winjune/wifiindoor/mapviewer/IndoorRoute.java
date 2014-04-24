package com.winjune.wifiindoor.mapviewer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.modifier.RotationModifier;
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

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.navi.NaviNode;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.Util;

public class IndoorRoute {


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
