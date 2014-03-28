package com.winjune.wifiindoor.mapviewer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.debug.Debug;
import org.json.JSONObject;

import android.widget.Toast;

import com.google.gson.Gson;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.map.FieldInfo;
import com.winjune.wifiindoor.map.MapInfo;
import com.winjune.wifiindoor.util.ImageButtonSprite;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.VersionOrMapIdRequest;

public class ControlButtonsHUD {

	public static void createBottomButtons(MapViewerActivity mapViewer, ArrayList<IconButtonInfo> buttonInfoList, float pX, float pY, float pWidth, float pHeight) {
//        float pX = 10;
//        float pY = 700;
//        float pWidth = 520;
//        float pHeight = 60;
        float lineWidth = 4;
        float color = 0.95f;
        int buttonCount = buttonInfoList.size();
		Rectangle coloredRect = new Rectangle(pX, pY, pWidth, pHeight, mapViewer.getVertexBufferObjectManager());
//		coloredRect.setColor(color, color, color);
		Line lineTop = new Line(0, 0, pWidth, 0, lineWidth, mapViewer.getVertexBufferObjectManager());
		lineTop.setColor(color, color, color);
		Line lineBottom = new Line(0, pHeight, pWidth, pHeight, lineWidth, mapViewer.getVertexBufferObjectManager());
		lineBottom.setColor(color, color, color);
		Line lineLeft = new Line(0, 0, 0, pHeight, lineWidth, mapViewer.getVertexBufferObjectManager());
		lineLeft.setColor(color, color, color);
		Line lineRight = new Line(pWidth, 0, pWidth, pHeight, lineWidth, mapViewer.getVertexBufferObjectManager());
		lineRight.setColor(color, color, color);
		
		float buttonWidth = pWidth/buttonCount;
		
		coloredRect.attachChild(lineTop);
		coloredRect.attachChild(lineBottom);
		coloredRect.attachChild(lineLeft);
		coloredRect.attachChild(lineRight);
		
		BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mapViewer.getTextureManager(), 512, 512);
		ITextureRegion backgroudNormal = BitmapTextureAtlasTextureRegionFactory.createFromResource(mBitmapTextureAtlas, mapViewer, R.drawable.button_background_normal);
		ITextureRegion backgroudPressed = BitmapTextureAtlasTextureRegionFactory.createFromResource(mBitmapTextureAtlas, mapViewer, R.drawable.button_background_pressed);
		
		for (int i = 0;i<buttonCount;i++) {
			IconButtonInfo iconButtonInfo = buttonInfoList.get(i);
			if (iconButtonInfo != null) {
				ITextureRegion iconRegion = BitmapTextureAtlasTextureRegionFactory.createFromResource(mBitmapTextureAtlas, mapViewer, iconButtonInfo.getIconResourceId());
				iconButtonInfo.setIconRegion(iconRegion);
			}
		}

		try {
			mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(10, 20, 30));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}

		for (int i = 0;i<buttonCount;i++) {
			IconButtonInfo iconButtonInfo = buttonInfoList.get(i);
			if (iconButtonInfo != null) {
				ImageButtonSprite textButton = new ImageButtonSprite(buttonWidth*i, 0,
						backgroudNormal, backgroudPressed,iconButtonInfo.getIconRegion(),
						mapViewer.getVertexBufferObjectManager(), null, iconButtonInfo.getText(),
						mapViewer.mFont_mapinfo, buttonWidth, pHeight, 1.2f, 1.2f, -12f);
				coloredRect.attachChild(textButton);
				mapViewer.hud.registerTouchArea(textButton);
			}
		}


/*		ITextureRegion iconNearby = BitmapTextureAtlasTextureRegionFactory.createFromResource(mBitmapTextureAtlas, mapViewer, R.drawable.nearby);
		ITextureRegion iconRoute = BitmapTextureAtlasTextureRegionFactory.createFromResource(mBitmapTextureAtlas, mapViewer, R.drawable.route);
		ITextureRegion iconSearch = BitmapTextureAtlasTextureRegionFactory.createFromResource(mBitmapTextureAtlas, mapViewer, R.drawable.find);
		ITextureRegion iconMine = BitmapTextureAtlasTextureRegionFactory.createFromResource(mBitmapTextureAtlas, mapViewer, R.drawable.mine);
*/		
/*		TiledTextureRegion tiledTextureRegion1 = new TiledTextureRegion(mFace1TextureRegion.getTexture(),
				mFace1TextureRegion, mFace2TextureRegion);
		
		ImageButtonSprite textButton1 = new ImageButtonSprite(0, 0,
				tiledTextureRegion1,
				mapViewer.getVertexBufferObjectManager(), "测试",
				mapViewer.mFont_mapinfo, -40);
		TiledTextureRegion tiledTextureRegion2 = new TiledTextureRegion(mFace1TextureRegion.getTexture(),
				mFace1TextureRegion, mFace2TextureRegion);
		
		textButton1.setScale(0.5f);
	    textButton1.setBlue(0f);
		textButton1.setSize(170, 60);
		ImageButtonSprite textButton2 = new ImageButtonSprite(220, 10,
				tiledTextureRegion2,
				mapViewer.getVertexBufferObjectManager(), "查找",
				mapViewer.mFont_mapinfo, -40);
		TiledTextureRegion tiledTextureRegion3 = new TiledTextureRegion(mFace1TextureRegion.getTexture(),
				mFace1TextureRegion, mFace2TextureRegion);
		
//		textButton1.setSize(100, 100);
*/		
/*		ImageButtonSprite textButton1 = new ImageButtonSprite(0, 0,
				backgroudNormal, backgroudPressed,iconNearby,
				mapViewer.getVertexBufferObjectManager(), null, "周围 ",
				mapViewer.mFont_mapinfo, buttonWidth, pHeight);
		ImageButtonSprite textButton2 = new ImageButtonSprite(buttonWidth, 0,
				backgroudNormal, backgroudPressed,iconRoute,
				mapViewer.getVertexBufferObjectManager(), null, "路线",
				mapViewer.mFont_mapinfo, buttonWidth, pHeight);
		ImageButtonSprite textButton3 = new ImageButtonSprite(buttonWidth*2, 0,
				backgroudNormal, backgroudPressed,iconSearch,
				mapViewer.getVertexBufferObjectManager(), null, "搜索",
				mapViewer.mFont_mapinfo, buttonWidth, pHeight);
		ImageButtonSprite textButton4 = new ImageButtonSprite(buttonWidth*3, 0,
				backgroudNormal, backgroudPressed,iconMine,
				mapViewer.getVertexBufferObjectManager(), null, "我的",
				mapViewer.mFont_mapinfo, buttonWidth, pHeight);*/

		mapViewer.hud.attachChild(coloredRect);
/*		coloredRect.attachChild(textButton1);
		coloredRect.attachChild(textButton2);
		coloredRect.attachChild(textButton3);
		coloredRect.attachChild(textButton4);
		mapViewer.hud.registerTouchArea(textButton1);
		mapViewer.hud.registerTouchArea(textButton2);
		mapViewer.hud.registerTouchArea(textButton3);
		mapViewer.hud.registerTouchArea(textButton4);
		mapViewer.hud.setTouchAreaBindingOnActionDownEnabled(true);
*///		for (int i=0;i<buttonCount-1;i++)
//		{
//		}
		for (int i=1;i<buttonCount;i++)
		{
			Line lineSplit = new Line(buttonWidth*i, 10, buttonWidth*i, 50, 2, mapViewer.getVertexBufferObjectManager());
			lineSplit.setColor(color, color, color);
			coloredRect.attachChild(lineSplit);			
		}
/*		Line lineSplit1 = new Line(buttonWidth, 10, buttonWidth, 50, 2, mapViewer.getVertexBufferObjectManager());
		lineSplit1.setColor(color, color, color);
		coloredRect.attachChild(lineSplit1);
		Line lineSplit2 = new Line(2*buttonWidth, 10, 2*buttonWidth, 50, 2, mapViewer.getVertexBufferObjectManager());
		lineSplit2.setColor(color, color, color);
		coloredRect.attachChild(lineSplit2);
		Line lineSplit3 = new Line(3*buttonWidth, 10, 3*buttonWidth, 50, 2, mapViewer.getVertexBufferObjectManager());
		lineSplit3.setColor(color, color, color);
		coloredRect.attachChild(lineSplit3);
*/
		}
	
}
