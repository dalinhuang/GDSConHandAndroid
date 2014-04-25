package com.winjune.wifiindoor.poi;

import java.util.ArrayList;

import com.winjune.wifiindoor.map.FieldInfo;
import com.winjune.wifiindoor.map.MapInfo;

public class POIManager {
	
	public static ArrayList<PlaceOfInterest> POIList = new ArrayList<PlaceOfInterest>();
	
	
	
	public static PlaceOfInterest findNearestPOI(int mapId, int placeX, int PlaceY) {
	
		return POIList.get(2);
	}
	
	
	public static String[] buildAutoCompleteText(){
        String[] labelArray = new String[0];
        ArrayList <String> labelList = new ArrayList<String>();

        for (PlaceOfInterest aPOI:POIList) {
        	if ((aPOI.label != null) && !(aPOI.label.isEmpty()))        		
        		labelList.add(aPOI.label);				
		}		
        
        labelArray = labelList.toArray(labelArray);
        
        return labelArray;
	}
	
	public static ArrayList<PlaceOfInterest> findPOIbyLabel(String text) {
		
		ArrayList<PlaceOfInterest> matchedPOIs = new ArrayList<PlaceOfInterest>();

		for (PlaceOfInterest poi: POIList) {
			
			if (poi.label != null) {
				if (poi.label.contains(text)) {
					matchedPOIs.add(poi);
				}
			}
		}
		
		return matchedPOIs;
	}
	

	
	
	public static PlaceOfInterest getPOIbyId(int poiId) {				
		return POIList.get(poiId);
	}	
	
	public static void addSamples(){
		BusStation aBusStation = new BusStation();
		BusLine aBusLine;
		aBusLine = new BusLine( aBusStation.id, // busStation POI id;
								"801路",
								"9:00", 
								"17:00",
								"票价:2元,月票通用",
								"大学城科学中心总站;广大公寓;广大生活区;广大;大学城枢纽站;华师;体育中心总站");
		
		aBusStation.addBusLine(aBusLine);

		aBusLine = new BusLine( aBusStation.id, // busStation POI id;
				"802路",
				"9:00", 
				"17:00",
				"票价:2元,月票通用",
				"大学城科学中心总站;广大公寓;广大生活区;广大;大学城枢纽站;华师;恒宝广场总站");

		aBusStation.addBusLine(aBusLine);	
		
		aBusLine = new BusLine( aBusStation.id, // busStation POI id;
				"803路",
				"9:00", 
				"17:00",
				"票价:4元,月票通用",
				"大学城科学中心总站;广大公寓;广大生活区;广大;大学城枢纽站;华师;黄沙大道总站");

		aBusStation.addBusLine(aBusLine);		
		
		
		
		POIList.add(aBusStation);
		
		
		TheatreInfo aTheatre = new TheatreInfo();
		MovieInfo aMovie;
		
		aMovie = new MovieInfo(aTheatre.id,
								"美国队长2",
								"50元",
								"《霍比特人》的故事大致发生在《魔戒》三部曲之前60年左右，讲述弗罗多的叔叔——“霍比特人”比尔博·巴金斯（马丁·弗瑞曼 饰）的冒险历程。他被卷入了一场收回矮人的藏宝地孤山的旅程——这个地方被恶龙史矛革所占领着。由于灰袍巫师甘道夫（伊安·麦克莱恩 饰），比尔博出乎意料地加入了由13个矮人组成的冒险队伍中。他们要面对成千上万的哥布林、半兽人，致命的座狼骑士以及巨大的蜘蛛怪，变形者以及巫师……");
		aMovie.addTodaySchedule("10:00-11:30;12:30-13:00");
		aTheatre.addMovie(aMovie);


		aMovie = new MovieInfo(aTheatre.id,
								"巨浪",
								"30元",
								"《007 天幕危机》詹姆斯·邦德(丹尼尔·克雷格饰)在伊斯坦堡的任务失败后而失去踪影，外界推测他已身亡，北约卧底探员资料竟因而外泄，身为邦德上司的M夫人(朱迪·丹奇饰)因此受到情报安全委员会新主席马洛利(拉尔夫·费因斯饰)的强烈质疑，遂成为政府调查的对象。而总部MI6竟遭攻击，被众人以为殉职的邦德秘密现身协助M夫人，她要邦德追查一名极度危险的罪犯，于是他循着线索前往澳门与上海。在神秘女子赛菲茵(贝纳尼丝·玛尔洛饰)与探员伊芙(娜奥米·哈里斯饰)的协助下，邦德追踪到在背后搞鬼的神秘人物洛乌西法(哈维尔·巴登饰)，更意外发现M夫人不为人知的秘密。为了拯救总部，邦德是否能再次不顾一切出面化解危机？");		
		aMovie.addTodaySchedule("9:00-9:30;10:30-11:00; 12:30-13:00; 13:30-14:00");
		aTheatre.addMovie(aMovie);
		
		aMovie = new MovieInfo(aTheatre.id,
								"太阳黑子",
								"30元",
								"《西游·降魔篇》讲述的是一个妖魔横行的世界，百姓苦不堪言。年轻的驱魔人玄奘以“舍小我，成大我”的大无畏精神，历尽艰难险阻，依次收服猪妖以及妖王之王孙悟空为徒，并用“大爱”将他们感化，而玄奘自己也终于领悟到了“大爱”的真意。为救天下苍生于水火，为赎还自己的罪恶，师徒四人义无反顾的踏上西行取经之路。");	
		aMovie.addTodaySchedule("10:10-11:30;12:30-13:00");
		aTheatre.addMovie(aMovie);

		POIList.add(aTheatre);		
		
		RestaurantInfo aRestaurantInfo = new RestaurantInfo();
		aRestaurantInfo.label = "餐厅";
		aRestaurantInfo.generalDesc = "今日8折优惠";
		
		aRestaurantInfo.addMenuItem("主食","草帽饼", "", "18元");
		aRestaurantInfo.addMenuItem("主食","水饺", "", "18元");
		aRestaurantInfo.addMenuItem("主食","牛肉拉面", "", "18元");
		aRestaurantInfo.addMenuItem("主食","牛腩面", "", "18元");
		
		aRestaurantInfo.addMenuItem("饮料","草帽饼", "", "18元");
		aRestaurantInfo.addMenuItem("饮料","水饺", "", "18元");
		aRestaurantInfo.addMenuItem("饮料","汽水", "", "18元");
		aRestaurantInfo.addMenuItem("饮料","咖啡", "", "18元");

		POIList.add(aRestaurantInfo);
					
	}
}
