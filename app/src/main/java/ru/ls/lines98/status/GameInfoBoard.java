package ru.ls.lines98.status;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Typeface;

import ru.ls.lines98.MainActivity;
import ru.ls.lines98.game.GamePanel;
import ru.ls.lines98.game.Square;
import ru.ls.lines98.common.ColorUtil;
import ru.ls.lines98.common.Timer;
import ru.ls.lines98.option.GameInfo;
import ru.ls.lines98.option.NextBallDisplayType;
import ru.ls.lines98.playerscore.DBHelper;
import ru.ls.lines98.playerscore.ScoreHistoryDAO;


public class GameInfoBoard {

	private int left = 1;
	private int top = 2;
	private int width = Square.SIZE * 9;
	private int height = Square.SIZE + 5;

	private GamePanel gamePanel;

	private Score highestScore = new Score();
	private Score score = new Score();
	private DigitalClock clock = new DigitalClock();
	private NextBallBoard nextBallBoard = new NextBallBoard();

	private Timer clockTimer = new Timer(1000, new Runnable() {
		@Override
		public void run() {
			clock.setSeconds(clock.getSeconds() + 1);
			gamePanel.invalidate ();

		}
	});

	public GameInfoBoard(GamePanel gamePanel) {
		this.gamePanel = gamePanel;

		highestScore.setLeft(10);
		highestScore.setTop(top + (height - highestScore.getHeight()) / 2);

		score.setLeft(left + width - score.getWidth() - 10);
		score.setTop(top + (height - score.getHeight()) / 2);


		nextBallBoard.setLeft(left + (width - nextBallBoard.getWidth()) / 2);
		nextBallBoard.setTop(1);
		nextBallBoard.setNextColors(new int[] { Color.BLACK, Color.BLACK, Color.BLACK });

		clock.setLeft(left + (width - clock.getWidth()) / 2);
		clock.setTop(nextBallBoard.getHeight() + (int)(nextBallBoard.getHeight() * 0.05));


		clockTimer.start();

		ScoreHistoryDAO scoreHistoryDAO = new ScoreHistoryDAO(MainActivity._this);

		new Thread(() -> highestScore.setScore(scoreHistoryDAO.getHighScore(GameInfo.getCurrentInstance().getGameType()))).start();
	}

	public void draw(Canvas canvas) {
		ColorUtil.Graphics g = new ColorUtil.Graphics(canvas);
		g.setColor(Color.BLACK);
		g.fill3DRect(left, top, width, height, true);
		highestScore.draw(g);
		score.draw(g);
		int color = g.getColor();

		NextBallDisplayType displayType = GameInfo.getCurrentInstance().getNextBallDisplayType();
		if (displayType == NextBallDisplayType.ShowBoth || displayType == NextBallDisplayType.ShowOnTop) {
			nextBallBoard.draw(g);
		}

		drawGameType(g);
		g.setColor(color);
		clock.draw(g);
	}

	public Score getHighestScore() {
		return highestScore;
	}

	public Score getScore() {
		return score;
	}

	public DigitalClock getClock() {
		return clock;
	}

	public NextBallBoard getNextBallBoard() {
		return nextBallBoard;
	}

	public void setClockState(boolean isRun) {
		if (isRun) {
			if (!clockTimer.isRunning()) {
				clockTimer.start();
			}
		} else {
			clockTimer.stop();
		}
	}
	private void drawGameType(ColorUtil.Graphics g) {
		String gameTypeString = GameInfo.getCurrentInstance().getGameType().toString().toUpperCase();
		Paint myPaint = g.getPaint();
		myPaint.setTextSize(7);

		g.setFont(Typeface.DEFAULT_BOLD);
		g.setPaint(myPaint);

		float w = myPaint.measureText(gameTypeString, 0, gameTypeString.length());

		g.setColor(Color.rgb(0, 96, 191));
		g.drawString(gameTypeString, (int)(left + (width - w) / 2), top + 8);
	}

	private void drawGameType(Canvas canvas) {
		ColorUtil.Graphics g = new ColorUtil.Graphics(canvas);
		drawGameType(g);
	}

}
