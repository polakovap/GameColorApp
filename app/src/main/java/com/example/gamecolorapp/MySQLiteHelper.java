package com.example.gamecolorapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "game_scores.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SCORES = "scores";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLAYER_NAME = "player_name";
    public static final String COLUMN_SCORE = "score";

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_SCORES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PLAYER_NAME + " TEXT NOT NULL, "
            + COLUMN_SCORE + " INTEGER NOT NULL);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create the db table on the first app run
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

        // Insert seed data
        db.execSQL("INSERT INTO " + TABLE_SCORES + " (player_name, score) VALUES ('Alice', 80);");
        db.execSQL("INSERT INTO " + TABLE_SCORES + " (player_name, score) VALUES ('Bob', 30);");
        db.execSQL("INSERT INTO " + TABLE_SCORES + " (player_name, score) VALUES ('Charlie', 40);");

    }

    //handle db upgrades when changing schema
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }
}
