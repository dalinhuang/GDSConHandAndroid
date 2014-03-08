package com.winjune.wifiindoor.version;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.IpsWebService;
import com.winjune.wifiindoor.webservice.messages.IpsMsgConstants;
import com.winjune.wifiindoor.webservice.types.ApkVersionReply;
import com.winjune.wifiindoor.webservice.types.ApkVersionRequest;

public class ApkVersionManager {
	
	private static int apkVersionCode;
	private static String apkVersionName;
	private static boolean apkVersionChecked = false;
	private static boolean apkUpdatePending = false;
	private static ApkVersionReply apkVersionReply;	
	
	
	public static void initApkVersionInfo(Activity activity){
		
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
	}
	
	public static int getApkVersionCode() {
		return apkVersionCode;
	}	
	
	public static void setApkVersionCode(int apkVersionCode) {
		ApkVersionManager.apkVersionCode = apkVersionCode;
	}

	public static String getApkVersionName() {
		return apkVersionName;
	}

	public static void setApkVersionName(String apkVersionName) {
		ApkVersionManager.apkVersionName = apkVersionName;
	}

	public static boolean isApkVersionChecked() {
		return apkVersionChecked;
	}

	public static void setApkVersionChecked(boolean apkVersionChecked) {
		ApkVersionManager.apkVersionChecked = apkVersionChecked;
	}

	public static boolean isApkUpdatePending() {
		return apkUpdatePending;
	}

	public static void setApkUpdatePending(boolean apkUpdatePending) {
		ApkVersionManager.apkUpdatePending = apkUpdatePending;
	}

	public static ApkVersionReply getApkVersionReply() {
		return apkVersionReply;
	}

	public static void setApkVersionReply(ApkVersionReply apkVersionReply) {
		ApkVersionManager.apkVersionReply = apkVersionReply;
	}
	
	public static void doNewVersionUpdate(final Activity activity) {

		if (apkVersionReply == null) {
			return;
		}
		
		setApkUpdatePending(false);
		
		StringBuffer sb = new StringBuffer();  
	    sb.append(activity.getString(R.string.apk_current_version));  
	    sb.append(getApkVersionName());  
	    sb.append(" Code:");  
	    sb.append(getApkVersionCode());  
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
                    Util.downFile(activity, 
                    		Util.fullUrl(IndoorMapData.APK_FILE_PATH_REMOTE, apkVersionReply.getApkUrl()),
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
	
	public static void CheckVersionUpgrade(Activity activity) {
		// Send back the client's version code & name for statistics purpose
		ApkVersionRequest version = new ApkVersionRequest(getApkVersionCode(), getApkVersionName());
		
		try {
			Gson gson = new Gson();
			String json = gson.toJson(version);
			JSONObject data = new JSONObject(json);

			if (IpsWebService.sendMessage(activity, IpsMsgConstants.MT_APK_VERSION_QUERY, data)) {
				Util.showShortToast(activity, R.string.query_apk_version);
			} else {
				// All errors should be handled in the sendToServer
				// method
			}
		} catch (Exception ex) {
			Util.showToast(activity, "GET APK VERSION ERROR: " + ex.getMessage(), Toast.LENGTH_LONG);
			ex.printStackTrace();
		}
	}
	
	public static void handleApkVersionReply(Activity activity, ApkVersionReply version) {
		setApkVersionChecked(true);
		setApkVersionReply(version);
		
		if (version.getVersionCode() > getApkVersionCode() ) {
			if (!Util.isNetworkConfigPending()) {
				doNewVersionUpdate(activity);
			} else {
				setApkUpdatePending(true);
			}	
		} else {
			Util.showShortToast(activity, R.string.latest_apk_version);
		}
	}		
		
}
