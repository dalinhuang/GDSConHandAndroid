package com.winjune.wifiindoor.webservice.types;

import java.util.ArrayList;

import com.winjune.common.webservice.core.types.IType;
import com.winjune.wifiindoor.map.CellCollectStatus;
import com.winjune.wifiindoor.map.MapCollectStatus;

public class CollectStatusReply implements IType{

	private int mapId;
	private int versionCode;
	private ArrayList<CellStatusReply> cells;
	
	public ArrayList<CellStatusReply> getCells() {
		return cells;
	}

	public void setCells(ArrayList<CellStatusReply> cells) {
		this.cells = cells;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int id) {
		this.mapId = id;
	}
	
	public MapCollectStatus toMapCollectStatus() {
		
		MapCollectStatus mcs = new MapCollectStatus();
		
		mcs.setMapId(mapId);
		mcs.setVersionCode(versionCode);
		
		if (cells != null) {		
			ArrayList<CellCollectStatus> cells2 = new ArrayList<CellCollectStatus>();
			
			for (CellStatusReply cell:cells) {
				CellCollectStatus cell2 = new CellCollectStatus();
				
				if (cell != null) {
					cell2.setSerial(cell.getSerial());
					cell2.setX(cell.getX());
					cell2.setY(cell.getY());
					cell2.setCount(cell.getCount());
				}
				
				cells2.add(cell2);
			}
			
			mcs.setCells(cells2);
		}
		
		return mcs;
	}
}
