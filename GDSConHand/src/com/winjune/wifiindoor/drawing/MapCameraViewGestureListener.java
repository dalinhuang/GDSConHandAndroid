package com.winjune.wifiindoor.drawing;

import org.andengine.engine.camera.ZoomCamera;

import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.mapviewer.MapDrawer;
import com.winjune.wifiindoor.mapviewer.MapViewerUtil;
import com.winjune.wifiindoor.util.Util;

import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public final class MapCameraViewGestureListener implements OnGestureListener {
	private static final float VIEW_MOVEMENT_BASE_RATIO = 0.1f;  // Adjust this to let the scroll follow your finger

	private MapViewerActivity activity;

	public MapCameraViewGestureListener(MapViewerActivity activity) {
		this.activity = activity;
	}
	
	private boolean isMovingCamera;

	public boolean onDown(MotionEvent e) {
		//Log.e("onDown", "[isMovingCamera=true]");
		isMovingCamera = true;
		return true;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		//Log.e("onFling", "[set isMovingCamera to false]");
		
		if (isMovingCamera){
			isMovingCamera = false;
		}else{
		
		}
		
		return true;
	}

	public void onLongPress(MotionEvent e) {
		//Log.e("onLongPress", "[onLongPress]"+e.getX()+","+e.getY());
		MapViewerUtil.handleLongPress(activity, e);
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		//Log.e("onScroll", "[isMovingCamera]="+isMovingCamera);
		
		ZoomCamera camera = MapViewerUtil.getMCamera(activity);

		if (isMovingCamera){
			float ratio = getMoveRatio();
			
			MapDrawer.setCameraCenterAndReloadMapPieces(
					activity,
					camera.getCenterX() + ratio * distanceX * Util.getCurrentCellPixel(), 
					camera.getCenterY() + ratio * distanceY * Util.getCurrentCellPixel(),
					true);
		}
		
		return true;
	}

	public void onShowPress(MotionEvent e) {
		//Log.e("onShowPress", "[onShowPress]");
	}
	


	public boolean onSingleTapUp(MotionEvent e) {
		//Log.e("onSingleTapUp", "[onSingleTapUp]");
		return true;
	}

	private float getMoveRatio() {
		ZoomCamera camera = MapViewerUtil.getMCamera(activity);
		// When in zoom mode, the ratio need to be changed
		return VIEW_MOVEMENT_BASE_RATIO / camera.getZoomFactor();
	}
}

