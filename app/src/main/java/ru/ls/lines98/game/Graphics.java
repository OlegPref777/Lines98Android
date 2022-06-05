package ru.ls.lines98.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

public class Graphics {
    private Canvas MyCanvas = null;
    private Paint MyPaint = new Paint();

    public Graphics(Canvas canvas){
        MyCanvas = canvas;
        MyPaint.setColor(Color.GREEN);
        MyPaint.setStyle(Paint.Style.STROKE);
    }
    public void setAntiAlias(boolean On){
        MyPaint.setAntiAlias(On);
    }

    public void setPaint(Paint paint){
        MyPaint = paint;
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle){
        MyPaint.setStyle(Paint.Style.FILL);
        RectF MyRect = new RectF((float)x, (float)y, (float)(width) + (float)x, (float)(height) + (float)y);
        MyCanvas.drawArc(MyRect, (float)startAngle, (float)arcAngle,false, MyPaint);
    }

    public void setColor(int color) {
        MyPaint.setColor(color);
    }

    public void drawString(String text, int x, int y) {
        MyCanvas.drawText(text, (float)x, (float)y, MyPaint);
    }

    public void fillRect(int x, int y, int width, int height) {
        MyPaint.setStyle(Paint.Style.FILL);
        RectF MyRect = new RectF((float)x, (float)y, (float)(width) + (float)x, (float)(height) + (float)y);
        MyCanvas.drawRect(MyRect, MyPaint);
    }

    public Typeface getFont() {
        return MyPaint.getTypeface();
    }

    public Paint getPaint() {
        return MyPaint;
    }

    public void setFont(Typeface typeface) {
        MyPaint.setTypeface(typeface);
    }

    public void fill3DRect(int x, int y, int width, int height, boolean b) {
        fillRect(x, y, width, height);
    }
}
