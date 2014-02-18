package com.winjune.wifiindoor.drawing.graphic.model;

import org.andengine.opengl.texture.TextureManager;

import android.content.res.AssetManager;



public abstract class Unit {
	
	private int width;
	
	private int height;
	
	private int initialRotation;
	
	public Unit(){
		
	}

	public Unit(int width, int height, int initialRotation) {
		super();
		this.width = width;
		this.height = height;
		this.initialRotation = initialRotation;
	}
	
	public Unit(int initialRotation) {
		super();
		this.initialRotation = initialRotation;
	}
	
	public static int getNearestPowerOfTwo(int v) {
		int result = 2;
		while (result < v) {
			result *= 2;
		}
		return result;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getInitialRotation() {
		return initialRotation;
	}

	public void setInitialRotation(int initialRotation) {
		this.initialRotation = initialRotation;
	}
	
	private static TextureManager textureManager;
	private static AssetManager assetManager;
	
	public static TextureManager getTextureManager() {
		return textureManager;
	}

	public static void setTextureManager(TextureManager inTextureManager) {
		textureManager = inTextureManager;
	}

	public static AssetManager getAssetManager() {
		return assetManager;
	}

	public static void setAssetManager(AssetManager inAssetManager) {
		assetManager = inAssetManager;
	}
	
	public void clearCache(){
		
	}

}
