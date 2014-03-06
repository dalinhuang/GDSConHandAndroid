package com.winjune.wifiindoor.drawing.graphic.model;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;

import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.AndEngineGraphicsHelper;
import com.winjune.wifiindoor.drawing.graphic.ImageLoader;

// Obsoleted
public class BackGroundUnit2 extends Unit {
	
	private TextureRegion textureRegion;

	@Override
	public void clearCache() {
		super.clearCache();

		textureRegion = null;
	}

	public Sprite load(MapViewerActivity activity, int totalWidth, int totalHeight) {
		if (textureRegion == null) {
			BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), getNearestPowerOfTwo(totalWidth), getNearestPowerOfTwo(totalHeight), TextureOptions.BILINEAR);
			textureRegion = AndEngineGraphicsHelper.createFromBitmap(textureAtlas, ImageLoader.loadAssetAsBitmap("svg/background.svg", totalWidth, totalHeight));	
			textureAtlas.load();
		}

		return new Sprite(0, 0, totalWidth, totalHeight, textureRegion, activity.getVertexBufferObjectManager());
	}
}
