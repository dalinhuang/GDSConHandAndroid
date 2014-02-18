package com.winjune.wifiindoor.types;

import com.winjune.wifiindoor.webservice.types.IType;

public class VersionOrMapIdRequest implements IType{
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
