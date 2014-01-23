package com.ericsson.cgc.aurora.wifiindoor.map;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;
import com.thoughtworks.xstream.XStream;

public class IndoorMap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8000576340428393784L;
	
	private String name;
	private String version;
	private int versionCode;
	private String editTime;
	private int id;
	private String pictureName;
	
	private InitialMap initialMap;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getVersion(){
		return version;
	}
	
	public void setVersion(String version){
		this.version = version;
	}
	
	public String getEditTime(){
		return editTime;
	}
	
	public void setEditTime(String editTime){
		this.editTime = editTime;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}
	
	public InitialMap getInitialMap(){
		return initialMap;
	}
	
	public void setInitialMap(InitialMap initialMap){
		this.initialMap = initialMap;
	}
	
	//Set Alias for the XML serialization
	private void setAlias(XStream xs){
		xs.alias("IndoorMap", com.ericsson.cgc.aurora.wifiindoor.map.IndoorMap.class);
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
		
		File file = Util.openOrCreateFileInPath(IndoorMapData.MAP_FILE_PATH_LOCAL+mapId+"/", IndoorMapData.MAP_XML_NAME, false);
		
		if (file == null) {
			return false;
		}
		
		//Write to the map file
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

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

}
