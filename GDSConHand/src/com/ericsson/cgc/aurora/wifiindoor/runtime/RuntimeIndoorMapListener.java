package com.ericsson.cgc.aurora.wifiindoor.runtime;

import org.andengine.entity.sprite.AnimatedSprite;

public interface RuntimeIndoorMapListener {	
	void appear(RuntimeUser user);
	void edit(Cell cell);
	void initial(RuntimeIndoorMap runtimeIndoorMap);
	void locate(RuntimeIndoorMap runtimeIndoorMap, int rowNo, int colNo, int userType, int idx);
	void modeChanged(AnimatedSprite sprite, int mode);
}