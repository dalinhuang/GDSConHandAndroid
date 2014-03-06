package com.winjune.wifiindoor.webservice.types;

import com.winjune.common.webservice.core.types.IType;

public class VersionOrMapIdRequest implements IType{
	private int code;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
