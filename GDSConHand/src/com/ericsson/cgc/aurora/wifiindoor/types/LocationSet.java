package com.ericsson.cgc.aurora.wifiindoor.types;

import java.util.ArrayList;

import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

public class LocationSet implements IType {
	private ArrayList<Location> locations;

	public ArrayList<Location> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}
	
	public Location balanceLocation() {
		if (locations == null) {
			return new Location(-1, -1, -1, -1);
		} else {
			if (locations.isEmpty()) {
				return new Location(-1, -1, -1, -1);
			} else {
				ArrayList<Location> cloneLocs = new ArrayList<Location>();
				
				for (Location location:locations) {
					if (location == null){
						continue;
					}
					
					if ((location.getX()==-1) || (location.getY()==-1) ) {
						continue;
					}
					
					cloneLocs.add(location);
				}
				
				if (cloneLocs.isEmpty()) {
					return new Location(-1, -1, -1, -1);
				}
				
				int[] counters = new int[cloneLocs.size()];
				int[] maps = new int[cloneLocs.size()];
				int[] versions = new int[cloneLocs.size()];
				int num = 0;
				
				for (Location location:locations) {
					boolean found = false;
					for (int i=0;i<num;i++) {
						if ((maps[i] == location.getMapId()) && (versions[i] == location.getMapVersion())) {
							counters[i]++;
							found = true;
						}
					}
					
					if (!found) {
						counters[num] = 1;
						maps[num] = location.getMapId();
						versions[num] = location.getMapVersion();
						num++;
					}
				}
				
				int bestIdx = -1;
				int bestCnt = -1;
				
				for (int i=0;i<num;i++) {
					if (counters[i] > bestCnt) {
						bestIdx = i;
						bestCnt = counters[i];
					}
				}
				
				if (bestIdx == -1) {
					return new Location(-1, -1, -1, -1);
				}
				
				locations.clear();
				
				int mapId = maps[bestIdx];
				int version = versions[bestIdx];
				float sumX = 0f;
				float sumY = 0f;		
				
				for (Location location:cloneLocs) {
					if (location.getMapId() == mapId) {
						sumX += location.getX();
						sumY += location.getY();
						locations.add(location);
					}
				}

				return new Location(mapId,  Math.round(sumX/bestCnt), Math.round(sumY/bestCnt), version);
			}
		}		
	}
	
}
