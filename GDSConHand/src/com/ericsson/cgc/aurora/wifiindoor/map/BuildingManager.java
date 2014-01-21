package com.ericsson.cgc.aurora.wifiindoor.map;

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

import com.ericsson.cgc.aurora.wifiindoor.R;
import com.ericsson.cgc.aurora.wifiindoor.types.BuildingManagerReply;
import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;
import com.thoughtworks.xstream.XStream;

/**
 * @author haleyshi
 *
 */
public class BuildingManager implements Serializable {
	private static final long serialVersionUID = -6470586431123595924L;
	
	private int versionCode;
	private ArrayList<Building> buildings;
	
	//Add an Item
	public void addItem(Building building){
		if (buildings == null){
			buildings = new ArrayList<Building>();
		}
			
		buildings.add(building);
	}
	
	// Load from map manager file
	public int fromXML() {
		XStream xs = new XStream();
		setAlias(xs);

		try {			
			FileInputStream fis = new FileInputStream(getFullPathForManagerFile());
			xs.fromXML(fis, this);
			fis.close();
		} catch (FileNotFoundException fmfex) {
			return IndoorMapData.FILE_RC_FILE_NOT_FOUND;
		} catch (IOException ioex){
			return IndoorMapData.FILE_RC_IO_ERROR;
		}
		
		return IndoorMapData.FILE_RC_OK;
	}
	
	// Load from map manager file, 1st time loading use this
	public boolean fromXML(InputStream map_file_is) {
		XStream xs = new XStream();
		setAlias(xs);

		try {			
			xs.fromXML(map_file_is, this);
			
			map_file_is.close();
			
			// Also copy file to device
			//toXML();
			
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return false;
	}
	
	public ArrayList<Building> getBuildings(){
		return buildings;
	}
	
	private String getFullPathForManagerFile() {
		return Util.getFilePath(IndoorMapData.MAP_FILE_PATH_LOCAL) + IndoorMapData.BUILDING_MANAGER_FILE_NAME;
	}

	//Get the Item by the Index
	public Building getItemByIndex(int index){
		if (buildings == null){
			return null;
		}
		
		if (index >= getItemNumber()) {
			return null;
		}
		
		return buildings.get(index);
	}	

	//Number of the Buildings
	public int getItemNumber(){
		if (buildings == null){
			return 0;
		}
		
		return buildings.size();
	}
	
	public int getVersionCode() {
		return versionCode;
	}
	
	@SuppressWarnings("deprecation")
	public boolean loadBuildingManager(final Activity activity, Resources resources) {
		try{
			if (fromXML() == IndoorMapData.FILE_RC_FILE_NOT_FOUND){
				InputStream map_file_is = new FileInputStream(getFullPathForManagerFile());
				//Read from map manager file
				if (!fromXML(map_file_is)){
					map_file_is.close();
					
					AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
			        alertDialog.setTitle(resources.getString(R.string.error_happens));
			        alertDialog.setMessage(resources.getString(R.string.map_file_broken));
			        alertDialog.setButton(resources.getString(R.string.ok), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int which) {
			        	   activity.finish();
			           }
			        });
			        alertDialog.setIcon(R.drawable.error);
			        alertDialog.show();
			        
					return false;
				}
				map_file_is.close();
			} 
			
			return true;
        } catch (Exception e){
        	e.printStackTrace();
        }
        
        return false;
	}

	public void mergeBuildings(BuildingManagerReply manager2) {
		if (manager2 == null) {
			return;
		}
		
		if (buildings == null) {
			buildings = manager2.toBuildings();
			return;
		}
		
		if (buildings.isEmpty()) {
			buildings = manager2.toBuildings();
			return;
		}
		
		ArrayList<Building> buildings2 = manager2.toBuildings();
		
		for (Building building2 : buildings2) {
			int id2 = building2.getId();
			boolean upd = false;
			
			for (Building building : buildings) {
				// Update or delete
				if (building.getId() == id2) {
					buildings.remove(building);
					
					if ((building2.getMaps() != null) && (building2.getMaps().isEmpty())) { // treat the null/empty mapList as delete
						buildings.add(building2);
					}
					
					upd = true;
					break;
				}
			}
			
			if (!upd) { // New Building
				buildings.add(building2);
			}
		}
	}

	//Remove an Item
	public void removeItem(Building building){
		buildings.remove(building);
	}
		
	//Set Alias for the XML serialization
	private void setAlias(XStream xs){
		xs.alias("BuildingManager", com.ericsson.cgc.aurora.wifiindoor.map.BuildingManager.class);
		xs.alias("Building", com.ericsson.cgc.aurora.wifiindoor.map.Building.class);		
	}

	public void setBuildings(ArrayList<Building> buildings) {
		this.buildings = buildings;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	//Serialize current Map Manager to XML file
	public boolean toXML(){
		//Serialize this object
		XStream xs = new XStream();
		setAlias(xs);
		
		File file = Util.openOrCreateFileInPath(IndoorMapData.MAP_FILE_PATH_LOCAL, IndoorMapData.BUILDING_MANAGER_FILE_NAME, false);
		
		if (file == null) {
			return false;
		}
		
		//Write to the manager file
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
}
