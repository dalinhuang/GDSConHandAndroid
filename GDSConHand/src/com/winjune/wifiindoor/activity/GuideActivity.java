package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.R.menu;
import com.winjune.wifiindoor.util.IndoorMapData;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity implements OnPageChangeListener, OnClickListener {

	private ViewPager mViewPager;
	private ViewPagerAdapter mVPAdapter;
	private List<View> mViews;
	
	private final static int[] mPics = {R.drawable.guide_1,
		R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4};
	private ImageView[] mDots;
	private Button start;
	
	private int mCurrentIndex;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		
        mViews = new ArrayList<View>();       

        LinearLayout.LayoutParams mParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);     

        //Initialize the guide pictures
        for(int i=0; i<mPics.length; i++) {

            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
           // iv.getBackground().setAlpha(0);
            iv.setImageResource(mPics[i]);
            mViews.add(iv);

        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        //Initialize Adapter
        mVPAdapter = new ViewPagerAdapter(mViews);
        mViewPager.setAdapter(mVPAdapter);

        mViewPager.setOnPageChangeListener(this);    

        initDots();
        
        start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		    	Intent intent = new Intent(GuideActivity.this, MapViewerActivity.class);
		    	startActivity(intent);
		        finish();
			}
		});
        start.setVisibility(View.INVISIBLE);
        
	}
	
	private void initDots() {
		
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);      

        mDots = new ImageView[mPics.length];

        for (int i = 0; i < mPics.length; i++) {

            mDots[i] = (ImageView) ll.getChildAt(i);
            mDots[i].setEnabled(false);// Set the gray
            mDots[i].setOnClickListener(this);
            mDots[i].setTag(i);

        }

        mCurrentIndex = 0;

        mDots[mCurrentIndex].setEnabled(true);//set to selected state

	}
	
    private void setCurView(int position)
    {
        if (position < 0 || position >= mPics.length) {

            return;

        }

        mViewPager.setCurrentItem(position);

    }

    private void setCurDot(int positon)

    {
        if (positon < 0 || positon > mPics.length - 1 || mCurrentIndex == positon) {

            return;

        }

        mDots[positon].setEnabled(true);

        mDots[mCurrentIndex].setEnabled(false);

        mCurrentIndex = positon;

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.guide, menu);
		return true;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setCurDot(arg0);
		
		if (arg0 == mPics.length - 1) {
			start.setVisibility(View.VISIBLE);
		}
		else {
			start.setVisibility(View.INVISIBLE);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        int position = (Integer)v.getTag();

        setCurView(position);

        setCurDot(position);

	}
	
    public class ViewPagerAdapter extends PagerAdapter{

        private List<View> views;   

        public ViewPagerAdapter (List<View> views){

            this.views = views;

        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {

            ((ViewPager) arg0).removeView(views.get(arg1));       

        }

        @Override
        public void finishUpdate(View arg0) {

            // TODO Auto-generated method stub     

        }

        @Override
        public int getCount() {

            if (views != null)
            {
                return views.size();
            }         

            return 0;

        }
 
        @Override
        public Object instantiateItem(View arg0, int arg1) {

            ((ViewPager) arg0).addView(views.get(arg1), 0);         

            return views.get(arg1);

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return (arg0 == arg1);

        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {

            // TODO Auto-generated method stub

            return null;

        }

        @Override
        public void startUpdate(View arg0) {

            // TODO Auto-generated method stub    

        }

    } // ViewPagerAdapter

}
