package com.winjune.wifiindoor.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andengine.entity.sprite.AnimatedSprite;

import android.app.Activity;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.drawing.graphic.model.MapPieceSprite;
import com.winjune.wifiindoor.lib.map.MapDataR;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;

enum MapZoomLevel {
	Normal,
	Large
}

public class RuntimeMap {
	
	private static final float DEFAULT_ZOOM_FACTOR = 0.80f;
	private static final float MAX_ZOOM_FACTOR = 5.00f;
	private static final float MIN_ZOOM_FACTOR = 0.50f;
	private static final float NORMAL_2_LARGE_SWITCH_FACTOR = 2.00f;
	private static final float LARGE_2_NORMAL_SWITCH_FACTOR = 1.00f;

	
	private MapZoomLevel zoomLevel = MapZoomLevel.Normal;
	
	private int mapId;
	private String mapLabel;
	private String normalMapUrl;
	private String largeMapUrl;
	private int maxLatitude;
	private int maxLongitude;

	private int rowNum;
	private int colNum;
	private RuntimeUser user;
	private RuntimeUser target;
	private ArrayList<RuntimeUser> tracks;
	
	private Cell[][] cells;
	
	private List<RuntimeMapListener> listeners = new ArrayList<RuntimeMapListener>();
	
	private ArrayList<String> informations;
	private boolean infoPushed;
	private long infoPushTime;
	
	private HashMap<MapResource, MapPieceSprite> mapResources;

	
	private int normalMapWidth;
	private int normalMapHeight;
	private int normalCellPixel;
	private int largeMapWidth;
	private int largeMapHeight;
	private int largeCellPixel;	
	private float normalDefaultZoomFactor = 0.00f;
	private float normalMinZoomFactor = 0.00f;
	private float normalMaxZoomFactor = 0.00f;
	private float normal2LargeScale = 0.00f;
	private float large2NormalScale = 0.00f;
	
	public void load(MapDataR mData){

		this.normalMapUrl = mData.getNormalMapUrl();
		this.largeMapUrl = mData.getLargeMapUrl();
		this.mapLabel = mData.getLabel();
		this.mapId = mData.getId();
		this.maxLatitude = mData.getLatitude();
		this.maxLongitude = mData.getLongitude();
		this.normalCellPixel = mData.getCellPixel();
		
		calcNormalMapSize();
		calcLargeMapSize();
		calcZoomFactors();
		calcMapScale();
		
		rowNum = normalMapHeight/normalCellPixel;
        colNum = normalMapWidth/normalCellPixel;
        		
		setInfoPushed(false);
		setInformations(null);
		setInfoPushTime(0);       
		
		cells = new Cell[rowNum][];
		for (int i = 0; i < cells.length; i++) {
			Cell[] brow = new Cell[colNum];
			cells[i] = brow;
			for (int j = 0; j < colNum; j++) {
				brow[j] = new Cell(i, j);
				brow[j].setPassable(true);
			}
		}
		
		mapResources = initMapResources(normalMapUrl);
	}
	
	public void initMap(MapViewerActivity mapViewer){
		drawUser(mapViewer);
	}
	
	private void calcNormalMapSize() {
		if ((normalMapUrl == null) || normalMapUrl.isEmpty())
			return; 
				
	    String pieces[] = normalMapUrl.trim().split(";");
	               
	    //Get last piece
	    if (pieces.length > 0)   {
	    	String lastPiece = pieces[pieces.length-1];
	        String attrs[] = lastPiece.split(",");
	        if (attrs.length == IndoorMapData.ATTR_NUMBER_PER_MAP_PIECE) {
	        	int left = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_LEFT].trim());
	            int top = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_TOP].trim());
	            int width = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_WIDTH].trim());
	            int height = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_HEIGHT].trim()); 
	            normalMapWidth = left + width;
	            normalMapHeight = top + height;

	         }
	     }		
	}
	
	private void calcLargeMapSize() {
		if ((largeMapUrl == null) || largeMapUrl.isEmpty())
			return; 
				
	    String pieces[] = largeMapUrl.trim().split(";");
	               
	    //Get last piece
	    if (pieces.length > 0)   {
	    	String lastPiece = pieces[pieces.length-1];
	        String attrs[] = lastPiece.split(",");
	        if (attrs.length == IndoorMapData.ATTR_NUMBER_PER_MAP_PIECE) {
	        	int left = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_LEFT].trim());
	            int top = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_TOP].trim());
	            int width = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_WIDTH].trim());
	            int height = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_HEIGHT].trim()); 
	            largeMapWidth = left + width;
	            largeMapHeight = top + height;

	         }
	     }		
	}	
	
	
	private HashMap<MapResource, MapPieceSprite> initMapResources(String mapUrl) {
		if ((mapUrl == null) || mapUrl.isEmpty())
			return null;
		
		HashMap<MapResource, MapPieceSprite> resources = new HashMap<MapResource, MapPieceSprite>();
		String pieces[] = mapUrl.trim().split(";");
		
		for (int i=0; i<pieces.length; i++) {
			String piece = pieces[i].trim();
		
			if (piece.isEmpty())
				continue;		
				
			String attrs[] = piece.split(",");
			if (attrs.length == IndoorMapData.ATTR_NUMBER_PER_MAP_PIECE) {
				try {
					int left = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_LEFT].trim());
					int top = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_TOP].trim());
					int width = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_WIDTH].trim());
					int height = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_HEIGHT].trim());
					String name = attrs[IndoorMapData.MAP_PIECE_ATTR_NAME].trim();
							
					MapResource resource = new MapResource();
					resource.setLeft(left);
					resource.setTop(top);
					resource.setWidth(width);
					resource.setHeight(height);
					resource.setName(name);
					resources.put(resource, null);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}	
		
		return resources;
	}
	
	private void calcMapScale(){
		if (largeMapUrl == null)
			return;
		
		large2NormalScale = largeMapWidth * 100 / normalMapWidth;
		large2NormalScale = (float)(Math.round(large2NormalScale)/100.0);

		normal2LargeScale = normalMapWidth * 100/ largeMapWidth;
		normal2LargeScale = (float)(Math.round(normal2LargeScale)/100.0);
		
		largeCellPixel = (int)(normalCellPixel * largeMapWidth * 100 / normalMapWidth); 
		largeCellPixel = (int)(Math.round(largeCellPixel)/100.0);
	}
	
	public boolean zoomInMap(){
		
		if (zoomLevel == MapZoomLevel.Large)
			return false;
		
		if ((largeMapUrl == null) || (largeMapUrl.isEmpty()))
			return false;
						
		mapResources = initMapResources(largeMapUrl);
		
		zoomLevel = MapZoomLevel.Large;
		
		return true;
	}
	
	public boolean zoomOutMap(){
		
		if (zoomLevel == MapZoomLevel.Normal)
			return false;
	
		mapResources = initMapResources(normalMapUrl);
		
		zoomLevel = MapZoomLevel.Normal;	
		
		return true;		
	}
	
	private void calcZoomFactors(){
		
		normalDefaultZoomFactor = DEFAULT_ZOOM_FACTOR;
		
		if (Util.getCameraWidth() < normalMapWidth) {
			normalDefaultZoomFactor = Util.getCameraWidth() * 100 /normalMapWidth;
			normalDefaultZoomFactor = (float)(Math.round(normalDefaultZoomFactor)/100.0);
		}
			
		
		if (Util.getCameraHeight() < normalMapHeight){
			float tempF = 0.00f;
			tempF = Util.getCameraHeight() * 100 /normalMapHeight;
			tempF = (float)(Math.round(tempF)/100.0);
			
			if (tempF < normalDefaultZoomFactor)
				normalDefaultZoomFactor = tempF;
		}
		
		if (normalDefaultZoomFactor < MIN_ZOOM_FACTOR)
			normalMinZoomFactor = normalDefaultZoomFactor;
		else
			normalMinZoomFactor = MIN_ZOOM_FACTOR;
		
		normalMaxZoomFactor = MAX_ZOOM_FACTOR;
	}
	
	public float getMinZoomFactor(){
		if (zoomLevel == MapZoomLevel.Normal)
			return normalMinZoomFactor;
		else 
			return LARGE_2_NORMAL_SWITCH_FACTOR;
	}
	
	public float getMaxZoomFactor(){
		if ((zoomLevel == MapZoomLevel.Normal) &&
			(largeMapUrl != null))
			return getNormal2LargeThreshold();
		
		return normalMaxZoomFactor;
	}	
		
	public float getNormal2LargeThreshold(){
		float tempF = large2NormalScale * 100 * NORMAL_2_LARGE_SWITCH_FACTOR;
		tempF = (float)(Math.round(tempF)/100.0);

		return tempF;
	}
	
	public float getLarge2NormalThreshold(){
		return LARGE_2_NORMAL_SWITCH_FACTOR;
	}	
	
	public float getNormal2LargeScale(){
		return normal2LargeScale;
	}
	
	public float getLarge2Normalcale(){
		return large2NormalScale;
	}	
	
	public float getDefaultZoomFactor(){		
		return normalDefaultZoomFactor;
	}
	
	public float getNormal2LargeZoomFactor(){
		return NORMAL_2_LARGE_SWITCH_FACTOR;
	}
	
	public float getLarge2NormalZoomFactor(){
		float tempF = large2NormalScale * 100 * LARGE_2_NORMAL_SWITCH_FACTOR;
		tempF = (float)(Math.round(tempF)/100.0);		
		
		return tempF;
	}
	
	public Cell[][] getCells() {
		return cells;
	}


	public void initial(){
		for (RuntimeMapListener l : listeners) {
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
	
	public void addListener(RuntimeMapListener listener) {
		this.listeners.add(listener);
	}

	public boolean removeListener(RuntimeMapListener listener) {
		return this.listeners.remove(listener);
	}
	
	public int getRowNum() {
		return rowNum;
	}

	public int getColNum() {
		return colNum;
	}
	
	public int getMapWidth(){
		if (zoomLevel == MapZoomLevel.Normal)
			return normalMapWidth;
		else
			return largeMapWidth;
	}
	
	public int getMapHeight(){
		if (zoomLevel == MapZoomLevel.Normal)
			return normalMapHeight;
		else
			return largeMapHeight;	
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
	
	public List<RuntimeMapListener> getListeners(){
		return listeners;
	}
	
	public void changeMode(AnimatedSprite sprite, int mode){
		for (RuntimeMapListener l : listeners){
			l.modeChanged(sprite, mode);
		}
	}
	
	public boolean locateUser(int colNo, int rowNo) {

		Cell currentCell = getCellAt(rowNo, colNo); // The map has a revert x,y, so revert the incoming x,y here to get the right cell
		user.setCurrentCell(currentCell);

		for (RuntimeMapListener l : listeners) {
			l.appear(user);
		}

		return true;
	}
	
	public boolean locateTarget(int colNo, int rowNo) {
		if (target != null) {
			Cell currentCell = getCellAt(rowNo, colNo); // The map has a revert x,y, so revert the incoming x,y here to get the right cell
			target.setCurrentCell(currentCell);
	
			for (RuntimeMapListener l : listeners) {
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
	
			for (RuntimeMapListener l : listeners) {
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

	public String getMapLabel() {
		return mapLabel;
	}

	public void setMapLabel(String mapName) {
		this.mapLabel = mapName;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getNormalMapUrl() {
		return normalMapUrl;
	}
	
	public int getMaxLatitude(){
		return maxLatitude;
	}
	
	public int getMaxLongitude(){
		return maxLongitude;
	}

	public void setNormalMapUrl(String mapUrl) {
		this.normalMapUrl = mapUrl;
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

		String text = null;
		
		for (String information : informations){
			//text += information + "\n";
			text = information + "\n";
		}
		
		return text;
	}
	
	public String informationsToString(){
		if ((informations==null) || (informations.isEmpty())){
			return null;
		}
		
		String text = getMapLabel() + "\n";
		
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
		return 0;
	}	

	public int getCellPixel() {	
		if (zoomLevel == MapZoomLevel.Normal)
			return normalCellPixel;
		else 
			return largeCellPixel;
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

	public HashMap<MapResource, MapPieceSprite> getResources() {
		
		return mapResources;
	}

	
	private void drawUser(MapViewerActivity activity) {
		
		AnimatedSprite locationSprite = Library.genUser(activity, Constants.LOCATION_USER, getCellPixel());

		if (locationSprite == null){
			return;
		}
		
		locationSprite.setAlpha(VisualParameters.USER_PIN_ALPHA);
		
		RuntimeUser location = new RuntimeUser();
		
		location.setSprite(locationSprite);
		location.setStartCell(getCellAt(0, 0)); // Put it in the 1st cell
		
		setUser(location);
		
		AnimatedSprite targetSprite = Library.genUser(activity, Constants.TARGET_USER, getCellPixel());
		
		if (targetSprite == null){
			return;
		}
		
		targetSprite.setAlpha(VisualParameters.USER_PIN_ALPHA);
		
		RuntimeUser target = new RuntimeUser();
		
		target.setSprite(targetSprite);
		target.setStartCell(getCellAt(0, 0)); // Put it in the 1st cell
		
		setTarget(target);
		
		for (int i=0; i<Constants.MAX_TRACK_USERS; i++) {
			AnimatedSprite trackSprite = Library.genUser(activity, Constants.TRACK_USER, getCellPixel());
			
			if (trackSprite == null){
				return;
			}
			
			trackSprite.setAlpha(VisualParameters.USER_PIN_ALPHA);
			
			RuntimeUser track = new RuntimeUser();
			
			track.setSprite(trackSprite);
			track.setStartCell(getCellAt(0, 0)); // Put it in the 1st cell
			
			addTrack(track);
		}
	}	
}
