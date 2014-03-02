package com.winjune.wifiindoor.navi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.winjune.wifiindoor.navi.DijkstraNode;
import com.winjune.wifiindoor.navi.NaviNode;
import com.winjune.wifiindoor.navi.NaviData;

public class DijkstraMap {
    Set<DijkstraNode> nodes = new HashSet<DijkstraNode>();
    
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
    
    public Set<DijkstraNode> getNodes(){
    	return nodes;
    }
    
    private void addEdge(NaviData route){
    	int fromId = route.getFrom();
    	int toId = route.getTo();
    	int dist = route.getDistance();
    	
    	DijkstraNode fromNode = getNode(fromId);
    	DijkstraNode toNode = getNode(toId);
    	
    	fromNode.getChild().put(toNode, dist);
    	toNode.getChild().put(fromNode, dist);
    }
    
    public DijkstraMap(ArrayList<NaviNode> nodes, ArrayList<NaviData> routes) {
    	
    	for (NaviNode nNode: nodes) {
    		addNode(nNode);
    	}
    	
    	for (NaviData nRoute: routes) {
    		addEdge(nRoute);
    	}
    	
    }
}
