package com.winjune.wifiindoor.drawing;

import org.andengine.engine.camera.ZoomCamera;

import com.winjune.wifiindoor.MapViewerActivity;
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

	// 锟矫伙拷锟结触锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷1锟斤拷MotionEvent ACTION_DOWN锟斤拷锟斤拷
	public boolean onDown(MotionEvent e) {
		//Log.e("onDown", "[isMovingCamera=true]");
		isMovingCamera = true;
		return true;
	}

	// 锟矫伙拷锟斤拷锟铰达拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟狡讹拷锟斤拷锟缴匡拷,锟斤拷1锟斤拷MotionEvent ACTION_DOWN,
	// 锟斤拷锟紸CTION_MOVE, 1锟斤拷ACTION_UP锟斤拷锟斤拷
	// e1锟斤拷锟斤拷1锟斤拷ACTION_DOWN MotionEvent
	// e2锟斤拷锟斤拷锟揭伙拷锟紸CTION_MOVE MotionEvent
	// velocityX锟斤拷X锟斤拷锟较碉拷锟狡讹拷锟劫度ｏ拷锟斤拷锟斤拷/锟斤拷
	// velocityY锟斤拷Y锟斤拷锟较碉拷锟狡讹拷锟劫度ｏ拷锟斤拷锟斤拷/锟斤拷
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		//Log.e("onFling", "[set isMovingCamera to false]");
		
		if (isMovingCamera){
			isMovingCamera = false;
		}else{
		
		}
		
		return true;
	}

	// 锟矫伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟缴讹拷锟組otionEvent ACTION_DOWN锟斤拷锟斤拷
	public void onLongPress(MotionEvent e) {
		//Log.e("onLongPress", "[onLongPress]"+e.getX()+","+e.getY());
		MapViewerUtil.handleLongPress(activity, e);
	}

	// 锟矫伙拷锟斤拷锟铰达拷锟斤拷锟斤拷锟斤拷锟斤拷锟较讹拷锟斤拷锟斤拷1锟斤拷MotionEvent ACTION_DOWN, 锟斤拷锟紸CTION_MOVE锟斤拷锟斤拷
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

	// 锟矫伙拷锟结触锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷未锟缴匡拷锟斤拷锟较讹拷锟斤拷锟斤拷一锟斤拷1锟斤拷MotionEvent ACTION_DOWN锟斤拷锟斤拷
	// 注锟斤拷锟給nDown()锟斤拷锟斤拷锟角匡拷锟斤拷锟斤拷锟矫伙拷锟斤拷煽锟斤拷锟斤拷锟斤拷隙锟斤拷锟阶刺�	
	public void onShowPress(MotionEvent e) {
		//Log.e("onShowPress", "[onShowPress]");
	}
	

	// 锟矫伙拷锟斤拷锟结触锟斤拷锟斤拷锟斤拷锟斤拷锟缴匡拷锟斤拷锟斤拷一锟斤拷1锟斤拷MotionEvent ACTION_UP锟斤拷锟斤拷
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

