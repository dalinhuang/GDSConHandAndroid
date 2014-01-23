package com.ericsson.cgc.aurora.wifiindoor.types;

import com.ericsson.cgc.aurora.wifiindoor.map.MapManagerItem;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

public class MapManagerItemReply implements IType{		
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

		public MapManagerItem toMapItem() {
			MapManagerItem item = new MapManagerItem();
			item.setMapId(mapId);
			item.setColumns(columns);
			item.setRows(rows);
			item.setTitle(title);
			item.setVersion(version);
			
			return item;
		}
}
