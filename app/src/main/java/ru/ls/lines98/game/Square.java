package ru.ls.lines98.game;


import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class Square {

	public Square(View component) {
		this.component = component;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;

		if (this.ball != null) {
			this.ball.square = this;
		}
	}

	public boolean hasBall() {
		return ball != null;
	}

	public BallState getBallState() {
		if (ball == null) {
			return BallState.REMOVED;
		}

		return ball.getBallState();
	}

	public boolean isEnableDestroy(int color) {
		if (ball == null) {
			return false;
		}

		return ball.getBallState() == BallState.MATURE && ball.getColor() == color;
	}

	public void draw(Canvas g, boolean showGrowingBalls) {
		drawBackground(g);

		if (ball != null) {
			if (showGrowingBalls) {
				ball.draw(g);
			} else if (ball.getBallState() != BallState.GROWING) {
				ball.draw(g);
			}
		}
	}

	public void repaint() {
		component.invalidate(left, top, size, size);
	}

	private void drawBackground(Canvas canvas) {
		Graphics g = new Graphics(canvas);
		g.setColor(Color.LTGRAY);
		g.fill3DRect(left, top, SIZE, SIZE, true);
	}

	private int left;
	private int top;
	private int size = SIZE;
	private Ball ball;

	private View component;

	public static int SIZE = 45;
}
