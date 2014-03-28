package com.winjune.wifiindoor.mapviewer;

import org.andengine.opengl.texture.region.ITextureRegion;

public class IconButtonInfo {
	private int iconResourceId;
	private String text;
	ITextureRegion iconRegion;
	public int getIconResourceId() {
		return iconResourceId;
	}
	public void setIconResourceId(int iconResourceId) {
		this.iconResourceId = iconResourceId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public ITextureRegion getIconRegion() {
		return iconRegion;
	}
	public void setIconRegion(ITextureRegion iconRegion) {
		this.iconRegion = iconRegion;
	}
		
}
