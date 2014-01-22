package com.ericsson.cgc.aurora.wifiindoor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.LayoutGameActivity;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.ericsson.cgc.aurora.wifiindoor.ads.AdGroup;
import com.ericsson.cgc.aurora.wifiindoor.ads.AdSpriteListener;
import com.ericsson.cgc.aurora.wifiindoor.ads.ScreenAdvertisement;
import com.ericsson.cgc.aurora.wifiindoor.algorithm.NaviPath;
import com.ericsson.cgc.aurora.wifiindoor.algorithm.NaviUtil;
import com.ericsson.cgc.aurora.wifiindoor.drawing.GraphicIndoorMapListener;
import com.ericsson.cgc.aurora.wifiindoor.drawing.MapCameraViewGestureListener;
import com.ericsson.cgc.aurora.wifiindoor.drawing.ModeControl;
import com.ericsson.cgc.aurora.wifiindoor.drawing.SoundIndoorMapListener;
import com.ericsson.cgc.aurora.wifiindoor.drawing.ZoomControl;
import com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.model.AnimatedUnit;
import com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.model.Library;
import com.ericsson.cgc.aurora.wifiindoor.drawing.graphic.model.SpriteListener;
import com.ericsson.cgc.aurora.wifiindoor.map.FieldInfo;
import com.ericsson.cgc.aurora.wifiindoor.map.IndoorMap;
import com.ericsson.cgc.aurora.wifiindoor.map.IndoorMapLoader;
import com.ericsson.cgc.aurora.wifiindoor.map.MapInfo;
import com.ericsson.cgc.aurora.wifiindoor.map.NaviInfo;
import com.ericsson.cgc.aurora.wifiindoor.map.NaviNode;
import com.ericsson.cgc.aurora.wifiindoor.runtime.Cell;
import com.ericsson.cgc.aurora.wifiindoor.runtime.RuntimeIndoorMap;
import com.ericsson.cgc.aurora.wifiindoor.types.CollectInfo;
import com.ericsson.cgc.aurora.wifiindoor.types.InfoQueryRequest;
import com.ericsson.cgc.aurora.wifiindoor.types.Location;
import com.ericsson.cgc.aurora.wifiindoor.types.LocationQueryInfo;
import com.ericsson.cgc.aurora.wifiindoor.types.LocationSet;
import com.ericsson.cgc.aurora.wifiindoor.types.NfcLocation;
import com.ericsson.cgc.aurora.wifiindoor.types.QueryInfo;
import com.ericsson.cgc.aurora.wifiindoor.types.TestLocateCollectReply;
import com.ericsson.cgc.aurora.wifiindoor.types.TestLocateCollectRequest;
import com.ericsson.cgc.aurora.wifiindoor.types.VersionOrMapIdRequest;
import com.ericsson.cgc.aurora.wifiindoor.types.WifiFingerPrint;
import com.ericsson.cgc.aurora.wifiindoor.util.AdData;
import com.ericsson.cgc.aurora.wifiindoor.util.AdUtil;
import com.ericsson.cgc.aurora.wifiindoor.util.Constants;
import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;
import com.ericsson.cgc.aurora.wifiindoor.util.VisualParameters;
import com.ericsson.cgc.aurora.wifiindoor.util.WifiIpsSettings;
import com.ericsson.cgc.aurora.wifiindoor.webservice.MsgConstants;
import com.google.gson.Gson;

public class MapViewerActivity extends LayoutGameActivity implements SensorEventListener {

	class AdvertisePeriodThread extends Thread{
        //运行状态，下一步骤有大用
        public boolean isRunning = true;
        public boolean isInit = false;
        public void run() {
            while(isRunning){
                try {
                  if (isInit){	 
                    
                    mAdvertisement.refreshAdvertise();
                    hud.detachChild(mapADSprite);
                    hud.unregisterTouchArea(mapADSprite);
                    mapADSprite.dispose();
                    mapADSprite = null;
                   // mapADSprite.reset();
                    Library.ADVERTISE.resetAdUnit();
                    showAd(false);
                    sleep(AdData.AD_PERIDOIC_SLEEP_TIME);

                  }
                  
                    
           } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    }
	protected static final String TAG = MapViewerActivity.class.getSimpleName();

	private static final boolean DEBUG = WifiIpsSettings.DEBUG;
	private ZoomCamera mCamera;
	private Font mFont_hints;
	private Font mFont_mapinfo;
	private Font mFont_menu;
	private int totalWidth;
	private int totalHeight;
	private int cameraWidth;
	private int cameraHeight;
	private ScaleGestureDetector zoomGestureDector;
	private GestureDetector gestureDetector;
	private Scene mainScene;
	private MenuScene mMenuScene;
	private Sprite backgroundSprite;
	private Sprite mapPicSprite;
	private ZoomControl zoomControl;
	private ModeControl modeControl;
	private int mMode;
	private HUD hud;
	private Text mMapText;
	private Text mClockText;
	private Text mBatteryText;
	private Text mHintText;
	private Sound mLongPressedSound;
  private ScreenAdvertisement mAdvertisement;

	private Sprite mapADSprite;
		//private TabHost mTabHost;

	private Bundle bundle;

	private GraphicIndoorMapListener graphicListener;
	private IndoorMapLoader indoorMapLoader;

	private RuntimeIndoorMap runtimeIndoorMap;
	private int currentCollectingX = -1;
	private int currentCollectingY = -1;

	private boolean collectingOnGoing = false;
	private static final int MENU_ITEM_BACK = Menu.FIRST;
	private static final int MENU_ITEM_INFO = Menu.FIRST + 1;
	private static final int MENU_ITEM_CONFIG = Menu.FIRST + 2;

	private static final int MENU_ITEM_EXIT = Menu.FIRST + 3;
	private Thread mPeriodicLocateMeThread;
	private boolean periodicLocateMeOn;

	private boolean periodicLoacting;
	private ProgressDialog mProgressDialog;

	private Toast infoQueryToast;
	private int mNfcEditState;
	private int mTargetColNo;
	
	private int mTargetRowNo;
	private long lastBackTime;
	
	private long lastManualLocateTime;
	private Thread mUpdateClockThread;
	private boolean updateClockOn; 
	
	private int mOrientation;
	
	private ArrayList<Text> mapInfos;
	private NaviInfo naviInfo;
	private int naviMyPlaceX;
	private int naviMyPlaceY;
	private int naviFromNode;
	
	private int naviToNode;
	private int LEFT_SPACE;
	private int RIGHT_SPACE;
	private int TOP_SPACE;
	private int BOTTOM_SPACE;
	private int CONTROL_BUTTON_WIDTH;	
	private int CONTROL_BUTTON_HEIGHT;
	private int CONTROL_BUTTON_MARGIN;
	private int TAB_BUTTON_WIDTH;	
	private int TAB_BUTTON_HEIGHT;
	
	private int TAB_BUTTON_MARGIN;

private float density = 1.5f;

	private AdvertisePeriodThread advertisePeriodThread;

	private void addNfcQrLocation() {
		// Not return when no NFC, since we can support Camera for QR Code
		if (!Util.getNfcInfoManager().isNfcEmbeded()) {
			//Util.showLongToast(this, R.string.no_nfc_embeded);
			updateHintText(R.string.no_nfc_embeded);
		} else {
			if (!Util.getNfcInfoManager().isNfcEnabled()) {
				//Util.showLongToast(this, R.string.no_nfc_enabled);
				updateHintText(R.string.no_nfc_enabled);
			}
		}

		if (mNfcEditState == IndoorMapData.NFC_EDIT_STATE_SCANNING) {
			// Cancel both last one and this one
			mNfcEditState = IndoorMapData.NFC_EDIT_STATE_NULL;
			//Util.showLongToast(this, R.string.last_nfc_scan_ongoing_failure);
			updateHintText(R.string.last_nfc_scan_ongoing_failure);
			return;
		}

		mNfcEditState = IndoorMapData.NFC_EDIT_STATE_SCANNING;
		//Util.showLongToast(this, R.string.start_scan_nfc);
		updateHintText(R.string.start_scan_nfc);
	}

	private void addTextTag(FieldInfo fieldInfo) {
		float pX = fieldInfo.getX() * runtimeIndoorMap.getCellPixel();
		float pY = fieldInfo.getY() * runtimeIndoorMap.getCellPixel();
		
		/* Sometimes it cause problem: include: 字符串显示叠加, 闪屏
		Text text = new TickerText(pX, pY, mFont, textStr, 
				new TickerTextOptions(HorizontalAlign.CENTER, 10),  //出现速度
				getVertexBufferObjectManager());
		
		text.registerEntityModifier(
			new SequenceEntityModifier(
				new ParallelEntityModifier(
					new AlphaModifier(1, 0.7f, 0.7f),  //透明度过渡
					new ScaleModifier(1, 0.6f, 1f)   //缩放过渡
				),
				new RotationModifier(1, 0, 0)          //旋转过渡
			)
		);
		text.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		*/
		
		Text text = new Text(pX,
				pY, 
				mFont_mapinfo, 
				fieldInfo.getInfo(),
				100,
				getVertexBufferObjectManager());
		text.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		text.setAlpha(fieldInfo.getAlpha());
		text.setRotation(fieldInfo.getRotation()); // For future use if we need to rotate a angle
		text.setScale(fieldInfo.getScale()); // For future use if we need to display some label with a bigger/smaller scale
		
		mainScene.attachChild(text);
		
		// Store so we can clear them in future if needed
		if (mapInfos == null) {
			mapInfos = new ArrayList<Text>();
		}
		
		mapInfos.add(text);
	}

	public void checkAndDownloadAd(AdGroup adGroup){ 
		if (advertisePeriodThread.isInit == true)
		{advertisePeriodThread.isInit = false;}
		
		mAdvertisement.setadGroup(adGroup);
		mAdvertisement.checkAndDownloadAds();
		if (advertisePeriodThread.isInit == false)
		{advertisePeriodThread.isInit = true;}
		
	}

	private void collectFingerprint(final boolean silent) {
		if (DEBUG)
			Log.d(TAG, "Start collectFingerprint");

		if (!Util.getWifiInfoManager().isWifiEmbeded()) {
			if (!silent)
				//Util.showLongToast(this, R.string.no_wifi_embeded);
				updateHintText(R.string.no_wifi_embeded);
			return;
		}

		if (!Util.getWifiInfoManager().isWifiEnabled()) {
			if (!silent)
				//Util.showLongToast(this, R.string.no_wifi_enabled);
				updateHintText(R.string.no_wifi_enabled);
			return;
		}
		
		if (!silent)
			//Util.showShortToast(this, R.string.collecting);
			updateHintText(R.string.collecting);
		
		if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER) {
			if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {			
				CollectInfo collect = new CollectInfo();
				Location location = new Location(runtimeIndoorMap.getMapId(), mTargetColNo, mTargetRowNo, runtimeIndoorMap.getVersionCode());
				collect.setLocation(location);
	
				WifiFingerPrint fingnerPrint = Util.getWifiInfoManager().mergeSamples();
				fingnerPrint.log();
				collect.setWifiFingerPrint(fingnerPrint);
	
				try {
					Gson gson = new Gson();
					String json = gson.toJson(collect);
					JSONObject data = new JSONObject(json);
	
					if (Util.sendToServer(this, MsgConstants.MT_COLLECT, data)) {
						// For test purpose, display the [x,y]
						if (!silent) 
							updateHintText(getResources().getString(R.string.collected)
									+ " ["
									+ mTargetColNo
									+ ","
									+ mTargetRowNo + "]");
							/*
							Util.showToast(this,
									getResources().getString(R.string.collected)
											+ " ["
											+ x
											+ ","
											+ y + "]",
									Toast.LENGTH_LONG);
									*/
	
						Log.e("COLLECT", "Send MT_COLLECT to Server: TRUE");
					} else {
						// All errors should be handled in the sendToServer
						// method
					}
	
				} catch (Exception ex) {
					//Util.showToast(this, "102 " + ex.toString(), Toast.LENGTH_LONG);
					ex.printStackTrace();
					updateHintText("COLLECT: 102 ERROR: " + ex.getMessage());
				}
				
				return;
			} // if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
		} // if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER) {
		
		
		// No enough buffered data or buffer not used, use a thread
		
		// To pass the x, y to our new Thread
		if ((currentCollectingX != -1) || (currentCollectingX != -1)) {
			if (!silent)
				//Util.showShortToast(this, R.string.collect_ongoing_failure);
				updateHintText(R.string.collect_ongoing_failure);
			return;
		}

		if (isCollectingOnGoing()) {
			if (!silent)
				updateHintText(R.string.another_collect_ongoing);
				//Util.showShortToast(this, R.string.another_collect_ongoing);
			return;
		}

		setCollectingOnGoing(true);

		currentCollectingX = mTargetColNo;
		currentCollectingY = mTargetRowNo;

		new Thread() {
			public void run() {
				CollectInfo collect = new CollectInfo();
				Location location = new Location(runtimeIndoorMap.getMapId(), currentCollectingX, currentCollectingY, runtimeIndoorMap.getVersionCode());
				collect.setLocation(location);

				WifiFingerPrint fingnerPrint = new WifiFingerPrint(IndoorMapData.REQUEST_COLLECT);
				fingnerPrint.log();

				setCollectingOnGoing(false);

				collect.setWifiFingerPrint(fingnerPrint);

				// JSONObject data = new JSONObject();

				try {
					Gson gson = new Gson();
					String json = gson.toJson(collect);
					JSONObject data = new JSONObject(json);

					if (Util.sendToServer(MapViewerActivity.this, MsgConstants.MT_COLLECT, data)) {
						// For test purpose, display the [x,y]
						if (!silent)
							updateHintText(getResources().getString(R.string.collected)
									+ " ["
									+ currentCollectingX
									+ ","
									+ currentCollectingY + "]");
							/*Util.showToast(MapViewerActivity.this,
									getResources()
											.getString(R.string.collected)
											+ " ["
											+ currentCollectingX
											+ ","
											+ currentCollectingY + "]",
									Toast.LENGTH_LONG);*/
					} else {
						// All errors should be handled in the sendToServer
						// method
					}

				} catch (Exception ex) {
					//Util.showToast(MapViewerActivity.this,"002 " + ex.toString(), Toast.LENGTH_LONG);
					ex.printStackTrace();
					updateHintText("COLLECT: 002 ERROR: " + ex.getMessage());
				}

				// We are ready for next collecting on new position
				currentCollectingX = -1;
				currentCollectingY = -1;

				if (DEBUG)
					Log.d(TAG, "End collectFingerprint Thread");
			}
		}.start();
	}

	protected MenuScene createMenuScene() {
		if (DEBUG)
			Log.d(TAG, "Start createMenuScene");

		final MenuScene menuScene = new MenuScene(this.mCamera);

		final IMenuItem backMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_ITEM_BACK, mFont_menu, getResources()
						.getString(R.string.menu_back),
						getVertexBufferObjectManager()),
				new org.andengine.util.color.Color(1.0f, 0.0f, 0.0f),
				new org.andengine.util.color.Color(0.0f, 0.0f, 0.0f));
		backMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(backMenuItem);

		final IMenuItem infoMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_ITEM_INFO, mFont_menu, getResources()
						.getString(R.string.menu_info),
						getVertexBufferObjectManager()),
				new org.andengine.util.color.Color(1.0f, 0.0f, 0.0f),
				new org.andengine.util.color.Color(0.0f, 0.0f, 0.0f));
		backMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(infoMenuItem);

		final IMenuItem configMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_ITEM_CONFIG, mFont_menu, getResources()
						.getString(R.string.menu_config),
						getVertexBufferObjectManager()),
				new org.andengine.util.color.Color(1.0f, 0.0f, 0.0f),
				new org.andengine.util.color.Color(0.0f, 0.0f, 0.0f));
		configMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(configMenuItem);

		final IMenuItem exitMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_ITEM_EXIT, mFont_menu, getResources()
						.getString(R.string.menu_exit),
						getVertexBufferObjectManager()),
				new org.andengine.util.color.Color(1.0f, 0.0f, 0.0f),
				new org.andengine.util.color.Color(0.0f, 0.0f, 0.0f));
		exitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(exitMenuItem);

		menuScene.buildAnimations();

		menuScene.setBackgroundEnabled(false);

		menuScene.setOnMenuItemClickListener(new IOnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClicked(final MenuScene pMenuScene,
					final IMenuItem pMenuItem, final float pMenuItemLocalX,
					final float pMenuItemLocalY) {
				switch (pMenuItem.getID()) {
				case MENU_ITEM_BACK:
					// Remove the menu
					mainScene.clearChildScene();
					return true;
				case MENU_ITEM_INFO:
					// Show all available Information
					showInfo();
					return true;
				case MENU_ITEM_CONFIG:
					Intent openConfigIntent = new Intent(MapViewerActivity.this, TunerActivity.class);
					startActivity(openConfigIntent);
					return true;
				case MENU_ITEM_EXIT:
					// End Activity.
					exitApp();
					return true;
				default:
					return false;
				}
			}
		});

		if (DEBUG)
			Log.d(TAG, "End createMenuScene");

		return menuScene;
	}

	private void decideNextActionAfterLongPress() {
		if ((mTargetColNo==-1) || (mTargetRowNo==-1)) {
			//Util.showLongToast(this, R.string.need_a_selected_location);
			updateHintText(R.string.need_a_selected_location);
			return;
		}
		
		runOnUiThread(new Runnable() {
			  public void run() {
				  int messageId = R.string.confirm;
					
					switch (mMode)  {
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
					default:
					}	
					
				    AlertDialog.Builder builder = new AlertDialog.Builder(MapViewerActivity.this);
					
					builder.setIcon(R.drawable.ic_launcher);
					builder.setTitle(getResources().getString(R.string.confirm) + " [" + mTargetColNo + "," + mTargetRowNo + "]");
					builder.setMessage(messageId);
					builder.setPositiveButton(R.string.yes, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (mMode)  {
							case IndoorMapData.MAP_MODE_VIEW:
								setCurrentLocation();
								break;
							case IndoorMapData.MAP_MODE_EDIT:
								collectFingerprint(false); // x, y
								break;
							case IndoorMapData.MAP_MODE_EDIT_TAG:
								addNfcQrLocation();
								break;
							case IndoorMapData.MAP_MODE_DELETE_FINGERPRINT:
								deleteFingerprint();
								break;
							case IndoorMapData.MAP_MODE_TEST_LOCATE:
								testLocate();
								break;
							case IndoorMapData.MAP_MODE_TEST_COLLECT:
								testCollect();
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

	private void deleteFingerprint() {
		Location location = new Location(runtimeIndoorMap.getMapId(), mTargetColNo, mTargetRowNo, runtimeIndoorMap.getVersionCode());
		
		try {
			Gson gson = new Gson();
			String json = gson.toJson(location);
			JSONObject data = new JSONObject(json);

			if (Util.sendToServer(this, MsgConstants.MT_DELETE_FINGERPRINT, data)) {
				//Util.showShortToast(this, R.string.delete_fingerprint_at_this_location);
				updateHintText(getResources().getString(R.string.delete_fingerprint_at_this_location) + " @[" + mTargetColNo + "," + mTargetRowNo + "]");
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			//Util.showToast(MapViewerActivity.this, "DEL_FP01 " + ex.toString(), Toast.LENGTH_LONG);
			ex.printStackTrace();
			updateHintText("DEL_FP01 ERROR: " + ex.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private void dismissProgressDialog() {
		try {
			if (mProgressDialog != null)
				if (mProgressDialog.isShowing())
					mProgressDialog.dismiss();
		} catch (IllegalArgumentException e) {
			// We don't mind. android cleared it for us.
		}
	}
	
	private void downloadMapInfo(int mapId) {
		VersionOrMapIdRequest id = new VersionOrMapIdRequest();
		id.setCode(mapId);

		try {
			
			Gson gson = new Gson();
			String json = gson.toJson(id);
			JSONObject data = new JSONObject(json);

			if (Util.sendToServer(this, MsgConstants.MT_MAP_INFO_QUERY, data)) {
				
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			Util.showToast(this, "GET MAP INFO ERROR: " + ex.getMessage(), Toast.LENGTH_LONG);
			ex.printStackTrace();
		}
	}
	
	private void downloadNaviInfo(int mapId) {
		VersionOrMapIdRequest id = new VersionOrMapIdRequest();
		id.setCode(mapId);

		try {
			
			Gson gson = new Gson();
			String json = gson.toJson(id);
			JSONObject data = new JSONObject(json);

			if (Util.sendToServer(this, MsgConstants.MT_NAVI_INFO_QUERY, data)) {
				
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			Util.showToast(this, "GET NAVI INFO ERROR: " + ex.getMessage(), Toast.LENGTH_LONG);
			ex.printStackTrace();
		}
	}
	
	// send the NFC tagId + [MapID,X,Y] to server, so the Fine Location against
	// this NFC can be stored/updated
	private void editNfcQrTagInMap(String tagId) {
		// send Nfc/Qr Locate messsage to server
		NfcLocation nfcLoc = new NfcLocation(tagId,
				runtimeIndoorMap.getMapId(), mTargetColNo, mTargetRowNo, runtimeIndoorMap.getVersionCode());

		//Util.showShortToast(this, R.string.store_nfc_info_into_map);
		updateHintText(R.string.store_nfc_info_into_map);

		try {
			Gson gson = new Gson();
			String json = gson.toJson(nfcLoc);
			JSONObject data = new JSONObject(json);

			if (Util.sendToServer(this, MsgConstants.MT_EDIT_NFC_QR, data)) {
				//Util.showShortToast(this, R.string.nfc_info_stored);
				updateHintText(R.string.nfc_info_stored);
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			//Util.showToast(MapViewerActivity.this, "NFC02 " + ex.toString(), Toast.LENGTH_LONG);
			ex.printStackTrace();
			updateHintText("NFC: 002 ERROR: " + ex.getMessage());
		}
	}

	/*
	 * public void onBackPressed() { // Do nothing }
	 */

	private void exitApp() {
		finish();
	}

	@Override
	protected int getLayoutID() {
		return R.layout.map_viewer;
	}



	public ZoomCamera getMCamera() {
		return mCamera;
	}
	
	public int getMode() {
		return mMode;
	}

	private int getNaviNodeIdFromSpinnerId (int spinnerId) {
		if ((naviInfo == null) || (naviInfo.getNodes() == null)) {
			return -1;
		}
		
		if (spinnerId == 0) { // My Place
			return getNearestNaviNode();
		}
		
		if (spinnerId > naviInfo.getNodes().size()) {
			return -1;
		}
		
		NaviNode node = naviInfo.getNodes().get(spinnerId-1);
		if (node == null) {
			return -1;
		}
		return node.getId();
	}
	
	private int getNearestNaviNode() {
		if ((naviMyPlaceX == -1) || (naviMyPlaceY == -1)) {
			return -1;
		}
		
		if ((naviInfo == null) || (naviInfo.getNodes() == null)) {
			return -1;
		}
		
		int nodeNo = -1;
		int delta = Integer.MAX_VALUE;
		for (NaviNode node: naviInfo.getNodes()) {
			if (node != null) {
				if (node.getMapId() == runtimeIndoorMap.getMapId()) { // Same Map
					int delta2 = Math.abs(naviMyPlaceX - node.getX()) + Math.abs(naviMyPlaceY - node.getY());
					
					if (delta2 < delta) {
						delta = delta2;
						nodeNo = node.getId();
					}
					
					if (delta == 0) {
						return nodeNo;
					}
				}
			}
		}
		
		return nodeNo;
	}
	
	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.map_rendersurfaceview;
	}

	// TODO: how to display the navi. result?
	private void goNavigator() {
		String naviStr = "";
		
		if (((naviMyPlaceX == -1) || (naviMyPlaceY == -1)) 
			&& ((naviFromNode == 0) || (naviToNode == 0))) {
			naviStr = getResources().getString(R.string.navi_my_place_unknown);
			navigator(naviStr);
			return;
		}
		
		int fromNode = getNaviNodeIdFromSpinnerId(naviFromNode);
		int toNode = getNaviNodeIdFromSpinnerId(naviToNode);

		if ((fromNode == -1) || (toNode == -1)) {
			naviStr = getResources().getString(R.string.navi_failed_no_data);
			navigator(naviStr);
			return;
		} 
		
		if ((naviInfo == null) || (naviInfo.getNodes() == null) || (naviInfo.getPaths() == null)) {
			naviStr = getResources().getString(R.string.navi_failed_no_data);
			navigator(naviStr);
			return;
		}
		
		NaviPath bestRoute = NaviUtil.getBestNaviPath(naviInfo.getPaths(), fromNode, toNode);
		
		if (bestRoute == null) {
			naviStr = getResources().getString(R.string.navi_failed_no_route);
			navigator(naviStr);
			return;
		}
		
		naviStr += getResources().getString(R.string.navi_total_distance) + bestRoute.getDist() + getResources().getString(R.string.navi_meter);
		naviStr += "\n";

		naviStr += "\n";
		if (naviFromNode == 0) {
			naviStr += getResources().getString(R.string.navi_my_place) + " >> ";
		}
		
		naviStr += NaviUtil.getNodeName(naviInfo.getNodes(), fromNode);
		int from = fromNode;

		for (int i=0; i<bestRoute.getStepSize(); i++) {
			int to = bestRoute.getSteps()[i];
			naviStr += "\n" + NaviUtil.getPathDescription(naviInfo.getPaths(), from, to, getResources().getString(R.string.navi_meter));
			naviStr += "\n";
			naviStr += "\n" + NaviUtil.getNodeName(naviInfo.getNodes(), to);
			from = to;
		}
		
		if (naviToNode == 0) {
			naviStr += " >> " + getResources().getString(R.string.navi_my_place);
		}
		
		naviStr += "\n" + getResources().getString(R.string.navi_over);
		
		navigator(naviStr);
	}

	public void handleLongPress(MotionEvent e) {
		float zoomFactor = mCamera.getZoomFactor();
		float centerX = mCamera.getCenterX();
		float centerY = mCamera.getCenterY();
		float width = mCamera.getWidth();
		float height = mCamera.getHeight();
		float x = e.getX() / zoomFactor + centerX - width / 2;
		float y = e.getY() / zoomFactor + centerY - height / 2;

		// Out of Lower Bound
		if ((x < LEFT_SPACE)
				|| (y < TOP_SPACE)) {
			//Util.showShortToast(this, R.string.out_of_map_bound);
			updateHintText(R.string.out_of_map_bound);
			return;
		}

		int colNo = (int) ((x - LEFT_SPACE) / runtimeIndoorMap.getCellPixel());
		int rowNo = (int) ((y - TOP_SPACE) / runtimeIndoorMap.getCellPixel());

		// Out of Upper Bound
		if ((colNo >= runtimeIndoorMap.getColNum())
				|| (rowNo >= runtimeIndoorMap.getRowNum())) {
			//Util.showShortToast(this, R.string.out_of_map_bound);
			updateHintText(R.string.out_of_map_bound);
			return;
		}

		// Vibrate
		if (Util.getVibrator() != null) {			
			// Nexus7 does not has a Vibrator but it get into our codes, let it play sounds
			if (Util.getDeviceName().trim().equalsIgnoreCase("Nexus 7")) {
				// Play sound, repeat 1 time
				mLongPressedSound.setLoopCount(1);
				mLongPressedSound.play();
			} else {
				Util.getVibrator().vibrate(500);
			}
		} else {
			// Play sound, repeat 1 time
			mLongPressedSound.setLoopCount(1);
			mLongPressedSound.play();
		}
		
		// Put a flag on the chosen cell
		graphicListener.locate(runtimeIndoorMap, colNo, rowNo, Constants.TARGET_USER, 0);
		updateHintText(getResources().getString(R.string.current_selected_location) + " @[" + colNo + "," + rowNo + "]");
		
		// Set for next Action
		mTargetColNo = colNo;
		mTargetRowNo = rowNo;
	}

	public void handleTestReply(TestLocateCollectReply testLocation) {
		updateLocation(testLocation);
	}

	private boolean handleTouchEvent(MotionEvent event) {

		if (gestureDetector.onTouchEvent(event)) {
			//Log.e("Touch", "gestureDetector.onTouchEvent");
			return true;
		}

		if (zoomGestureDector.onTouchEvent(event)) {
			//Log.e("Touch", "zoomGestureDector.onTouchEvent");
			return true;
		}

		return false;
	}

	private void hideAd() {
		//mAdvertisement.hideAdvertisement();
	}

	private void infoMe(int colNo, int rowNo) {
		InfoQueryRequest infoQueryReq = new InfoQueryRequest();
		ArrayList<Location> locations = new ArrayList<Location>();

		if (runtimeIndoorMap.isRefreshInfoNeeded()) {
			locations.add(new Location(runtimeIndoorMap.getMapId(), -1, -1, runtimeIndoorMap.getVersionCode()));
		}

		if ((colNo != -1) && (rowNo != -1)) {
			int areaSize = (7 - 1) / 2;

			int fromCol = Math.max(0, colNo - areaSize);
			int fromRow = Math.max(0, rowNo - areaSize);
			int toCol = Math.min(colNo + areaSize,
					runtimeIndoorMap.getColNum() - 1);
			int toRow = Math.min(rowNo + areaSize,
					runtimeIndoorMap.getRowNum() - 1);

			for (int col = fromCol; col <= toCol; col++) {
				for (int row = fromRow; row <= toRow; row++) {
					Cell cell = runtimeIndoorMap.getCellAt(row, col); // y, x:
																		// Cells
																		// has
																		// the
																		// revert
																		// x, y
																		// as
																		// colNo,
																		// rowNo
					if (cell != null) {
						if (cell.isRefreshInfoNeeded()) {
							locations.add(new Location(runtimeIndoorMap
									.getMapId(), col, row, runtimeIndoorMap.getVersionCode()));
						}
					}
				}
			}
		}

		if (!locations.isEmpty()) {
			infoQueryReq.setLocations(locations);

			try {
				Gson gson = new Gson();
				String json = gson.toJson(infoQueryReq);
				JSONObject data = new JSONObject(json);

				if (Util.sendToServer(this, MsgConstants.MT_INFO_QUERY, data)) {
					
				} else {
					// All errors should be handled in the sendToServer
					// method
				}
			} catch (Exception ex) {
				//Util.showToast(this, "004 " + ex.toString(), Toast.LENGTH_LONG);
				ex.printStackTrace();
				updateHintText("PUSH_INFO: 004 ERROR: " + ex.getMessage());
			}
		}
	}

	private void initailHUDBatteryBar() {
		mBatteryText = new Text(cameraWidth - density * 50,
				0, 
				mFont_hints, 
				"---%",
				4,
				getVertexBufferObjectManager());
		mBatteryText.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		mBatteryText.setAlpha(VisualParameters.MAP_FONT_ALPHA);

		hud.attachChild(mBatteryText);
	}
	
	@SuppressLint("SimpleDateFormat")
	private void initailHUDClockBar() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		String clockStr = sdf.format(new Date(System.currentTimeMillis()));

		mClockText = new Text(cameraWidth - density * 150,
				0, 
				mFont_hints, 
				clockStr,
				8,
				getVertexBufferObjectManager());
		mClockText.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		mClockText.setAlpha(VisualParameters.MAP_FONT_ALPHA);

		hud.attachChild(mClockText);
	}
	
	@SuppressLint("SimpleDateFormat")
	private void initailHUDHintBar() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		String hintStr = sdf.format(new Date(System.currentTimeMillis())) + " " + getResources().getString(R.string.load_complete);
		
		// There is a bug that the future text can not be longer than the start one and that's why I appends some spaces here
		hintStr += "                                                            "
				+ "                                 ";
		
		mHintText = new Text(0,
				mFont_hints.getLineHeight(),   // Flexible according to the height of 1st line text
				mFont_hints, 
				hintStr,
				100,
				getVertexBufferObjectManager());
		mHintText.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		mHintText.setAlpha(VisualParameters.MAP_FONT_ALPHA);

		hud.attachChild(mHintText);
	}

	private void initailHUDMapShowBar() {
		String modeStr = getResources().getString(R.string.view_mode);
		StringBuilder builder = new StringBuilder();
		
		builder.append(getResources().getString(R.string.map))
		.append(runtimeIndoorMap.getMapName()).append(" - ").append(modeStr); 
		
		// There is a bug that the future text can not be longer than the start one and that's why I appends some spaces here
		builder.append("                              ");

		mMapText = new Text(0,
				0, 
				mFont_hints, 
				builder.toString(),
				100,
				getVertexBufferObjectManager());
		mMapText.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		mMapText.setAlpha(VisualParameters.MAP_FONT_ALPHA);

		hud.attachChild(mMapText);
	}

	private void initCamera() {
		// Get the display
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		
		mOrientation = getResources().getConfiguration().orientation;
		
		display.getMetrics(outMetrics);
		
		cameraWidth = outMetrics.widthPixels;
		cameraHeight = outMetrics.heightPixels;
		
		density = Math.min(cameraWidth, cameraHeight) / 480;
		
		// Left white but not black
		/*cameraHeight -= VisualParameters.BOTTOM_SPACE_FOR_TABHOST_BAR;
		
		if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
			cameraHeight -= VisualParameters.BOTTOM_SPACE_FOR_ADS_PORTRAIT;
		} else if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			cameraWidth -= VisualParameters.RIGHT_SPACE_FOR_ADS_LANDSCAPE;
		}*/
		
		int CONTROL_BUTTON_NUMBER = Library.CONTROL_BUTTON_NUMBER;
		int TAB_BUTTON_NUMBER = Library.TAB_BUTTON_NUMBER;
		
		CONTROL_BUTTON_WIDTH = 30;
		CONTROL_BUTTON_MARGIN = 10;
		
		// Ensure the ICON is not too small on large screen
		int MIN_VALUE = Math.max(60, Math.round(Math.min(cameraWidth, cameraHeight)/10));

		TAB_BUTTON_WIDTH = TAB_BUTTON_HEIGHT
				= Math.min(MIN_VALUE, Math.round(cameraWidth / TAB_BUTTON_NUMBER / 1.5f));
		TAB_BUTTON_MARGIN = Math.round ((cameraWidth - TAB_BUTTON_WIDTH * TAB_BUTTON_NUMBER) / TAB_BUTTON_NUMBER / 2);  // Here use 2 to let the TAB fill the whole width
		
		TOP_SPACE = 0;
		BOTTOM_SPACE = 0;
		LEFT_SPACE = 0;
		RIGHT_SPACE = 0;
		

		if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {			
			BOTTOM_SPACE += VisualParameters.BOTTOM_SPACE_FOR_ADS_PORTRAIT;
			CONTROL_BUTTON_WIDTH = CONTROL_BUTTON_HEIGHT 
					= Math.min(MIN_VALUE, Math.round((cameraHeight - TAB_BUTTON_HEIGHT - VisualParameters.BOTTOM_SPACE_FOR_ADS_PORTRAIT) / CONTROL_BUTTON_NUMBER / 1.5f));
			CONTROL_BUTTON_MARGIN = Math.min(MIN_VALUE, Math.round ((cameraHeight - TAB_BUTTON_HEIGHT - VisualParameters.BOTTOM_SPACE_FOR_ADS_PORTRAIT - CONTROL_BUTTON_HEIGHT * CONTROL_BUTTON_NUMBER) / CONTROL_BUTTON_NUMBER / 3f)); // Here use 3f to let the control tab layout on the top of height
		} else if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			RIGHT_SPACE += VisualParameters.RIGHT_SPACE_FOR_ADS_LANDSCAPE;
			CONTROL_BUTTON_WIDTH = CONTROL_BUTTON_HEIGHT 
					= Math.min(MIN_VALUE, Math.round((cameraHeight - TAB_BUTTON_HEIGHT) / CONTROL_BUTTON_NUMBER / 1.5f));
			CONTROL_BUTTON_MARGIN = Math.min(MIN_VALUE, Math.round ((cameraHeight - TAB_BUTTON_HEIGHT - CONTROL_BUTTON_HEIGHT * CONTROL_BUTTON_NUMBER) / CONTROL_BUTTON_NUMBER / 3f));// Here use 3f to let the control tab layout on the top of height
		}	
		
		BOTTOM_SPACE += TAB_BUTTON_HEIGHT;
		RIGHT_SPACE += CONTROL_BUTTON_WIDTH;
		
		/* Not work
		// Title Bar + Status Bar
		int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();	
		
		// Status Bar
		Rect frame = new Rect();    
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);    
        int statusBarHeight = frame.top; 
        
        // Title Bar
        int titleBarHeight = contentTop - statusBarHeight;
        */

		//Log.e("Camera", cameraWidth+","+cameraHeight);

		mCamera = new ZoomCamera(0, 0, cameraWidth, cameraHeight);
	}

	@SuppressLint("ShowToast")
	private void initData() {
		if (DEBUG)
			Log.e(TAG, "Start initialData");

		backgroundSprite = null;
		mapPicSprite = null;		

		currentCollectingX = -1;
		currentCollectingY = -1;
		setCollectingOnGoing(false);
		mMode = IndoorMapData.MAP_MODE_VIEW;
		mNfcEditState = IndoorMapData.NFC_EDIT_STATE_NULL;
		mTargetColNo = -1;
		mTargetRowNo = -1;
		periodicLocateMeOn = true;
		periodicLoacting = false;
		
		naviMyPlaceX = -1;
		naviMyPlaceY = -1;
		naviFromNode = -1;
		naviToNode = -1;
		
		updateClockOn = true;
		
		lastManualLocateTime = System.currentTimeMillis();
		lastBackTime = lastManualLocateTime - 6000;

		infoQueryToast = Toast.makeText(this,
				getResources().getString(R.string.no_latest_info),
				Toast.LENGTH_LONG);
		infoQueryToast.setMargin(0, 0);
		infoQueryToast.setGravity(Gravity.TOP, 0, 0);

		if (DEBUG)
			Log.e(TAG, "End initialData");
	}
	
	private void initialHUDMenuBar() {

		if (DEBUG)
			Log.d(TAG, "Start initialHUDMenuBar");

		int x = cameraWidth - CONTROL_BUTTON_WIDTH;
		int y = CONTROL_BUTTON_MARGIN;
		/* Hoare
		y += CONTROL_BUTTON_HEIGHT + CONTROL_BUTTON_MARGIN * 2;
		Library.MENU_ZOOM.load(this, CONTROL_BUTTON_WIDTH, CONTROL_BUTTON_HEIGHT);
		putHUDControlUnit(Library.MENU_ZOOM, x, y, new SpriteListener() {

			private boolean zoomMostIn = true;

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {

				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					if (zoomMostIn) {
						zoomControl.zoomMostOut();
						zoomMostIn = false;
						sprite.setCurrentTileIndex(1);
					} else {
						zoomControl.zoomMostIn();
						zoomMostIn = true;
						sprite.setCurrentTileIndex(0);

					}
				}

				return true;
			}
		});
		 Hoare */ 
		// the planning menu will only be displayed when the planning mode is open, otheriwse hide the 2 button.
		if (WifiIpsSettings.PLANNING_MODE) {
			
			y += CONTROL_BUTTON_HEIGHT + CONTROL_BUTTON_MARGIN * 16;
			Library.MENU_MODE.load(this, CONTROL_BUTTON_WIDTH, CONTROL_BUTTON_HEIGHT);
			putHUDControlUnit(Library.MENU_MODE, x, y, new SpriteListener() {
	
				@Override
				public boolean onAreaTouched(AnimatedSprite sprite,
						TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
						float pTouchAreaLocalY) {
	
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						mMode++; // Put this line inner this check or it will cause
									// big problem
						if (mMode == IndoorMapData.MAP_MODE_MAX) {
							mMode = 0;
						}
						
						switch (mMode) {
							case IndoorMapData.MAP_MODE_VIEW:
								//Hoare
								//showAd(false);
								break;
							default:
								hideAd();
						}
						
						mTargetColNo = -1;
						mTargetRowNo = -1;
						mainScene.getChildByIndex(Constants.LAYER_USER).detachChild(runtimeIndoorMap.getTarget().getSprite());
	
						modeControl.changeMode(sprite, mMode);
					}
	
					return true;
				}
			});
	
			y += CONTROL_BUTTON_HEIGHT + CONTROL_BUTTON_MARGIN * 2;
			Library.MENU_ACTION.load(this, CONTROL_BUTTON_WIDTH, CONTROL_BUTTON_HEIGHT);
			putHUDControlUnit(Library.MENU_ACTION, x, y, new SpriteListener() {
	
				@Override
				public boolean onAreaTouched(AnimatedSprite sprite,
						TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
						float pTouchAreaLocalY) {
	
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						decideNextActionAfterLongPress();
					}
	
					return true;
				}
			});
		} // Planning mode
		
		if (DEBUG)
			Log.d(TAG, "End initialHUDMenuBar");

	}

	private void initialHUDTabBar() {
		int x = TAB_BUTTON_MARGIN;
		int y = cameraHeight - TAB_BUTTON_HEIGHT;
		
		Library.MENU_SCAN_QR.load(this, CONTROL_BUTTON_WIDTH, CONTROL_BUTTON_HEIGHT);
		putHUDControlUnit(Library.MENU_SCAN_QR, x, y, new SpriteListener() {

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {

				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					Intent openCameraIntent = new Intent(MapViewerActivity.this, QrScannerActivity.class);
					startActivityForResult(openCameraIntent, 0);
				}

				return true;
			}
		});
		
		
		x += TAB_BUTTON_WIDTH + TAB_BUTTON_MARGIN * 2;
		
		Library.MENU_NAVI.load(this, TAB_BUTTON_WIDTH, TAB_BUTTON_HEIGHT);
		putHUDControlUnit(Library.MENU_NAVI, x, y, new SpriteListener() {

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					showNaviBar();
				}
				
				return true;
			}
		});

		x += TAB_BUTTON_WIDTH + TAB_BUTTON_MARGIN * 2;
			 
		Library.MENU_LOCATE.load(this, TAB_BUTTON_WIDTH, TAB_BUTTON_HEIGHT);
		putHUDControlUnit(Library.MENU_LOCATE, x, y, new SpriteListener() {

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					lastManualLocateTime = System.currentTimeMillis();
					locateMe(false);
				}
				
				return true;
			}
		});	
		
		// Hoare: floor switch menu for quick switch floors
		x += TAB_BUTTON_WIDTH + TAB_BUTTON_MARGIN * 2;
		Library.TAB_SWITCH.load(this, TAB_BUTTON_WIDTH, TAB_BUTTON_HEIGHT);
		putHUDControlUnit(Library.TAB_SWITCH, x, y, new SpriteListener() {

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
	        		Intent intent_map_selector = new Intent(MapViewerActivity.this, MapSelectorActivity.class);
	        		Bundle mBundle = new Bundle(); 
					mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR);
					intent_map_selector.putExtras(mBundle); 
	        		startActivity(intent_map_selector);	
				}
				
				return true;
			}
		});

		x += TAB_BUTTON_WIDTH + TAB_BUTTON_MARGIN * 2;
		Library.TAB_MSG.load(this, TAB_BUTTON_WIDTH, TAB_BUTTON_HEIGHT);
		putHUDControlUnit(Library.TAB_MSG, x, y, new SpriteListener() {

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {

				Intent intent_pusher = new Intent(MapViewerActivity.this, InfoPusherActivity.class); 
				Bundle mBundle = new Bundle(); 
				String info1 = runtimeIndoorMap.informationsToString();
				String info2 = runtimeIndoorMap.informationsToStringForLocations();
				
				mBundle.putString(IndoorMapData.BUNDLE_KEY_MAP_INFO, info1);
				mBundle.putString(IndoorMapData.BUNDLE_KEY_LOCATION_INFO, info2);
				intent_pusher.putExtras(mBundle); 
				startActivity(intent_pusher);

				return true;
			}
		});
	}
	
	public boolean isCollectingOnGoing() {
		return collectingOnGoing;
	}

	private void loadMapInfo() {
		MapInfo mapInfo = new MapInfo();
		boolean updateNeeded = false;

		try {
			InputStream map_file_is = new FileInputStream(Util.getMapInfoFilePathName(""+runtimeIndoorMap.getMapId()));
			
			mapInfo.fromXML(map_file_is);
			// file has already been closed
			//map_file_is.close();
			
			// For Files in SD Card but not
			//load_map_rc = designMap.fromXML(IndoorMapData.map_file_path + map_file_name);
			
			if (mapInfo.getVersionCode() != runtimeIndoorMap.getVersionCode()) {
				updateNeeded = true;
			}
		} catch (Exception e) {
			updateNeeded = true;
		}
		
		if (updateNeeded) {
			downloadMapInfo(runtimeIndoorMap.getMapId());
			return;
		}
		
		showMapInfo(mapInfo, false);
	}

	private void loadNaviInfo() {
		naviInfo = new NaviInfo();
		boolean updateNeeded = false;

		try {
			InputStream map_file_is = new FileInputStream(Util.getNaviInfoFilePathName(""+runtimeIndoorMap.getMapId()));
			
			naviInfo.fromXML(map_file_is);
			// file has already been closed
			//map_file_is.close();
			
			// For Files in SD Card but not
			//load_map_rc = designMap.fromXML(IndoorMapData.map_file_path + map_file_name);
			
			if (naviInfo.getVersionCode() != runtimeIndoorMap.getVersionCode()) {
				updateNeeded = true;
			}
		} catch (Exception e) {
			updateNeeded = true;
		}
		
		if (updateNeeded) {
			downloadNaviInfo(runtimeIndoorMap.getMapId());
		}	
	}
	
	private void locateMe(boolean periodic) {

		if (DEBUG)
			Log.d(TAG, "Start LocateMe");

		// Not run periodic locating when on Edit Mode 
		if (periodic && (mMode != IndoorMapData.MAP_MODE_VIEW)) {
			return;
		}

		if (periodic && isCollectingOnGoing()) {
			return;
		}

		if (isCollectingOnGoing()) {
			//Util.showShortToast(this, R.string.another_collect_ongoing);
			updateHintText(R.string.another_collect_ongoing);
			return;
		}

		if (!Util.getWifiInfoManager().isWifiEmbeded()) {
			//Util.showLongToast(this, R.string.no_wifi_embeded);
			updateHintText(R.string.no_wifi_embeded);
			return;
		}

		if (!Util.getWifiInfoManager().isWifiEnabled()) {
			//Util.showLongToast(this, R.string.no_wifi_enabled);
			updateHintText(R.string.no_wifi_enabled);
			return;
		}
		
		periodicLoacting = periodic;		
		//Util.showShortToast(this, R.string.locate_collecting);
		updateHintText(R.string.locate_collecting);
		
		if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR) {
			if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
				try {
					WifiFingerPrint fingnerPrint = Util.getWifiInfoManager().mergeSamples();				
					fingnerPrint.log();
					try {
						Gson gson = new Gson();
						String json = gson.toJson(fingnerPrint);
						JSONObject data = new JSONObject(json);
	
						if (Util.sendToServer(this, MsgConstants.MT_LOCATE, data)) {
							//Util.showShortToast(this, R.string.locate_collected);
							updateHintText(R.string.locate_collected);
						} else {
							// All errors should be handled in the sendToServer
							// method
						}
					} catch (Exception ex) {
						//Util.showToast(this, "LOCATE:104 " + ex.toString(), Toast.LENGTH_LONG);
						ex.printStackTrace();
						updateHintText("LOCATE:104 ERROR: " + ex.getMessage());
						finish();
						return;
					}
				} catch (Exception e) {
					//Util.showToast(this, "LOCATE:103 " + e.toString(), Toast.LENGTH_LONG);
					e.printStackTrace();
					updateHintText("LOCATE:103 ERROR: " + e.getMessage());
					finish();
					return;
				}
				
				return;
			} //if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
		} //if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR) {
			
		// No enough buffered data or buffer not used, use a thread
		
		setCollectingOnGoing(true);

		new Thread() {
			public void run() {
				try {
					WifiFingerPrint fingnerPrint = new WifiFingerPrint(IndoorMapData.REQUEST_LOCATE);
					fingnerPrint.log();
					
					setCollectingOnGoing(false);

					try {
						// data.put("req", IndoorMapData.REQUEST_LOCATE);
						// data.put("fingerprint", fingerPrint);
						Gson gson = new Gson();
						String json = gson.toJson(fingnerPrint);
						JSONObject data = new JSONObject(json);

						if (Util.sendToServer(MapViewerActivity.this, MsgConstants.MT_LOCATE, data)) {
							//Util.showShortToast(MapViewerActivity.this, R.string.locate_collected);
							updateHintText(R.string.locate_collected);
						} else {
							// All errors should be handled in the sendToServer
							// method
						}
					} catch (Exception ex) {
						//Util.showToast(MapViewerActivity.this, "004 " + ex.toString(), Toast.LENGTH_LONG);
						updateHintText("LOCATE: 004 ERROR: " + ex.getMessage());
					}
				} catch (Exception e) {
					setCollectingOnGoing(false);
					
					//Util.showToast(MapViewerActivity.this, "003 " + e.toString(), Toast.LENGTH_LONG);
					updateHintText("LOCATE: 003 ERROR: " + e.getMessage());
				}

				if (DEBUG)
					Log.d(TAG, "End LocateMe Thread");
			}
		}.start();
	}

	private void navigator(String naviStr) {
		Intent intent_navigator = new Intent(MapViewerActivity.this, NavigatorActivity.class); 
		Bundle mBundle = new Bundle(); 
		
		mBundle.putString(IndoorMapData.BUNDLE_KEY_NAVI_RESULT, naviStr);
		intent_navigator.putExtras(mBundle); 
		startActivity(intent_navigator);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String tagId = bundle.getString("result");
			
			Log.e("QR", "tagID="+tagId);
			
			if (mMode == IndoorMapData.MAP_MODE_EDIT_TAG) {
				if (mNfcEditState == IndoorMapData.NFC_EDIT_STATE_SCANNING) {
					mNfcEditState = IndoorMapData.NFC_EDIT_STATE_FINISH;
					
					// send the QR Code tagId + [MapID,X,Y] to server, so the Fine
					// Location against this QR Code can be stored/updated
					editNfcQrTagInMap(tagId);
	
					// Collect Fingerprint on this location silently
					collectFingerprint(true); // x, y
				} else {
					//Util.showLongToast(this, R.string.select_position_before_scan_qr);
					updateHintText(R.string.select_position_before_scan_qr);
				}
			} else {
				Util.nfcQrLocateMe(this, tagId); 
			}
		} else {
			// Reset the state
			if (mNfcEditState == IndoorMapData.NFC_EDIT_STATE_SCANNING) {
				mNfcEditState = IndoorMapData.NFC_EDIT_STATE_FINISH;
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - lastBackTime > 5000){
			lastBackTime = System.currentTimeMillis();
			Util.showShortToast(this, R.string.press_back_more);
		} else {
			finish();
			
			System.gc();
			try {
				finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (DEBUG)
			Log.d(TAG, "onConfigurationChanged()");
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		// This should be done before everything since we need some data set in
		// this step to set the width, camera etc.

		initData();

		// Get MapID from the Map Chooser/Locator
		bundle = getIntent().getExtras();
		IndoorMap indoorMap = (IndoorMap) bundle.getSerializable(IndoorMapData.BUNDLE_KEY_MAP_INSTANCE);
		indoorMapLoader = new IndoorMapLoader(this, indoorMap);
		runtimeIndoorMap = indoorMapLoader.getRuntimeIndoorMap();
		Util.setRuntimeIndoorMap(runtimeIndoorMap); // To avoid pass the map in parameter everywhere
		
		// Initialize and Set Camera
		initCamera();
		
		int mapWidth = runtimeIndoorMap.getColNum() * runtimeIndoorMap.getCellPixel();
		int mapHeight = runtimeIndoorMap.getRowNum() * runtimeIndoorMap.getCellPixel();

		totalWidth = mapWidth + LEFT_SPACE + RIGHT_SPACE;
		totalHeight = mapHeight + TOP_SPACE + BOTTOM_SPACE;

		// To align with the Camera
		if (totalWidth < cameraWidth) {
			totalWidth = cameraWidth;
		}

		if (totalHeight < cameraHeight) {
			totalHeight = cameraHeight;
		}

		// Cancel Align with the big size but use the real one
		/*
		float xMulti = 1f * totalWidth / cameraWidth;
		float yMulti = 1f * totalHeight / cameraHeight;

		// Align with the big one but not small one
		if (xMulti > yMulti) {
			totalHeight = (int) (cameraHeight * xMulti);
		} else if (xMulti < yMulti) {
			totalWidth = (int) (cameraWidth * yMulti);
		}
		*/

		//Change to: Calculate the Zoom Out rate that the less scaled width or height can be displayed in the screen.
		float min_zoom_factor = 1f * cameraWidth / totalWidth;
		float height_min_zoom_factor = 1f * cameraHeight / totalHeight;
		if (min_zoom_factor < height_min_zoom_factor) {
			min_zoom_factor = height_min_zoom_factor;
		}		
		zoomControl = new ZoomControl(mCamera, 10.0f, min_zoom_factor);
		/*
		// Calculate the Zoom In rate that the whole map can be displayed in the
		// screen.
		zoomControl = new ZoomControl(mCamera, 1, 1f * cameraWidth / totalWidth);
		*/
		
		// Control the Map Mode
		modeControl = new ModeControl(runtimeIndoorMap);

		mCamera.setBounds(0, 0, totalWidth, totalHeight);
		mCamera.setBoundsEnabled(true);

		// to enable finger scroll of camera
		gestureDetector = new GestureDetector(this,
				new MapCameraViewGestureListener(this));

		// zoom when multi-touchs
		zoomGestureDector = new ScaleGestureDetector(this,
				zoomControl.getScaleGestureListner());

		// FullScreen? Landscape? & Camera?
		ScreenOrientation pScreenOrientation = ScreenOrientation.PORTRAIT_SENSOR;
		if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			pScreenOrientation = ScreenOrientation.LANDSCAPE_SENSOR;
		}
		
		//final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(cameraWidth, cameraHeight), mCamera);
		final EngineOptions engineOptions = new EngineOptions(false, pScreenOrientation, new RatioResolutionPolicy(cameraWidth, cameraHeight), mCamera);

		// Support Music & Sound
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		//engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON); // Keep Screen On,
																     // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// Support MultiTouch
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);

		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {

		FontFactory.setAssetBasePath("font/");

		BitmapTextureAtlas fontTexture_hints = new BitmapTextureAtlas(
				getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		mFont_hints = FontFactory.createFromAsset(
						getFontManager(),
						fontTexture_hints,
						getAssets(),
						"comic.ttf",
						density * VisualParameters.FONT_CHAR_WIDTH_HINTS,
						true, Color.BLUE);
		mFont_hints.load();
		
		BitmapTextureAtlas fontTexture_mapInfo = new BitmapTextureAtlas(
				getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		mFont_mapinfo = FontFactory.createFromAsset(
						getFontManager(),
						fontTexture_mapInfo,
						getAssets(),
						"comic.ttf",
						density * VisualParameters.FONT_CHAR_WIDTH_MAPINFO,
						true, Color.BLACK);
		mFont_mapinfo.load();
		
		BitmapTextureAtlas fontTexture_menu = new BitmapTextureAtlas(
				getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		mFont_menu = FontFactory.createFromAsset(
						getFontManager(),
						fontTexture_menu,
						getAssets(),
						"comic.ttf",
						density * VisualParameters.FONT_CHAR_WIDTH_MENU,
						true, Color.RED);
		mFont_menu.load();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		/* Load all the textures this map needs. */
		// Background
		backgroundSprite = Library.BACKGROUND.load(this, runtimeIndoorMap);
		backgroundSprite.setPosition(LEFT_SPACE, TOP_SPACE);

		// Background - Pic
		mapPicSprite = Library.MAP_PICTURE.load(this, runtimeIndoorMap);
		mapPicSprite.setPosition(LEFT_SPACE, TOP_SPACE);

		SoundFactory.setAssetBasePath("mfx/");
		/* Load all the sounds this map needs. */
		try {
			mLongPressedSound = SoundFactory.createSoundFromAsset(getSoundManager(), this, "cell_collected.ogg");
		} catch (final IOException e) {
			e.printStackTrace();
		}

		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// mEngine.registerUpdateHandler(new FPSLogger()); // FPS Logger

		// Main Scene
		mainScene = new Scene();
		for (int i = 0; i < Constants.LAYER_INDEX; i++) {
			mainScene.attachChild(new Entity());
		}

		Library.initial(mEngine.getTextureManager(), getAssets());
		
		// No background color needed as we have a fullscreen background sprite.
		mainScene.setBackgroundEnabled(true);
		mainScene.setBackground(new Background(255,255,255));
		mainScene.getChildByIndex(Constants.LAYER_BACKGROUND).attachChild(backgroundSprite);

		mainScene.getChildByIndex(Constants.LAYER_MAP).attachChild(mapPicSprite);
		mainScene.registerTouchArea(mapPicSprite);

		mainScene.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				return handleTouchEvent(pSceneTouchEvent.getMotionEvent());
			}

		});

		// Menu
		mMenuScene = createMenuScene();

		// Draw the original in Map units, e.g. user
		indoorMapLoader.initialMap();

		// HUDs
		hud = new HUD();
		mCamera.setHUD(hud);
		initailHUDMapShowBar();
		initailHUDClockBar();
		initailHUDBatteryBar();
		initailHUDHintBar();
		initialHUDMenuBar();
		initialHUDTabBar();

		// Listeners
		graphicListener = new GraphicIndoorMapListener(this, mainScene, mMapText);
		graphicListener.setOffsetX(LEFT_SPACE);
		graphicListener.setOffsetY(TOP_SPACE);
		runtimeIndoorMap.addListener(graphicListener);
		runtimeIndoorMap.addListener(new SoundIndoorMapListener());

		// Initial in map listeners
		runtimeIndoorMap.initial();

		// Set User ID
		runtimeIndoorMap.getUser().setId(Util.getWifiInfoManager().getMyMac());

		// The 1st action
		int req = bundle.getInt(IndoorMapData.BUNDLE_KEY_REQ_FROM);

		// Update Location
		switch (req) {
		case IndoorMapData.BUNDLE_VALUE_REQ_FROM_LOCATOR:
			int colNo = bundle.getInt(IndoorMapData.BUNDLE_KEY_LOCATION_COL);
			int rowNo = bundle.getInt(IndoorMapData.BUNDLE_KEY_LOCATION_ROW);

			updateLocation(runtimeIndoorMap.getMapId(), runtimeIndoorMap.getVersionCode(), colNo, rowNo);

			break;
		case IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR:
			infoMe(-1, -1); // For map-wide Info
			break;
		default:
		}
		
		// Show the Map Info Layer
		loadMapInfo();
		
		// InitData for Navigator
		loadNaviInfo();
		
		//createTabHost();
		
		//mAdvertisement = new ScreenAdvertisement(this, R.id.map_ad);
		
		//here just need to display default advertise. 
		mAdvertisement = new ScreenAdvertisement(this,runtimeIndoorMap);
		mAdvertisement.initAdvertiseData();
		
		try {
			InputStream inputStream = getResources().getAssets().open("default_ad/ericsson_ad0.png");
	        File file = AdUtil.createFileFromInputStream(inputStream,"ericsson_ad0.png");
	        AdData.FILE_DEFAULT_AD=file;
	        if (file.exists()){
	        	// Hoare: disable AD
	        	// showAd(true);     	
	        }
	        inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		pOnCreateSceneCallback.onCreateSceneFinished(mainScene);
	}

	@Override
	protected void onDestroy() {
		Util.setEnergySave(true);
		
		// Disable NFC Foreground Dispatch
		Util.disableNfc(this);
		
		// Disable ACCELEROMETER
	    Util.disableAcclerometer(this);
	    
	    mAdvertisement.deleteAdvertises();
	    
	    Util.cancelToast();

		super.onDestroy();
		
		System.gc();
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {

		if (pKeyCode == KeyEvent.KEYCODE_DPAD_UP
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			zoomControl.zoomIn();
			return true;
		} else if (pKeyCode == KeyEvent.KEYCODE_DPAD_DOWN
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			zoomControl.zoomOut();
			return true;
		} else if (pKeyCode == KeyEvent.KEYCODE_MENU
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (this.mainScene.hasChildScene()) {
				/* Remove the menu and reset it. */
				this.mainScene.back();
			} else {
				/* Attach the menu. */
				this.mainScene.setChildScene(this.mMenuScene, false, true, true);
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		if (DEBUG)
			Log.d(TAG, "onNewIntent()");

		super.onNewIntent(intent);

		String tagId = Util.getNfcInfoManager().getTagId(intent);
		
		if (tagId == null) {
			return;
		}

		if (mMode == IndoorMapData.MAP_MODE_EDIT_TAG) {
			if (mNfcEditState == IndoorMapData.NFC_EDIT_STATE_SCANNING) {
				// read out the NFC chip ID
				// tagId = tagFromIntent.getId().toString();

				mNfcEditState = IndoorMapData.NFC_EDIT_STATE_FINISH;
				//Util.showLongToast(this, R.string.nfc_scan_finished);
				updateHintText(R.string.nfc_scan_finished);

				// send the NFC tagId + [MapID,X,Y] to server, so the Fine
				// Location against this NFC can be stored/updated
				editNfcQrTagInMap(tagId);

				// Collect Fingerprint on this location silently
				collectFingerprint(true); // x, y
			}
		} else {
			Util.nfcQrLocateMe(this, tagId); 
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if (DEBUG) {
			Log.e(TAG, "onPause()");
		}

		Util.setEnergySave(true);
		
		if (mUpdateClockThread != null){
			updateClockOn = false;
			mUpdateClockThread = null;
		}
		
		if (mPeriodicLocateMeThread != null){
			periodicLocateMeOn = false;
			mPeriodicLocateMeThread = null;
		}
		
		if (advertisePeriodThread != null){
			advertisePeriodThread.isRunning = false;
			advertisePeriodThread = null;
		}

		// Disable NFC Foreground Dispatch
		Util.disableNfc(this);
		
		// Enable ACCELEROMETER
		Util.disableAcclerometer(this);
	
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	/*
	private void createTabHost() {
		mTabHost = new TabHost(this);
		mTabHost.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

	    TabWidget tabWidget = new TabWidget(this);
	    tabWidget.setId(android.R.id.tabs);
	    mTabHost.addView(tabWidget, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

	    FrameLayout frameLayout = new FrameLayout(this);
	    frameLayout.setId(android.R.id.tabcontent);
	    frameLayout.setPadding(0, 65, 0, 0);
	    mTabHost.addView(frameLayout, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

	    mTabHost.setup();

	    TabSpec ts1 = mTabHost.newTabSpec("hook");
	    ts1.setIndicator("Hook");
	    ts1.setContent(new TabHost.TabContentFactory(){
	         public View createTabContent(String tag)
	         {

	             return null;
	         }  
	    }); 
	    mTabHost.addTab(ts1);

	    TabSpec ts2 = mTabHost.newTabSpec("chain");
	    ts1.setIndicator("Chain");
	    ts1.setContent(new TabHost.TabContentFactory(){
	         public View createTabContent(String tag)
	         {

	             return null;
	         }  
	    }); 
	    mTabHost.addTab(ts2);

	    TabSpec ts3 = mTabHost.newTabSpec("boots");
	    ts1.setIndicator("Boots");
	    ts1.setContent(new TabHost.TabContentFactory(){
	         public View createTabContent(String tag)
	         {

	             return null;
	         }  
	    }); 
	    mTabHost.addTab(ts3);
	} */

	@Override
	protected void onResume() {
		super.onResume();
		System.gc();

		if (DEBUG)
			Log.e(TAG, "onResume()");
		
		Util.setEnergySave(false);
		periodicLocateMeOn = true;
		updateClockOn = true;

		Log.e("ViewerActivity", "Start IpsMessageHandler");
		
		if (Util.getIpsMessageHandler() != null) {
			Util.getIpsMessageHandler().setActivity(this);
			Util.getIpsMessageHandler().startTransportServiceThread();
		} else {
			Util.initial(this);
		}

		startPeriodicLocateMeThread();
		startUpdateClockThread();
		startPeriodicAdvertiseThread();

		// Enable NFC Foreground Dispatch
		Util.enableNfc(this);
		
		// Enable ACCELEROMETER
		Util.enableAcclerometer(this);
		
		// Listen on Battery
		BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
		            if (mBatteryText != null) {
		            	// 获取当前电量
			            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			            // 获取总电量
			            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
		            	mBatteryText.setText(level * 100 / scale + "%");  
		            }
		        }
			}
			
		}; 
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (Util.isShakeDetected(event)) {
		        locateMe(false);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		// Disable NFC Foreground Dispatch
		Util.disableNfc(this);
		
		// Enable ACCELEROMETER
		Util.disableAcclerometer(this);
		
		Util.cancelToast();
		Util.setEnergySave(true);
	}
	
	@Override
	public void onWindowFocusChanged(boolean pHasWindowFocus) {
		super.onWindowFocusChanged(pHasWindowFocus);

		if (DEBUG)
			Log.d(TAG, "onWindowFocusChanged()");
	}

	private void putAdvertiseUnit(boolean default_ad) {	
		// For double check
		if (mapADSprite != null) {		
			mapADSprite.detachSelf();
		}

		Library.ADVERTISE.setAdPictureName(mAdvertisement.readAdPicName());
		Library.ADVERTISE.setUrls(mAdvertisement.readAdUrl());
		
		AdSpriteListener spriteListener = new AdSpriteListener() {

			@Override
			public boolean onAreaTouched(Sprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {

				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					//get the url and open link. 
					Uri uri = Uri.parse(Library.ADVERTISE.getUrls());  
					Intent it = new Intent(Intent.ACTION_VIEW, uri); 
					startActivity(it);
				}

				return true;
			}
		};
		
		//Hoare: comment ad
		//mapADSprite = Library.ADVERTISE.load(MapViewerActivity.this, runtimeIndoorMap, spriteListener, default_ad);
		
		float adWidth = mapADSprite.getWidth();
		float adHeight = mapADSprite.getHeight();		

		float x = 0;
		float y = 0;
		
		if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Rotate base on the sprite's center point
			x = cameraWidth - 1.5f * CONTROL_BUTTON_WIDTH - adWidth * 0.5f - adHeight * 0.5f;
			y = (cameraHeight - 1.5f * TAB_BUTTON_HEIGHT) * 0.5f - adHeight * 0.5f;	
			mapADSprite.setRotation(90.0f);
		} else {
			if (adWidth < cameraWidth) {
				x = (cameraWidth - 1.5f * CONTROL_BUTTON_WIDTH - adWidth) * 0.5f;
			}
			
			y = cameraHeight - 2.5f * TAB_BUTTON_HEIGHT;
		}
		
		mapADSprite.setPosition(x, y);		

		hud.attachChild(mapADSprite);

		hud.registerTouchArea(mapADSprite);
	}
	
	private void putHUDControlUnit(AnimatedUnit unit, int posX, int posY,
			SpriteListener spriteListener) {

		if (DEBUG)
			Log.d(TAG, "Start putHUDControlUnit");

		AnimatedSprite sprite = unit.load(this, spriteListener);

		sprite.setPosition(posX, posY);
		sprite.setAlpha(VisualParameters.CONTROL_BUTTON_ALPHA);

		hud.attachChild(sprite);

		hud.registerTouchArea(sprite);

		if (DEBUG)
			Log.d(TAG, "End putHUDControlUnit");
	}
	
	private void setCameraCenterTo(int colNo, int rowNo) {
		float x = colNo; 
		float y = rowNo;
					
		if (colNo < runtimeIndoorMap.getColNum()) {
			x += 0.5f;
		}
		
		if (rowNo < runtimeIndoorMap.getRowNum()) {
			y += 0.5f;
		}
		
		float pCenterX = (x * runtimeIndoorMap.getCellPixel() + LEFT_SPACE);
		float pCenterY = (y * runtimeIndoorMap.getCellPixel() + TOP_SPACE);

		mCamera.setCenter(pCenterX, pCenterY);
	}
	
	public void setCollectingOnGoing(boolean collectingOnGoing) {
		this.collectingOnGoing = collectingOnGoing;
	}

	private void setCurrentLocation() {
		updateLocation(runtimeIndoorMap.getMapId(), runtimeIndoorMap.getVersionCode(), mTargetColNo, mTargetRowNo);
	}

	// Handle the reply Message for Navi. Info update
	public void setNaviInfo(NaviInfo naviInfo) {
		if (naviInfo == null) {
			return;
		}
		
		this.naviInfo = naviInfo;
		
		// Store into file
		naviInfo.toXML();
	}
	
	private void showAd(boolean default_ad) {
/*		putAdvertiseUnit(default_ad);	
		
		mAdvertisement.showAdvertisement();*/
	}
	
	private void showInfo() {
		//infoQueryToast.show();
		Intent intent_pusher = new Intent(MapViewerActivity.this, InfoPusherActivity.class); 
		Bundle mBundle = new Bundle(); 
		String info1 = runtimeIndoorMap.informationsToString();
		String info2 = runtimeIndoorMap.informationsToStringForLocations();
		
		mBundle.putString(IndoorMapData.BUNDLE_KEY_MAP_INFO, info1);
		mBundle.putString(IndoorMapData.BUNDLE_KEY_LOCATION_INFO, info2);
		intent_pusher.putExtras(mBundle); 
		startActivity(intent_pusher);
	}
	
	public void showInfo(QueryInfo queryInfo) {
		ArrayList<LocationQueryInfo> infos = queryInfo.getInfos();

		if ((infos == null) || (infos.isEmpty())) {
			return;
		}

		String text = "";

		for (LocationQueryInfo info : infos) {
			Location loc = info.getLocation();
			ArrayList<String> messages = info.getMessages();

			int col = loc.getX();
			int row = loc.getY();

			// Not for this map
			if (loc.getMapId() != runtimeIndoorMap.getMapId()) {
				continue;
			}

			// No info
			if ((messages == null) || (messages.isEmpty())) {
				continue;
			}

			if ((col == -1) || (row == -1)) {
				// For map overall
				if (runtimeIndoorMap.isSameInfo(messages)) {
					continue;
				} else {
					text += runtimeIndoorMap.informationsToString(messages);
					runtimeIndoorMap.setInfo(messages);
				}
			} else {
				// For cell

				// Out of bound
				if ((col > runtimeIndoorMap.getColNum())
						|| (row > runtimeIndoorMap.getRowNum()) || (col < 0)
						|| (row < 0)) {
					continue;
				}

				Cell cell = runtimeIndoorMap.getCellAt(row, col);

				if (cell.isSameInfo(messages)) {
					continue;
				} else {
					text += cell.informationsToString(messages);
					cell.setInfo(messages);
				}
			}
		}

		if (text.equals("")) {
			return;
		}

		infoQueryToast.setText(text);
		infoQueryToast.show();
	}
	
    public void showMapInfo(MapInfo mapInfo, boolean storeNeeded) {
		if (mapInfo == null) {
			return;
		}
		
		// Clear old Map info
		if (mapInfos == null) {
			mapInfos = new ArrayList<Text>();
		} else {
			for (Text text:mapInfos) {
				if (text != null) {
					mainScene.detachChild(text);
				}
			}
			mapInfos.clear();
		}
		
		// Show New Map Info
		ArrayList<FieldInfo> fieldInfos = mapInfo.getFields();
		
		if (fieldInfos == null) {
			return;
		}
		
		for (FieldInfo fieldInfo : fieldInfos) {
			if (fieldInfo != null) {
				addTextTag(fieldInfo);
			}
		}
		
		// Store in File, put it here so the info may be re-encoded above in future.
		if (storeNeeded) {
			mapInfo.toXML();
		}
	}


	private void showNaviBar() {	
		runOnUiThread(new Runnable() {
			  public void run() {
				    
				    AlertDialog.Builder builder = new AlertDialog.Builder(MapViewerActivity.this);
					
				    builder.setIcon(R.drawable.ic_launcher);
				    builder.setTitle(R.string.navi_from_to);
					
					Spinner sipnnerFrom;
					Spinner sipnnerTo;
					ArrayAdapter<String> adapter;
					String[] nodeNames;
					
					if ((naviInfo == null) || (naviInfo.getNodes() == null) || (naviInfo.getNodes().isEmpty())) {
						builder.setMessage(R.string.navi_no_node);
					} else {
						LayoutInflater inflater = getLayoutInflater();
						View layout = inflater.inflate(R.layout.navigator_input, (ViewGroup) findViewById(R.id.navi));
						builder.setView(layout);
						
						sipnnerFrom =  (Spinner) layout.findViewById(R.id.from_list);
						sipnnerTo =  (Spinner) layout.findViewById(R.id.to_list);
						
						nodeNames = new String[naviInfo.getNodes().size()+1]; // Include 'My Place'
						nodeNames[0] = MapViewerActivity.this.getResources().getString(R.string.navi_my_place);
						int idx = 1;
						for (NaviNode node : naviInfo.getNodes()) {
							nodeNames[idx] = node.getName();
							idx++;
						}
						
						adapter = new ArrayAdapter<String>(MapViewerActivity.this, android.R.layout.simple_spinner_item, nodeNames);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						
						if (sipnnerFrom != null) {
							sipnnerFrom.setAdapter(adapter);
							
							if (naviFromNode != -1) {
								sipnnerFrom.setSelection(naviFromNode);
							}
							
							sipnnerFrom.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									naviFromNode = arg2;
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									naviFromNode = -1;
								}
								
							});
						}
						
						if (sipnnerTo != null) {
							sipnnerTo.setAdapter(adapter);
							
							if (naviToNode != -1) {
								sipnnerTo.setSelection(naviToNode);
							}
							
							sipnnerTo.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									naviToNode = arg2;
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									naviToNode = -1;
								}
								
							});
						}
					}				   
					
					builder.setPositiveButton(R.string.navi_go, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if ((naviFromNode == -1) || (naviToNode == -1) || (naviFromNode == naviToNode)) {
								Util.showLongToast(MapViewerActivity.this, R.string.navi_node_select_wrong);
							} else {
								goNavigator();
							}
						}
					});

					builder.setNegativeButton(R.string.cancel, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							naviFromNode = -1;
							naviToNode = -1;
						}
					});
					
					builder.create();
					builder.show();
			  }
		});
	}
	
	@SuppressWarnings("unused")
	private void showProgressDialog() {
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle(R.string.get_data_dialog_title);
			dialog.setMessage(getString(R.string.get_data_dialog_content));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			mProgressDialog = dialog;
		}

		if (!mProgressDialog.isShowing())
			mProgressDialog.show();
	}
	
	@SuppressLint("SimpleDateFormat")
	private void showTestResult(TestLocateCollectReply testLocation, Location balanceLocation) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		String message = "";		
		long currentTime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		if (testLocation.getReTest() == 1) {
			if (mMode == IndoorMapData.MAP_MODE_TEST_COLLECT) {
				message += "第一次在该点采集指纹！无法参考此测试结果！\n";
				message += "如有必要请多次采集数据并查看测试结果！\n";	
			} else {
				if (mMode == IndoorMapData.MAP_MODE_TEST_LOCATE) {
					message += "该点指纹未采集！已自动保存指纹！无法参考此测试结果！\n";
					message += "请稍后重新测试该点！如有必要请删除并重新采集该点数据！\n";
				} else {
					message += "模式已被切换！\n";
				}
			}	
		}
		
		Location origLocation = testLocation.getOrigLocation();
		
		if (origLocation.getMapId() == balanceLocation.getMapId()){
			message += "实际地点：[" + origLocation.getX() + "," + origLocation.getY() + "]\n";
			message += "定位地点：[" + balanceLocation.getX() + "," + balanceLocation.getY() + "]\n";
		} else {
			message += "定位地点和实际地点不在同一张地图！\n";
		}		
		
		float timeInterval1 = (currentTime - testLocation.getTimestamp1()) / 1000f;
		message += "总耗时间：" + timeInterval1 + "s\n";
		float timeInterval2 = (testLocation.getTimestamp3() - testLocation.getTimestamp2()) / 1000f;
		message += "定位耗时：" + timeInterval2 + "s\n";
		float timeInterval3 = timeInterval1 - timeInterval2;
		message += "网络耗时：" + timeInterval3 + "s";
		
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("测试结果 @ " + sdf.format(new Date(currentTime)));
		builder.setMessage(message);
		
		builder.setPositiveButton("我知道了", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});

		builder.create();
		builder.show();
	}
	
	private void startPeriodicAdvertiseThread(){
		if (advertisePeriodThread == null){
		advertisePeriodThread = new AdvertisePeriodThread();
		advertisePeriodThread.isRunning = true;
		advertisePeriodThread.isInit = false;
		advertisePeriodThread.start();
		}else {		
			if (DEBUG)
				Log.d(TAG, "PeriodicLocateMeThread already starts.");
		}
			
	}

	private void startPeriodicLocateMeThread() {
		if (mPeriodicLocateMeThread == null) {

			// Locate Me Periodically
			mPeriodicLocateMeThread = new Thread() {
				public void run() {
					while (true) { // Run forever
						if (!periodicLocateMeOn) {
							break; // Stop Thread on pause
						}
						
						try {
							sleep(IndoorMapData.PERIODIC_LOCATE_INTERVAL);
						} catch (InterruptedException e) {
							continue;
						}

						if (!periodicLocateMeOn) {
							break; // Stop Thread on pause
						}
						
						long currentTime = System.currentTimeMillis();
						
						if (currentTime-lastManualLocateTime<IndoorMapData.PERIODIC_LOCATE_INTERVAL) {
							// No Periodically Location Update if some manual update happens inner this interval
							continue;
						}	
							
						locateMe(true); // Periodic Locating
					}
				}
			};

			if (DEBUG)
				Log.d(TAG, "PeriodicLocateMeThread starts.");

			mPeriodicLocateMeThread.start();
		} else {
			if (DEBUG)
				Log.d(TAG, "PeriodicLocateMeThread already starts.");
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	private void startUpdateClockThread() {
		if (mUpdateClockThread == null) {

			// Locate Me Periodically
			mUpdateClockThread = new Thread() {
				public void run() {
					while (true) { // Run forever
						if (!updateClockOn) {
							break; // Stop Thread on pause
						}
						
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							continue;
						}

						if (!updateClockOn) {
							break; // Stop Thread on pause
						}
						
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
						String clockStr = sdf.format(new Date(System.currentTimeMillis()));
						if (mClockText != null) {
							mClockText.setText(clockStr);
						}
					}
				}
			};

			if (DEBUG)
				Log.d(TAG, "mUpdateClockThread starts.");

			mUpdateClockThread.start();
		} else {
			if (DEBUG)
				Log.d(TAG, "mUpdateClockThread already starts.");
		}
	}
	
	private void testCollect() {
		if (!Util.getWifiInfoManager().isWifiEmbeded()) {
			//Util.showLongToast(this, R.string.no_wifi_embeded);
			updateHintText(R.string.no_wifi_embeded);
			return;
		}

		if (!Util.getWifiInfoManager().isWifiEnabled()) {
			//Util.showLongToast(this, R.string.no_wifi_enabled);
			updateHintText(R.string.no_wifi_enabled);
			return;
		}
		
		//Util.showShortToast(this, R.string.collecting);
		updateHintText(R.string.collecting);
		
		if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER) {
			if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
				TestLocateCollectRequest testPosition = new TestLocateCollectRequest();
				testPosition.setLocation(new Location(runtimeIndoorMap.getMapId(), mTargetColNo, mTargetRowNo, runtimeIndoorMap.getVersionCode()));
				WifiFingerPrint fingnerPrint = Util.getWifiInfoManager().mergeSamples();
				fingnerPrint.log();
				testPosition.setFignerPrint(fingnerPrint);
				testPosition.setTimestamp(System.currentTimeMillis());
	
				try {
					Gson gson = new Gson();
					String json = gson.toJson(testPosition);
					JSONObject data = new JSONObject(json);
	
					if (Util.sendToServer(this, MsgConstants.MT_COLLECT, data)) {
						// For test purpose, display the [x,y]
						updateHintText(getResources().getString(R.string.collected)
								+ " ["
								+ mTargetColNo
								+ ","
								+ mTargetRowNo + "]");
							/*
							Util.showToast(this,
									getResources().getString(R.string.collected)
											+ " ["
											+ x
											+ ","
											+ y + "]",
									Toast.LENGTH_LONG);
									*/
					} else {
						// All errors should be handled in the sendToServer
						// method
					}
	
				} catch (Exception ex) {
					//Util.showToast(this, "102 " + ex.toString(), Toast.LENGTH_LONG);
					ex.printStackTrace();
					updateHintText("TEST_COLLECT: 102 ERROR: " + ex.getMessage());
				}
				
				return;
			} // if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
		} // if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER) {
		
		
		// No enough buffered data or buffer not used, use a thread
		
		// To pass the x, y to our new Thread
		if ((currentCollectingX != -1) || (currentCollectingX != -1)) {
			//Util.showShortToast(this, R.string.collect_ongoing_failure);
			updateHintText(R.string.collect_ongoing_failure);
			return;
		}

		if (isCollectingOnGoing()) {
			updateHintText(R.string.another_collect_ongoing);
			//Util.showShortToast(this, R.string.another_collect_ongoing);
			return;
		}

		setCollectingOnGoing(true);

		currentCollectingX = mTargetColNo;
		currentCollectingY = mTargetRowNo;

		new Thread() {
			public void run() {
				TestLocateCollectRequest testPosition = new TestLocateCollectRequest();
				testPosition.setLocation(new Location(runtimeIndoorMap.getMapId(), currentCollectingX, currentCollectingY, runtimeIndoorMap.getVersionCode()));
				WifiFingerPrint fingnerPrint = new WifiFingerPrint(IndoorMapData.REQUEST_COLLECT);
				fingnerPrint.log();
				testPosition.setFignerPrint(fingnerPrint);
				testPosition.setTimestamp(System.currentTimeMillis());

				setCollectingOnGoing(false);

				try {
					Gson gson = new Gson();
					String json = gson.toJson(testPosition);
					JSONObject data = new JSONObject(json);

					if (Util.sendToServer(MapViewerActivity.this, MsgConstants.MT_COLLECT_TEST, data)) {
						// For test purpose, display the [x,y]
						updateHintText(getResources().getString(R.string.collected)
								+ " ["
								+ currentCollectingX
								+ ","
								+ currentCollectingY + "]");
							/*Util.showToast(MapViewerActivity.this,
									getResources()
											.getString(R.string.collected)
											+ " ["
											+ currentCollectingX
											+ ","
											+ currentCollectingY + "]",
									Toast.LENGTH_LONG);*/
					} else {
						// All errors should be handled in the sendToServer
						// method
					}

				} catch (Exception ex) {
					//Util.showToast(MapViewerActivity.this,"002 " + ex.toString(), Toast.LENGTH_LONG);
					ex.printStackTrace();
					updateHintText("TEST_COLLECT: 002 ERROR: " + ex.getMessage());
				}

				// We are ready for next collecting on new position
				currentCollectingX = -1;
				currentCollectingY = -1;

				if (DEBUG)
					Log.d(TAG, "End testCollect Thread");
			}
		}.start();
	}

	private void testLocate() {
		if (!Util.getWifiInfoManager().isWifiEmbeded()) {
			//Util.showLongToast(this, R.string.no_wifi_embeded);
			updateHintText(R.string.no_wifi_embeded);
			return;
		}

		if (!Util.getWifiInfoManager().isWifiEnabled()) {
			//Util.showLongToast(this, R.string.no_wifi_enabled);
			updateHintText(R.string.no_wifi_enabled);
			return;
		}
		
		//Util.showShortToast(this, R.string.locate_collecting);
		updateHintText(R.string.locate_collecting);
		
		if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR) {
			if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
				try {
					TestLocateCollectRequest testPosition = new TestLocateCollectRequest();
					testPosition.setLocation(new Location(runtimeIndoorMap.getMapId(), mTargetColNo, mTargetRowNo, runtimeIndoorMap.getVersionCode()));
					WifiFingerPrint fingnerPrint = Util.getWifiInfoManager().mergeSamples();
					fingnerPrint.log();
					
					testPosition.setFignerPrint(fingnerPrint);
					testPosition.setTimestamp(System.currentTimeMillis());
					try {
						Gson gson = new Gson();
						String json = gson.toJson(testPosition);
						JSONObject data = new JSONObject(json);
	
						if (Util.sendToServer(this, MsgConstants.MT_LOCATE_TEST, data)) {
							//Util.showShortToast(this, R.string.locate_collected);
							updateHintText(R.string.locate_collected);
						} else {
							// All errors should be handled in the sendToServer
							// method
						}
					} catch (Exception ex) {
						//Util.showToast(this, "LOCATE_TEST:104 " + ex.toString(), Toast.LENGTH_LONG);
						ex.printStackTrace();
						updateHintText("LOCATE_TEST:104 ERROR " + ex.getMessage());
						finish();
						return;
					}
				} catch (Exception e) {
					//Util.showToast(this, "LOCATE_TEST:103 " + e.toString(), Toast.LENGTH_LONG);
					e.printStackTrace();
					updateHintText("LOCATE_TEST:103 ERROR " + e.getMessage());
					finish();
					return;
				}
				
				return;
			} //if (Util.getWifiInfoManager().hasEnoughSavedSamples()) {
		} //if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR) {
			
		// No enough buffered data or buffer not used, use a thread
		
		if (isCollectingOnGoing()) {
			//Util.showShortToast(this, R.string.another_collect_ongoing);
			updateHintText(R.string.another_collect_ongoing);
			return;
		}
		
		// To pass the x, y to our new Thread
		if ((currentCollectingX != -1) || (currentCollectingX != -1)) {
			//Util.showShortToast(this, R.string.collect_ongoing_failure);
			updateHintText(R.string.collect_ongoing_failure);
			return;
		}
		
		setCollectingOnGoing(true);
		
		currentCollectingX = mTargetColNo;
		currentCollectingY = mTargetRowNo;

		new Thread() {
			public void run() {
				try {
					TestLocateCollectRequest testPosition = new TestLocateCollectRequest();
					testPosition.setLocation(new Location(runtimeIndoorMap.getMapId(), currentCollectingX, currentCollectingY, runtimeIndoorMap.getVersionCode()));
					WifiFingerPrint fingnerPrint = new WifiFingerPrint(IndoorMapData.REQUEST_LOCATE);
					fingnerPrint.log();
					testPosition.setFignerPrint(fingnerPrint);
					testPosition.setTimestamp(System.currentTimeMillis());

					setCollectingOnGoing(false);

					try {
						// data.put("req", IndoorMapData.REQUEST_LOCATE);
						// data.put("fingerprint", fingerPrint);
						Gson gson = new Gson();
						String json = gson.toJson(testPosition);
						JSONObject data = new JSONObject(json);

						if (Util.sendToServer(MapViewerActivity.this, MsgConstants.MT_LOCATE_TEST, data)) {
							/*
							 * if (waitForLocation()) { // do nothing } else {
							 * // All errors should be handled in the
							 * waitForLocation // method }
							 */
							//Util.showShortToast(MapViewerActivity.this, R.string.locate_collected);
							updateHintText(R.string.locate_collected);
						} else {
							// All errors should be handled in the sendToServer
							// method
						}
					} catch (Exception ex) {
						//Util.showToast(MapViewerActivity.this, "004 " + ex.toString(), Toast.LENGTH_LONG);
						ex.printStackTrace();
						updateHintText("TEST_LOC004 ERROR: " + ex.getMessage());
					}
				} catch (Exception e) {
					setCollectingOnGoing(false);
					
					//Util.showToast(MapViewerActivity.this,"003 " + e.toString(), Toast.LENGTH_LONG);
					e.printStackTrace();
					updateHintText("TEST_LOC003 ERROR: " + e.getMessage());
				}
				
				// We are ready for next collecting on new position
				currentCollectingX = -1;
				currentCollectingY = -1;

				if (DEBUG)
					Log.d(TAG, "End MT_LOCATE_TEST Thread");
			}
		}.start();
	}
	
	@SuppressLint("SimpleDateFormat")
	private void updateHintText(int textId) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		String clockStr = sdf.format(new Date(System.currentTimeMillis()));
		mHintText.setText(clockStr + " " + getResources().getString(textId));
	}
	
	@SuppressLint("SimpleDateFormat")
	private void updateHintText(String text) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		String clockStr = sdf.format(new Date(System.currentTimeMillis()));
		mHintText.setText(clockStr + " " + text);
	}


	private boolean updateLocation(int mapId, int mapVersion, int colNo, int rowNo) {
		if (DEBUG)
			Log.e("updateLocation", "mapId="+mapId+",mapVersion="+mapVersion+",colNo="+colNo+",rowNo="+rowNo);

		if ((mapId == -1) || (rowNo == -1) || (colNo == -1)) {
			//Util.showLongToast(this, R.string.no_match_location);
			updateHintText(R.string.no_match_location);

			if (DEBUG)
				Log.d(TAG, "End updateLocation: Fail");
			return false;
		}

		if ( (mapId == runtimeIndoorMap.getMapId()) && (mapVersion == runtimeIndoorMap.getVersionCode()) ) {
			// Inner same Map with same version
			//Util.showShortToast(this, R.string.located);
			updateHintText(R.string.located);
			
			// Out of bound
			if ((rowNo >= runtimeIndoorMap.getRowNum()) || (colNo >= runtimeIndoorMap.getColNum())) {
				//Util.showLongToast(this, R.string.map_out_of_date);
				updateHintText(R.string.out_of_map_bound);
				return false;
			}

			graphicListener.locate(runtimeIndoorMap, colNo, rowNo, Constants.LOCATION_USER, 0);

			setCameraCenterTo(colNo, rowNo); // x,y
			
			// Set last known good location
			naviMyPlaceX = colNo;
			naviMyPlaceY = rowNo;

			// Show Location based News
			infoMe(colNo, rowNo);
		} else {
			// Not loading new map
			if (periodicLoacting || (mMode != IndoorMapData.MAP_MODE_VIEW)){
				
				// Not Load new Map automatically when in EditMode or Periodic
				// Location Update if not the same MapId
				// If only version changes, load/upgrade new map in all scenarios
				if (mapId != runtimeIndoorMap.getMapId()) {
					updateHintText(R.string.location_not_in_this_map);
	
					// Not display the user
					mainScene.getChildByIndex(Constants.LAYER_USER).detachChild(
							runtimeIndoorMap.getUser().getSprite());
					
					return true;
				}
			} 
			
			// loading new map
			updateHintText(R.string.loading_new_map);

			Intent intent_locate_map = new Intent(MapViewerActivity.this,
					MapLocatorActivity.class);
			// Bundle bundle = getIntent().getExtras();
			Bundle mBundle = new Bundle();
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM,
					IndoorMapData.BUNDLE_VALUE_REQ_FROM_VIEWER);
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP, mapId);
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_MAP_VERSION, mapVersion);
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_COL, colNo);
			mBundle.putInt(IndoorMapData.BUNDLE_KEY_LOCATION_ROW, rowNo);
			intent_locate_map.putExtras(mBundle);
			startActivity(intent_locate_map);

			finish();
			
			System.gc();
			try {
				finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		return true;
	}
	
	public void updateLocation(Location location) {
		// Not display the user		
		mainScene.getChildByIndex(Constants.LAYER_USER).detachChild(runtimeIndoorMap.getUser().getSprite());
		for (int i=0; i<runtimeIndoorMap.getTracksNum();i++){
			mainScene.getChildByIndex(Constants.LAYER_USER).detachChild(runtimeIndoorMap.getTrack(i).getSprite());
		}
		
		updateLocation(location.getMapId(), location.getMapVersion(), location.getX(), location.getY());
	}

	public void updateLocation(LocationSet locationSet) {
		Location banlanceLocation = locationSet.balanceLocation();

		updateLocation(banlanceLocation);
		
		if (banlanceLocation.getMapId() == runtimeIndoorMap.getMapId()) {
			updateTrack(locationSet.getLocations());
		}
        
		// TODO: The server side is not ready, comment this line to avoid the 404 Not Found error
		//mAdvertisement.getAds(banlanceLocation);
	}




	private void updateLocation(TestLocateCollectReply testLocation) {
		LocationSet locationSet = testLocation.getLocations();
		
		Location banlanceLocation = locationSet.balanceLocation();

		updateLocation(banlanceLocation);
		
		if (banlanceLocation.getMapId() == runtimeIndoorMap.getMapId()) {
			updateTrack(locationSet.getLocations());
		}
	
		showTestResult(testLocation, banlanceLocation);
	}

	private void updateTrack(ArrayList<Location> locations) {
		// Sanity Check

		if (locations == null) {
			return;
		}
				
		int idx = 0;
		
		for (Location location:locations) {
			int mapId = location.getMapId();
			int version = location.getMapVersion();
			int colNo = location.getX();
			int rowNo = location.getY();
			
			if (DEBUG)
				Log.e("updateTrack", "mapId="+mapId+",colNo="+colNo+",rowNo="+rowNo);
			
			if ((mapId == -1) || (rowNo == -1) || (colNo == -1)) {
				// Not display the track
				continue;
			}

			if ( (mapId == runtimeIndoorMap.getMapId()) && (version == runtimeIndoorMap.getVersionCode()) ) {
				// Inner same Map with same Version
				
				// Out of bound
				if ((rowNo >= runtimeIndoorMap.getRowNum()) || (colNo >= runtimeIndoorMap.getColNum())) {
					continue;
				}
				
				graphicListener.locate(runtimeIndoorMap, colNo, rowNo, Constants.LAYER_USER, idx);
				idx++;
			} else {
				// ignore if mapId or version changed
				continue;
			}
		}
		
	}
}
