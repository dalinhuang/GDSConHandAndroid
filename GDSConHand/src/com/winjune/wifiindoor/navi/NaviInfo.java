package com.winjune.wifiindoor.navi;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

/**
 * @author haleyshi
 *
 */
public class NaviInfo implements Serializable{
	private static final long serialVersionUID = -1466799645587786272L;
	
	private int id;
	private int versionCode;  // Reuse the map's versionCode, will cause re-download Map attributes and picture even only navi. info changes. But I do not want to restructure the DB & runtimeIndoorMap
	private ArrayList<NaviNode> nodes;
	private ArrayList<NaviData> paths;

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<NaviNode> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<NaviNode> nodes) {
		this.nodes = nodes;
	}
	
	public ArrayList<NaviData> getPaths() {
		return paths;
	}

	public void setPaths(ArrayList<NaviData> paths) {
		this.paths = paths;
	}
	
	//Set Alias for the XML serialization
	private void setAlias(XStream xs){
		xs.alias("NaviInfo", com.winjune.wifiindoor.navi.NaviInfo.class);
		xs.alias("NaviNode", com.winjune.wifiindoor.navi.NaviNode.class);
		xs.alias("NaviData", com.winjune.wifiindoor.navi.NaviData.class);
		//Invoke other objects' setAlias methods here
	}
	
	public boolean toXML(){
		return toXML(id);
	}
	
	//Serialize current IndoorMap to XML file
	private boolean toXML(int mapId){
		//Serialize this object
		XStream xs = new XStream();
		setAlias(xs);
		
		File file = Util.openOrCreateFileInPath(IndoorMapData.MAP_FILE_PATH_LOCAL+mapId+"/", IndoorMapData.NAVI_INFO_FILE_NAME, false);
		
		if (file == null) {
			return false;
		}
		
		//Write to the map info file
		try{
			FileOutputStream fos = new FileOutputStream(file);
			xs.toXML(this, fos);
			
			fos.close();
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//Set current InoorMap from XML file
	public boolean fromXML(InputStream map_file_is){
		XStream xs = new XStream();
		setAlias(xs);

		try {
			xs.fromXML(map_file_is, this);
			
			map_file_is.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	//Set current IndoorMap from XML file
	public boolean fromXML(String map_file_path){
		XStream xs = new XStream();
		setAlias(xs);

		try {
			FileInputStream fis = new FileInputStream(map_file_path);
			xs.fromXML(fis, this);
			
			fis.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	//test
	public String toString(){
		//Serialize this object
		XStream xs = new XStream();
		setAlias(xs);
		return xs.toXML(this);
	}

}