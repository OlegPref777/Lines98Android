package ru.ls.lines98.playerscore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ru.ls.lines98.MainActivity;
import ru.ls.lines98.common.StorageUtil;

public class PlayerScoreHistory {

	private List<PlayerScore> scores;
	private static PlayerScoreHistory instance = new PlayerScoreHistory();
	public static PlayerScoreHistory getInstance() {
		return instance;
	}

	private PlayerScoreHistory() {
		//scores = StorageUtil.<List<PlayerScore>>load(MainActivity._this, SCORE_HISTORY_FILE_NAME).orElse(new ArrayList<>());
	}

	public int getHighestScore() {
		return scores.stream().mapToInt(s -> s.getScore()).max().orElse(0);
	}

	public List<PlayerScore> getTopScores() {
		return scores;
	}

	public boolean isNewRecord(int score) {
		return score > 0
				&& (scores.size() < SCORE_HISTORY_LIMIT || scores.stream().anyMatch(ps -> ps.getScore() < score));
	}

	public void addHighScore(PlayerScore playerScore) {
		scores = Stream.concat(scores.stream(), Stream.of(playerScore))
				.sorted(Comparator.comparingInt(PlayerScore::getScore).reversed()).limit(SCORE_HISTORY_LIMIT)
				.collect(Collectors.toList());
	}

	public void save() {
		//StorageUtil.save(scores, MainActivity._this, SCORE_HISTORY_FILE_NAME);
	}

	private final static int SCORE_HISTORY_LIMIT = 10;




}
