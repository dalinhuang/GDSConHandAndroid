package com.winjune.wifiindoor.activity;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;


import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.map.InterestPlace;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

public class InterestPlaceTTSViewerActivity extends Activity implements OnInitListener {	
	private TextToSpeech AutoGuideTTS = null;
	private WebView mWebView = null;
	
	@Override
	protected void onResume() {
		super.onResume();
		Util.setEnergySave(false);
		Util.setCurrentForegroundActivity(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		
		if (AutoGuideTTS != null) {
			AutoGuideTTS.stop();	
		}
		
		Util.setEnergySave(true);
		Util.setCurrentForegroundActivity(null);		
	}
	
    
    // Need to stop the audio
    @Override
    protected void onDestroy() {
    	
    	

    	AutoGuideTTSShutdown();
    	
    	super.onDestroy();
    }    
    
	@Override
	public void onBackPressed() {
		        

		
		super.onBackPressed();						
	}		
	
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	InterestPlace place = null;
    	String text = null;
    	String picture = null;
    	String audio = null;
    	   	
    	super.onCreate(savedInstanceState);
    	
    	Log.i("InterestPlaceViewerTTSActivity","aaaa");
    	
    	
    	showWebView();
    	
    	Log.i("InterestPlaceViewerTTSActivity","bbbb");
    	       
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);  
        // get the screen attr for conversion between dp and px  
        float scale = this.getResources().getDisplayMetrics().density;  
        
        ScrollView scroll = new ScrollView(getApplicationContext());
        scroll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        LinearLayout mainLayout = new LinearLayout(getApplicationContext());
        LayoutParams layout_text_parm = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mainLayout.setLayoutParams(layout_text_parm);
        mainLayout.setOrientation(LinearLayout.VERTICAL);      

        final TextView textInfo = new TextView(getApplicationContext());
        textInfo.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textInfo.setAutoLinkMask(Linkify.ALL);
        textInfo.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Large); */       
        
        Bundle bundle = getIntent().getExtras();
        int req = bundle.getInt(IndoorMapData.BUNDLE_KEY_REQ_FROM);
               
        if (req == IndoorMapData.BUNDLE_VAL_INTEREST_REQ_FROM_TOUCH) {
        	place = (InterestPlace) bundle.getSerializable(IndoorMapData.BUNDLE_KEY_INTEREST_PLACE_INSTANCE);
        	
        	if (place != null) {        	        
    			text = place.getInfo();
    			picture = place.getUrlPic();
    			audio = place.getUrlAudio();
        	}
        }    	
        else if (req == IndoorMapData.BUNDLE_VAL_INTEREST_REQ_FROM_INPUT) {
        	place = (InterestPlace) bundle.getSerializable(IndoorMapData.BUNDLE_KEY_INTEREST_PLACE_INSTANCE);
        	
        	if (place != null) {        	        
    			audio = place.getUrlAudio();
    			text = place.getInfo();
        	}
        } 
        else {
        	return;
        }
        				
        AutoGuideTTSSpeak(text);		
		
    }

   
   // Callback by tts engine
   @Override
   public void onInit(int status) {  
       if(status == TextToSpeech.SUCCESS){  
           // we use Chinese      	       	   
          int result =  AutoGuideTTS.isLanguageAvailable(Locale.CHINA);
          
          Log.i("TTS result", ""+result);
          
          if((result == TextToSpeech.LANG_COUNTRY_AVAILABLE) |  
             (result == TextToSpeech.LANG_AVAILABLE)) {   
        	  AutoGuideTTS.setLanguage(Locale.CHINA);
        	  Util.showLongToast(this, R.string.tts_start_soon);	   
          } else {
        	  AutoGuideTTSShutdown();
        	  Util.showLongToast (this, R.string.tts_language_unsupported);                   	  
          }                     
       }  
   }  
      	
	private  void AutoGuideTTSSpeak(String text ){
				
		//Create a tts instanse first
		if (AutoGuideTTS == null) {        	
			AutoGuideTTS = new TextToSpeech(this, this);
		}
		
		//start a thread to speak since we need wait for the tts binding
		final String text1 = text;

		// getMaxSpeechInputLength API is supported after API18
		// final int maxLength =  TextToSpeech.getMaxSpeechInputLength();
		final int maxLength = 2048;
		
		new Thread() {  
	        public void run() { 
	        	
	        	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	    		// need to double check if the tts engine is started
	        	if (AutoGuideTTS != null) {
	        		String text2 = text1;
	        		Boolean isContinued = true;
	        		
	        		while (isContinued) {
	        			String text3 = text2;
	        				        			
	        			if (text2.length() > maxLength) {	        				
	        				text3 = text2.substring(0, maxLength-1);	
	        				text2 = text2.substring(maxLength);	        				
	        			} else {
	        				isContinued = false;
	        			}	        				
	        			
		        		AutoGuideTTS.speak(text3, TextToSpeech.QUEUE_ADD, null);		        		
	        		}
	    		}	        	
	        }  
	    }.start(); 
	}
	
	// Shutdown TTS engine, if there is something playing, will stop it first. 
	private void AutoGuideTTSShutdown(){
	    if (AutoGuideTTS != null)
	    {
	    	AutoGuideTTS.stop();
	    	AutoGuideTTS.shutdown();
	    	AutoGuideTTS = null;
	    }	    
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void showWebView(){		// webView与js交互代码
		
			mWebView = new WebView(this);
			setContentView(mWebView);
			
			mWebView.requestFocus();
			
			mWebView.setWebChromeClient(new WebChromeClient(){
				@Override
				public void onProgressChanged(WebView view, int progress){
					InterestPlaceTTSViewerActivity.this.setTitle("Loading...");
					InterestPlaceTTSViewerActivity.this.setProgress(progress);
					
					if(progress >= 80) {
						InterestPlaceTTSViewerActivity.this.setTitle("JsAndroid Test");
					}
				}
			});
			
			mWebView.setOnKeyListener(new View.OnKeyListener() {		// webview can go back
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
						mWebView.goBack();
						return true;
					}
					return false;
				}
			});
			
			WebSettings webSettings = mWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setDefaultTextEncodingName("utf-8");

			mWebView.addJavascriptInterface(getHtmlObject(), "jsObj");
		    mWebView.loadUrl("file:///android_asset/index.html");
		
	}
	
	private Object getHtmlObject(){
		Object insertObj = new Object(){
			public void HtmlcallJava(){
				//Util.showLongToast(InterestPlaceTTSViewerActivity.this, R.string.tts_start_soon);
				Intent intent_show_interest_place = new Intent(InterestPlaceTTSViewerActivity.this, InterestPlaceViewerActivity.class);
				Bundle bundle = getIntent().getExtras();
				intent_show_interest_place.putExtras(bundle); 
				InterestPlaceTTSViewerActivity.this.startActivity(intent_show_interest_place);
				InterestPlaceTTSViewerActivity.this.finish();
			}
			
			
			
		};
		
		return insertObj;
	}
		
}