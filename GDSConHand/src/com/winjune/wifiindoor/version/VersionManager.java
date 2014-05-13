package com.winjune.wifiindoor.version;

import java.util.ArrayList;

import android.app.Activity;
import android.os.StrictMode;
import android.widget.Toast;

import com.winjune.wifiindoor.lib.version.VersionInfoR;
import com.winjune.wifiindoor.lib.version.VersionInfoT;
import com.winjune.wifiindoor.util.IndoorMapData;
import com.winjune.wifiindoor.util.Util;

public class VersionManager {
	
	public static void downloadVersionInfo(Activity activity){
		
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
		
		oldInfo.fromXML(oldFullFileName, oldInfo);
		newInfo.fromXML(newFullFileName, newInfo);
		ArrayList<VersionInfoR> newVersionInfos = newInfo.getVersions();
		ArrayList<VersionInfoR> oldVersionInfos = oldInfo.getVersions();
		for (int i = 0; i < newVersionInfos.size(); i++)
		{
			for (int j = 0; j < oldVersionInfos.size(); j++)
			{
				if (newVersionInfos.get(i).tableName.equalsIgnoreCase(oldVersionInfos.get(j).tableName))
				{
					if (newVersionInfos.get(i).version>oldVersionInfos.get(j).version)
					{
						//New version founded download new version
						Util.downFile(activity,
						Util.fullUrl(IndoorMapData.XML_FILE_PATH_REMOTE, newVersionInfos.get(i).tableName+"_table.xml"),
						IndoorMapData.CONFIG_FILE_PATH,
						newVersionInfos.get(i).tableName+"_table.xml",                     		
						false,      // Open after download
						"",
						false, //useHandler
						false);// Use Thread	
						Util.showToast(activity, newVersionInfos.get(i).tableName+" table downloaded!", Toast.LENGTH_LONG);
					}
				}
			}
			
		}
	}	
}
