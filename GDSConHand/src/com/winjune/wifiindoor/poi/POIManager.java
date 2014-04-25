package com.winjune.wifiindoor.poi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

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
	
	public static Integer[] getHallsWithPlayhouse(){		
		HashSet<Integer>  hallSet = new HashSet<Integer> ();
  
        for (PlaceOfInterest et:POIList){        	
       		if (et.poiType == POIType.Playhouse)
       			hallSet.add(et.hallId);
        }
        	
        Integer[] placesGroup = hallSet.toArray(new Integer[0]);        
        return placesGroup;
	}		
	
	public static void addSamples(){
		
	}
	
	public static void addBusStationSamples(){
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
	}
	
	public static void addTheatreSamples(){
		
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
	}
	
	public static void addRestaurantSamples() {
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
	

	public static void addPlayhouseSamples(){		
		PlaceOfInterest poi;
		
		poi = new PlaceOfInterest("试验与发现馆");	
		POIList.add(poi);
		PlayhouseInfo eventItem1	= new PlayhouseInfo("电磁舞台");
		eventItem1.setPlace(0,0, poi.id);
		eventItem1.addNormalDayTimes("10:20;14:30");
		eventItem1.addHolidayTime(10, 30, 0, 0);
		eventItem1.addHolidayTime(12, 0, 0, 0);
		eventItem1.addHolidayTime(15, 30, 0, 0);	
		POIList.add(eventItem1);
	
		poi = new PlaceOfInterest("儿童天地馆");

		PlayhouseInfo eventItem2	= new PlayhouseInfo("基因剧场");
		POIList.add(poi);
		eventItem2.setPlace(0,0, poi.id);
		eventItem2.addNormalDayTime(11, 0, 0, 0);
		eventItem2.addNormalDayTime(14, 0, 0, 0);
		eventItem2.addHolidayTime(11, 0, 0, 0);
		eventItem2.addHolidayTime(14, 0, 0, 0);
		eventItem2.addHolidayTime(14, 30, 0, 0);	
		POIList.add(eventItem2);
		
		PlayhouseInfo eventItem3	= new PlayhouseInfo("启蒙剧场");
		eventItem3.setPlace(0,0, poi.id);	
		eventItem3.addHolidayTime(14, 30, 0, 0);	
		POIList.add(eventItem3);
		
		poi = new PlaceOfInterest("交通世界馆");
		POIList.add(poi);
		PlayhouseInfo eventItem4	= new PlayhouseInfo("磁悬浮技术");
		eventItem4.setPlace(0,0, poi.id);
		eventItem4.addWeekdayTime(10, 30, 12, 0);
		eventItem4.addWeekdayTime(14, 0, 15, 30);
		eventItem4.addFestivalTime(10, 0, 12, 0);
		eventItem4.addFestivalTime(14, 0, 16, 0);	
		POIList.add(eventItem4);	

		
		PlayhouseInfo eventItem5	= new PlayhouseInfo("模拟汽车工厂");
		eventItem5.setPlace(0,0, poi.id);
		eventItem5.addAllTime(9, 30, 12, 0);
		eventItem5.addAllTime(13, 0, 16, 30);			
		POIList.add(eventItem5);	

		poi = new PlaceOfInterest("数码世界馆");
		POIList.add(poi);
		PlayhouseInfo eventItem6	= new PlayhouseInfo("机器人搏击");
		eventItem6.setPlace(0,0, poi.id);
		eventItem6.addAllTime(10, 45, 0, 0);
		eventItem6.addAllTime(11, 45, 0, 0);
		eventItem6.addAllTime(14, 45, 0, 0);
		eventItem6.addAllTime(15, 45, 0, 0);	
		eventItem6.addFestivalTime(13, 45, 0, 0);
		POIList.add(eventItem6);	

		poi = new PlaceOfInterest("飞天之梦馆");
		POIList.add(poi);
		PlayhouseInfo eventItem7	= new PlayhouseInfo("太空生活表演");
		eventItem7.setPlace(0,0, poi.id);
		eventItem7.addAllTime(10, 30, 0, 0);
		eventItem7.addNormalDayTime(14, 20, 0, 0);
		eventItem7.addFestivalTime(13, 0, 0, 0);
		eventItem7.addFestivalTime(15, 0, 0, 0);	
		POIList.add(eventItem7);	

		
		PlayhouseInfo eventItem8	= new PlayhouseInfo("航天发射指挥控制中心");
		eventItem8.setPlace(0,0, poi.id);
		eventItem8.addNormalDayTime(10, 45, 0, 0);
		eventItem8.addNormalDayTime(13, 0, 0, 0);
		eventItem8.addNormalDayTime(14, 0, 0, 0);
		eventItem8.addHolidayTime(11, 0, 0, 0);
		eventItem8.addHolidayTime(13, 30, 0, 0);
		eventItem8.addHolidayTime(15, 0, 16, 0);	
		POIList.add(eventItem8);	

		PlayhouseInfo eventItem9	= new PlayhouseInfo("飞行模拟剧场");
		eventItem9.setPlace(0,0, poi.id);
		eventItem9.addAllTime(10, 15, 0, 0);
		eventItem9.addAllTime(11, 15, 0, 0);
		eventItem9.addAllTime(14, 0, 0, 0);
		eventItem9.addAllTime(14, 45, 0, 0);	
		eventItem9.addAllTime(15, 30, 0, 0);
		POIList.add(eventItem9);	
		
		poi = new PlaceOfInterest("绿色家园馆");
		POIList.add(poi);
		PlayhouseInfo eventItem10	= new PlayhouseInfo("台风体验");
		eventItem10.setPlace(0,0, poi.id);
		eventItem10.addAllTime(10, 0, 10, 30);
		eventItem10.addAllTime(10, 50, 11, 30);
		eventItem10.addAllTime(12, 50, 13, 30);
		eventItem10.addAllTime(13, 50, 14, 30);	
		eventItem10.addAllTime(14, 50, 15, 30);
		POIList.add(eventItem10);	
		
		PlayhouseInfo eventItem11	= new PlayhouseInfo("地球物体剧场");
		eventItem11.setPlace(0,0, 6);
		eventItem11.addAllTime(10, 45, 0, 0);
		eventItem11.addAllTime(11, 35, 0, 0);
		eventItem11.addAllTime(13, 35, 0, 0);
		eventItem11.addAllTime(14, 35, 0, 0);	
		eventItem11.addAllTime(15, 35, 0, 0);
		POIList.add(eventItem11);	
		
		poi = new PlaceOfInterest("人与健康馆");
		PlayhouseInfo eventItem12	= new PlayhouseInfo("虚拟人体漫游");
		eventItem12.setPlace(0,0, poi.id);
		eventItem12.addAllTime(9, 50, 0, 0);
		eventItem12.addAllTime(10, 30, 0, 0);
		eventItem12.addAllTime(11, 10, 0, 0);
		eventItem12.addAllTime(11, 50, 0, 0);	
		eventItem12.addAllTime(13, 40, 0, 0);
		eventItem12.addAllTime(14, 20, 0, 0);
		eventItem12.addAllTime(15, 0, 0, 0);
		eventItem12.addAllTime(15, 40, 0, 0);
		eventItem7.addFestivalTime(12, 30, 0, 0);
		eventItem7.addFestivalTime(13, 0, 0, 0);
		eventItem7.addFestivalTime(16, 20, 0, 0);
		POIList.add(eventItem12);	
		
		poi = new PlaceOfInterest("感知与思维馆");
		POIList.add(poi);
		PlayhouseInfo eventItem13	= new PlayhouseInfo("感知线索剧场");
		eventItem13.setPlace(0,0, poi.id);
		eventItem13.addAllTime(9, 30, 10, 0);
		eventItem13.addAllTime(10, 0, 10, 30);
		eventItem13.addAllTime(10, 30, 11, 0);
		eventItem13.addAllTime(11, 0, 11, 30);	
		eventItem13.addAllTime(11, 30, 12, 0);
		eventItem13.addAllTime(13, 30, 14, 0);
		eventItem13.addAllTime(14, 0, 14, 30);
		eventItem13.addAllTime(14, 30, 15, 0);
		eventItem13.addAllTime(15, 0, 15, 30);
		eventItem13.addAllTime(15, 30, 16, 0);
		eventItem13.addFestivalTime(12, 0, 12, 30);
		eventItem13.addFestivalTime(12, 30, 13, 30);
		eventItem13.addFestivalTime(16, 0, 16, 30);		
		POIList.add(eventItem13);
						
	}	
	
	public static ArrayList<MovieInfo> getMovieListByAlarm(){
		 
		ArrayList<MovieInfo> movieList = new ArrayList<MovieInfo>();
		
		for (PlaceOfInterest poi : POIList){
			
			if (poi.poiType == POIType.Theatre){
				
				TheatreInfo ti = (TheatreInfo) poi;
				ArrayList<MovieInfo> movies = ti.getMovies();
				
				for (MovieInfo mi : movies){
					
					if (mi.hasMovieAlarmToday()){
						movieList.add(mi);
					}
				}
			}
		}
		
		return movieList;
	}
	
	public static String getHallLabel(int hallId){
		
		return POIList.get(hallId).label;
	}	
}
