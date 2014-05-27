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
	public static String NormalMark[] = {"icon_marka.png",
										 "icon_markb.png",
										 "icon_markc.png",
										 "icon_markd.png",
										 "icon_marke.png",
										 "icon_markf.png",
										 "icon_markg.png",
										 "icon_markh.png",
										 "icon_marki.png",
										 "icon_markj.png"};
	
	public static String FocusMark[] = {"icon_focus_marka.png",
										"icon_focus_markb.png",
										"icon_focus_markc.png",
										"icon_focus_markd.png",
										"icon_focus_marke.png",
										"icon_focus_markf.png",
										"icon_focus_markg.png",
										"icon_focus_markh.png",
										"icon_focus_marki.png",
										"icon_focus_markj.png"};
	
	public static void showSearchResultsOnMap(MapViewerActivity mapViewer, SearchContext searchContext) {
		
		//
		NaviBar.clearNaviInfo(mapViewer);
		
		// Clear old search result markers 
		clearLocationPlaces(mapViewer);
		
		// Show New Map Info
				
		for (int i=0; i < searchContext.poiResults.size(); i++) { 
			PlaceOfInterest poi = searchContext.poiResults.get(i);	
			
			if (poi.mapId !=  Util.getRuntimeIndoorMap().getMapId())
				continue;
			
			LocationSprite mSprite = attachSearchResultSprite(mapViewer, poi, i);
			
			if (i == searchContext.currentFocusIdx) {
				if (mSprite != null) {
					mSprite.changeState(State.FOCUSED);
					mapViewer.focusPlace = mSprite;
				}					
			}
		}
		PlaceOfInterest poi = searchContext.poiResults.get(searchContext.currentFocusIdx);
		poi.showContextMenu(mapViewer.getCurrentFocus());
		
	}
	
	public static void clearLocationPlaces(MapViewerActivity mapViewer) {
		if (mapViewer.locationPlaces != null) {
			for (Sprite place:mapViewer.locationPlaces) {
				if (place != null) {
					mapViewer.mainScene.getChildByIndex(Constants.LAYER_SEARCH).detachChild(place);
					mapViewer.mainScene.unregisterTouchArea(place);
				}
			}
			mapViewer.locationPlaces.clear();
		}		
		
		mapViewer.focusPlace = null;		
	}
	
	public static LocationSprite attachSearchResultSprite(final MapViewerActivity mapViewer, PlaceOfInterest poi, final int index) {

		if (index > NormalMark.length)
			return null;
		
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mapViewer.getTextureManager(), 512, 512);
		
		ITextureRegion searchResultFocusedMarkerITR = null;
		ITextureRegion searchResultMarkerITR = null;
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		try {		
			searchResultFocusedMarkerITR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, mapViewer, FocusMark[index]);
			searchResultMarkerITR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, mapViewer, NormalMark[index]);
						
			mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			mBitmapTextureAtlas.load();			
			
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
			return null;
		}
		
		float adjustedX = poi.getMapX() - searchResultMarkerITR.getWidth()/2;
		float adjustedY = poi.getMapY() - searchResultMarkerITR.getHeight();
		
				
		LocationSprite mSprite = new LocationSprite(adjustedX, 
													adjustedY, 														
													searchResultFocusedMarkerITR,
													searchResultMarkerITR,
													mapViewer.getVertexBufferObjectManager());		
				
		mSprite.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(LocationSprite pSprite,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {				
				// TODO Auto-generated method stub
				
				mapViewer.focusPlace.changeState(State.NORMAL);
				
				pSprite.changeState(State.FOCUSED);						
				mapViewer.focusPlace = pSprite;
			}
			
		});			
		
		//searchResultSprite.setScale(mapViewer.zoomControl.getZoomFactor());		
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_SEARCH).attachChild(mSprite);
		mapViewer.mainScene.registerTouchArea(mSprite);
		
		// Store so we can clear them in future if needed
		if (mapViewer.locationPlaces == null) {
			mapViewer.locationPlaces = new ArrayList<LocationSprite>();
		}

		mapViewer.locationPlaces.add(mSprite);	
		
		return mSprite;
	}	


}
