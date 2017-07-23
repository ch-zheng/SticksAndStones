package com.afrikappakorps.sticksandstones.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SticksAndStonesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ultraclash.db";
    private static final int DATABASE_VERSION = 6;

    public SticksAndStonesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ULTRA_CLASH_TABLE =
                "CREATE TABLE " + SticksAndStonesContract.PlayerEntry.TABLE_NAME + " (" +
                        SticksAndStonesContract.PlayerEntry._ID + " INTEGER PRIMARY KEY, " +
                        SticksAndStonesContract.PlayerEntry.COLUMN_PLAYER_NAME + " TEXT NOT NULL, " +
                        SticksAndStonesContract.PlayerEntry.COLUMN_POINT_COUNT + " INTEGER NOT NULL" +
                        "); ";
        db.execSQL(SQL_CREATE_ULTRA_CLASH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SticksAndStonesContract.PlayerEntry.TABLE_NAME);
        onCreate(db);
    }
}
