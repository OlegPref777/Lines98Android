package ru.ls.lines98.game;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.LinearGradient;
import android.graphics.Shader;

import kotlin.NotImplementedError;

public class GradientPaint extends Paint {
    public GradientPaint(Point pt1, int color1, Point pt2, int color2){
        setShader(new LinearGradient(pt1.x, pt1.y, pt2.x, pt2.y, color1, color2, Shader.TileMode.MIRROR));
    }
}
