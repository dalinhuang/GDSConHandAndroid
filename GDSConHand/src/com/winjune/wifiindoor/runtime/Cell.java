package com.winjune.wifiindoor.runtime;

import java.util.ArrayList;

import org.andengine.entity.sprite.AnimatedSprite;


/**
 * @author haleyshi
 * 
 */
public class Cell {

	private int rowNo;
	private int colNo;
	
	private boolean passable;
	
	private AnimatedSprite backgroundSprite;
	
	private ArrayList<String> informations;
	private boolean infoPushed;
	private long infoPushTime;

	public Cell(int rowNo, int colNo) {
		this.rowNo = rowNo;
		this.colNo = colNo;
		setInfoPushed(false);
		setInformations(null);
		setInfoPushTime(0);
	}

	public int getRowNo() {
		return rowNo;
	}

	public int getColNo() {
		return colNo;
	}


	public void setPassable(boolean passable) {
		this.passable = passable;
	}
	
	public boolean isPassable() {
		return passable;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colNo;
		result = prime * result + rowNo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (colNo != other.colNo)
			return false;
		if (rowNo != other.rowNo)
			return false;
		return true;
	}

	public void setColNo(int colNo) {
		this.colNo = colNo;
	}

	@Override
	public String toString() {
		return "Cell [rowNo=" + rowNo + ", colNo=" + colNo + "]";
	}

	public AnimatedSprite getBackgroundSprite() {
		return backgroundSprite;
	}

	public void setBackgroundSprite(AnimatedSprite backgroundSprite) {
		this.backgroundSprite = backgroundSprite;
	}

	public boolean isInfoPushed() {
		return infoPushed;
	}

	public void setInfoPushed(boolean infoPushed) {
		this.infoPushed = infoPushed;
	}

	public ArrayList<String> getInformations() {
		return informations;
	}

	public void setInformations(ArrayList<String> informations) {
		this.informations = informations;
	}

	public long getInfoPushTime() {
		return infoPushTime;
	}

	public void setInfoPushTime(long infoPushTime) {
		this.infoPushTime = infoPushTime;
	}
	
	public void setInfo(ArrayList<String> informations) {
		setInfoPushed(true);
		setInformations(informations);
		setInfoPushTime(System.currentTimeMillis());
	}
	
	public String informationsToString(ArrayList<String> informations){
		if ((informations==null) || (informations.isEmpty())){
			return null;
		}
		
		String text = "";
		
		for (String information : informations){
			text += information + "\n";
		}
		
		return text;
	}
	
	public String informationsToString(){
		if ((informations==null) || (informations.isEmpty())){
			return null;
		}
		
		String text = "";
		
		for (String information : informations){
			text += information + "\n";
		}
		
		return text;
	}
	
	public boolean isSameInfo(ArrayList<String> informations){
		String sample = informationsToString(informations);
		String myInfo = informationsToString(this.informations);
		
		if (sample==null) {
			return true;
		}
		
		if (sample.equals(myInfo)){
			return true;
		}
		
		return false;
	}
	
	public boolean isRefreshInfoNeeded(){
		if (!isInfoPushed()) {
			setInfoPushed(true);
			infoPushTime = System.currentTimeMillis();
			return true;
		}
		
		// Refresh if more than 30 minutes
		if (System.currentTimeMillis() - infoPushTime > 1800000) {
			infoPushTime = System.currentTimeMillis();
			return true;
		}
		
		return false;
	}
	
}

