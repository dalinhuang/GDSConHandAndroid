package com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.model;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import com.ericsson.cgc.aurora.wifiindoor.MapViewerActivity;
import com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.AndEngineGraphicsHelper;
import com.ericsson.cgc.aurora.wifiindoor.runtime.RuntimeIndoorMap;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;

public class BackGroundUnit extends Unit {

	private static Bitmap createBackground(RuntimeIndoorMap runtimeIndoorMap) {	
		int rowCount = runtimeIndoorMap.getRowNum();
		int colCount = runtimeIndoorMap.getColNum();
		int cellPixel = runtimeIndoorMap.getCellPixel();
		int mapWidth = colCount * cellPixel;
		int mapHeight = rowCount * cellPixel;
		
		Paint paint = new Paint();

		paint.setAntiAlias(true);
				
		// Draw the background's background
		Bitmap bitmap = Bitmap.createBitmap(mapWidth, mapHeight, Bitmap.Config.ARGB_4444); // Change from ARGB_8888 to fix the Out of Memory problem

		Canvas canvas = new Canvas(bitmap);
		
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL_AND_STROKE);
		canvas.drawRect(0, 0, mapWidth, mapHeight, paint);
		// Picture of the map
		//Bitmap bitmapPic = ImageLoader.loadAssetAsBitmap("maps/"+runtimeIndoorMap.getId()+".png", mapWidth, mapHeight);
		//canvas.drawBitmap(bitmapPic, offsetX, offsetY, paint);

		// Draw the background's lines
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2f);
		paint.setColor(Color.BLUE);
		//draw borders
		canvas.drawRect(0, 0, mapWidth, mapHeight, paint);		
		
		// Draw inner lines
		paint.setStrokeWidth(1f);

		for (int i = 1; i < rowCount; i++) {
			int startX = 0;
			int startY = i * cellPixel;
			int stopX = mapWidth;
			int stopY = startY;
			
			if (i % 10 == 0) {
				paint.setColor(Color.RED);
			} else {
				paint.setColor(Color.BLUE);
			}
			
			canvas.drawLine(startX, startY, stopX, stopY, paint);
		}

		for (int i = 1; i < colCount; i++) {
			int startX = i * cellPixel;
			int startY = 0;
			int stopX = startX;
			int stopY = mapHeight;
			
			if (i % 10 == 0) {
				paint.setColor(Color.RED);
			} else {
				paint.setColor(Color.BLUE);
			}
			
			canvas.drawLine(startX, startY, stopX, stopY, paint);
		}
		
	    paint = null;
	    canvas = null;
	    System.gc();

		return bitmap;
	}
	
	TextureRegion backgroundTextureRegion;
	
	@Override
	public void clearCache() {
		super.clearCache();
		backgroundTextureRegion = null;
	}

	public Sprite load(MapViewerActivity activity, RuntimeIndoorMap runtimeIndoorMap) {		
		int rowCount = runtimeIndoorMap.getRowNum();
		int colCount = runtimeIndoorMap.getColNum();		
		int mapWidth = colCount * Util.getCurrentCellPixel();
		int mapHeight = rowCount * Util.getCurrentCellPixel();
		
		if (backgroundTextureRegion==null){
			BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), getNearestPowerOfTwo(mapWidth), getNearestPowerOfTwo(mapHeight), TextureOptions.BILINEAR);
			backgroundTextureRegion = AndEngineGraphicsHelper.createFromBitmap(textureAtlas, createBackground(runtimeIndoorMap));
			textureAtlas.load();
		}
		return new Sprite(0, 0, backgroundTextureRegion, activity.getVertexBufferObjectManager());
	}
}