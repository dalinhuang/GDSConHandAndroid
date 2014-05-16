package com.winjune.wifiindoor.map;

import java.util.ArrayList;

import org.andengine.entity.sprite.AnimatedSprite;

import android.util.Log;

import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.lib.map.MapDataR;
import com.winjune.wifiindoor.runtime.Cell;
import com.winjune.wifiindoor.runtime.RuntimeIndoorMap;
import com.winjune.wifiindoor.runtime.RuntimeUser;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;


@SuppressWarnings("unused")
public class IndoorMapLoader {
	private MapViewerActivity activity;
	private RuntimeIndoorMap runtimeIndoorMap;

	private MapDataR designMap;
	
	public MapDataR getDesignMap(){
		return designMap;
	}

	// Load from the Map File according the category and file path.
	public IndoorMapLoader(MapViewerActivity activity, MapDataR designMap) {
		this.activity = activity;
		this.designMap = designMap;
		loadIndoorMap();
	}

   private void cacNumOfRowsAndCols(String mapUrl, int cellPixel, int[] rowsAndcols) {
        if ((mapUrl != null) && (!mapUrl.isEmpty())) {
        	String pieces[] = mapUrl.trim().split(";");
               
             //Get last piece
            if (pieces.length > 0)   {
            	String lastPiece = pieces[pieces.length-1];
                String attrs[] = lastPiece.split(",");
                if (attrs.length == IndoorMapData.ATTR_NUMBER_PER_MAP_PIECE) {
                	int left = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_LEFT].trim());
                    int top = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_TOP].trim());
                    int width = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_WIDTH].trim());
                    int height = Integer.parseInt(attrs[IndoorMapData.MAP_PIECE_ATTR_HEIGHT].trim());                            
                    rowsAndcols[0] = (top+height)/cellPixel;
                    rowsAndcols[1] = (left+width)/cellPixel;
                }
            }
        }
   }
	
	
   private void loadIndoorMap() {		
		// TODO: load from design map or save file

		int rowNum = 0;
		int colNum = 0;
		int[] temp = {rowNum,colNum}; 
		cacNumOfRowsAndCols(designMap.getNormalMapUrl(), designMap.getCellPixel(), temp);
		rowNum = temp[0];
		colNum = temp[1];
		
		Cell[][] cellMatrix = new Cell[rowNum][];

		for (int i = 0; i < cellMatrix.length; i++) {
			Cell[] brow = new Cell[colNum];
			cellMatrix[i] = brow;
			for (int j = 0; j < colNum; j++) {
				brow[j] = new Cell(i, j);
				brow[j].setPassable(true); // TODO hardcode
			}
		}

		runtimeIndoorMap = new RuntimeIndoorMap(cellMatrix, designMap.getLabel(), designMap.getId(), 
				designMap.getNormalMapUrl(), designMap.getCellPixel());
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

		int cellPixel = designMap.getCellPixel();
		
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
