package com.winjune.wifiindoor;

import android.app.Activity;
import android.os.Bundle; 
import android.view.KeyEvent; 
import android.webkit.WebView; 
import android.webkit.WebViewClient;

public class InterestPlaceWebViewActivity extends Activity {
    private WebView webview; 
    
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        // 
        webview = new WebView(this); 

        // enable JavaScrip  
        webview.getSettings().setJavaScriptEnabled(true); 
        webview.setWebViewClient(new myWebViewClient());
        
        //
        webview.loadUrl("http://www.baidu.com/"); 

        setContentView(webview); 
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
