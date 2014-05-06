package com.winjune.wifiindoor.activity.mapviewer;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.LocationSprite;
import com.winjune.wifiindoor.drawing.graphic.model.LocationSprite.OnClickListener;
import com.winjune.wifiindoor.drawing.graphic.model.LocationSprite.State;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.poi.SearchContext;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.Util;

public class SearchBar {	
	
	public static void showSearchResultsOnMap(MapViewerActivity mapViewer, SearchContext searchContext) {
		
		// Clear old search result markers 
		clearSearchResultSprite(mapViewer);
		
		// Show New Map Info
				
		for (int i=0; i < searchContext.poiResults.size(); i++) { 
			PlaceOfInterest poi = searchContext.poiResults.get(i);			
			attachSearchResultSprite(mapViewer, poi, i);
		}
		PlaceOfInterest poi = searchContext.poiResults.get(searchContext.currentFocusIdx);
		poi.showContextMenu(mapViewer.getCurrentFocus());
		
	}
	
	public static void clearSearchResultSprite(MapViewerActivity mapViewer) {
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
	}
	
	public static void attachSearchResultSprite(final MapViewerActivity mapViewer, PlaceOfInterest poi, final int index) {		
		
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mapViewer.getTextureManager(), 512, 512);
		
		ITextureRegion searchResultFocusedMarkerITR = null;
		ITextureRegion searchResultMarkerITR = null;
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		try {		
			searchResultFocusedMarkerITR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, mapViewer, "icon_focus_marka.png");
			searchResultMarkerITR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, mapViewer, "icon_marka.png");
						
			mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			mBitmapTextureAtlas.load();			
			
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
			return;
		}		
				
		LocationSprite searchResultSprite = new LocationSprite(poi.getX() * Util.getRuntimeIndoorMap().getCellPixel(), 
														poi.getY() * Util.getRuntimeIndoorMap().getCellPixel(), 														
														searchResultFocusedMarkerITR,
														searchResultMarkerITR,
														mapViewer.getVertexBufferObjectManager());		
				
		searchResultSprite.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(LocationSprite pSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				// TODO Auto-generated method stub
				pSprite.changeState(State.FOCUSED);				
			}
			
		});
		
		//searchResultSprite.setScale(mapViewer.zoomControl.getZoomFactor());		
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_SEARCH).attachChild(searchResultSprite);
		mapViewer.mainScene.registerTouchArea(searchResultSprite);
		
		// Store so we can clear them in future if needed
		if (mapViewer.searchPlaces == null) {
			mapViewer.searchPlaces = new ArrayList<Sprite>();
		}

		mapViewer.searchPlaces.add(searchResultSprite);				
	}	


}
