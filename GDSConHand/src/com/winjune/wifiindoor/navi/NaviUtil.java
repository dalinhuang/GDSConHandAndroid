package com.winjune.wifiindoor.navi;

public class NaviUtil {
	
	public static TrackNode  Dijkstra(int[][] wMatrix, int startIndex, int endIndex) {
        
        if (wMatrix == null || wMatrix.length == 0 ||
        	wMatrix.length != wMatrix[0].length) {
            	return null;
        }
		
		int[][] arcs = wMatrix;
		int num = arcs.length;

		TrackNode[] tempPaths = new TrackNode[num];
        
        for (int i = 0; i < num; i++) {
        	tempPaths[i] = new TrackNode(num);
        	tempPaths[i].addStep(startIndex);
        }
        
		// 标识节点是否已找到最短路径，从startIndex到n 为finish[n]=true;
		boolean[] finish = new boolean[num];

		// 记录从 startIndex 到 n 的最短路径为 min[n]
		int[] D = new int[num];
		// 使用队列记录路径途经节点

		// 初始化
		for (int i = 0; i < num; i++) {
			D[i] = arcs[startIndex][i];
			finish[i] = false;
		}
		finish[startIndex] = true;
		D[startIndex] = -1;

		int v = 0, min = 0;
		// 一个一个循环找出最短距离（共num－1个）
		for (int i = 1; i < num; i++) {
			min = Integer.MAX_VALUE;
			// 扫描找出非final集中最小的D[]
			for (int w = 0; w < num; w++) {
				if (!finish[w] && D[w] < min && D[w] != -1) {
					v = w;
					min = D[w];
				}
			}

			finish[v] = true;
			// 已找到目标，退出循环
			if (v == endIndex) {
				tempPaths[v].addStep(v);
				tempPaths[v].setDist(D[v]);
				return tempPaths[v];
			}

			// 更新各D[]数据
			for (int w = 0; w < num; w++) {
				if (!finish[w] && arcs[v][w] != -1) {
					if ((arcs[v][w] + min) < D[w] || D[w] == -1) {
						D[w] = arcs[v][w] + min;
						tempPaths[w] = tempPaths[v];
						//tempPaths[w].addStep(v);
					}
				}
			}
			
			tempPaths[v].addStep(v);      
		}
		
		return null;
	}
	
	public static TrackNode Dijkstra2(int[][] wMatrix, int startIndex, int endIndex) {
        
		if (wMatrix == null || wMatrix.length == 0 ||
            	wMatrix.length != wMatrix[0].length) {
                	return null;
            }	
		
		int nodeNum = wMatrix.length;
		TrackNode[] tempPaths = new TrackNode[nodeNum];
        
        for (int i = 0; i < nodeNum; i++) {
        	tempPaths[i] = new TrackNode(nodeNum);
        	tempPaths[i].addStep(startIndex);
        }	    
		
		boolean[] isLabel = new boolean[nodeNum];// 是否标号  
	        int[] indexs = new int[nodeNum];// 所有标号的点的下标集合，以标号的先后顺序进行存储，实际上是一个以数组表示的栈  
	        int i_count = 0;//栈的顶点  
	        int[] distance = wMatrix[startIndex].clone();// v0到各点的最短距离的初始值  
	        int index = startIndex;// 从初始点开始  
	        int presentShortest = 0;//当前临时最短距离  

	 
	        indexs[i_count++] = index;// 把已经标号的下标存入下标集中  
	        isLabel[index] = true;  
	          
	        while (i_count < nodeNum) {  
	            // 第一步：标号v0,即w[0][0]找到距离v0最近的点  
	 
	            int min = Integer.MAX_VALUE;  
	            for (int i = 0; i < nodeNum; i++) {  
	                if (!isLabel[i] && distance[i] != -1 && i != index) {  
	                    // 如果到这个点有边,并且没有被标号  
	                    if (distance[i] < min) {  
	                        min = distance[i];  
	                        index = i;// 把下标改为当前下标  
	                    }  
	                }  
	            } 

	            isLabel[index] = true;//对点进行标号  
	            indexs[i_count++] = index;// 把已经标号的下标存入下标集中  
	            
	            if (index == endIndex) {//found the end node  
	            	tempPaths[index].addStep(index);
	            	tempPaths[index].setDist(distance[index]);
	                return tempPaths[index];  
	            }  
	            
	            if (wMatrix[indexs[i_count - 1]][index] == -1 
	                    || presentShortest + wMatrix[indexs[i_count - 1]][index] > distance[index]) {  
	                // 如果两个点没有直接相连，或者两个点的路径大于最短路径  
	                presentShortest = distance[index];  
	            } else {  
	                presentShortest += wMatrix[indexs[i_count - 1]][index];  
	            }  
	 
	            // 第二步：将distance中的距离加入vi  
	            for (int i = 0; i < nodeNum; i++) {  
	                // 如果vi到那个点有边，则v0到后面点的距离加  
	                if (distance[i] == -1 && wMatrix[index][i] != -1) {// 如果以前不可达，则现在可达了  
	                    distance[i] = presentShortest + wMatrix[index][i];  
	                    tempPaths[i] = tempPaths[index];
	                    //tempPaths[i].addStep(index);
	                } else if (wMatrix[index][i] != -1 
	                        && presentShortest + wMatrix[index][i] < distance[i]) {  
	                    // 如果以前可达，但现在的路径比以前更短，则更换成更短的路径  
	                    distance[i] = presentShortest + wMatrix[index][i];  
	                    tempPaths[i] = tempPaths[index];
	                    //tempPaths[i].addStep(index);                  
	                }  
	 
	            }  
	            
	            tempPaths[index].addStep(index);	            
	        }
	        
	        return null;
	}
}
