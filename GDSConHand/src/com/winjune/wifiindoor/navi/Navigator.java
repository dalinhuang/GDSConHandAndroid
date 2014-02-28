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
	private int maxNodeId = 0;
	private	int[][] weightMatrix;
	private String [][] routeDescMatrix;
	private int[] nodeId;
	private int[] nodeIndex;
	private boolean isReady = false;
	
	public void init(NaviInfo naviInfo, String unitStr ) {
		
		nodes = naviInfo.getNodes();
		paths = naviInfo.getPaths();
		nodeNum  = nodes.size();
		this.unitStr = unitStr;
		
		int i, j;
		
		// init the weightMatrix matrix of between every 2 nodes
		weightMatrix = new int[nodeNum][nodeNum];
		routeDescMatrix = new String[nodeNum][nodeNum];
		nodeId = new int[nodeNum];
		for (i = 0; i < nodeNum; i++){
			nodeId[i] = nodes.get(i).getId();
			
			// find the max node id and will use it later  
			if (nodeId[i] > maxNodeId) {
				maxNodeId = nodeId[i];
			}
			
			for (j = 0; j < nodeNum; j++)  {
				weightMatrix [i][j] = -1; // -1 means that there is no direct link between the 2 nodes
				routeDescMatrix[i][j] = "";
			}
		}
		
		// create a max node id table to store the node index
		nodeIndex = new int[maxNodeId+1];
		for (i = 0; i < maxNodeId; i++){
			nodeIndex[i] = -1;
		}
		
		i = 0;
		for (NaviNode node: nodes){			
			nodeIndex[node.getId()] = i ++;
		}
		
		for (NaviData route: paths) {
			int dist = (int) (route.getDistance() +0.5f);
			int startIndex = nodeIndex[route.getFrom()];
			int endIndex = nodeIndex[route.getTo()];
			
			// need to ensure the start node and end node are defined in node database
			if ((startIndex != -1) && (endIndex != -1)) {	
				weightMatrix[startIndex][endIndex] = dist;
				weightMatrix[endIndex][startIndex] = dist;
				routeDescMatrix[startIndex][endIndex] = route.getForwardInfo();
				routeDescMatrix[endIndex][startIndex] = route.getBackwardInfo();
			}
		}		
		
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
        
        if ((startNode > maxNodeId) || (endNode > maxNodeId)) {
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
        
        TrackNode pathIndex = null; 
        
        if (targetOptions.isEmpty()) { 
        	// only one target node
        	int startIndex = nodeIndex[startNode];
        	int endIndex = nodeIndex[endNode];
        
        	pathIndex = NaviUtil.Dijkstra2(weightMatrix, startIndex, endIndex);
		} else {
			TrackNode tmpPath;
			
			int startIndex = nodeIndex[startNode];
			for (NaviNode node : nodes) {	        	
	        	int endIndex = nodeIndex[node.getId()];	        
	        	tmpPath = NaviUtil.Dijkstra2(weightMatrix, startIndex, endIndex);
	        	
	        	// no path between startNode and this node
	        	if (tmpPath == null)
	        		continue;
	        	
	        	if (pathIndex == null){
	        		pathIndex = tmpPath;
	        	} else if(tmpPath.getDist() < pathIndex.getDist()) {
	        		// change to the target with the shorter path
	        		pathIndex = tmpPath;
	        	}
			}
		}               
        
        //The shortest path has been discovered, build the navi path        
        if (pathIndex != null ){
        	NaviPath path = new NaviPath(nodeNum);        	
        	
        	int tmpNodeId;
        	int prevNodeId;
        	int tmpNodeIdx;
        	int prevNodeIdx;
        	
        	int[] stepsIndex = pathIndex.getSteps();
          
            for (int i = 0; i < pathIndex.getStepSize(); i++) { 
            	           
            	tmpNodeIdx = stepsIndex[i];            	
            	tmpNodeId = nodeId[tmpNodeIdx];            	
            	          		
           		path.addStep(tmpNodeId);
           		
           		// build step description from step 2
           		if (i > 0) {
           			prevNodeIdx = stepsIndex[i-1];
           			prevNodeId = nodeId[prevNodeIdx];
           			
           			String stepDesc =  getNodeName(prevNodeId) + " ->-> " 
           						+ getNodeName(tmpNodeId) + ": " 
           						+ weightMatrix[prevNodeIdx][tmpNodeIdx] + unitStr + "\n"; // distance
           			
           			stepDesc += routeDescMatrix[prevNodeIdx][tmpNodeIdx] + "\n\n";
           			
           			path.appendPathDesc(stepDesc);
           		}
           		
            }                        
                        
            path.setDist(pathIndex.getDist());
            
            return path;
        } else {
        	//we don't find the end node index
        	Log.i("Navigator", "no path between the 2 nodes");
        	return null;
        }
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
}

