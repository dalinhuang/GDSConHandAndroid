package com.winjune.wifiindoor.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ericsson.cgc.aurora.wifiindoor.R;
import com.winjune.wifiindoor.ads.AdMessageHandler;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

public class AdUtil {
	
	private static boolean addownloadOngoing = false;
	private static AdMessageHandler adMessageHandler = null;
	
	public static String getFilePath(String relativePath) {
		String filePath = WifiIpsSettings.FILE_CACHE_FOLDER + relativePath;
    	String sdStatus = Environment.getExternalStorageState();

		if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			return filePath;
		} else {
			return Environment.getExternalStorageDirectory().getPath() + filePath;
		}
	}
	
	
	public static String getAdPicturePathName(String mapId, String pictureName) {
		return getFilePath(AdData.AD_FILE_PATH_LOCAL) + mapId + "/" + pictureName;
	}
	
	
	public static boolean isADDownloadOngoing() {
		return addownloadOngoing;
	}
	
	public static void downloadAd(Activity activity, String mapId, String url) {
		 downFile(activity,// WifiIpsSettings.URL_PREFIX +"146.11.0.117:8081"+url,
        		WifiIpsSettings.URL_PREFIX + WifiIpsSettings.SERVER + "/" + AdData.AD_FILE_PATH_REMOTE + GetFileName(url),
        		AdData.AD_FILE_PATH_REMOTE + mapId + "/",
        		GetFileName(url),
        		false,   // No need to open after download
        		"",
        		true);  // always use a thread, wait for finish
	}
	
	
	private static void downFile(final Activity activity, final String url, final String localRelativePath, final String localFileName, final boolean openAfterDone, final String mimeType, final boolean useThread) {
		if (addownloadOngoing) {
			Util.showLongToast(activity, R.string.another_download_ongoing);
			return;
		}
		
		addownloadOngoing = true;
		
		
		if (!useThread) {
			downloadFile(activity, url, localRelativePath, localFileName, openAfterDone, mimeType);
		} else {
			new Thread() {  
		        public void run() {  
		        	downloadFile(activity, url, localRelativePath, localFileName, openAfterDone, mimeType);
		        }  
		    }.start(); 
		}

	}
	
	private static void downloadFile(Activity activity, String url,
			String localRelativePath, String localFileName,
			boolean openAfterDone, String mimeType) {
		
		HttpClient client = new DefaultHttpClient();  
        HttpGet get = new HttpGet(url);  
        HttpResponse response;  
        
        try {  
            response = client.execute(get);  
            HttpEntity entity = response.getEntity();  
            long length = entity.getContentLength();  
            InputStream is = entity.getContent();  
            FileOutputStream fileOutputStream = null;
            
            if (is != null) {	                    
            	File file = openOrCreateFileInPath(localRelativePath, localFileName, true);
            	
            	if (file == null) {
            		addownloadOngoing = false;
            		return;
            	}
            	
            	fileOutputStream = new FileOutputStream(file);  
                byte[] buf = new byte[1024];  
                int ch = -1;  
                @SuppressWarnings("unused")
				int count = 0;  
                while ((ch = is.read(buf)) != -1) {  
                    fileOutputStream.write(buf, 0, ch);  
                    count += ch;  
                    if (length > 0) {  
                    }  
                }  
            }  
            
            fileOutputStream.flush();  
            
            if (fileOutputStream != null) {  
                fileOutputStream.close();  
            }
            
            //doneDownload(activity, getFilePath(localRelativePath), localFileName, openAfterDone, mimeType);          
        } catch (ClientProtocolException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        }  
        
        addownloadOngoing = false;
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
	
	public static boolean isAdDownloadOngoing() {
		return addownloadOngoing;
	}

	
	public static void initialAdSystem(Activity activity){
		if (adMessageHandler == null){
			setAdMessageHandler(new AdMessageHandler());
			   //adMessageHandler.setActivity(activity);
		}
	}
	
	public static void setAdMessageHandler(AdMessageHandler adMessageHandler) {
		AdUtil.adMessageHandler = adMessageHandler;
	}
	
    public static String GetFileName(String file){
    	
	    StringTokenizer st=new StringTokenizer(file,"/");
	    while(st.hasMoreTokens())
	    {
	      file=st.nextToken();
	    }
	    return file;
    }
    
	public static File createFileFromInputStream(InputStream inputStream, String my_file_name) {

		   try{
			   File f = new File(Environment.getExternalStorageDirectory() + File.separator + "test.txt");
			   f.createNewFile();
		      //File f = new File("temp","temp");
		      //if(!f.exists()){
		    //	  f.createNewFile();
		     // }
		      OutputStream outputStream = new FileOutputStream(f);
		      byte buffer[] = new byte[1024];
		      int length = 0;

		      while((length=inputStream.read(buffer)) > 0) {
		        outputStream.write(buffer,0,length);
		      }

		      outputStream.close();
		      inputStream.close();

		      return f;
		   }catch (IOException e) {
		         //Logging exception
		   }

		return null;
		}

}
