package com.winjune.wifiindoor.drawing.graphic.model;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import com.winjune.wifiindoor.drawing.graphic.AndEngineGraphicsHelper;
import com.winjune.wifiindoor.drawing.graphic.ImageLoader;

import android.graphics.Bitmap;

public class AnimatedUnit extends Unit {

	private AnimatedSprite sprite;
	
	public AnimatedUnit(String[] images, int width, int height,
			int initialRotation) {
		super(width, height, initialRotation);
		setImages(images);
	}
	
	public AnimatedUnit(String[] images, int initialRotation) {
		super(initialRotation);
		setImages(images);
	}

	private String[] images;

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	private TiledTextureRegion tiledTextureRegion;

	@Override
	public void clearCache() {
		super.clearCache();

		tiledTextureRegion = null;
	}

	public AnimatedSprite load(BaseGameActivity activity) {
		return load(activity, null);
	}
	
	public AnimatedSprite load(BaseGameActivity activity, int widthPixels, int heightPixels) {
		setWidth(widthPixels);
		setHeight(heightPixels);
		
		return load(activity, null);
	}
	
	public AnimatedSprite load(BaseGameActivity activity, final SpriteListener spriteListener, int widthPixels, int heightPixels) {
		setWidth(widthPixels);
		setHeight(heightPixels);
		return load(activity, spriteListener);
	}

	public AnimatedSprite load(BaseGameActivity activity, final SpriteListener spriteListener) {

		if (tiledTextureRegion == null) {

			int tiledNum = getImages().length;

			BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), getNearestPowerOfTwo(tiledNum * getWidth()), getNearestPowerOfTwo(getHeight()), TextureOptions.BILINEAR);
			
			String[] images = getImages();
			Bitmap[] bitmaps = new Bitmap[images.length];

			for (int i = 0; i < images.length; i++) {
				bitmaps[i] = ImageLoader.loadAssetAsBitmap(images[i], getWidth(), getHeight());
			}
			
			tiledTextureRegion = AndEngineGraphicsHelper.createTiledFromBitmap(textureAtlas, bitmaps);	
			textureAtlas.load();
		}

		AnimatedSprite sprite = new AnimatedSprite(0, 0, getWidth(),getHeight(), tiledTextureRegion, activity.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (spriteListener != null) {
					return (spriteListener.onAreaTouched(this, pSceneTouchEvent,pTouchAreaLocalX, pTouchAreaLocalY));
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};

		sprite.setRotation(getInitialRotation());
		
		setSprite(sprite);

		return sprite;
	}

	public AnimatedSprite getSprite() {
		return sprite;
	}

	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}
}
