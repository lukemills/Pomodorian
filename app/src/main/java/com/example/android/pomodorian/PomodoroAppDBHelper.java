package com.example.android.pomodorian;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lukem on 11/8/2017.
 */

public class PomodoroAppDBHelper extends SQLiteOpenHelper {
    // Database information
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sessions.db";

    private Context mContext;

    // Constructor of the database; stores context for use in deletion of entire database
    public PomodoroAppDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, DATABASE_VERSION);
        mContext = context;
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
    public void addProduct(WorkSession session) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryEntity.COLUMN_NAME, product.getmName());
        values.put(InventoryEntity.COLUMN_UPC, product.getmUPC());
        values.put(InventoryEntity.COLUMN_DESCRIPTION, product.getmDescription());
        values.put(InventoryEntity.COLUMN_RESTOCK, product.getmUPC());
        values.put(InventoryEntity.COLUMN_IMAGE, image);
        values.put(InventoryEntity.COLUMN_QUANTITY, product.getmQuantity());
        values.put(InventoryEntity.COLUMN_UNITS, product.getmUnits());
        values.put(InventoryEntity.COLUMN_APIID, product.getmApiId());

        switch (table){
            case 'i':
                db.insert(InventoryEntity.IN_STOCK_TABLE_NAME, null, values);
                break;
            case 'o':
                db.insert(InventoryEntity.OUT_STOCK_TABLE_NAME, null, values);
                break;
            case 's':
                db.insert(InventoryEntity.SHOP_STOCK_TABLE_NAME, null, values);
                break;
            default:
                break;
        }
        db.clos


    }
