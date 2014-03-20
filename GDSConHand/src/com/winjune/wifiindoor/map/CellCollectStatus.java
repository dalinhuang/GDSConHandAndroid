package com.winjune.wifiindoor.map;

import java.io.Serializable;

public class CellCollectStatus implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8037338805321343496L;
	
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
