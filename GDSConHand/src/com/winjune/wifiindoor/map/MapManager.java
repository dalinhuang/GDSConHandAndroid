package com.winjune.wifiindoor.map;

import java.util.ArrayList;

import com.winjune.wifiindoor.lib.map.MapDataR;
import com.winjune.wifiindoor.lib.map.MapDataT;

/**
 * @author haleyshi
 *
 */
public class MapManager {	
	public static final String mapTableName = "map_table.xml";
	
	public static MapDataT mapList =  new MapDataT();
	
	public static void loadOfflineData(String path){
		
		mapList.fromXML(path+mapTableName, mapList);		
	}
	
	public static MapDataR getDefaultMap(){
		return mapList.maps.get(1);
	}
	
	//Get the MapManagerItem by the Index
	public static MapDataR getMapByIndex(int index){		
		return mapList.maps.get(index);
	}		
	
	public static ArrayList<MapDataR> getMaps(){
		return mapList.maps;
	}
}