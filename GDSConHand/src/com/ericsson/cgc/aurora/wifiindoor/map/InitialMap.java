package com.ericsson.cgc.aurora.wifiindoor.map;


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
	
	public int getCellPixel() {
		return cellPixel;
	}
	
	public int getColumns(){
		return columns;
	}
	
	public int getRows(){
		return rows;
	}
	
	public void setCellPixel(int cellPixel) {
		this.cellPixel = cellPixel;
	}

	public void setColumns(int columns){
		this.columns = columns;
	}

	public void setRows(int rows){
		this.rows = rows;
	}
}
