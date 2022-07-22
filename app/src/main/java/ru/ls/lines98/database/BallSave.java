package ru.ls.lines98.database;

import ru.ls.lines98.game.Ball;
import ru.ls.lines98.game.BallState;

public class BallSave {
    private int color;
    private BallState state;
    public BallSave(Ball ball) {
        state = ball.getBallState();
        color = ball.getColor();
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setState(BallState state) {
        this.state = state;
    }

    public int getColor() {
        return color;
    }

    public BallState getState() {
        return state;
    }
}
