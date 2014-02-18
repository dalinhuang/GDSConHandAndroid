package com.winjune.wifiindoor.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.winjune.wifiindoor.webservice.types.IType;
import com.winjune.wifiindoor.webservice.util.ParcelUtils;

public class ApkVersionReply implements IType, Parcelable {
	private int versionCode;
	private String versionName;
	private String apkUrl;
	
	public ApkVersionReply(int versionCode, String versionName, String apkUrl) {
		setVersionCode(versionCode);
		setVersionName(versionName);
		setApkUrl(apkUrl);
	}
	
	public ApkVersionReply(Parcel in) {
		versionCode = in.readInt();
		versionName = ParcelUtils.readStringFromParcel(in);
		apkUrl = ParcelUtils.readStringFromParcel(in);
	}
	
    public static final Parcelable.Creator<ApkVersionReply> CREATOR = new Parcelable.Creator<ApkVersionReply>() {
        public ApkVersionReply createFromParcel(Parcel in) {
            return new ApkVersionReply(in);
        }

        @Override
        public ApkVersionReply[] newArray(int size) {
            return new ApkVersionReply[size];
        }
    };
	
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

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	
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
		dest.writeInt(versionCode);
		ParcelUtils.writeStringToParcel(dest, versionName);
		ParcelUtils.writeStringToParcel(dest, apkUrl);
	}

	@Override
	public String toString() {
		return "ApkVersion [versionCode=" + versionCode + ", versionName=" + versionName + "]";
	}
}
