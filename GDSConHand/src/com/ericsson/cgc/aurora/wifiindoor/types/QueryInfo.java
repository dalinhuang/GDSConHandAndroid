/**
 * @(#)QueryInfo.java
 * Jun 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.ericsson.cgc.aurora.wifiindoor.types;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

/**
 * @author ezhipin
 *
 */
public class QueryInfo implements IType, Parcelable {
	private ArrayList<LocationQueryInfo> mInfos;
	
	public QueryInfo() {
		mInfos = new ArrayList<LocationQueryInfo>();
	}
	
	public void add(LocationQueryInfo info) {
		mInfos.add(info);
	}
	
	public int size() {
		return mInfos.size();
	}

	public ArrayList<LocationQueryInfo> getInfos() {
		return mInfos;
	}

	public void setInfos(ArrayList<LocationQueryInfo> infos) {
		mInfos = infos;
	}
	
	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
