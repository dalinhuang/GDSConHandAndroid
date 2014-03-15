package com.winjune.wifiindoor.navi;

import java.util.HashMap;
import java.util.Map;

public class DijkstraNode {
    private int id;
	private String name;
    private Map<DijkstraNode,Integer> child=new HashMap<DijkstraNode,Integer>();
    
    public DijkstraNode(int id, String name){
    	this.id = id;
        this.name=name;
    }
    
    public void clear(){
    	child.clear();
    }
    
    public int getId(){
    	return id;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	public Map<DijkstraNode, Integer> getChild() {
        return child;
    }
    public void setChild(Map<DijkstraNode, Integer> child) {
        this.child = child;
    }
}
