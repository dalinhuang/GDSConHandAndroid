package com.winjune.wifiindoor.activity;

import java.util.List;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.R.id;
import com.winjune.wifiindoor.R.layout;
import com.winjune.wifiindoor.R.string;
import com.winjune.wifiindoor.map.InterestPlace;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.ShareUtil;
import com.winjune.wifiindoor.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle; 
import android.view.KeyEvent; 
import android.view.View;
import android.webkit.WebView; 
import android.webkit.WebViewClient;
import android.widget.Button;

public class InterestPlaceWebViewActivity extends Activity {
    private WebView webview;
    private String url = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.interest_place_webview);
        
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
				ShareUtil.shareToWeibo(InterestPlaceWebViewActivity.this, url);
				
				return false;
			}
		});
        
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
    
    class myWebViewClient extends WebViewClient{ 

    	//override the webview client to not use the 3rd browser
    	@Override 
     	public boolean shouldOverrideUrlLoading(WebView view, String url) { 
    		view.loadUrl(url); 
    		return true; 
     	}  
    }
}
