package com.winjune.wifiindoor.drawing.runtime;

/**
 * @author haleyshi
 *
 */
public class PreciseCellLocation {
	private float rowPosition;
	
	private float colPosition;

	public float getRowPosition() {
		return rowPosition;
	}

	public void setRowPosition(float rowPosition) {
		this.rowPosition = rowPosition;
	}

	public float getColPosition() {
		return colPosition;
	}

	public void setColPosition(float colPosition) {
		this.colPosition = colPosition;
	}

	public PreciseCellLocation(float rowPosition, float colPosition) {
		super();
		this.rowPosition = rowPosition;
		this.colPosition = colPosition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(colPosition);
		result = prime * result + Float.floatToIntBits(rowPosition);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PreciseCellLocation other = (PreciseCellLocation) obj;
		if (Float.floatToIntBits(colPosition) != Float
				.floatToIntBits(other.colPosition))
			return false;
		if (Float.floatToIntBits(rowPosition) != Float
				.floatToIntBits(other.rowPosition))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PreciseCellLocation [rowPosition=" + rowPosition
				+ ", colPosition=" + colPosition + "]";
	}
}
