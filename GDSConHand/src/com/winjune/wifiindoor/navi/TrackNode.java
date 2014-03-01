package com.winjune.wifiindoor.navi;

public class TrackNode {
	private int totalDist;
	private int stepSize;
	private int[] steps;
		
	public TrackNode(int nodeNo) {
		totalDist = -1;
		stepSize = 0;
		steps = new int[nodeNo];
			
		// For sanity
		for (int i=0; i<nodeNo; i++) {
			steps[i] = -1;
		}
	}
	
	public void addStep(int endNode) {
		steps[stepSize] = endNode;
		stepSize++;				
	}
	
	public int getLastStep() {
		if (stepSize > 0) {
			return steps[stepSize-1];
		}else {
			return -1;
		}
	}
	
	public int[] getSteps() {
		return steps;
	}
	
	public int getStepSize() {
		return stepSize;
	}
	
	public int getDist() {
		return totalDist;
	}
	
	public void setDist(int dist) {
		this.totalDist = dist;
	}
}
