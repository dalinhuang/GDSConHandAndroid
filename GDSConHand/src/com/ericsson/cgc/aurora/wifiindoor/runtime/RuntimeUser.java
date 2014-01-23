package com.ericsson.cgc.aurora.wifiindoor.runtime;

import java.util.List;
import org.andengine.entity.sprite.Sprite;

import com.ericsson.cgc.aurora.wifiindoor.drawing.runtime.PreciseCellLocation;

/**
 * @author haleyshi
 * 
 */
public class RuntimeUser {

	private List<Cell> path;
	private Cell startCell;
	private Cell currentCell;
	private Cell previousCell;
	private Cell nextCell;
	
	private String id;
	private float speed = 0.5f; //this should be less than 1, 1 means it pass the cell just by one interval
	private float currentProcessOfCell = 0;
	private boolean stopping;
	private boolean enableRotation;
	
	public RuntimeUser() {
	}

	public void setCurrentCell(Cell currentCell) {
		this.currentCell = currentCell;
	}
	
	public Cell getStartCell() {
		return startCell;
	}
	
	public void setStartCell(Cell startCell) {
		this.startCell = startCell;
	}

	public void setPath(List<Cell> path) {
		this.path = path;

		if (currentCell != null) {
			headingForNextCell();
		}
	}

	private Cell getNextStep() {
		if (path == null || path.isEmpty()) {
			this.stopping = true;
			return null;
		}
		this.stopping = false;
		return path.remove(0);
	}

	public Cell getCurrentCell() {
		return this.currentCell;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		// the speed should be less than 1
		if (speed > 1) {
			throw new IllegalArgumentException("speed should be less than 1");
		}
		this.speed = speed;
	}

	public void initialNavigating(RuntimeIndoorMap runtimeIndoorMap, Cell startCell) {
		setStartCell(startCell);
		setPath(runtimeIndoorMap.findPathFromParticularPosition(startCell));
		currentCell = startCell;
		headingForNextCell();
	}

	public boolean moving() {

		if (stopping) {
			return true;
		}

		// according to its speed
		currentProcessOfCell += (speed * 1);

		if (currentProcessOfCell >= 1) {
			// move into the next cell
			currentProcessOfCell--;
			previousCell = currentCell;
			currentCell = nextCell;

			headingForNextCell();
		}

		if (nextCell == null) {
			return false;
		}

		return true;
	}
	
	public void setEnableRotation(boolean enableRotation) {
		this.enableRotation = enableRotation;
	}

	private void headingForNextCell() {

		nextCell = getNextStep();

		if (nextCell == null) {
			return;
		}
		
		if (!enableRotation){
			return;
		}

		float rotation = 0;

		if (previousCell == null) {
			// by default the head up to down
			rotation = 0;

			int xPlus = nextCell.getColNo() - currentCell.getColNo();
			int yPlus = nextCell.getRowNo() - currentCell.getRowNo();

			if (xPlus < 0) {
				rotation = +90;
			} else if (xPlus > 0) {
				rotation = -90;
			} else if (yPlus > 0) {
				rotation = 0;
			} else if (yPlus < 0) {
				rotation = 180;
			}
		} else {
			float deltaX = (1f * nextCell.getColNo() + previousCell.getColNo())
					/ 2 - currentCell.getColNo();
			float deltaY = (1f * nextCell.getRowNo() + previousCell.getRowNo())
					/ 2 - currentCell.getRowNo();

			if (deltaX == 0 || deltaY == 0) {

			} else if (deltaX > 0 && deltaY > 0) {
				// |-
				if (nextCell.getRowNo() > previousCell.getRowNo()) {
					rotation = -90;
				} else {
					rotation = 90;
				}

			} else if (deltaX < 0 && deltaY < 0) {
				// _|
				if (nextCell.getRowNo() > previousCell.getRowNo()) {
					rotation = 90;
				} else {
					rotation = -90;
				}

			} else if (deltaX > 0 && deltaY < 0) {
				// |_
				if (nextCell.getRowNo() > previousCell.getRowNo()) {
					rotation = -90;
				} else {
					rotation = 90;
				}
			} else if (deltaX < 0 && deltaY > 0) {
				// -|
				if (nextCell.getRowNo() > previousCell.getRowNo()) {
					rotation = 90;
				} else {
					rotation = -90;
				}

			}
		}

		sprite.setRotation(sprite.getRotation() + rotation);
	}

	public PreciseCellLocation getPreciseCellLocation() {
		if (nextCell == null) {
			return new PreciseCellLocation(currentCell.getRowNo(), currentCell.getColNo());
		}
		
		float rowPosition = currentCell.getRowNo() * (1 - currentProcessOfCell)
				+ nextCell.getRowNo() * currentProcessOfCell;
		float colPosition = currentCell.getColNo() * (1 - currentProcessOfCell)
				+ nextCell.getColNo() * currentProcessOfCell;
		return new PreciseCellLocation(rowPosition, colPosition);
	}

	private Sprite sprite;

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public List<Cell> getPath() {
		return this.path;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}

