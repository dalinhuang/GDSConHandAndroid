package com.ericsson.cgc.aurora.wifiindoor.map;

import java.util.ArrayList;

import org.andengine.entity.sprite.AnimatedSprite;

import android.util.Log;

import com.ericsson.cgc.aurora.wifiindoor.MapViewerActivity;
import com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.model.Library;
import com.ericsson.cgc.aurora.wifiindoor.runtime.Cell;
import com.ericsson.cgc.aurora.wifiindoor.runtime.RuntimeIndoorMap;
import com.ericsson.cgc.aurora.wifiindoor.runtime.RuntimeUser;
import com.ericsson.cgc.aurora.wifiindoor.util.Constants;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;
import com.ericsson.cgc.aurora.wifiindoor.util.VisualParameters;


@SuppressWarnings("unused")
public class IndoorMapLoader {
	private MapViewerActivity activity;
	private RuntimeIndoorMap runtimeIndoorMap;

	private IndoorMap designMap;
	
	public IndoorMap getDesignMap(){
		return designMap;
	}

	// Load from the Map File according the category and file path.
	public IndoorMapLoader(MapViewerActivity activity, IndoorMap designMap) {
		this.activity = activity;
		this.designMap = designMap;
		loadIndoorMap();
	}

	private void loadIndoorMap() {		
		// TODO: load from design map or save file
		InitialMap initialMap = designMap.getInitialMap();

		int rows = initialMap.getRows();
		int cols = initialMap.getColumns();
		Cell[][] cellMatrix = new Cell[rows][];

		for (int i = 0; i < cellMatrix.length; i++) {
			Cell[] brow = new Cell[cols];
			cellMatrix[i] = brow;
			for (int j = 0; j < cols; j++) {
				brow[j] = new Cell(i, j);
				brow[j].setPassable(true); // TODO hardcode
			}
		}

		runtimeIndoorMap = new RuntimeIndoorMap(cellMatrix, designMap.getName(), designMap.getId(), designMap.getVersion(), 
				designMap.getPictureName(), designMap.getInitialMap().getCellPixel(), designMap.getVersionCode());
	}
	
	public RuntimeIndoorMap getRuntimeIndoorMap() {
		return runtimeIndoorMap;
	}
	

	public void initialMap() {
		createOriginalUnitsAndCell();
	}
	
	// Draw the CELL according to the CELL status
	private void createOriginalUnitsAndCell() {
		// No need to draw the background Sprite				
		/*ArrayList<MapField> mapFields = designMap.getInitialMap()
				.getMapFields();
		int fieldsNumber = mapFields.size();

		for (int i = 0; i < fieldsNumber; i++) {

			MapField mapField = mapFields.get(i);

			drawCellBackground(mapField);
		}*/
		
		drawUser();
	}
	
	/*
	private void drawCellBackground(MapField mapField) {
		int status = mapField.getStatus();

		int rowNo = mapField.getX();
		int colNo = mapField.getY();
		
		if (designMap.getInitialMap() == null) {
			return;
		}
		
		AnimatedSprite cellSprite = Library.genCell(activity, status, designMap.getInitialMap().getCellPixel());

		if (cellSprite == null){
			return;
		}
		
		runtimeIndoorMap.getCellAt(rowNo, colNo).setBackgroundSprite(cellSprite);
	}*/
	
	private void drawUser() {

		if (designMap.getInitialMap() == null) {
			return;
		}
		
		int cellPixel = designMap.getInitialMap().getCellPixel();
		
		AnimatedSprite locationSprite = Library.genUser(activity, Constants.LOCATION_USER, cellPixel);

		if (locationSprite == null){
			return;
		}
		
		locationSprite.setAlpha(VisualParameters.USER_PIN_ALPHA);
		
		RuntimeUser location = new RuntimeUser();
		
		location.setSprite(locationSprite);
		location.setStartCell(runtimeIndoorMap.getCellAt(0, 0)); // Put it in the 1st cell
		
		runtimeIndoorMap.setUser(location);
		
		AnimatedSprite targetSprite = Library.genUser(activity, Constants.TARGET_USER, cellPixel);
		
		if (targetSprite == null){
			return;
		}
		
		targetSprite.setAlpha(VisualParameters.USER_PIN_ALPHA);
		
		RuntimeUser target = new RuntimeUser();
		
		target.setSprite(targetSprite);
		target.setStartCell(runtimeIndoorMap.getCellAt(0, 0)); // Put it in the 1st cell
		
		runtimeIndoorMap.setTarget(target);
		
		for (int i=0; i<Constants.MAX_TRACK_USERS; i++) {
			AnimatedSprite trackSprite = Library.genUser(activity, Constants.TRACK_USER, cellPixel);
			
			if (trackSprite == null){
				return;
			}
			
			trackSprite.setAlpha(VisualParameters.USER_PIN_ALPHA);
			
			RuntimeUser track = new RuntimeUser();
			
			track.setSprite(trackSprite);
			track.setStartCell(runtimeIndoorMap.getCellAt(0, 0)); // Put it in the 1st cell
			
			runtimeIndoorMap.addTrack(track);
		}
	}

}
