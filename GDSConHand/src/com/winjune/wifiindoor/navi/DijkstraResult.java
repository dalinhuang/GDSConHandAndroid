package com.winjune.wifiindoor.navi;

public class DijkstraResult {
	public int dist;
	public int stepSize;
	public int[] steps;
	public String pathDesc;
	
	public DijkstraResult(int nodeNo) {
		stepSize = 0;
		dist = -1;
		steps = new int[nodeNo];
		pathDesc = "";
		
		// For sanity
		for (int i=0; i<nodeNo; i++) {
			steps[i] = -1;
		}
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}

	public int getStepSize() {
		return stepSize;
	}

	public void setStepSize(int stepSize) {
		this.stepSize = stepSize;
	}

	public int[] getSteps() {
		return steps;
	}

	public void setSteps(int[] steps) {
		this.steps = steps;
	}
	
	public void addStep(int endNode, float distAdd) {
		steps[stepSize] = endNode;
		stepSize++;
		
		dist += distAdd;
	}
	
	public void addStep(int endNode) {
		steps[stepSize] = endNode;
		stepSize++;				
	}
	
	public void appendPathDesc(String tmpStr) {
		
		pathDesc += tmpStr;
	}
	
	public String getPathDesc(){
		return pathDesc;
	}
		
}
