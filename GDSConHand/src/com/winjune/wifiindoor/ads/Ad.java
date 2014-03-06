package com.winjune.wifiindoor.ads;

import com.winjune.common.webservice.core.types.IType;

public class Ad implements IType {
	private int id;
	private String thumbnailImgUrl;
	private String largeImgUrl;
	private String url;
	private int duration;
	private String fromDate;
	private String toDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getThumbnailImgUrl() {
		return thumbnailImgUrl;
	}

	public void setThumbnailImgUrl(String thumbnailImgUrl) {
		this.thumbnailImgUrl = thumbnailImgUrl;
	}

	public String getLargeImgUrl() {
		return largeImgUrl;
	}

	public void setLargeImgUrl(String largeImgUrl) {
		this.largeImgUrl = largeImgUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
}