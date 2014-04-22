package com.winjune.wifiindoor.mapviewer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.drawing.graphic.model.SpriteListener;
import com.winjune.wifiindoor.poi.SearchContext;
import com.winjune.wifiindoor.poi.SearchResult;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

public class SearchBar {

	public static final float FLAG_ICON_SCALE = 4;

	public static void showSearchPlacesInfo(MapViewerActivity mapViewer, SearchContext mapSearchInfo) {
		if (mapSearchInfo == null) {
			return;
		}
		
		// Clear old Interest Places info
		if (mapViewer.searchPlaces == null) {
			mapViewer.searchPlaces = new ArrayList<Sprite>();
		} else {
			for (Sprite place:mapViewer.searchPlaces) {
				if (place != null) {
					mapViewer.mainScene.getChildByIndex(Constants.LAYER_SEARCH).detachChild(place);
					mapViewer.mainScene.unregisterTouchArea(place);
				}
			}
			mapViewer.searchPlaces.clear();
		}
		
		// Show New Map Info
		ArrayList<SearchResult> places = mapSearchInfo.getSearchFields();
		
		if (places == null) {
			return;
		}
		
		for (SearchResult place : places) {
			if (place != null) {
				// X and Y = -1 mean the guide audio 
				if ((place.getX() != -1)&& (place.getY() != -1)) {  
					addSearchPlace(mapViewer, place);
				}
			}
		}
		
	}

	private static void addSearchPlace(MapViewerActivity mapViewer, SearchResult place) {
		// Create and attach Sprite
		Sprite placeSprite = createSearchPlaceSprite(mapViewer, place);
		
		// Store so we can clear them in future if needed
		if (mapViewer.searchPlaces == null) {
			mapViewer.searchPlaces = new ArrayList<Sprite>();
		}

		mapViewer.searchPlaces.add(placeSprite);
	}
	
	private static Sprite createSearchPlaceSprite(final MapViewerActivity mapViewer, final SearchResult place) {		
		Sprite placeSprite = Library.SEARCH_PLACE.load(mapViewer, new SpriteListener() {

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				if (mapViewer.mMode != IndoorMapData.MAP_MODE_VIEW) {
					return false;
				}

				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {}

				return true;
			}
		}, Util.getRuntimeIndoorMap().getCellPixel(), Util.getRuntimeIndoorMap().getCellPixel());
		
		float pX = place.getX() * Util.getRuntimeIndoorMap().getCellPixel();
		float pY = place.getY() * Util.getRuntimeIndoorMap().getCellPixel();
		placeSprite.setPosition(pX, pY);
		placeSprite.setScale(FLAG_ICON_SCALE);
		
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_SEARCH).attachChild(placeSprite);
		mapViewer.mainScene.registerTouchArea(placeSprite);
		
		return placeSprite;
	}


}
