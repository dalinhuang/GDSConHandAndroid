package com.ericsson.cgc.aurora.wifiindoor.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andengine.entity.sprite.AnimatedSprite;

import com.ericsson.cgc.aurora.wifiindoor.R;
import com.ericsson.cgc.aurora.wifiindoor.algorithm.PathSearcher;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;

public class RuntimeIndoorMap {
	
	private String mapName;
	private int mapId;
	private String mapVersion;
	private int versionCode;
	private String mapPictureName;
	
	private int cellPixel;	
	private int rowNum;
	private int colNum;
	private RuntimeUser user;
	private RuntimeUser target;
	private ArrayList<RuntimeUser> tracks;
	
	private Cell[][] cells;
	private Cell startingPosition;
	private Cell targetPosition;
	@SuppressWarnings("unused")
	private List<Cell> cachedPathFromStartingPosition;
	private Map<Cell, Boolean> keyCellsMap = new HashMap<Cell, Boolean>();
	
	private List<RuntimeIndoorMapListener> listeners = new ArrayList<RuntimeIndoorMapListener>();
	
	private ArrayList<String> informations;
	private boolean infoPushed;
	private long infoPushTime;

	public RuntimeIndoorMap(Cell[][] cellMatrix, String mapName, 
			int mapId, String mapVersion, String mapPictureName, int cellPixel, int versionCode) {
		this.cells = cellMatrix;
		this.rowNum = cells.length;
		this.colNum = cells[0].length;
		this.mapName = mapName;
		this.mapId = mapId;
		this.mapVersion = mapVersion;
		this.mapPictureName = mapPictureName;
		setVersionCode(versionCode);
		setCellPixel(cellPixel);
		
		// No need. Only do this when on Navigating
		//this.startingPosition = startingPosition;
		//this.targetPosition = targetPosition;
		//findPathFromStartPositionAndCachePath();
		
		setInfoPushed(false);
		setInformations(null);
		setInfoPushTime(0);
	}

	public Cell[][] getCells() {
		return cells;
	}


	public void initial(){
		for (RuntimeIndoorMapListener l : listeners) {
			l.initial(this);
		}
	}
	
	public Cell getCellAt(int rowNo, int colNo) {
		if (rowNo < 0 || rowNo > rowNum - 1){
			return null;
		}
		if (colNo < 0 || colNo > colNum - 1){
			return null;
		}
		return cells[rowNo][colNo]; // x,y, Cells has the revert x, y as colNo, rowNo
	}
	
	public void addListener(RuntimeIndoorMapListener listener) {
		this.listeners.add(listener);
	}

	public boolean removeListener(RuntimeIndoorMapListener listener) {
		return this.listeners.remove(listener);
	}
	
	public int getRowNum() {
		return rowNum;
	}

	public int getColNum() {
		return colNum;
	}
	
	public boolean[][] getPassableMatrix() {

		// TODO to cache the boolean matrix

		boolean[][] bMatrix = new boolean[cells.length][];

		for (int i = 0; i < cells.length; i++) {
			Cell[] irow = cells[i];
			boolean[] brow = new boolean[irow.length];
			bMatrix[i] = brow;
			for (int j = 0; j < irow.length; j++) {
				brow[j] = irow[j].isPassable();
			}
		}

		return bMatrix;
	}
	
	public List<RuntimeIndoorMapListener> getListeners(){
		return listeners;
	}
	
	public void changeMode(AnimatedSprite sprite, int mode){
		for (RuntimeIndoorMapListener l : listeners){
			l.modeChanged(sprite, mode);
		}
	}
	
	@SuppressWarnings("unused")
	private void findPathFromStartPositionAndCachePath() {

		List<Cell> path = PathSearcher.findPath(getPassableMatrix(), startingPosition, targetPosition);

		if (path == null) {
			throw new IllegalStateException("Bad Map there is no path");
		}

		cachedPathFromStartingPosition = path;

		keyCellsMap.clear();
	}

	@SuppressWarnings("unused")
	private List<Cell> findPathFromParticularPosition(int rowNo, int colNo) {
		return findPathFromParticularPosition(getCellAt(rowNo, colNo));
	}

	List<Cell> findPathFromParticularPosition(Cell startPosition) {
		List<Cell> findPath = PathSearcher.findPath(getPassableMatrix(),
				startPosition, targetPosition);

		return findPath;
	}
	
	@SuppressWarnings("unused")
	private List<Cell> findPath(int rowNo1, int colNo1, int rowNo2, int colNo2) {
		return findPath(getCellAt(rowNo1, colNo1), getCellAt(rowNo2, colNo2));
	}

	private List<Cell> findPath(Cell startPosition, Cell targetPosition) {
		List<Cell> findPath = PathSearcher.findPath(getPassableMatrix(),
				startPosition, targetPosition);

		return findPath;
	}

	public boolean locateUser(int colNo, int rowNo) {

		Cell currentCell = getCellAt(rowNo, colNo); // The map has a revert x,y, so revert the incoming x,y here to get the right cell
		user.setCurrentCell(currentCell);

		for (RuntimeIndoorMapListener l : listeners) {
			l.appear(user);
		}

		return true;
	}
	
	public boolean locateTarget(int colNo, int rowNo) {
		if (target != null) {
			Cell currentCell = getCellAt(rowNo, colNo); // The map has a revert x,y, so revert the incoming x,y here to get the right cell
			target.setCurrentCell(currentCell);
	
			for (RuntimeIndoorMapListener l : listeners) {
				l.appear(target);
			}
	
			return true;
		} 
		
		return false;
	}
	
	public boolean locateTrack(int colNo, int rowNo, int idx) {
		if (tracks == null) {
			return false;
		}
		
		if (idx >= tracks.size()) {
			return false;
		}
		
		RuntimeUser track = tracks.get(idx);
		
		if (track != null) {
			Cell currentCell = getCellAt(rowNo, colNo); // The map has a revert x,y, so revert the incoming x,y here to get the right cell
			track.setCurrentCell(currentCell);
			
			// Always display the track even it is the same with the balanced Location
			/*
			if (inSameCellWithUser(currentCell)) {
				return false;
			} */
	
			for (RuntimeIndoorMapListener l : listeners) {
				l.appear(track);
			}
	
			return true;
		} 
		
		return false;
	}

	@SuppressWarnings("unused")
	private boolean inSameCellWithUser(Cell currentCell) {
		
		if (user == null) {
			return false;
		}
		
		Cell cell2 = user.getCurrentCell();
		if (cell2 == null) {
			return false;
		}

		if (currentCell == null) {
			return false;
		}
		
		if ((currentCell.getColNo() == cell2.getColNo()) && (currentCell.getRowNo() == cell2.getRowNo())) {
			return true;
		}
		
		return false;
	}

	public RuntimeUser getUser() {
		return user;
	}

	public void setUser(RuntimeUser user) {
		this.user = user;
	}
	
	public RuntimeUser getTarget() {
		return target;
	}

	public void setTarget(RuntimeUser target) {
		this.target = target;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getMapVersion() {
		return mapVersion;
	}

	public void setMapVersion(String mapVersion) {
		this.mapVersion = mapVersion;
	}

	public String getMapPictureName() {
		return mapPictureName;
	}

	public void setMapPictureName(String mapPictureName) {
		this.mapPictureName = mapPictureName;
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
		
		String text = getMapName() + "\n";
		
		for (String information : informations){
			text += information + "\n";
		}
		
		return text;
	}
	
	public String informationsToString(){
		if ((informations==null) || (informations.isEmpty())){
			return null;
		}
		
		String text = getMapName() + "\n";
		
		for (String information : informations){
			text += information + "\n";
		}
		
		return text;
	}
	
	public String informationsToStringForLocations(){
		if ((cells==null) || (cells.length==0)){
			return Util.getResources().getText(R.string.none).toString();
		}
		
		String text = "";
		
		for (Cell[] cell_i : cells){
			for (Cell cell_i_j : cell_i){
				String info = cell_i_j.informationsToString();
				if (info != null) {
					text += info + "\n";	
				}
			}
		}
		
		if (text.length() == 0) {
			return Util.getResources().getText(R.string.none).toString();
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

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getCellPixel() {
		return cellPixel;
	}

	public void setCellPixel(int cellPixel) {
		this.cellPixel = cellPixel;
	}

	public RuntimeUser getTrack(int i) {
		if (tracks != null){
			return tracks.get(i);
		}
		
		return null;
	}

	public void addTrack(RuntimeUser track) {
		if (tracks == null){
			tracks = new ArrayList<RuntimeUser>();
		}
		
		tracks.add(track);
	}
	
	public int getTracksNum() {
		if (tracks == null) {
			return -1;
		}
		
		return tracks.size();
	}

}
