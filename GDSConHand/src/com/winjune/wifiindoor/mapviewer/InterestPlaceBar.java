package com.winjune.wifiindoor.mapviewer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.activity.poiviewer.POIAudioPlayerActivity;
import com.winjune.wifiindoor.activity.poiviewer.POIBaseActivity;
import com.winjune.wifiindoor.activity.poiviewer.POINormalViewerActivity;
import com.winjune.wifiindoor.activity.poiviewer.POITtsPlayerActivity;
import com.winjune.wifiindoor.activity.poiviewer.POIWebViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.AnimatedUnit;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.drawing.graphic.model.SpriteListener;
import com.winjune.wifiindoor.map.InterestPlace;
import com.winjune.wifiindoor.map.InterestPlacesInfo;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.poi.*;
import com.winjune.wifiindoor.util.VisualParameters;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.VersionOrMapIdRequest;

public class InterestPlaceBar {

	public static void loadInterestPlaces(MapViewerActivity mapViewer) {
		InterestPlacesInfo interestPlacesInfo = new InterestPlacesInfo();
		boolean updateNeeded = false; //Hoare: update every time regardless map version, for test only

		try {
			InputStream map_file_is = new FileInputStream(Util.getInterestPlacesInfoFilePathName(""+Util.getRuntimeIndoorMap().getMapId()));
			
			interestPlacesInfo.fromXML(map_file_is);
			// file has already been closed
			//map_file_is.close();
			
			// For Files in SD Card but not
			//load_map_rc = designMap.fromXML(IndoorMapData.map_file_path + map_file_name);
			
			if (interestPlacesInfo.getVersionCode() != Util.getRuntimeIndoorMap().getVersionCode()) {
				updateNeeded = true;
			}
		} catch (Exception e) {
			updateNeeded = true;
		}
		
		if (updateNeeded) {
			// Hoare: harcode map_id 1 as the GDSC map
			int mapid = Util.getRuntimeIndoorMap().getMapId();
			
			if (mapid == 1 ) {
				mapid = 2;
			}				
				
			downloadInterestPlaces(mapViewer, mapid);
			return;
		}
		
		
		// POIManager.interestPlacesInfo2Poi(interestPlacesInfo);
		
		showPOIIconOnMap(mapViewer);
		// showInterestPlacesInfo(mapViewer, interestPlacesInfo, false);
	}

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
	

	public static void showInterestPlacesInfo(MapViewerActivity mapViewer, InterestPlacesInfo interestPlacesInfo, boolean storeNeeded) {
		if (interestPlacesInfo == null) {
			return;
		}
		
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
		
		// Show New Map Info
		ArrayList<InterestPlace> places = interestPlacesInfo.getFields();
		
		if (places == null) {
			return;
		}
		
		for (InterestPlace place : places) {
			if (place != null) {
				// X and Y = -1 mean the guide audio 
				if ((place.getX() != -1)&& (place.getY() != -1)) {  
					addInterestPlace(mapViewer, place);
				}
			}
		}
		
		// Store in File, put it here so the info may be re-encoded above in future.
		if (storeNeeded) {
			interestPlacesInfo.toXML();
		}
	}

	private static void addInterestPlace(MapViewerActivity mapViewer, InterestPlace place) {
		// Create and attach Sprite
		Sprite placeSprite = createInterestPlaceSprite(mapViewer, place);
		
		// Store so we can clear them in future if needed
		if (mapViewer.interestPlaces == null) {
			mapViewer.interestPlaces = new ArrayList<Sprite>();
		}

		mapViewer.interestPlaces.add(placeSprite);
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
		
	
	private static Sprite createInterestPlaceSprite(final MapViewerActivity mapViewer, final InterestPlace place) {		
        
		AnimatedUnit  whichSvg = Library.INTEREST_PLACE_FOR_IE;	
		
		String text = null;
    	String picture = null;
    	String audio = null;
    	String webUrl = null;
		
		text = place.getInfo();
		picture = place.getUrlPic();
		audio = place.getUrlAudio();
		webUrl = place.getUrlVideo();
		
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
					
				   displayInterestPlaceContent(mapViewer, place);
				}

				return true;
			}
		}, Util.getRuntimeIndoorMap().getCellPixel(), Util.getRuntimeIndoorMap().getCellPixel());
		
		float pX = place.getX() * Util.getRuntimeIndoorMap().getCellPixel();
		float pY = place.getY() * Util.getRuntimeIndoorMap().getCellPixel();
		placeSprite.setPosition(pX, pY);
		
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_USER).attachChild(placeSprite);
		mapViewer.mainScene.registerTouchArea(placeSprite);
		
		return placeSprite;
	}

	private static void downloadInterestPlaces(MapViewerActivity mapViewer, int mapId) {
		VersionOrMapIdRequest id = new VersionOrMapIdRequest();
		id.setCode(mapId);

		try {
			
			Gson gson = new Gson();
			String json = gson.toJson(id);
			JSONObject data = new JSONObject(json);

			if (IpsWebService.sendMessage(mapViewer, IpsMsgConstants.MT_INTEREST_PLACES_QUERY, data)) {
				
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			Util.showToast(mapViewer, "GET INTEREST PLACES ERROR: " + ex.getMessage(), Toast.LENGTH_LONG);
			ex.printStackTrace();
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
	
	private static boolean needEnterTTSActivity(InterestPlace place){
		boolean isTTSSupported = false;
		boolean enterTTSActivity = true;
		
		
		isTTSSupported = Util.isTTSSupported();
		
		String text = null;
    	String picture = null;
    	String audio = null;
		
		text = place.getInfo();
		picture = place.getUrlPic();
		audio = place.getUrlAudio();
		
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
	
	private static void displayInterestPlaceContent(MapViewerActivity mapViewer, InterestPlace place){
		
		// refine video URL as the general web page
		// if the web page URL is defined, load web page from URL using web browswer
		if (place.getUrlVideo() != null) {
			Intent intent_show_interest_place = new Intent(mapViewer, POIWebViewerActivity.class); 
			Bundle mBundle = new Bundle(); 
			mBundle.putSerializable(IndoorMapData.BUNDLE_KEY_INTEREST_PLACE_INSTANCE, place);
			intent_show_interest_place.putExtras(mBundle); 
			mapViewer.startActivity(intent_show_interest_place);
			
		} else {
			Intent intent_show_interest_place = new Intent(mapViewer, POIAudioPlayerActivity.class);
			
			if (needEnterTTSActivity(place)){
				intent_show_interest_place = new Intent(mapViewer, POITtsPlayerActivity.class); 
			}
			else{
				intent_show_interest_place = new Intent(mapViewer, POIAudioPlayerActivity.class); 
			}
			
			Bundle mBundle = new Bundle(); 
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM,
				IndoorMapData.BUNDLE_VAL_INTEREST_REQ_FROM_TOUCH);
			mBundle.putSerializable(IndoorMapData.BUNDLE_KEY_INTEREST_PLACE_INSTANCE, place);
			intent_show_interest_place.putExtras(mBundle); 
			mapViewer.startActivity(intent_show_interest_place);
		}		
	}		
}
	

