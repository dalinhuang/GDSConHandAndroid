package com.winjune.wifiindoor.drawing.graphic.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.FileBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TextureRegion;

import android.content.res.AssetManager;
import android.util.Log;

import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;
import com.winjune.wifiindoor.webservice.IpsWebService;

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
				if (Util.getIsDefaultMap()) {
					// Copy the default assets image file to the map directory.
					file = Util.openOrCreateFileInPath(IndoorMapData.MAP_FILE_PATH_LOCAL
							+ Util.getRuntimeIndoorMap().getMapId() + "/", fileName, true);
					try {
						InputStream in = activity.getAssets().open(fileName);
						OutputStream out = new FileOutputStream(file);
						byte[] buffer = new byte[1024];
						int read;
						while ((read = in.read(buffer)) != -1) {
							out.write(buffer, 0, read);
						}
						in.close();
						in = null;
						out.flush();
						out.close();
						out = null;

					} catch (Exception e) {
						// Get default map failure
						Log.e("MapPiece", "Default map can't be loaded to path=" + Util.getMapPicturePathName(""+Util.getRuntimeIndoorMap().getMapId(), fileName));
					}

				}
				else
				{
				Log.i("MapPiece", "File not exist, path=" + Util.getMapPicturePathName(""+Util.getRuntimeIndoorMap().getMapId(), fileName));
				if (!IpsWebService.isHttpConnectionEstablished()) { // Mission impossible
					return null;
				}

				Log.i("MapPiece", "Download file, path=" + Util.getMapPicturePathName(""+Util.getRuntimeIndoorMap().getMapId(), fileName));
				Util.downloadMapPicture(activity, ""+Util.getRuntimeIndoorMap().getMapId(), fileName);
				}
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