package com.winjune.wifiindoor.webservice.types;

import com.winjune.common.webservice.core.types.IType;
import com.winjune.wifiindoor.map.InitialMap;

public class InitialMapReply implements IType {
	private int cellPixel;
	private int rows;
	private int columns;
	
	public int getCellPixel() {
		return cellPixel;
	}
	
	public void setCellPixel(int cellPixel) {
		this.cellPixel = cellPixel;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	public InitialMap toInitialMap() {
		InitialMap map = new InitialMap();
		map.setCellPixel(cellPixel);
		map.setColumns(columns);
		map.setRows(rows);
		
		return map;
	}
}
