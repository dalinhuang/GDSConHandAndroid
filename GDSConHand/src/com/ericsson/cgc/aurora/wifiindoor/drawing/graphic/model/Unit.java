package com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.model;

import org.andengine.opengl.texture.TextureManager;

import android.content.res.AssetManager;



public abstract class Unit {
	
	public static AssetManager getAssetManager() {
		return assetManager;
	}
	
	public static int getNearestPowerOfTwo(int v) {
		int result = 2;
		while (result < v) {
			result *= 2;
		}
		return result;
	}
	
	public static TextureManager getTextureManager() {
		return textureManager;
	}
	
	public static void setAssetManager(AssetManager inAssetManager) {
		assetManager = inAssetManager;
	}

	public static void setTextureManager(TextureManager inTextureManager) {
		textureManager = inTextureManager;
	}
	
	private int width;
	
	private int height;

	private int initialRotation;

	private static TextureManager textureManager;

	private static AssetManager assetManager;

	public Unit(){
		
	}

	public Unit(int initialRotation) {
		super();
		this.initialRotation = initialRotation;
	}

	public Unit(int width, int height, int initialRotation) {
		super();
		this.width = width;
		this.height = height;
		this.initialRotation = initialRotation;
	}
	
	public void clearCache(){
		
	}
	public int getHeight() {
		return height;
	}
	
	public int getInitialRotation() {
		return initialRotation;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setInitialRotation(int initialRotation) {
		this.initialRotation = initialRotation;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

}
