package com.ericsson.cgc.aurora.wifiindoor.drawing.graphic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class AndroidGraphicsHelper {

	public static Bitmap putTogether(int rows, int cols,
			Bitmap... originBitmaps) {
		
		if (rows == 1 && cols == 1){
			//warn
		}else{
			if (rows == 1){
				cols = originBitmaps.length;
			}
			
			if (cols == 1){
				rows = originBitmaps.length;
			}
		}
		
		if (rows <= 0 || cols <= 0){
			throw new IllegalArgumentException();
		}
		
		int width = originBitmaps[0].getWidth();
		int height = originBitmaps[0].getHeight();

		Bitmap bitmap = Bitmap.createBitmap(width * cols, height * rows,
				Bitmap.Config.ARGB_4444);  // Fix the Out of Memory issue

		Canvas canvas = new Canvas(bitmap);
		
		int rowNo = 0;
		int colNo = 0;
		
		for (Bitmap originBitmap : originBitmaps) {
			
			canvas.drawBitmap(originBitmap, new Rect(0, 0, width, height),
					new Rect(colNo * width, rowNo * height,
							(colNo + 1) * width, (rowNo + 1) * height), null);
			colNo++;
			if (colNo == cols) {
				rowNo++;
				colNo = 0;
			}
			
			// Try to fix the bitmap Out of Memory issue
			//if(!originBitmap.isRecycled() ){
			//	Log.e("BITMAP", "Recycle "+originBitmap.hashCode());
			//	originBitmap.recycle(); // Recycle the memory used by bitmap
		    //    System.gc();      // Call System garbage collector
		    //}
		}
			
		return bitmap;
	}

	public static Bitmap resizeBitmap(int width, int height, Bitmap originBitmap) {
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_4444); // Fix the Out of Memory issue

		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(originBitmap, new Rect(0, 0, originBitmap.getWidth(),
				originBitmap.getHeight()), new Rect(0, 0, width, height), null);
		
		// Try to fix the bitmap Out of Memory issue
		//if(!originBitmap.isRecycled() ){
		//	Log.e("BITMAP", "Recycle "+originBitmap.hashCode());
		//	originBitmap.recycle(); // Recycle the memory used by bitmap
	    //    System.gc();      // Call System garbage collector
	    //}
		
		return bitmap;
	}

}
