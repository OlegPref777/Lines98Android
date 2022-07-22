package ru.ls.lines98.playerscore;
import static ru.ls.lines98.playerscore.DBHelper.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ru.ls.lines98.option.GameType;

public class SaveGameDAO {
    private static final String TABLE_SAVES = "SAVES";
    private static final String COLUMN_SAVE_DATE = "SAVE_DATE";
    private static final String COLUMN_SAVE_CONTENT = "SAVE_CONTENT";
    private DBHelper dbHelper;
    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CreateDBStatement = "CREATE TABLE " + TABLE_SAVES + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SAVE_DATE + " INT, " + COLUMN_PLAY_TIME + " INT, " + COLUMN_GAME_TYPE + " INT, " + COLUMN_SCORE + " INT, "  + COLUMN_SAVE_CONTENT + " TEXT)";
        sqLiteDatabase.execSQL(CreateDBStatement);
    }

    public SaveGameDAO (Context context){
        dbHelper = DBHelper.newDBHelper(context);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion ==2){
            onCreate(sqLiteDatabase);
        }
    }

    public boolean addOne(SaveGame saveGame){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        cv.put(COLUMN_SAVE_DATE, saveGame.getSaveDate().getTime());
        cv.put(COLUMN_PLAY_TIME, saveGame.getPlayTimeSeconds());
        cv.put(COLUMN_GAME_TYPE, saveGame.getGameType().getValue());
        cv.put(COLUMN_SCORE, saveGame.getScore());
        cv.put(COLUMN_SAVE_CONTENT, gson.toJson(saveGame));
        long result = db.insert(TABLE_SAVES, null, cv);
        db.close();
        return result != -1;
    }

    public boolean ClearRecords(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.delete(TABLE_SAVES, null, null);
        db.close();
        return result != 0;
    }
    public int getMaxId(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String SQLStatement = "SELECT "+ COLUMN_ID + " FROM " + TABLE_SAVES + " WHERE " + COLUMN_SAVE_DATE + " = (SELECT MAX(" + COLUMN_SAVE_DATE + ") FROM "+ TABLE_SAVES + ")";
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
            return -1;
        }


    }
    public SaveGame getLast() {
        SaveGame Ret = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        int Id = getMaxId();
        if (Id != -1){

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String SQLStatement = "SELECT * FROM " + TABLE_SAVES + " WHERE " + COLUMN_ID + " = " + Id;
            Cursor cursor =  db.rawQuery (SQLStatement, null);
            GameType[] GameTypeValues  = GameType.values();
            if (cursor.moveToFirst()){
                int id = cursor.getInt(0);
                long SaveDate = cursor.getLong(1);
                int PlayTime = cursor.getInt(2);
                int gameType = cursor.getInt(3);
                int Score = cursor.getInt(4);
                String SaveContent = cursor.getString(5);
                GameType MyGameType = null;
                for (GameType gameTypeValue : GameTypeValues) {
                    if (gameTypeValue.getValue() == gameType) {
                        MyGameType = gameTypeValue;
                        break;
                    }
                }
                Ret = gson.fromJson(SaveContent, SaveGame.class);
                Ret.setId(id);
                Ret.setGameType(MyGameType);
            }
            cursor.close();
            db.close();
        }
        return Ret;
    }
}
