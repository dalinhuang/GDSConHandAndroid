package com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.model;

import java.io.File;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.FileBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TextureRegion;

import android.util.Log;

import com.ericsson.cgc.aurora.wifiindoor.MapViewerActivity;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;
import com.ericsson.cgc.aurora.wifiindoor.util.VisualParameters;

public class MapPieceUnit extends Unit {

	public MapPieceUnit(){
		
	}

	private TextureRegion textureRegion;

	@Override
	public void clearCache() {
		super.clearCache();
		textureRegion = null;
	}

	public Sprite load(MapViewerActivity activity, String fileName, float pic_width, float pic_height) {
		// From External File
		if (textureRegion == null) {
			File file = new File(Util.getMapPicturePathName(""+Util.getRuntimeIndoorMap().getMapId(), fileName));
			
			Log.i("MapPiece", "Create Map Piece from file, path=" + Util.getMapPicturePathName(""+Util.getRuntimeIndoorMap().getMapId(), fileName));
			
			if (!file.exists()) {
				Log.i("MapPiece", "File not exist, path=" + Util.getMapPicturePathName(""+Util.getRuntimeIndoorMap().getMapId(), fileName));
				if (!Util.isHttpConnectionEstablished()) { // Mission impossible
					return null;
				}

				Log.i("MapPiece", "Download file, path=" + Util.getMapPicturePathName(""+Util.getRuntimeIndoorMap().getMapId(), fileName));
				Util.downloadMapPicture(activity, ""+Util.getRuntimeIndoorMap().getMapId(), fileName);
			}
			
			if (!file.exists()) {
				return null;
			}
			
			BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), getNearestPowerOfTwo((int)Math.ceil(pic_width)), getNearestPowerOfTwo((int)Math.ceil(pic_height)), TextureOptions.BILINEAR);
			FileBitmapTextureAtlasSource fileBitmapTextureAtlasSource = FileBitmapTextureAtlasSource.create(file);  
			textureRegion = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromSource(textureAtlas, fileBitmapTextureAtlasSource, 0, 0);  
			
			textureAtlas.load();
		}
		
	    Sprite sprite=new Sprite(0, 0, textureRegion, activity.getVertexBufferObjectManager());
	    sprite.setAlpha(VisualParameters.MAP_PIC_ALPHA);

		return sprite;
	}
}