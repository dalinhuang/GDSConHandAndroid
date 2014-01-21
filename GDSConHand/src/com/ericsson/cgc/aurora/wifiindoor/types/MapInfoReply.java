package com.ericsson.cgc.aurora.wifiindoor.types;

import java.util.ArrayList;

import com.ericsson.cgc.aurora.wifiindoor.map.FieldInfo;
import com.ericsson.cgc.aurora.wifiindoor.map.MapInfo;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

public class MapInfoReply implements IType {
	private int id;
	private int versionCode;
	private ArrayList<FieldInfoReply> fields;

	public ArrayList<FieldInfoReply> getFields() {
		return fields;
	}

	public int getId() {
		return id;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setFields(ArrayList<FieldInfoReply> fields) {
		this.fields = fields;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	
	public MapInfo toMapInfo() {
		MapInfo mapInfo = new MapInfo();
		
		mapInfo.setId(id);
		mapInfo.setVersionCode(versionCode);
		
		if (fields != null) {		
			ArrayList<FieldInfo> fields2 = new ArrayList<FieldInfo>();
			
			for (FieldInfoReply field:fields) {
				FieldInfo field2 = new FieldInfo();
				
				if (field != null) {
					field2.setX(field.getX());
					field2.setY(field.getY());
					field2.setInfo(field.getInfo());
					field2.setScale(field.getScale());
					field2.setAlpha(field.getAlpha());
					field2.setRotation(field.getRotation());
				}
				
				fields2.add(field2);
			}
			
			mapInfo.setFields(fields2);
		}
			
		return mapInfo;
	}
}
