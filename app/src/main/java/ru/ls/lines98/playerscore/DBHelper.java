package ru.ls.lines98.playerscore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.ls.lines98.option.GameType;

public class DBHelper extends SQLiteOpenHelper {
    private final static String SCORE_HISTORY_FILE_NAME = "ScoreHistory.db";
    private final static int SCORE_HISTORY_DB_VERSION = 1;
    public static final String COLUMN_SCORE = "SCORE";
    public static final String TABLE_HIGH_SCORES = "HIGH_SCORES";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_RECORD_DATE = "RECORD_DATE";
    public static final String COLUMN_PLAY_TIME = "PLAY_TIME";
    public static final String COLUMN_GAME_TYPE = "GAME_TYPE";

    public DBHelper(@Nullable Context context) {
        super(context, SCORE_HISTORY_FILE_NAME, null, SCORE_HISTORY_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CreateDBStatement = "CREATE TABLE " + TABLE_HIGH_SCORES + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECORD_DATE + " INT, " + COLUMN_PLAY_TIME + " INT, " + COLUMN_GAME_TYPE + " INT, " + COLUMN_SCORE + " INT)";
        sqLiteDatabase.execSQL(CreateDBStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(PlayerScore score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RECORD_DATE, score.getRecordDate().getTime());
        cv.put(COLUMN_PLAY_TIME, score.getPlayTimeSeconds());
        cv.put(COLUMN_GAME_TYPE, score.getGameType().getValue());
        cv.put(COLUMN_SCORE, score.getScore());
        long result = db.insert(TABLE_HIGH_SCORES, null, cv);
        db.close();
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public List<PlayerScore> getAll(){
        List<PlayerScore> Ret = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String SQLStatement = "SELECT * FROM " + TABLE_HIGH_SCORES;
        Cursor cursor =  db.rawQuery (SQLStatement, null);
        GameType[] GameTypeValues  = GameType.values();
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                long RecordDate = cursor.getLong(1);
                int PlayTime = cursor.getInt(2);
                int gameType = cursor.getInt(3);
                int Score = cursor.getInt(4);
                GameType MyGameType = null;
                for (int i = 0; i < GameTypeValues.length; i++){
                    if(GameTypeValues[i].getValue() == gameType){
                        MyGameType = GameTypeValues[i];
                        break;
                    }
                }
                Ret.add(new PlayerScore(id, new Date(RecordDate), PlayTime, MyGameType, Score));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return Ret;
    }
    public int getHighScore(GameType gameType){
        SQLiteDatabase db = this.getReadableDatabase();
        String SQLStatement = "SELECT MAX("+ COLUMN_SCORE + ") AS " + COLUMN_SCORE + " FROM " + TABLE_HIGH_SCORES + " WHERE " + COLUMN_GAME_TYPE + " = " + gameType.getValue() ;
        Cursor cursor =  db.rawQuery (SQLStatement, null);
        GameType[] GameTypeValues  = GameType.values();

        if (cursor.moveToFirst()){
            int Score = cursor.getInt(0);
            cursor.close();
            db.close();
            return Score;
        } else {
            cursor.close();
            db.close();
            return 0;
        }
    }

}
