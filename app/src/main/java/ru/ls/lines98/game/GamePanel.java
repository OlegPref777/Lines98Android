package ru.ls.lines98.game;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import ru.ls.lines98.MainActivity;

public class GamePanel extends View {

	public GamePanel(Context context){
		super(context);
		this.gameFrame = (MainActivity) context;
	}

	public void setGameBoard(final GameBoard gameBoard) {
		this.gameBoard = gameBoard;
//		setPreferredSize(gameBoard.getBoardSize());

		gameBoard.newGame();
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
					int x = (int) motionEvent.getX();
					int y = (int) motionEvent.getY();
					Position pos = gameBoard.squareFromMousePos(x, y);
					Square square = gameBoard.getSquare(pos);

					if (square.getBallState() == BallState.MATURE) {
						gameBoard.selectBall(pos);
					} else {
						if (gameBoard.getSelectedPosition() != null) {
							if (gameBoard.moveTo(pos)) {
							}
						}
					}

					if (gameBoard.isGameOver()) {
						gameFrame.endGame();
					}
				}
				return true;
			}
		});
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}
	@Override protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		gameBoard.draw(canvas);
	}

	private GameBoard gameBoard;
	private MainActivity gameFrame;

	private static final long serialVersionUID = -5724271697960761395L;
}
