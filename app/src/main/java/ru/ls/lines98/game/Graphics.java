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

    public void fill3DRect(int x, int y, int width, int height, boolean raised) {
        int c = getColor();
        int brighter = brighter(c);
        int darker = darker(c);

        if (!raised) {
            setColor(darker);
        }
        fillRect(x + 1, y + 1, width - 2, height - 2);
        setColor(raised ? brighter : darker);
        fillRect(x, y, 2, height - 1);
        fillRect(x + 2, y, width - 2, 2);
        //MyCanvas.drawLine((float)x, (float)y, (float)x, (float)y + height - 1, MyPaint);//Вертикально вниз по левому краю
        //MyCanvas.drawLine((float)x + 1, (float)y, (float)x + width - 2, (float)y, MyPaint);//Горизонтально вправо по верху

        setColor(raised ? darker : brighter);
        fillRect(x, y + height, width, 2);
        fillRect(x + width, y,  2, height - 1);

//        MyCanvas.drawLine((float)x + 1, (float)y + height - 1, (float)x + width - 1, (float)y + height - 1, MyPaint); //Горизонтально вправо по низу
//        MyCanvas.drawLine((float)x + width - 1, (float)y, (float)x + width - 1, (float)y + height - 2, MyPaint);//Вертикально вниз по правому краю
        setColor(c);
    }

    double FACTOR = 0.7;
    private int brighter(int color) {

        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        int alpha = Color.alpha(color);

        int i = (int)(1.0/(1.0-FACTOR));
        if ( r == 0 && g == 0 && b == 0) {
            return Color.argb(alpha, i, i, i);
        }
        if ( r > 0 && r < i ) r = i;
        if ( g > 0 && g < i ) g = i;
        if ( b > 0 && b < i ) b = i;

        return Color.argb(alpha, Math.min((int)(r/FACTOR), 255),
                Math.min((int)(g/FACTOR), 255),
                Math.min((int)(b/FACTOR), 255)
                );
    }

    public int darker(int color) {
        return Color.argb(Color.alpha(color), Math.max((int)(Color.red(color)  * FACTOR), 0),
                Math.max((int)(Color.green(color) * FACTOR), 0),
                Math.max((int)(Color.blue(color) *FACTOR), 0));
    }
    private int getColor() {
        return MyPaint.getColor();
    }
}
