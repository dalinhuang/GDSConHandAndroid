package com.ericsson.cgc.aurora.wifiindoor.ads;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

public interface AdSpriteListener {
	
	public boolean onAreaTouched(Sprite sprite, final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY);

}
