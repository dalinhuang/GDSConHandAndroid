package com.ericsson.cgc.aurora.wifiindoor.map;

import java.io.Serializable;

import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;


// Obsoleted
/**
 * @author haleyshi
 *
 */
public class MapField implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6708200874276088730L;
	private int x;  // Here map the x to rows and y to columns to align with the bMatrix definition
	private int y;
	private int status;
	
	public int checkSanity() {
		return IndoorMapData.MAP_SANITY_RC_OK;
	}
	
	public int getStatus(){
		return status;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean isDoor(){
		if (status == IndoorMapData.NEAR_DOOR){
			return true;
		}
		
		return false;
	}
	
	// Decide if this is the Entry
	public boolean isEntry(){
		if (status == IndoorMapData.NEAR_ENTRY){
			return true;
		}
		
		return false;
	}
	
	// Decide if this is the Exit
	public boolean isExit(){
		if (status == IndoorMapData.NEAR_EXIT){
			return true;
		}
		
		return false;
	}
	
	// Decide if this cell is originally passable for Monsters
	public boolean isPassable(){
		if (status == IndoorMapData.NO_WAY){
			return false;
		}
		
		return true;
	}
	
	public boolean isRoom(){
		if (status == IndoorMapData.INNER_ROOM){
			return true;
		}
		
		return false;
	}
	
	// Decide if this is the wanted Field according to x, y
	public boolean isWanted(int x, int y){
		if ((this.x == x) & (this.y == y)){
			return true;
		}
		
		return false;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}

	
}
