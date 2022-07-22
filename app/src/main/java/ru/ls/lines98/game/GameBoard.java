package ru.ls.lines98.game;


import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import ru.ls.lines98.MainActivity;
import ru.ls.lines98.R;
import ru.ls.lines98.common.ColorUtil;
import ru.ls.lines98.option.GameInfo;
import ru.ls.lines98.option.GameType;
import ru.ls.lines98.option.NextBallDisplayType;
import ru.ls.lines98.playerscore.SaveGameDAO;
import ru.ls.lines98.sound.SoundManager;
import ru.ls.lines98.playerscore.BallSave;
import ru.ls.lines98.status.GameInfoBoard;
import ru.ls.lines98.playerscore.SaveGame;
import ru.ls.lines98.status.GameState;

public class GameBoard {

	public GameBoard(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		gameInfoBoard = new GameInfoBoard(gamePanel);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				squareArray[i][j] = new Square(this.gamePanel);
				squareArray[i][j].setLeft(left + j * Square.SIZE);
				squareArray[i][j].setTop(top + i * Square.SIZE);
			}
		}
	}

	//@Override protected void onDraw(Canvas canvas) {
	public void draw(Canvas g) {
		gameInfoBoard.draw(g);

		boolean displayGrowingBalls = GameInfo.getCurrentInstance().getNextBallDisplayType() == NextBallDisplayType.ShowBoth || GameInfo.getCurrentInstance().getNextBallDisplayType() == NextBallDisplayType.ShowOnField;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				squareArray[i][j].draw(g, displayGrowingBalls);
			}
		}
	}

	public GameInfoBoard getGameInfoBoard() {
		return gameInfoBoard;
	}

	/**
	 * @return size of the game board including game information displayed on the
	 *         top
	 */
	public Dimension getBoardSize() {
		return new Dimension(2 * left + col * Square.SIZE, top + row * Square.SIZE + 1);
	}

	public void newGame(GameType gameType) {
		String AppName = MainActivity._this.getResources().getString(R.string.app_name);
		MainActivity._this.setTitle(AppName + " \"" + gameType.toString() + "\"");
		initBoard();
		addGrowingBall();
		bakGameState = null;
		gameOver = false;

		gameInfoBoard.getNextBallBoard().setNextColors(nextColorArray);
		gameInfoBoard.getClock().setSeconds(0);
		gameInfoBoard.getScore().setScore(0);
		gameInfoBoard.setClockState(true);

		GameInfo.getCurrentInstance().setGameType(gameType);

		gamePanel.invalidate();
	}

	public void newGame() {
		newGame(GameInfo.getCurrentInstance().getDefaultGameType());
	}

	public Square getSquare(Position pos) {
		if (pos != null){
			return squareArray[pos.x][pos.y];
		}else {
			return null;
		}
	}

	public Position getSelectedPosition() {
		return selectedPos;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void selectBall(Position pos) {
		if (selectedPos != null) {
			getSquare(selectedPos).getBall().unSelect();
		}

		selectedPos = pos;
		getSquare(selectedPos).getBall().select();
	}

	public void stepBack() {
		if (bakGameState != null) {
			restoreGame(bakGameState);
		}
	}

	public void saveGame() {
		SaveGame mySaveGame = new SaveGame();
		mySaveGame.setGameType (GameInfo.getCurrentInstance().getGameType());
		mySaveGame.setSaveDate(new Date());
		mySaveGame.setPlayTimeSeconds(gameInfoBoard.getClock().getSeconds());
		mySaveGame.setScore(gameInfoBoard.getScore().getScore());
		BallSave Balls[][] = mySaveGame.getBallSaves();
		int NextColors[] = mySaveGame.getNextColors();

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (squareArray[i][j].getBall() != null) {
					Balls[i][j] = new BallSave(squareArray[i][j].getBall());
				} else {
					Balls[i][j] = null;
				}
			}
		}


		for (int i = 0; i < 3; i++) {
			NextColors[i] = nextColorArray[i];
		}


		mySaveGame.setNextPositions(nextPositionList);

		new SaveGameDAO(MainActivity._this).addOne(mySaveGame);
	}

	public void loadGame(SaveGame mySaveGame) {

		String AppName = MainActivity._this.getResources().getString(R.string.app_name);
		MainActivity._this.setTitle(AppName + " \"" + mySaveGame.getGameType().toString() + "\"");

		bakGameState = null;
		gameOver = false;

		if (selectedPos != null) {
			getSquare(selectedPos).getBall().unSelect();
			selectedPos = null;
		}

		BallSave[][] ballSaves =  mySaveGame.getBallSaves();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (ballSaves[i][j] != null){
					squareArray[i][j].setBall(new Ball(ballSaves[i][j], squareArray[i][j]));
				}else {
					squareArray[i][j].setBall(null);
				}

			}
		}

		int[] NextColors = mySaveGame.getNextColors();
		for (int i = 0; i < 3; i++) {
			nextColorArray[i] =  NextColors[i];
		}

		nextPositionList = mySaveGame.getNextPositions();

		gameInfoBoard.getNextBallBoard().setNextColors(nextColorArray);
		gameInfoBoard.getClock().setSeconds(mySaveGame.getPlayTimeSeconds());
		gameInfoBoard.getScore().setScore(mySaveGame.getScore());
		gameInfoBoard.setClockState(true);

		GameInfo.getCurrentInstance().setGameType(mySaveGame.getGameType());

		gamePanel.invalidate();
	}

	public Position squareFromMousePos(int x, int y) {
		int i = (y - top) / Square.SIZE;
		int j = (x - left) / Square.SIZE;

		if (y < top || i >= row || x < left || j >= col) {
			return null;
		}

		return new Position(i, j);
	}

	public boolean moveTo(final Position positionTo) {
		if (moveThread != null && moveThread.isAlive()) {
			return false;
		}

		if (selectedPos == null) {
			throw new IllegalStateException("Selected position not null");
		}

		if (selectedPos.equals(positionTo)) {
			return false;
		}

		final List<Position> positionList = findPath(positionTo);
		if (positionList.size() == 0) {
			SoundManager.playCantMoveSound();
			return false;
		}

		if (bakGameState == null) {
			bakGameState = new GameState();
		}
		saveState(bakGameState);

		final Square squareFrom = squareArray[selectedPos.x][selectedPos.y];
		final Ball ballFrom = squareFrom.getBall();

		ballFrom.unSelect();
		selectedPos = null;
		squareFrom.setBall(null);

		SoundManager.playMoveSound();

		moveThread = new Thread(() -> {
			Square square = null;
			Ball saveBall;

			for (int i = 1; i < positionList.size(); i++) {
				Position pos = positionList.get(i);
				square = getSquare(pos);

				saveBall = square.getBall();

				square.setBall(new Ball(ballFrom.getColor(), BallState.MATURE, square));

				gamePanel.invalidate();

				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				square.setBall(saveBall);
			}

			Ball ballTo = square.getBall();
			square.setBall(ballFrom);

			List<Square> completeSquareList = getCompleteSquare(positionTo);
			if (completeSquareList.size() > 0) {
				explosionBall(completeSquareList);

				if (ballTo != null) {
					if (!square.hasBall()) {
						square.setBall(ballTo);
					} else {
						setNewGrowingPos(positionList.get(positionList.size() - 1), ballTo);
					}
				}
			} else {
				if (ballTo != null) {
					setNewGrowingPos(positionList.get(positionList.size() - 1), ballTo);
				}

				nextStep();
			}
			gamePanel.invalidate();
		});

		moveThread.start();

		return true;
	}

	private void saveState(GameState MyGameState) {

		MyGameState.Score =gameInfoBoard.getScore().getScore();

		try {
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					if (squareArray[i][j].getBall() != null) {
						MyGameState.bakBallArray[i][j] = (Ball) (squareArray[i][j].getBall().clone());
					} else {
						MyGameState.bakBallArray[i][j] = null;
					}
				}
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 3; i++) {
			MyGameState.bakNextColorArray[i] = nextColorArray[i];
		}

		MyGameState.bakNextPositionList = new ArrayList<Position>();
		MyGameState.bakNextPositionList.addAll(nextPositionList);
	}

	private void explosionBall(List<Square> listCompleteSquare) {
		Ball.hideBall(listCompleteSquare);
		int score = listCompleteSquare.size() + (listCompleteSquare.size() - 4) * listCompleteSquare.size();
		gameInfoBoard.getScore().setScore(gameInfoBoard.getScore().getScore() + score);
	}

	private void setNewGrowingPos(Position oldPos, Ball ball) {
		if (!nextPositionList.remove(oldPos)) {
			throw new IllegalArgumentException();
		}

		List<Position> positionList = getEmptyPositions();
		if (positionList.size() > 0) {
			Position pos = positionList.get(new Random().nextInt(positionList.size()));
			getSquare(pos).setBall(ball);

			nextPositionList.add(pos);
		}
	}

	private void nextStep() {
		List<Square> squareList = new ArrayList<Square>();
		for (Position pos : nextPositionList) {
			squareList.add(getSquare(pos));
		}
		Ball.growBall(squareList);

		for (Position pos : nextPositionList) {
			if (getSquare(pos).hasBall() && getSquare(pos).getBallState() == BallState.MATURE) {
				List<Square> listCompleteSquare = getCompleteSquare(pos);
				if (listCompleteSquare.size() > 0) {
					explosionBall(listCompleteSquare);
				}
			}
		}

		addGrowingBall();
		gameInfoBoard.getNextBallBoard().setNextColors(nextColorArray);

		if (nextPositionList.size() < 3) {
			gameOver = true;
		}
	}

	private void initBoard() {
		if (selectedPos != null) {
			getSquare(selectedPos).getBall().unSelect();
			selectedPos = null;
		}

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				squareArray[i][j].setBall(null);
			}
		}

		List<Position> listEmptyPosition = getEmptyPositions();
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			int idx = random.nextInt(listEmptyPosition.size());
			Square square = getSquare(listEmptyPosition.get(idx));
			square.setBall(new Ball(ColorUtil.getRandomColor(), BallState.MATURE, square));
			listEmptyPosition.remove(idx);
		}
	}

	private void addGrowingBall() {
		generateNextBall();
		generateNextColor();
		for (int i = 0; i < nextPositionList.size(); i++) {
			Square square = getSquare(nextPositionList.get(i));
			square.setBall(new Ball(nextColorArray[i], BallState.GROWING, square));
		}
	}

	private void generateNextBall() {
		nextPositionList = new ArrayList<Position>();
		Random random = new Random();
		List<Position> listEmptySquare = getEmptyPositions();

		for (int i = 0; i < 3; i++) {
			if (listEmptySquare.size() > 0) {
				int idx = random.nextInt(listEmptySquare.size());
				nextPositionList.add(listEmptySquare.get(idx));
				listEmptySquare.remove(idx);
			}
		}
	}

	private List<Position> getEmptyPositions() {
		List<Position> listPosition = new LinkedList<Position>();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (!squareArray[i][j].hasBall()) {
					listPosition.add(new Position(i, j));
				}
			}
		}

		return listPosition;
	}

	private void generateNextColor() {
		nextColorArray[0] = ColorUtil.getRandomColor();
		nextColorArray[1] = ColorUtil.getRandomColor();
		nextColorArray[2] = ColorUtil.getRandomColor();
	}

	private List<Position> findPath(Position positionTo) {
		List<Position> pathList = new LinkedList<Position>();
		Queue<Position2> positionQueue = new LinkedList<Position2>();

		resetVisitedArray();

		positionQueue.add(new Position2(selectedPos.x, selectedPos.y));
		while (positionQueue.size() > 0) {
			Position pos = positionQueue.poll();
			visitedArray[pos.x][pos.y] = true;

			if (pos.x == positionTo.x && pos.y == positionTo.y) {
				do {
					pathList.add(0, pos);
					pos = ((Position2) pos).prevPosition;
				} while (pos != null);

				break;
			}

			positionQueue.addAll(getNeighborsSquare((Position2) pos));
		}

		return pathList;
	}

	private void resetVisitedArray() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				visitedArray[i][j] = false;
			}
		}
	}

	private List<Position2> getNeighborsSquare(Position2 pos) {
		List<Position2> positionList = new LinkedList<Position2>();
		int x, y;

		if (pos.x > 0) {
			x = pos.x - 1;
			y = pos.y;
			if (!visitedArray[x][y] && squareArray[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new Position2(x, y, pos));
			}
		}

		if (pos.x < col - 1) {
			x = pos.x + 1;
			y = pos.y;
			if (!visitedArray[x][y] && squareArray[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new Position2(x, y, pos));
			}
		}

		if (pos.y > 0) {
			x = pos.x;
			y = pos.y - 1;
			if (!visitedArray[x][y] && squareArray[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new Position2(x, y, pos));
			}
		}

		if (pos.y < row - 1) {
			x = pos.x;
			y = pos.y + 1;
			if (!visitedArray[x][y] && squareArray[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new Position2(x, y, pos));
			}
		}

		return positionList;
	}

	private List<Square> getCompleteSquare(Position pos) {
		if (GameInfo.getCurrentInstance().getGameType() == GameType.LINE) {
			return getLinesComplete(pos);
		} else if (GameInfo.getCurrentInstance().getGameType() == GameType.SQUARE) {
			return getSquaresComplete(pos);
		} else {
			return getBlocksComplete(pos);
		}
	}

	private List<Square> getLinesComplete(Position pos) {
		List<Square> listCompleteSquare = new ArrayList<Square>();
		int color = getSquare(pos).getBall().getColor();

		List<Square> listTempSquare;
		Square square;
		int i, j;

		listTempSquare = new ArrayList<Square>();
		j = 1;
		while (pos.y + j < col && (square = squareArray[pos.x][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			j++;
		}
		j = -1;
		while (pos.y + j >= 0 && (square = squareArray[pos.x][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			j--;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		listTempSquare = new ArrayList<Square>();
		i = 1;
		while (pos.x + i < col && (square = squareArray[pos.x + i][pos.y]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i++;
		}
		i = -1;
		while (pos.x + i >= 0 && (square = squareArray[pos.x + i][pos.y]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i--;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		listTempSquare = new ArrayList<Square>();
		i = 1;
		j = 1;
		while (pos.x + i < col && pos.y + j < row
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i++;
			j++;
		}
		i = -1;
		j = -1;
		while (pos.x + i >= 0 && pos.y + j >= 0
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i--;
			j--;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		listTempSquare = new ArrayList<Square>();
		i = 1;
		j = -1;
		while (pos.x + i < col && pos.y + j >= 0
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i++;
			j--;
		}
		i = -1;
		j = 1;
		while (pos.x + i >= 0 && pos.y + j < row
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i--;
			j++;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		if (listCompleteSquare.size() > 0) {
			listCompleteSquare.add(squareArray[pos.x][pos.y]);
		}

		return listCompleteSquare;
	}

	private List<Square> getSquaresComplete(Position pos) {
		List<Square> listCompleteSquare = new ArrayList<Square>();

		int color = squareArray[pos.x][pos.y].getBall().getColor();
		boolean b1 = false;
		boolean b2 = false;
		boolean b3 = false;

		if (pos.x > 0 && pos.y > 0) {
			if (squareArray[pos.x][pos.y - 1].isEnableDestroy(color)
					&& squareArray[pos.x - 1][pos.y - 1].isEnableDestroy(color)
					&& squareArray[pos.x - 1][pos.y].isEnableDestroy(color)

			) {
				listCompleteSquare.add(squareArray[pos.x][pos.y - 1]);
				listCompleteSquare.add(squareArray[pos.x - 1][pos.y - 1]);
				listCompleteSquare.add(squareArray[pos.x - 1][pos.y]);
				b1 = true;
			}
		}

		if (pos.x > 0 && pos.y < row - 1) {
			if (squareArray[pos.x - 1][pos.y].isEnableDestroy(color)
					&& squareArray[pos.x - 1][pos.y + 1].isEnableDestroy(color)
					&& squareArray[pos.x][pos.y + 1].isEnableDestroy(color)

			) {
				if (!b1) {
					listCompleteSquare.add(squareArray[pos.x - 1][pos.y]);
				}
				listCompleteSquare.add(squareArray[pos.x - 1][pos.y + 1]);
				listCompleteSquare.add(squareArray[pos.x][pos.y + 1]);
				b2 = true;
			}
		}

		if (pos.x < col - 1 && pos.y < row - 1) {
			if (squareArray[pos.x][pos.y + 1].isEnableDestroy(color)
					&& squareArray[pos.x + 1][pos.y + 1].isEnableDestroy(color)
					&& squareArray[pos.x + 1][pos.y].isEnableDestroy(color)

			) {
				if (!b2) {
					listCompleteSquare.add(squareArray[pos.x][pos.y + 1]);
				}
				listCompleteSquare.add(squareArray[pos.x + 1][pos.y + 1]);
				listCompleteSquare.add(squareArray[pos.x + 1][pos.y]);
				b3 = true;
			}
		}

		if (pos.x < col - 1 && pos.y > 0) {
			if (squareArray[pos.x + 1][pos.y].isEnableDestroy(color)
					&& squareArray[pos.x + 1][pos.y - 1].isEnableDestroy(color)
					&& squareArray[pos.x][pos.y - 1].isEnableDestroy(color)

			) {
				if (!b3) {
					listCompleteSquare.add(squareArray[pos.x + 1][pos.y]);
				}
				listCompleteSquare.add(squareArray[pos.x + 1][pos.y - 1]);

				if (!b1) {
					listCompleteSquare.add(squareArray[pos.x][pos.y - 1]);
				}
			}
		}

		if (listCompleteSquare.size() > 0) {
			listCompleteSquare.add(squareArray[pos.x][pos.y]);
		}

		return listCompleteSquare;
	}

	private List<Square> getBlocksComplete(Position pos) {
		resetVisitedArray();

		List<Square> listCompleteSquare = getBlocksComplete(pos, getSquare(pos).getBall().getColor());

		if (listCompleteSquare.size() >= 7) {
			return listCompleteSquare;
		}

		return new ArrayList<Square>();
	}

	private List<Square> getBlocksComplete(Position pos, int color) {
		List<Square> listSquare = new ArrayList<Square>();

		if (!visitedArray[pos.x][pos.y]) {
			visitedArray[pos.x][pos.y] = true;

			Square square = getSquare(pos);
			if (square.isEnableDestroy(color)) {
				listSquare.add(square);

				if (pos.y > 0) {
					listSquare.addAll(getBlocksComplete(new Position(pos.x, pos.y - 1), color));
				}

				if (pos.x > 0) {
					listSquare.addAll(getBlocksComplete(new Position(pos.x - 1, pos.y), color));
				}

				if (pos.y < row - 1) {
					listSquare.addAll(getBlocksComplete(new Position(pos.x, pos.y + 1), color));
				}

				if (pos.x < col - 1) {
					listSquare.addAll(getBlocksComplete(new Position(pos.x + 1, pos.y), color));
				}
			}
		}

		return listSquare;
	}


	private void restoreGame(GameState gameState) {
		if (selectedPos != null) {
			getSquare(selectedPos).getBall().unSelect();
			selectedPos = null;
		}

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				squareArray[i][j].setBall(gameState.bakBallArray[i][j]);
			}
		}

		for (int i = 0; i < 3; i++) {
			nextColorArray[i] = gameState.bakNextColorArray[i];
		}

		nextPositionList = gameState.bakNextPositionList;

		gameInfoBoard.getScore().setScore(gameState.Score);

		gamePanel.invalidate();
	}

	public static int row = 9;
	public static int  col = 9;
	private Square[][] squareArray = new Square[row][col];
	boolean[][] visitedArray = new boolean[row][col];
	private Position selectedPos;

	private int left = 1;
	private int top = Square.SIZE + 8;

	private int[] nextColorArray = new int[3];
	private List<Position> nextPositionList;
	private GamePanel gamePanel;
	private Thread moveThread;

	private GameState bakGameState;
	private GameState savGameState;
	private int savSeconds;

	private GameInfoBoard gameInfoBoard;

	private boolean gameOver;

	private class Position2 extends Position {
		public Position2 prevPosition;

		public Position2(int x, int y) {
			super(x, y);
		}

		public Position2(int x, int y, Position2 prevPosition) {
			super(x, y);
			this.prevPosition = prevPosition;
		}
	}

}
