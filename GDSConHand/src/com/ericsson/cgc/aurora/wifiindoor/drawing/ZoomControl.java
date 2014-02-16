package com.ericsson.cgc.aurora.wifiindoor.drawing;

import org.andengine.engine.camera.ZoomCamera;

import com.ericsson.cgc.aurora.wifiindoor.MapLocatorActivity;
import com.ericsson.cgc.aurora.wifiindoor.MapViewerActivity;
import com.ericsson.cgc.aurora.wifiindoor.R;
import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;
import com.ericsson.cgc.aurora.wifiindoor.util.WifiIpsSettings;

import android.content.Intent;
import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

/**
 * @author haleyshi
 *
 */
public class ZoomControl {
	
	private MapViewerActivity activity;
	
	private static float CHANGE_UNIT = 0.1f;

	private ZoomCamera mCamera;
	
	private float maxZoomFactor;
	private float minZoomFactor;
	
	private OnScaleGestureListener scaleGestureListner;

	public ZoomControl(MapViewerActivity zActivity, ZoomCamera zCamera, float maxZoomFactor, float minZoomFactor, final float density) {
		
		this.activity = zActivity;
		this.mCamera = zCamera;
		this.maxZoomFactor = maxZoomFactor;
		this.minZoomFactor = minZoomFactor;
		CHANGE_UNIT = 0.1f * density;
		
		this.scaleGestureListner = new OnScaleGestureListener() {
			
			@Override
			public void onScaleEnd(ScaleGestureDetector detector) {
				//Log.d("zoomGestureDector", "scale end");
			}
			
			private float previousSpan;
			
			@Override
			public boolean onScaleBegin(ScaleGestureDetector detector) {
				//Log.d("zoomGestureDector", "scale begin");
				previousSpan = detector.getCurrentSpan();
				return true;
			}
			
			private float spanChangeUnit = 10 * 1.0f * density;
			
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				float currentSpan = detector.getCurrentSpan();

				//Log.d("zoomGestureDector", "scale on, span:" + currentSpan);
				
				if (previousSpan - currentSpan > spanChangeUnit){
					int num = (int) ((previousSpan - currentSpan) / spanChangeUnit);
					//Log.d("zoomGestureDector", "zoom in :" + num);
					zoomOut(1);
					previousSpan = currentSpan;
				}
				
				if (currentSpan - previousSpan > spanChangeUnit){
					int num = (int) ((currentSpan - previousSpan) / spanChangeUnit);
					//Log.d("zoomGestureDector", "zoom out :" + num);
					zoomIn(1);
					previousSpan = currentSpan;
				}
				
				return true;
			}
		};
	}

	public void zoomIn(){
		zoomIn(1);
	}
	
	public void zoomIn(int num){
		float target = mCamera.getZoomFactor() + num * CHANGE_UNIT;
		if (target < maxZoomFactor){
			mCamera.setZoomFactor(target);
		}else{			
			//Hoare: for test, mapid is harcoded		
			if (WifiIpsSettings.ZOOM_SWITCH ) {
				if (Util.getRuntimeIndoorMap().getMapId() == 2) {
					int colNo = activity.getCenterColNo();
					int rowNo = activity.getCenterRowNo();
					
					Intent intent_locate_map = new Intent(activity,MapLocatorActivity.class);
					Bundle mBundle = new Bundle();
					mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_VIEWER);
					mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP, 1);
					mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_COL, colNo);
					mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_ROW, rowNo);				
					intent_locate_map.putExtras(mBundle);
					activity.startActivity(intent_locate_map);	
					//activity.finish();
					//System.gc();
				} else {
					mCamera.setZoomFactor(maxZoomFactor);
				}
			}else {
				mCamera.setZoomFactor(maxZoomFactor);
			}
		}
	}
	
	public void zoomOut(){
		zoomOut(1);
	}
	
	public void zoomOut(int num){
		float target = mCamera.getZoomFactor() - num * CHANGE_UNIT;
		if (target > minZoomFactor) {
			mCamera.setZoomFactor(target);
			
			// Need to reload map pieice to ensure no blank areas
			activity.setCameraCenterAndReloadMapPieces(
					mCamera.getCenterX(), 
					mCamera.getCenterY(),
					true);
		} else {
			//Hoare: for test
			if (WifiIpsSettings.ZOOM_SWITCH) {
				if (Util.getRuntimeIndoorMap().getMapId() == 1) {
					
					int colNo = activity.getCenterColNo();
					int rowNo = activity.getCenterRowNo();
					
					// loading new map
					Intent intent_locate_map = new Intent(activity,MapLocatorActivity.class);
					// Bundle bundle = getIntent().getExtras();
					Bundle mBundle = new Bundle();
					mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_VIEWER);
					mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP, 2);
					mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_COL, colNo);
					mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_ROW, rowNo);
					intent_locate_map.putExtras(mBundle);
					activity.startActivity(intent_locate_map);
					//activity.finish();
					//System.gc();
				} else {
					mCamera.setZoomFactor(minZoomFactor);
				}
			} else {
				mCamera.setZoomFactor(minZoomFactor);
			}
		}
	}
	
	public void zoomMostIn(){
		mCamera.setZoomFactor(maxZoomFactor);
	}

	public void zoomMostOut(){
		mCamera.setZoomFactor(minZoomFactor);
		
		// Need to reload map piece to ensure there is no white areas
		activity.setCameraCenterAndReloadMapPieces(
				mCamera.getCenterX(), 
				mCamera.getCenterY(),
				true);		
	}
	
	public float getZoomFactor(){
		return mCamera.getZoomFactor();
	}
	
	public OnScaleGestureListener getScaleGestureListner() {
		return scaleGestureListner;
	}
}
