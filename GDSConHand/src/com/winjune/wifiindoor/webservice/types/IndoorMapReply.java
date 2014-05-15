package com.winjune.wifiindoor.webservice.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import com.thoughtworks.xstream.XStream;
import com.winjune.common.webservice.core.types.IType;
import com.winjune.wifiindoor.map.IndoorMap;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

public class IndoorMapReply implements IType, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 87208639628090845L;
	private int id;
	private String normalMapUrl;
	private String largeMapUrl;
	private String name;
	private String label;	
	private int cellPixel;
	private int longitude;
	private int latitude;
	private int versionCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	
	public String getNormalMapUrl() {
		return normalMapUrl;
	}

	public void setNormalMapUrl(String normalMapUrl) {
		this.normalMapUrl = normalMapUrl;
	}

	public String getLargeMapUrl() {
		return largeMapUrl;
	}

	public void setLargeMapUrl(String largeMapUrl) {
		this.largeMapUrl = largeMapUrl;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getCellPixel() {
		return cellPixel;
	}

	public void setCellPixel(int cellPixel) {
		this.cellPixel = cellPixel;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public void getRowsAndColumns(int[] rowsAndcols) {
		if ((normalMapUrl != null) && (!normalMapUrl.isEmpty())) {
			String pieces[] = normalMapUrl.trim().split(";");
			
			//Get last piece
			if (pieces.length > 0)
			{
				String lastPiece = pieces[pieces.length-1];
				String attrs[] = lastPiece.split(",");
				if (attrs.length == IndoorMapData.ATTR_NUMBER_PER_MAP_PIECE) {
					int left = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_LEFT].trim());
					int top = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_TOP].trim());
					int width = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_WIDTH].trim());
					int height = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_HEIGHT].trim());					
					rowsAndcols[0] = (top+height)/cellPixel;
					rowsAndcols[1] = (left+width)/cellPixel;
				}
			}
		}
	}

	public int getColumns() {
		return latitude;
	}

	//Set Alias for the XML serialization
	private void setAlias(XStream xs){
		xs.alias("IndoorMap", com.winjune.wifiindoor.map.IndoorMap.class);
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

}
