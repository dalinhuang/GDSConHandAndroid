package com.winjune.wifiindoor.navi;

import java.util.ArrayList;

import com.winjune.wifiindoor.lib.map.NaviNodeR;
import com.winjune.wifiindoor.lib.map.NaviNodeT;
import com.winjune.wifiindoor.lib.map.NaviPathR;
import com.winjune.wifiindoor.lib.map.NaviPathT;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.util.Util;

import android.util.Log;

public class Navigator {
	
	private static String LOG_TAG = "Navigator";
	
	private static boolean isReady = false;	
	
	private static NaviNodeT nodeTable;
	private static NaviPathT pathTable;	
	
	private static int myPlaceMapId = 0;
	private static int myPlaceLongitudeX = 0;;
	private static int myPlaceLatitudeY = 0;
		
	public static void init() {
		
		isReady = false;
		
		loadOfflineData();	
		
		// Navigator is enabled
		isReady = true;
	}
	
	public static ArrayList<NaviNodeR> go(int startPoiId, int endPoiId){
		NaviNodeR startNaviNode, endNaviNode, startNode, endNode;
		ArrayList<NaviNodeR> paths;
						
		if (startPoiId == 0) {
			startNaviNode = getNearestNaviNode(myPlaceMapId, myPlaceLongitudeX, myPlaceLatitudeY);
			
			startNode = new NaviNodeR(0, myPlaceMapId, myPlaceLongitudeX, myPlaceLatitudeY, "我的位置");			
		}
		else{
			PlaceOfInterest startPoi = POIManager.getPOIbyId(startPoiId);			
			if (startPoi == null) {
				Log.e(LOG_TAG, "Wrong start node ");
				return null;
			}
			
			startNaviNode = getNearestNaviNode(startPoi.mapId, startPoi.getPlaceX(), startPoi.getMapY());
			
			startNode = new NaviNodeR(0, startPoi.mapId, startPoi.getPlaceX(), startPoi.getMapY(), startPoi.getLabel());
		}
	
		PlaceOfInterest endPoi = POIManager.getPOIbyId(endPoiId);			
		if (endPoi == null){
			Log.e(LOG_TAG, "Wrong end node ");
			return null;	
		}
		
		endNaviNode = getNearestNaviNode(endPoi.mapId, endPoi.getPlaceX(), endPoi.getMapY());
		endNode = new NaviNodeR(0, endPoi.mapId, endPoi.getPlaceX(), endPoi.getMapY(), endPoi.getLabel());
		
		if ((startNaviNode == null) || (endNaviNode == null)){
			Log.e(LOG_TAG, "Wrong navi node ");		
			return null;
		}
		
		if (startNaviNode == endNaviNode){
			paths = new ArrayList<NaviNodeR>();			
			paths.add(startNaviNode);
			
		} else{					
			DijkstraResult dResults = getShortestPath(startNaviNode.getId(), endNaviNode.getId());
			if ((dResults == null) || (dResults.stepSize <=1)) {
				Log.e(LOG_TAG, "Wrong DijkstraResult ");
				return null;	
			}
			
			paths = buildPaths(dResults);
		}
		
		// add start node to the start of the list and end node to the end of the list
		paths.add(0, startNode);
		paths.add(endNode);
		
		return paths;		
	}
	
	public static void loadOfflineData(){
		
	}
	
	public static void setMyPosition(int mapId, int colNo, int rowNo){
		myPlaceMapId = mapId;
		
		myPlaceLongitudeX = colNo * Util.getRuntimeIndoorMap().getCellPixel() 
			     * (Util.getRuntimeIndoorMap().getMaxLongitude())/ (Util.getRuntimeIndoorMap().getMapWidth());
	
		myPlaceLatitudeY = rowNo * Util.getRuntimeIndoorMap().getCellPixel() 
		     	 * (Util.getRuntimeIndoorMap().getMaxLatitude())/ (Util.getRuntimeIndoorMap().getMapHeight());		
	}
	
	public static ArrayList<NaviNodeR> getNodes(){
		return nodeTable.getNodes();
	}
	
	public static ArrayList<NaviPathR> getPaths(){
		return pathTable.getPaths();
	}
		
	// User selects 'My place'
	// we try to look for a nearest node according to his location
	public static NaviNodeR getNearestNaviNode(int mapId, int placeX, int PlaceY) {
		NaviNodeR nearestNode = null;
		
		double nearestDist = Double.MAX_VALUE;
		double distX;
		double distY;
				
		for (NaviNodeR mNode:nodeTable.getNodes()) {
        	if (mNode.getMapId() == mapId) {        		  
        		distX =  Math.sqrt(Math.abs(mNode.getPlaceX() - placeX));
        		distY =  Math.sqrt(Math.abs(mNode.getPlaceY() - PlaceY));
        		double mDist = distX + distY;
        		if ( mDist < nearestDist) {
        			nearestNode = mNode;
        			nearestDist = mDist;
        		}        		        		 
        	}        	
		}								
		
		return nearestNode;
	}	
	

	public static DijkstraResult getShortestPath(int startNode, int endNode) {
        		
        if (!isReady) {
        	Log.e(LOG_TAG, "Navigator is not ready");
        	return null;
        }               
       
        DijkstraPath pathPlanner = new DijkstraPath();        
                        	
        DijkstraMap map = new DijkstraMap(nodeTable, pathTable);

        DijkstraResult path = pathPlanner.planPath(map, startNode, endNode);
        
        return path;
	}
	
	private static NaviNodeR getNaviNodeById(int id){
		for (NaviNodeR node: nodeTable.nodes){
			if (node.getId() == id)
				return node;
		}
		
		return null;
	}
	
	private static ArrayList<NaviNodeR> buildPaths(DijkstraResult results){
		ArrayList<NaviNodeR> paths = new ArrayList<NaviNodeR>();
		
		for (int i=0; i<results.stepSize; i++){
			NaviNodeR stepNode = getNaviNodeById(results.steps[i]);
		
			if (stepNode == null){				
				return null;				
			}
			paths.add(stepNode);			
		}
		
		return paths;
	}
	
	private static String getPathDesc(int fromNode, int toNode){
		return null;
	}
	
}

