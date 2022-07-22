package ru.ls.lines98.database;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ru.ls.lines98.MainActivity;

public class DBHelper extends SQLiteOpenHelper {
    private final static String DB_FILE_NAME = "Lines98.db";
    private final static int DB_VERSION = 3;
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_PLAY_TIME = "PLAY_TIME";
    public static final String COLUMN_GAME_TYPE = "GAME_TYPE";
    public static final String COLUMN_SCORE = "SCORE";


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
        SaveGameDAO.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        ScoreHistoryDAO.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
//        SaveGameDAO.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        Log.e(TAG, "Updating table from " + oldVersion + " to " + newVersion);
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.
        try {
            for (int i = oldVersion; i < newVersion; ++i) {
                String migrationName = String.format("from_%d_to_%d.sql", i, (i + 1));
                Log.d(TAG, "Looking for migration file: " + migrationName);
                readAndExecuteSQLScript(sqLiteDatabase, migrationName);
            }
        } catch (Exception exception) {
            Log.e(TAG, "Exception running upgrade script:", exception);
        }
    }
    private void readAndExecuteSQLScript(SQLiteDatabase db, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            Log.d(TAG, "SQL script file name is empty");
            return;
        }

        Log.d(TAG, "Script found. Executing...");
        AssetManager assetManager = MainActivity._this.getAssets();
        BufferedReader reader = null;

        try {
            InputStream is = assetManager.open(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            executeSQLScript(db, reader);
        } catch (IOException e) {
            Log.e(TAG, "IOException:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException:", e);
                }
            }
        }
    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                db.execSQL(statement.toString());
                statement = new StringBuilder();
            }
        }
    }
}
