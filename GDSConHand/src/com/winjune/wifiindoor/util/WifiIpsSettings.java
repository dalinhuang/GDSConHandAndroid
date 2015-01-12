/**
 * @(#)WipsSettings.java
 * May 27, 2013
 *
 * Copyright 2012 - 2013 Nortels Software Inc. All rights reserved.
 */
package com.winjune.wifiindoor.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @author ezhipin, haleyshi
 * 
 */
public class WifiIpsSettings {
	
	
	public static String FILE_CACHE_FOLDER = "/GDSConHand/";
	
	public static boolean DEBUG = false;
	
	public static String PRIMARY_SERVER = "192.168.1.19"; //"115.29.220.45";
	public static String SECONDARY_SERVER = "192.168.1.19";
	public static String CMCC_Site = PRIMARY_SERVER;		//	中国移动
	public static String CU_Site = SECONDARY_SERVER;		//	中国联通
	public static String CT_Site = PRIMARY_SERVER;		//	中国电信
	public static String SERVER_PORT = "8080";
	public static String SERVER_SUB_DOMAIN = "/GDSCAppServer"; //"/wifiips", "/WifiIpsServer";
	public static String SERVER = null;
	
	public static int CONNECTION_TIMEOUT = 60000; //30000; // 30 s
	public static int SOCKET_TIMEOUT = 60000; //30000; // 30s
		
	public static String URL_PREFIX = "http://";
	public static String URL_API_TEST = "/Test";
	public static String URL_API_LOCATE = "/locate";
	public static String URL_API_LOCATE_TEST = "/locateTest";
	public static String URL_API_COLLECT_TEST = "/collectTest";
	public static String URL_API_COLLECT = "/collect";
	public static String URL_API_QUERY = "/query";
	public static String URL_API_NFC_COLLECT = "/nfcCollect";
	public static String URL_API_LOCATE_BASE_NFC = "/nfcLocate";
	public static String URL_API_DELETE_FINGERPRINT = "/fpDelete";
	public static String URL_API_QUERY_APK_VERSION = "/queryApk";
	public static String URL_API_QUERY_BUILDING = "/queryBuildingList";
	public static String URL_API_QUERY_MAP_LIST = "/queryMapList";
	public static String URL_API_DOWNLOAD_MAP = "/dowloadMap";
	public static String URL_API_QUERY_MAP_INFO = "/queryMapInfo";
	public static String URL_API_QUERY_NAVI_INFO = "/queryNaviInfo";
	public static String URL_API_QUERY_ADVERTISE_INFO = "/queryAd";
	public static String URL_API_QUERY_INTEREST_PLACES = "/queryInterestPlaces";
	public static String URL_API_QUERY_COLLECT_STATUS = "/queryCollectStatus";

	public static String getServerIpByDomainName(String domainName) {
		// Spawns a 'sh' process first, and then execute 'ping' in that shell

		String ipAddress = null;

		try {
			Process p = new ProcessBuilder("sh").redirectErrorStream(true)
					.start();
			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			os.writeBytes("ping -c 4 -w 30 " + domainName + '\n');
			os.flush();

			// Close the terminal
			os.writeBytes("exit\n");
			os.flush();

			// read ping replys
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;

			/*
			 * PING e7d89d67950843.eapac.ericsson.se (146.11.0.23) 56(84) bytes
			 * of data. 64 bytes from 146.11.0.23: icmp_seq=1 ttl=127 time=3.75
			 * ms 64 bytes from 146.11.0.23: icmp_seq=2 ttl=127 time=19.0 ms 64
			 * bytes from 146.11.0.23: icmp_seq=3 ttl=127 time=16.1 ms 64 bytes
			 * from 146.11.0.23: icmp_seq=4 ttl=127 time=3.14 ms
			 * 
			 * --- e7d89d67950843.eapac.ericsson.se ping statistics --- 4
			 * packets transmitted, 4 received, 0% packet loss, time 3004ms rtt
			 * min/avg/max/mdev = 3.143/10.528/19.043/7.156 ms
			 */
			int lineIndex = 0;
			ArrayList<String> ipList = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				if ((lineIndex > 0) && (lineIndex < 5)) {
					int beginIndex = line.indexOf("from");
					if (beginIndex > -1) {
						int endIndex = line.indexOf(":");
						if (endIndex > beginIndex + 8) {  // IP at least 8 chars
							String ip = line
									.substring(beginIndex + 5, endIndex);

							if ((ip != null) && !TextUtils.isEmpty(ip))
								ipList.add(ip);
						}
					}
				}

				lineIndex++;
			}

			if (ipList.size() > 0) {
				ipAddress = ipList.get(0);
			}
			else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
				
		if (isValidIp(ipAddress))
			return ipAddress;
		else
			return null;				
	}

	private static boolean isValidIp(String ip) {
		if (ip == null) {
			return false;
		}
		
		String[] nums = ip.split("\\.");

		if (nums.length == 4) {
			return true;
		} else {
			return false;
		}
	}

    public static String getImsi(Context context) {  
        String imsi = "";  
        try {   //��ͨ������ȡimsi  
            TelephonyManager tm = (TelephonyManager) context.  
                    getSystemService(Context.TELEPHONY_SERVICE);  
            imsi = tm.getSubscriberId();  
            if (imsi==null || "".equals(imsi)) imsi = tm.getSimOperator();  
            Class<?>[] resources = new Class<?>[] {int.class};  
            Integer resourcesId = new Integer(1);  
            if (imsi==null || "".equals(imsi)) {  
                try {   //���÷����ȡ    MTK�ֻ�  
                    Method addMethod = tm.getClass().getDeclaredMethod("getSubscriberIdGemini", resources);  
                    addMethod.setAccessible(true);  
                    imsi = (String) addMethod.invoke(tm, resourcesId);  
                } catch (Exception e) {  
                    imsi = null;  
                }  
            }  
            if (imsi==null || "".equals(imsi)) {  
                try {   //���÷����ȡ    չѶ�ֻ�  
                    Class<?> c = Class  
                            .forName("com.android.internal.telephony.PhoneFactory");  
                    Method m = c.getMethod("getServiceName", String.class, int.class);  
                    String spreadTmService = (String) m.invoke(c, Context.TELEPHONY_SERVICE, 1);  
                    TelephonyManager tm1 = (TelephonyManager) context.getSystemService(spreadTmService);  
                    imsi = tm1.getSubscriberId();  
                } catch (Exception e) {  
                    imsi = null;  
                }  
            }  
            if (imsi==null || "".equals(imsi)) {  
                try {   //���÷����ȡ    ��ͨ�ֻ�  
                    Method addMethod2 = tm.getClass().getDeclaredMethod("getSimSerialNumber", resources);  
                    addMethod2.setAccessible(true);  
                    imsi = (String) addMethod2.invoke(tm, resourcesId);  
                } catch (Exception e) {  
                    imsi = null;  
                }  
            }  
            if (imsi==null || "".equals(imsi)) {  
                imsi = "000000";  
            }  
            return imsi;  
        } catch (Exception e) {  
            return "000000";  
        }  
    }  
	
	public static boolean getServerAddress(Context context, boolean force_retry) {
		
		// reset server address
		if (force_retry) {
			SERVER = null;
		}

		if (SERVER != null) {
			return true;
		}

		SERVER = PRIMARY_SERVER + ":" + SERVER_PORT	+ SERVER_SUB_DOMAIN;

		
/*		String imsi = getImsi(context);

		try {
			if (imsi!=null){ 
				if(imsi.startsWith("46000") || imsi.startsWith("46002")) {
					//因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号 //中国移动
					SERVER = CMCC_Site + ":" + SERVER_PORT	+ SERVER_SUB_DOMAIN;
				} else if(imsi.startsWith("46001")) {
					//中国联通
					SERVER = CU_Site+ ":" + SERVER_PORT	+ SERVER_SUB_DOMAIN;
				} else if(imsi.startsWith("46003")) {
					//中国电信
					SERVER = CT_Site + ":" + SERVER_PORT	+ SERVER_SUB_DOMAIN;
				} else {
					//其他移动网络
					SERVER = PRIMARY_SERVER + ":" + SERVER_PORT	+ SERVER_SUB_DOMAIN;
				}
			} else {
				// 没有IMSI可能是个PAD, 使用默认的地址
				SERVER = PRIMARY_SERVER + ":" + SERVER_PORT	+ SERVER_SUB_DOMAIN;
			}		
		} catch (UnknownHostException e) {
			SERVER = PRIMARY_SERVER + ":" + SERVER_PORT	+ SERVER_SUB_DOMAIN;
			e.printStackTrace();
		}*/
		return true;
	}
	
	//Check if the destination address is pingable, it could be domain name or ip
	//seems it doesn't work in some devices, like Nexus 7
	public static boolean isPingable(String destAdress) {
						
		try {
			Process p = new ProcessBuilder("sh").redirectErrorStream(true)
					.start();
			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			os.writeBytes("ping -c 1 -w 30 " + destAdress + '\n');
			os.flush();

			// Close the terminal
			os.writeBytes("exit\n");
			os.flush();

			// read ping replys
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line;

			/*
			 * ping -c 4 -w 30 10.178.255.124
			 * PING 10.178.255.124 (10.178.255.124) 56(84) bytes of data.
			 * 64 bytes from 10.178.255.124: icmp_req=1 ttl=64 time=0.039 ms
			 * 64 bytes from 10.178.255.124: icmp_req=2 ttl=64 time=0.029 ms
			 * 64 bytes from 10.178.255.124: icmp_req=3 ttl=64 time=0.028 ms
			 * 64 bytes from 10.178.255.124: icmp_req=4 ttl=64 time=0.036 ms
			 * 
			 * --- 10.178.255.124 ping statistics ---
			 * 4 packets transmitted, 4 received, 0% packet loss, time 3000ms
			 * rtt min/avg/max/mdev = 0.028/0.033/0.039/0.004 ms
			 */
			while ((line = reader.readLine()) != null) {
				if (line.contains("0 received")) {
					return false; 
				}
			}
		} catch (IOException e) {
			return false;
		}	

		return true;
	}
}
