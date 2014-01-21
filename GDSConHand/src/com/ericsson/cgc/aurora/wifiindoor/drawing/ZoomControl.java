package com.ericsson.cgc.aurora.wifiindoor.drawing;

import org.andengine.engine.camera.ZoomCamera;

import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

import com.ericsson.cgc.aurora.wifiindoor.util.VisualParameters;

/**
 * @author haleyshi
 *
 */
public class ZoomControl {
	
	private static final float CHANGE_UNIT = 0.1f;

	private ZoomCamera mCamera;
	
	private float maxZoomFactor;
	private float minZoomFactor;
	
	private OnScaleGestureListener scaleGestureListner;

	public ZoomControl(ZoomCamera zCamera, float maxZoomFactor, float minZoomFactor) {
		this.mCamera = zCamera;
		this.maxZoomFactor = maxZoomFactor;
		this.minZoomFactor = minZoomFactor;
		
		this.scaleGestureListner = new OnScaleGestureListener() {
			
			private float previousSpan;
			
			private float spanChangeUnit = 10 * VisualParameters.density;
			
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				float currentSpan = detector.getCurrentSpan();

				//Log.d("zoomGestureDector", "scale on, span:" + currentSpan);
				
				if (previousSpan - currentSpan > spanChangeUnit){
					int num = (int) ((previousSpan - currentSpan) / spanChangeUnit);
					//Log.d("zoomGestureDector", "zoom in :" + num);
					zoomOut(num);
					previousSpan = currentSpan;
				}
				
				if (currentSpan - previousSpan > spanChangeUnit){
					int num = (int) ((currentSpan - previousSpan) / spanChangeUnit);
					//Log.d("zoomGestureDector", "zoom out :" + num);
					zoomIn(num);
					previousSpan = currentSpan;
				}
				
				return true;
			}
			
			@Override
			public boolean onScaleBegin(ScaleGestureDetector detector) {
				//Log.d("zoomGestureDector", "scale begin");
				previousSpan = detector.getCurrentSpan();
				return true;
			}
			
			@Override
			public void onScaleEnd(ScaleGestureDetector detector) {
				//Log.d("zoomGestureDector", "scale end");
			}
		};
	}

	public OnScaleGestureListener getScaleGestureListner() {
		return scaleGestureListner;
	}
	
	public float getZoomFactor(){
		return mCamera.getZoomFactor();
	}
	
	public void zoomIn(){
		zoomIn(1);
	}
	
	public void zoomIn(int num){
		float target = mCamera.getZoomFactor() + num * CHANGE_UNIT;
		if (target < maxZoomFactor){
			mCamera.setZoomFactor(target);
		}else{
			mCamera.setZoomFactor(maxZoomFactor);
		}
	}
	
	public void zoomMostIn(){
		mCamera.setZoomFactor(maxZoomFactor);
	}

	public void zoomMostOut(){
		mCamera.setZoomFactor(minZoomFactor);
	}
	
	public void zoomOut(){
		zoomOut(1);
	}
	
	public void zoomOut(int num){
		float target = mCamera.getZoomFactor() - num * CHANGE_UNIT;
		if (target > minZoomFactor) {
			mCamera.setZoomFactor(target);
		}else{
			mCamera.setZoomFactor(minZoomFactor);
		}
	}
}
