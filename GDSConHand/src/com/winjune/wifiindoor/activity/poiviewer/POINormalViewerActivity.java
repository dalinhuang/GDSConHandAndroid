package com.winjune.wifiindoor.activity.poiviewer;

import com.winjune.wifiindoor.R;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class POINormalViewerActivity extends POIBaseActivity implements OnTouchListener {
	public static final String TAG = POINormalViewerActivity.class.getSimpleName();
	
	ImageView imagePager;
	
	private int[] arrayPictures = { R.drawable.guide_1, R.drawable.guide_2, 
            R.drawable.guide_3};
	
    // 要显示的图片在图片数组中的Index  
    private int pictureIndex; 
    // 左右滑动时手指按下的X坐标  
    private float touchDownX; 
    // 左右滑动时手指松开的X坐标  
    private float touchUpX; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_normal);
		
		imagePager = (ImageView) this.findViewById(R.id.image_slide);
		imagePager.setOnTouchListener(this);
		
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
                pictureIndex = pictureIndex == 0 ? arrayPictures.length - 1 
                        : pictureIndex - 1; 
                // 设置当前要看的图片  
                imagePager.setImageResource(arrayPictures[pictureIndex]); 
                // 从右往左，看下一张  
            } else if (touchDownX - touchUpX > 100) { 
                // 取得当前要看的图片的index  
                pictureIndex = pictureIndex == arrayPictures.length - 1 ? 0 
                        : pictureIndex + 1; 

                // 设置当前要看的图片  
                imagePager.setImageResource(arrayPictures[pictureIndex]); 
            } 
            return true; 
        } 
        return false; 
    
    }
}
