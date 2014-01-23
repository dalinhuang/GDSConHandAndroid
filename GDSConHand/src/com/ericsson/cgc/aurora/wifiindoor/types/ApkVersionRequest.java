package com.ericsson.cgc.aurora.wifiindoor.types;

public class ApkVersionRequest {
	private int versionCode;
	private String versionName;
	
	public ApkVersionRequest(int versionCode, String versionName) {
		setVersionCode(versionCode);
		setVersionName(versionName);
	}

	public int getVersionCode() {
		return versionCode;
	}
	
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	
	public String getVersionName() {
		return versionName;
	}
	
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
}
