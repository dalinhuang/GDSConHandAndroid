package com.ericsson.cgc.aurora.wifiindoor.network;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkInfoManager {
	private ConnectivityManager connectivityManager;
	private Context context;

	public NetworkInfoManager(Context paramContext) {	
		connectivityManager = (ConnectivityManager) paramContext.getSystemService(Context.CONNECTIVITY_SERVICE); 
		setContext(paramContext);
	}
	
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}	
	
	public ConnectivityManager getConnectivityManager() {
		return connectivityManager;
	}
	
	public boolean isConnected(){
		if (connectivityManager!=null){
			if (connectivityManager.getActiveNetworkInfo()!=null){
				return connectivityManager.getActiveNetworkInfo().isConnected();
			}
		}
		
		return false;
	}
	
	public boolean isAvailable(){
		if (connectivityManager!=null){
			if (connectivityManager.getActiveNetworkInfo()!=null){
				return connectivityManager.getActiveNetworkInfo().isAvailable();
			}
		}
		
		return false;
	}
	
	public boolean isConnectedOrConnecting(){
		if (connectivityManager!=null){
			if (connectivityManager.getActiveNetworkInfo()!=null){
				return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
			}
		}
		
		return false;
	}
	
	public boolean isWifiConnected(){
		if (connectivityManager!=null){
			if (connectivityManager.getActiveNetworkInfo()!=null){
				return (connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);
			}
		}
		
		return false;
	}
	
	public boolean is2G3GConnected(){
		if (connectivityManager!=null){
			if (connectivityManager.getActiveNetworkInfo()!=null){
				return (connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE);
			}
		}
		
		return false;
	}
	
}
