package com.winjune.wifiindoor.poi;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.Collections;
import com.thoughtworks.xstream.XStream;
import com.winjune.wifiindoor.util.Util;

/**
 * @author jeffzha
 *
 */
// To do list
// the records needs to be sorted reversely
// before add new record, need to check if it is already in the records, if yes, need to adjust the order
public class RouteSearchHistory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7751052119801300610L;
	public ArrayList<String> records = new ArrayList<String>();

	public void loadCachedData() {
		fromXML();
	}
	
	public ArrayList<String> getHistory(){
		// we need to reverse the records, so the record 
		// will be displayed according to time
		ArrayList<String>  reversedRecords = records;
		
		Collections.reverse(reversedRecords);
		
		return reversedRecords;
	}
	
	public void addRecord(String searchInput) {		
		if (searchInput == null)
			return;
		
		System.out.println("searchInput" + searchInput);
		
		String input =  searchInput.trim();
		
		if (!input.isEmpty()) {	
			// check if there is same record existing
			// we first remove this record and then add it to the end of the list
			records.remove(input);
			records.add(input);
			toXML();
		}
	}
		
	public void clearRecords(){
		records.clear();
		toXML();
	}
	
	
	//Serialize current IndoorMap to XML file
	private boolean toXML(){
		//Serialize this object
		XStream xs = new XStream();
				
		//Write to the map info file
		try{
			FileOutputStream fos = new FileOutputStream(Util.getRouteSearchHistoryFileName());
			
			xs.toXML(this, fos);
			
			fos.close();
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
		
	//Set current IndoorMap from XML file
	private boolean fromXML(){
		XStream xs = new XStream();

		try {
			FileInputStream fis = new FileInputStream(Util.getRouteSearchHistoryFileName());
			
			xs.fromXML(fis, this);
			
			fis.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	
}
