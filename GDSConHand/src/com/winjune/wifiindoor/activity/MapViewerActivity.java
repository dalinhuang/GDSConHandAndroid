package com.winjune.wifiindoor.activity;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.LayoutGameActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.widget.Toast;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.id;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.R.string;
import com.winjune.wifiindoor.ads.ScreenAdvertisement;
import com.winjune.wifiindoor.drawing.GraphicIndoorMapListener;
import com.winjune.wifiindoor.drawing.MapCameraViewGestureListener;
import com.winjune.wifiindoor.drawing.ModeControl;
import com.winjune.wifiindoor.drawing.SoundIndoorMapListener;
import com.winjune.wifiindoor.drawing.ZoomControl;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.map.IndoorMap;
import com.winjune.wifiindoor.map.IndoorMapLoader;
import com.winjune.wifiindoor.mapviewer.AdBanner;
import com.winjune.wifiindoor.mapviewer.AdBanner.AdvertisePeriodThread;
import com.winjune.wifiindoor.mapviewer.InfoBanner;
import com.winjune.wifiindoor.mapviewer.InterestPlaceBar;
import com.winjune.wifiindoor.mapviewer.LabelBar;
import com.winjune.wifiindoor.mapviewer.LocateBar;
import com.winjune.wifiindoor.mapviewer.MapDrawer;
import com.winjune.wifiindoor.mapviewer.MapHUD;
import com.winjune.wifiindoor.mapviewer.MapViewerUtil;
import com.winjune.wifiindoor.mapviewer.NaviBar;
import com.winjune.wifiindoor.mapviewer.PlanBar;
import com.winjune.wifiindoor.navi.NaviInfo;
import com.winjune.wifiindoor.navi.Navigator;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;
import com.winjune.wifiindoor.util.WifiIpsSettings;
import com.winjune.wifiindoor.webservice.IpsWebService;


public class MapViewerActivity extends LayoutGameActivity implements SensorEventListener {

	public static final String TAG = MapViewerActivity.class.getSimpleName();
	public static final boolean DEBUG = WifiIpsSettings.DEBUG;

	public ZoomCamera mCamera;
	public Font mFont_hints;
	public Font mFont_mapinfo;
	public Font mFont_menu;
	public int totalWidth;
	public int totalHeight;
	public int cameraWidth;
	public int cameraHeight;
	public ScaleGestureDetector zoomGestureDector;
	public GestureDetector gestureDetector;
	public Scene mainScene;
	private MenuScene mMenuScene;
	public Sprite backgroundSprite;
	//private Sprite mapPicSprite;
	public ZoomControl zoomControl;
	public ModeControl modeControl;
	public int mMode;
	public HUD hud;
	public Text mMapText;
	public Text mClockText;
	public Text mBatteryText;
	public Text mHintText;
	public Sound medSound;
	public ScreenAdvertisement mAdvertisement;
	public Sprite mapADSprite;
	//private TabHost mTabHost;
	private BroadcastReceiver batteryReceiver;

	private Bundle bundle;

	public GraphicIndoorMapListener graphicListener;
	private IndoorMapLoader indoorMapLoader;

	public int currentCollectingX = -1;
	public int currentCollectingY = -1;
	public boolean collectingOnGoing = false;

	public static final int MENU_ITEM_BACK = Menu.FIRST;
	public static final int MENU_ITEM_INFO = Menu.FIRST + 1;
	public static final int MENU_ITEM_CONFIG = Menu.FIRST + 2;
	public static final int MENU_ITEM_EXIT = Menu.FIRST + 3;

	public Thread mPeriodicLocateMeThread;
	public boolean periodicLocateMeOn;
	public boolean periodicLoacting;

	public ProgressDialog mProgressDialog;
	public Toast infoQueryToast;

	public int mNfcEditState;
	public int mTargetColNo;
	public int mTargetRowNo;
	
	private long lastBackTime;
	public long lastManualLocateTime;
	public boolean reDrawPending;
	public Thread reDrawThread;
	public boolean reDrawOn;
	public boolean reDrawOngoing;
	
	public Thread mUpdateClockThread;
	public boolean updateClockOn;
	public int mOrientation; 
	
	public ArrayList<Text> mapInfos;
	public ArrayList<Sprite> interestPlaces;
	public ArrayList<Rectangle> collectedFlags; // Flags for fingerprint collected cells
	
	public NaviInfo naviInfo;
	public Navigator myNavigator;
	
	public int naviMyPlaceX;
	public int naviMyPlaceY;
	public int naviFromNode;
	public int naviToNode;	
	
	public int LEFT_SPACE;
	public int RIGHT_SPACE;
	public int TOP_SPACE;
	public int BOTTOM_SPACE;
	public int CONTROL_BUTTON_WIDTH;
	public int CONTROL_BUTTON_HEIGHT;	
	public int CONTROL_BUTTON_MARGIN;
	public int TAB_BUTTON_WIDTH;
	public int TAB_BUTTON_HEIGHT;	
	public int TAB_BUTTON_MARGIN;
		
	public float density = 1.5f;

	public AdvertisePeriodThread advertisePeriodThread;

	@SuppressLint("ShowToast")
	private void initData() {
		if (DEBUG)
			Log.e(TAG, "Start initialData");

		backgroundSprite = null;
		//mapPicSprite = null;	
		
		batteryReceiver = null;

		currentCollectingX = -1;
		currentCollectingY = -1;
		LocateBar.setCollectingOnGoing(this, false);
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
		
		reDrawPending = false;
		reDrawOn = true;
		reDrawOngoing = false;

		infoQueryToast = Toast.makeText(this,
				getResources().getString(R.string.no_latest_info),
				Toast.LENGTH_LONG);
		infoQueryToast.setMargin(0, 0);
		infoQueryToast.setGravity(Gravity.TOP, 0, 0);

		if (DEBUG)
			Log.e(TAG, "End initialData");
	}

	/*
	 * public void onBackPressed() { // Do nothing }
	 */

	@Override
	protected void onDestroy() {
		Util.setEnergySave(true);
		
		// Disable NFC Foreground Dispatch
		Util.disableNfc(this);
		
		// Disable ACCELEROMETER
	    Util.disableAcclerometer(this);
	    
	    if (mAdvertisement != null) {
	    	mAdvertisement.deleteAdvertises();
	    }
	    
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
		
		if (reDrawThread != null){
			reDrawOn = false;
			reDrawThread = null;
		}

		// Disable NFC Foreground Dispatch
		Util.disableNfc(this);
		
		// Enable ACCELEROMETER
		Util.disableAcclerometer(this);
		unregisterReceiver(batteryReceiver);
		
		Util.setCurrentForegroundActivity(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.gc();

		if (DEBUG)
			Log.e(TAG, "onResume()");
		
		Util.setEnergySave(false);
		periodicLocateMeOn = true;
		updateClockOn = true;
		reDrawOn = true;

		Log.e("ViewerActivity", "Start IpsMessageHandler");
		
		
		IpsWebService.setActivity(this);
		IpsWebService.activateWebService();
		
		LocateBar.startPeriodicLocateMeThread(this);
		
		MapHUD.startUpdateClockThread(this);
		
		MapDrawer.startRefreshMapThread(this);
		
		AdBanner.startPeriodicAdvertiseThread(this);

		// Enable NFC Foreground Dispatch
		Util.enableNfc(this);
		
		// Enable ACCELEROMETER
		Util.enableAcclerometer(this);
		
		// Listen on Battery
		
		if (batteryReceiver == null) {
			batteryReceiver = new BroadcastReceiver() {
	
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
		}
		
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
        
        Util.setCurrentForegroundActivity(this);
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
					PlanBar.editNfcQrTagInMap(this, tagId);
	
					// Collect Fingerprint on this location silently
					PlanBar.collectFingerprint(this, true); // x, y
				} else {
					//Util.showLongToast(this, R.string.select_position_before_scan_qr);
					MapHUD.updateHinText(this, R.string.select_position_before_scan_qr);
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
	public void onBackPressed() {

		// twice quit APP		    		
		if (System.currentTimeMillis() - lastBackTime > 3000){
			lastBackTime = System.currentTimeMillis();
			Util.showShortToast(this, R.string.press_back_more);
		} else {
			MapViewerUtil.exitApp();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean pHasWindowFocus) {
		super.onWindowFocusChanged(pHasWindowFocus);

		if (DEBUG)
			Log.d(TAG, "onWindowFocusChanged()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (DEBUG)
			Log.d(TAG, "onConfigurationChanged()");
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
				MapHUD.updateHinText(this, R.string.nfc_scan_finished);

				// send the NFC tagId + [MapID,X,Y] to server, so the Fine
				// Location against this NFC can be stored/updated
				PlanBar.editNfcQrTagInMap(this, tagId);

				// Collect Fingerprint on this location silently
				PlanBar.collectFingerprint(this, true); // x, y
			}
		} else {
			Util.nfcQrLocateMe(this, tagId); 
		}

	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		Log.i("MapViewer", "onCreateEngineOptions...");
		
		// This should be done before everything since we need some data set in
		// this step to set the width, camera etc.
		initData();

		// Get MapID from the Map Chooser/Locator
		bundle = getIntent().getExtras();
		IndoorMap indoorMap = (IndoorMap) bundle.getSerializable(IndoorMapData.BUNDLE_KEY_MAP_INSTANCE);
		indoorMapLoader = new IndoorMapLoader(this, indoorMap);
		Util.setRuntimeIndoorMap(indoorMapLoader.getRuntimeIndoorMap()); // To avoid pass the map in parameter everywhere
		
		// Initialize and Set Camera
		MapViewerUtil.initCamera(this);
		
		int mapWidth = Util.getRuntimeIndoorMap().getColNum() * Util.getRuntimeIndoorMap().getCellPixel();
		int mapHeight = Util.getRuntimeIndoorMap().getRowNum() * Util.getRuntimeIndoorMap().getCellPixel();

		totalWidth = mapWidth + LEFT_SPACE + RIGHT_SPACE;
		totalHeight = mapHeight + TOP_SPACE + BOTTOM_SPACE;

		// To align with the Camera
		if (totalWidth < cameraWidth) {
			totalWidth = cameraWidth;
		}

		if (totalHeight < cameraHeight) {
			totalHeight = cameraHeight;
		}

		//Obsoleted: Change to: Calculate the Zoom Out rate that the less scaled width or height can be displayed in the screen.
		//float min_zoom_factor = 1f * cameraWidth / totalWidth;
		//float height_min_zoom_factor = 1f * cameraHeight / totalHeight;
		//if (min_zoom_factor < height_min_zoom_factor) {
		//	min_zoom_factor = height_min_zoom_factor;
		//}	
		//float max_zoom_factor = Math.max(min_zoom_factor * 2, 10.0f);
		//float current_zoom_factor = Math.min(min_zoom_factor * 3, 5.0f);
		
		// Change to: do not allow zoomFactor too small, to avoid all or too much map pieces be displayed in the Screen and cause the OOM issue 
		float min_zoom_factor = 0.8f;		
		float max_zoom_factor = 6f;		
		float current_zoom_factor = 3;
		
		// Original zoom factor
		mCamera.setZoomFactor(current_zoom_factor);
		// Allowed zoom Factors
		zoomControl = new ZoomControl(this, mCamera, max_zoom_factor, min_zoom_factor, density);

		// Control the Map Mode
		modeControl = new ModeControl(Util.getRuntimeIndoorMap());

		mCamera.setBounds(0, 0, totalWidth, totalHeight);
		mCamera.setBoundsEnabled(true);

		// to enable finger scroll of camera
		gestureDetector = new GestureDetector(this,
				new MapCameraViewGestureListener(this));

		// zoom when multi-touchs
		zoomGestureDector = new ScaleGestureDetector(this,
				zoomControl.getScaleGestureListner());//Hoare

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
		
		Log.i("MapViewer", "onCreateResources...");

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
		
		
		// Ensure the map info font is not too small on large screen
		// float mapInfoFontSize = Math.round(Math.min(cameraWidth, cameraHeight)/72);
	
		//not like other font, the map info font size is an absolute size, 
		//the exact size should be defined in database as its scale
		// the reason is that the font size is related the picture density
		BitmapTextureAtlas fontTexture_mapInfo = new BitmapTextureAtlas(
				getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		mFont_mapinfo = FontFactory.createFromAsset(
						getFontManager(),
						fontTexture_mapInfo,
						getAssets(),
						"comic.ttf",
						VisualParameters.FONT_CHAR_ABS_WIDTH_MAPINFO,
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

		SoundFactory.setAssetBasePath("mfx/");
		/* Load all the sounds this map needs. */
		try {
			medSound = SoundFactory.createSoundFromAsset(getSoundManager(), this, "cell_collected.ogg");
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// mEngine.registerUpdateHandler(new FPSLogger()); // FPS Logger
		Log.i("MapViewer", "onCreateScene...");
		
		// Main Scene
		mainScene = new Scene();
		for (int i = 0; i < Constants.LAYER_INDEX; i++) {
			mainScene.attachChild(new Entity());
		}
		
		Library.initial(mEngine.getTextureManager(), getAssets());
		
		// Draw the original in Map units, e.g. user
		indoorMapLoader.initialMap();

		// No background color needed as we have a fullscreen background sprite.
		mainScene.setBackgroundEnabled(true);
		mainScene.setBackground(new Background(255,255,255)); // white color
		// Background lines
		if (VisualParameters.BACKGROUND_LINES_NEEDED && VisualParameters.PLANNING_MODE_ENABLED) {
			backgroundSprite = Library.BACKGROUND3.load(this, cameraWidth, cameraHeight);
			backgroundSprite.setPosition(LEFT_SPACE, TOP_SPACE);
			mainScene.getChildByIndex(Constants.LAYER_BACKGROUND).attachChild(backgroundSprite);
		}

		// Create Map pieces on demands
		//mainScene.getChildByIndex(Constants.LAYER_MAP).attachChild(mapPicSprite);
		//mainScene.registerTouchArea(mapPicSprite);

		mainScene.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				return MapViewerUtil.handleTouchEvent(MapViewerActivity.this, pSceneTouchEvent.getMotionEvent());
			}

		});

		// Menu
		mMenuScene = MapHUD.createMenuScene(this);

		// HUDs
		hud = new HUD();
		mCamera.setHUD(hud);
		
		MapHUD.initailHUDMapShowBar(this);
		MapHUD.initailHUDClockBar(this);
		MapHUD.initailHUDBatteryBar(this);
		MapHUD.initailHUDHintBar(this);
		MapHUD.initialHUDMenuBar(this);
		MapHUD.initialHUDTabBar(this);
		

		// Listeners
		graphicListener = new GraphicIndoorMapListener(this, mainScene, mMapText);
		graphicListener.setOffsetX(LEFT_SPACE);
		graphicListener.setOffsetY(TOP_SPACE);
		Util.getRuntimeIndoorMap().addListener(graphicListener);
		Util.getRuntimeIndoorMap().addListener(new SoundIndoorMapListener());

		// Initial in map listeners
		Util.getRuntimeIndoorMap().initial();

		// Set User ID
		Util.getRuntimeIndoorMap().getUser().setId(Util.getWifiInfoManager().getMyMac());

		// The 1st action
		int req = bundle.getInt(IndoorMapData.BUNDLE_KEY_REQ_FROM);

		// Update Location
		switch (req) {
		case IndoorMapData.BUNDLE_VALUE_REQ_FROM_LOCATOR:
			int colNo = bundle.getInt(IndoorMapData.BUNDLE_KEY_LOCATION_COL);
			int rowNo = bundle.getInt(IndoorMapData.BUNDLE_KEY_LOCATION_ROW);

			LocateBar.updateLocation(this, 
									Util.getRuntimeIndoorMap().getMapId(), 
									Util.getRuntimeIndoorMap().getVersionCode(), 
									colNo, rowNo);

			break;
		case IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR:
			//Hoare: to do: entry can be configured in database
			if (( Util.getRuntimeIndoorMap().getMapId() == 1) |
				(Util.getRuntimeIndoorMap().getMapId() == 2)){
				MapDrawer.setCameraCenterTo(this, 39, 77, false); // set Center to left_top cell
			}
			
			InfoBanner.infoMe(this, -1, -1); // For map-wide Info
			break;
		default:
		}
		
		// Show the Map Info Layer
		LabelBar.loadMapInfo(this);
		
		// InitData for Navigator
		NaviBar.loadNaviInfo(this);
		
		// Show the Interest places layer
		InterestPlaceBar.loadInterestPlaces(this);
		
		//createTabHost();
		
		AdBanner.showDefaultAd(this);

		pOnCreateSceneCallback.onCreateSceneFinished(mainScene);
	}
	
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	protected int getLayoutID() {
		return R.layout.map_viewer;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		return R.id.map_rendersurfaceview;
	}
		
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (Util.isShakeDetected(event)) {
		        LocateBar.locateMe(this, false);
		}
	}		
}
