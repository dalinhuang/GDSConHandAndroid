package com.winjune.wifiindoor.navi;

import java.util.ArrayList;

public class NaviHistory {
	
	ArrayList<NaviHisItem> historyList;
	
	class NaviHisItem {
		NaviNode startNode;
		NaviNode endNode;
	}
}
