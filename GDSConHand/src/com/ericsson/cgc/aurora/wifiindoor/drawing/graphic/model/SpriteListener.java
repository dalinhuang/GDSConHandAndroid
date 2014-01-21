package com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.model;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;

/**
 * @author haleyshi
 * 
 */
public interface SpriteListener {
	
	public boolean onAreaTouched(AnimatedSprite sprite, final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY);

}
