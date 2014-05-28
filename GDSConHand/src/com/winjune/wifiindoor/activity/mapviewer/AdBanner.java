package com.winjune.wifiindoor.activity.mapviewer;

import java.io.File;
import java.io.InputStream;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Log;

import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.ads.AdGroup;
import com.winjune.wifiindoor.ads.AdSpriteListener;
import com.winjune.wifiindoor.ads.ScreenAdvertisement;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.util.AdData;
import com.winjune.wifiindoor.util.AdUtil;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;

public class AdBanner {
	
	public static void showAd(MapViewerActivity mapViewer, boolean default_ad) {
		// we don't display AD in planning mode
		if (VisualParameters.PLANNING_MODE_ENABLED)
			return;
		
		if (!VisualParameters.ADS_ENABLED) {
			return;
		}
		
		putAdvertiseUnit(mapViewer, default_ad);	
		
		mapViewer.mAdvertisement.showAdvertisement();
	}
	
	public static void putAdvertiseUnit(final MapViewerActivity mapViewer, boolean default_ad) {	
		// For double check
		if (mapViewer.mapADSprite != null) {		
			mapViewer.mapADSprite.detachSelf();
		}

		Library.ADVERTISE.setAdPictureName(mapViewer.mAdvertisement.readAdPicName());
		Library.ADVERTISE.setUrls(mapViewer.mAdvertisement.readAdUrl());
		
		AdSpriteListener spriteListener = new AdSpriteListener() {

			@Override
			public boolean onAreaTouched(Sprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {

				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					//get the url and open link. 
					Uri uri = Uri.parse(Library.ADVERTISE.getUrls());  
					Intent it = new Intent(Intent.ACTION_VIEW, uri); 
					mapViewer.startActivity(it);
				}

				return true;
			}
		};
		
		mapViewer.mapADSprite = Library.ADVERTISE.load(mapViewer, Util.getRuntimeMap(), spriteListener, default_ad);
		
		float adWidth = mapViewer.mapADSprite.getWidth();
		float adHeight = mapViewer.mapADSprite.getHeight();		

		float x = 0;
		float y = 0;
		
		if (mapViewer.mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Rotate base on the sprite's center point
			x = Util.getCameraWidth() - 1.5f * mapViewer.CONTROL_BUTTON_WIDTH - adWidth * 0.5f - adHeight * 0.5f;
			y = Util.getCameraHeight() * 0.5f - adHeight * 0.5f;	
			mapViewer.mapADSprite.setRotation(90.0f);
		} else {
			if (adWidth <  Util.getCameraWidth()) {
				x = ( Util.getCameraWidth() - adWidth) * 0.5f;
			}
			
			y =  Util.getCameraWidth() - adHeight;
		}
		
		mapViewer.mapADSprite.setPosition(x, y);

		mapViewer.hud.attachChild(mapViewer.mapADSprite);

		mapViewer.hud.registerTouchArea(mapViewer.mapADSprite);
	}
	
	public static void checkAndDownloadAd(MapViewerActivity mapViewer, AdGroup adGroup){ 
		if (mapViewer.advertisePeriodThread.isInit == true){
			mapViewer.advertisePeriodThread.isInit = false;
		}
		
		mapViewer.mAdvertisement.setadGroup(adGroup);
		mapViewer.mAdvertisement.checkAndDownloadAds();
		if (mapViewer.advertisePeriodThread.isInit == false){
			mapViewer.advertisePeriodThread.isInit = true;
		}		
	}
	
    public static class AdvertisePeriodThread extends Thread{
    	
    	public MapViewerActivity mapViewer;
    	public AdvertisePeriodThread(MapViewerActivity activity) {
    		mapViewer = activity;
    	}
    	
        //运行状态，下一步骤有大用
        public boolean isRunning = true;
        public boolean isInit = false;
        public void run() {
            while(isRunning){
               try {
                  if (isInit){	 
                    
                	  mapViewer.mAdvertisement.refreshAdvertise();
                	  mapViewer.hud.detachChild(mapViewer.mapADSprite);
                	  mapViewer.hud.unregisterTouchArea(mapViewer.mapADSprite);
                	  mapViewer.mapADSprite.dispose();
                	  mapViewer.mapADSprite = null;
                	  // mapADSprite.reset();
                	  Library.ADVERTISE.resetAdUnit();
                	  showAd(mapViewer, false);
                	  sleep(AdData.AD_PERIDOIC_SLEEP_TIME);

                  }       
	           } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
        }
    }
    
	public static void startPeriodicAdvertiseThread(MapViewerActivity mapViewer){
		// we don't display AD in planning mode
		if (VisualParameters.PLANNING_MODE_ENABLED)
			return;
		
		if (!VisualParameters.ADS_ENABLED) {
			return;
		}
		
		if (mapViewer.advertisePeriodThread == null){
			mapViewer.advertisePeriodThread = new AdvertisePeriodThread(mapViewer);
			mapViewer.advertisePeriodThread.isRunning = true;
			mapViewer.advertisePeriodThread.isInit = false;
			mapViewer.advertisePeriodThread.start();
		}else {		
			Log.d("AdBanner", "PeriodicLocateMeThread already starts.");
		}
			
	}

	public static void showDefaultAd(MapViewerActivity mapViewer){
		
		// we don't display AD in planning mode
		if (VisualParameters.PLANNING_MODE_ENABLED)
			return;
		
		if (VisualParameters.ADS_ENABLED) {
			//here just need to display default advertise. 
			mapViewer.mAdvertisement = new ScreenAdvertisement(mapViewer,Util.getRuntimeMap());
			mapViewer.mAdvertisement.initAdvertiseData();
			
			try {
				InputStream inputStream = mapViewer.getResources().getAssets().open("default_ad/sample_ad1.png");
		        File file = AdUtil.createFileFromInputStream(inputStream,"sample_ad1.png");
		        AdData.FILE_DEFAULT_AD=file;
		        if (file.exists()){
		        	showAd(mapViewer, true);     	
		        }
		        inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	

	public static void hideAd(MapViewerActivity mapViewer) {
		if (VisualParameters.ADS_ENABLED){
			mapViewer.mAdvertisement.hideAdvertisement();
		}
	}
		
}
