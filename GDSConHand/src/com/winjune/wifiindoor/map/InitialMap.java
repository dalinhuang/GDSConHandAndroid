package com.winjune.wifiindoor.map;


import java.io.Serializable;

/**
 * @author haleyshi
 *
 */
public class InitialMap implements Serializable{
	private static final long serialVersionUID = 710299020582345713L;
	
	private int cellPixel;
	private int rows;
	private int columns;
	
	public int getRows(){
		return rows;
	}
	
	public void setRows(int rows){
		this.rows = rows;
	}
	
	public int getColumns(){
		return columns;
	}
	
	public void setColumns(int columns){
		this.columns = columns;
	}

	public int getCellPixel() {
		return cellPixel;
	}

	public void setCellPixel(int cellPixel) {
		this.cellPixel = cellPixel;
	}
}
