package com.shtainyky.tvprogram.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shtainyky.tvprogram.model.Category;
import com.shtainyky.tvprogram.model.Channel;
import com.shtainyky.tvprogram.model.Program;
import com.shtainyky.tvprogram.utils.QueryPreferences;

import java.util.ArrayList;
import java.util.List;

import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_CATEGORY_ID;
import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_CATEGORY_IMAGE_URL;
import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_CATEGORY_TITLE;
import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_CHANNEL_CATEGORY_ID;
import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_CHANNEL_ID;
import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_CHANNEL_IMAGE_URL;
import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_CHANNEL_IS_PREFERRED;
import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_CHANNEL_TITLE;
import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_PROGRAM_CHANNEL_ID;
import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_PROGRAM_DATE;
import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_PROGRAM_TIME;
import static com.shtainyky.tvprogram.database.DatabaseConstants.COLUMN_PROGRAM_TITLE;
import static com.shtainyky.tvprogram.database.DatabaseConstants.TABLE_NAME_CATEGORIES;
import static com.shtainyky.tvprogram.database.DatabaseConstants.TABLE_NAME_CHANNELS;
import static com.shtainyky.tvprogram.database.DatabaseConstants.TABLE_NAME_PROGRAMS;

public class DatabaseSource {

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private SQLiteOpenHelper mDbHelper;

    public DatabaseSource(Context context) {
        mContext = context;
        mDbHelper = new DatabaseHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    private void open() {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    private void close() {
        mDbHelper.close();
    }

    //working with channels
    public List<String> getAllChannelsTitles() {
        List<String> channelsTitles = new ArrayList<>();
        if (QueryPreferences.areCategoriesLoaded(mContext)) {
            String selectQuery = "SELECT * FROM " + TABLE_NAME_CHANNELS;
            open();

            Cursor cursor = mDatabase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    channelsTitles.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
            close();
        }
        return channelsTitles;
    }

    public List<Channel> getAllChannel() {
        List<Channel> channels = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_CHANNELS;
        open();

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Channel channel = new Channel();
                channel.setId(cursor.getInt(0));
                channel.setName(cursor.getString(1));
                channel.setCategory_name(getCategoryNameForChannel(cursor.getInt(3)));
                channels.add(channel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        close();

        return channels;
    }

    private String getCategoryNameForChannel(int id) {
        String query = "SELECT " + COLUMN_CATEGORY_TITLE + " FROM " + TABLE_NAME_CATEGORIES
                + " WHERE " + COLUMN_CATEGORY_ID + " = " + String.valueOf(id);
        open();
        String titleCategory;
        Cursor cursor = mDatabase.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            titleCategory = cursor.getString(0);
            cursor.close();
        } else titleCategory = "Category";
        close();
        return titleCategory;
    }

    public void insertListChannels(List<Channel> channels) {
        open();
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_CHANNEL_ID, channel.getId());
            values.put(COLUMN_CHANNEL_TITLE, channel.getName());
            values.put(COLUMN_CHANNEL_IMAGE_URL, channel.getPictureUrl());
            values.put(COLUMN_CHANNEL_CATEGORY_ID, channel.getCategory_id());
            values.put(COLUMN_CHANNEL_IS_PREFERRED, 0);
            mDatabase.insert(TABLE_NAME_CHANNELS, null, values);
        }

        QueryPreferences.setChannelLoaded(mContext, true);
    }

    //working with categories
    public void insertListCategories(List<Category> categories) {
        open();
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_ID, category.getId());
            values.put(COLUMN_CATEGORY_TITLE, category.getTitle());
            values.put(COLUMN_CATEGORY_IMAGE_URL, category.getImageUrl());
            mDatabase.insert(TABLE_NAME_CATEGORIES, null, values);
        }
        close();
        QueryPreferences.setCategoryLoaded(mContext, true);
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_CATEGORIES;
        open();

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(0));
                category.setTitle(cursor.getString(1));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();

        return categories;
    }

    //working with programs
    public  void insertListPrograms(List<Program> programs) {
        open();
        for (int i = 0; i < programs.size(); i++) {
            Program program = programs.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_PROGRAM_TITLE, program.getTitle());
            values.put(COLUMN_PROGRAM_DATE, program.getDate());
            values.put(COLUMN_PROGRAM_TIME, program.getTime());
            values.put(COLUMN_PROGRAM_CHANNEL_ID, program.getChannel_id());
            mDatabase.insert(TABLE_NAME_PROGRAMS, null, values);

            Log.i("myLog", "COLUMN_PROGRAM_TITLE = " + program.getTitle());
            Log.i("myLog", "COLUMN_PROGRAM_CHANNEL_ID = " + program.getChannel_id());
            Log.i("myLog", "COLUMN_PROGRAM_DATE = " + program.getDate());

        }
        Log.i("myLog", "queryNumEntries.queryNumEntries() = " + DatabaseUtils.queryNumEntries(mDatabase, TABLE_NAME_PROGRAMS));
        close();
        QueryPreferences.setProgramLoaded(mContext, true);
    }

    public List<Program> getPrograms(int channelId, String forDate) {
        List<Program> programs = new ArrayList<>();
        Log.i("myLog", "getProgramschannelId = " + channelId + "forDate= " + forDate);

        String[] tableColumns = new String[] {COLUMN_PROGRAM_DATE, COLUMN_PROGRAM_TIME, COLUMN_PROGRAM_TITLE};
        String whereClause = COLUMN_PROGRAM_CHANNEL_ID + " = ? AND " + COLUMN_PROGRAM_DATE + " = ?";
        String[] whereArgs = new String[] {String.valueOf(channelId), forDate};
        open();
        Cursor cursor = mDatabase.query(TABLE_NAME_PROGRAMS, tableColumns, whereClause, whereArgs,null, null,null,null);
        Log.i("myLog", "cursor.g = " + cursor.getColumnCount());
        Log.i("myLog", "cursor.moveToFirst() = " + cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            int titleColIndex = cursor.getColumnIndex(COLUMN_PROGRAM_TITLE);
            int dateColIndex = cursor.getColumnIndex(COLUMN_PROGRAM_DATE);
            int timeColIndex = cursor.getColumnIndex(COLUMN_PROGRAM_TIME);
            Log.i("myLog", "cursor.COLUMN_PROGRAM_TIME() = " + timeColIndex);
            do {
                Program program = new Program();
                program.setTitle(cursor.getString(titleColIndex));
                program.setTime(cursor.getString(timeColIndex));
                program.setDate(cursor.getString(dateColIndex));
                programs.add(program);
                Log.i("myLog", "setTitle" + cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return programs;
    }
    public List<Program> getAllpr() {
        List<Program> programs = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_PROGRAMS;
        open();
        //Log.i("myLog", "queryNumEntries.queryNumEntries() = " + DatabaseUtils.queryNumEntries(mDatabase, TABLE_NAME_PROGRAMS));
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        Log.i("myLog", "cursor.moveToFirst() = " + cursor.moveToFirst());

        if (cursor.moveToFirst()) {
            int titleColIndex = cursor.getColumnIndex(COLUMN_PROGRAM_TITLE);
            int dateColIndex = cursor.getColumnIndex(COLUMN_PROGRAM_DATE);
            int timeColIndex = cursor.getColumnIndex(COLUMN_PROGRAM_TIME);
            do {
                Program program = new Program();
                program.setDate(cursor.getString(dateColIndex));
                program.setTitle(cursor.getString(titleColIndex));
                program.setTime(cursor.getString(timeColIndex));
                programs.add(program);
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();

        return programs;
    }


}
