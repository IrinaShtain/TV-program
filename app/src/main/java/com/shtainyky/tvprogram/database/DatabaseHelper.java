package com.shtainyky.tvprogram.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.shtainyky.tvprogram.database.ContractClass.Categories.COLUMN_CATEGORY_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Categories.COLUMN_CATEGORY_IMAGE_URL;
import static com.shtainyky.tvprogram.database.ContractClass.Categories.COLUMN_CATEGORY_TITLE;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_CATEGORY_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_IMAGE_URL;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_IS_PREFERRED;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_TITLE;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.COLUMN_PROGRAM_CHANNEL_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.COLUMN_PROGRAM_DATE;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.COLUMN_PROGRAM_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.COLUMN_PROGRAM_TIME;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.COLUMN_PROGRAM_TITLE;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tvprogram.db";

    private static final String TABLE_NAME_CATEGORIES = ContractClass.Categories.TABLE_NAME_CATEGORIES;

    private static final String DATABASE_CREATE_CATEGORIES = "create table " + TABLE_NAME_CATEGORIES + " (" +
            COLUMN_CATEGORY_ID + " integer primary key , " +
            COLUMN_CATEGORY_TITLE + " text not null ," +
            COLUMN_CATEGORY_IMAGE_URL + " text not null);";
    private static final String DROP_TABLE_CATEGORIES = "DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORIES;


    private static final String TABLE_NAME_PROGRAMS = ContractClass.Programs.TABLE_NAME_PROGRAMS;

    private static final String DATABASE_CREATE_PROGRAMS = "create table " + TABLE_NAME_PROGRAMS + " (" +
            COLUMN_PROGRAM_ID + " integer primary key autoincrement, " +
            COLUMN_PROGRAM_TITLE + " text not null ," +
            COLUMN_PROGRAM_TIME + " text not null ," +
            COLUMN_PROGRAM_DATE + " text not null ," +
            COLUMN_PROGRAM_CHANNEL_ID + " integer not null);";
    private static final String DROP_TABLE_PROGRAMS = "DROP TABLE IF EXISTS " + TABLE_NAME_PROGRAMS;

    private static final String TABLE_NAME_CHANNELS = ContractClass.Channels.TABLE_NAME_CHANNELS;

    private static final String DATABASE_CREATE_CHANNELS = "create table " + TABLE_NAME_CHANNELS + " (" +
            COLUMN_CHANNEL_ID + " integer primary key , " +
            COLUMN_CHANNEL_TITLE + " text not null ," +
            COLUMN_CHANNEL_IMAGE_URL + " text not null ," +
            COLUMN_CHANNEL_CATEGORY_ID + " integer not null ," +
            COLUMN_CHANNEL_IS_PREFERRED + " integer not null);";
    private static final String DROP_TABLE_CHANNELS = "DROP TABLE IF EXISTS " + TABLE_NAME_CHANNELS;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_CATEGORIES);
        db.execSQL(DATABASE_CREATE_CHANNELS);
        db.execSQL(DATABASE_CREATE_PROGRAMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CATEGORIES);
        db.execSQL(DROP_TABLE_CHANNELS);
        db.execSQL(DROP_TABLE_PROGRAMS);
        onCreate(db);
    }
}
