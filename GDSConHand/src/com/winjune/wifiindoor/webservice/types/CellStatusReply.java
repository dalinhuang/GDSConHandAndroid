package com.winjune.wifiindoor.webservice.types;

import com.winjune.common.webservice.core.types.IType;

public class CellStatusReply implements IType{

	private int serial;
	private int x;
	private int y;
	private int count;

	public int getSerial() {
		return serial ;
	}
	
	public void setSerial(int serial ){
		this.serial  = serial;
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
