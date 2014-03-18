package com.winjune.wifiindoor.navi;

import java.util.LinkedHashMap;

public class DijkstraNode {
    private int id;
	private String name;
    private LinkedHashMap<DijkstraNode,Integer> child;
    
    public DijkstraNode(int id, String name){
    	this.id    = id;
        this.name  = name;
        this.child = new LinkedHashMap<DijkstraNode,Integer>();
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
	public LinkedHashMap<DijkstraNode, Integer> getChild() {
        return child;
    }
    public void setChild(LinkedHashMap<DijkstraNode, Integer> child) {
        this.child = child;
    }
}
