package com.winjune.wifiindoor.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.winjune.wifiindoor.runtime.Cell;

public class PathSearcher {

	public static List<Cell> findPath(boolean[][] passableMatrix, Cell start,
			Cell goal) {
		StarNode startNode = new StarNode(start);
		StarNode goalNode = new StarNode(goal);
		// open list
		LinkedList<StarNode> open = new LinkedList<StarNode>();
		LinkedList<StarNode> close = new LinkedList<StarNode>();
		// init
		startNode.setSearchParent(null);
		open.add(startNode);
		close.add(startNode);

		while (!open.isEmpty()) {

			StarNode node = open.removeFirst();
			if (node.equals(goalNode)) {
				return construct(node);
			} else {
				for (StarNode n : getNeighbors(passableMatrix, node, goalNode)) {
					if (!open.contains(n) && !close.contains(n)) {
						// System.out.println(node.getLocation() + " -> " +
						// n.getLocation());
						n.setSearchParent(node);
						open.add(n);
						close.add(n);
					}
				}
			}
		}

		return null;
	}

	public static List<Cell> findKeyCells(boolean[][] passableMatrix,
			Cell start, Cell goal) {

		List<Cell> result = findPath(passableMatrix, start, goal);

		Iterator<Cell> iterator = result.iterator();

		while (iterator.hasNext()) {
			Cell cell = iterator.next();
			boolean[][] cloneMap = passableMatrix.clone();
			cloneMap[cell.getRowNo()][cell.getColNo()] = false;
			if (findPath(cloneMap, start, goal) == null) {
				iterator.remove();
			}
		}

		return result;
	}

	/**
	 * 
	 * @param passableMatrix
	 * @return
	 */
	private static boolean isRealRoad(boolean[][] passableMatrix, int x, int y) {
		if (x < 0 || y < 0 || y >= passableMatrix[0].length
				|| x >= passableMatrix.length) {
			return false;
		} else {
			return passableMatrix[x][y];
		}
	}

	private static Random random = new Random();

	private static List<StarNode> getNeighbors(boolean[][] passableMatrix,
			StarNode node, StarNode goalNode) {

		List<StarNode> neighbors = new ArrayList<StarNode>();
		int x = node.getLocation().getRowNo();
		int y = node.getLocation().getColNo();

		// XXX the order of path search!

		int targetX = goalNode.getLocation().getRowNo();
		int targetY = goalNode.getLocation().getColNo();

		boolean xPrior;
		if (targetX == x) {
			xPrior = false;
		} else {
			if (targetY == y) {
				xPrior = true;
			} else {
				xPrior = random.nextBoolean();
			}
		}

		int[] order = new int[4];
		int xIndex_1;
		int xIndex_2;
		int yIndex_1;
		int yIndex_2;

		if (xPrior) {
			xIndex_1 = 0;
			xIndex_2 = 3;
			yIndex_1 = 1;
			yIndex_2 = 2;
		} else {
			yIndex_1 = 0;
			yIndex_2 = 3;
			xIndex_1 = 1;
			xIndex_2 = 2;
		}

		if (targetX > x) {
			order[xIndex_1] = X_ADD;
			order[xIndex_2] = X_MINUS;
		} else if (targetX < x) {
			order[xIndex_1] = X_MINUS;
			order[xIndex_2] = X_ADD;
		}

		if (targetY > y) {
			order[yIndex_1] = Y_ADD;
			order[yIndex_2] = Y_MINUS;
		} else if (targetY < y) {
			order[yIndex_1] = Y_MINUS;
			order[yIndex_2] = Y_ADD;
		}

		for (int i = 0; i < order.length; i++) {
			int value = order[i];
			switch (value) {
			case X_ADD:
				addNeighbor(neighbors, passableMatrix, x + 1, y);
				break;
			case X_MINUS:
				addNeighbor(neighbors, passableMatrix, x - 1, y);
				break;
			case Y_ADD:
				addNeighbor(neighbors, passableMatrix, x, y + 1);
				break;
			case Y_MINUS:
				addNeighbor(neighbors, passableMatrix, x, y - 1);
				break;
			}
		}

		return neighbors;
	}

	private static final int X_ADD = 1;
	private static final int X_MINUS = 2;
	private static final int Y_ADD = 3;
	private static final int Y_MINUS = 4;

	private static void addNeighbor(List<StarNode> neighbors,
			boolean[][] passableMatrix, int x, int y) {
		if (isRealRoad(passableMatrix, x, y)) {
			neighbors.add(new StarNode(new Cell(x, y)));
		}
	}

	private static List<Cell> construct(StarNode node) {

		LinkedList<Cell> path = new LinkedList<Cell>();
		while (node != null) {
			path.addFirst(node.getLocation());
			node = node.getSearchParent();
		}
		// remove the current positon from the path
		path.removeFirst();
		return path;
	}

}
