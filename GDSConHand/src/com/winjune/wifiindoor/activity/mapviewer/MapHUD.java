package com.winjune.wifiindoor.activity.mapviewer;

import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.AnimatedUnit;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.drawing.graphic.model.SpriteListener;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;

public class MapHUD {

	public static void putHUDControlUnit(MapViewerActivity mapViewer, AnimatedUnit unit, int posX, int posY,
			SpriteListener spriteListener) {

		if (MapViewerActivity.DEBUG)
			Log.d(MapViewerActivity.TAG, "Start putHUDControlUnit");

		AnimatedSprite sprite = unit.load(mapViewer, spriteListener);

		sprite.setPosition(posX, posY);
		sprite.setAlpha(VisualParameters.CONTROL_BUTTON_ALPHA);

		mapViewer.hud.attachChild(sprite);

		mapViewer.hud.registerTouchArea(sprite);

		if (MapViewerActivity.DEBUG)
			Log.d(MapViewerActivity.TAG, "End putHUDControlUnit");
	}


	public static void initialHUDMenuBar(final MapViewerActivity mapViewer) {

		if (MapViewerActivity.DEBUG)
			Log.d(MapViewerActivity.TAG, "Start initialHUDMenuBar");

		int x = Util.getCameraWidth() - mapViewer.CONTROL_BUTTON_WIDTH;
		int y = mapViewer.CONTROL_BUTTON_HEIGHT;						

		if (VisualParameters.PLANNING_MODE_ENABLED) {
			Library.BUTTON_MODE.load(mapViewer, mapViewer.CONTROL_BUTTON_WIDTH, mapViewer.CONTROL_BUTTON_HEIGHT);
			putHUDControlUnit(mapViewer, Library.BUTTON_MODE, x, y, new SpriteListener() {
	
				@Override
				public boolean onAreaTouched(AnimatedSprite sprite,
						TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
						float pTouchAreaLocalY) {
	
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						mapViewer.mMode++; // Put this line inner this check or it will cause
									// big problem
						if (mapViewer.mMode == IndoorMapData.MAP_MODE_MAX) {
							mapViewer.mMode = 0;
						}
						
						switch (mapViewer.mMode) {
							case IndoorMapData.MAP_MODE_VIEW:
								AdBanner.showAd(mapViewer, false);
								break;
							default:
								AdBanner.hideAd(mapViewer);
						}
						
						mapViewer.mTargetColNo = -1;
						mapViewer.mTargetRowNo = -1;
						mapViewer.mainScene.getChildByIndex(Constants.LAYER_USER).detachChild(Util.getRuntimeIndoorMap().getTarget().getSprite());
	
						mapViewer.modeControl.changeMode(sprite, mapViewer.mMode);
					}
	
					return true;
				}
			});
	
			y += mapViewer.CONTROL_BUTTON_HEIGHT + mapViewer.CONTROL_BUTTON_MARGIN * 2;
			Library.BUTTON_ACTION.load(mapViewer, mapViewer.CONTROL_BUTTON_WIDTH, mapViewer.CONTROL_BUTTON_HEIGHT);
			putHUDControlUnit(mapViewer, Library.BUTTON_ACTION, x, y, new SpriteListener() {
	
				@Override
				public boolean onAreaTouched(AnimatedSprite sprite,
						TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
						float pTouchAreaLocalY) {
	
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						decideNextActionAfter(mapViewer);
					}
	
					return true;
				}
			});
						
		}// planning mode

		if (MapViewerActivity.DEBUG)
			Log.d(MapViewerActivity.TAG, "End initialHUDMenuBar");

	}

	@SuppressLint("SimpleDateFormat")
	public static void initailHUDHintBar(MapViewerActivity mapViewer) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		String hintStr = sdf.format(new Date(System.currentTimeMillis())) + " " + mapViewer.getResources().getString(R.string.load_complete);
		
		// There is a bug that the future text can not be longer than the start one and that's why I appends some spaces here
		hintStr += "                                                            "
				+ "                                 ";
		
		mapViewer.mHintText = new Text(0,
				mapViewer.mFontHint.getLineHeight(),   // Flexible according to the height of 1st line text
				mapViewer.mFontHint, 
				hintStr,
				100,
				mapViewer.getVertexBufferObjectManager());
		mapViewer.mHintText.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		mapViewer.mHintText.setAlpha(VisualParameters.MAP_FONT_ALPHA);

		mapViewer.hud.attachChild(mapViewer.mHintText);
	}
	
	public static void decideNextActionAfter(final MapViewerActivity mapViewer) {
		
		if ((mapViewer.mTargetColNo==-1) || (mapViewer.mTargetRowNo==-1)) {
			//Util.showLongToast(this, R.string.need_a_selected_location);
			updateHinText(mapViewer, R.string.need_a_selected_location);
			return;
		}
		
		mapViewer.runOnUiThread(new Runnable() {
			  public void run() {
					
				  
					int messageId = R.string.confirm;
					
					switch (mapViewer.mMode)  {
					case IndoorMapData.MAP_MODE_VIEW:
						messageId = R.string.confirm_self_location_set;
						break;
					case IndoorMapData.MAP_MODE_EDIT:
						messageId = R.string.confirm_wifi_collect;
						break;
					case IndoorMapData.MAP_MODE_EDIT_TAG:
						messageId = R.string.confirm_nfc_qr_collect;
						break;
					case IndoorMapData.MAP_MODE_DELETE_FINGERPRINT:
						messageId = R.string.confirm_delete_fingerprint;
						break;
					case IndoorMapData.MAP_MODE_TEST_LOCATE:
						messageId = R.string.confirm_test_locate;
						break;
					case IndoorMapData.MAP_MODE_TEST_COLLECT:
						messageId = R.string.confirm_test_collect;
						break;
					case IndoorMapData.MAP_MODE_CONTINUOUS_COLLECT:
						if (mapViewer.mContStarted){
							messageId = R.string.confirm_cont_test_stop;
						}
						else{
							messageId = R.string.confirm_cont_test_start;
						}
						break;
					default:
					}	
					
				    AlertDialog.Builder builder = new AlertDialog.Builder(mapViewer);
					
					builder.setIcon(R.drawable.ic_launcher);
					builder.setTitle(mapViewer.getResources().getString(R.string.confirm) + " [" + mapViewer.mTargetColNo + "," + mapViewer.mTargetRowNo + "]");
					builder.setMessage(messageId);
					builder.setPositiveButton(R.string.yes, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (mapViewer.mMode)  {
							case IndoorMapData.MAP_MODE_VIEW:
								LocateBar.setCurrentLocation(mapViewer);
								break;
							case IndoorMapData.MAP_MODE_EDIT:
								PlanBar.collectFingerprint(mapViewer, false); // x, y
								break;
							case IndoorMapData.MAP_MODE_EDIT_TAG:
								PlanBar.addNfcQrLocation(mapViewer);
								break;
							case IndoorMapData.MAP_MODE_DELETE_FINGERPRINT:
								PlanBar.deleteFingerprint(mapViewer);
								break;
							case IndoorMapData.MAP_MODE_TEST_LOCATE:
								PlanBar.testLocate(mapViewer);
								break;
							case IndoorMapData.MAP_MODE_TEST_COLLECT:
								PlanBar.testCollect(mapViewer);
								break;
							case IndoorMapData.MAP_MODE_CONTINUOUS_COLLECT:
								if (mapViewer.mContStarted){
									PlanBar.continuousCollectStop(mapViewer);
								}
								else{
									PlanBar.continuousCollectStart(mapViewer);
								}
								break;
							default:
							}		
						}
					});

					builder.setNegativeButton(R.string.no, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
					
					builder.create();
					builder.show();
			  }
		});
	}	
	
	@SuppressLint("SimpleDateFormat")
	public static void updateHinText(MapViewerActivity mapViewer, String text) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		String clockStr = sdf.format(new Date(System.currentTimeMillis()));
		mapViewer.mHintText.setText(clockStr + " " + text);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static void updateHinText(MapViewerActivity mapViewer, int textId) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		String clockStr = sdf.format(new Date(System.currentTimeMillis()));
		mapViewer.mHintText.setText(clockStr + " " + mapViewer.getResources().getString(textId));
	}

	@SuppressWarnings("unused")
	public static void showProgressDialog(MapViewerActivity mapViewer) {
		if (mapViewer.mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(mapViewer);
			dialog.setTitle(R.string.get_data_dialog_title);
			dialog.setMessage(mapViewer.getString(R.string.get_data_dialog_content));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			mapViewer.mProgressDialog = dialog;
		}

		if (!mapViewer.mProgressDialog.isShowing())
			mapViewer.mProgressDialog.show();
	}

	@SuppressWarnings("unused")
	public static void dismissProgressDialog(MapViewerActivity mapViewer) {
		try {
			if (mapViewer.mProgressDialog != null)
				if (mapViewer.mProgressDialog.isShowing())
					mapViewer.mProgressDialog.dismiss();
		} catch (IllegalArgumentException e) {
			// We don't mind. android cleared it for us.
		}
	}	
		
	
}
