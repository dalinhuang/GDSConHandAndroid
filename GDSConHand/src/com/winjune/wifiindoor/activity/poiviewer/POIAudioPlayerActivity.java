package com.winjune.wifiindoor.activity.poiviewer;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
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
import com.winjune.wifiindoor.R.drawable;
import com.winjune.wifiindoor.R.string;
import com.winjune.wifiindoor.map.InterestPlace;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

public class POIAudioPlayerActivity extends Activity {	
	private OnClickListener listener2;
	private MediaPlayer mPlayer = null;
	private ImageButton audioPlayButton = null;
	private ImageButton audioStopButton = null;
	private SeekBar audioSeekbar = null; 
	private Button shareButton = null; //Button for sharing the content to social media
	
	
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
		
		
		
		Util.setEnergySave(true);
		Util.setCurrentForegroundActivity(null);		
	}
	
    
    // Need to stop the audio
    @Override
    protected void onDestroy() {
    	
    	shutdownMediaPlayer();


    	
    	super.onDestroy();
    }    
    
	@Override
	public void onBackPressed() {
		        
		shutdownMediaPlayer();
		

		
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
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
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
        textInfo.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Large);        
        
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
				
		//Play audio
		if ((audio !=null) && (!audio.trim().isEmpty())) {
			LinearLayout audioLayout = new LinearLayout(getApplicationContext());
			
			audioLayout.setGravity(Gravity.CENTER);
		        
		    audioPlayButton = new ImageButton(this);
			audioStopButton = new ImageButton(this);
			audioSeekbar = new SeekBar(this);
			audioSeekbar.setOnSeekBarChangeListener(new AudioSeekbarCL()); 
		        
			audioPlayButton.setEnabled(false);
		    audioStopButton.setEnabled(false);
				
		    // define image button  
		    LinearLayout.LayoutParams playButtonParams = new LinearLayout.LayoutParams(  
		    								ViewGroup.LayoutParams.WRAP_CONTENT,  
		    								ViewGroup.LayoutParams.WRAP_CONTENT);
		    
		    playButtonParams.leftMargin = (int) (6 * scale + 0.5f);
		   		   
		    LinearLayout.LayoutParams stopButtonParams = new LinearLayout.LayoutParams(  
		    								ViewGroup.LayoutParams.WRAP_CONTENT,  
		    								ViewGroup.LayoutParams.WRAP_CONTENT); 
		    
		    stopButtonParams.leftMargin = (int) (6 * scale + 0.5f);
		   
		    LinearLayout.LayoutParams seekBarParams = new LinearLayout.LayoutParams(  
	                						ViewGroup.LayoutParams.MATCH_PARENT,  
	                						ViewGroup.LayoutParams.WRAP_CONTENT); 
		    seekBarParams.leftMargin = (int) (6 * scale + 0.5f);
		    seekBarParams.rightMargin = (int) (6 * scale + 0.5f);
		    		         		        				
		    audioPlayButton.setBackgroundResource(R.drawable.play_enable);
		    audioStopButton.setBackgroundResource(R.drawable.stop_enable);
				
		    audioLayout.addView(audioPlayButton, playButtonParams);
		    audioLayout.addView(audioStopButton, stopButtonParams);
		    audioLayout.addView(audioSeekbar, seekBarParams);
		    mainLayout.addView(audioLayout);
				
		    initMediaPlayer(audio);
				
		    OnClickListener audioPlayOCL = new View.OnClickListener() {		 
			   @Override
		       public void onClick(View v) {
				   
				   //switch play and pause 
				   if (mPlayer.isPlaying()) {
					   AudioPause();
				   } else {
					   AudioPlay(); 
				   }
		       }
		    };
		        
		    OnClickListener audioStopOCL = new View.OnClickListener() {		 
		       @Override
		       public void onClick(View v) {		                
		           AudioStop();
		       }
		   };		        
		  
		   audioPlayButton.setOnClickListener(audioPlayOCL);
		   audioStopButton.setOnClickListener(audioStopOCL);				
		} // audio play
			
		// Display Text
		if ( text != null) {
			textInfo.setText(text);
			mainLayout.addView(textInfo);
			
			
					
		}

		//Display picture
		if ((picture != null) && (!picture.trim().isEmpty())) {
			
			final String[] pictures = picture.split(";");
			
			listener2 = new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					return;
				}					
			};
				
			for (int i=0; i<pictures.length; i++) {
				String picFilName = pictures[i];
					
				if (picFilName.trim().isEmpty()) {
					continue;
				}
								
				RelativeLayout pictureLayout = new RelativeLayout(getApplicationContext());
					
				final ImageView imageInfo = new ImageView(getApplicationContext());
				RelativeLayout.LayoutParams pictureLayoutParams = new RelativeLayout.LayoutParams(  
			                ViewGroup.LayoutParams.MATCH_PARENT,  
			                ViewGroup.LayoutParams.MATCH_PARENT);
				pictureLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT); 
				//pictureLayoutParams.topMargin = (int) (2 * scale + 0.5f);
				pictureLayoutParams.topMargin = 2;
					
				imageInfo.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

				//Hoare: cache the file in local folder first
				loadCachedOrDownloadIMG(imageInfo, picFilName);
					
				//imageInfo.setScaleType(ImageView.ScaleType.FIT_CENTER);
					
				pictureLayout.addView(imageInfo, pictureLayoutParams);
										  					
				mainLayout.addView(pictureLayout);
			}
			
			// Add the button to share the content
			shareButton = new Button(this);
			shareButton.setEnabled(true);	        
	        shareButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			shareButton.setText(R.string.share);
			
			RelativeLayout shareLayout = new RelativeLayout(getApplicationContext());
			RelativeLayout.LayoutParams shareLayoutParams = new RelativeLayout.LayoutParams(  
	                ViewGroup.LayoutParams.MATCH_PARENT,  
	                ViewGroup.LayoutParams.MATCH_PARENT);
			shareLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			
			shareLayout.addView(shareButton);
			mainLayout.addView(shareLayout);
			
			final String weiboContent = text;
			
			shareButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String content; 
					
	    			Intent intent = new Intent(Intent.ACTION_SEND); // Intent to be sent to Sina Weibo APP
	    			intent.setType("image/*");
	    			intent.putExtra(Intent.EXTRA_SUBJECT, R.string.share);
	    			
	    			// Add the text content to the intent
	    			if (weiboContent != null) {
		    			content = weiboContent;	    	
		    			if (content.length() > 280) {
		    				content = content.substring(0, 277); // cut to match 140 Chinese character for Sina Weibo 
		    			}
	    			}else {
	    				content  = getString(R.string.share_prefix);
	    			}
	    					    		
	    			intent.putExtra(Intent.EXTRA_TEXT,content);
	    			
	    			// Add the stream of the picture to the intent
					String localFilePath = Util.getFilePath(IndoorMapData.IMG_FILE_PATH_LOCAL);
	    	    	String fullFileName = localFilePath + pictures[0]; // By default share the first picture
	    			File f = new File (fullFileName);
	    			Uri uri = Uri.fromFile(f);
	    			intent.putExtra(Intent.EXTRA_STREAM, uri);
	    			
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
	    					Util.showLongToast(POIAudioPlayerActivity.this, R.string.no_weibo_installed);
	    					return;
	    				} else {
	    					intent.setClassName(sinaPackage, info.activityInfo.name);
	    					startActivity(intent);
	    				}
	    			} // !matches.isEmpty()
				} // onClick(View v)
			});
		}// Display picture	
		
		scroll.addView(mainLayout);
		setContentView(scroll);				
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
    	    		
    	    		Util.downFile(POIAudioPlayerActivity.this, imgURL, IndoorMapData.IMG_FILE_PATH_LOCAL, imgFileName,                     		
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

   
		
}