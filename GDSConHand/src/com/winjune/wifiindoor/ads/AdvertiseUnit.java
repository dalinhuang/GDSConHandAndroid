package com.winjune.wifiindoor.ads;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.FileBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.TextureRegion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.winjune.wifiindoor.activity.MapViewerActivity;
import com.winjune.wifiindoor.drawing.graphic.model.Unit;
import com.winjune.wifiindoor.runtime.RuntimeMap;
import com.winjune.wifiindoor.util.AdData;
import com.winjune.wifiindoor.util.AdUtil;
import com.winjune.wifiindoor.util.VisualParameters;

public class AdvertiseUnit extends Unit {
    
	private AnimatedSprite sprite;
	
	private String AdPictureName;
	
	private String Urls;
	private TextureRegion textureRegion;
	
	public AdvertiseUnit(){
	}
	
	public AdvertiseUnit(String Urls, int width, int height,
			int initialRotation) {
		super(width, height, initialRotation);
		setUrls(Urls);
	}
	
	public AdvertiseUnit(String Urls, int initialRotation, String AdPictureName) {
		super(initialRotation);
		setUrls(Urls);
		setAdPictureName(AdPictureName);
	}


	public String getUrls() {
		return Urls;
	}

	public void setUrls(String Urls) {
		this.Urls = Urls;
	}
	
	public String getAdPictureName() {
		return AdPictureName;
	}

	public void setAdPictureName(String AdPictureName) {
		this.AdPictureName = AdPictureName;
	}
	

	@Override
	public void clearCache() {
		super.clearCache();

		textureRegion = null;
	}
/*
	public AnimatedSprite load(BaseGameActivity activity) {
		return load(activity, null);
	}
	
	public AnimatedSprite load(BaseGameActivity activity, int widthPixels, int heightPixels) {
		setWidth(widthPixels);
		setHeight(heightPixels);
		
		return load(activity, null);
	}
	*/
	

	public Sprite load(MapViewerActivity activity, RuntimeMap runtimeIndoorMap,final AdSpriteListener spriteListener, boolean default_ad) {
        // TODO: why hard-code?
		int mapWidth = 1000;
		int mapHeight = 400;
       
		if (textureRegion == null) {
			//File file = new File(Util.getMapPicturePathName(""+runtimeIndoorMap.getMapId(), runtimeIndoorMap.getMapPictureName()));	
     
			File file = new File(AdUtil.getAdPicturePathName(""+runtimeIndoorMap.getMapId(), getAdPictureName()));
			
		    if (default_ad){
	        	  file = AdData.FILE_DEFAULT_AD;
	         }
			
			if (!file.exists()) {
				while (AdUtil.isAdDownloadOngoing()) {
					//wait
				}
				// TODO: Download this file
				//Util.downloadMapPicture(activity, ""+runtimeIndoorMap.getMapId(), getAdPictureName());
				while (AdUtil.isAdDownloadOngoing()) {
					//wait for download finish
				}
			}
			
			if (!file.exists()) { 
				Log.i("Ad Load", "file " + file.getPath() + " is null!");
				return null;
			}
			
			if (activity == null) { 
				Log.i("Ad Load", "activity is still null!");
				return null;
			}
			
			if (activity.getTextureManager() == null) {  // Too Early
				Log.i("Ad Load", "activity.getTextureManager() is still null!");
				return null;
			}
			
			BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), getNearestPowerOfTwo(mapWidth), getNearestPowerOfTwo(mapHeight), TextureOptions.BILINEAR);
			FileBitmapTextureAtlasSource fileBitmapTextureAtlasSource = FileBitmapTextureAtlasSource.create(file);  
			textureRegion = (TextureRegion) BitmapTextureAtlasTextureRegionFactory.createFromSource(textureAtlas, fileBitmapTextureAtlasSource, 0, 0);  
			
			textureAtlas.load();
		}

	    Sprite sprite=new Sprite(mapWidth,mapHeight,textureRegion, activity.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (spriteListener != null) {
					return (spriteListener.onAreaTouched(this, pSceneTouchEvent,pTouchAreaLocalX, pTouchAreaLocalY));
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		
	    sprite.setAlpha(VisualParameters.AD_PIC_ALPHA);

		return sprite;
		
		
		
	}
	
    public Bitmap returnBitMap(String url) {   
  	   URL myFileUrl = null;   
  	   Bitmap bitmap = null;   
  	   try {   
  	    myFileUrl = new URL(url);   
  	   } catch (MalformedURLException e) {   
  	    e.printStackTrace();   
  	   }   
  	   try {   
  	    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();   
  	    conn.setDoInput(true);   
  	    conn.setConnectTimeout(2000);
  	    
  	    System.out.println("got here");
  	    //int responseCode=conn.getResponseCode();   
  	    conn.connect();   
  	   System.out.println(conn.getResponseCode());
  	   System.out.println("got here");
  	    InputStream is = conn.getInputStream();   
  	    bitmap = BitmapFactory.decodeStream(is);   
  	    is.close();   
  	   } catch (IOException e) {   
  	    e.printStackTrace();   
  	   }   
  	   return bitmap;   
  	} 

	public AnimatedSprite getSprite() {
		return sprite;
	}

	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}
	
	public void resetAdUnit(){
		textureRegion=null;
		
	}
	

	

}
