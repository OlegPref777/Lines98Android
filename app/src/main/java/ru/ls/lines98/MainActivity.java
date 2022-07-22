package ru.ls.lines98;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;


import ru.ls.lines98.game.Ball;
import ru.ls.lines98.game.GameBoard;
import ru.ls.lines98.game.GamePanel;
import ru.ls.lines98.game.Square;

import ru.ls.lines98.dialogs.AboutDialog;
import ru.ls.lines98.option.GameInfo;
import ru.ls.lines98.option.GameType;
import ru.ls.lines98.dialogs.SettingsDialog;
import ru.ls.lines98.playerscore.SaveGameDAO;
import ru.ls.lines98.playerscore.ScoreHistoryDAO;
import ru.ls.lines98.sound.SoundManager;
import ru.ls.lines98.status.GameInfoBoard;
import ru.ls.lines98.dialogs.HighScoreDialog;
import ru.ls.lines98.playerscore.PlayerScore;
import ru.ls.lines98.playerscore.SaveGame;


import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    GamePanel gamePanel;

    public static Point getDisplaySize() {
        if (_this == null){
            throw new NullPointerException();
        }
        if (DisplaySize == null){
            DisplaySize = new Point();
            Display display = _this.getWindowManager().getDefaultDisplay();
            display.getSize(DisplaySize);
        }
        return DisplaySize;
    }

    private static Point DisplaySize = null;
    public static MainActivity _this = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _this = this;
        SoundManager.soundManager = new SoundManager(this);

        Square.SIZE = getDisplaySize().x / 9;
        Ball.MATURITY_SIZE = Square.SIZE * 33 / 45;
        Ball.GROWING_SIZE = Square.SIZE * 9 / 45;

        setContentView(R.layout.activity_main);
        gamePanel = findViewById(R.id.gamePanel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GameBoard gameBoard = new GameBoard(gamePanel);
        gamePanel.setGameBoard(gameBoard);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveHighScore();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //New Menu
        if (id == R.id.NewLinesGame){
            saveHighScore();
            gamePanel.getGameBoard().newGame(GameType.LINE);
            return true;
        }

        if (id == R.id.NewSquaresGame){
            saveHighScore();
            gamePanel.getGameBoard().newGame(GameType.SQUARE);
            return true;
        }

        if (id == R.id.NewBlocksGame){
            saveHighScore();
            gamePanel.getGameBoard().newGame(GameType.BLOCK);
            return true;
        }
        //Options Menu
        if (id == R.id.Settings){
            SettingsDialog settingsDialog = new SettingsDialog(this);
            settingsDialog.ShowDialog();
            gamePanel.invalidate();
            return true;
        }
        if (id == R.id.HighScores){
            showHighScoreDialog();
            return true;
        }

        //Control Menu
        if (id == R.id.StepBack){
            gamePanel.getGameBoard().stepBack();
            return true;
        }
        if (id == R.id.EndGame){
            endGame();
            return true;
        }

        if (id == R.id.LoadGame){
            SaveGame mySaveGame = new SaveGameDAO(MainActivity._this).getLast();
            if (mySaveGame != null){
                gamePanel.getGameBoard().loadGame(mySaveGame);
                return true;
            }else {
                return false;
            }
        }

        if (id == R.id.SaveGame){
            gamePanel.getGameBoard().saveGame();
            return true;
        }


        //About Menu
        if (id == R.id.About){
            AboutDialog aboutDialog = new AboutDialog(MainActivity.this);
            aboutDialog.ShowDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void saveHighScore() {
        GameInfoBoard gameInfoBoard = gamePanel.getGameBoard().getGameInfoBoard();
        // Stop the playing clock
        gameInfoBoard.setClockState(false);
        ScoreHistoryDAO scoreHistoryDAO = new ScoreHistoryDAO(this);
        if (gameInfoBoard.getScore().getScore() > 0){
            scoreHistoryDAO.UpdateRecord(new PlayerScore(-1, new Date(), gameInfoBoard.getClock().getSeconds(), GameInfo.getCurrentInstance().getGameType(), gameInfoBoard.getScore().getScore()));
        }
    }

    public void endGame() {
        saveHighScore();
        gamePanel.getGameBoard().newGame();
    }

    private void showHighScoreDialog() {
        HighScoreDialog highScoreDialog = new HighScoreDialog(MainActivity.this);
        highScoreDialog.ShowDialog();
    }
}