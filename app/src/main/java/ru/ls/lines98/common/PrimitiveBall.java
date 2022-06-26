package ru.ls.lines98.common;

import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;

import androidx.annotation.RequiresApi;


//https://www.javatpoint.com/android-simple-Canvas-example
public class PrimitiveBall {

	public PrimitiveBall() {
	}

	public PrimitiveBall(int width, int height) {
		this.width = width;
		this.height = height;
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void draw(ColorUtil.Graphics g2){
		Paint oldPaint = g2.getPaint();
		int dx = (int) (width / Math.sqrt(2));
		int dy = (int) (height / Math.sqrt(2));
		Point leftBottomPoint = new Point(getLeft() + (width - dx) / 2, getTop() + (height + dy) / 2);
		Point middlePoint = new Point(leftBottomPoint.x + dx / 2, leftBottomPoint.y - dy / 2);
		Point rightTopPoint = new Point(leftBottomPoint.x + dx, leftBottomPoint.y - dy);

		Paint p = new GradientPaint(leftBottomPoint, Color.BLACK, rightTopPoint, Color.BLACK);
		g2.setPaint(p);
		g2.fillArc(getLeft() + 1, getTop() + 1, width - 2, height - 2, 0, 360);

		Paint paint = new GradientPaint(leftBottomPoint, color, middlePoint, Color.BLACK);
		g2.setPaint(paint);
		g2.fillArc(getLeft(), getTop(), width, height, 134, 182);

		Paint paint2 = new GradientPaint(middlePoint, Color.BLACK, rightTopPoint, color);
		g2.setPaint(paint2);
		g2.fillArc(getLeft(), getTop(), width, height, -46, 182);

		g2.setPaint(oldPaint);
	}
	@RequiresApi(api = Build.VERSION_CODES.O)
	public void draw(Canvas canvas) {
		ColorUtil.Graphics g2 = new ColorUtil.Graphics(canvas);
		draw(g2);

	}

	protected int left;
	protected int top;
	protected int width;
	protected int height;
	protected int color;
}
