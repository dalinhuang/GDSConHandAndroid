package com.winjune.wifiindoor.navi;

import java.util.ArrayList;

import com.winjune.wifiindoor.R;

import android.app.Activity;
import android.util.Log;

public class Navigator {
	
	private ArrayList<NaviNode> nodes;
	private ArrayList<NaviData> paths;
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
				routeDescMatrix[startIndex][endIndex] = route.getInfo();
				weightMatrix[endIndex][startIndex] = dist;
			}
		}
		
		isReady = true;
	}
	
	

	
	public NaviPath getShortestPath(int startNode, int endNode) {
        
        if (!isReady) {
        	return null;
        }
        
        if ((startNode > maxNodeId) || (endNode > maxNodeId)) {
        	return null;
        }
        
        int startIndex = nodeIndex[startNode];
        int endIndex = nodeIndex[endNode];
        
        TrackNode pathIndex = NaviUtil.Dijkstra2(weightMatrix, startIndex, endIndex);
    
        //The shortest path has been discovered.        
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

