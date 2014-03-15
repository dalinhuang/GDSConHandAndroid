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
import com.winjune.wifiindoor.activity.InterestPlaceViewerActivity;
import com.winjune.wifiindoor.activity.InterestPlaceTTSViewerActivity;
import com.winjune.wifiindoor.activity.InterestPlaceWebViewActivity;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.AnimatedUnit;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.drawing.graphic.model.SpriteListener;
import com.winjune.wifiindoor.map.InterestPlace;
import com.winjune.wifiindoor.map.InterestPlacesInfo;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.VersionOrMapIdRequest;

public class InterestPlaceBar {

	public static void showGuideAudioBar(final MapViewerActivity mapViewer) {	
		mapViewer.runOnUiThread(new Runnable() {
			  public void run() {
				  InterestPlaceBar.createGuideAudioBar(mapViewer);
			  }
		});
	}	
	
	public static void createGuideAudioBar(final MapViewerActivity mapViewer) {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(mapViewer);
		
	    builder.setIcon(R.drawable.ic_launcher);
	    builder.setTitle(R.string.audio_guide_title);
		
		LayoutInflater inflater = mapViewer.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.guide_audio_input, (ViewGroup) mapViewer.findViewById(R.id.audio_guide_input));
		builder.setView(layout);
		
		builder.setPositiveButton(R.string.play_audio_guide, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 EditText inputIPNumText = (EditText) layout.findViewById(R.id.audio_no_input_result);
				 String inputIPNumStr = inputIPNumText.getText().toString();
				 Boolean IPFound = false;
																		 
				 if (inputIPNumStr != null) {								 	
					 	int inputIPNum = Integer.parseInt(inputIPNumStr); 
					 
						InterestPlacesInfo interestPlacesInfo = new InterestPlacesInfo();
						
						// load interest place list	
						try {
							InputStream map_file_is = new FileInputStream(Util.getInterestPlacesInfoFilePathName(""+Util.getRuntimeIndoorMap().getMapId()));
							
							interestPlacesInfo.fromXML(map_file_is);
						} catch (Exception e) {																		
							e.printStackTrace();
						}
						
						// look for the matched place record									
						ArrayList<InterestPlace> places = interestPlacesInfo.getFields();
						
						if (places != null) {																		
							for (InterestPlace place : places) {
								if (place != null) {
									if (place.getSerial() == inputIPNum) {																										
										IPFound = true;
										
										displayInterestPlaceContent(mapViewer, place);								 												
									}												
								}
							}
						}									
				 }
				 
				 if (!IPFound) { 
					 Util.showLongToast(mapViewer, R.string.audio_no_not_exist);							
				 }
				 
				 dialog.dismiss();
			}
		});

		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		builder.create();
		builder.show();
	}
	
	public static void loadInterestPlaces(MapViewerActivity mapViewer) {
		InterestPlacesInfo interestPlacesInfo = new InterestPlacesInfo();
		boolean updateNeeded = true; //Hoare: update every time regardless map version, for test only

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
		
		showInterestPlacesInfo(mapViewer, interestPlacesInfo, false);
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
			Intent intent_show_interest_place = new Intent(mapViewer, InterestPlaceWebViewActivity.class); 
			Bundle mBundle = new Bundle(); 
			mBundle.putSerializable(IndoorMapData.BUNDLE_KEY_INTEREST_PLACE_INSTANCE, place);
			intent_show_interest_place.putExtras(mBundle); 
			mapViewer.startActivity(intent_show_interest_place);
			
		} else {
			Intent intent_show_interest_place = new Intent(mapViewer, InterestPlaceViewerActivity.class);
			
			if (needEnterTTSActivity(place)){
				intent_show_interest_place = new Intent(mapViewer, InterestPlaceTTSViewerActivity.class); 
			}
			else{
				intent_show_interest_place = new Intent(mapViewer, InterestPlaceViewerActivity.class); 
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
