package com.winjune.wifiindoor.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.winjune.wifiindoor.R;
import com.google.gson.Gson;
import com.winjune.wifiindoor.drawing.graphic.ImageLoader;
import com.winjune.wifiindoor.network.CellInfoManager;
import com.winjune.wifiindoor.network.NetworkInfoManager;
import com.winjune.wifiindoor.network.NfcInfoManager;
import com.winjune.wifiindoor.network.WifiInfoManager;
import com.winjune.wifiindoor.runtime.RuntimeIndoorMap;
import com.winjune.wifiindoor.webservice.IpsMessageHandler;
import com.winjune.wifiindoor.webservice.MsgConstants;
import com.winjune.wifiindoor.webservice.OutgoingMessageQueue;
import com.winjune.wifiindoor.webservice.WebService;
import com.winjune.wifiindoor.webservice.types.ApkVersionReply;
import com.winjune.wifiindoor.webservice.types.ApkVersionRequest;
import com.winjune.wifiindoor.webservice.types.NfcFingerPrint;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Util {
	
	private static Resources resources = null;
	private static WifiInfoManager wifiInfoManager = null;
	private static CellInfoManager cellInfoManager = null;
	private static NetworkInfoManager networkInfoManager = null;
	private static NfcInfoManager nfcInfoManager = null;
	private static Vibrator vibrator = null;
	private static SensorManager sensorManager = null;
	private static IpsMessageHandler ipsMessageHandler = null;
	private static String deviceName;
	private static String accountName;
	private static int apkVersionCode;
	private static String apkVersionName;
	private static boolean apkVersionChecked;
	private static boolean apkUpdatePending;
	private static ApkVersionReply apkVersionReply;
	private static boolean networkConfigPending;
	private static boolean networkConfigShowing;
	private static boolean httpConnectionEstablished;
	private static boolean serverReachable;
	
	private static boolean initialed = false;
	private static boolean saveEnergy = true;
	
	private static RuntimeIndoorMap runtimeIndoorMap = null;
		
	private static Toast toast = null;
	
	private static ProgressDialog pBar;
	private static Handler handler = new Handler();
	
	private static Activity currentForegroundActivity = null;
	
	public static void initial(Activity activity){		
		if (initialed) {
			return;
		}
		
		// Change the configuration from configuration file
        Tuner.initial();
		
		resources = activity.getResources();
		
		setApkUpdatePending(false);
		setApkVersionChecked(false);
		setNetworkConfigPending(false);
		setNetworkConfigShowing(false);
		setHttpConnectionEstablished(false);
		setServerReachable(false);
		
		setCurrentForegroundActivity(activity);
		
		if (wifiInfoManager == null){
			setWifiInfoManager(new WifiInfoManager(activity.getApplicationContext()));
			if (wifiInfoManager.isWifiEnabled()) { // Try to open WIFI if it is not enabled			
				keepScannning();
			}
		}
		
		if (cellInfoManager == null){
			setCellInfoManager(new CellInfoManager(activity.getApplicationContext()));
		}
		
		if (networkInfoManager == null){
			setNetworkInfoManager(new NetworkInfoManager(activity.getApplicationContext()));
		}
		
		if (nfcInfoManager == null) {
			setNfcInfoManager(new NfcInfoManager(activity.getApplicationContext()));
		}
		
		if (vibrator == null){
			setVibrator((Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE)); 
		}
		
		if (sensorManager == null){
			setSensorManager((SensorManager) activity.getSystemService(Context.SENSOR_SERVICE)); 
		}
		
		if (ipsMessageHandler == null){
			setIpsMessageHandler(new IpsMessageHandler());
			ipsMessageHandler.setActivity(activity);
		}
		
		setDeviceName(Build.MODEL);
		
		AccountManager accountManager = AccountManager.get(activity.getApplicationContext()); 
		Account[] accounts = accountManager.getAccountsByType("com.google");
		if (accounts.length > 0) {
			setAccountName(accounts[0].name);
		} else {
			setAccountName(wifiInfoManager.getMyMac());
		}
		
	    try {
	        PackageManager manager = activity.getPackageManager();
	        PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
	        setApkVersionCode(info.versionCode);
	        setApkVersionName(info.versionName);
	    } catch (Exception e) {
	        e.printStackTrace();
	        setApkVersionCode(-1);
	        setApkVersionName("error");
	    }
	    
	    setApkVersionChecked(false);
	    setApkUpdatePending(false);
	    setApkVersionReply(null);
	   	
		initialed = true;
	}
	
	private static void keepScannning() {		
		saveEnergy = false;
		 
		new Thread(){
			 public void run(){
			    while (true) { // Forever running				 
					if (!saveEnergy) {
				    	// Keep Scanning & Try to buffer when application on
				    	wifiInfoManager.saveWifiInBackground();
				    }
				    
				    long waitMs = IndoorMapData.PERIODIC_WIFI_SCAN_INTERVAL;
				    
				    if (IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_COLLECTER || IndoorMapData.PERIODIC_WIFI_CAPTURE_ON_FOR_LOCATOR) {
				    	waitMs = IndoorMapData.PERIODIC_WIFI_CAPTURE_INTERVAL;
				    }
				    
				    try {
						sleep(waitMs);
					} catch (InterruptedException e) {
						
					}
			    } //while (true) { 
			 }
		 }.start();	 
	}
	
	public static void setEnergySave(boolean b) {
		saveEnergy = b;
	}

	// Align Number Text
	public static String alignInt(int number, int size){
		String text = Integer.toString(number);
		int zeroNum = size - text.length();
		
		// We do not care if the length() > size, for the resouce calculator should consider this
		
		for (int i=0; i<zeroNum; i++){
			text = "0" + text;
		}
		
		return text;
	}
	
	public static Resources getResources(){
		return resources;
	}

	public static WifiInfoManager getWifiInfoManager() {
		return wifiInfoManager;
	}

	public static void setWifiInfoManager(WifiInfoManager wifiInfoManager) {
		Util.wifiInfoManager = wifiInfoManager;
	}

	public static CellInfoManager getCellInfoManager() {
		return cellInfoManager;
	}

	public static void setCellInfoManager(CellInfoManager cellInfoManager) {
		Util.cellInfoManager = cellInfoManager;
	}

	public static NetworkInfoManager getNetworkInfoManager() {
		return networkInfoManager;
	}

	public static void setNetworkInfoManager(NetworkInfoManager networkInfoManager) {
		Util.networkInfoManager = networkInfoManager;
	}
	
	public static NfcInfoManager getNfcInfoManager() {
		return nfcInfoManager;
	}

	public static void setNfcInfoManager(NfcInfoManager nfcInfoManager) {
		Util.nfcInfoManager = nfcInfoManager;
	}

	public static Vibrator getVibrator() {
		return vibrator;
	}

	public static void setVibrator(Vibrator vibrator) {
		Util.vibrator = vibrator;
	}

	public static void showLongToast(final Activity activity, final int resId) {
		activity.runOnUiThread(new Runnable() {
			  public void run() {
				  if(toast == null) {
					  toast = Toast.makeText(activity, resId, Toast.LENGTH_LONG);
				  } else {
					  toast.setText(resId);
					  toast.setDuration(Toast.LENGTH_LONG);
				  }
				  
				  toast.show();			    
			  }
		});
	}
	
	public static void showShortToast(final Activity activity, final int resId) {
		activity.runOnUiThread(new Runnable() {
			  public void run() {
				  if(toast == null) {
					  toast = Toast.makeText(activity, resId, Toast.LENGTH_SHORT);
				  } else {
					  toast.setText(resId);
					  toast.setDuration(Toast.LENGTH_SHORT);
				  }
			  
				  toast.show();
			  }
		});		
	}
	
	public static void showToast(final Activity activity, final int resId, final int duration) {
		activity.runOnUiThread(new Runnable() {
			  public void run() {
				  if(toast == null) {
					  toast = Toast.makeText(activity, resId, duration);
				  } else {
					  toast.setText(resId);
					  toast.setDuration(duration);
				  }
			  
				  toast.show();
			  }
		});	
	}
	
	public static void showToast(final Activity activity, final String text, final int duration) {
		activity.runOnUiThread(new Runnable() {
			  public void run() {
				  if(toast == null) {
					  toast = Toast.makeText(activity, text, duration);
				  } else {
					  toast.setText(text);
					  toast.setDuration(duration);
				  }
			  
				  toast.show();
			  }
		});	
	}
	
	public static void cancelToast() {  
        if (toast != null) {  
        	toast.cancel();  
        }  
    }

	public static IpsMessageHandler getIpsMessageHandler() {
		return ipsMessageHandler;
	}

	public static void setIpsMessageHandler(IpsMessageHandler ipsMessageHandler) {
		Util.ipsMessageHandler = ipsMessageHandler;
	}
	
	public static String getDeviceName(){
		return deviceName;
	}
	
	public static void setDeviceName(String deviceName){
		Util.deviceName = deviceName;
	}
	
	public static String getAccountName(){
		return accountName;
	}
	
	public static void setAccountName(String accountName){
		Util.accountName = accountName;
	}

	public static RuntimeIndoorMap getRuntimeIndoorMap() {
		return runtimeIndoorMap;
	}

	public static void setRuntimeIndoorMap(RuntimeIndoorMap runtimeIndoorMap) {
		Util.runtimeIndoorMap = runtimeIndoorMap;
	}
	
	public static int getCurrentCellPixel() {
		if (runtimeIndoorMap == null) {
			return 0;
		}
		
		return runtimeIndoorMap.getCellPixel();
	}
	
	public static int getApkVersionCode() {
		return apkVersionCode;
	}	
	
	public static void setApkVersionCode(int apkVersionCode) {
		Util.apkVersionCode = apkVersionCode;
	}

	public static String getApkVersionName() {
		return apkVersionName;
	}

	public static void setApkVersionName(String apkVersionName) {
		Util.apkVersionName = apkVersionName;
	}

	public static boolean isApkVersionChecked() {
		return apkVersionChecked;
	}

	public static void setApkVersionChecked(boolean apkVersionChecked) {
		Util.apkVersionChecked = apkVersionChecked;
	}

	public static boolean isApkUpdatePending() {
		return apkUpdatePending;
	}

	public static void setApkUpdatePending(boolean apkUpdatePending) {
		Util.apkUpdatePending = apkUpdatePending;
	}

	public static ApkVersionReply getApkVersionReply() {
		return apkVersionReply;
	}

	public static void setApkVersionReply(ApkVersionReply apkVersionReply) {
		Util.apkVersionReply = apkVersionReply;
	}
	
	@SuppressLint("NewApi")
	public static void configWifiNetwork() {
		if (isNetworkConfigShowing()) {
			return;
		}
		
		if ((currentForegroundActivity == null) || (currentForegroundActivity.isFinishing())) {
			return;
		}
		
		if ((android.os.Build.VERSION.SDK_INT >= 17) && (currentForegroundActivity.isDestroyed())) {
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(currentForegroundActivity);
		setNetworkConfigPending(true);
		
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(R.string.config_network);
		builder.setMessage(R.string.mobile_2g_3g_to_wifi);
		builder.setPositiveButton(R.string.config, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
	
				if (android.os.Build.VERSION.SDK_INT > 10) {
					intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName(
							"com.android.settings",
							"com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				currentForegroundActivity.startActivity(intent);
				setNetworkConfigPending(false);
				setNetworkConfigShowing(false);
			}
		});

		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setNetworkConfigPending(false);
				setNetworkConfigShowing(false);
				if (Util.isApkUpdatePending()) {
					Util.doNewVersionUpdate(currentForegroundActivity);
				}
			}
		});
		
		builder.create();
		builder.show();
		setNetworkConfigShowing(true);
	}
	
	@SuppressLint("NewApi")
	public static void configWirelessNetwork() {
		if (isNetworkConfigShowing()) {
			return;
		}
		
		if ((currentForegroundActivity == null) || (currentForegroundActivity.isFinishing())) {
			return;
		}
		
		if ((android.os.Build.VERSION.SDK_INT >= 17) && (currentForegroundActivity.isDestroyed())) {
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(currentForegroundActivity);
		
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(R.string.config_network);
		builder.setMessage(R.string.mobile_no_wireless);
		builder.setPositiveButton(R.string.config, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;

				if (android.os.Build.VERSION.SDK_INT > 10) {
					intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName(
							"com.android.settings",
							"com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				currentForegroundActivity.startActivity(intent);
				setNetworkConfigPending(false);
				setNetworkConfigShowing(false);
			}
		});

		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setNetworkConfigPending(false);
				setNetworkConfigShowing(false);
				if (isApkUpdatePending()) {
					// This will never be true and also we can not do update without network connection
					// but I just leave it here
					Util.doNewVersionUpdate(currentForegroundActivity);
				}
			}
		});
		
		builder.create();
		builder.show();
		setNetworkConfigShowing(true);
	}
	
	public static String fullUrl(String short_path, String file_name) {
		return WifiIpsSettings.URL_PREFIX + WifiIpsSettings.SERVER + "/" + short_path + file_name;
	}
	
	public static void doNewVersionUpdate(final Activity activity) {

		if (apkVersionReply == null) {
			return;
		}
		
		setApkUpdatePending(false);
		
		StringBuffer sb = new StringBuffer();  
	    sb.append(activity.getString(R.string.apk_current_version));  
	    sb.append(Util.getApkVersionName());  
	    sb.append(" Code:");  
	    sb.append(Util.getApkVersionCode());  
	    sb.append(", " + activity.getString(R.string.apk_new_version));  
	    sb.append(apkVersionReply.getVersionName());  
	    sb.append(" Code:");  
	    sb.append(apkVersionReply.getVersionCode());  
	    sb.append(", " + activity.getString(R.string.apk_update_or_not));  
	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(activity);	
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(R.string.apk_new_version_available);	
		builder.setMessage(sb.toString());  
		
		builder.setPositiveButton(R.string.yes,
            new OnClickListener() {  
                @Override  
                public void onClick(DialogInterface dialog,  
                        int which) { 
                    downFile(activity, 
                    		fullUrl(IndoorMapData.APK_FILE_PATH_REMOTE, apkVersionReply.getApkUrl()),
                    		IndoorMapData.APK_FILE_PATH_LOCAL,
                    		"WifiIPS_"+apkVersionReply.getVersionName()+".apk",
                    		true,             // Open after download
                    		"application/vnd.android.package-archive",
                    		true,           // Use Handler
                    		true); 			// Use Thread
                }  
            });
	    
	    builder.setNegativeButton(R.string.no,  
            new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog,  
                        int whichButton) { 
                    //finish();  
                }  
            });
	   
	    builder.create();
	    builder.show();
	}
	
	public static String getFilePath() {
		return getFilePath("");
	}
	
	public static String getFilePath(String relativePath) {
		String filePath = WifiIpsSettings.FILE_CACHE_FOLDER + relativePath;
    	String sdStatus = Environment.getExternalStorageState();

		if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			return filePath;
		} else {
			return Environment.getExternalStorageDirectory().getPath() + filePath;
		}
	}
	
	public static String getMapFilePathName(String mapId) {
		return getFilePath(IndoorMapData.MAP_FILE_PATH_LOCAL) + mapId + "/" + IndoorMapData.MAP_XML_NAME;
	}
	
	public static String getMapInfoFilePathName(String mapId) {
		return getFilePath(IndoorMapData.MAP_FILE_PATH_LOCAL) + mapId + "/" + IndoorMapData.MAP_INFO_FILE_NAME;
	}
	
	public static String getInterestPlacesInfoFilePathName(String mapId) {
		return getFilePath(IndoorMapData.MAP_FILE_PATH_LOCAL) + mapId + "/" + IndoorMapData.INTEREST_PLACE_INFO_FILE_NAME;
	}	
	
	public static String getNaviInfoFilePathName(String mapId) {
		return getFilePath(IndoorMapData.MAP_FILE_PATH_LOCAL) + mapId + "/" + IndoorMapData.NAVI_INFO_FILE_NAME;
	}
	
	public static String getMapPicturePathName(String mapId, String pictureName) {
		return getFilePath(IndoorMapData.MAP_FILE_PATH_LOCAL) + mapId + "/" + pictureName;
	}

	public static boolean downFile(final Activity activity, final String url, final String localRelativePath, final String localFileName, 
			final boolean openAfterDone, final String mimeType, 
			final boolean useHandler, final boolean useThread) {
		
		if (useHandler) {
			pBar = new ProgressDialog(activity);  
	        
			pBar.setTitle(R.string.download_ongoing);  
	        pBar.setMessage(activity.getResources().getText(R.string.please_wait));  
	        pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
	        pBar.setCanceledOnTouchOutside(false);
			pBar.show(); 
		}
		
		if (!useThread) {
			return downloadFile(activity, url, localRelativePath, localFileName, openAfterDone, mimeType, useHandler);
		} else {
			new Thread() {  
		        public void run() {  
		        	downloadFile(activity, url, localRelativePath, localFileName, openAfterDone, mimeType, useHandler);
		        }  
		    }.start(); 
		}
		
		return true;  // If we choose to use a new Thread, that means we do not care about the result
	}
	
	private static boolean downloadFile(Activity activity, String url,
			String localRelativePath, String localFileName,
			boolean openAfterDone, String mimeType, boolean useHandler) {
		
		HttpClient client = new DefaultHttpClient();  
        HttpGet get = new HttpGet(url);  
        HttpResponse response;  
        
        try {  
            response = client.execute(get);  
            
            // In case the status code is not 200 OK
            if (response.getStatusLine().getStatusCode() != 200) {
            	Log.i("Download File", "Download file " + url + " failed with reason code: " + response.getStatusLine().getStatusCode());
            	showLongToast(activity, R.string.download_file_failed);
            	return false;
            }
            
            HttpEntity entity = response.getEntity();  
            long length = entity.getContentLength();  
            InputStream is = entity.getContent();  
            FileOutputStream fileOutputStream = null;

            Log.i("Download File", url);
            
            if (is != null) {	                    
            	File file = openOrCreateFileInPath(localRelativePath, localFileName+"_download", true);
            	
            	if (file == null) {
            		showLongToast(activity, R.string.download_file_failed);
            		return false;
            	}
            	
            	fileOutputStream = new FileOutputStream(file);  
                byte[] buf = new byte[1024];  
                int ch = -1;  
				int count = 0; 
				int update_count = 500;
                while ((ch = is.read(buf)) != -1) { 
                	//Log.i("Read", ch+"");
                    fileOutputStream.write(buf, 0, ch);  
                    count += ch;  
                    //Log.i("Write", ch+"");
                    update_count++;
                    if (update_count >= 512) {
                    	updateProgress(count, length, useHandler);
                    	update_count = 0;
                    	fileOutputStream.flush(); 
                    }
                }  

	            fileOutputStream.flush();  
	            
	            if (fileOutputStream != null) {  
	                fileOutputStream.close();  
	            }
	            
	            file.renameTo(new File(getFilePath(localRelativePath), localFileName));
            } 
            
            doneDownload(activity, getFilePath(localRelativePath), localFileName, openAfterDone, mimeType, useHandler); 
            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        }  

        return false;
	}
	
	private static void updateProgress(final int count, final long length, boolean useHandler) {
		if (!useHandler) {
			return;
		}
		
        handler.post(new Runnable() {
            public void run() {
            	long percent = (long) count * 100 / length;
            	
            	if (count < 1024) {
            		pBar.setMessage(percent + "%(" + count + "/" + length + "B)");
            	} else {
            		pBar.setMessage(percent + "%(" + count/1024 + "/" + length/1024 + "KB)");
            	}
            }
	   });
	}

	private static void doneDownload(final Activity activity, final String filePath, final String fileName, final boolean openAfterDone, final String mimeType, final boolean useHandler) {
        handler.post(new Runnable() {
            public void run() {
            	if (useHandler) {
            		pBar.cancel();
            	}
            	
                if (openAfterDone) {
                	update(activity, filePath, fileName, mimeType);
                }
            }
	   });
	}	
	
    private static void update(final Activity activity, final String filePath, final String fileName, final String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath, fileName)), mimeType);
        activity.startActivity(intent);
    }

	public static boolean isNetworkConfigPending() {
		return networkConfigPending;
	}

	public static void setNetworkConfigPending(boolean networkConfigPending) {
		Util.networkConfigPending = networkConfigPending;
	}

	public static boolean isHttpConnectionEstablished() {
		return httpConnectionEstablished;
	}

	public static void setHttpConnectionEstablished(
			boolean httpConnectionEstablished) {
		Util.httpConnectionEstablished = httpConnectionEstablished;
	}

	public static File openOrCreateFileInPath(String relativePath, String fileName, boolean deleteOldFile) {
		//create the path & file if not exist
		File dir = null;
		File file = null;

		//If file or directory not exist, create it.
		try {
			//create file on device
			dir = new File(getFilePath(relativePath));
		    file = new File (dir, fileName);
		    
		    // Delete old file
		    if (deleteOldFile) {
		    	if (file.exists()) {
		    		file.delete();
		    	}
		    }
			
			//Create DIR and FILE if not exist
			if (!dir.exists()){
				if (!dir.mkdirs()){
					Log.e("File", "    ---------create dir " + dir.getPath() + " failed");
					return null;
				} else {
					if (!file.createNewFile()){
						Log.e("File", "    ---------create file " + file.getPath() + " failed");
						return null;
					}
				}
			} else {
				if(!file.exists()){
					if (!file.createNewFile()){
						Log.e("File", "    ---------create file " + file.getPath() + " failed");
						return null;
					}
				}
			}
		} catch (IOException ex){
			ex.printStackTrace();
			return null;
		}
		
		return file;
	}
	
	public static boolean sendToServer(Activity activity, int requestCode, JSONObject data) {
		
		Log.e("Request", "Code: " + requestCode + ", Data" + data.toString());
		
		if (!isServerReachable()) {
			showLongToast(activity, R.string.server_unreachable);
			return false;
		}
		
		if (!isHttpConnectionEstablished()) {
			showLongToast(activity, R.string.no_http_connection);
			return false;
		}
		
		if (!getNetworkInfoManager().isConnected()) {
			showLongToast(activity, R.string.no_data_connection);
			return false;
		}
		
		JSONObject json = new JSONObject();
		try {
			json.put("RequestCode", requestCode);
			json.put("RequestPayload", data);
			if (Util.isHttpConnectionEstablished()) {
				OutgoingMessageQueue.offer(json);
			} else {
				final JSONObject json1 = json;
				
				// Use a Thread to wait for 1 more minute if the HTTP connection is not ready
				new Thread() {
					public void run() {
						for (int counter=0;counter<60;counter++) { // Run 60 * 1 s
							if (Util.isHttpConnectionEstablished()) {
								OutgoingMessageQueue.offer(json1);
								break;
							}
							
							try {
								sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}.start();
			}

			return true;
		} catch (JSONException e) {
			showToast(activity, "031 " + e.toString(), Toast.LENGTH_LONG);
		}
		

		return false;
	}

	public static void downloadMapPicture(Activity activity, String mapId, String pictureName) {
		 downFile(activity, 
				fullUrl(IndoorMapData.MAP_FILE_PATH_REMOTE + mapId + "/", pictureName),
         		IndoorMapData.MAP_FILE_PATH_REMOTE + mapId + "/",
         		pictureName,
         		false,   // No need to open after download
         		"",
         		false,   // Not need Handler
         		false);  // Not Use Thread, already inner a Thread
	}
	
	public static void initApp(final Activity activity) {				
		VisualParameters.initial(activity);
        ImageLoader.initial(activity);
        initialed = false;
        initial(activity);
        
        if (!getWifiInfoManager().isWifiEmbeded()) {
			showLongToast(activity, R.string.no_wifi_embeded);
			return;
		}
        
        // If can not enable the WIFI, pause and reject any action
        if (!getWifiInfoManager().isWifiEnabled()) {			
        	showLongToast(activity, R.string.no_wifi_enabled);	
			return;
		}
        
        // Configure network
        if (getNetworkInfoManager() != null) {
        	//Hoare: todo
        	
        	if (getNetworkInfoManager().is2G3GConnected()) {
        		configWifiNetwork();      		
        	} else {
        		if (!getNetworkInfoManager().isConnected()) {
        			configWirelessNetwork();
        		}
        	}
        	
        }
	}
	
	public static void connetcToServer(final Activity activity) {
		int counter = 0;
		 
		while (!WifiIpsSettings.getServerAddress(true)) 
	    { 
	    	if (counter >= 5) { // Max wait for 30 seconds for the network
	    		showLongToast(activity, R.string.wrong_server);
	    		return;
	    	}
			
			counter++;
			try {
				Thread.sleep(500);  // wait 500ms
			} catch (InterruptedException e) {
				continue;
			}
	    }
	    	
		loadWebService();
		
		if (getIpsMessageHandler() == null) {
			// showLongToast(activity, R.string.wrong_server);
			return;
		}
		
		// Start the Ips Message Handler Thread if it has not been started yet.
		getIpsMessageHandler().startTransportServiceThread();
		
		setHttpConnectionEstablished(true);
		
		//Hoare: bypass ping check since it doesn't work in some mobiles
		setServerReachable(true);
		//setServerReachable(WifiIpsSettings.isPingable());
				
		return;
	}
	
	private static void loadWebService() {
		WebService instance = WebService.getInstance();

		if (instance == null) {
			return;
		}

		instance.initialize(WifiIpsSettings.SERVER, getApkVersionName());
	}
	
	public static void CheckVersionUpgrade(Activity activity) {
		// Send back the client's version code & name for statistics purpose
		ApkVersionRequest version = new ApkVersionRequest(getApkVersionCode(), getApkVersionName());
		
		try {
			Gson gson = new Gson();
			String json = gson.toJson(version);
			JSONObject data = new JSONObject(json);

			if (sendToServer(activity, MsgConstants.MT_APK_VERSION_QUERY, data)) {
				showShortToast(activity, R.string.query_apk_version);
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			showToast(activity, "GET APK VERSION ERROR: " + ex.getMessage(), Toast.LENGTH_LONG);
			ex.printStackTrace();
		}
	}
	
	public static void handleApkVersionReply(Activity activity, ApkVersionReply version) {
		Util.setApkVersionChecked(true);
		Util.setApkVersionReply(version);
		
		if (version.getVersionCode() > Util.getApkVersionCode() ) {
			if (!Util.isNetworkConfigPending()) {
				Util.doNewVersionUpdate(activity);
			} else {
				Util.setApkUpdatePending(true);
			}	
		} else {
			Util.showShortToast(activity, R.string.latest_apk_version);
		}
	}	

	public static SensorManager getSensorManager() {
		return sensorManager;
	}

	public static void setSensorManager(SensorManager sensorManager) {
		Util.sensorManager = sensorManager;
	}

	public static void enableNfc(Activity activity) {
		if (nfcInfoManager == null) {
			return;
		}
		
		NfcAdapter mNfcAdapter = nfcInfoManager.getNfcAdapter();
		
		if (mNfcAdapter != null) {
			PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			
			IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED); //ACTION_NDEF_DISCOVERED
	        try {
	            ndef.addDataType("*/*");    // Handles all MIME based dispatches. You should specify only the ones that you need.
	        }
	        catch (MalformedMimeTypeException e) {
	            throw new RuntimeException("fail", e);
	        }
	        IntentFilter[] intentFiltersArray = new IntentFilter[] { ndef, };
			String[][] techListsArray = new String[][] { new String[] { MifareClassic.class.getName() } }; //NfcF.class.getName()
			
			mNfcAdapter.enableForegroundDispatch(activity, pendingIntent, intentFiltersArray, techListsArray);
		}
	}

	public static void disableNfc(Activity activity) {
		if (nfcInfoManager == null) {
			return;
		}
		
		NfcAdapter mNfcAdapter = nfcInfoManager.getNfcAdapter();
		
		if (mNfcAdapter != null) {
			try {
				mNfcAdapter.disableForegroundDispatch(activity);
			} catch (IllegalStateException e) {
				//e.printStackTrace();
			}
		}
	}
	
	public static void nfcQrLocateMe(Activity activity, String tagId) {
		// send Nfc Locate messsage to server
		NfcFingerPrint fingnerPrint = new NfcFingerPrint(tagId);
		
		showShortToast(activity, R.string.locate_base_on_nfc);
		
		try {
			Gson gson = new Gson();
			String json = gson.toJson(fingnerPrint);
			JSONObject data = new JSONObject(json);

			if (sendToServer(activity, MsgConstants.MT_LOCATE_FROM_NFC_QR, data)) {
				// Do nothing
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			showToast(activity, "NFC01 " + ex.toString(), Toast.LENGTH_LONG);
		}
	}

	public static void enableAcclerometer(SensorEventListener listener) {
		sensorManager.registerListener(listener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public static void disableAcclerometer(SensorEventListener listener) {
		sensorManager.unregisterListener(listener);
	}

	public static boolean isNetworkConfigShowing() {
		return networkConfigShowing;
	}

	public static void setNetworkConfigShowing(boolean networkConfigShowing) {
		Util.networkConfigShowing = networkConfigShowing;
	}

	public static boolean isShakeDetected(SensorEvent event) {
		int sensorType = event.sensor.getType();
		  
		//values[0]:X values[1]:Y values[2]: Z
		float[] values = event.values;
		  
		if (sensorType == Sensor.TYPE_ACCELEROMETER){		  
			
		   // Adjust the sensitivity, g=9.8
		   if ((Math.abs(values[0])>14 && Math.abs(values[1])>14)
				   || (Math.abs(values[0])>14 && Math.abs(values[2])>14)
				   || (Math.abs(values[1])>14 && Math.abs(values[2])>14)) {
		        // vibrate to let user know
		        Util.getVibrator().vibrate(500);	        
		        return true;
	       }
		}
		
		return false;
	}

	public static boolean isServerReachable() {
		return serverReachable;
	}

	public static void setServerReachable(boolean serverReachable) {
		Util.serverReachable = serverReachable;
	}	
	
	public static Activity getCurrentForegroundActivity() {
		return currentForegroundActivity;
	}

	public static void setCurrentForegroundActivity(
			Activity currentForegroundActivity) {
		Util.currentForegroundActivity = currentForegroundActivity;
	}	
}

