package com.winjune.wifiindoor.poi;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

/**
 * @author jeffzha
 *
 */
public class SearchHistory implements Serializable{
	private static final long serialVersionUID = 7866873863978783133L;

	private static ArrayList<String> history = new ArrayList<String>();

	public static String[] getHistoryArray(){
        String[] recordArray = new String[0];
           
        recordArray = history.toArray(recordArray);
        
        return recordArray;
	}
	
	public static ArrayList<String> getHistory(){
		return history;
	}
	
	public static void addHistoryRecord(String searchInput) {				
		if (searchInput != null) {			
			history.add(searchInput);
		}
	}
	
	public static void loadHistory() {

		try {		
			fromXML(Util.getSearchInfoFilePathName());
			// file has already been closed
			//map_file_is.close();
			
			// For Files in SD Card but not
			//load_map_rc = designMap.fromXML(IndoorMapData.map_file_path + map_file_name);
			
		} catch (Exception e) {
			
		}
				
	}
	
	public static void clearHistory(){
		history.clear();
	}
	
	//Set Alias for the XML serialization
	private static void setAlias(XStream xs){
		xs.alias("SearchContext", com.winjune.wifiindoor.poi.SearchContext.class);
		//Invoke other objects' setAlias methods here
	}
	
	//Serialize current IndoorMap to XML file
	private static boolean toXML(){
		//Serialize this object
		XStream xs = new XStream();
		setAlias(xs);
		
		File file = Util.openOrCreateFileInPath(IndoorMapData.MAP_FILE_PATH_LOCAL, IndoorMapData.MAP_SEARCH_INFO_FILE_NAME, false);
		
		if (file == null) {
			return false;
		}
		
		//Write to the map info file
		try{
			FileOutputStream fos = new FileOutputStream(file);
			xs.toXML(fos);
			
			fos.close();
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//Set current InoorMap from XML file
	public static boolean fromXML(InputStream map_file_is){
		XStream xs = new XStream();
		setAlias(xs);

		try {
			xs.fromXML(map_file_is);
			
			map_file_is.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	//Set current IndoorMap from XML file
	public static boolean fromXML(String map_file_path){
		XStream xs = new XStream();
		setAlias(xs);

		try {
			FileInputStream fis = new FileInputStream(map_file_path);
			xs.fromXML(fis);
			
			fis.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	
}
