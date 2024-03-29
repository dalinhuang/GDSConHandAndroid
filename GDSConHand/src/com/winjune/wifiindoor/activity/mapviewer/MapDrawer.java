package com.winjune.wifiindoor.activity.mapviewer;

import java.util.Set;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import android.util.Log;

import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.Library;
import com.winjune.wifiindoor.drawing.graphic.model.MapPieceSprite;
import com.winjune.wifiindoor.drawing.graphic.model.MapPieceUnit;
import com.winjune.wifiindoor.runtime.MapResource;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.MathUtil;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;

public class MapDrawer {
	
	
	public static void zoomInMap(final MapViewerActivity mapViewer){
		
		if (!VisualParameters.ZOOM_SWITCH_ENABLED)
			return;
		
		// store the center position
		final float centerX = mapViewer.mCamera.getCenterX();
		final float centerY = mapViewer.mCamera.getCenterY();

		if (Util.getRuntimeMap().zoomInMap()){
			
			mapViewer.runOnUpdateThread(new Runnable() {
				@Override
					public void run() {
					
					switchMapPrepare(mapViewer);			
					switchMapExcute(mapViewer);
					
					// set to the corresponding zoom factor
					mapViewer.mCamera.setZoomFactor(Util.getRuntimeMap().getNormal2LargeZoomFactor());
					
					// calculate the center position according to the scale
					// set to the old center position
					float adjustedX = centerX * Util.getRuntimeMap().getLarge2Normalcale();
					float adjustedY = centerY * Util.getRuntimeMap().getLarge2Normalcale();
					mapViewer.mCamera.setCenter(adjustedX, adjustedY);
	            }        
	        }); 						
		}
		
	}
	
	public static void zoomOutMap(final MapViewerActivity mapViewer){
		if (!VisualParameters.ZOOM_SWITCH_ENABLED)
			return;		
		
		final float centerX = mapViewer.mCamera.getCenterX();
		final float centerY = mapViewer.mCamera.getCenterY();
		
		if (Util.getRuntimeMap().zoomOutMap()){
			
			mapViewer.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {					
					
					switchMapPrepare(mapViewer);
					switchMapExcute(mapViewer);
					// set to the corresponding zoom factor
					mapViewer.mCamera.setZoomFactor(Util.getRuntimeMap().getLarge2NormalZoomFactor());
					// calculate the center position according to the scale
					// set to the old center position
					float adjustedX = centerX * Util.getRuntimeMap().getNormal2LargeScale();
					float adjustedY = centerY * Util.getRuntimeMap().getNormal2LargeScale();
					mapViewer.mCamera.setCenter(adjustedX, adjustedY);	
	            }        
	        }); 						
		
		}
		
	}
	
	public static void switchMapPrepare(MapViewerActivity mapViewer) {
		// to do: dispose all sprite resources
		
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_BACKGROUND).detachChildren();
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_MAP).detachChildren();
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_FLAG).detachChildren();
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_USER).detachChildren();
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_SEARCH).detachChildren();
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_ROUTE).detachChildren();		
				
		POIBar.clearPoiInfo(mapViewer);
		
		SearchBar.clearLocationPlaces(mapViewer);
				
		CollectedFlag.clearCollectedFlag(mapViewer);
		
		NaviBar.clearNaviInfo(mapViewer);
				
	}
	
	public static void switchMapExcute(MapViewerActivity mapViewer) {	
		
		int mapWidth = Util.getRuntimeMap().getMapWidth();
		int mapHeight = Util.getRuntimeMap().getMapHeight();

		MapViewerUtil.resetCameraBounds(mapViewer, mapWidth, mapHeight);
		
		mapViewer.zoomControl.resetZoomFactor(Util.getRuntimeMap().getMinZoomFactor(), 
											Util.getRuntimeMap().getMaxZoomFactor());
		
		mapViewer.reDrawPending = true;
		
		MapDrawer.drawBackground(mapViewer);

		drawMap(mapViewer);
		
		POIBar.showPoiInfo(mapViewer);
	}	
	
	public static void drawBackground(MapViewerActivity mapViewer){
		if (!VisualParameters.PLANNING_MODE_ENABLED)
			return;

		if (mapViewer.backgroundSprite != null){
			mapViewer.backgroundSprite.dispose();			
		}
		
		Library.BACKGROUND3.clearCache();
		
		mapViewer.backgroundSprite = Library.BACKGROUND3.load(mapViewer, 
									Util.getRuntimeMap().getMapWidth(), 
									Util.getRuntimeMap().getMapHeight());
		
		mapViewer.backgroundSprite.setPosition(mapViewer.LEFT_SPACE, mapViewer.TOP_SPACE);
		mapViewer.mainScene.getChildByIndex(Constants.LAYER_BACKGROUND).attachChild(mapViewer.backgroundSprite);
	}
	
	public static void setCameraCenterTo(MapViewerActivity mapViewer, int colNo, int rowNo, boolean fromMove) {
		float x = colNo; 
		float y = rowNo;
					
		if (colNo < Util.getRuntimeMap().getColNum()) {
			x += 0.5f;
		}
		
		if (rowNo < Util.getRuntimeMap().getRowNum()) {
			y += 0.5f;
		}
		
		float pCenterX = (x * Util.getRuntimeMap().getCellPixel() + mapViewer.LEFT_SPACE);
		float pCenterY = (y * Util.getRuntimeMap().getCellPixel() + mapViewer.TOP_SPACE);

		setCameraCenterAndReloadMapPieces(mapViewer, pCenterX, pCenterY, fromMove);
	}
	
	public static void setCameraCenterAndReloadMapPieces(MapViewerActivity mapViewer, float pCenterX, float pCenterY, boolean fromMove) {
		mapViewer.mCamera.setCenter(pCenterX, pCenterY);
	
		// Slow down the reDraw request from Move event
		if (fromMove) {
			mapViewer.reDrawPending = true;
			return;
		}
		
		mapViewer.reDrawPending = true;
		drawMap(mapViewer);
		
		POIBar.showPoiInfo(mapViewer);
	}
	
	public static void drawMap(final MapViewerActivity mapViewer) {		
		if (!mapViewer.reDrawPending) {
			return;
		}
		
		if (mapViewer.reDrawOngoing) {
			mapViewer.reDrawPending = false;
			return;
		}
		
		mapViewer.reDrawOngoing = true;		
		mapViewer.reDrawPending = false;
		
		//float zoomFactor = mCamera.getZoomFactor();
		float centerX = mapViewer.mCamera.getCenterX();  // re-calc for Center may not be the one passed in for the edge zones, already count in the zoomFactor
		float centerY = mapViewer.mCamera.getCenterY();  // re-calc for Center may not be the one passed in for the edge zones, already count in the zoomFactor
		float width = mapViewer.mCamera.getWidth();     // = cameraWidth / zoomFactor
		float height = mapViewer.mCamera.getHeight();   // = cameraWidth / zoomFactor
		
		//Log.i("Screen Passed in", pCenterX + "," + pCenterY + "," + cameraWidth + "," + cameraHeight);
		//Log.i("Screen Factors", centerX + "," + centerY + "," + width + "," + height + "," + zoomFactor);
		
		final float map_left = centerX - width / 2;
		final float map_top = centerY - height / 2;
		final float map_right = centerX + width / 2;
		final float map_bottom = centerY + height / 2;			
		
		Set<MapResource> resources = Util.getRuntimeMap().getResources().keySet();
		
		//Log.i("setCameraCenterAndReloadMapPieces", "Checking " + resources.size() + " map pieces");
		//Log.i("Screen", map_left + "," + map_top + "," + map_right + "," + map_bottom);
		
		for (final MapResource resource : resources) {
			if (resource == null) {
				Log.e("ERROR", "Piece with key=null");
				continue;
			}
			
			final float left = resource.getLeft() + mapViewer.LEFT_SPACE;
			final float top = resource.getTop() + mapViewer.TOP_SPACE;
			final float pic_width = resource.getWidth();
			final float pic_height = resource.getHeight();
			final float right = left + pic_width;
			final float bottom = top + pic_height;
			final String name = resource.getName();
			
			//Log.i("MapPiece", left + "," + top + "," + right + "," + bottom + "," + name);
			
			if ((name == null) || (name.isEmpty())){
				Log.e("ERROR", "Piece with name=" + name);
			}
			
			// Create bitmaps and Attach Spites on demand
			// 2 Rects has cross area
			MapPieceSprite mapPieceSprite = Util.getRuntimeMap().getResources().get(resource);
			if (MathUtil.hasCrossArea(map_left, map_top, map_right, map_bottom, left, top, right, bottom)) { // This peice should be displayed	
				if (mapPieceSprite == null) { // Create bitmap and sprite on-demand	
					mapPieceSprite = new MapPieceSprite();
					mapPieceSprite.setState(MapPieceSprite.PREPAREING);
					Util.getRuntimeMap().getResources().put(resource, mapPieceSprite); // Let the next round will not try to download/load this Sprite again
					
					new Thread() {
						@Override
						public void run() {
							MapPieceSprite currentPieceSprite = Util.getRuntimeMap().getResources().get(resource);						
							Sprite loadedMapPieceSprite = new MapPieceUnit().load(mapViewer, name, pic_width, pic_height);
							
							if (loadedMapPieceSprite == null) {
								Log.e("ERROR", "Fail to load piece, [" + left + "," + top + "," + right + "," + bottom + "], path=" + Util.getMapPicturePathName(Util.getRuntimeMap().getMapId()+"", name));
								Util.getRuntimeMap().getResources().put(resource, null);
								return;
							}
							
							Log.i("Screen", map_left + "," + map_top + "," + map_right + "," + map_bottom);
							Log.i("MapPiece", "Load map piece, [" + left + "," + top + "," + right + "," + bottom + "], path=" + Util.getMapPicturePathName(Util.getRuntimeMap().getMapId()+"", name));
							loadedMapPieceSprite.setPosition(left, top);
							
							currentPieceSprite.setSprite(loadedMapPieceSprite);			
							Util.getRuntimeMap().getResources().put(resource, currentPieceSprite);
							currentPieceSprite.setState(MapPieceSprite.READY);						
							
							mapViewer.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									MapPieceSprite currentPieceSprite = Util.getRuntimeMap().getResources().get(resource);
									if ((currentPieceSprite != null) && (currentPieceSprite.getState() == MapPieceSprite.READY)) {
										Sprite sprite = currentPieceSprite.getSprite();
										if (sprite != null) {
											if (!sprite.hasParent()) {  // For race-conditions, this sprite may be attached twice
												Log.i("MapPiece", "Attach map piece, [" + left + "," + top + "," + right + "," + bottom + "], path=" + Util.getMapPicturePathName(Util.getRuntimeMap().getMapId()+"", name));
												mapViewer.mainScene.getChildByIndex(Constants.LAYER_MAP).attachChild(sprite);
												mapViewer.mainScene.registerTouchArea(sprite);
												currentPieceSprite.setState(MapPieceSprite.ATTACHED);
											} else {
												Log.e("MapPiece", "Map piece has already been attahed, [" + left + "," + top + "," + right + "," + bottom + "], path=" + Util.getMapPicturePathName(Util.getRuntimeMap().getMapId()+"", name));
											}	
										}
									} else {
										Log.e("ERROR", "Fail to attach piece, [" + left + "," + top + "," + right + "," + bottom + "], path=" + Util.getMapPicturePathName(Util.getRuntimeMap().getMapId()+"", name));
									}
								}								
							});	
						}
					}.start();
				}
			} else {
				mapViewer.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						MapPieceSprite currentPieceSprite = Util.getRuntimeMap().getResources().get(resource);
						if ( currentPieceSprite != null && currentPieceSprite.getState() == MapPieceSprite.ATTACHED) { // destroy un-needed bitmaps / sprite
							Log.i("Screen", map_left + "," + map_top + "," + map_right + "," + map_bottom);
							Log.i("MapPiece", "Destory map piece [" + left + "," + top + "," + right + "," + bottom + "], path=" + Util.getMapPicturePathName(Util.getRuntimeMap().getMapId()+"", name));				
							
							Sprite sprite = currentPieceSprite.getSprite();					
							if (sprite != null) {
								if (sprite.hasParent()) {
									Log.i("MapPiece", "Detach map piece [" + left + "," + top + "," + right + "," + bottom + "], path=" + Util.getMapPicturePathName(Util.getRuntimeMap().getMapId()+"", name));
									mapViewer.mainScene.getChildByIndex(Constants.LAYER_MAP).detachChild(sprite);
									mapViewer.mainScene.unregisterTouchArea(sprite);
									sprite.dispose();
									Util.getRuntimeMap().getResources().put(resource, null);
									currentPieceSprite = null;
									sprite = null;
								}
							} else {
								Log.e("ERROR", "Piece has already been detached, [" + left + "," + top + "," + right + "," + bottom + "], path=" + Util.getMapPicturePathName(Util.getRuntimeMap().getMapId()+"", name));
							}
						} else {
							if (currentPieceSprite != null) {
								Util.getRuntimeMap().getResources().put(resource, null);
								currentPieceSprite = null;
								Log.e("WARNING", "Try to detach a piece has not been attached, [" + left + "," + top + "," + right + "," + bottom + "], path=" + Util.getMapPicturePathName(Util.getRuntimeMap().getMapId()+"", name));
							}
						}
					}
				});
			}
		}
				
		System.gc();
		
		mapViewer.reDrawOngoing = false;
	}
	
	public static void startRefreshMapThread(final MapViewerActivity mapViewer) {
		if (mapViewer.reDrawThread == null) {

			// Redraw Periodically
			mapViewer.reDrawThread = new Thread() {
				public void run() {
					while (true) { // Run forever
						if (!mapViewer.reDrawOn) {
							break;
						}
						
						try {
							sleep(2000);
						} catch (InterruptedException e) {
							continue;
						}

						drawMap(mapViewer);
					}
				}
			};

			mapViewer.reDrawThread.start();
		} else {
			if (mapViewer.DEBUG)
				Log.d(mapViewer.TAG, "reDrawThread already starts.");
		}
	}	
}
