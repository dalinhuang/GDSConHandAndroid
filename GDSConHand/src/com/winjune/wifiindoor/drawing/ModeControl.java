package com.winjune.wifiindoor.drawing;

import org.andengine.entity.sprite.AnimatedSprite;

import com.winjune.wifiindoor.runtime.RuntimeIndoorMap;

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
