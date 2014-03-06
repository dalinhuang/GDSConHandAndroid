package com.winjune.wifiindoor.ads;

import java.util.List;

import com.winjune.common.webservice.core.types.IType;

public class AdGroup implements IType {
	private List<Ad> ads;
	
	public List<Ad> getAds() {
		return ads;
	}

	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}
}