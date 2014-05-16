package com.winjune.wifiindoor.runtime;

import org.andengine.entity.sprite.AnimatedSprite;

public interface RuntimeMapListener {	
	void initial(RuntimeMap runtimeIndoorMap);
	void edit(Cell cell);
	void modeChanged(AnimatedSprite sprite, int mode);
	void appear(RuntimeUser user);
	void locate(RuntimeMap runtimeIndoorMap, int rowNo, int colNo, int userType, int idx);
}