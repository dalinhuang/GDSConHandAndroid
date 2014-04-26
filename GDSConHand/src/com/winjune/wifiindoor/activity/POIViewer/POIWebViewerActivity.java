package com.winjune.wifiindoor.activity.POIViewer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.map.InterestPlace;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.ShareUtil;

public class POIWebViewerActivity extends Activity {
    private WebView webview;
    private String url = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_interest_place_webview);
        
        //
        webview = (WebView) findViewById(R.id.interest_place);

        // enable JavaScrip  
        //webview.getSettings().setJavaScriptEnabled(true); 
        webview.setWebViewClient(new myWebViewClient());
        
        // retrieve URL
        Bundle bundle = getIntent().getExtras();        
        InterestPlace place = (InterestPlace) bundle.getSerializable(IndoorMapData.BUNDLE_KEY_INTEREST_PLACE_INSTANCE);        	
        if (place != null) {        	        
        	url = place.getUrlVideo();        	
        }    	
        
        webview.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				ShareUtil.shareToWeibo(POIWebViewerActivity.this, url);
				
				return false;
			}
		});
        
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean bDownloadPic = sharedPrefs.getBoolean("download_picture", true);
        webview.getSettings().setLoadsImagesAutomatically(bDownloadPic);

        webview.loadUrl(url); 

    } 
     
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) { 
            webview.goBack(); //goBack() mean return last page 
            return true; 
        } 
        
        return super.onKeyDown(keyCode, event); 
	
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.interest_place_web, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items    
				switch (item.getItemId()) {        
					case R.id.action_share:    
						ShareUtil.shareToWeibo(this, url);
						return true;                
					default:            
						return super.onOptionsItemSelected(item);    
				}
	}
    
    class myWebViewClient extends WebViewClient{ 

    	//override the webview client to not use the 3rd browser
    	@Override 
     	public boolean shouldOverrideUrlLoading(WebView view, String url) { 
    		view.loadUrl(url); 
    		return true; 
     	}  
    }
}
