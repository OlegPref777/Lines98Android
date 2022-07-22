package ru.ls.lines98.database;

import static ru.ls.lines98.database.DBHelper.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.ls.lines98.option.GameType;

public class ScoreHistoryDAO {
    public static final String TABLE_HIGH_SCORES = "HIGH_SCORES";
    public static final String COLUMN_RECORD_DATE = "RECORD_DATE";

    private DBHelper dbHelper;
    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CreateDBStatement = "CREATE TABLE " + TABLE_HIGH_SCORES + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECORD_DATE + " INT, " + COLUMN_PLAY_TIME + " INT, " + COLUMN_GAME_TYPE + " INT, " + COLUMN_SCORE + " INT)";
        sqLiteDatabase.execSQL(CreateDBStatement);
    }

    public ScoreHistoryDAO (Context context){
        dbHelper = DBHelper.newDBHelper(context);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public boolean addOne(PlayerScore score){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RECORD_DATE, score.getRecordDate().getTime());
        cv.put(COLUMN_PLAY_TIME, score.getPlayTimeSeconds());
        cv.put(COLUMN_GAME_TYPE, score.getGameType().getValue());
        cv.put(COLUMN_SCORE, score.getScore());
        long result = db.insert(TABLE_HIGH_SCORES, null, cv);
        db.close();
        return result != -1;
    }

    public List<PlayerScore> getAll(){
        List<PlayerScore> Ret = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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
                for (GameType gameTypeValue : GameTypeValues) {
                    if (gameTypeValue.getValue() == gameType) {
                        MyGameType = gameTypeValue;
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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
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

    public boolean UpdateScoreRecord(PlayerScore score){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_RECORD_DATE, score.getRecordDate().getTime());
        cv.put(COLUMN_PLAY_TIME, score.getPlayTimeSeconds());
        cv.put(COLUMN_GAME_TYPE, score.getGameType().getValue());
        cv.put(COLUMN_SCORE, score.getScore());

        long result = db.update(TABLE_HIGH_SCORES, cv, COLUMN_GAME_TYPE + " = ? ", new String[] {String.valueOf(score.getGameType().getValue())});
        db.close();
        return result != 0;
    }

    public boolean UpdateRecord(PlayerScore score){
        int HighScore = getHighScore(score.getGameType());
        if (HighScore == 0){
            return addOne(score);
        }
        if (score.getScore() >= HighScore){
            return UpdateScoreRecord(score);
        }
        return false;
    }

    public boolean ClearRecords(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.delete(TABLE_HIGH_SCORES, null, null);
        db.close();
        return result != 0;
    }
}
