package com.winjune.wifiindoor.poi;


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
 * @author jeffzha
 *
 */
public class SearchContext implements Serializable{
	private static final long serialVersionUID = 7866873863978783133L;
	
	private int id;
	private int versionCode;  // Reuse the map's versionCode, will cause re-download Map attributes and picture even only filed info changes. But I do not want to restructure the DB & runtimeIndoorMap
	private ArrayList<SearchResult> searchFields;

	public ArrayList<SearchResult> getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(ArrayList<SearchResult> searchFields) {
		this.searchFields = searchFields;
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
		}
