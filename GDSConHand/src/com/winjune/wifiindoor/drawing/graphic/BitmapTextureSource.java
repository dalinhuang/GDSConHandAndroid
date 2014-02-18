package com.winjune.wifiindoor.drawing.graphic;

import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 */
public class BitmapTextureSource implements IBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Bitmap bitmap;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public BitmapTextureSource(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public BitmapTextureSource clone() {
		return new BitmapTextureSource(this.bitmap);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.bitmap.hashCode() + "," + this.bitmap.getWidth() + " x " + this.bitmap.getHeight() + ")";
	}

	@Override
	public int getTextureX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTextureY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTextureX(int pTextureX) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTextureY(int pTextureY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTextureWidth() {
		return bitmap.getWidth();
	}

	@Override
	public int getTextureHeight() {
		return bitmap.getHeight();
	}

	@Override
	public void setTextureWidth(int pTextureWidth) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTextureHeight(int pTextureHeight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Bitmap onLoadBitmap(Config pBitmapConfig) {
		return bitmap.copy(bitmap.getConfig(), false);
	}

	@Override
	public IBitmapTextureAtlasSource deepCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
