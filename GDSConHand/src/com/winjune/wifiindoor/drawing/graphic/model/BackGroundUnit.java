package com.winjune.wifiindoor.drawing.graphic.model;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.AndEngineGraphicsHelper;
import com.winjune.wifiindoor.runtime.RuntimeIndoorMap;
import com.winjune.wifiindoor.util.Util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class BackGroundUnit extends Unit {

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
			backgroundTextureRegion = AndEngineGraphicsHelper.createFromBitmap(textureAtlas, createBackground(activity, runtimeIndoorMap));
			textureAtlas.load();
		}
		return new Sprite(0, 0, backgroundTextureRegion, activity.getVertexBufferObjectManager());
	}

	private static Bitmap createBackground(MapViewerActivity activity, RuntimeIndoorMap runtimeIndoorMap) {	
		int rowCount = runtimeIndoorMap.getRowNum();
		int colCount = runtimeIndoorMap.getColNum();
		int cellPixel = runtimeIndoorMap.getCellPixel();
		int mapWidth = colCount * cellPixel;
		int mapHeight = rowCount * cellPixel;
		
		// Draw the background's background
		Paint paint_background = new Paint();
		paint_background.setAntiAlias(true);
		paint_background.setColor(activity.getResources().getColor(R.color.map_background));
		paint_background.setStyle(Style.FILL_AND_STROKE);
				
		Bitmap bitmap = Bitmap.createBitmap(mapWidth, mapHeight, Bitmap.Config.ARGB_4444); // Change from ARGB_8888 to fix the Out of Memory problem

		Canvas canvas = new Canvas(bitmap);

		canvas.drawRect(0, 0, mapWidth, mapHeight, paint_background);

		// Draw the background's lines
		Paint paint_line_1 = new Paint();
		paint_line_1.setAntiAlias(true);
		paint_line_1.setStyle(Style.STROKE);
		paint_line_1.setStrokeWidth(2f);
		paint_line_1.setColor(activity.getResources().getColor(R.color.map_dark));
		
		// Draw inner lines
		Paint paint_line_2 = new Paint();
		paint_line_2.setAntiAlias(true);
		paint_line_2.setStyle(Style.FILL);
		paint_line_2.setStrokeWidth(1f);
		paint_line_2.setColor(activity.getResources().getColor(R.color.map_light));
		
		// Draw inner lines, every 10th one
		Paint paint_line_3 = new Paint();
		paint_line_3.setAntiAlias(true);
		paint_line_3.setStyle(Style.FILL);
		paint_line_3.setStrokeWidth(1f);
		paint_line_3.setColor(activity.getResources().getColor(R.color.map_dark));
		
		//draw borders
		canvas.drawRect(0, 0, mapWidth, mapHeight, paint_line_1);		

		for (int i = 1; i < rowCount; i++) {
			int startX = 0;
			int startY = i * cellPixel;
			int stopX = mapWidth;
			int stopY = startY;
			
			if (i % 10 == 0) {
				canvas.drawLine(startX, startY, stopX, stopY, paint_line_3);
			} else {
				canvas.drawLine(startX, startY, stopX, stopY, paint_line_2);
			}
		}

		for (int i = 1; i < colCount; i++) {
			int startX = i * cellPixel;
			int startY = 0;
			int stopX = startX;
			int stopY = mapHeight;
			
			if (i % 10 == 0) {
				canvas.drawLine(startX, startY, stopX, stopY, paint_line_3);
			} else {
				canvas.drawLine(startX, startY, stopX, stopY, paint_line_2);
			}
		}
		
		paint_background = null;
		paint_line_1 = null;
		paint_line_2 = null;
		paint_line_3 = null;
	    canvas = null;
	    System.gc();

		return bitmap;
	}
}