package com.winjune.wifiindoor.activity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.map.InterestPlace;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

public class InterestPlaceTTSViewerActivity extends Activity implements OnInitListener {	
	private OnClickListener listener2;
	private MediaPlayer mPlayer = null;
	private ImageButton audioPlayButton = null;
	private ImageButton audioStopButton = null;
	private SeekBar audioSeekbar = null; 
	private Button shareButton = null; //Button for sharing the content to social media
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
		AudioPause();
		
		if (AutoGuideTTS != null) {
			AutoGuideTTS.stop();	
		}
		
		Util.setEnergySave(true);
		Util.setCurrentForegroundActivity(null);		
	}
	
    
    // Need to stop the audio
    @Override
    protected void onDestroy() {
    	
    	shutdownMediaPlayer();

    	AutoGuideTTSShutdown();
    	
    	super.onDestroy();
    }    
    
	@Override
	public void onBackPressed() {
		        
		shutdownMediaPlayer();
		
		AutoGuideTTSShutdown();
		
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

    private  void loadCachedOrDownloadIMG(final ImageView imageInfo, final String imgFileName){
       	new Thread() {
    		public void run() {   			
    	    	String localFilePath = Util.getFilePath(IndoorMapData.IMG_FILE_PATH_LOCAL);
    	    	String fullFileName = localFilePath + imgFileName;
    	    	final String imgURL= Util.fullUrl(IndoorMapData.IMG_FILE_PATH_LOCAL, imgFileName);
    			File   file = new File (fullFileName);
    			 
    	    	if (!file.exists()) {
    	    		//The file doesn't exist, download the file to the cache folder
    	    		
    	    		Util.downFile(InterestPlaceTTSViewerActivity.this, imgURL, IndoorMapData.IMG_FILE_PATH_LOCAL, imgFileName,                     		
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
							imageInfo.setOnClickListener(listener2);
						}
						imageInfo.invalidate();		    
					}
				});		        
    		}
    	}.start();
    }    

    private void initMediaPlayer(String audioFile) {
    	
    	final String audioURL= Util.fullUrl(IndoorMapData.AUDIO_FILE_PATH_REMOTE, audioFile);
	    	
    	mPlayer = new MediaPlayer();       

        try {
			mPlayer.setDataSource(audioURL);
			mPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //set data source
        

        
        mPlayer.setOnPreparedListener (new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer arg0) {
                // enable player button
                audioPlayButton.setEnabled(true);
               
                //get the length of the audio and setup the seekbar, default is 100
                //audioSeekbar.setMax(mPlayer.getDuration());
            }
        });
                       
        new Thread(new AudioSeekBarRefresh()).start();
        
        // Detect the completetion event
        mPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Toast.makeText(MainMusic.this, "onCompletion", Toast.LENGTH_SHORT).show();
                AudioStop();
            }
        });
    }
 
    private void AudioStop() {
        mPlayer.stop();        
        audioPlayButton.setBackgroundResource(R.drawable.play_enable);
        audioStopButton.setEnabled(false);
        try {
            mPlayer.prepare();
            mPlayer.seekTo(0);
            audioPlayButton.setEnabled(true);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }    
 
    private void AudioPlay() {
 
        mPlayer.start();
        audioPlayButton.setBackgroundResource(R.drawable.pause_enable);
        audioStopButton.setEnabled(true);
    }
    
    private void AudioPause(){
    	if (mPlayer != null) {    	
    		mPlayer.pause();
    		audioPlayButton.setBackgroundResource(R.drawable.play_enable);
    	}
    }
    
    private void shutdownMediaPlayer() {
    	if (mPlayer != null){
	        // stop the audio first
	        if (audioStopButton != null) {
		        if (audioStopButton.isEnabled()) {
		        	AudioStop();
		        }
		        
	        }
	        
	        mPlayer.release();
	        mPlayer = null;
    	}    	
    }
    
	class AudioSeekbarCL implements OnSeekBarChangeListener {  
		int progress;  
		
		@Override
        public void onStopTrackingTouch(SeekBar seekBar) { 
	            mPlayer.seekTo(progress);  
        }

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			this.progress = progress * mPlayer.getDuration()  
                    / audioSeekbar.getMax();  
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}  
    }  	
	
	private Handler audioSeekBarHandler = new Handler() {

    	public void handleMessage(Message msg) {
    		
    		if (mPlayer == null){
    			return;
    		}
    		
            int position = mPlayer.getCurrentPosition();  
            int duration = mPlayer.getDuration();  
              
            if (duration > 0) {  
                long pos = audioSeekbar.getMax() * position / duration;  
                audioSeekbar.setProgress((int) pos);  
            }  
    	};
	};
	
	class AudioSeekBarRefresh implements Runnable{
	    @Override
	    public void run() {
	        while(true){
	        	
	        	if (mPlayer== null) {
	        		return;
	        	}
	        	
	        	if (mPlayer.isPlaying() && !(audioSeekbar.isPressed())) {
	        		audioSeekBarHandler.sendMessage(audioSeekBarHandler.obtainMessage());
	        	}
	        	
	            try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }

	    }
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

			
		    mWebView.loadUrl("file:///android_asset/index.html");
		
	}
		
}