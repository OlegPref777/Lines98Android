package ru.ls.lines98.status;

import static ru.ls.lines98.game.GameBoard.*;

import java.util.List;

import ru.ls.lines98.game.Ball;
import ru.ls.lines98.game.Position;

public class GameState {
    public Ball[][] bakBallArray = new Ball[row][col];
    public int[] bakNextColorArray = new int[3];
    public List<Position> bakNextPositionList;
    public int Score;
}