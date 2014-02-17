package com.ericsson.cgc.aurora.wifiindoor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.MediaController;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

import com.ericsson.cgc.aurora.wifiindoor.map.InterestPlace;
import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;

public class InterestPlaceViewerActivity extends Activity {	
	private OnClickListener listener2;
	private MediaPlayer mPlayer = null;
	private ImageButton audioPlayButton = null;
	private ImageButton audioStopButton = null;
	
	@Override
	protected void onResume() {
		super.onResume();
		Util.setEnergySave(false);
		Util.setCurrentForegroundActivity(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Util.setEnergySave(true);
		Util.setCurrentForegroundActivity(null);
	}
	
    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
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
        scroll.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        LinearLayout mainLayout = new LinearLayout(getApplicationContext());
        LayoutParams layout_text_parm = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        mainLayout.setLayoutParams(layout_text_parm);
        mainLayout.setOrientation(LinearLayout.VERTICAL);      

        TextView textInfo = new TextView(getApplicationContext());
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
        	
        else {
        	 
        	
        }
		
			// Display Text
			if ( text != null) {
				textInfo.setText(text);
				mainLayout.addView(textInfo);
			}
			
			//Play audio
			/*if ((audio !=null) && (!audio.trim().isEmpty()))*/ 
			{
		 
				
		        RelativeLayout audioLayout = new RelativeLayout(getApplicationContext());
		        
				audioPlayButton = new ImageButton(this);
				//final ImageView PauseButton = new ImageButton(this);
				audioStopButton = new ImageButton(this);
		        
				audioPlayButton.setEnabled(false);
		        audioStopButton.setEnabled(false);
				
		        // define image button  
		        RelativeLayout.LayoutParams playButtonParams = new RelativeLayout.LayoutParams(  
		                ViewGroup.LayoutParams.WRAP_CONTENT,  
		                ViewGroup.LayoutParams.WRAP_CONTENT);  
		        RelativeLayout.LayoutParams stopButtonParams = new RelativeLayout.LayoutParams(  
		                ViewGroup.LayoutParams.WRAP_CONTENT,  
		                ViewGroup.LayoutParams.WRAP_CONTENT); 
		        
		        // setup rules for image buttons  
		        playButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);  		        // 
		        playButtonParams.addRule(RelativeLayout.CENTER_VERTICAL); 
		        playButtonParams.leftMargin = (int) (50 * scale + 0.5f); 		   
		        stopButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);  
		        stopButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);  
		        stopButtonParams.rightMargin = (int) (50 * scale + 0.5f); 
		         		        				
				audioPlayButton.setBackgroundResource(R.drawable.play_enable);
				
				audioStopButton.setBackgroundResource(R.drawable.stop_enable);
				
				audioLayout.addView(audioPlayButton, playButtonParams);
				audioLayout.addView(audioStopButton, stopButtonParams);
				mainLayout.addView(audioLayout);
				
				initMediaPlayer();
				
		        OnClickListener audioPlayOCL = new View.OnClickListener() {		 
		            @Override
		            public void onClick(View v) {		                
		                    //Toast.makeText(MainMusic.this, "点击播放", Toast.LENGTH_SHORT).show();
		                    AudioPlay();
		                    		               
		            }
		        };
		        
		        OnClickListener audioStopOCL = new View.OnClickListener() {		 
		            @Override
		            public void onClick(View v) {		                
		                    //Toast.makeText(MainMusic.this, "点击播放", Toast.LENGTH_SHORT).show();
		                    AudioStop();
		                    		               
		            }
		        };		        
		  
		        audioPlayButton.setOnClickListener(audioPlayOCL);
		        audioStopButton.setOnClickListener(audioStopOCL);
				
			}
			

			//Display picture
			if ((picture != null) && (!picture.trim().isEmpty())) {
				String[] pictures = picture.split(";");
				
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
					
					//Cache this picture locally if needed
					

					RelativeLayout pictureLayout = new RelativeLayout(getApplicationContext());
					
					final ImageView imageInfo = new ImageView(getApplicationContext());
					RelativeLayout.LayoutParams pictureLayoutParams = new RelativeLayout.LayoutParams(  
			                ViewGroup.LayoutParams.FILL_PARENT,  
			                ViewGroup.LayoutParams.FILL_PARENT);
					pictureLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT); 
					pictureLayoutParams.topMargin = (int) (20 * scale + 0.5f); 					
					
					imageInfo.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));

					//Hoare: cache the file in local folder first
					loadCachedOrDownloadIMG(imageInfo, picFilName);
					//final String IMG_URL= Util.fullUrl(IndoorMapData.IMG_FILE_PATH_REMOTE, pictures[i]);
					//getBitmapFromUrl(imageInfo, IMG_URL);
					
					imageInfo.setScaleType(ImageView.ScaleType.FIT_CENTER);
					
					pictureLayout.addView(imageInfo, pictureLayoutParams);
					
/*					imageInfo.setImageResource(R.drawable.click_here);
					
					final String IMG_URL= Util.fullUrl(IndoorMapData.IMG_FILE_PATH_REMOTE, pictures[i]);
					
					OnClickListener listener1 = new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							getBitmapFromUrl(imageInfo, IMG_URL);
						}
						
					};
					
					imageInfo.setOnClickListener(listener1);*/
										  					
					mainLayout.addView(pictureLayout);
				}		
		} else {
			textInfo.setText(R.string.no_description);
			mainLayout.addView(textInfo);
		}		
		
		scroll.addView(mainLayout);
		setContentView(scroll);				
		
    }
    

    private void initMediaPlayer() {
 
    	mPlayer = new MediaPlayer();
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sample_bicycle);

        //mPlayer.setDataSource(PATH_TO_FILE); //set data source
        //mPlayer.prepare();        
        
        mPlayer.setOnPreparedListener (new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer arg0) {
                // enable player button
                //Toast.makeText(MainMusic.this, "onPrepared", Toast.LENGTH_SHORT).show();
                audioPlayButton.setEnabled(true);
            }
        });
                       
 
        // 定义播放完成监听器
        mPlayer.setOnCompletionListener(new OnCompletionListener() {
 
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Toast.makeText(MainMusic.this, "onCompletion", Toast.LENGTH_SHORT).show();
                AudioStop();
            }
        });
    }
 
    // 停止播放
    private void AudioStop() {
        mPlayer.stop();
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
        audioPlayButton.setEnabled(false);
        audioStopButton.setEnabled(true);
    }
 

    // Need to stop the audio
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        if (audioStopButton != null) {
	        if (audioStopButton.isEnabled()) {
	        	AudioStop();
	        }
        }
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
    	    		
    	    		Util.downFile(InterestPlaceViewerActivity.this, imgURL, IndoorMapData.IMG_FILE_PATH_LOCAL, imgFileName,                     		
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