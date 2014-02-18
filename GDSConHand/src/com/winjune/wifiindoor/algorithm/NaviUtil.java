package com.winjune.wifiindoor.algorithm;

import java.util.ArrayList;

import com.winjune.wifiindoor.map.NaviData;
import com.winjune.wifiindoor.map.NaviNode;

public class NaviUtil {
	public static NaviPath getBestNaviPath(ArrayList<NaviData> routes, int start, int end) {
		if (routes == null) {
			return null;
		}
		
		if (routes.isEmpty()) {
			return null;
		}
		
		ArrayList<NaviPath> tempPaths = new ArrayList<NaviPath>();
		
		for (NaviData route : routes) { // Add new Steps
			if (route.getFrom() == start) { // with the newStart
				NaviPath path = new NaviPath(routes.size());
				path.addFirstStep(route.getTo(), route.getDistance());
				tempPaths.add(path);
			}
		}
		
		boolean needSearchAhead = true;
		while (needSearchAhead) {
			needSearchAhead = false;
			
			if (tempPaths.isEmpty()) {
				return null;
			}
			
			ArrayList<NaviPath> tempPaths2 = new ArrayList<NaviPath>();
					
			for (NaviPath path : tempPaths) {	
				int thisStart = path.getSteps()[path.getStepSize()-1]; // New start for this round
				
				if (thisStart == end) { // Already reach end
					tempPaths2.add(path);
					continue;
				}
				
				needSearchAhead = true; // Not all routes are reaching the end
				
				for (NaviData route : routes) { // Add new Steps
					if (route.getFrom() == thisStart) { // with the newStart				
						boolean alreadyInclude = false;
						
						if (start == route.getTo()) {
							continue;
						}
						
						for (int i=0; i<path.getStepSize(); i++) {
							if (path.getSteps()[i] == route.getTo()) {
								alreadyInclude = true;
								break;
							}
						}
						
						if (alreadyInclude) {
							continue;
						}
						
						NaviPath path2 = path;
						path2.addEnd(route.getTo(), route.getDistance());
						tempPaths2.add(path2);
					}
				}
			}
			
			if (tempPaths2.isEmpty()) {
				return null;
			}
			
			// Filter to get the best temp path with the same start-end pair
			boolean needNextLoop = true;
			while (needNextLoop) {
				needNextLoop = false;
				
				int thisEnd = tempPaths2.get(0).getSteps()[tempPaths2.get(0).getStepSize()-1];
				float thisDist = tempPaths2.get(0).getDist();
				
				for (int i=tempPaths2.size()-1; i>0; i--) {
					if (tempPaths2.get(i).getSteps()[tempPaths2.get(i).getStepSize()-1] == thisEnd) { // same start-end pair with 1st Node
						if (tempPaths2.get(i).getDist() < thisDist) {
							tempPaths2.set(0, tempPaths2.get(i));
						} 
						
						tempPaths2.remove(i);
						needNextLoop = true;
					}
				}
			} //needNextLoop
			
			tempPaths = tempPaths2;
		} // needSearchAhead
		
		if (tempPaths.isEmpty()) {
			return null;
		}
		
		return tempPaths.get(0);
	}

	public static String getNodeName(ArrayList<NaviNode> nodes, int fromNode) {
		if (nodes == null) {
			return null;
		}
		
		for (NaviNode node : nodes) {
			if (node == null) {
				continue;
			}
			
			if (node.getId() == fromNode) {
				return node.getName();
			}
		}
		
		return null;
	}

	public static String getPathDescription(ArrayList<NaviData> paths, int from, int to, String meter) {
		if (paths == null) {
			return null;
		}
	
		for (NaviData path : paths) {
			if (path == null) {
				continue;
			}
			
			if ((path.getFrom() == from) && (path.getTo() == to)) { // with the start and end
				return "[" + path.getDistance() + meter + "]" + path.getInfo();
			}
		}
		
		return null;
	}


}
