package com.winjune.wifiindoor.algorithm;

public class NaviPath {
	public float dist;
	public int stepSize;
	public int[] steps;
	
	public NaviPath(int nodeNo) {
		stepSize = 0;
		dist = -1;
		steps = new int[nodeNo];
		
		// For sanity
		for (int i=0; i<nodeNo; i++) {
			steps[i] = -1;
		}
	}

	public float getDist() {
		return dist;
	}

	public void setDist(float dist) {
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
	
	public void removeLastStep(){
		stepSize --;
	}
	
	public NaviPath clone(){
		NaviPath temp = new NaviPath(this.steps.length);
		temp.stepSize = this.stepSize;
		temp.dist = this.dist;
		temp.steps = this.steps;
		
		return temp;
		
	}
	
	
	public void addFirstStep(int endNode, float distAdd) {
		steps[0] = endNode;
		stepSize = 1;	
		dist = distAdd;
	}
}
