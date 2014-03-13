package com.winjune.wifiindoor.drawing.graphic.model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.TextureManager;

import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.ads.AdvertiseUnit;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.util.VisualParameters;

import android.content.res.AssetManager;

/**
 * @author haleyshi
 * 
 */
public class Library {
	
	public static void initial(TextureManager textureManager,
			AssetManager assetManager){
		
		Unit.setAssetManager(assetManager);
		Unit.setTextureManager(textureManager);
		
		Field[] fields = Library.class.getFields();
		
		for (Field field : fields){
			if (Modifier.isStatic(field.getModifiers()) && Unit.class.isAssignableFrom(field.getType())){
				try {
					Unit unit = (Unit) field.get(null);
					unit.clearCache();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	//public static final BackGroundUnit BACKGROUND = new BackGroundUnit();
	//public static final BackGroundUnit2 BACKGROUND2 = new BackGroundUnit2();
	public static final BackGroundUnit3 BACKGROUND3 = new BackGroundUnit3();
	//public static final MapPictureUnit MAP_PICTURE = new MapPictureUnit();
	public static final AdvertiseUnit ADVERTISE = new AdvertiseUnit();
	
	/**
	 * Control
	 */
	//Menu
	public static final int CONTROL_BUTTON_NUMBER = 5;
	// Tab	
	public static final int TAB_BUTTON_NUMBER = 6;
	
	public static final AnimatedUnit BUTTON_LOCATE = new AnimatedUnit(
			new String[] { "svg/locate.svg" }, 0);
	
	public static final AnimatedUnit BUTTON_SCAN_QR = new AnimatedUnit(
			new String[] { "svg/qr_scan.svg" }, 0);
	
	public static final AnimatedUnit BUTTON_NAVI = new AnimatedUnit(
			new String[] { "svg/route.svg" }, 0);
	
	public static final AnimatedUnit BUTTON_MODE = new AnimatedUnit(
			new String[] { "svg/view.svg", "svg/edit.svg", "svg/nfc_edit.svg", "svg/delete.svg", "svg/test_locate.svg", "svg/test_collect.svg" }, 0);
	
	public static final AnimatedUnit BUTTON_ZOOM = new AnimatedUnit(
			new String[] { "svg/zoomMostIn.svg", "svg/zoomMostOut.svg" }, 0);
	
	public static final AnimatedUnit BUTTON_ACTION = new AnimatedUnit(
			new String[] { "svg/action.svg" }, 0);	
		
	public static final AnimatedUnit BUTTON_STAR = new AnimatedUnit(
			new String[] { "svg/star.svg" }, 0);
	
	public static final AnimatedUnit BUTTON_MAP = new AnimatedUnit(
			new String[] { "svg/stairs.svg" }, 0);
	
	public static final AnimatedUnit BUTTON_MSG = new AnimatedUnit(
			new String[] { "svg/msg.svg" }, 0);	
	
	public static final AnimatedUnit BUTTON_SEARCH = new AnimatedUnit(
			new String[] { "svg/search.svg" }, 0);	
	
	/**
	 * User, occupy 1 * 1 Cells
	 */
	public static final AnimatedUnit LOCATION_USER = new AnimatedUnit(new String[] { "svg/location.svg" }, 0);	
	public static final AnimatedUnit TARGET_USER = new AnimatedUnit(new String[] { "svg/target.svg" }, 0);
	public static final AnimatedUnit TRACK_USER = new AnimatedUnit(new String[] { "svg/point.svg" }, 0);
	
	public static AnimatedSprite genUser(MapViewerActivity activity, int userType, int cellPixel){
		AnimatedSprite sprite = null;
		
		switch (userType){
			case Constants.LOCATION_USER:
				sprite = Library.LOCATION_USER.load(activity, cellPixel, cellPixel);
				break;
			case Constants.TARGET_USER:
				sprite = Library.TARGET_USER.load(activity, cellPixel, cellPixel);
				break;
			case Constants.TRACK_USER:
				sprite = Library.TRACK_USER.load(activity, cellPixel, cellPixel);
				break;
			default:
		}
		
		return sprite;
	}
	
	/**
	 * Interest Place, occupy 1 * 1 Cells
	 */
	public static final AnimatedUnit INTEREST_PLACE = new AnimatedUnit(new String[] { "svg/star.svg" }, 0);
	public static final AnimatedUnit INTEREST_PLACE_FOR_IE = new AnimatedUnit(new String[] { "svg/ie.svg" }, 0);
	public static final AnimatedUnit INTEREST_PLACE_FOR_PIC = new AnimatedUnit(new String[] { "svg/pic.svg" }, 0);
	public static final AnimatedUnit INTEREST_PLACE_FOR_SPEECH = new AnimatedUnit(new String[] { "svg/speech.svg" }, 0);

	/**
	 * Search Place, occupy 1 * 1 Cells
	 */
	public static final AnimatedUnit SEARCH_PLACE = new AnimatedUnit(new String[] { "svg/locate.svg" }, 0);
	
	/**
	 * Map CELL
	 */
	/*
	public static final AnimatedUnit CELL_ENTRY = new AnimatedUnit(new String[] { "svg/entry.svg" }, 0);
	
	public static final AnimatedUnit CELL_EXIT = new AnimatedUnit(new String[] { "svg/exit.svg" }, 0);
	
	public static final AnimatedUnit CELL_OBSTACLE = new AnimatedUnit(new String[] { "svg/obstacle.svg" }, 0);
	
	public static final AnimatedUnit CELL_PATH = new AnimatedUnit(new String[] { "svg/path.svg" }, 0);	
	
	public static AnimatedSprite genCell(MapViewerActivity activity, int cell_status, int cellPixel){
		AnimatedSprite cellSprite = null;

		switch (cell_status) {
		case IndoorMapData.PUBLIC_PATH:
			cellSprite = Library.CELL_PATH.load(activity, cellPixel);
			break;
		case IndoorMapData.NO_WAY:
			cellSprite = Library.CELL_OBSTACLE.load(activity, cellPixel);
			break;
		default:
		}
		
		return cellSprite;
	}
	*/
	
	/**
	 * Collected flag, attached when the fingleprint of one cell is collected. Occupy 1 * 1 Cells
	 * Added by Derek
	 */
	public static Rectangle genFlag (MapViewerActivity activity, final float pX, final float pY) {
		
		int cellPixel = Util.getRuntimeIndoorMap().getCellPixel();
		
		Rectangle flag = new Rectangle(pX, pY, cellPixel, cellPixel, activity.getVertexBufferObjectManager());
		flag.setColor(0, 1, 0); // Green color
		flag.setAlpha(VisualParameters.MAP_PIC_ALPHA);
		
		return flag;
	}
}
