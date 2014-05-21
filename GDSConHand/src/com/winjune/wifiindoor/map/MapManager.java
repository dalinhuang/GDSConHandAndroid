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
		if (mapList.maps.size() > 0)
			return mapList.maps.get(0);
		else
			return null;
	}
	
	public static MapDataR getMapByLabel(String label){
		
		for (MapDataR map:mapList.maps){
			if (map.getLabel().equals(label))
				return map;
		}
		
		return null;
	}
		
	public static MapDataR getMapById(int id){
		
		for (MapDataR map:mapList.maps) {
			if (map.getId() == id)
				return map;
		}
		
		return null;				
	}
	
	public static ArrayList<MapDataR> getMaps(){
		return mapList.maps;
	}
}