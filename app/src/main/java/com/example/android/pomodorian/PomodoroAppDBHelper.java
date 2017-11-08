package com.example.android.pomodorian;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lukem on 11/8/2017.
 */

public class PomodoroAppDBHelper extends SQLiteOpenHelper {
    // Database information
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sessions.db";


    // Constructor of the database; stores context for use in deletion of entire database
    public PomodoroAppDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, DATABASE_VERSION);
    }

    // Create the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_SESSIONS);
    }

    // Drop the old table when database is upgraded, then re-instantiate a new database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_SESSIONS);
        onCreate(db);
    }


    // SQL used to create the database
    private static final String SQL_CREATE_ENTRIES_SESSIONS = "CREATE TABLE " +
            PomodoroContract.PomodoroEntity.SESSIONS_TABLE_NAME + "(" +
            PomodoroContract.PomodoroEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            PomodoroContract.PomodoroEntity.COLUMN_DATE + " TEXT, " +
            PomodoroContract.PomodoroEntity.COLUMN_TIME + " TEXT, " +
            PomodoroContract.PomodoroEntity.COLUMN_DURATION + " INTEGER, " +
            PomodoroContract.PomodoroEntity.COLUMN_WORK_DURATION + " INTEGER, " +
            PomodoroContract.PomodoroEntity.COLUMN_BREAK_DURATION + " INTEGER, " +
            PomodoroContract.PomodoroEntity.COLUMN_STREAKS + " TEXT " +
            ");";

    // SQL used to drop the table
    private static final String SQL_DELETE_ENTRIES_SESSIONS =
            "DROP TABLE IF EXISTS " + PomodoroContract.PomodoroEntity.SESSIONS_TABLE_NAME;

    // addSession adds a new row to database for the given session.
    public void addSession(Session session) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PomodoroContract.PomodoroEntity.COLUMN_TIME, session.getTime());
        values.put(PomodoroContract.PomodoroEntity.COLUMN_DATE, session.getDate());
        values.put(PomodoroContract.PomodoroEntity.COLUMN_DURATION, session.getDuration());
        values.put(PomodoroContract.PomodoroEntity.COLUMN_WORK_DURATION, session.getWork_duration());
        values.put(PomodoroContract.PomodoroEntity.COLUMN_BREAK_DURATION, session.getBreak_duration());
        values.put(PomodoroContract.PomodoroEntity.COLUMN_STREAKS, session.getStreaks());

        db.insert(PomodoroContract.PomodoroEntity.SESSIONS_TABLE_NAME, null, values);
        db.close();
    }

    // Display data displays the entire database in a textView
    public Cursor getCursorToDb() {
        String query;
        SQLiteDatabase db = getReadableDatabase();
        query = "SELECT * FROM " + PomodoroContract.PomodoroEntity.SESSIONS_TABLE_NAME + " WHERE 1";
        return db.rawQuery(query, null);
    }

    public Cursor getCursorOnQuery(String query){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query, null);
    }

    // deleteProduct deletes a given product (represented by its argument productName) from the database
    public void deleteSession(int key) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + PomodoroContract.PomodoroEntity.SESSIONS_TABLE_NAME + " WHERE "
                        + PomodoroContract.PomodoroEntity._ID + "=\"" + key + "\";");
    }

}
