package com.ericsson.cgc.aurora.wifiindoor.types;

import java.util.ArrayList;

import com.ericsson.cgc.aurora.wifiindoor.map.InterestPlace;
import com.ericsson.cgc.aurora.wifiindoor.map.InterestPlacesInfo;
import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

public class InterestPlacesInfoReply implements IType {
	private int id;
	private int versionCode;  // Reuse the map's versionCode, will cause re-download Map attributes and picture even only filed info changes. But I do not want to restructure the DB & runtimeIndoorMap
	private ArrayList<InterestPlaceReply> fields;

	public ArrayList<InterestPlaceReply> getFields() {
		return fields;
	}

	public void setFields(ArrayList<InterestPlaceReply> fields) {
		this.fields = fields;
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
	
	public InterestPlacesInfo toInterestPlacesInfo() {
		InterestPlacesInfo interestPlacesInfo = new InterestPlacesInfo();
		
		interestPlacesInfo.setId(id);
		interestPlacesInfo.setVersionCode(versionCode);
		
		if (fields != null) {		
			ArrayList<InterestPlace> fields2 = new ArrayList<InterestPlace>();
			
			for (InterestPlaceReply field:fields) {
				InterestPlace field2 = new InterestPlace();
				
				if (field != null) {
					field2.setX(field.getX());
					field2.setY(field.getY());
					field2.setInfo(field.getInfo());
					field2.setUrlPic(field.getUrlPic());
					field2.setUrlAudio(field.getUrlAudio());
					field2.setUrlVideo(field.getUrlVideo());
				}
				
				fields2.add(field2);
			}
			
			interestPlacesInfo.setFields(fields2);
		}
			
		return interestPlacesInfo;
	}
}
