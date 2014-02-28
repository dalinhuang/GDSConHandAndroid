package com.winjune.wifiindoor.types;

import java.util.ArrayList;

import com.winjune.wifiindoor.navi.NaviData;
import com.winjune.wifiindoor.navi.NaviInfo;
import com.winjune.wifiindoor.navi.NaviNode;
import com.winjune.wifiindoor.webservice.types.IType;

public class NaviInfoReply implements IType {
	private int id;
	private int versionCode; 
	private ArrayList<NaviNodeReply> nodes;
	private ArrayList<NaviDataReply> paths;

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<NaviNodeReply> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<NaviNodeReply> nodes) {
		this.nodes = nodes;
	}
	
	public ArrayList<NaviDataReply> getPaths() {
		return paths;
	}

	public void setPaths(ArrayList<NaviDataReply> paths) {
		this.paths = paths;
	}
	
	public NaviInfo toNaviInfo() {
		NaviInfo naviInfo = new NaviInfo();
		
		naviInfo.setId(id);
		naviInfo.setVersionCode(versionCode);
		
		if (nodes != null) {		
			ArrayList<NaviNode> nodes2 = new ArrayList<NaviNode>();
			
			for (NaviNodeReply node:nodes) {
				NaviNode node2 = new NaviNode();
				
				if (node != null) {
					node2.setId(node.getId());
					node2.setMapId(node.getMapId());
					node2.setX(node.getX());
					node2.setY(node.getY());
					node2.setName(node.getName());
					node2.setNameId(node.getNameId());
				}
				
				nodes2.add(node2);
			}
			
			naviInfo.setNodes(nodes2);
		}
		
		if (paths != null) {		
			ArrayList<NaviData> paths2 = new ArrayList<NaviData>();
			
			for (NaviDataReply path:paths) {
				NaviData path2 = new NaviData();
				
				if (path != null) {
					path2.setFrom(path.getFrom());
					path2.setTo(path.getTo());
					path2.setDistance(path.getDistance());
					path2.setForwardInfo(path.getForwardInfo());
					path2.setBackwardInfo(path.getBackwardInfo());
				}
				
				paths2.add(path2);
			}
			
			naviInfo.setPaths(paths2);
		}
			
		return naviInfo;
	}
}
