package com.winjune.wifiindoor.drawing.graphic;

import java.io.IOException;
import java.io.InputStream;

import com.winjune.wifiindoor.drawing.graphic.svg.SVGImageBuilder;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * @author haleyshi
 * 
 */
public class ImageLoader {
	
	private static boolean initialed = false;

	private static Activity activity;

	public static void initial(Activity inActivity) {		
		if (initialed) {
			return;
		}

		initialed = true;
		
		activity = inActivity;
	}

	public static Bitmap loadAssetAsBitmap(String imagePath, int width,
			int height) {

		try {
			InputStream inputStream = activity.getAssets().open(imagePath);

			// cache bitmap map to file
			Bitmap bitmap = SVGImageBuilder.instance.generateFromFile(
					inputStream, width, height);
			
			return bitmap;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
