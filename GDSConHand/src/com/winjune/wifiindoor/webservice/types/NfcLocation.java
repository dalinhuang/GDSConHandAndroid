package com.winjune.wifiindoor.webservice.types;

public class NfcLocation {
	private String tagId;
	private Location location;

	public NfcLocation(String tagId, int mapId, int mNfcColNo, int mNfcRowNo, int mapVersion) {
		setTagId(tagId);
		location = new Location(mapId, mNfcColNo, mNfcRowNo, mapVersion);
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
