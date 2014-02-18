package com.winjune.wifiindoor.map;

import java.io.Serializable;

public class InterestPlace implements Serializable {
	private static final long serialVersionUID = 3609353380870295280L;
	
	private int x;
	private int y;
	private String info;
	private String urlPic;
	private String urlAudio;
	private String urlVideo;

	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getUrlPic() {
		return urlPic;
	}

	public void setUrlPic(String urlPic) {
		this.urlPic = urlPic;
	}

	public String getUrlAudio() {
		return urlAudio;
	}

	public void setUrlAudio(String urlAudio) {
		this.urlAudio = urlAudio;
	}

	public String getUrlVideo() {
		return urlVideo;
	}

	public void setUrlVideo(String urlVideo) {
		this.urlVideo = urlVideo;
	}

}
