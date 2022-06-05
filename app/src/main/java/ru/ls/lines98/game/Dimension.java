package ru.ls.lines98.game;

import android.graphics.Point;

import kotlin.NotImplementedError;

public class Dimension {
    private Point _this = new Point();
    public Dimension(int width, int height) {
        _this.x = width;
        _this.y = height;
        throw new NotImplementedError();
    }

    public int getWidth() {
        return _this.x;
    }
    public int getHeight() {
        return _this.y;
    }
}
