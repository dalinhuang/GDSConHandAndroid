package com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.model;

import org.andengine.entity.sprite.Sprite;

public class MapPieceSprite {	
	public static final int UNKNOWN = 0;
	public static final int PREPAREING = 1;
	public static final int READY = 2;
	public static final int ATTACHED = 3;
	
	private Sprite sprite;
	private int state; 
	
	public MapPieceSprite() {
		state = UNKNOWN;
		sprite = null;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
