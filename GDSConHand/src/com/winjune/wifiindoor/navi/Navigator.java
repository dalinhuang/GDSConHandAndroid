package com.winjune.wifiindoor.navi;

import java.util.ArrayList;

import com.winjune.wifiindoor.util.Util;

import android.util.Log;

public class Navigator {
	
	private ArrayList<NaviNode> nodes;
	private ArrayList<NaviData> paths;
	private String[] spinnerNames;
	private int[] spinnerIdxToNodeId;
	private String unitStr = ""; // 我们用什么单位
	private int nodeNum;
	private boolean isReady = false;
	DijkstraMap map = null;
	
	public void init(NaviInfo naviInfo, String unitStr ) {
		
				
		nodes = naviInfo.getNodes();
		paths = naviInfo.getPaths();
		
		if (nodes == null)
		{
			Log.e("Navigator", "No nodes are configred");
			return;
		}
		
		nodeNum  = nodes.size();
		this.unitStr = unitStr;
		
		int count = 1;
		for (NaviNode node : nodes) {
			// we only show general node name
			if ( node.getNameId() == 0) { 
				count ++;
			}
		}
				
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
		
		map = new DijkstraMap(nodes, paths);
		
		isReady = true;
	}
	
	public String[] getNodeSpinnerNames(){
		return spinnerNames;
	}
	
	public int getNodeIdBySpinnerIdx(int index) {		
		return spinnerIdxToNodeId[index];
	}
	
	public int getNearestNaviNode(int myPlaceX, int myPlaceY) {
		if ((myPlaceX == -1) || (myPlaceY == -1)) {
			return -1;
		}	
		
		int nodeNo = -1;
		int delta = Integer.MAX_VALUE;
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
        	return null;
        }
               
        // There may be a few end options
     	ArrayList<NaviNode>  targetOptions = new ArrayList<NaviNode>();	
        
        for (NaviNode node : nodes) {
        	if (node.getId() == endNode){
        		if ((node.getX() != -1) && (node.getY() != -1)) {
        			//except for node with general names, other nodes with the 
        			//general name should not be treated as the target node, like entrance
        			break;
        		}        	
        	}        	
        	// 针对公共设施比如洗手间, 可能有多个洗手间,但是知有一个通用的显示, 选取最近的
        	if (node.getNameId() == endNode) {
        		targetOptions.add(node);
        	}
        }
        
        DijkstraPath pathPlanner = new DijkstraPath();        
        NaviPath path = null;
        
        
        if (targetOptions.isEmpty()) { 
        	// only one target node
        	path = pathPlanner.planPath(map, startNode, endNode);
		} else {
			NaviPath tmpPath;
			
			for (NaviNode node : targetOptions) {	        	
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

