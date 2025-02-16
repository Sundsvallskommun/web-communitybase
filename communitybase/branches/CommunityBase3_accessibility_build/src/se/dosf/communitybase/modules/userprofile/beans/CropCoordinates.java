package se.dosf.communitybase.modules.userprofile.beans;

import java.util.ArrayList;

public class CropCoordinates {

	private int x1;

	private int y1;

	private int x2;

	private int y2;
	
	public CropCoordinates() {
		
	}

	public CropCoordinates(ArrayList<Integer> coords) {

		this.x1 = coords.get(0);
		this.y1 = coords.get(1);
		this.x2 = coords.get(2);
		this.y2 = coords.get(3);
	}

	@Override
	public String toString() {

		return "x1: " + x1 + ", y1: " + y1 + ", x2: " + x2 + ", y2: " + y2;
	}

	public int getX1() {

		return x1;
	}

	public void setX1(int x1) {

		this.x1 = x1;
	}

	public int getY1() {

		return y1;
	}

	public void setY1(int y1) {

		this.y1 = y1;
	}

	public int getX2() {

		return x2;
	}

	public void setX2(int x2) {

		this.x2 = x2;
	}

	public int getY2() {

		return y2;
	}

	public void setY2(int y2) {

		this.y2 = y2;
	}
}