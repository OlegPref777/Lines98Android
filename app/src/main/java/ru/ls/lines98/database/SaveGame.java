package ru.ls.lines98.database;

import java.util.Date;
import java.util.List;

import ru.ls.lines98.game.GameBoard;
import ru.ls.lines98.game.Position;
import ru.ls.lines98.option.GameType;

public class SaveGame {
    private int id;
    private Date SaveDate;
    private ru.ls.lines98.option.GameType gameType;
    private int PlayTimeSeconds;
    private int Score;
    private BallSave[][] ballSaves = new BallSave[GameBoard.row][GameBoard.col];
    private int[] NextColors = new int[3];
    private List<Position> NextPositions;
    private boolean AutoSave;

    public boolean isAutoSave() {
        return AutoSave;
    }

    public void setAutoSave(boolean autoSave) {
        AutoSave = autoSave;
    }



    public BallSave[][] getBallSaves() {
        return ballSaves;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBallSaves(BallSave[][] ballSaves) {
        this.ballSaves = ballSaves;
    }

    public int[] getNextColors() {
        return NextColors;
    }

    public void setNextColors(int[] nextColors) {
        NextColors = nextColors;
    }

    public List<Position> getNextPositions() {
        return NextPositions;
    }

    public void setNextPositions(List<Position> nextPositions) {
        NextPositions = nextPositions;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public Date getSaveDate() {
        return SaveDate;
    }

    public void setSaveDate(Date saveDate) {
        SaveDate = saveDate;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public int getPlayTimeSeconds() {
        return PlayTimeSeconds;
    }

    public void setPlayTimeSeconds(int playTimeSeconds) {
        PlayTimeSeconds = playTimeSeconds;
    }
}
