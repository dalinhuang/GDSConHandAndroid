package com.winjune.wifiindoor.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.winjune.wifiindoor.R;
import com.winjune.wifiindoor.activity.poiviewer.POIWebViewerActivity;

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
	
	public static void shareToChooser(Activity caller, String content, ArrayList<Uri> images) {
	
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
                    targeted.putExtra(Intent.EXTRA_SUBJECT, R.string.share);
                    targeted.setType("image/*");
                    
                    targeted.putExtra(Intent.EXTRA_TEXT, shareContent);
                    if (!images.isEmpty()) {
                    	// Sina Weibo does not support multiple images share, only share the 1st image.
                    	targeted.putExtra(Intent.EXTRA_STREAM, images.get(0)); 
                    }
                    else{
                    	// Share the text only if there is no image
                    	targeted.putExtra(Intent.EXTRA_TEXT, shareContent);
                    }
                    targeted.setPackage(activityInfo.packageName);
                    targetedShareIntents.add(targeted);
                }
                if (activityInfo.packageName.equalsIgnoreCase(weixinPackage)) {
                	
                    Intent targeted = new Intent(Intent.ACTION_SEND);
                    targeted.putExtra(Intent.EXTRA_SUBJECT, R.string.share);
                    targeted.setType("image/*");
                    //targeted.putExtra(Intent.EXTRA_TEXT, shareContent);
                    targeted.setPackage(activityInfo.packageName);
                    
                    if (activityInfo.name.equalsIgnoreCase(weixinClassName)) {
                    	
                    	if (!images.isEmpty()){
                    		// Weixin does not support either multiple images share
                        	// or has some issue to share the text with image (image needs to be downloaded by the user manually),
                        	// so only share the 1st image.
                    		targeted.putExtra(Intent.EXTRA_STREAM, images.get(0));
                    	}
                    	else{
                    		// Share the text only if there is no image
                    		targeted.putExtra(Intent.EXTRA_TEXT, shareContent);
                    	}
                    	targeted.setClassName(weixinPackage, weixinClassName);
                    }
                    if (activityInfo.name.equalsIgnoreCase(pengyouquanClassName)) {
                    	targeted.putExtra(Intent.EXTRA_TEXT, shareContent);
                    	if (!images.isEmpty()){
                    		if (images.size() > 1) {
                    			targeted.setAction(Intent.ACTION_SEND_MULTIPLE);
                    		}
                    		targeted.putParcelableArrayListExtra(Intent.EXTRA_STREAM, images);
                    	}
                    	targeted.setClassName(weixinPackage, pengyouquanClassName);
                    }
                    
                    targetedShareIntents.add(targeted);
                }
                
            }    
        	
        	if (targetedShareIntents.isEmpty()) {
        		Util.showShortToast(caller, R.string.no_weibo_weixin);
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
            	Util.showShortToast(caller, R.string.no_share_component);
                ex.printStackTrace();
            }
        }
	}
	
	// Method to get the Uri list for images to be shared.
	// All the sharing images should be stored in a single directory, which is specified in the parameter "directory". 
	public static ArrayList<Uri> getUriListForImages(Activity caller, String directory) {
        ArrayList<Uri> myList = new ArrayList<Uri>();
        String imageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ directory;
        File imageDirectory = new File(imageDirectoryPath);
        String[] fileList = imageDirectory.list();
        if(fileList.length != 0) {
            for(int i=0; i<fileList.length; i++){
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
