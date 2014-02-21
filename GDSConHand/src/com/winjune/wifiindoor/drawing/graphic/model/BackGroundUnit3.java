package com.winjune.wifiindoor.drawing.graphic.model;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;

import com.ericsson.cgc.aurora.wifiindoor.R;
import com.winjune.wifiindoor.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.AndEngineGraphicsHelper;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class BackGroundUnit3 extends Unit {

	TextureRegion backgroundTextureRegion;
	
	@Override
	public void clearCache() {
		super.clearCache();
		backgroundTextureRegion = null;
	}
	
	public Sprite load(MapViewerActivity activity, int screenWidth, int screenHeight) {		
		int cellPixel = Util.getRuntimeIndoorMap().getCellPixel();
		int colCount = screenWidth / cellPixel + VisualParameters.BACKROUND_LINES_BUFFER_SIZE; // take 5 cells for buffer
		int rowCount = screenHeight / cellPixel + VisualParameters.BACKROUND_LINES_BUFFER_SIZE;
		int backgroundWidth = colCount * cellPixel; 
		int backgroundHeight = rowCount * cellPixel;
		
		if (backgroundTextureRegion==null){
			BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), getNearestPowerOfTwo(backgroundWidth), getNearestPowerOfTwo(backgroundHeight), TextureOptions.BILINEAR);
			backgroundTextureRegion = AndEngineGraphicsHelper.createFromBitmap(textureAtlas, createBackground(activity, colCount, rowCount));
			textureAtlas.load();
		}
		
		return new Sprite(0, 0, backgroundTextureRegion, activity.getVertexBufferObjectManager());
	}

	private static Bitmap createBackground(MapViewerActivity activity, int colCount, int rowCount) {
		int cellPixel = Util.getRuntimeIndoorMap().getCellPixel();
		int backgroundWidth = colCount * cellPixel; 
		int backgroundHeight = rowCount * cellPixel;
		
		// Draw the background's background
		Paint paint_background = new Paint();
		paint_background.setAntiAlias(true);
		paint_background.setColor(activity.getResources().getColor(R.color.map_background));
		paint_background.setStyle(Style.FILL_AND_STROKE);
				
		Bitmap bitmap = Bitmap.createBitmap(backgroundWidth, backgroundHeight, Bitmap.Config.ARGB_4444); // Change from ARGB_8888 to fix the Out of Memory problem

		Canvas canvas = new Canvas(bitmap);

		canvas.drawRect(0, 0, backgroundWidth, backgroundHeight, paint_background);

		// Draw the background's lines
		Paint paint_line_1 = new Paint();
		paint_line_1.setAntiAlias(true);
		paint_line_1.setStyle(Style.FILL);
		paint_line_1.setStrokeWidth(1f);
		paint_line_1.setColor(activity.getResources().getColor(R.color.map_dark));
		
		//draw borders	

		for (int i = 1; i < rowCount; i++) {
			int startX = 0;
			int startY = i * cellPixel;
			int stopX = backgroundWidth;
			int stopY = startY;
			
			canvas.drawLine(startX, startY, stopX, stopY, paint_line_1);
		}

		for (int i = 1; i < colCount; i++) {
			int startX = i * cellPixel;
			int startY = 0;
			int stopX = startX;
			int stopY = backgroundHeight;
			
			canvas.drawLine(startX, startY, stopX, stopY, paint_line_1);
		}
		
		paint_background = null;
		paint_line_1 = null;
	    canvas = null;
	    System.gc();

		return bitmap;
	}
}