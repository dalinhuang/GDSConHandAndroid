package com.winjune.wifiindoor.activity;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.speech.ErrorCode;
import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.SpeechSynthesizer;
import com.iflytek.speech.SynthesizerListener;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.map.InterestPlace;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

public class InterestPlaceTTSViewerActivity extends Activity {
	private static String TAG = "TtsDemo";
	private WebView mWebView = null;
	private SpeechSynthesizer mTts;
	private boolean isStopButton = true;
	private boolean needStart = false;
	private Toast mToast;
	private ProgressBar progressHorizontal;
	private ImageView pauseButton;
	private ImageView resumeButton;
	private static final int RESTART = 1;
	String text;

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

	// Need to stop the audio
	@Override
	protected void onDestroy() {

		
		 mTts.stopSpeaking(mTtsListener);
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
		setContentView(R.layout.activity_interest_place_tts);
		
		progressHorizontal = (ProgressBar) findViewById(R.id.progress_horizontal);
		pauseButton = (ImageView) findViewById(R.id.pause);
		resumeButton = (ImageView) findViewById(R.id.resume);

		mTts = new SpeechSynthesizer(this, mTtsInitListener);
		mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		
		final TextView textInfo = (TextView) findViewById(R.id.interest_text);
        textInfo.setAutoLinkMask(Linkify.ALL);
        textInfo.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Large);        
       

		/*
		 * requestWindowFeature(Window.FEATURE_NO_TITLE); // get the screen attr
		 * for conversion between dp and px float scale =
		 * this.getResources().getDisplayMetrics().density;
		 * 
		 * ScrollView scroll = new ScrollView(getApplicationContext());
		 * scroll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		 * LayoutParams.MATCH_PARENT));
		 * 
		 * LinearLayout mainLayout = new LinearLayout(getApplicationContext());
		 * LayoutParams layout_text_parm = new
		 * LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		 * mainLayout.setLayoutParams(layout_text_parm);
		 * mainLayout.setOrientation(LinearLayout.VERTICAL);
		 * 
		 * final TextView textInfo = new TextView(getApplicationContext());
		 * textInfo.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT)); textInfo.setAutoLinkMask(Linkify.ALL);
		 * textInfo.setTextAppearance(getApplicationContext(),
		 * android.R.style.TextAppearance_Large);
		 */

		Bundle bundle = getIntent().getExtras();
		int req = bundle.getInt(IndoorMapData.BUNDLE_KEY_REQ_FROM);

		if (req == IndoorMapData.BUNDLE_VAL_INTEREST_REQ_FROM_TOUCH) {
			place = (InterestPlace) bundle
					.getSerializable(IndoorMapData.BUNDLE_KEY_INTEREST_PLACE_INSTANCE);

			if (place != null) {
				text = place.getInfo();
				picture = place.getUrlPic();
				audio = place.getUrlAudio();
			}
		} else if (req == IndoorMapData.BUNDLE_VAL_INTEREST_REQ_FROM_INPUT) {
			place = (InterestPlace) bundle
					.getSerializable(IndoorMapData.BUNDLE_KEY_INTEREST_PLACE_INSTANCE);

			if (place != null) {
				audio = place.getUrlAudio();
				text = place.getInfo();
			}
		} else {
			return;
		}
  
		textInfo.setText(text);
		AutoGuideTTSSpeak(text);
		

	}

	
	
	 
	 public void backClick(View v) {
	    	onBackPressed();    	
	    }  

	/**
	 * 初期化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {

		@Override
		public void onInit(ISpeechModule arg0, int code) {
			Log.d(TAG, "InitListener init() code = " + code);

			if (code == ErrorCode.SUCCESS) {
				// ((Button)findViewById(R.id.tts_play)).setEnabled(true);
			}
		}
	};

	private void showTip(final String str) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mToast.setText(str);
				mToast.show();
			}
		});
	}

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener.Stub() {
		@Override
		public void onBufferProgress(int progress) throws RemoteException {
			Log.d(TAG, "onBufferProgress :" + progress);
			// showTip("onBufferProgress :" + progress);
		}

		@Override
		public void onCompleted(int code) throws RemoteException {
			Log.d(TAG, "onCompleted code =" + code);
			// showTip("onCompleted code =" + code);

			Message msgSuc = new Message();
			msgSuc.what = RESTART;
			myHandler.sendMessage(msgSuc);

		}

		@Override
		public void onSpeakBegin() throws RemoteException {
			Log.d(TAG, "onSpeakBegin");
			// showTip("onSpeakBegin");
		}

		@Override
		public void onSpeakPaused() throws RemoteException {
			Log.d(TAG, "onSpeakPaused.");
			// showTip("onSpeakPaused.");
		}

		@Override
		public void onSpeakProgress(int progress) throws RemoteException {
			Log.d(TAG, "onSpeakProgress :" + progress);
			// showTip("onSpeakProgress :" + progress);

			progressHorizontal.setProgress(progress);

		}

		@Override
		public void onSpeakResumed() throws RemoteException {
			Log.d(TAG, "onSpeakResumed.");
			// showTip("onSpeakResumed");
		}
	};

	private void AutoGuideTTSSpeak(final String text) {

		new Thread() {
			public void run() {

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int code = mTts.startSpeaking(text, mTtsListener);
				if (code != 0) {
					showTip("start speak error : " + code);
				} else {
					showTip("start speak success.");
				}

			}
		}.start();
	}

	public void stopTTS(View v) {
		mTts.pauseSpeaking(mTtsListener);
		isStopButton = false;
		pauseButton.setVisibility(View.GONE);
		resumeButton.setVisibility(View.VISIBLE);

	}

	public void resumeTTS(View v) {
		if (needStart) {
			needStart = false;
			
			int code = mTts.startSpeaking(text, mTtsListener);
			if (code != 0) {
				showTip("start speak error : " + code);
			} else
				showTip("start speak success.");

		} else {
			mTts.resumeSpeaking(mTtsListener);
		}

		isStopButton = true;
		resumeButton.setVisibility(View.GONE);
		pauseButton.setVisibility(View.VISIBLE);

	}

	// 接收消息的Handler
	final Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == RESTART) {
				isStopButton = false;
				pauseButton.setVisibility(View.GONE);
				resumeButton.setVisibility(View.VISIBLE);

				progressHorizontal.setProgress(0);
				needStart = true;
			}
		};
	};

}