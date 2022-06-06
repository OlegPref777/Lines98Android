package ru.ls.lines98.game.status;

import android.graphics.Canvas;

import ru.ls.lines98.MainActivity;
import ru.ls.lines98.game.Graphics;
import ru.ls.lines98.game.Square;

public class DigitalClock {

	public DigitalClock() {
		int koefficient = Square.SIZE / 45;;
		for (int i = 0; i < digits.length; i++) {
			digits[i] = new Digit(9 * koefficient, 8 * koefficient, 3 * koefficient);
		}
	}

	public void draw(Graphics g){
		for (Digit digit : digits) {
			digit.draw(g);
		}

		int tmp = digits[0].getHeight() / 4;
		int tmpLeft;

		tmpLeft = left + digits[4].getWidth() + 1;
		g.fillRect(tmpLeft, top + tmp, 1, 1);
		g.fillRect(tmpLeft, top + 3 * tmp, 1, 1);

		tmpLeft = left + 3 * digits[4].getWidth() + 3 + 2 + 1;
		g.fillRect(tmpLeft, top + tmp, 1, 1);
		g.fillRect(tmpLeft, top + 3 * tmp, 1, 1);
	}
	public void draw(Canvas canvas) {
		Graphics g = new Graphics(canvas);
		draw(g);
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;

		int second = seconds % 60;
		int minute = (seconds / 60) % 60;
		int hour = (seconds / 60) / 60;

		digits[4].setValue(hour % 10);

		digits[3].setValue(minute / 10);
		digits[2].setValue(minute % 10);

		digits[1].setValue(second / 10);
		digits[0].setValue(second % 10);
	}

	public void setLeft(int left) {
		this.left = left;

		digits[4].setLeft(left);

		left += digits[4].getWidth() + 3;
		digits[3].setLeft(left);

		left += digits[3].getWidth() + 2;
		digits[2].setLeft(left);

		left += digits[2].getWidth() + 3;
		digits[1].setLeft(left);

		left += digits[1].getWidth() + 2;
		digits[0].setLeft(left);
	}

	public void setTop(int top) {
		this.top = top;

		for (Digit digit : digits) {
			digit.setTop(top);
		}
	}

	public int getWidth() {
		return digits[0].getWidth() * 5 + 3 * 2 + 2 * 2;
	}

	public int getHeight() {
		return digits[0].getHeight();
	}

	@Override
	public String toString() {
		int second = seconds % 60;
		int minute = (seconds / 60) % 60;
		int hour = (seconds / 60) / 60;

		return String.format("%d:%d:%d", hour, minute, second);
	}

	private int seconds;
	private int left;
	private int top;

	private Digit[] digits = new Digit[5];
}
