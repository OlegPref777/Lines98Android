package ru.ls.lines98.status;
import android.graphics.Canvas;
import android.graphics.Color;

import ru.ls.lines98.common.ColorUtil;

public class Digit {

	public Digit(int ledHorizontalWidth, int ledVerticalWidth, int ledHeight) {
		this.ledHorizontalWidth = ledHorizontalWidth;
		this.ledVerticalWidth = ledVerticalWidth;
		this.ledHeight = ledHeight;

		hOutside = ledHeight / 3;
		hInside = ledHeight - 2 * hOutside;
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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getWidth() {
		return 2 * ledHeight + ledHorizontalWidth;
	}

	public int getHeight() {
		return 3 * ledHeight + 2 * (2 + hInside + ledVerticalWidth);
	}

	public void draw(ColorUtil.Graphics g){
		drawHorizontalLed(g, left + ledHeight, top, states[value][0] ? onColor
				: offColor);
		drawHorizontalLed(g, left + ledHeight, top + ledHeight
				+ ledVerticalWidth + 2 * hInside, states[value][1] ? onColor
				: offColor);
		drawHorizontalLed(g, left + ledHeight, top + 2
						* (ledHeight + ledVerticalWidth + 2 * hInside),
				states[value][2] ? onColor : offColor);

		drawVerticalLed(g, left, top + ledHeight + hInside,
				states[value][3] ? onColor : offColor);
		drawVerticalLed(g, left + ledHeight + ledHorizontalWidth, top
				+ ledHeight + hInside, states[value][4] ? onColor : offColor);

		drawVerticalLed(g, left, top + 2 * ledHeight + ledVerticalWidth + 3
				* hInside, states[value][5] ? onColor : offColor);
		drawVerticalLed(g, left + ledHeight + ledHorizontalWidth, top + 2
						* ledHeight + ledVerticalWidth + 3 * hInside,
				states[value][6] ? onColor : offColor);

	}

//	public void draw(Canvas canvas) {
//		Graphics g = new Graphics(canvas);
//		draw(g);
//	}

	private void drawHorizontalLed(ColorUtil.Graphics g, int l, int t, int c){
		g.setColor(c);
		g.fillRect(l, t, ledHorizontalWidth, hOutside);
		g.fillRect(l - hOutside, t + hOutside, ledHorizontalWidth + 2 * hOutside, hInside);
		g.fillRect(l, t + hOutside + hInside, ledHorizontalWidth, hOutside);
	}

	private void drawHorizontalLed(Canvas canvas, int l, int t, int c) {
		ColorUtil.Graphics g = new ColorUtil.Graphics(canvas);
		drawHorizontalLed(g, l, t, c);
	}

	private void drawVerticalLed(ColorUtil.Graphics g, int l, int t, int c){
		g.setColor(c);
		g.fillRect(l, t, hOutside, ledVerticalWidth);
		g.fillRect(l + hOutside, t - hInside, hInside, ledVerticalWidth + 2
				* hInside);
		g.fillRect(l + hOutside + hInside, t, hOutside, ledVerticalWidth);

	}
	private void drawVerticalLed(Canvas canvas, int l, int t, int c) {
		ColorUtil.Graphics g = new ColorUtil.Graphics(canvas);
		drawVerticalLed(g, l, t, c);
	}

	private int left;
	private int top;
	private int value = 0;
	private int onColor = DEFAULT_ON_COLOR;
	private int offColor = DEFAULT_OFF_COLOR;

	private int ledHorizontalWidth;
	private int ledVerticalWidth;
	private int ledHeight;

	private int hOutside;
	private int hInside;

	public static final int DEFAULT_ON_COLOR = Color.rgb(131, 209, 252);
	public static final int DEFAULT_OFF_COLOR =  Color.rgb(0, 0, 64);

	private static final boolean[][] states = new boolean[][] {
			{ true, false, true, true, true, true, true },
			{ false, false, false, false, true, false, true },
			{ true, true, true, false, true, true, false },
			{ true, true, true, false, true, false, true },
			{ false, true, false, true, true, false, true },
			{ true, true, true, true, false, false, true },
			{ true, true, true, true, false, true, true },
			{ true, false, false, false, true, false, true },
			{ true, true, true, true, true, true, true },
			{ true, true, true, true, true, false, true } };
}
