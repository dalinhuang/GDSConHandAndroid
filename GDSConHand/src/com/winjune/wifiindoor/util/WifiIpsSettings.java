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
import java.util.ArrayList;

import android.text.TextUtils;

/**
 * @author ezhipin, haleyshi
 * 
 */
public class WifiIpsSettings {
	
	
	public static String FILE_CACHE_FOLDER = "/GDSConHand/";
	
	public static boolean DEBUG = false;
	
	public static String PRIMARY_SERVER = "14.18.207.183";
	public static String SECONDARY_SERVER = "14.18.207.183";
	public static String SERVER_PORT = "80";
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

	//Hoare: to do, select the fast server
	public static boolean getServerAddress(boolean force_retry) {
		
		if (force_retry) {
			SERVER = null;
		}

		if (SERVER != null) {
			return true;
		}

		
		SERVER = PRIMARY_SERVER + ":" + SERVER_PORT	+ SERVER_SUB_DOMAIN;
		
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
