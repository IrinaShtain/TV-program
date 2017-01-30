package com.shtainyky.tvprogram.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstants.DATABASE_CREATE_CATEGORIES);
        db.execSQL(DatabaseConstants.DATABASE_CREATE_CHANNELS);
        db.execSQL(DatabaseConstants.DATABASE_CREATE_PROGRAMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseConstants.DROP_TABLE_CATEGORIES);
        db.execSQL(DatabaseConstants.DROP_TABLE_CHANNELS);
        db.execSQL(DatabaseConstants.DROP_TABLE_PROGRAMS);
        onCreate(db);
    }
}
