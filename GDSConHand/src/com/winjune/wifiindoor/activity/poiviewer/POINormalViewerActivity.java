package com.winjune.wifiindoor.activity.poiviewer;

import java.io.File;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.poi.POIManager;
import com.winjune.wifiindoor.util.Constants;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class POINormalViewerActivity extends POIBaseActivity implements OnTouchListener {
	public static final String TAG = POINormalViewerActivity.class.getSimpleName();
	
	ImageView imagePager;
	
	private String[] imgs;
	
    private int imgIdx;  
    private float touchDownX;  
    private float touchUpX; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_normal);

        // retrieve URL
		Bundle bundle = getIntent().getExtras();
		poiId = bundle.getInt(Constants.BUNDLE_KEY_POI_ID);
		poi = POIManager.getPOIbyId(poiId);
		
		updateTitleInfo();		
		
		imagePager = (ImageView) this.findViewById(R.id.image_slide);
		
		if ((poi.picUrl != null) && (!poi.picUrl.trim().isEmpty())){
			imgs = poi.picUrl.trim().split(";");									
			imagePager.setOnTouchListener(this);
		} else {// detach image viewer	
			ViewGroup vg = (ViewGroup)imagePager.getParent();
			
			vg.removeView(imagePager);
		}
		
		setupContentButton();		
	}
	
    @Override 
    public boolean onTouch(View v, MotionEvent event) { 
        if (event.getAction() == MotionEvent.ACTION_DOWN) { 
            // 取得左右滑动时手指按下的X坐标  
            touchDownX = event.getX(); 
            return true; 
        } else if (event.getAction() == MotionEvent.ACTION_UP) { 
            // 取得左右滑动时手指松开的X坐标  
            touchUpX = event.getX(); 
            // 从左往右，看前一张  
            if (touchUpX - touchDownX > 100) { 
                // 取得当前要看的图片的index  
                imgIdx = imgIdx == 0 ? imgs.length - 1 
                        : imgIdx - 1; 
                // 设置当前要看的图片  
                // imagePager.setImageResource(imgs[imgIdx]); 
                // 从右往左，看下一张  
            } else if (touchDownX - touchUpX > 100) { 
                // 取得当前要看的图片的index  
                imgIdx = imgIdx == imgs.length - 1 ? 0 
                        : imgIdx + 1; 

                // 设置当前要看的图片  
                // imagePager.setImageResource(imgs[imgIdx]); 
            } 
            return true; 
        } 
        return false;     
    }
      
    private  void loadAndCachedImg(final ImageView imageInfo, final String imgFileName){
       	new Thread() {
    		public void run() {   			
    	    	String localFilePath = Util.getFilePath(IndoorMapData.IMG_FILE_PATH_LOCAL);
    	    	String fullFileName = localFilePath + imgFileName;
    	    	final String imgURL= Util.fullUrl(IndoorMapData.IMG_FILE_PATH_LOCAL, imgFileName);
    			File   file = new File (fullFileName);
    			 
    	    	if (!file.exists()) {
    	    		//The file doesn't exist, download the file to the cache folder
    	    		
    	    		Util.downFile(POINormalViewerActivity.this, imgURL, IndoorMapData.IMG_FILE_PATH_LOCAL, imgFileName,                     		
    	    							false,      // Open after download
    	    							"",
    	    							false, //useHandler
    	    							false);// Use Thread	

    			 }
    	    	
    	    	final Bitmap bitmap = BitmapFactory.decodeFile(fullFileName);
    				        
		        runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap == null) {
							imageInfo.setImageResource(R.drawable.no_pic);	
						} else {
							imageInfo.setImageBitmap(bitmap);
						}
						imageInfo.invalidate();		    
					}
				});		        
    		}
    	}.start();
    }    
    
}
