package com.winjune.wifiindoor.version;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.os.StrictMode;
import android.widget.Toast;

import com.winjune.wifiindoor.lib.version.VersionInfoR;
import com.winjune.wifiindoor.lib.version.VersionInfoT;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

public class VersionManager {
	
	public static ArrayList<String> checkVersionInfo(Activity activity){
		
		ArrayList<String> updateTableList = new ArrayList<String>();
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			}
		Util.downFile(activity,
		Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, IndoorMapData.VERSION_TABLE_FILE_NAME),
		IndoorMapData.CONFIG_FILE_PATH,
		"new_"+IndoorMapData.VERSION_TABLE_FILE_NAME,                     		
		false,      // Open after download
		"",
		false, //useHandler
		false);// Use Thread	
		
		VersionInfoT oldInfo = new VersionInfoT();
		VersionInfoT newInfo = new VersionInfoT();
		String oldFullFileName = Util.getFilePath(IndoorMapData.CONFIG_FILE_PATH + IndoorMapData.VERSION_TABLE_FILE_NAME);
		String newFullFileName = Util.getFilePath(IndoorMapData.CONFIG_FILE_PATH + "new_"+IndoorMapData.VERSION_TABLE_FILE_NAME);
		
		oldInfo = (VersionInfoT) oldInfo.fromJson(oldFullFileName, VersionInfoT.class);
		newInfo = (VersionInfoT) newInfo.fromJson(newFullFileName, VersionInfoT.class);
		ArrayList<VersionInfoR> newVersionInfos = newInfo.getVersions();
		ArrayList<VersionInfoR> oldVersionInfos = oldInfo.getVersions();
		for (int i = 0; i < newVersionInfos.size(); i++)
		{
			/* Hoare: skip version check
			for (int j = 0; j < oldVersionInfos.size(); j++)
			{
				if (newVersionInfos.get(i).tableName.equalsIgnoreCase(oldVersionInfos.get(j).tableName))
				{
					if (newVersionInfos.get(i).version>oldVersionInfos.get(j).version)
					{
						updateTableList.add(newVersionInfos.get(i).tableName);
					}
				}
			}
			*/
			
			updateTableList.add(newVersionInfos.get(i).tableName);
			
		}
		return updateTableList;
	}
	
	public static void downloadVersionInfo(Activity activity, ArrayList<String> updateTableList){
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			}
		String tablist = "";
		for (int i = 0; i < updateTableList.size(); i++)
		{
			Util.downFile(activity,
			Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, updateTableList.get(i) + "_table.json"),
			IndoorMapData.CONFIG_FILE_PATH,
			updateTableList.get(i) + "_table.json",                     		
			false,      // Open after download
			"",
			false, //useHandler
			false);// Use Thread	
			tablist += updateTableList.get(i) + ",";
		}
		
		if (updateTableList.size() > 0)
		{
		   Util.showToast(activity, tablist.substring(0,tablist.length() -1)+" table(s) downloaded!", Toast.LENGTH_LONG);
			//Replace old file with new version file
	    	File newFile = Util.openOrCreateFileInPath(IndoorMapData.CONFIG_FILE_PATH, "new_"+IndoorMapData.VERSION_TABLE_FILE_NAME, false);
	    	File oldFile = Util.openOrCreateFileInPath(IndoorMapData.CONFIG_FILE_PATH, IndoorMapData.VERSION_TABLE_FILE_NAME, false);
	    	newFile.renameTo(oldFile);
		}
		
	}	
	
}
