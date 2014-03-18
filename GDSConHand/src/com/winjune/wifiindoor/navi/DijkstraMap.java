package com.winjune.wifiindoor.navi;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import android.util.Log;

import com.winjune.wifiindoor.navi.DijkstraNode;
import com.winjune.wifiindoor.navi.NaviNode;
import com.winjune.wifiindoor.navi.NaviData;

public class DijkstraMap {
	private String LOG_TAG = "Navigator"; 
	
	LinkedHashSet<DijkstraNode> nodes;
    
    public void clear(){
    	
       	for (DijkstraNode node: nodes) {
    		node.clear();    		
    	}
       	
    	nodes.clear();
    }
    
    private void addNode(NaviNode node) {
    	
    	DijkstraNode dNode =new DijkstraNode(node.getId(), node.getName());  
    	
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
    
    private void addEdge(NaviData route){
    	int fromId = route.getFrom();
    	int toId = route.getTo();
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
    
    public DijkstraMap(ArrayList<NaviNode> nodes, ArrayList<NaviData> routes) {    	
    	this.nodes = new LinkedHashSet<DijkstraNode>();
    	
    	// build the map
    	for (NaviNode nNode: nodes) {
    		addNode(nNode);
    	}
    	
    	for (NaviData nRoute: routes) {
    		addEdge(nRoute);
    	}
    	
    }
}
