package ru.ls.lines98.game;

public enum BallState {
	GROWING(1), MATURE(2), ANIMATE(3), REMOVED(4);
	private int value;

	BallState(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
