package com.winjune.wifiindoor.activity.mapviewer;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.content.Intent;
import android.os.Bundle;

import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.activity.poiviewer.POIAudioPlayerActivity;
import com.winjune.wifiindoor.activity.poiviewer.POINormalViewerActivity;
import com.winjune.wifiindoor.activity.poiviewer.POITtsPlayerActivity;
import com.winjune.wifiindoor.activity.poiviewer.POIWebViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.AnimatedUnit;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.drawing.graphic.model.SpriteListener;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

public class POIBar {
	
	public static void clearPoiInfo(MapViewerActivity mapViewer){
		// Clear old Map info
		if (mapViewer.poiLabels != null) {
			for (Text text:mapViewer.poiLabels) {
				if (text != null) {
					text.detachSelf();
				}
			}
			mapViewer.poiLabels.clear();
		}
				
		
		// Clear old POI icons
		if (mapViewer.poiIcons != null) {					
			for (Sprite place:mapViewer.poiIcons) {
				if (place != null) {
					place.detachSelf();					
					mapViewer.mainScene.unregisterTouchArea(place);
				}
			}
			mapViewer.poiIcons.clear();
		}		
	}
	
	public static void showPoiInfo(MapViewerActivity mapViewer){	
		
		float currentZoomFactor = mapViewer.mCamera.getZoomFactor();
		
		// Firstly, clear the old poi info
		clearPoiInfo(mapViewer);
		
		// Show New Map Info

		for (PlaceOfInterest poi: POIManager.POIList) {			
			if ((currentZoomFactor >= poi.minZoomFactor)
				 && (currentZoomFactor <= poi.maxZoomFactor)) {			
				Text mLabel = null;
				
				if (poi.mapId != Util.getRuntimeIndoorMap().getMapId())
					continue;
				
				if ((poi.placeX == 0) &&(poi.placeY == 0))
					continue;
				
				if ((poi.label != null ) && (!poi.label.trim().isEmpty())) {
					mLabel = showPoiLabel(mapViewer,poi);
					if (mLabel != null)
						mapViewer.poiLabels.add(mLabel);
				}
			
			    Sprite mIcon = showPoiIcon(mapViewer, poi, mLabel);
			    if (mIcon != null)
			    	mapViewer.poiIcons.add(mIcon);
			}
		}			
				
	}
	
	
	private static Sprite showPoiIcon(final MapViewerActivity mapViewer, final PlaceOfInterest poi, Text label) {		
        
		AnimatedUnit  whichSvg = Library.INTEREST_PLACE_FOR_IE;	
		
		String text = null;
    	String picture = poi.picUrl;
    	String audio = null;
    	String webUrl = null;
		
		text = poi.detailedDesc;
		audio = poi.audioUrl;
		webUrl = poi.getWebUrl();
	
		
		if ((webUrl !=null) && (!webUrl.trim().isEmpty())){
			whichSvg = Library.INTEREST_PLACE_FOR_IE;
		}
		else if ((audio !=null) && (!audio.trim().isEmpty())){
			whichSvg = Library.INTEREST_PLACE_FOR_SPEECH;
		}
		else if ((text !=null) && (!text.trim().isEmpty())){
			whichSvg = Library.INTEREST_PLACE_FOR_SPEECH;
		}	
		else if ((picture !=null) && (!picture.trim().isEmpty())){
			whichSvg = Library.INTEREST_PLACE_FOR_PIC;
		}		
		
		int mSize;
		float pX, pY;		
		
		// adjust the icon position according to label 
		if (label != null){
			mSize = (int)label.getHeight();
			pX = poi.getMapX() - 1.1f*label.getHeight() - label.getWidth()/2;
			pY = poi.getMapY() - label.getHeight()/2;			
		} else {
			mSize = Util.getRuntimeIndoorMap().getCellPixel();
			pX = poi.getMapX()- mSize/2;
			pY = poi.getMapY() -mSize/2;
		}

				
		Sprite placeSprite = whichSvg.load(mapViewer, new SpriteListener() {

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				if (mapViewer.mMode != IndoorMapData.MAP_MODE_VIEW) {
					return false;
				}

				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					
				   displayInterestPlaceContent(mapViewer, poi);
				}

				return true;
			}
		}, mSize, mSize);
		

		placeSprite.setPosition(pX, pY);
		
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_USER).attachChild(placeSprite);
		mapViewer.mainScene.registerTouchArea(placeSprite);
		
		return placeSprite;
	}
	
	
	private static void displayInterestPlaceContent(MapViewerActivity mapViewer, PlaceOfInterest poi){
		
		// refine video URL as the general web page
		// if the web page URL is defined, load web page from URL using web browswer
		if ((poi.webUrl != null) && !poi.webUrl.trim().isEmpty()) {
			Intent intent_show_interest_place = new Intent(mapViewer, POIWebViewerActivity.class); 
			Bundle mBundle = new Bundle(); 
			mBundle.putInt(Constants.BUNDLE_KEY_POI_ID, poi.id);
			intent_show_interest_place.putExtras(mBundle); 
			mapViewer.startActivity(intent_show_interest_place);
			
		} else {
			Intent intent_show_interest_place = new Intent(mapViewer, POIAudioPlayerActivity.class);
			
			if (needEnterTTSActivity(poi)){
				intent_show_interest_place = new Intent(mapViewer, POITtsPlayerActivity.class); 
			}
			else{
				intent_show_interest_place = new Intent(mapViewer, POINormalViewerActivity.class); 
			}
			
			Bundle mBundle = new Bundle(); 
			mBundle.putInt(Constants.BUNDLE_KEY_POI_ID,poi.id);
			intent_show_interest_place.putExtras(mBundle); 
			mapViewer.startActivity(intent_show_interest_place);
		}		
	}		
		
	private static boolean needEnterTTSActivity(PlaceOfInterest poi){
		boolean isTTSSupported = false;
		boolean enterTTSActivity = true;
		
		
		isTTSSupported = Util.isTTSSupported();
		
		String text = null;
    	String picture = null;
    	String audio = null;
		
		text = poi.detailedDesc;
		picture = poi.picUrl;
		audio = poi.audioUrl;
		
		if ((audio !=null) && (!audio.trim().isEmpty())){
			enterTTSActivity = false;
		}
		
		if ((picture !=null) && (!picture.trim().isEmpty())){
			enterTTSActivity = false;
		}
		
		if (!isTTSSupported) {
			enterTTSActivity = false;
		}
		
		if ((text == null) || (text.trim().isEmpty())) {
			enterTTSActivity = false;
		}
		
		return enterTTSActivity ;																
		
	}
	
	private static Text showPoiLabel(MapViewerActivity mapViewer, PlaceOfInterest poi) {
		float pX = poi.getMapX();// * Util.getRuntimeIndoorMap().getCellPixel();
		float pY = poi.getMapY();// * Util.getRuntimeIndoorMap().getCellPixel();
				
		Text text = new Text(pX,
				pY, 
				mapViewer.mFontLabel, 
				poi.label,
				100,
				mapViewer.getVertexBufferObjectManager());
		text.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		text.setAlpha(poi.getAlpha());
		text.setRotation(poi.getRotation()); // For future use if we need to rotate a angle
		text.setScale(poi.getScale()); // For future use if we need to display some label with a bigger/smaller scale
		text.setPosition(pX-text.getWidth()/2, pY-text.getHeight()/2);
		
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_USER).attachChild(text); 
		
		mapViewer.poiLabels.add(text);
		
		return text;
	}	
	

}
