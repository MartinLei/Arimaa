package de.htwg.se.arimaa.util.character;

public class Position {
	private int x;
	private int y;

	public Position(int x, int y) {
		 setX(x);
		 setY(y);
	}

	public void setPositon(int x, int y) {

		setX(x);
		setY(y);
	}

	public void setX(int x) {

		this.x = x;
	}

	public void setY(int y) {

		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}