package com.ericsson.cgc.aurora.wifiindoor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ericsson.cgc.aurora.wifiindoor.map.InterestPlace;
import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;

public class InterestPlaceViewerActivity extends Activity {	
	private OnClickListener listener2;
	
	@Override
	protected void onResume() {
		super.onResume();
		Util.setEnergySave(false);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Util.setEnergySave(true);
	}
	
    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(getApplicationContext());
        scroll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView textInfo = new TextView(getApplicationContext());
        textInfo.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textInfo.setAutoLinkMask(Linkify.ALL);
        textInfo.setTextAppearance(getApplicationContext(), android.R.attr.textAppearanceSmall);
        
        Bundle bundle = getIntent().getExtras();
		InterestPlace place = (InterestPlace) bundle.getSerializable(IndoorMapData.BUNDLE_KEY_INTEREST_PLACE_INSTANCE);
		
		if (place != null) {
			String text = place.getInfo();
			String picture = place.getUrlPic();
			
			// Display Text
			if ( text != null) {
				textInfo.setText(text);
				layout.addView(textInfo);
			}
			
			if ((picture != null) && (!picture.trim().isEmpty())) {
				String[] pictures = picture.split(";");
				
				listener2 = new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						return;
					}					
				};
				
				for (int i=0; i<pictures.length; i++) {
					if (pictures[i].trim().isEmpty()) {
						continue;
					}
					
					final ImageView imageInfo = new ImageView(getApplicationContext());
					imageInfo.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
					imageInfo.setImageResource(R.drawable.click_here);
					
					final String IMG_URL= Util.fullUrl(IndoorMapData.IMG_FILE_PATH_REMOTE, pictures[i]);
					
					OnClickListener listener1 = new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							getBitmapFromUrl(imageInfo, IMG_URL);
						}
						
					};
					
					imageInfo.setOnClickListener(listener1);
										  					
					layout.addView(imageInfo);
				}
			}
		} else {
			textInfo.setText(R.string.no_description);
			layout.addView(textInfo);
		}		
		
		scroll.addView(layout);
		setContentView(scroll);
    }
    
    private void getBitmapFromUrl(final ImageView imageInfo, final String imgUrl) {
    	new Thread() {
    		public void run() {   			
		        Bitmap bitmap = null;
		        try {
		        	URL url = new URL(imgUrl);
		            InputStream is = url.openConnection().getInputStream();
		            BufferedInputStream bis = new BufferedInputStream(is);
		            bitmap = BitmapFactory.decodeStream(bis);
		            bis.close();
		        } catch (MalformedURLException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (Exception e) {
		            e.printStackTrace();
		        } 
		        
		        final Bitmap bitmap2 = bitmap;
		        
		        runOnUiThread(new Runnable() {
					public void run() {
						if (bitmap2 == null) {
							imageInfo.setImageResource(R.drawable.no_pic);	
						} else {
							imageInfo.setImageBitmap(bitmap2);
							imageInfo.setOnClickListener(listener2);
						}
						imageInfo.invalidate();		    
					}
				});		        
    		}
    	}.start();
    }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}