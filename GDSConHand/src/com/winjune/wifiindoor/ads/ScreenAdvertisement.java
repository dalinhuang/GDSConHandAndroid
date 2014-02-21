package com.winjune.wifiindoor.ads;

import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.util.Xml;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.winjune.wifiindoor.runtime.RuntimeIndoorMap;
import com.winjune.wifiindoor.types.Location;
import com.winjune.wifiindoor.util.AdData;
import com.winjune.wifiindoor.util.AdUtil;
import com.winjune.wifiindoor.util.Util;
import com.winjune.wifiindoor.webservice.MsgConstants;

public class ScreenAdvertisement {

	final Activity activity;
    private int advertisementId;
	
	private List<Ad> AdList; 
	private AdGroup adGroup;
	private String AdXMLPath;
	private int mapId;
	public AdvertiseUnit advertiseUnit;
	
	private int AdID;
	private String AdPicName;
	private String AdUrl;
	private static boolean AdFileLoadSuccess = false;
	
	public ScreenAdvertisement(final Activity activity) {
		this.activity = activity;
		
	}

	public ScreenAdvertisement(final Activity activity, RuntimeIndoorMap runtimeIndoorMap) {
		this.activity = activity;
		this.mapId=runtimeIndoorMap.getMapId();
		this.AdList=new ArrayList<Ad>();
		advertiseUnit = new AdvertiseUnit();
		setADXMLPath();
	}
	
	public void setadGroup(AdGroup adGroup){
		this.adGroup = adGroup;
	}
	
    private void setADXMLPath(){
    	this.setAdXMLPath(AdUtil.getFilePath(AdData.AD_FILE_PATH_LOCAL) + mapId + "/" + AdData.AD_XML_NAME_LOCAL);	
    }
    
    private String getAdPicPath(String AdPicName){
    	return AdUtil.getFilePath(AdData.AD_FILE_PATH_LOCAL) + mapId + "/" + AdPicName;	
    }

	//hide ads.
	private void unshowAds () {
		AdView adView = (AdView)activity.findViewById(advertisementId);
		adView.setVisibility(android.view.View.INVISIBLE);
		adView.setEnabled(false);
	}
	
	final Runnable unshowAdsRunnable = new Runnable() {
		public void run() {
			unshowAds();
		}
	};

	final Runnable showAdsRunnable = new Runnable() {
		public void run() {
		//	showAds();
		}
	};

	public void showAdvertisement() {
		activity.runOnUiThread(showAdsRunnable);		
		//adsHandler.post(showAdsRunnable);

	}

	public void hideAdvertisement() {
		//adsHandler.post(unshowAdsRunnable);
		activity.runOnUiThread(unshowAdsRunnable);
	}
	
	public void initAdvertiseData(){
		AdPicName = "sample_ad.png";
		AdUrl = "http://gdsc.southcn.com";
		AdID = 0;
	}
	
	public void checkAndDownloadAds(){
		
		if (adGroup == null) {
			return;
		}
		
		if (adGroup.getAds() == null) {
			return;
		}
		
		for(int i = 0;i<adGroup.getAds().size();i++){
			File file = new File (getAdPicPath(AdUtil.GetFileName(adGroup.getAds().get(i).getThumbnailImgUrl())));
			if (!file.exists()) {
				while (AdUtil.isAdDownloadOngoing()) {
					//wait
				}
				String url = adGroup.getAds().get(i).getThumbnailImgUrl();
				AdUtil.downloadAd(activity, ""+mapId, url);
				
				while (AdUtil.isAdDownloadOngoing()) {
					//wait for download finish
				}
			}
		}
		
	}
	
    public static String writeXML(List<Ad> ads, Writer writer){

        XmlSerializer serializer = Xml.newSerializer();

        try {

            serializer.setOutput(writer);

            serializer.startDocument("UTF-8", true);

          //锟斤拷一锟斤拷锟斤拷锟斤拷为锟斤拷锟斤拷占锟�锟斤拷锟绞癸拷锟斤拷锟斤拷锟秸硷拷,锟斤拷锟斤拷锟斤拷锟斤拷为null

            serializer.startTag("", "advertises");

            for (Ad ad : ads){

                serializer.startTag("", "AdvertiseBase");

                //serializer.startTag("", "AdID");
                //serializer.text(ad.ReadAdID());
                //serializer.endTag("", "AdID");
                
                serializer.startTag("", "AdPicName");
                serializer.text(ad.getThumbnailImgUrl());
                serializer.endTag("", "AdPicName");
                
                serializer.startTag("", "AdUrl");
                serializer.text(ad.getUrl());
                serializer.endTag("", "AdUrl");


                serializer.endTag("", "AdvertiseBase");

            }

            serializer.endTag("", "advertises");

            serializer.endDocument();

            return writer.toString();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }
        
    public void getAdsReal(File fXmlFile) {
    	
    	
    	 
        try {
     
    	//File fXmlFile = new File("/Users/mkyong/staff.xml");
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	Document doc = dBuilder.parse(fXmlFile);
     
    	//optional, but recommended
    	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
    	doc.getDocumentElement().normalize();
    	int count = 0;
     
    	//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
     
    	NodeList nList = doc.getElementsByTagName("AdvertiseBase");
     
    	System.out.println("----------------------------");
     
    	for (int temp = 0; temp < nList.getLength(); temp++) {
     
    		Node nNode = nList.item(temp);
     
    		//System.out.println("\nCurrent Element :" + nNode.getNodeName());
     
    		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
     
    			Element eElement = (Element) nNode;
    			
    			Ad Advertise = new Ad();
    			//String aa = eElement.getElementsByTagName("AdPicName").item(0).getTextContent();
    			
    			Advertise.setThumbnailImgUrl(eElement.getElementsByTagName("AdPicName").item(0).getTextContent());
    			Advertise.setUrl(eElement.getElementsByTagName("AdUrl").item(0).getTextContent());
    			Advertise.setId(count);
    			count++;
    			this.AdList.add(Advertise);
    			Advertise = null;
     
    		}
    	}
        } catch (Exception e) {
    	e.printStackTrace();
        }
    }
	
  

    public void refreshAdvertise(){
    	if (AdID >= (adGroup.getAds().size()-1)){
    		AdID= 0;
    		AdPicName = AdUtil.GetFileName(adGroup.getAds().get(0).getThumbnailImgUrl());
    		AdUrl = adGroup.getAds().get(0).getUrl();}
    	else{ AdID++;
    	AdPicName = AdUtil.GetFileName(adGroup.getAds().get(AdID).getThumbnailImgUrl());
    	AdUrl = adGroup.getAds().get(AdID).getUrl();}
    		
    }
    
    public String readAdPicName(){
    	return this.AdPicName;
    }
    
    public String readAdUrl(){
    	return this.AdUrl;
    }
    public boolean getAdFileLoadSuccess(){
    	return AdFileLoadSuccess;
    }
    
    public void getAds (Location location){
    	
		try {
			//WifiFingerPrint fingnerPrint = Util.getWifiInfoManager().mergeSamples();				
			//fingnerPrint.log();
			try {
				Gson gson = new Gson();
				String json = gson.toJson(location);
				JSONObject data = new JSONObject(json);

				if (Util.sendToServer(this.activity, MsgConstants.MT_AD_INFO_QUERY, data)) {
					//Util.showShortToast(this, R.string.locate_collected);
					//updateHintText(R.string.locate_collected);
				} else {
					// All errors should be handled in the sendToServer
					// method
				}
			} catch (Exception ex) {
				//Util.showToast(this, "LOCATE:104 " + ex.toString(), Toast.LENGTH_LONG);
				ex.printStackTrace();
				//updateHintText("LOCATE:104 ERROR: " + ex.getMessage());
				//finish();
				return;
			}
		} catch (Exception e) {
			//Util.showToast(this, "LOCATE:103 " + e.toString(), Toast.LENGTH_LONG);
			e.printStackTrace();
			//updateHintText("LOCATE:103 ERROR: " + e.getMessage());
			//finish();
			return;
		}
		
		return;
    	
    }
    
    public void setAdList(List<Ad> AdList){
    	this.AdList=AdList;
    }
    
    public void updateAndShowAds(){
    	
    }
    
    public void deleteAdvertises(){
    	
		if (adGroup == null) {
			return;
		}
		
		if (adGroup.getAds() == null) {
			return;
		}
    	
    	for(int i = 0;i<adGroup.getAds().size();i++){
			File file = new File (getAdPicPath(AdUtil.GetFileName(adGroup.getAds().get(i).getThumbnailImgUrl())));
			if (file.exists()) {

				file.delete();
				

			}
		}
    	
    }
    
	public String getAdXMLPath() {
		return AdXMLPath;
	}

	public void setAdXMLPath(String adXMLPath) {
		AdXMLPath = adXMLPath;
	}

}
	   
    
