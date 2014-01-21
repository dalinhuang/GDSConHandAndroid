package com.ericsson.cgc.aurora.wifiindoor.types;

import com.ericsson.cgc.aurora.wifiindoor.map.InitialMap;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

public class InitialMapReply implements IType {
	private int cellPixel;
	private int rows;
	private int columns;
	
	public int getCellPixel() {
		return cellPixel;
	}
	
	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public void setCellPixel(int cellPixel) {
		this.cellPixel = cellPixel;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public InitialMap toInitialMap() {
		InitialMap map = new InitialMap();
		map.setCellPixel(cellPixel);
		map.setColumns(columns);
		map.setRows(rows);
		
		return map;
	}
}
