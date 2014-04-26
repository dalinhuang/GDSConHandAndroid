package com.winjune.wifiindoor.activity;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.POIViewer.AlarmActivity;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;

public class UserCenterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
	}
	
	public void backClick(View v){
		onBackPressed();
	}	
	
	public void shareMyPosBarClick(View v){
		
	}

	public void settingBarClick(View v){
        Intent i = new Intent(this, SettingActivity.class); 
		startActivity(i);		
	}
	
	public void feedbackBarClick(View v){
		
	}

	public void planningTuningBarClick(View v){
        Intent i = new Intent(this, TunerActivity.class); 
		startActivity(i);				
	}
	
	public void checkUpdateBarClick(View v){
		StringBuffer sb = new StringBuffer();  
		sb.append(this.getString(R.string.app_name));
		sb.append("V3.1版,更新说明:");
	    sb.append("\r\n" + "1. 解决部分情况下卡顿或重启问题；");  
	    sb.append("\r\n" + "2. 解决Android4.4系统下某些情况下无法识别存储卡问题；");
	    sb.append("\r\n" + "3. 性能优化。");
	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);	
		builder.setTitle("版本更新提示");	
		builder.setMessage(sb.toString());
	    builder.setNegativeButton(R.string.download_later,  
            new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog,  
                        int whichButton) { 
                    //finish();  
                }  
            });
	   
		builder.setPositiveButton(R.string.download_now,
	            new OnClickListener() {  
	                @Override  
	                public void onClick(DialogInterface dialog,  
	                        int which) { 
	                    Util.downFile(UserCenterActivity.this, 
	                    		Util.fullUrl(IndoorMapData.APK_FILE_PATH_REMOTE, "/"),
	                    		IndoorMapData.APK_FILE_PATH_LOCAL,
	                    		"WifiIPS_"+"GDSC"+".apk",
	                    		true,             // Open after download
	                    		"application/vnd.android.package-archive",
	                    		true,           // Use Handler
	                    		true); 			// Use Thread
	                }  
	            });
		    
	    builder.create();
	    builder.show();
	}
	
	public void aboutBarClick(View v){
		
	}
	
	public void exitAppBarClick(View v) {		
 
         setResult(RESULT_FIRST_USER);
         
         finish();
	}
	
	public void myFavorateClick(View v){
		
		Intent intent = new Intent(UserCenterActivity.this, AlarmActivity.class);
		startActivity(intent);
	}
}
