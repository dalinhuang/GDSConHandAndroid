package com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.model;

import java.io.File;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.FileBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TextureRegion;

import com.ericsson.cgc.aurora.wifiindoor.MapViewerActivity;
import com.ericsson.cgc.aurora.wifiindoor.runtime.RuntimeIndoorMap;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;
import com.ericsson.cgc.aurora.wifiindoor.util.VisualParameters;



@SuppressWarnings("unused")
public class MapPictureUnit extends Unit {

	private TextureRegion textureRegion;

	public MapPictureUnit(){
		
	}

	@Override
	public void clearCache() {
		super.clearCache();
		textureRegion = null;
	}

	public Sprite load(MapViewerActivity activity, RuntimeIndoorMap runtimeIndoorMap) {
		int rowCount = runtimeIndoorMap.getRowNum();
		int colCount = runtimeIndoorMap.getColNum();		
		int mapWidth = colCount * Util.getCurrentCellPixel();
		int mapHeight = rowCount * Util.getCurrentCellPixel();
		
		// Fix the OOM issue
		/*
		if (textureRegion==null){
			BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), getNearestPowerOfTwo(mapWidth), getNearestPowerOfTwo(mapHeight), TextureOptions.BILINEAR);
			textureRegion = AndEngineGraphicsHelper.createFromBitmap(textureAtlas, decodeAssetBigImageFile(activity.getApplicationContext(), IndoorMapData.MAP_FILE_PATH + runtimeIndoorMap.getMapId() + "/" +runtimeIndoorMap.getMapPictureName()));
			textureAtlas.load();
		}
		
		return new Sprite(0, 0, textureRegion, activity.getVertexBufferObjectManager());
		*/

		// From External File
		if (textureRegion == null) {
			File file = new File(Util.getMapPicturePathName(""+runtimeIndoorMap.getMapId(), runtimeIndoorMap.getMapPictureName()));	
			
			if (!file.exists()) {
				while (Util.isDownloadOngoing()) {
					//wait
				}
				Util.downloadMapPicture(activity, ""+runtimeIndoorMap.getMapId(), runtimeIndoorMap.getMapPictureName());
				while (Util.isDownloadOngoing()) {
					//wait for download finish
				}
			}
			
			BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), getNearestPowerOfTwo(mapWidth), getNearestPowerOfTwo(mapHeight), TextureOptions.BILINEAR);
			FileBitmapTextureAtlasSource fileBitmapTextureAtlasSource = FileBitmapTextureAtlasSource.create(file);  
			textureRegion = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromSource(textureAtlas, fileBitmapTextureAtlasSource, 0, 0);  
			
			textureAtlas.load();
		}
		/* From Assets
		if (textureRegion == null) {
			BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), getNearestPowerOfTwo(mapWidth), getNearestPowerOfTwo(mapHeight), TextureOptions.BILINEAR); 
			textureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, activity, "/" + IndoorMapData.MAP_FILE_PATH_LOCAL + runtimeIndoorMap.getMapId() + "/" +runtimeIndoorMap.getMapPictureName(),0,0);        
			
			textureAtlas.load();
		}
		*/
	        
	    Sprite sprite=new Sprite(mapWidth,mapHeight,textureRegion, activity.getVertexBufferObjectManager());
	    sprite.setAlpha(VisualParameters.MAP_PIC_ALPHA);

		return sprite;
	}
	
	/*
	private Bitmap decodeAssetBigImageFile(Context context, String imagePath) {

		Bitmap b = null;
		
		Log.e("Bitmap", "ImagePath: "+imagePath);

		if (context == null) {
			Log.e("Bitmap", "Null Context");
			return b;
		}

		try {
			// Decode image size
			BitmapFactory.Options o = decodeImageSize(context, imagePath);

			int scale = 1;
			if (o.outHeight > 1024 || o.outWidth > 1024) {
				scale = (int) Math.pow(2,
						(int) Math.round(Math.log(1024 / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			InputStream fis = context.getAssets().open(imagePath);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}


	private BitmapFactory.Options decodeImageSize(Context context, String imagePath) {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		
		try {			
			if (context == null){
				return o;
			}
			
			InputStream fis = context.getAssets().open(imagePath);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return o;
	}
	*/
}