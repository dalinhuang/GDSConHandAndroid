package com.winjune.wifiindoor.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.InterestPlaceWebViewActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.widget.Toast;

public class ShareUtil {
	
	final static String sinaPackage = "com.sina.weibo";
	final static String sinaClassName = "com.sina.weibo.EditActivity";
	final static String weixinPackage = "com.tencent.mm";
	final static String weixinClassName = "com.tencent.mm.ui.tools.ShareImgUI";
	final static String pengyouquanClassName = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
	
	public static void sendToChooser(Activity caller, String content) {
	
        String shareContent = caller.getString(R.string.share_prefix) + content;
        
        Intent it = new Intent(Intent.ACTION_SEND);
        it.setType("image/*");
        List<ResolveInfo> resInfo = caller.getPackageManager().queryIntentActivities(it, 0);
        
        if (!resInfo.isEmpty()) {
        
        	List<Intent> targetedShareIntents = new ArrayList<Intent>();
            
        	for (ResolveInfo info : resInfo) {
        		
                ActivityInfo activityInfo = info.activityInfo;
               
                // judgments : activityInfo.packageName, activityInfo.name, etc.
                if (activityInfo.packageName.equalsIgnoreCase(sinaPackage)) {
                    
                    Intent targeted = new Intent(Intent.ACTION_SEND);
                    targeted.setType("text/plain");
                    
                    targeted.putExtra(Intent.EXTRA_TEXT, shareContent);
                    targeted.setPackage(activityInfo.packageName);
                    targetedShareIntents.add(targeted);
                }
                if (activityInfo.packageName.equalsIgnoreCase(weixinPackage)) {
                	
                    Intent targeted = new Intent(Intent.ACTION_SEND);
                    targeted.setType("image/*");
                    targeted.putExtra(Intent.EXTRA_TEXT, shareContent);
                    targeted.setPackage(activityInfo.packageName);
                    
                    if (activityInfo.name.equalsIgnoreCase(weixinClassName)) {
                    	targeted.setClassName(weixinPackage, weixinClassName);
                    }
                    if (activityInfo.name.equalsIgnoreCase(pengyouquanClassName)) {
                    	targeted.setClassName(weixinPackage, pengyouquanClassName);
                    }
                    
                    targetedShareIntents.add(targeted);
                }
                
            }    
        	
        	if (targetedShareIntents.isEmpty()) {
        		Toast.makeText(caller, "Can't find weibo or weixin", Toast.LENGTH_SHORT).show();
        		return;
        	}
        	
        	Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), caller.getString(R.string.selection_hint));
            if (chooserIntent == null) {
                return;
            }
            // A Parcelable[] of Intent or LabeledIntent objects as set with
            // putExtra(String, Parcelable[]) of additional activities to place
            // a the front of the list of choices, when shown to the user with a
            // ACTION_CHOOSER.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[] {}));
            try {
                caller.startActivity(chooserIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(caller, "Can't find share component to share", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        }
	}
	
	public static void shareToWeibo(Activity caller) {
		
		Uri uri = null;
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_SUBJECT, R.string.share);
		intent.putExtra(Intent.EXTRA_TEXT,"我在广东科学中心看到这个有趣的展品，想跟你分享");
		//intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, getUriListForImages());
		// File f = new File(Environment.getExternalStorageDirectory().getPath()+"/documents/GDSC_real.jpg");
		// File f = new File("/mnt/sdcard/documents/GDSC_real.jpg");
		
		if (uri == null) {
			try {

				Resources r = caller.getResources();
				
				/* Uri uri =  Uri.parse("/data/data/"
					    + r.getResourcePackageName(R.drawable.gdsc_origin) + "/"
					    + r.getResourceTypeName(R.drawable.gdsc_origin) + "/"
					    + r.getResourceEntryName(R.drawable.gdsc_origin) + ".jpg");
				tv.setText(uri.toString()); */
				
				// InputStream in = new BufferedInputStream(new FileInputStream(uri.toString()));
				
				//Copy the resource into the external storage of Image content provider
				ContentValues values = new ContentValues();
	            values.put(Images.Media.TITLE, "gdsc");
	            values.put(Images.Media.DISPLAY_NAME, "gdsc");
	            values.put(Images.Media.DATE_TAKEN, new Date().getTime());
	            values.put(Images.Media.MIME_TYPE, "image/png");
	            
	            String imageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/DCIM/100ANDRO/gdsc";
	            values.put("_data", imageDirectoryPath);
	            
	            ContentResolver contentResolver = caller.getApplicationContext().getContentResolver();
	            uri = contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
	            				
				Bitmap origin = BitmapFactory.decodeResource(r, R.drawable.gdsc_start);
				
				OutputStream os = null;
				
				os = contentResolver.openOutputStream(uri);
				origin.compress(Bitmap.CompressFormat.PNG, 100, os);
				os.close();	
				
			}
			catch (IOException e) {
				e.printStackTrace();
				return;
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		
		intent.putExtra(Intent.EXTRA_STREAM, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		PackageManager pm = caller.getPackageManager();
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
			
			if (info == null) {
				// showToast(context, "没有找到新浪微博");
				return;
			} else {
				intent.setClassName(sinaPackage, info.activityInfo.name);
				caller.startActivity(intent);
			}
		}
	}
	
	public static void shareToWeibo(Activity caller, String url) {
		
		Intent intent = new Intent(Intent.ACTION_SEND); // Intent to be sent to Sina Weibo APP
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, R.string.share);
		
		if (url == null){
			return;
		}
			    			
		// Add the text content to the intent
		String prefix = caller.getString(R.string.share_prefix);    			
		
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
				Util.showLongToast(caller, R.string.url_is_too_long);
				return;
			}
		}
		
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		String sinaPackage = "com.sina.weibo";
		
		// Check whether sina weibo is installed
		PackageManager pm = caller.getPackageManager();
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
				Util.showLongToast(caller, R.string.no_weibo_installed);
				return;
			} else {
				intent.setClassName(sinaPackage, info.activityInfo.name);
				caller.startActivity(intent);
			}
		} // !matches.isEmpty()
		
		return;
	}
	
	public static void shareToWeixin(Activity caller) {
		
		Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_SUBJECT, R.string.share);
		intent.putExtra(Intent.EXTRA_TEXT,"我在广东科学中心看到这个有趣的展品，想跟你分享");
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, getUriListForImages(caller));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		PackageManager pm = caller.getPackageManager();
		List<ResolveInfo> matches = pm.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		if (!matches.isEmpty()) {
			
			ResolveInfo info = null;
			for (ResolveInfo each : matches) {
				
				String className = each.activityInfo.name;

				if ((pengyouquanClassName.equals(className))) {
					info = each;
					break;				
				}
			}
			
			if (info == null) {
				// showToast(context, "没有找到新浪微博");
				Log.e("WeiXin", "没有找到微信朋友圈");
				return;
			} else {
				intent.setClassName(weixinPackage, info.activityInfo.name);
				caller.startActivity(intent);
			}
		}
	}
	
	// Sina Weibo does not accept multiple images now, but Weixin can. 
	public static void sendMultipleImages(Activity caller) {
		
		Intent intent=new Intent(Intent.ACTION_SEND_MULTIPLE);
		intent.setType("image/*");
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, getUriListForImages(caller));
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		intent.putExtra(Intent.EXTRA_TEXT, "你好 ");
		intent.putExtra(Intent.EXTRA_TITLE, "我的分享");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		caller.startActivity(Intent.createChooser(intent, "请选择")); 
	}
	
	
	/**
	 * 设置需要分享的照片放入Uri类型的集合里
	 */
	private static ArrayList<Uri> getUriListForImages(Activity caller) {
        ArrayList<Uri> myList = new ArrayList<Uri>();
        String imageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/DCIM/Camera/";
        File imageDirectory = new File(imageDirectoryPath);
        String[] fileList = imageDirectory.list();
        if(fileList.length != 0) {
            for(int i=0; i<2; i++){
                try{
                    ContentValues values = new ContentValues(7);
                    values.put(Images.Media.TITLE, fileList[i]);
                    values.put(Images.Media.DISPLAY_NAME, fileList[i]);
                    values.put(Images.Media.DATE_TAKEN, new Date().getTime());
                    values.put(Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(Images.ImageColumns.BUCKET_ID, imageDirectoryPath.hashCode());
                    values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, fileList[i]);
                    values.put("_data", imageDirectoryPath + fileList[i]); //"_data" is the constant value of MediaStore.MediaColumns
                    ContentResolver contentResolver = caller.getApplicationContext().getContentResolver();
                    Uri uri = contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
                    myList.add(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return myList;
    }

}
