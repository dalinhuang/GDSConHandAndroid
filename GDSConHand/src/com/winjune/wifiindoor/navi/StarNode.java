package com.winjune.wifiindoor.navi;

import com.winjune.wifiindoor.runtime.Cell;

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

	public void setSearchParent(StarNode searchParent) {
		this.searchParent = searchParent;
	}

	public StarNode getSearchParent() {
		return searchParent;
	}

	public void setLocation(Cell location) {
		this.location = location;
	}

	public Cell getLocation() {
		return this.location;
	}
}