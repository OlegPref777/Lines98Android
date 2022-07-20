package ru.ls.lines98.playerscore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;

public class DBHelper extends SQLiteOpenHelper {
    private final static String DB_FILE_NAME = "Lines98.db";
    private final static int DB_VERSION = 1;


    public static DBHelper newDBHelper (@Nullable Context context) {
        File dbFile = context.getDatabasePath("ScoreHistory.db");
        if (dbFile.exists()){
            File newDatabaseFile = new File(dbFile.getParentFile(), DB_FILE_NAME);
            if (!dbFile.renameTo(newDatabaseFile)){
                Log.e("DBHelper", "Cannot rename file " + dbFile.getAbsolutePath() + " to " + newDatabaseFile.getAbsolutePath());
            }
        }
        return new DBHelper(context);
    }

    private DBHelper(@Nullable Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        ScoreHistoryDAO.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        ScoreHistoryDAO.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }




}
