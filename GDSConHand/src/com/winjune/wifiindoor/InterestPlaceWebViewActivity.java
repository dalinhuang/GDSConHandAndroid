package com.winjune.wifiindoor;

import java.util.List;

import com.winjune.wifiindoor.R;
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
    private Button share;
    private String url;
    
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.interest_place_webview);
        
        //
        webview = (WebView) findViewById(R.id.interest_place);
        share = (Button) findViewById(R.id.web_share);

        // enable JavaScrip  
        webview.getSettings().setJavaScriptEnabled(true); 
        webview.setWebViewClient(new myWebViewClient());
        
        //
        url = "http://www.baidu.com/";
        webview.loadUrl(url); 
        
        // Share Button
		
		share.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  			
    			Intent intent = new Intent(Intent.ACTION_SEND); // Intent to be sent to Sina Weibo APP
    			intent.setType("text/plain");
    			intent.putExtra(Intent.EXTRA_SUBJECT, R.string.share);
    			
    			// Add the text content to the intent
    			String prefix = getString(R.string.share_prefix);
    			
    			if ((prefix.length() + url.length()) < 280) {
    				// make sure the length of the content is no more than 140 Chinese words
    				String content = prefix + url;
    				intent.putExtra(Intent.EXTRA_TEXT,content);
    			}
    			else {
    				if (url.length() < 280) {
    					// share the URL directly as it is quite long
    					intent.putExtra(Intent.EXTRA_TEXT, url);
    				}
    				else {
    					// The length of the url is more than 140 words. Compressed url should be used in the future
    					Util.showLongToast(InterestPlaceWebViewActivity.this, R.string.url_is_too_long);
    					return;
    				}
    			}
    			
    			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			
    			String sinaPackage = "com.sina.weibo";
    			
    			// Check whether sina weibo is installed
    			PackageManager pm = getPackageManager();
    			List<ResolveInfo> matches = pm.queryIntentActivities(intent,
    					PackageManager.MATCH_DEFAULT_ONLY);
    			if (!matches.isEmpty()) {
    					    				
    				ResolveInfo info = null;
    				for (ResolveInfo each : matches) {	    					
    					String pkgName = each.activityInfo.applicationInfo.packageName;
    					if (sinaPackage.equals(pkgName)) {
    						info = each;
    						break;				
    					}
    				}
    				
    				if (info == null) { // if sina weibo is NOT installed
    					Util.showLongToast(InterestPlaceWebViewActivity.this, R.string.no_weibo_installed);
    					return;
    				} else {
    					intent.setClassName(sinaPackage, info.activityInfo.name);
    					startActivity(intent);
    				}
    			} // !matches.isEmpty()
			} // onClick(View v)
		});

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
