package com.winjune.wifiindoor.drawing;

import org.andengine.entity.sprite.AnimatedSprite;

import com.winjune.wifiindoor.runtime.RuntimeMap;

/**
 * @author haleyshi
 *
 */
public class ModeControl {

	private RuntimeMap runtimeIndoorMap;
	
	public ModeControl(RuntimeMap runtimeIndoorMap) {
		this.runtimeIndoorMap = runtimeIndoorMap;
	}

	public void changeMode(AnimatedSprite sprite, int mode){
		runtimeIndoorMap.changeMode(sprite, mode);
	}
}
