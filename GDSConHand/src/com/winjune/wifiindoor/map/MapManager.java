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
import com.winjune.wifiindoor.types.MapManagerReply;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

/**
 * @author haleyshi
 *
 */
public class MapManager implements Serializable {
	private static final long serialVersionUID = 6444601145013696018L;
	
	private int versionCode;
	private ArrayList<MapManagerItem> downloadingMapItems;
	private ArrayList<MapManagerItem> updatedMapItems;
	private ArrayList<MapManagerItem> outdatedMapItems;
	
	// get ManagerItems from category
	private ArrayList<MapManagerItem> getMapManagerItems(int category){
		switch (category){
		case IndoorMapData.MAP_FILE_DOWNLOADING:
			return downloadingMapItems;
		case IndoorMapData.MAP_FILE_UPDATED:
			return updatedMapItems;
		case IndoorMapData.MAP_FILE_OUTDATED:
			return outdatedMapItems;
		default:
		}
		
		return null;
	}
	
	//Number of the Maps
	public int getMapManagerItemNumber(int category){
		ArrayList<MapManagerItem> mapManagerItems = getMapManagerItems(category);
		
		if (mapManagerItems == null){
			return 0;
		}
		
		return mapManagerItems.size();
	}
	
	//Get the MapManagerItem by the Index
	public MapManagerItem getMapManagerItemByIndex(int category, int index){
		ArrayList<MapManagerItem> mapManagerItems = getMapManagerItems(category);
		
		if (mapManagerItems == null){
			return null;
		}
		
		return mapManagerItems.get(index);
	}
	
	//Add a MapManagerItem
	public void addMapManagerItem(int category, MapManagerItem mapManagerItem){
		ArrayList<MapManagerItem> mapManagerItems = getMapManagerItems(category);
		
		if (mapManagerItems == null){
			mapManagerItems = new ArrayList<MapManagerItem>();
			
			switch (category){
			case IndoorMapData.MAP_FILE_DOWNLOADING:
				downloadingMapItems = mapManagerItems;
				break;
			case IndoorMapData.MAP_FILE_UPDATED:
				updatedMapItems = mapManagerItems;
				break;
			case IndoorMapData.MAP_FILE_OUTDATED:
				outdatedMapItems = mapManagerItems;
				break;
			default:
			}
		}
		
		mapManagerItems.add(mapManagerItem);
	}
	
	//Remove a MapManagerItem
	public void removeMapManagerItem(int category, MapManagerItem mapManagerItem){
		getMapManagerItems(category).remove(mapManagerItem);
	}
	
	//Set Alias for the XML serialization
	private void setAlias(XStream xs){
		xs.alias("MapManager", com.winjune.wifiindoor.map.MapManager.class);
		xs.alias("MapManagerItem", com.winjune.wifiindoor.map.MapManagerItem.class);		
	}

	//Serialize current Map Manager to XML file
	public boolean toXML(){
		//Serialize this object
		XStream xs = new XStream();
		setAlias(xs);
		
		File file = Util.openOrCreateFileInPath(IndoorMapData.MAP_FILE_PATH_LOCAL, IndoorMapData.MAP_MANAGER_FILE_NAME, false);
		
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
	
	private String getFullPathForManagerFile() {
		return Util.getFilePath(IndoorMapData.MAP_FILE_PATH_LOCAL) + IndoorMapData.MAP_MANAGER_FILE_NAME;
	}
	
	@SuppressWarnings("deprecation")
	public boolean loadMapManager(final Activity activity, Resources resources) {
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
        }
        
        return false;
	}

	public ArrayList<MapManagerItem> getDownloadingMapItems() {
		return downloadingMapItems;
	}

	public void setDownloadingMapItems(ArrayList<MapManagerItem> downloadingMapItems) {
		this.downloadingMapItems = downloadingMapItems;
	}

	public ArrayList<MapManagerItem> getOutdatedMapItems() {
		return outdatedMapItems;
	}

	public void setOutdatedMapItems(ArrayList<MapManagerItem> outdatedMapItems) {
		this.outdatedMapItems = outdatedMapItems;
	}

	public ArrayList<MapManagerItem> getUpdatedMapItems() {
		return updatedMapItems;
	}

	public void setUpdatedMapItems(ArrayList<MapManagerItem> updatedMapItems) {
		this.updatedMapItems = updatedMapItems;
	}

	public MapManager getSubMapManager(int[] mapsInBuilding) {
		 
		if (mapsInBuilding == null) {
			return this;
		}
		
		MapManager subMapManager = new MapManager();
		subMapManager.setVersionCode(versionCode);
		subMapManager.updatedMapItems = new ArrayList<MapManagerItem>();
		subMapManager.outdatedMapItems = new ArrayList<MapManagerItem>();
		subMapManager.downloadingMapItems = new ArrayList<MapManagerItem>();
		
		if (updatedMapItems != null) {		
			for(int i=0; i<updatedMapItems.size(); i++)   
			{ 
				 MapManagerItem item = updatedMapItems.get(i);
				 if (!itemOf(mapsInBuilding, item.getMapId())){
			    	continue; // Not list this Map if it is not belongs to maps
			     }
				 subMapManager.addMapManagerItem(IndoorMapData.MAP_FILE_UPDATED, item);
			}
		}
		
		if (outdatedMapItems!=null) {
			for(int i=0; i<outdatedMapItems.size(); i++)   
			{ 			 
				 MapManagerItem item = outdatedMapItems.get(i);
				 if (!itemOf(mapsInBuilding, item.getMapId())){
			    	continue; // Not list this Map if it is not belongs to maps
			     }
				 subMapManager.addMapManagerItem(IndoorMapData.MAP_FILE_OUTDATED, item);
			}
		}
		 
		if (downloadingMapItems !=null)
		{
			for(int i=0; i<downloadingMapItems.size(); i++)   
			{ 
				 MapManagerItem item = downloadingMapItems.get(i);
				 if (!itemOf(mapsInBuilding, item.getMapId())){
			    	continue; // Not list this Map if it is not belongs to maps
			     }
				 subMapManager.addMapManagerItem(IndoorMapData.MAP_FILE_DOWNLOADING, item);
			}
		}

		return subMapManager;
	}
	
	private boolean itemOf(int[] maps, int mapId) {
		if (maps == null) {
			return true;
		}
		
		for (int i=0;i<maps.length;i++){
			if (mapId == maps[i]){
				return true;
			}
		}
		
		return false;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public void mergeMaps(MapManagerReply manager2) {
		if (manager2 == null) {
			return;
		}
		
		if (updatedMapItems == null) {
			updatedMapItems = manager2.toMapItems();
			return;
		}
		
		ArrayList<MapManagerItem> items2 = manager2.toMapItems();
		
		for (MapManagerItem item2:items2) {
			int id2 = item2.getMapId();
			boolean upd = false;
			
			for (MapManagerItem item:updatedMapItems) {
				// Update or delete
				if (item.getMapId() == id2) {
					updatedMapItems.remove(item);
					
					if ((item2.getColumns() != -1) && (item2.getRows() !=-1 )) { // let # of columns/rows with -1 as a indicator to delete the map
						updatedMapItems.add(item2);
					}
					
					upd = true;
					break;
				}
			}
			
			if (!upd) { // New
				updatedMapItems.add(item2);
			}
		}
	}
	
}