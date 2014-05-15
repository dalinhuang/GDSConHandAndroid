package com.winjune.wifiindoor.activity.mapviewer;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.content.Intent;
import android.os.Bundle;

import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.activity.poiviewer.POIAudioPlayerActivity;
import com.winjune.wifiindoor.activity.poiviewer.POIBaseActivity;
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
import com.winjune.wifiindoor.util.VisualParameters;

public class POIBar {
	
	public static void showPOIIconOnMap(MapViewerActivity mapViewer) {
		// we don't display the interest place in planning mode
		if (VisualParameters.PLANNING_MODE_ENABLED)
			return;
		
		
		// Clear old Interest Places info
		if (mapViewer.interestPlaces == null) {
			mapViewer.interestPlaces = new ArrayList<Sprite>();
		} else {
			for (Sprite place:mapViewer.interestPlaces) {
				if (place != null) {
					mapViewer.mainScene.getChildByIndex(Constants.LAYER_USER).detachChild(place);
					mapViewer.mainScene.unregisterTouchArea(place);
				}
			}
			mapViewer.interestPlaces.clear();
		}
		
		
		for (PlaceOfInterest poi : POIManager.POIList) {			
			// X and Y = -1 mean the guide audio 
			if ((poi.getX() != -1)&& (poi.getY() != -1)) {  
					addInterestPlace(mapViewer, poi);
				}
			
		}
		
	}
	
	private static void addInterestPlace(MapViewerActivity mapViewer, PlaceOfInterest poi) {
		// Create and attach Sprite
		Sprite placeSprite = createPOISprite(mapViewer, poi);
		
		// Store so we can clear them in future if needed
		if (mapViewer.interestPlaces == null) {
			mapViewer.interestPlaces = new ArrayList<Sprite>();
		}

		mapViewer.interestPlaces.add(placeSprite);
	}
	
	private static Sprite createPOISprite(final MapViewerActivity mapViewer, final PlaceOfInterest poi) {		
        
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
		}, Util.getRuntimeIndoorMap().getCellPixel(), Util.getRuntimeIndoorMap().getCellPixel());
		
		float pX = poi.getX() * Util.getRuntimeIndoorMap().getCellPixel();
		float pY = poi.getY() * Util.getRuntimeIndoorMap().getCellPixel();
		placeSprite.setPosition(pX, pY);
		
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_USER).attachChild(placeSprite);
		mapViewer.mainScene.registerTouchArea(placeSprite);
		
		return placeSprite;
	}
	
	
	private static void displayInterestPlaceContent(MapViewerActivity mapViewer, PlaceOfInterest poi){
		
		// refine video URL as the general web page
		// if the web page URL is defined, load web page from URL using web browswer
		if (poi.webUrl != null) {
			Intent intent_show_interest_place = new Intent(mapViewer, POIWebViewerActivity.class); 
			Bundle mBundle = new Bundle(); 
			mBundle.putInt(POIWebViewerActivity.BUNDLE_KEY_POI_ID, poi.id);
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
			mBundle.putInt(POIBaseActivity.BUNDLE_KEY_POI_ID,poi.id);
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

	public static void showPOILabeOnMap(MapViewerActivity mapViewer) {
		float currentZoomFactor = mapViewer.mCamera.getZoomFactor();
		
		// Clear old Map info
		if (mapViewer.mapInfos == null) {
			mapViewer.mapInfos = new ArrayList<Text>();
		} else {
			for (Text text:mapViewer.mapInfos) {
				if (text != null) {
					mapViewer.mainScene.detachChild(text);
				}
			}
			mapViewer.mapInfos.clear();
		}
		
		// Show New Map Info

		for (PlaceOfInterest poi: POIManager.POIList) {			
			if ((currentZoomFactor >= poi.minZoomFactor)
				 && (currentZoomFactor <= poi.maxZoomFactor))
				addTextTag(mapViewer,poi);			
		}		
	}		
	
	
	private static void addTextTag(MapViewerActivity mapViewer, PlaceOfInterest poi) {
		float pX = poi.getX();// * Util.getRuntimeIndoorMap().getCellPixel();
		float pY = poi.getY();// * Util.getRuntimeIndoorMap().getCellPixel();
				
		Text text = new Text(pX,
				pY, 
				mapViewer.mFont_mapinfo, 
				poi.label,
				100,
				mapViewer.getVertexBufferObjectManager());
		text.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		text.setAlpha(poi.getAlpha());
		text.setRotation(poi.getRotation()); // For future use if we need to rotate a angle
		text.setScale(poi.getScale()); // For future use if we need to display some label with a bigger/smaller scale
		text.setPosition(pX-text.getWidth()/2, pY-text.getHeight()/2);
		
		mapViewer.mainScene.attachChild(text);
		
		// Store so we can clear them in future if needed
		if (mapViewer.mapInfos == null) {
			mapViewer.mapInfos = new ArrayList<Text>();
		}
		
		mapViewer.mapInfos.add(text);
	}	
	

}
