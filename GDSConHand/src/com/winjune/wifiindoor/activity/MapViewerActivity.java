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
import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.mapviewer.*;
import com.winjune.wifiindoor.activity.mapviewer.AdBanner.AdvertisePeriodThread;
import com.winjune.wifiindoor.activity.poiviewer.EventViewerActivity;
import com.winjune.wifiindoor.ads.ScreenAdvertisement;
import com.winjune.wifiindoor.drawing.GraphicMapListener;
import com.winjune.wifiindoor.drawing.MapViewGestureListener;
import com.winjune.wifiindoor.drawing.ModeControl;
import com.winjune.wifiindoor.drawing.SoundMapListener;
import com.winjune.wifiindoor.drawing.ZoomControl;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.drawing.graphic.model.LocationSprite;
import com.winjune.wifiindoor.lib.map.MapDataR;
import com.winjune.wifiindoor.map.MapManager;
import com.winjune.wifiindoor.navi.NaviContext;
import com.winjune.wifiindoor.poi.PlaceOfInterest;
import com.winjune.wifiindoor.poi.SearchContext;
import com.winjune.wifiindoor.runtime.RuntimeMap;
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
	public Font mFontHint;
	public Font mFontLabel;
	public ScaleGestureDetector zoomGestureDector;
	public GestureDetector gestureDetector;
	public Scene mainScene;
	public Sprite backgroundSprite;

	public ZoomControl zoomControl;
	public ModeControl modeControl;
	public int mMode;
	public HUD hud;
	public Text mHintText;
	public Sound medSound;
	public ScreenAdvertisement mAdvertisement;
	public Sprite mapADSprite;
	
	public GraphicMapListener graphicListener;

	public int currentCollectingX = -1;
	public int currentCollectingY = -1;
	public boolean collectingOnGoing = false;

	public Thread mPeriodicLocateMeThread;
	public boolean periodicLocateMeOn;
	public boolean periodicLoacting;

	public ProgressDialog mProgressDialog;
	public Toast infoQueryToast;

	public int mNfcEditState;
	public int mTargetColNo;
	public int mTargetRowNo;
	
	public long lastBackTime;
	public long lastManualLocateTime;
	public boolean reDrawPending;
	public Thread reDrawThread;
	public boolean reDrawOn;
	public boolean reDrawOngoing;
	
	public int mOrientation; 
	
	public ArrayList<Text> poiLabels;
	public ArrayList<Sprite> poiIcons;
	public LocationSprite focusPlace;
	public ArrayList<LocationSprite> locationPlaces;
	public ArrayList<Rectangle> collectedFlags; // Flags for fingerprint collected cells
	
	public int LEFT_SPACE;
	public int RIGHT_SPACE;
	public int TOP_SPACE;
	public int BOTTOM_SPACE;
	public int CONTROL_BUTTON_WIDTH;
	public int CONTROL_BUTTON_HEIGHT;	
	public int CONTROL_BUTTON_MARGIN;

	public static final int REQUEST_CODE = 0;
	public static final int CAMERA_REQUEST_CODE = REQUEST_CODE;
	public static final int USER_CENTER_REQUEST_CODE = REQUEST_CODE + 2;


	public AdvertisePeriodThread advertisePeriodThread;
	
	public long continuousCollectStartTime;
	public long continuousCollectStopTime;
	public int mContStartRowNo;
	public int mContStartColNo;
	public boolean mContStarted;

	@SuppressLint("ShowToast")


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
		reDrawOn = true;

		Log.e("ViewerActivity", "Start IpsMessageHandler");
		
		IpsWebService.activateWebService(this);
		
		LocateBar.startPeriodicLocateMeThread(this);
			
		MapDrawer.startRefreshMapThread(this);
		
		AdBanner.startPeriodicAdvertiseThread(this);

		// Enable NFC Foreground Dispatch
		Util.enableNfc(this);
		
		// Enable ACCELEROMETER
		Util.enableAcclerometer(this);	
        
        Util.setCurrentForegroundActivity(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case USER_CENTER_REQUEST_CODE:			
		  if(resultCode == RESULT_FIRST_USER ) {  
			  finish();
			  MapViewerUtil.exitApp();
	       }
		  break;
		case CAMERA_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				String tagId = bundle.getString("result");

				Log.e("QR", "tagID=" + tagId);

				if (mMode == IndoorMapData.MAP_MODE_EDIT_TAG) {
					if (mNfcEditState == IndoorMapData.NFC_EDIT_STATE_SCANNING) {
						mNfcEditState = IndoorMapData.NFC_EDIT_STATE_FINISH;

						// send the QR Code tagId + [MapID,X,Y] to server, so
						// the Fine
						// Location against this QR Code can be stored/updated
						PlanBar.editNfcQrTagInMap(this, tagId);

						// Collect Fingerprint on this location silently
						PlanBar.collectFingerprint(this, true); // x, y
					} else {
						// Util.showLongToast(this,
						// R.string.select_position_before_scan_qr);
						MapHUD.updateHinText(this,
								R.string.select_position_before_scan_qr);
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
			break;
		default:
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
		setIntent(intent);
		
		String mAction = intent.getAction();		
		if (mAction  == null)			
			return;
		
		Bundle mBundle = getIntent().getExtras();
		
		if (mAction.equals(Constants.ActionLocate)){
			PlaceOfInterest poi = (PlaceOfInterest)mBundle.getSerializable(Constants.BUNDLE_LOCATION_CONTEXT);
			LocateBar.attachLocationSprite(this, poi);
			poi.showContextMenu(getCurrentFocus());			
		}else if (mAction.equals(Constants.ActionSearch)) {			
			SearchContext mContext = (SearchContext) mBundle.getSerializable(Constants.BUNDLE_RESULT_SEARCH_CONTEXT);		
		   	SearchBar.showSearchResultsOnMap(this, mContext);
		}else if (mAction.equals(Constants.ActionRoute)){
			NaviContext mContext =  (NaviContext)mBundle.getSerializable(Constants.BUNDLE_KEY_NAVI_CONTEXT);
			// check the current runtime map 
			// switch to the start node's map
			int startMapId = mContext.naviRoute.get(0).getMapId();
			if (startMapId != Util.getRuntimeIndoorMap().getMapId()){
				final MapDataR mapData = MapManager.getMapById(startMapId);			
				switchRuntimeMap(mapData);
				refreshMapLabel(mapData.getLabel());		
			}		
		
			NaviBar.showNaviResulOnMap(this, mContext);
		  	mContext.showContextMenu(this);	
	    }
			

//		String tagId = Util.getNfcInfoManager().getTagId(intent);
//		
//		if (tagId == null) {
//			return;
//		}
//
//		if (mMode == IndoorMapData.MAP_MODE_EDIT_TAG) {
//			if (mNfcEditState == IndoorMapData.NFC_EDIT_STATE_SCANNING) {
//				// read out the NFC chip ID
//				// tagId = tagFromIntent.getId().toString();
//
//				mNfcEditState = IndoorMapData.NFC_EDIT_STATE_FINISH;
//				//Util.showLongToast(this, R.string.nfc_scan_finished);
//				MapHUD.updateHinText(this, R.string.nfc_scan_finished);
//
//				// send the NFC tagId + [MapID,X,Y] to server, so the Fine
//				// Location against this NFC can be stored/updated
//				PlanBar.editNfcQrTagInMap(this, tagId);
//
//				// Collect Fingerprint on this location silently
//				PlanBar.collectFingerprint(this, true); // x, y
//			}
//		} else {
//			Util.nfcQrLocateMe(this, tagId); 
//		}

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		// update map switch label
		TextView mapSwitchT = (TextView) findViewById(R.id.text_map_switch);
		mapSwitchT.setText(Util.getRuntimeIndoorMap().getMapLabel());		
	}
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		Log.i("MapViewer", "onCreateEngineOptions...");
				
		// This should be done before everything since we need some data set in
		// this step to set the width, camera etc.
		MapViewerUtil.initData(this);
 
		// Initialize runtime map data
		MapDataR mapData = (MapDataR) MapManager.getDefaultMap();		
		RuntimeMap mRuntimeMap = new RuntimeMap();
		mRuntimeMap.load(mapData);		
		Util.setRuntimeIndoorMap(mRuntimeMap); 		
		
		// Initialize and Set Camera
		
		MapViewerUtil.initCamera(this, mRuntimeMap.getDefaultZoomFactor(),
									   mRuntimeMap.getMapWidth(), 
									   mRuntimeMap.getMapHeight());
		
		// Allowed zoom Factors
		zoomControl = new ZoomControl(this, mCamera, 
										mRuntimeMap.getMaxZoomFactor(), 
										mRuntimeMap.getMinZoomFactor(), 
										Util.getcameraDensity());

		// Control the Map Mode
		modeControl = new ModeControl(Util.getRuntimeIndoorMap());		

		// to enable finger scroll of camera
		gestureDetector = new GestureDetector(this,	new MapViewGestureListener(this));

		// zoom when multi-touchs
		zoomGestureDector = new ScaleGestureDetector(this, 	zoomControl.getScaleGestureListner());

		// FullScreen? Landscape? & Camera?
		ScreenOrientation pScreenOrientation = ScreenOrientation.PORTRAIT_SENSOR;
		if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			pScreenOrientation = ScreenOrientation.LANDSCAPE_SENSOR;
		}
		
		//final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(cameraWidth, cameraHeight), mCamera);
		final EngineOptions engineOptions = new EngineOptions(false, 
															pScreenOrientation, 
															new RatioResolutionPolicy(Util.getCameraWidth(), Util.getCameraHeight()),
															mCamera);

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
		mFontHint = FontFactory.createFromAsset(
						getFontManager(),
						fontTexture_hints,
						getAssets(),
						"comic.ttf",
						Util.getcameraDensity() * VisualParameters.FONT_CHAR_WIDTH_HINTS,
						true, Color.BLUE);
		mFontHint.load();
		
		
		// Ensure the map info font is not too small on large screen
		// float mapInfoFontSize = Math.round(Math.min(cameraWidth, cameraHeight)/72);
	
		//not like other font, the map info font size is an absolute size, 
		//the exact size should be defined in database as its scale
		// the reason is that the font size is related the picture density
		BitmapTextureAtlas fontTexture_mapInfo = new BitmapTextureAtlas(
				getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		mFontLabel = FontFactory.createFromAsset(
						getFontManager(),
						fontTexture_mapInfo,
						getAssets(),
						"comic.ttf",
						VisualParameters.FONT_CHAR_ABS_WIDTH_MAPINFO,
						true, Color.BLACK);
		mFontLabel.load();

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
		Util.getRuntimeIndoorMap().initUserLayer(this);
		
		// No background color needed as we have a fullscreen background sprite.
		mainScene.setBackgroundEnabled(true);
		mainScene.setBackground(new Background(255,255,255)); // white color
		mainScene.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				return MapViewerUtil.handleTouchEvent(MapViewerActivity.this, pSceneTouchEvent.getMotionEvent());
			}

		});

		//Background
		MapDrawer.drawBackground(this);
	
		// HUDs
		hud = new HUD();
		mCamera.setHUD(hud);
		
		MapHUD.initailHUDHintBar(this);
		MapHUD.initialHUDMenuBar(this);	

		// Listeners
		graphicListener = new GraphicMapListener(this, mainScene);
		graphicListener.setOffsetX(LEFT_SPACE);
		graphicListener.setOffsetY(TOP_SPACE);
		Util.getRuntimeIndoorMap().addListener(graphicListener);
		Util.getRuntimeIndoorMap().addListener(new SoundMapListener());

		// Initial in map listeners
		Util.getRuntimeIndoorMap().initListeners();

		// Set User ID
		Util.getRuntimeIndoorMap().getUser().setId(Util.getWifiInfoManager().getMyMac());

		// The 1st action
		

		// Update Location
		int midRowNo = Util.getRuntimeIndoorMap().getRowNum()/2;
		int midColNo = Util.getRuntimeIndoorMap().getColNum()/2;
	
		MapDrawer.setCameraCenterTo(this, midColNo, midRowNo, false); // set Center to left_top cell
			
		InfoBanner.infoMe(this, -1, -1); // For map-wide Info
						
		// AdBanner.showDefaultAd(this);			

		pOnCreateSceneCallback.onCreateSceneFinished(mainScene);
		
	}
	
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_map_viewer;
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
	
	public void mapSwitchBarClick(View v) {
		mapSwitchPopShow(v);

	}
	
	private void mapSwitchPopShow(View v){
		LayoutInflater inflater = getLayoutInflater(); 
		View popWin = inflater.inflate(R.layout.popup_map_switch, null);
			   
		final PopupWindow pop = new PopupWindow(popWin, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false); 		  				
		pop.setBackgroundDrawable(new BitmapDrawable()); 
        pop.setOutsideTouchable(true); 
        pop.setFocusable(true);        
		
		ArrayList<MapDataR> maps = MapManager.getMaps();
		
		for (MapDataR map:maps){
			if (map.getId() != Util.getRuntimeIndoorMap().getMapId()) {
				TextView mapLabel = (TextView)inflater.inflate(R.layout.template_map_switch_text, (ViewGroup) popWin, false);				
				mapLabel.setText(map.getLabel());
				((ViewGroup)popWin).addView(mapLabel);
				
				mapLabel.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						MapViewerActivity.this.mapSwitchClick(v);	
						pop.dismiss();
					}
					
				});
			}
		}        
        
        pop.showAsDropDown(v);;
	}
	
	public void mapSwitchClick(View v){
		
		String label = ((TextView)v).getText().toString();
		
		Log.e("Map Switcher",label);
		
		MapDataR mapData = (MapDataR) MapManager.getMapByLabel(label);	
		switchRuntimeMap(mapData);
		
		refreshMapLabel(mapData.getLabel());
	}
	
	public void switchRuntimeMap(MapDataR mapData){
		if (mapData != null){
			
			RuntimeMap mRuntimeMap = new RuntimeMap();
						
			Util.setRuntimeIndoorMap(mRuntimeMap); // To avoid pass the map in parameter everywhere

			mRuntimeMap.load(mapData);
			
			// Listeners
			graphicListener = new GraphicMapListener(this, mainScene);
			graphicListener.setOffsetX(LEFT_SPACE);
			graphicListener.setOffsetY(TOP_SPACE);
			Util.getRuntimeIndoorMap().addListener(graphicListener);
			Util.getRuntimeIndoorMap().addListener(new SoundMapListener());

			// Initial in map listeners
			Util.getRuntimeIndoorMap().initListeners();

			Util.getRuntimeIndoorMap().initUserLayer(this);
			// Set User ID
			Util.getRuntimeIndoorMap().getUser().setId(Util.getWifiInfoManager().getMyMac());						
			
			MapDrawer.switchMapPrepare(this);
			MapDrawer.switchMapExcute(this);
			mCamera.setZoomFactor(Util.getRuntimeIndoorMap().getDefaultZoomFactor());
							
		}		
	}
	
	public void refreshMapLabel(String label){
		// update map switch label
		TextView mapSwitchT = (TextView) findViewById(R.id.text_map_switch);
		mapSwitchT.setText(label);			
	}
	
	public void scanBtnClick(View v){
		Intent i = new Intent(this, QrScannerActivity.class); 
        startActivity(i);		
	}
	
	public void searchBarClick(View v) {
        Intent i = new Intent(this, LabelSearchActivity.class); 
        startActivity(i);		
	}
	
	public void surroundingBarClick(View v){
        Intent i = new Intent(this, ShortcutEntryActivity.class); 
        startActivity(i);		
	}	
	
	public void routeBarClick(View v){
	
		Intent i = new Intent(this, RouteMainActivity.class); 
		startActivity(i);	
	}
	
	public void eventBarClick(View v){
        Intent i = new Intent(this, EventViewerActivity.class); 
		startActivity(i);		
	}
	
	public void userBarClick(View v) {
        Intent i = new Intent(this, UserCenterActivity.class); 
		startActivityForResult(i, USER_CENTER_REQUEST_CODE);
	}
	
	public void locationBtnClick(View v) {
		
		float x = mCamera.getBoundsWidth()/2;
		float y = mCamera.getBoundsHeight()/2;
		
		mCamera.setCenter(x, y);
		
		LocateBar.locateMe(this, false);
	}
	
	public void zoomInBtnClick(View v){
		zoomControl.zoomIn();
	}
	
	public void zoomOutBtnClick(View v){
		zoomControl.zoomOut();
	}

 	    
}
