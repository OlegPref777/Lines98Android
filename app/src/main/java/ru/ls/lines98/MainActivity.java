package ru.ls.lines98;

import android.app.AlertDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;


import ru.ls.lines98.game.Ball;
import ru.ls.lines98.game.GameBoard;
import ru.ls.lines98.game.GamePanel;
import ru.ls.lines98.game.Square;

import ru.ls.lines98.dialogs.AboutDialog;
import ru.ls.lines98.option.GameType;
import ru.ls.lines98.dialogs.OptionDialog;
import ru.ls.lines98.sound.SoundManager;
import ru.ls.lines98.status.GameInfoBoard;
import ru.ls.lines98.dialogs.HighScoreDialog;
import ru.ls.lines98.status.PlayerScore;
import ru.ls.lines98.status.PlayerScoreHistory;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

        setTitle("Lines98");

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
            OptionDialog optionDialog = new OptionDialog(this);
            optionDialog.ShowDialog();
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
            gamePanel.getGameBoard().loadGame();
            return true;
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

        PlayerScoreHistory playerScoreHistory = PlayerScoreHistory.getInstance();

        // Player gets a new high score
        if (playerScoreHistory.isNewRecord(gameInfoBoard.getScore().getScore())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("New high score");
            builder.setMessage("You've got a high score. Please input your name");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            final String[] playerName = {""};

            builder.setPositiveButton("OK", (dialog, which) -> playerName[0] = input.getText().toString());
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            //Setting the title manually
            AlertDialog alert = builder.create();
            alert.setTitle("AlertDialogExample");
            alert.show();
            if (playerName[0] != null && !"".equals(playerName[0])) {
                playerScoreHistory.addHighScore(new PlayerScore(playerName[0], gameInfoBoard.getScore().getScore(),	gameInfoBoard.getClock().toString()));
                playerScoreHistory.save();

                showHighScoreDialog();
            }
        }

        // Update highest score on the game status board
        gameInfoBoard.getHighestScore().setScore(playerScoreHistory.getHighestScore());
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