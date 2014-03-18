package com.winjune.wifiindoor.navi;

import java.util.ArrayList;

import com.winjune.wifiindoor.util.Util;

import android.util.Log;

public class Navigator {
	
	private static String LOG_TAG = "Navigator";
	public 	NaviInfo naviInfo = null;
	private String[] spinnerNames;
	private int[] spinnerIdxToNodeId;
	private String unitStr = ""; // 我们用什么单位
	private boolean isReady = false;	
	
	public void init(NaviInfo ni, String unitStr ) {
		
		isReady = false;
		
		if (naviInfo == null)
			naviInfo = new NaviInfo();
				
		naviInfo.copy(ni);
		
		ArrayList<NaviNode> nodes = naviInfo.getNodes();
		ArrayList<NaviData> paths = naviInfo.getPaths();
		
		if (( nodes == null) || (paths == null)){
			Log.e(LOG_TAG, "No nodes are configred");
			return;
		}
		
		if ((nodes.size() == 0) || (paths.size() == 0)){
			Log.e(LOG_TAG, "No nodes are configred");
			return;
		}
		
		this.unitStr = unitStr;
		
		//filter out all invisible nodes 
		int count = 1;
		for (NaviNode node : nodes) {
			// we only show general node name
			if ( node.getNameId() == 0) { 
				count ++;
			}
		}
		
		// build the name list for spinner 
		// And build a reverse table to get node id by spinner index 
		spinnerNames = new String[count];
		spinnerIdxToNodeId = new int[count];
		count = 1;
		for (NaviNode node : nodes) {
			// we only show general node name
			if ( node.getNameId() == 0) {
				spinnerNames[count] = node.getName();
				spinnerIdxToNodeId[count] = node.getId(); 
				count ++;
			}
		}			
		
		// Navigator is enabled
		isReady = true;
	}
	
	public String[] getNodeSpinnerNames(){
		return spinnerNames;
	}
	
	public int getNodeIdBySpinnerIdx(int index) {
		
		if ((index < 0) || (index >= spinnerIdxToNodeId.length)) {
			Log.e(LOG_TAG, "Wrong spinner idex");
			return -1;
		}
				
		return spinnerIdxToNodeId[index];
	}
	
	// User selects 'My place'
	// we try to look for a nearest node according to his location
	public int getNearestNaviNode(int myPlaceX, int myPlaceY) {
		if ((myPlaceX == -1) || (myPlaceY == -1)) {
			Log.e(LOG_TAG, "Wrong place");
			return -1;
		}	
		
		int nodeNo = -1;
		int delta = Integer.MAX_VALUE;
		
		ArrayList<NaviNode> nodes = naviInfo.getNodes();
		for (NaviNode node: nodes) {
			if (node != null) {
				if (node.getMapId() == Util.getRuntimeIndoorMap().getMapId()) { // Same Map
					int delta2 = Math.abs(myPlaceX - node.getX()) + Math.abs(myPlaceY - node.getY());
					
					if (delta2 < delta) {
						delta = delta2;
						nodeNo = node.getId();
					}
					
					if (delta == 0) {
						return nodeNo;
					}
				}
			}
		}
		
		return nodeNo;
	}	

	
	public NaviPath getShortestPath(int startNode, int endNode) {
        		
        if (!isReady) {
        	Log.e(LOG_TAG, "Navigator is not ready");
        	return null;
        }
               
        // There may be a few end options
     	ArrayList<NaviNode>  targetOptions = new ArrayList<NaviNode>();	
        
		ArrayList<NaviNode> nodes = naviInfo.getNodes();
        for (NaviNode node : nodes) {
        	if (node.getId() == endNode){
        		if ((node.getX() != -1) && (node.getY() != -1)) {
        			//except for node with general names, other nodes with the 
        			//general name should not be treated as the target node, like entrance
        			break;
        		}        	
        	}        	
        	// 针对公共设施比如洗手间, 可能有多个洗手间,但是知有一个通用的显示, 选取最近的
        	// Firstly we found all possible nodes
        	if (node.getNameId() == endNode) {
        		targetOptions.add(node);
        	}
        }
        
        DijkstraPath pathPlanner = new DijkstraPath();        
        NaviPath path = null;        
        
        if (targetOptions.isEmpty()) {
        	
        	DijkstraMap map = new DijkstraMap(nodes, naviInfo.getPaths());
        	// only one target node
        	path = pathPlanner.planPath(map, startNode, endNode);
		} else {
			NaviPath tmpPath;
			
			for (NaviNode node : targetOptions) {
				DijkstraMap map = new DijkstraMap(nodes, naviInfo.getPaths());
				
	        	tmpPath = pathPlanner.planPath(map, startNode, node.getId());
	        	
	        	// no path between startNode and this node
	        	if (tmpPath == null)
	        		continue;
	        	
	        	if (path == null){
	        		path = tmpPath;
	        		
	        	} else if(tmpPath.getDist() < path.getDist()) {
	        		// change to the target with the shorter path
	        		path = tmpPath;
	        	}
			}
		}   
        
        return path;
	}	
	
	public String getNodeName( int nodeId) {
		ArrayList<NaviNode> nodes = naviInfo.getNodes();
		if (nodes == null) {
			return null;
		}
		
		for (NaviNode node : nodes) {
			if (node == null) {
				continue;
			}
			
			if (node.getId() == nodeId) {
				return "[ " + node.getName() + " ]";
			}
		}
		
		return null;
	}	
	
	public String getSpinnerName(int spinnerIdx) {
		
		return spinnerNames[spinnerIdx];
	}
}

