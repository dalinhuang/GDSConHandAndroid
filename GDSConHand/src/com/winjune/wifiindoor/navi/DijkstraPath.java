package com.winjune.wifiindoor.navi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("UseSparseArrays")
public class DijkstraPath {	
	private String LOG_TAG = "Navigator";
	HashSet<DijkstraNode> open;
	HashSet<DijkstraNode> close;    
	HashMap<Integer,Integer> path;//封装路径距离
	HashMap<Integer,String> pathInfo;//封装路径信息
	
	public DijkstraPath(){
		open = new HashSet<DijkstraNode>();
		close = new HashSet<DijkstraNode>();
		path = new HashMap<Integer,Integer>();
		pathInfo =new HashMap<Integer,String>();
	}
    
    public DijkstraResult planPath(DijkstraMap map, int fromId, int toId) {    	
    	DijkstraNode start = map.getNode(fromId);
    	DijkstraNode toNode = map.getNode(toId);
    	
    	if (start == null) {
    		Log.e(LOG_TAG, "Null start: "+ fromId);
    		return null;
    	}
    	
    	if (toNode == null) {
    		Log.e(LOG_TAG, "Null end: "+ toId);
    		return null;    		
    	}    	    	
    		
    	Log.i(LOG_TAG, start.getName());

    	open = map.getNodes();
    	
    	Map<DijkstraNode,Integer> childs=start.getChild();        	
        for (DijkstraNode node:open) {
        	if (node == start) {
        		path.put(node.getId(), 0); 
        		pathInfo.put(node.getId(), start.getName()+"->"+node.getName()+":0\n");        		
        	} else {
        		path.put(node.getId(), Integer.MAX_VALUE); 
        		pathInfo.put(node.getId(), start.getName()+"->"+node.getName()+":M");
        	} 
        }
        
        for (DijkstraNode node: childs.keySet()) {
            	path.put(node.getId(), childs.get(node));  
            	pathInfo.put(node.getId(), start.getName()+" ->->-> "+node.getName()
            				 +": "+childs.get(node)+"\n\n");
        }
        
        open.remove(start);
        close.add(start);
    	
        computePath(start);
        
        Set<Map.Entry<Integer, String>> pathInfos= pathInfo.entrySet();
        for(Map.Entry<Integer, String> pathInfo:pathInfos){
        	if (pathInfo.getKey() == toId) {
        		DijkstraResult naviPath = new DijkstraResult(map.nodes.size());
        		
        		int dist = path.get(toId);
        		// not edge connected to the target node        		
        		if (dist == Integer.MAX_VALUE){
        			Log.i(LOG_TAG, "no edge is connected to target node.");
        			return null;
        		}
        		
        		Log.i(LOG_TAG, "Dist:"+dist);        		       		
        		naviPath.setDist(dist);
        		naviPath.appendPathDesc(pathInfo.getValue());
        		Log.i(LOG_TAG, pathInfo.getValue());
        		return naviPath;
        	}
        }
        
        Log.i(LOG_TAG, "no edge is connected to target node.");        
    	return null;
    }
    

    public void computePath(DijkstraNode start){
    	DijkstraNode nearest= getShortestPath(start);//取距离start节点最近的子节点,放入close
        
    	if(nearest==null){
            return;
        }
              
        close.add(nearest);
        open.remove(nearest);
        Map<DijkstraNode,Integer> childs=nearest.getChild();
        for(DijkstraNode child:childs.keySet()){
            if(open.contains(child)){//如果子节点在open中
                Integer newCompute=path.get(nearest.getId())+childs.get(child);
                if(path.get(child.getId())>newCompute){//之前设置的距离大于新计算出来的距离
                    path.put(child.getId(), newCompute);
                    pathInfo.put(child.getId(), pathInfo.get(nearest.getId())
                    		+" ->->-> "+child.getName()
                    		+": "+childs.get(child) +"\n\n");
                }
            }
        }
              
        computePath(start);//重复执行自己,确保所有子节点被遍历
        computePath(nearest);//向外一层层递归,直至所有顶点被遍历
    }
    /**
     * 获取与node最近的子节点
     */
    private DijkstraNode getShortestPath(DijkstraNode node){
    	
    	DijkstraNode res=null;
        int minDis=Integer.MAX_VALUE;
        Map<DijkstraNode,Integer> childs=node.getChild();
        for(DijkstraNode child:childs.keySet()){
            if(open.contains(child)){
                int distance=childs.get(child);
                if(distance<minDis){
                    minDis=distance;
                    res=child;
                }
            }
        }
        
        return res;
    }

}
