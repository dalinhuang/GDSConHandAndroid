package com.winjune.wifiindoor.map;

import java.io.Serializable;

/**
 * @author haleyshi
 *
 */
public class MapManagerItem implements Serializable {
	private static final long serialVersionUID = 8681196900546820564L;
	
	private int mapId;   
	private String title;  
	private String version; 
	private int rows;      
	private int columns; 

	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
}
