package com.ericsson.cgc.aurora.wifiindoor.drawing;

import org.andengine.entity.sprite.AnimatedSprite;

import com.ericsson.cgc.aurora.wifiindoor.runtime.RuntimeIndoorMap;

/**
 * @author haleyshi
 *
 */
public class ModeControl {

	private RuntimeIndoorMap runtimeIndoorMap;
	
	public ModeControl(RuntimeIndoorMap runtimeIndoorMap) {
		this.runtimeIndoorMap = runtimeIndoorMap;
	}

	public void changeMode(AnimatedSprite sprite, int mode){
		runtimeIndoorMap.changeMode(sprite, mode);
	}
}
