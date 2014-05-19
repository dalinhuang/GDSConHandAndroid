package com.winjune.wifiindoor.navi;

import java.util.LinkedHashSet;

import android.util.Log;

import com.winjune.wifiindoor.lib.map.NaviNodeR;
import com.winjune.wifiindoor.lib.map.NaviNodeT;
import com.winjune.wifiindoor.lib.map.NaviPathR;
import com.winjune.wifiindoor.lib.map.NaviPathT;
import com.winjune.wifiindoor.navi.DijkstraNode;

public class DijkstraMap {
	private String LOG_TAG = "Navigator"; 
	
	LinkedHashSet<DijkstraNode> nodes;
    
    public void clear(){
    	
       	for (DijkstraNode node: nodes) {
    		node.clear();    		
    	}
       	
    	nodes.clear();
    }
    
    private void addNode(NaviNodeR node) {
    	
    	DijkstraNode dNode =new DijkstraNode(node.getId(), node.getLabel());  
    	
    	nodes.add(dNode);
    } 
    
    public DijkstraNode getNode(int nodeId) {
    	
    	for (DijkstraNode node: nodes) {
    		if (nodeId == node.getId()) {
    			return node;
    		}
    	}
    	
    	return null;
    }
    
    public LinkedHashSet<DijkstraNode> getNodes(){
    	return nodes;
    }
    
    private void addEdge(NaviPathR route){
    	int fromId = route.getFromNode();
    	int toId = route.getToNode();
    	int dist = route.getDistance();
    	
    	DijkstraNode fromNode = getNode(fromId);
    	DijkstraNode toNode = getNode(toId);
    	
    	if ((fromNode == null) || (toNode == null)) {
    		Log.e(LOG_TAG, "Wong route:"+ fromId+" -> "+toId);
    		return;
    	}    			
    	
    	// we are building a no-dir map
    	// add both the forward and backward edges
    	fromNode.getChild().put(toNode, dist);
    	toNode.getChild().put(fromNode, dist);
    }
    
    public DijkstraMap(NaviNodeT naviNodeTable, NaviPathT naviPathTable) {    	
    	this.nodes = new LinkedHashSet<DijkstraNode>();
    	
    	// build the map
    	for (NaviNodeR nNode: naviNodeTable.getNodes()) {
    		addNode(nNode);
    	}
    	
    	for (NaviPathR nRoute: naviPathTable.getPaths()) {
    		addEdge(nRoute);
    	}
    	
    }
}
