package com.winjune.wifiindoor.mapviewer;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;

import com.winjune.wifiindoor.InfoPusherActivity;
import com.winjune.wifiindoor.MapSelectorActivity;
import com.winjune.wifiindoor.MapViewerActivity;
import com.winjune.wifiindoor.QrScannerActivity;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.TunerActivity;
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

		if (mapViewer.DEBUG)
			Log.d(mapViewer.TAG, "Start putHUDControlUnit");

		AnimatedSprite sprite = unit.load(mapViewer, spriteListener);

		sprite.setPosition(posX, posY);
		sprite.setAlpha(VisualParameters.CONTROL_BUTTON_ALPHA);

		mapViewer.hud.attachChild(sprite);

		mapViewer.hud.registerTouchArea(sprite);

		if (mapViewer.DEBUG)
			Log.d(mapViewer.TAG, "End putHUDControlUnit");
	}



	public static void initialHUDMenuBar(final MapViewerActivity mapViewer) {

		if (mapViewer.DEBUG)
			Log.d(mapViewer.TAG, "Start initialHUDMenuBar");

		int x = mapViewer.cameraWidth - mapViewer.CONTROL_BUTTON_WIDTH;
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
			
			y += mapViewer.CONTROL_BUTTON_HEIGHT + mapViewer.CONTROL_BUTTON_MARGIN * 2;
			Library.BUTTON_MAP.load(mapViewer, mapViewer.CONTROL_BUTTON_WIDTH, mapViewer.CONTROL_BUTTON_HEIGHT);
			putHUDControlUnit(mapViewer, Library.BUTTON_MAP, x, y, new SpriteListener() {
	
				@Override
				public boolean onAreaTouched(AnimatedSprite sprite,
						TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
						float pTouchAreaLocalY) {
	
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {						
		        		Intent intent_map_selector = new Intent(mapViewer, MapSelectorActivity.class);
		        		
		        		Bundle mBundle = new Bundle(); 
						mBundle.putInt(IndoorMapData.BUNDLE_KEY_REQ_FROM, IndoorMapData.BUNDLE_VALUE_REQ_FROM_SELECTOR);
						intent_map_selector.putExtras(mBundle); 
						mapViewer.startActivity(intent_map_selector);							
					}
	
					return true;
				}
			});		
			
		}// planning mode

		if (mapViewer.DEBUG)
			Log.d(mapViewer.TAG, "End initialHUDMenuBar");

	}
	
	public static void initialHUDTabBar(final MapViewerActivity mapViewer) {
		int x = mapViewer.TAB_BUTTON_MARGIN;
		int y = mapViewer.cameraHeight - mapViewer.TAB_BUTTON_HEIGHT;;		
		
		Library.BUTTON_NAVI.load(mapViewer, mapViewer.TAB_BUTTON_WIDTH, mapViewer.TAB_BUTTON_HEIGHT);	
		putHUDControlUnit(mapViewer, Library.BUTTON_NAVI, x, y, new SpriteListener() {

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {

				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					NaviBar.showNaviBar(mapViewer);
				}
				
				return true;
			}
		});
		
		x += mapViewer.TAB_BUTTON_WIDTH + mapViewer.TAB_BUTTON_MARGIN * 2;
		Library.BUTTON_SCAN_QR.load(mapViewer, mapViewer.TAB_BUTTON_WIDTH, mapViewer.TAB_BUTTON_HEIGHT);
		putHUDControlUnit(mapViewer, Library.BUTTON_SCAN_QR, x, y, new SpriteListener() {

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {

				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					Intent openCameraIntent = new Intent(mapViewer, QrScannerActivity.class);
					mapViewer.startActivityForResult(openCameraIntent, 0);
				}

				return true;
			}
		});

		x += mapViewer.TAB_BUTTON_WIDTH + mapViewer.TAB_BUTTON_MARGIN * 2;
		Library.BUTTON_LOCATE.load(mapViewer, mapViewer.TAB_BUTTON_WIDTH, mapViewer.TAB_BUTTON_HEIGHT);
		putHUDControlUnit(mapViewer,Library.BUTTON_LOCATE, x, y, new SpriteListener() {

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {
				
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
					mapViewer.lastManualLocateTime = System.currentTimeMillis();
					LocateBar.locateMe(mapViewer, false);
				}
				
				return true;
			}
		});		
		
		x += mapViewer.TAB_BUTTON_WIDTH + mapViewer.TAB_BUTTON_MARGIN * 2;
		Library.BUTTON_ZOOM.load(mapViewer, mapViewer.TAB_BUTTON_WIDTH, mapViewer.TAB_BUTTON_HEIGHT);
		putHUDControlUnit(mapViewer, Library.BUTTON_ZOOM, x, y, new SpriteListener() {
			
			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {										
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						// Interest place quick entry :
						InterestPlaceBar.showGuideAudioBar(mapViewer);
					}
				
				return true;
			}
		});
		
		x += mapViewer.TAB_BUTTON_WIDTH + mapViewer.TAB_BUTTON_MARGIN * 2;
		Library.BUTTON_MSG.load(mapViewer, mapViewer.TAB_BUTTON_WIDTH, mapViewer.TAB_BUTTON_HEIGHT);
		putHUDControlUnit(mapViewer, Library.BUTTON_MSG, x, y, new SpriteListener() {

			@Override
			public boolean onAreaTouched(AnimatedSprite sprite,
					TouchEvent pSceneTouchEvent, float pTouchAreaLocalX,
					float pTouchAreaLocalY) {

				Intent intent_pusher = new Intent(mapViewer, InfoPusherActivity.class); 
				Bundle mBundle = new Bundle(); 
				String info1 = Util.getRuntimeIndoorMap().informationsToString();
				String info2 = Util.getRuntimeIndoorMap().informationsToStringForLocations();
				
				mBundle.putString(IndoorMapData.BUNDLE_KEY_MAP_INFO, info1);
				mBundle.putString(IndoorMapData.BUNDLE_KEY_LOCATION_INFO, info2);
				intent_pusher.putExtras(mBundle); 
				mapViewer.startActivity(intent_pusher);

				return true;
			}
		});
	}

	public static void initailHUDMapShowBar(MapViewerActivity mapViewer) {
		String modeStr = mapViewer.getResources().getString(R.string.view_mode);
		StringBuilder builder = new StringBuilder();
		
		builder.append(mapViewer.getResources().getString(R.string.map))
		.append(Util.getRuntimeIndoorMap().getMapName()).append(" - ").append(modeStr); 
		
		// There is a bug that the future text can not be longer than the start one and that's why I appends some spaces here
		builder.append("                              ");

		mapViewer.mMapText = new Text(0,
				0, 
				mapViewer.mFont_hints, 
				builder.toString(),
				100,
				mapViewer.getVertexBufferObjectManager());
		mapViewer.mMapText.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		mapViewer.mMapText.setAlpha(VisualParameters.MAP_FONT_ALPHA);

		mapViewer.hud.attachChild(mapViewer.mMapText);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static void initailHUDClockBar(MapViewerActivity mapViewer) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		String clockStr = sdf.format(new Date(System.currentTimeMillis()));

		mapViewer.mClockText = new Text(mapViewer.cameraWidth - mapViewer.density * 150,
				0, 
				mapViewer.mFont_hints, 
				clockStr,
				8,
				mapViewer.getVertexBufferObjectManager());
		mapViewer.mClockText.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		mapViewer.mClockText.setAlpha(VisualParameters.MAP_FONT_ALPHA);

		mapViewer.hud.attachChild(mapViewer.mClockText);
	}
	
	public static void initailHUDBatteryBar(MapViewerActivity mapViewer) {
		mapViewer.mBatteryText = new Text(mapViewer.cameraWidth - mapViewer.density * 50,
				0, 
				mapViewer.mFont_hints, 
				"---%",
				4,
				mapViewer.getVertexBufferObjectManager());
		mapViewer.mBatteryText.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		mapViewer.mBatteryText.setAlpha(VisualParameters.MAP_FONT_ALPHA);

		mapViewer.hud.attachChild(mapViewer.mBatteryText);
	}

	@SuppressLint("SimpleDateFormat")
	public static void initailHUDHintBar(MapViewerActivity mapViewer) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		String hintStr = sdf.format(new Date(System.currentTimeMillis())) + " " + mapViewer.getResources().getString(R.string.load_complete);
		
		// There is a bug that the future text can not be longer than the start one and that's why I appends some spaces here
		hintStr += "                                                            "
				+ "                                 ";
		
		mapViewer.mHintText = new Text(0,
				mapViewer.mFont_hints.getLineHeight(),   // Flexible according to the height of 1st line text
				mapViewer.mFont_hints, 
				hintStr,
				100,
				mapViewer.getVertexBufferObjectManager());
		mapViewer.mHintText.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		mapViewer.mHintText.setAlpha(VisualParameters.MAP_FONT_ALPHA);

		mapViewer.hud.attachChild(mapViewer.mHintText);
	}

	public static MenuScene createMenuScene(final MapViewerActivity mapViewer) {
		if (mapViewer.DEBUG)
			Log.d(mapViewer.TAG, "Start createMenuScene");

		final MenuScene menuScene = new MenuScene(mapViewer.mCamera);

		final IMenuItem backMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(mapViewer.MENU_ITEM_BACK, mapViewer.mFont_menu, 
						mapViewer.getResources().getString(R.string.menu_back),
						mapViewer.getVertexBufferObjectManager()),
				new org.andengine.util.color.Color(1.0f, 0.0f, 0.0f),
				new org.andengine.util.color.Color(0.0f, 0.0f, 0.0f));
		backMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(backMenuItem);

		final IMenuItem infoMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(mapViewer.MENU_ITEM_INFO, mapViewer.mFont_menu, mapViewer.getResources()
						.getString(R.string.menu_info),
						mapViewer.getVertexBufferObjectManager()),
				new org.andengine.util.color.Color(1.0f, 0.0f, 0.0f),
				new org.andengine.util.color.Color(0.0f, 0.0f, 0.0f));
		backMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(infoMenuItem);

		final IMenuItem configMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(mapViewer.MENU_ITEM_CONFIG, mapViewer.mFont_menu, mapViewer.getResources()
						.getString(R.string.menu_config),
						mapViewer.getVertexBufferObjectManager()),
				new org.andengine.util.color.Color(1.0f, 0.0f, 0.0f),
				new org.andengine.util.color.Color(0.0f, 0.0f, 0.0f));
		configMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(configMenuItem);

		final IMenuItem exitMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(mapViewer.MENU_ITEM_EXIT, mapViewer.mFont_menu, mapViewer.getResources()
						.getString(R.string.menu_exit),
						mapViewer.getVertexBufferObjectManager()),
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
				case MapViewerActivity.MENU_ITEM_BACK:
					// Remove the menu
					mapViewer.mainScene.clearChildScene();
					return true;
				case MapViewerActivity.MENU_ITEM_INFO:
					// Show all available Information
					InfoBanner.showInfo(mapViewer);
					return true;
				case MapViewerActivity.MENU_ITEM_CONFIG:
					Intent openConfigIntent = new Intent(mapViewer, TunerActivity.class);
					mapViewer.startActivity(openConfigIntent);
					return true;
				case MapViewerActivity.MENU_ITEM_EXIT:
					// End Activity.
					MapViewerUtil.exitApp();
					return true;
				default:
					return false;
				}
			}
		});

		if (mapViewer.DEBUG)
			Log.d(mapViewer.TAG, "End createMenuScene");

		return menuScene;
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
	public static void startUpdateClockThread(final MapViewerActivity mapViewer) {
		if (mapViewer.mUpdateClockThread == null) {

			// Locate Me Periodically
			mapViewer.mUpdateClockThread = new Thread() {
				public void run() {
					while (true) { // Run forever
						if (!mapViewer.updateClockOn) {
							break; // Stop Thread on pause
						}
						
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							continue;
						}

						if (!mapViewer.updateClockOn) {
							break; // Stop Thread on pause
						}
						
						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
						String clockStr = sdf.format(new Date(System.currentTimeMillis()));
						if (mapViewer.mClockText != null) {
							mapViewer.mClockText.setText(clockStr);
						}
					}
				}
			};

			if (mapViewer.DEBUG)
				Log.d(mapViewer.TAG, "mUpdateClockThread starts.");

			mapViewer.mUpdateClockThread.start();
		} else {
			if (mapViewer.DEBUG)
				Log.d(mapViewer.TAG, "mUpdateClockThread already starts.");
		}
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
	
	/*
	private void createTabHost(MapViewerActivity mapViewer) {
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
	
}