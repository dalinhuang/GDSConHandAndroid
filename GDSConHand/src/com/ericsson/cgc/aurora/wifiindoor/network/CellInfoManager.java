package com.ericsson.cgc.aurora.wifiindoor.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ericsson.cgc.aurora.wifiindoor.types.MobileCell;
import com.ericsson.cgc.aurora.wifiindoor.types.NeighboringCell;

import android.annotation.SuppressLint;
import android.content.Context;

import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;

public class CellInfoManager {
	private int asu;

	private final PhoneStateListener listener;
	private TelephonyManager telephonyManager;
	private Context context;
	private CellLocation currentLocation;

	@SuppressWarnings("deprecation")
	public CellInfoManager(Context paramContext) {
		listener = new CellInfoListener(this);
		telephonyManager = (TelephonyManager) paramContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		setContext(paramContext);

		if (telephonyManager != null) {
			telephonyManager.listen(this.listener,
					PhoneStateListener.LISTEN_CELL_LOCATION
							| PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
			setCurrentLocation(telephonyManager.getCellLocation());
		} else {
			setCurrentLocation(null);
		}
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public TelephonyManager getTelephonyManager() {
		return telephonyManager;
	}

	public int getAsu() {
		return asu;
	}

	public void setAsu(int asu) {
		this.asu = asu;
	}

	public CellLocation getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(CellLocation location) {
		currentLocation = location;
	}

	public boolean isGsm() {
		if (telephonyManager != null) {
			if (telephonyManager.getPhoneType() == 1
					&& currentLocation instanceof GsmCellLocation) {
				return true;
			}
		}

		return false;
	}

	public boolean isCdma() {
		if (telephonyManager != null) {
			if (telephonyManager.getPhoneType() == 2
					&& currentLocation instanceof CdmaCellLocation) {
				return true;
			}
		}

		return false;
	}

	public static int dBm(int i) {
		int j;

		if (i >= 0 && i <= 31) {
			j = i * 2 + -113;
		} else {
			j = 0;
		}

		return j;
	}

	public List<NeighboringCellInfo> getNeighboringCells() {
		if (telephonyManager != null) {
			List<NeighboringCellInfo> lsCellInfo = telephonyManager
					.getNeighboringCellInfo();
			return lsCellInfo;
		}

		return null;
	}

	public List<NeighboringCell> getNeighboringCellList() {
		if (telephonyManager != null) {
			List<NeighboringCellInfo> lsCellInfo = telephonyManager
					.getNeighboringCellInfo();

			List<NeighboringCell> list = new ArrayList<NeighboringCell>();

			for (NeighboringCellInfo cell : lsCellInfo) {
				NeighboringCell item = new NeighboringCell();
				item.setLac(cell.getLac());
				item.setCid(cell.getCid());
				item.setPsc(cell.getPsc());
				item.setRssi(cell.getRssi());
				item.setNetworkType(cell.getNetworkType());

				list.add(item);
			}

			return list;
		}

		return null;
	}

	public JSONArray neiboringCellInfo() {
		JSONArray jsonArray = new JSONArray();

		for (NeighboringCellInfo neighbor : getNeighboringCells()) {
			JSONObject localJSONObject = getNeighborInfo(neighbor);
			jsonArray.put(localJSONObject);
		}

		return jsonArray;
	}

	private JSONObject getNeighborInfo(NeighboringCellInfo neighbor) {
		JSONObject jsonobject = new JSONObject();

		try {
			jsonobject.put("lac", neighbor.getLac());
			jsonobject.put("cid", neighbor.getCid());
			jsonobject.put("psc", neighbor.getPsc());
			jsonobject.put("dbm", neighbor.getRssi());
			jsonobject.put("type", neighbor.getNetworkType());
		} catch (Exception ex) {
		}

		return jsonobject;
	}

	public MobileCell getConnectionCell() {
		if (telephonyManager == null) {
			return null;
		}

		MobileCell cell = new MobileCell();

		try {
			if (isGsm()) {
				GsmCellLocation gsm_cell = (GsmCellLocation) currentLocation;

				cell.setMode("GSM");
				cell.setLac(gsm_cell.getLac());
				cell.setCid(gsm_cell.getCid());
				if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
					// Add check to support the API less than 9 without UMTS
					cell.setPsc(gsm_cell.getPsc());
				} else {
					cell.setPsc(-1);
				}
			} else if (isCdma()) {
				CdmaCellLocation cdma_cell = (CdmaCellLocation) currentLocation;

				cell.setMode("CDMA");
				cell.setSystemId(cdma_cell.getSystemId());
				cell.setNetworkId(cdma_cell.getNetworkId());
				cell.setBaseStationId(cdma_cell.getBaseStationId());
				cell.setBaseStationLatitude(cdma_cell.getBaseStationLatitude());
				cell.setBaseStationLongitude(cdma_cell.getBaseStationLatitude());

			} else {
				cell.setMode("UNKNOWN");
			} // if (cellInfoManager.isGsm())

			cell.setNetworkType(telephonyManager.getNetworkType());
			cell.setImsi(telephonyManager.getSubscriberId());
			cell.setMsisdn(telephonyManager.getLine1Number());
			cell.setOperator(telephonyManager.getNetworkOperator());
			cell.setOperatorName(telephonyManager.getNetworkOperatorName());
			cell.setDevice(telephonyManager.getDeviceId());
			// IMEI/SV
			String imei_sv = telephonyManager.getDeviceSoftwareVersion();
			if (imei_sv == null) {
				cell.setDeviceSoftwareVersion("00");
			} else {
				cell.setDeviceSoftwareVersion(telephonyManager
						.getDeviceSoftwareVersion());
			}

			return cell;
		} catch (Exception ex) {
			return null;
		}
	}

	@SuppressLint("NewApi")
	public JSONObject getConnectionInfo() {
		JSONObject jsonobject = new JSONObject();

		if (telephonyManager == null) {
			return null;
		}

		try {
			if (isGsm()) {
				GsmCellLocation gsm_cell = (GsmCellLocation) currentLocation;
				jsonobject.put("mode", "GSM");
				jsonobject.put("lac", gsm_cell.getLac());
				jsonobject.put("cid", gsm_cell.getCid());
				if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) { // Add
																								// check
																								// to
																								// support
																								// the
																								// API
																								// less
																								// than
																								// 9
																								// without
																								// UMTS
					jsonobject.put("psc", gsm_cell.getPsc());
				} else {
					jsonobject.put("psc", -1);
				}
			} else {
				if (isCdma()) {
					CdmaCellLocation cdma_cell = (CdmaCellLocation) currentLocation;
					jsonobject.put("mode", "CDMA");
					jsonobject.put("bss_system_id", cdma_cell.getSystemId());
					jsonobject.put("bss_network_id", cdma_cell.getNetworkId());
					jsonobject.put("bss_id", cdma_cell.getBaseStationId());
					jsonobject.put("bss_latitude",
							cdma_cell.getBaseStationLatitude());
					jsonobject.put("bss_longitude",
							cdma_cell.getBaseStationLongitude());
				} else {
					jsonobject.put("mode", "UNKNOWN");
				}
			} // if (cellInfoManager.isGsm())

			jsonobject.put("type", telephonyManager.getNetworkType());
			jsonobject.put("imsi", telephonyManager.getSubscriberId());
			jsonobject.put("msisdn", telephonyManager.getLine1Number());
			jsonobject.put("operator", telephonyManager.getNetworkOperator());
			jsonobject.put("operator_name",
					telephonyManager.getNetworkOperatorName());
			// jsonobject.put("mcc",
			// telephonyManager.getNetworkOperator().substring(0, 3));
			// jsonobject.put("mnc",
			// telephonyManager.getNetworkOperator().substring(3));
			jsonobject.put("device", telephonyManager.getDeviceId());
			// IMEI/SV
			String imei_sv = telephonyManager.getDeviceSoftwareVersion();
			if (imei_sv == null) {
				jsonobject.put("device_sw_version", "00");
			} else {
				jsonobject.put("device_sw_version",
						telephonyManager.getDeviceSoftwareVersion());
			}

		} catch (Exception ex) {
			return null;
		}

		return jsonobject;
	}

	private static String getJsonCellPos(int mcc, int mnc, int lac, int cid)
			throws JSONException {
		JSONObject jsonCellPos = new JSONObject();
		jsonCellPos.put("version", "1.1.0");
		jsonCellPos.put("host", "maps.google.com");

		JSONArray array = new JSONArray();
		JSONObject json1 = new JSONObject();
		json1.put("location_area_code", "" + lac + "");
		json1.put("mobile_country_code", "" + mcc + "");
		json1.put("mobile_network_code", "" + mnc + "");
		json1.put("age", 0);
		json1.put("cell_id", "" + cid + "");
		array.put(json1);

		jsonCellPos.put("cell_towers", array);
		return jsonCellPos.toString();
	}

	public static String httpPost(String url, String jsonCellPos)
			throws IOException {
		byte[] data = jsonCellPos.toString().getBytes();
		URL realUrl = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) realUrl
				.openConnection();
		httpURLConnection.setConnectTimeout(6 * 1000);
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Accept",
				"application/json, text/javascript, */*; q=0.01");
		httpURLConnection.setRequestProperty("Accept-Charset",
				"GBK,utf-8;q=0.7,*;q=0.3");
		httpURLConnection.setRequestProperty("Accept-Encoding",
				"gzip,deflate,sdch");
		httpURLConnection.setRequestProperty("Accept-Language",
				"zh-CN,zh;q=0.8");
		httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
		httpURLConnection.setRequestProperty("Content-Length",
				String.valueOf(data.length));
		httpURLConnection.setRequestProperty("Content-Type",
				"application/json; charset=UTF-8");

		httpURLConnection.setRequestProperty("Host", "www.minigps.net");
		httpURLConnection.setRequestProperty("Referer",
				"http://www.minigps.net/map.html");
		httpURLConnection
				.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4X-Requested-With:XMLHttpRequest");

		httpURLConnection.setRequestProperty("X-Requested-With",
				"XMLHttpRequest");
		httpURLConnection.setRequestProperty("Host", "www.minigps.net");

		DataOutputStream outStream = new DataOutputStream(
				httpURLConnection.getOutputStream());
		outStream.write(data);
		outStream.flush();
		outStream.close();

		if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream inputStream = httpURLConnection.getInputStream();
			return new String(read(inputStream));
		}
		return null;
	}

	public static byte[] read(InputStream inputSream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		int len = -1;
		byte[] buffer = new byte[1024];
		while ((len = inputSream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inputSream.close();

		return outStream.toByteArray();
	}

	public static String queryCellLocation(int mcc, int mnc, int lac, int cid) {
		try {
			String json = getJsonCellPos(mcc, mnc, lac, cid);
			String url = "http://www.minigps.net/minigps/map/google/location";
			return httpPost(url, json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "Unknown_Location";
	}

	class CellInfoListener extends PhoneStateListener {

		CellInfoListener(CellInfoManager manager) {
		}

		public void onCellLocationChanged(CellLocation paramCellLocation) {
			if (telephonyManager != null) {
				setCurrentLocation(CellInfoManager.this.telephonyManager
						.getCellLocation());
			}
		}

		public void onSignalStrengthChanged(int paramInt) {
			CellInfoManager.this.setAsu(paramInt);
		}

	}

}
