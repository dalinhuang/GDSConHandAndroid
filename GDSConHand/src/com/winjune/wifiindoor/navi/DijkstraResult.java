package com.winjune.wifiindoor.navi;

public class DijkstraResult {
	public int dist;	
	public String pathSteps;
	public String pathDesc;
	
	public DijkstraResult() {
		dist = Integer.MAX_VALUE;
		pathDesc = "";
		pathSteps = "";
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}
	
	public void setPathSteps(String steps){
		this.pathSteps = steps;
	}
	
	public void appendPathDesc(String tmpStr) {
		
		pathDesc += tmpStr;
	}
	
	public String getPathDesc(){
		return pathDesc;
	}
		
}
