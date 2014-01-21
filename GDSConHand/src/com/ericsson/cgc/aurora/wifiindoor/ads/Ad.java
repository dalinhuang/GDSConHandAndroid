package com.ericsson.cgc.aurora.wifiindoor.ads;

import com.ericsson.cgc.aurora.wifiindoor.webservice.types.IType;

public class Ad implements IType {
	private int id;
	private String thumbnailImgUrl;
	private String largeImgUrl;
	private String url;
	private int duration;
	private String fromDate;
	private String toDate;

	public int getDuration() {
		return duration;
	}

	public String getFromDate() {
		return fromDate;
	}

	public int getId() {
		return id;
	}

	public String getLargeImgUrl() {
		return largeImgUrl;
	}

	public String getThumbnailImgUrl() {
		return thumbnailImgUrl;
	}

	public String getToDate() {
		return toDate;
	}

	public String getUrl() {
		return url;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLargeImgUrl(String largeImgUrl) {
		this.largeImgUrl = largeImgUrl;
	}

	public void setThumbnailImgUrl(String thumbnailImgUrl) {
		this.thumbnailImgUrl = thumbnailImgUrl;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}