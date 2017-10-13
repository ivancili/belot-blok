package com.cili.ivan.belotblock.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GAME_DATABASE";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_GAMES = "games";
    private static final String TABLE_GAMES_COL0 = "gameCreatedOn";
    private static final String TABLE_GAMES_COL1 = "teamOneName";
    private static final String TABLE_GAMES_COL2 = "teamTwoName";

    private static final String TABLE_GAME_DETAILS = "gameDetails";
    private static final String TABLE_GAME_DETAILS_COL0 = "gameCreatedOn";
    private static final String TABLE_GAME_DETAILS_COL1 = "sessionStartedOn";
    private static final String TABLE_GAME_DETAILS_COL2 = "teamOneScore";
    private static final String TABLE_GAME_DETAILS_COL3 = "teamTwoScore";

    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createGamesTable;
        String createGameDetailsTable;

        createGamesTable = "CREATE TABLE " + TABLE_GAMES + " (" +
                TABLE_GAMES_COL0 + " TEXT PRIMARY KEY," +
                TABLE_GAMES_COL1 + " TEXT," +
                TABLE_GAMES_COL2 + " TEXT)";

        createGameDetailsTable = "CREATE TABLE " + TABLE_GAME_DETAILS + " (" +
                TABLE_GAME_DETAILS_COL0 + " TEXT," +
                TABLE_GAME_DETAILS_COL1 + " TEXT," +
                TABLE_GAME_DETAILS_COL2 + " TEXT," +
                TABLE_GAME_DETAILS_COL3 + " TEXT," +
                "PRIMARY KEY (" + TABLE_GAME_DETAILS_COL0 + "," + TABLE_GAME_DETAILS_COL1 + "))";

        db.execSQL(createGamesTable);
        db.execSQL(createGameDetailsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_GAMES);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_GAME_DETAILS);
        onCreate(db);
    }

    public boolean addNewGame(String gameCreatedOn, String teamOneName, String teamTwoName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_GAMES_COL0, gameCreatedOn);
        contentValues.put(TABLE_GAMES_COL1, teamOneName);
        contentValues.put(TABLE_GAMES_COL2, teamTwoName);

        long result = db.insert(TABLE_GAMES, null, contentValues);

        return result != -1;
    }

    public boolean addNewSessionToGame(String gameCreatedOn, String sessionStartedOn, String teamOneScore, String teamTwoScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_GAME_DETAILS_COL0, gameCreatedOn);
        contentValues.put(TABLE_GAME_DETAILS_COL1, sessionStartedOn);
        contentValues.put(TABLE_GAME_DETAILS_COL2, teamOneScore);
        contentValues.put(TABLE_GAME_DETAILS_COL3, teamTwoScore);

        long result = db.insert(TABLE_GAME_DETAILS, null, contentValues);

        return result != -1;
    }

    public Cursor getGames() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GAMES;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getGameFromGames(String gameCreatedOn) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * " +
                " FROM " + TABLE_GAMES +
                " WHERE " + TABLE_GAMES_COL0 + " LIKE " + gameCreatedOn;

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void clearSessionsForGame(String gameCreatedOn) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query;
        query = "DELETE " +
                "FROM " + TABLE_GAME_DETAILS + " " +
                "WHERE " + TABLE_GAME_DETAILS_COL0 + " LIKE " + gameCreatedOn;

        db.execSQL(query);
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery;

        clearDBQuery = "DELETE FROM " + TABLE_GAMES;
        db.execSQL(clearDBQuery);

        clearDBQuery = "DELETE FROM " + TABLE_GAME_DETAILS;
        db.execSQL(clearDBQuery);
    }

    public Cursor getSessionsForGame(String gameCreatedOn) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * " +
                " FROM " + TABLE_GAME_DETAILS +
                " WHERE " + TABLE_GAME_DETAILS_COL0 + " LIKE " + gameCreatedOn;

        Cursor data = db.rawQuery(query, null);
        return data;
    }

}
