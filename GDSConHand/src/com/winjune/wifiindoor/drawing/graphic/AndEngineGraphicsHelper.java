package com.winjune.wifiindoor.drawing.graphic;

import java.io.InputStream;

import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;



import com.winjune.wifiindoor.drawing.graphic.svg.SVGImageBuilder;

import android.graphics.Bitmap;

public class AndEngineGraphicsHelper {

	public static TextureRegion createFromSVG(final BitmapTextureAtlas pTexture, int width, int height, 
			InputStream inputStream) {
		Bitmap bitmap = SVGImageBuilder.instance.generateFromFile(inputStream, width,
				height);

		return createFromBitmap(pTexture, bitmap);		
	}
	
	public static TextureRegion createFromBitmap(final BitmapTextureAtlas pTexture,
			Bitmap bitmap) {
		final IBitmapTextureAtlasSource textureSource = new BitmapTextureSource(bitmap);
				
		return (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromSource(pTexture, textureSource,
				0, 0);
	}

	public static TiledTextureRegion createTiledFromBitmap(
			final BitmapTextureAtlas pTexture, Bitmap... bitmaps) {

		Bitmap bitmap = AndroidGraphicsHelper.putTogether(1, -1, bitmaps);

		final IBitmapTextureAtlasSource textureSource = new BitmapTextureSource(bitmap);
		
		//Log.e("BITMAP", "createTiledFromBitmap: Allocate "+bitmap.hashCode());
		
		// Try to fix the bitmap Out of Memory issue
		//if(!bitmap.isRecycled() ){
		//	Log.e("BITMAP", "createTiledFromBitmap: Recycle "+bitmap.hashCode());
		//	bitmap.recycle(); // Recycle the memory used by bitmap
	    //    System.gc();      // Call System garbage collector
	    //}

		return TextureRegionFactory.createTiledFromSource(pTexture,
				textureSource, 0, 0, bitmaps.length, 1);

	}

}
