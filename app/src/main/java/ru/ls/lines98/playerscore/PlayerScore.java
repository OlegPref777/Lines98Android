package ru.ls.lines98.playerscore;

import java.util.Date;

import ru.ls.lines98.option.GameType;

public class PlayerScore {
	private int id;
	private Date recordDate;
	private int playTimeSeconds;
	private int score;
	private GameType gameType;

	public PlayerScore(int id, Date recordDate, int playTimeSeconds, GameType gameType, int score) {
		this.id = id;
		this.recordDate = recordDate;
		this.playTimeSeconds = playTimeSeconds;
		this.score = score;
		this.gameType = gameType;
	}

	@Override
	public String toString() {
		return "PlayerScore{" +
				"id=" + id +
				", recordDate=" + recordDate +
				", playTimeSeconds=" + playTimeSeconds +
				", score=" + score +
				", gameType=" + gameType +
				'}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public int getPlayTimeSeconds() {
		return playTimeSeconds;
	}

	public void setPlayTimeSeconds(int playTimeSeconds) {
		this.playTimeSeconds = playTimeSeconds;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}
}
