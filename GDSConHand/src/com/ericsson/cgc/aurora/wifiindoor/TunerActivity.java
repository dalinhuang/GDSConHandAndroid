package com.ericsson.cgc.aurora.wifiindoor;

import com.ericsson.cgc.aurora.wifiindoor.util.IndoorMapData;
import com.ericsson.cgc.aurora.wifiindoor.util.Tuner;
import com.ericsson.cgc.aurora.wifiindoor.util.Util;
import com.ericsson.cgc.aurora.wifiindoor.util.VisualParameters;
import com.ericsson.cgc.aurora.wifiindoor.util.WifiIpsSettings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class TunerActivity extends Activity {
	private OnClickListener on_click_listener0 = null;
	private OnClickListener on_click_listener1 = null;
	private OnClickListener on_click_listener2 = null;
	private Button button0;
	private Button button1;
	private Button button2;
	
	private CheckBox PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR;
	private CheckBox PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER;
	private EditText PERIODIC_WIFI_CAPTURE_INTERVAL;
	private EditText MAX_AGE_FOR_WIFI_SAMPLES;
	private EditText MIN_WIFI_SAMPLES_BUFFERED;
	private EditText PERIODIC_LOCATE_INTERVAL;
	private EditText PERIODIC_WIFI_SCAN_INTERVAL;
	private EditText MAX_FINGERPRINTS_FOR_LOCATE;
	private EditText MAX_WAIT_MS_FOR_LOCATE;
	private EditText MAX_FINGERPRINTS_FOR_COLLECT;
	private EditText MAX_WAIT_MS_FOR_COLLECT;
	private EditText MIN_DBM_COUNT_IN;
	private EditText MAX_DBM_COUNT_IN;
	private EditText MIN_AP_COUNT_IN;
	private CheckBox DEBUG;
	private CheckBox SERVER_RUNNING_IN_LINUX;
	private EditText LINUX_SERVER_IP;
	private EditText LINUX_SERVER_PORT;
	private CheckBox USE_DOMAIN_NAME;
	private EditText SERVER_DOMAIN_NAME;
	private EditText SERVER_IP;
	private EditText SERVER_PORT;
	private EditText CONNECTION_TIMEOUT;
	private EditText SOCKET_TIMEOUT;
	private EditText SERVER_SUB_DOMAIN;
	private EditText URL_PREFIX;
	private EditText URL_API_LOCATE;
	private EditText URL_API_COLLECT;
	private EditText URL_API_QUERY;
	private EditText URL_API_NFC_COLLECT;
	private EditText URL_API_LOCATE_BASE_NFC;
	private CheckBox ADS_ENABLED;
	private CheckBox BANNERS_ENABLED;
	private CheckBox ENTRY_NEEDED;
	private CheckBox GOOGLE_MAP_EMBEDDED;
	private CheckBox BACKGROUND_LINES_NEEDED;
	
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // reset the data
        on_click_listener0 = new OnClickListener(){
        	public void onClick(View view){        		
        		resetToSavedValues();
        	}
        };
        
        // save the changes
        on_click_listener1 = new OnClickListener(){
        	public void onClick(View view){
        		saveValues();
        		TunerActivity.this.finish();
        	}
        };
        
        // close without change
        on_click_listener2 = new OnClickListener(){
        	public void onClick(View view){
        		TunerActivity.this.finish();
        	}
        };
        
        setContentView(R.layout.tuner);
        
        // Define Buttons and bind the listeners
        button0 = (Button) findViewById(R.id.resetButton);
        button0.setOnClickListener(on_click_listener0);
        button1 = (Button) findViewById(R.id.saveButton);
        button1.setOnClickListener(on_click_listener1);
        button2 = (Button) findViewById(R.id.closeButton);
        button2.setOnClickListener(on_click_listener2);
        
        PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR = (CheckBox) findViewById(R.id.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR);
    	PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER = (CheckBox) findViewById(R.id.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER);
    	PERIODIC_WIFI_CAPTURE_INTERVAL = (EditText) findViewById(R.id.PERIODIC_WIFI_CAPTURE_INTERVAL);
    	MAX_AGE_FOR_WIFI_SAMPLES = (EditText) findViewById(R.id.MAX_AGE_FOR_WIFI_SAMPLES);
    	MIN_WIFI_SAMPLES_BUFFERED = (EditText) findViewById(R.id.MIN_WIFI_SAMPLES_BUFFERED);
    	PERIODIC_LOCATE_INTERVAL = (EditText) findViewById(R.id.PERIODIC_LOCATE_INTERVAL);
    	PERIODIC_WIFI_SCAN_INTERVAL = (EditText) findViewById(R.id.PERIODIC_WIFI_SCAN_INTERVAL);
    	MAX_FINGERPRINTS_FOR_LOCATE = (EditText) findViewById(R.id.MAX_FINGERPRINTS_FOR_LOCATE);
    	MAX_WAIT_MS_FOR_LOCATE = (EditText) findViewById(R.id.MAX_WAIT_MS_FOR_LOCATE);
    	MAX_FINGERPRINTS_FOR_COLLECT = (EditText) findViewById(R.id.MAX_FINGERPRINTS_FOR_COLLECT);
    	MAX_WAIT_MS_FOR_COLLECT = (EditText) findViewById(R.id.MAX_WAIT_MS_FOR_COLLECT);
    	MIN_DBM_COUNT_IN = (EditText) findViewById(R.id.MIN_DBM_COUNT_IN);
    	MAX_DBM_COUNT_IN = (EditText) findViewById(R.id.MAX_DBM_COUNT_IN);
    	MIN_AP_COUNT_IN = (EditText) findViewById(R.id.MIN_AP_COUNT_IN);
    	DEBUG = (CheckBox) findViewById(R.id.DEBUG);
    	USE_DOMAIN_NAME = (CheckBox) findViewById(R.id.USE_DOMAIN_NAME);
    	SERVER_DOMAIN_NAME = (EditText) findViewById(R.id.SERVER_DOMAIN_NAME);
    	SERVER_IP = (EditText) findViewById(R.id.SERVER_IP);
    	SERVER_PORT = (EditText) findViewById(R.id.SERVER_PORT);
    	SERVER_RUNNING_IN_LINUX = (CheckBox) findViewById(R.id.SERVER_RUNNING_IN_LINUX);
    	LINUX_SERVER_IP = (EditText) findViewById(R.id.LINUX_SERVER_IP);
    	LINUX_SERVER_PORT = (EditText) findViewById(R.id.LINUX_SERVER_PORT);
    	CONNECTION_TIMEOUT = (EditText) findViewById(R.id.CONNECTION_TIMEOUT);
    	SOCKET_TIMEOUT = (EditText) findViewById(R.id.SOCKET_TIMEOUT);
    	SERVER_SUB_DOMAIN = (EditText) findViewById(R.id.SERVER_SUB_DOMAIN);
    	URL_PREFIX = (EditText) findViewById(R.id.URL_PREFIX);
    	URL_API_LOCATE = (EditText) findViewById(R.id.URL_API_LOCATE);
    	URL_API_COLLECT = (EditText) findViewById(R.id.URL_API_COLLECT);
    	URL_API_QUERY = (EditText) findViewById(R.id.URL_API_QUERY);
    	URL_API_NFC_COLLECT = (EditText) findViewById(R.id.URL_API_NFC_COLLECT);
    	URL_API_LOCATE_BASE_NFC = (EditText) findViewById(R.id.URL_API_LOCATE_BASE_NFC);
    	ADS_ENABLED = (CheckBox) findViewById(R.id.ADS_ENABLED);
    	BANNERS_ENABLED = (CheckBox) findViewById(R.id.BANNERS_ENABLED);
    	ENTRY_NEEDED = (CheckBox) findViewById(R.id.ENTRY_NEEDED);
    	GOOGLE_MAP_EMBEDDED = (CheckBox) findViewById(R.id.GOOGLE_MAP_EMBEDDED);
    	BACKGROUND_LINES_NEEDED = (CheckBox) findViewById(R.id.BACKGROUND_LINES_NEEDED);
    	
    	resetToSavedValues();
    }
    
    private void saveValues() {
    	
    	String name = null;
    	String value = null;
    	
    	name = "PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR";
    	value = String.valueOf(PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR.isChecked());
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER";
    	value = String.valueOf(PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER.isChecked());
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "PERIODIC_WIFI_CAPTURE_INTERVAL";
    	value = PERIODIC_WIFI_CAPTURE_INTERVAL.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "MAX_AGE_FOR_WIFI_SAMPLES";
    	value = MAX_AGE_FOR_WIFI_SAMPLES.getText().toString();
    	Tuner.getProperties().setProperty(name, value);

    	name = "MIN_WIFI_SAMPLES_BUFFERED";
    	value = MIN_WIFI_SAMPLES_BUFFERED.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	    	
    	name = "PERIODIC_LOCATE_INTERVAL";
    	value = PERIODIC_LOCATE_INTERVAL.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "PERIODIC_WIFI_SCAN_INTERVAL";
    	value = PERIODIC_WIFI_SCAN_INTERVAL.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	    	
    	name = "MAX_FINGERPRINTS_FOR_LOCATE";
    	value = MAX_FINGERPRINTS_FOR_LOCATE.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "MAX_WAIT_MS_FOR_LOCATE";
    	value = MAX_WAIT_MS_FOR_LOCATE.getText().toString();
    	Tuner.getProperties().setProperty(name, value);

    	name = "MAX_FINGERPRINTS_FOR_COLLECT";
    	value = MAX_FINGERPRINTS_FOR_COLLECT.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "MAX_WAIT_MS_FOR_COLLECT";
    	value = MAX_WAIT_MS_FOR_COLLECT.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "MIN_DBM_COUNT_IN";
    	value = MIN_DBM_COUNT_IN.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "MAX_DBM_COUNT_IN";
    	value = MAX_DBM_COUNT_IN.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "MIN_AP_COUNT_IN";
    	value = MIN_AP_COUNT_IN.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "DEBUG";
    	value = String.valueOf(DEBUG.isChecked());
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "SERVER_RUNNING_IN_LINUX";
    	value = String.valueOf(SERVER_RUNNING_IN_LINUX.isChecked());
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "LINUX_SERVER_IP";
    	value = LINUX_SERVER_IP.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "LINUX_SERVER_PORT";
    	value = LINUX_SERVER_PORT.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "USE_DOMAIN_NAME";
    	value = String.valueOf(USE_DOMAIN_NAME.isChecked());
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "SERVER_DOMAIN_NAME";
    	value = SERVER_DOMAIN_NAME.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "SERVER_IP";
    	value = SERVER_IP.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "SERVER_PORT";
    	value = SERVER_PORT.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "CONNECTION_TIMEOUT";
    	value = CONNECTION_TIMEOUT.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "SOCKET_TIMEOUT";
    	value = SOCKET_TIMEOUT.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "SERVER_SUB_DOMAIN";
    	value = SERVER_SUB_DOMAIN.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "URL_PREFIX";
    	value = URL_PREFIX.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "URL_API_LOCATE";
    	value = URL_API_LOCATE.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "URL_API_COLLECT";
    	value = URL_API_COLLECT.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "URL_API_QUERY";
    	value = URL_API_QUERY.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "URL_API_NFC_COLLECT";
    	value = URL_API_NFC_COLLECT.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "URL_API_LOCATE_BASE_NFC";
    	value = URL_API_LOCATE_BASE_NFC.getText().toString();
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "ADS_ENABLED";
    	value = String.valueOf(ADS_ENABLED.isChecked());
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "BANNERS_ENABLED";
    	value = String.valueOf(BANNERS_ENABLED.isChecked());
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "ENTRY_NEEDED";
    	value = String.valueOf(ENTRY_NEEDED.isChecked());
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "GOOGLE_MAP_EMBEDDED";
    	value = String.valueOf(GOOGLE_MAP_EMBEDDED.isChecked());
    	Tuner.getProperties().setProperty(name, value);
    	
    	name = "BACKGROUND_LINES_NEEDED";
    	value = String.valueOf(BACKGROUND_LINES_NEEDED.isChecked());
    	Tuner.getProperties().setProperty(name, value);
 
    	Tuner.saveConfig();
		Tuner.syncToConfig();
	}

	private void resetToSavedValues() {
		PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR.setChecked(IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR);
    	PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER.setChecked(IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER);
    	PERIODIC_WIFI_CAPTURE_INTERVAL.setText(String.valueOf(IndoorMapData.PERIODIC_WIFI_CAPTURE_INTERVAL));
    	MAX_AGE_FOR_WIFI_SAMPLES.setText(String.valueOf(IndoorMapData.MAX_AGE_FOR_WIFI_SAMPLES));
    	MIN_WIFI_SAMPLES_BUFFERED.setText(String.valueOf(IndoorMapData.MIN_WIFI_SAMPLES_BUFFERED));
    	PERIODIC_LOCATE_INTERVAL.setText(String.valueOf(IndoorMapData.PERIODIC_LOCATE_INTERVAL));
    	PERIODIC_WIFI_SCAN_INTERVAL.setText(String.valueOf(IndoorMapData.PERIODIC_WIFI_SCAN_INTERVAL));
    	MAX_FINGERPRINTS_FOR_LOCATE.setText(String.valueOf(IndoorMapData.MAX_FINGERPRINTS_FOR_LOCATE));
    	MAX_WAIT_MS_FOR_LOCATE.setText(String.valueOf(IndoorMapData.MAX_WAIT_MS_FOR_LOCATE));
    	MAX_FINGERPRINTS_FOR_COLLECT.setText(String.valueOf(IndoorMapData.MAX_FINGERPRINTS_FOR_COLLECT));
    	MAX_WAIT_MS_FOR_COLLECT.setText(String.valueOf(IndoorMapData.MAX_WAIT_MS_FOR_COLLECT));
    	MIN_DBM_COUNT_IN.setText(String.valueOf(IndoorMapData.MIN_DBM_COUNT_IN));
    	MAX_DBM_COUNT_IN.setText(String.valueOf(IndoorMapData.MAX_DBM_COUNT_IN));
    	MIN_AP_COUNT_IN.setText(String.valueOf(IndoorMapData.MIN_AP_COUNT_IN));
    	DEBUG.setChecked(WifiIpsSettings.DEBUG);
    	SERVER_RUNNING_IN_LINUX.setChecked(WifiIpsSettings.SERVER_RUNNING_IN_LINUX);
    	LINUX_SERVER_IP.setText(String.valueOf(WifiIpsSettings.LINUX_SERVER_IP));
    	LINUX_SERVER_PORT.setText(String.valueOf(WifiIpsSettings.LINUX_SERVER_PORT));
    	USE_DOMAIN_NAME.setChecked(WifiIpsSettings.USE_DOMAIN_NAME);
    	SERVER_DOMAIN_NAME.setText(String.valueOf(WifiIpsSettings.SERVER_DOMAIN_NAME));
    	SERVER_IP.setText(String.valueOf(WifiIpsSettings.SERVER_IP));
    	SERVER_PORT.setText(String.valueOf(WifiIpsSettings.SERVER_PORT));
    	CONNECTION_TIMEOUT.setText(String.valueOf(WifiIpsSettings.CONNECTION_TIMEOUT));
    	SOCKET_TIMEOUT.setText(String.valueOf(WifiIpsSettings.SOCKET_TIMEOUT));
    	SERVER_SUB_DOMAIN.setText(String.valueOf(WifiIpsSettings.SERVER_SUB_DOMAIN));
    	URL_PREFIX.setText(String.valueOf(WifiIpsSettings.URL_PREFIX));
    	URL_API_LOCATE.setText(String.valueOf(WifiIpsSettings.URL_API_LOCATE));
    	URL_API_COLLECT.setText(String.valueOf(WifiIpsSettings.URL_API_COLLECT));
    	URL_API_QUERY.setText(String.valueOf(WifiIpsSettings.URL_API_QUERY));
    	URL_API_NFC_COLLECT.setText(String.valueOf(WifiIpsSettings.URL_API_NFC_COLLECT));
    	URL_API_LOCATE_BASE_NFC.setText(String.valueOf(WifiIpsSettings.URL_API_LOCATE_BASE_NFC));  	
    	ADS_ENABLED.setChecked(VisualParameters.ADS_ENABLED);
    	BANNERS_ENABLED.setChecked(VisualParameters.BANNERS_ENABLED);
    	ENTRY_NEEDED.setChecked(VisualParameters.ENTRY_NEEDED);
    	GOOGLE_MAP_EMBEDDED.setChecked(VisualParameters.GOOGLE_MAP_EMBEDDED);
    	BACKGROUND_LINES_NEEDED.setChecked(VisualParameters.BACKGROUND_LINES_NEEDED);
	}
}
