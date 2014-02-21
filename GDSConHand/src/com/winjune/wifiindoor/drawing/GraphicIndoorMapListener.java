package com.winjune.wifiindoor.drawing;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import android.app.Activity;
import android.widget.Toast;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.drawing.runtime.PreciseCellLocation;
import com.winjune.wifiindoor.runtime.Cell;
import com.winjune.wifiindoor.runtime.RuntimeIndoorMap;
import com.winjune.wifiindoor.runtime.RuntimeIndoorMapListener;
import com.winjune.wifiindoor.runtime.RuntimeUser;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

public class GraphicIndoorMapListener implements RuntimeIndoorMapListener {

	private Scene mainScene;
	private Text mMapText;
	private String mapName;
	private Activity activity;

	public GraphicIndoorMapListener(Activity activity, Scene mainScene, Text mMapText) {
		this.activity = activity;
		this.mainScene = mainScene;
		this.mMapText = mMapText;
	}

	@Override
	public void initial(RuntimeIndoorMap runtimeIndoorMap) {
		// No need to draw the background Sprite
		/*
		Cell[][] cells = runtimeIndoorMap.getCells();
		for (Cell[] cellRow : cells) {
			for (Cell cell : cellRow) {

				AnimatedSprite backgroundSprite = cell.getBackgroundSprite();

				if (backgroundSprite != null) {
					mainScene.getChild(Constants.LAYER_BACKGROUND).attachChild(
							backgroundSprite);
				}

				setPosition(cell, backgroundSprite);
			}
		}*/
		
		this.mapName = runtimeIndoorMap.getMapName();

	}

	@Override
	public void locate(RuntimeIndoorMap runtimeIndoorMap, int colNo, int rowNo, int userType, int idx) {
		// to put a tag on a specific cell
		switch (userType) {
			case Constants.LOCATION_USER:
				runtimeIndoorMap.locateUser(colNo, rowNo); // x,y
				break;
			case Constants.TARGET_USER:
				runtimeIndoorMap.locateTarget(colNo, rowNo); // x,y
				break;
			case Constants.TRACK_USER:
				runtimeIndoorMap.locateTrack(colNo, rowNo, idx); // x,y
				break;
			default:
		} 
	}

	@Override
	public void modeChanged(AnimatedSprite sprite, int mode) {

		StringBuilder builder = new StringBuilder();
		String modeStr = "";
		
		
		switch (mode)  {
		case IndoorMapData.MAP_MODE_VIEW:
			modeStr = Util.getResources().getString(R.string.view_mode);
			sprite.setCurrentTileIndex(0);
			break;
		case IndoorMapData.MAP_MODE_EDIT:
			modeStr = Util.getResources().getString(R.string.edit_mode);
			sprite.setCurrentTileIndex(1);
			break;
		case IndoorMapData.MAP_MODE_EDIT_TAG:
			modeStr = Util.getResources().getString(R.string.nfc_edit_mode);
			sprite.setCurrentTileIndex(2);
			break;
		case IndoorMapData.MAP_MODE_DELETE_FINGERPRINT:
			modeStr = Util.getResources().getString(R.string.delete_fingerprint_mode);
			sprite.setCurrentTileIndex(3);
			break;
		case IndoorMapData.MAP_MODE_TEST_LOCATE:
			modeStr = Util.getResources().getString(R.string.test_locate_mode);
			sprite.setCurrentTileIndex(4);
			break;
		case IndoorMapData.MAP_MODE_TEST_COLLECT:
			modeStr = Util.getResources().getString(R.string.test_collect_mode);
			sprite.setCurrentTileIndex(5);
			break;
		default:
		}		

		builder.append(Util.getResources().getString(R.string.map))
				.append(mapName).append(" - ").append(modeStr);

		mMapText.setText(builder.toString());
		
		Util.showToast(activity, Util.getResources().getString(R.string.mode_change_to) + ": " + modeStr, Toast.LENGTH_SHORT);
	}

	private int offsetX;
	private int offsetY;

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	
	private void setPosition(RuntimeUser user) {
		setPosition(user.getPreciseCellLocation(), user.getSprite());
	}

	public void setPosition(Cell cell, Sprite sprite) {
		sprite.setPosition(offsetX + cell.getColNo()
				* Util.getCurrentCellPixel(), offsetY + cell.getRowNo()
				* Util.getCurrentCellPixel());
	}

	private void setPosition(PreciseCellLocation cell, Sprite sprite) {
		sprite.setPosition(offsetX + cell.getColPosition()
				* Util.getCurrentCellPixel(), offsetY + cell.getRowPosition()
				* Util.getCurrentCellPixel());
	}

	public Cell locateCell(float x, float y) {
		int colNo = (int) ((x - offsetX) / Util.getCurrentCellPixel());
		int rowNo = (int) ((y - offsetY) / Util.getCurrentCellPixel());

		return new Cell(rowNo, colNo);
	}

	@Override
	public void edit(Cell cell) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appear(RuntimeUser user) {
		setPosition(user);
		if (user.getSprite().hasParent()){
			
		} else {
			mainScene.getChildByIndex(Constants.LAYER_USER).attachChild(user.getSprite());
		}
	}
}

