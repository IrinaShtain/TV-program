package com.shtainyky.tvprogram.database;

public interface DatabaseConstants {
    int DATABASE_VERSION = 1;
    String DATABASE_NAME = "tvprogram.db";

    String TABLE_NAME_CATEGORIES = "Categories";
    String COLUMN_CATEGORY_ID = "_id";
    String COLUMN_CATEGORY_TITLE = "category_title";
    String COLUMN_CATEGORY_IMAGE_URL = "category_image_url";
    String DATABASE_CREATE_CATEGORIES = "create table " + TABLE_NAME_CATEGORIES + " (" +
            COLUMN_CATEGORY_ID + " integer primary key , " +
            COLUMN_CATEGORY_TITLE + " text not null ," +
            COLUMN_CATEGORY_IMAGE_URL + " text not null);";
    String DROP_TABLE_CATEGORIES = "DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORIES;


    String TABLE_NAME_PROGRAMS = "Programs";
    String COLUMN_PROGRAM_ID = "_id";
    String COLUMN_PROGRAM_CHANNEL_ID = "program_channel_id";
    String COLUMN_PROGRAM_TITLE = "program_title";
    String COLUMN_PROGRAM_TIME = "program_time";
    String COLUMN_PROGRAM_DATE = "program_date";
    String DATABASE_CREATE_PROGRAMS = "create table " + TABLE_NAME_PROGRAMS + " (" +
            COLUMN_PROGRAM_ID + " integer primary key autoincrement, " +
            COLUMN_PROGRAM_TITLE + " text not null ," +
            COLUMN_PROGRAM_TIME + " text not null ," +
            COLUMN_PROGRAM_DATE + " text not null ," +
            COLUMN_PROGRAM_CHANNEL_ID + " integer not null);";
    String DROP_TABLE_PROGRAMS = "DROP TABLE IF EXISTS " + TABLE_NAME_PROGRAMS;

    String TABLE_NAME_CHANNELS = "Channels";
    String COLUMN_CHANNEL_ID = "_id";
    String COLUMN_CHANNEL_TITLE = "channel_title";
    String COLUMN_CHANNEL_IMAGE_URL = "channel_image_url";
    String COLUMN_CHANNEL_CATEGORY_ID = "category_id";
    String COLUMN_CHANNEL_IS_PREFERRED = "is_preferred";
    String DATABASE_CREATE_CHANNELS = "create table " + TABLE_NAME_CHANNELS + " (" +
            COLUMN_CHANNEL_ID + " integer primary key , " +
            COLUMN_CHANNEL_TITLE + " text not null ," +
            COLUMN_CHANNEL_IMAGE_URL + " text not null ," +
            COLUMN_CHANNEL_CATEGORY_ID + " integer not null ," +
            COLUMN_CHANNEL_IS_PREFERRED + " integer not null);";
    String DROP_TABLE_CHANNELS = "DROP TABLE IF EXISTS " + TABLE_NAME_CHANNELS;

}
