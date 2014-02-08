package com.ericsson.cgc.aurora.wifiindoor.drawing;

import org.andengine.engine.camera.ZoomCamera;

import com.ericsson.cgc.aurora.wifiindoor.MapViewerActivity;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;

import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public final class MapCameraViewGestureListener implements OnGestureListener {
	private static final float VIEW_MOVEMENT_BASE_RATIO = 0.1f;  // Adjust this to let the scroll follow your finger

	private MapViewerActivity activity;

	public MapCameraViewGestureListener(MapViewerActivity activity) {
		this.activity = activity;
	}
	
	private boolean isMovingCamera;

	// �û��ᴥ����������1��MotionEvent ACTION_DOWN����
	public boolean onDown(MotionEvent e) {
		//Log.e("onDown", "[isMovingCamera=true]");
		isMovingCamera = true;
		return true;
	}

	// �û����´������������ƶ����ɿ�,��1��MotionEvent ACTION_DOWN,
	// ���ACTION_MOVE, 1��ACTION_UP����
	// e1����1��ACTION_DOWN MotionEvent
	// e2�����һ��ACTION_MOVE MotionEvent
	// velocityX��X���ϵ��ƶ��ٶȣ�����/��
	// velocityY��Y���ϵ��ƶ��ٶȣ�����/��
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		//Log.e("onFling", "[set isMovingCamera to false]");
		
		if (isMovingCamera){
			isMovingCamera = false;
		}else{
		
		}
		
		return true;
	}

	// �û��������������ɶ��MotionEvent ACTION_DOWN����
	public void onLongPress(MotionEvent e) {
		//Log.e("onLongPress", "[onLongPress]"+e.getX()+","+e.getY());
		activity.handleLongPress(e);
	}

	// �û����´����������϶�����1��MotionEvent ACTION_DOWN, ���ACTION_MOVE����
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		//Log.e("onScroll", "[isMovingCamera]="+isMovingCamera);
		
		ZoomCamera camera = activity.getMCamera();

		if (isMovingCamera){
			float ratio = getMoveRatio();
			
			activity.setCameraCenterAndReloadMapPieces(
					camera.getCenterX() + ratio * distanceX * Util.getCurrentCellPixel(), 
					camera.getCenterY() + ratio * distanceY * Util.getCurrentCellPixel(),
					true);
		}
		
		return true;
	}

	// �û��ᴥ����������δ�ɿ����϶�����һ��1��MotionEvent ACTION_DOWN����
	// ע���onDown()�����ǿ������û���ɿ������϶���״̬
	public void onShowPress(MotionEvent e) {
		//Log.e("onShowPress", "[onShowPress]");

	}

	// �û����ᴥ���������ɿ�����һ��1��MotionEvent ACTION_UP����
	public boolean onSingleTapUp(MotionEvent e) {
		//Log.e("onSingleTapUp", "[onSingleTapUp]");
		return true;
	}

	private float getMoveRatio() {
		ZoomCamera camera = activity.getMCamera();
		// When in zoom mode, the ratio need to be changed
		return VIEW_MOVEMENT_BASE_RATIO / camera.getZoomFactor();
	}
}

