package com.ericsson.cgc.aurora.wifiindoor.drawing;

import org.andengine.engine.camera.ZoomCamera;

import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

import com.ericsson.cgc.aurora.wifiindoor.MapViewerActivity;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;

public final class MapCameraViewGestureListener implements OnGestureListener {
	private static final float VIEW_MOVEMENT_BASE_RATIO = 0.1f;  // Adjust this to let the scroll follow your finger

	private MapViewerActivity activity;

	private boolean isMovingCamera;
	
	public MapCameraViewGestureListener(MapViewerActivity activity) {
		this.activity = activity;
	}

	private float getMoveRatio() {
		ZoomCamera camera = activity.getMCamera();
		// When in zoom mode, the ratio need to be changed
		return VIEW_MOVEMENT_BASE_RATIO / camera.getZoomFactor();
	}

	// 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
	public boolean onDown(MotionEvent e) {
		//Log.e("onDown", "[isMovingCamera=true]");
		isMovingCamera = true;
		return true;
	}

	// 用户按下触摸屏、快速移动后松开,由1个MotionEvent ACTION_DOWN,
	// 多个ACTION_MOVE, 1个ACTION_UP触发
	// e1：第1个ACTION_DOWN MotionEvent
	// e2：最后一个ACTION_MOVE MotionEvent
	// velocityX：X轴上的移动速度，像素/秒
	// velocityY：Y轴上的移动速度，像素/秒
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		//Log.e("onFling", "[set isMovingCamera to false]");
		
		if (isMovingCamera){
			isMovingCamera = false;
		}else{
		
		}
		
		return true;
	}

	// 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
	public void onLongPress(MotionEvent e) {
		//Log.e("onLongPress", "[onLongPress]"+e.getX()+","+e.getY());
		activity.handleLongPress(e);
	}

	// 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		//Log.e("onScroll", "[isMovingCamera]="+isMovingCamera);
		
		ZoomCamera camera = activity.getMCamera();

		if (isMovingCamera){
			float ratio = getMoveRatio();
			camera.setCenter(camera.getCenterX() + ratio * distanceX
					* Util.getCurrentCellPixel(), camera.getCenterY() + ratio
					* distanceY * Util.getCurrentCellPixel());

			//Log.e("onScroll", "distance[" + distanceX + "," + distanceY + "]");
			return true;
		}else{
		}
		
		return true;
	}

	// 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
	// 注意和onDown()的区别，强调的是没有松开或者拖动的状态
	public void onShowPress(MotionEvent e) {
		//Log.e("onShowPress", "[onShowPress]");

	}

	// 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
	public boolean onSingleTapUp(MotionEvent e) {
		//Log.e("onSingleTapUp", "[onSingleTapUp]");
		return true;
	}
}

