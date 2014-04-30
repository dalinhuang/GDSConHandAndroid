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
public class SearchHistory implements Serializable{
	private static final long serialVersionUID = 7866873863978783133L;

	private ArrayList<String> records = new ArrayList<String>();

	public String[] getHistoryArray(){
        String[] recordArray = new String[0];
           
        recordArray = records.toArray(recordArray);
        
        return recordArray;
	}
	
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
		String input =  searchInput.trim();
		
		if (searchInput != null) {	
			for (String record: records) {
				if (record.equals(input)) {
					// we first remove this record and then add it to the last
					records.remove(record); 					
					break;
				}
			}			
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
			FileOutputStream fos = new FileOutputStream(Util.getSearchHistoryFileName());
			xs.toXML(fos);
			
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
			FileInputStream fis = new FileInputStream(Util.getSearchHistoryFileName());
			
			xs.fromXML(fis);
			
			fis.close();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	
}
