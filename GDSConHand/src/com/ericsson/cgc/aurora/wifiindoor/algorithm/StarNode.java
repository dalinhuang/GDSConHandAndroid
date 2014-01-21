package com.ericsson.cgc.aurora.wifiindoor.algorithm;

import com.ericsson.cgc.aurora.wifiindoor.runtime.Cell;

public class StarNode {

	private Cell location;

	private StarNode searchParent;

	public StarNode(Cell location) {
		this.setLocation(location);
	}

	public boolean equals(Object obj) {
		if (obj instanceof StarNode) {
			StarNode n = (StarNode) obj;
			if (this.location.equals(n.location)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public Cell getLocation() {
		return this.location;
	}

	public StarNode getSearchParent() {
		return searchParent;
	}

	public void setLocation(Cell location) {
		this.location = location;
	}

	public void setSearchParent(StarNode searchParent) {
		this.searchParent = searchParent;
	}
}
