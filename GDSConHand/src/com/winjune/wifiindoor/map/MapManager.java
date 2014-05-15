package com.winjune.wifiindoor.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;

import com.winjune.wifiindoor.R;
import com.thoughtworks.xstream.XStream;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.types.MapManagerItemReply;
import com.winjune.wifiindoor.webservice.types.MapManagerReply;

/**
 * @author haleyshi
 *
 */
public class MapManager implements Serializable {
	private static final long serialVersionUID = 6444601145013696018L;
	
	public static int versionCode = 0;
	public static ArrayList<MapManagerItem> mapItems = new ArrayList<MapManagerItem>();
	
	
	//Number of the Maps
	public static int getItemNumber(){						
		return mapItems.size();
	}
	
	public static int getDefaultMapId(){
		return 2;
	}
	
	//Get the MapManagerItem by the Index
	public static MapManagerItem getMapByIndex(int index){		
		return mapItems.get(index);
	}	
	
	//Serialize current Map Manager to XML file
	public static boolean toXML(){
		//Serialize this object
		XStream xs = new XStream();
		
		File file = Util.openOrCreateFileInPath(IndoorMapData.MAP_FILE_PATH_LOCAL, IndoorMapData.MAP_MANAGER_FILE_NAME, false);
		
		if (file == null) {
			return false;
		}
		
		//Write to the manager file
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

	// Load from map manager file, 1st time loading use this
	public static boolean fromXML(InputStream map_file_is) {
		XStream xs = new XStream();
	
		try {			
			xs.fromXML(map_file_is);
			
			map_file_is.close();
			
			// Also copy file to device
			//toXML();
			
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	// Load from map manager file
	public static int fromXML() {
		XStream xs = new XStream();
		
		try {			
			FileInputStream fis = new FileInputStream(getFullPathForManagerFile());
			xs.fromXML(fis);
			fis.close();
		} catch (FileNotFoundException fmfex) {
			return IndoorMapData.FILE_RC_FILE_NOT_FOUND;
		} catch (IOException ioex){
			return IndoorMapData.FILE_RC_IO_ERROR;
		}
		
		return IndoorMapData.FILE_RC_OK;
	}
	
	private static  String getFullPathForManagerFile() {
		return Util.getFilePath(IndoorMapData.MAP_FILE_PATH_LOCAL) + IndoorMapData.MAP_MANAGER_FILE_NAME;
	}
	
	public static int getVersionCode() {
		return versionCode;
	}

	public static void setVersionCode(int versionCode) {
		MapManager.versionCode = versionCode;
	}		
}