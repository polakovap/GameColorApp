package com.example.gamecolorapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ScoresDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_PLAYER_NAME,
            MySQLiteHelper.COLUMN_SCORE
    };

    public ScoresDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    // Open the database for writing - open db connection
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Close the database
    public void close() {
        dbHelper.close();
    }

    // Add or update a player's score
    public void addOrUpdateScore(String playerName, int score) {
        // Validate playerName before proceeding
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PLAYER_NAME, playerName);
        values.put(MySQLiteHelper.COLUMN_SCORE, score);

        // Query to check if the player already exists
        Cursor cursor = database.query(
                MySQLiteHelper.TABLE_SCORES,
                allColumns,
                MySQLiteHelper.COLUMN_PLAYER_NAME + " = ?",
                new String[]{playerName},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            //player exists = update score
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ID));
            database.update(MySQLiteHelper.TABLE_SCORES, values,
                    MySQLiteHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        } else {
            //insert new record
            database.insert(MySQLiteHelper.TABLE_SCORES, null, values);
        }

        cursor.close();
    }

    // Retrieve the top players by score
    public List<String> getTopPlayers(int limit) {
        List<String> topPlayers = new ArrayList<>();

        Cursor cursor = database.query(
                MySQLiteHelper.TABLE_SCORES,
                allColumns,
                null,
                null,
                null,
                null,
                MySQLiteHelper.COLUMN_SCORE + " DESC",
                String.valueOf(limit)
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            @SuppressLint("Range") String playerName = cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_PLAYER_NAME));
            @SuppressLint("Range") int score = cursor.getInt(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SCORE));
            topPlayers.add(playerName + ": " + score);
            cursor.moveToNext();
        }

        cursor.close();
        return topPlayers;
    }
}
