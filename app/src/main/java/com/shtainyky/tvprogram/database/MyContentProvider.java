package com.shtainyky.tvprogram.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;

import static com.shtainyky.tvprogram.database.ContractClass.AUTHORITY;
import static com.shtainyky.tvprogram.database.ContractClass.Categories.PATH_CATEGORIES;
import static com.shtainyky.tvprogram.database.ContractClass.Categories.PATH_CATEGORIES_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.PATH_CHANNELS;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.PATH_CHANNELS_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.PATH_PROGRAMS;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.PATH_PROGRAMS_ID;

public class MyContentProvider extends ContentProvider {
    DatabaseHelper mDBHelper;
    private static HashMap mProgramsProjectionMap;
    private static HashMap mChannelsProjectionMap;
    private static HashMap mCategoriesProjectionMap;

    static {
        mProgramsProjectionMap = new HashMap();
        for (int i = 0; i < ContractClass.Programs.DEFAULT_PROJECTION.length; i++) {
            mProgramsProjectionMap.put(
                    ContractClass.Programs.DEFAULT_PROJECTION[i],
                    ContractClass.Programs.DEFAULT_PROJECTION[i]);
        }

        mChannelsProjectionMap = new HashMap();
        for (int i = 0; i < ContractClass.Channels.DEFAULT_PROJECTION.length; i++) {
            mChannelsProjectionMap.put(
                    ContractClass.Channels.DEFAULT_PROJECTION[i],
                    ContractClass.Channels.DEFAULT_PROJECTION[i]);
        }

        mCategoriesProjectionMap = new HashMap();
        for (int i = 0; i < ContractClass.Categories.DEFAULT_PROJECTION.length; i++) {
            mCategoriesProjectionMap.put(
                    ContractClass.Categories.DEFAULT_PROJECTION[i],
                    ContractClass.Categories.DEFAULT_PROJECTION[i]);
        }
    }


    private static final int PROGRAMS = 1001;
    private static final int PROGRAMS_ID = 1002;
    private static final int CHANNELS = 2001;
    private static final int CHANNELS_ID = 2002;
    private static final int CATEGORIES = 3001;
    private static final int CATEGORIES_ID = 3002;

    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, PATH_PROGRAMS, PROGRAMS);
        mUriMatcher.addURI(AUTHORITY, PATH_PROGRAMS_ID + "#", PROGRAMS_ID);
        mUriMatcher.addURI(AUTHORITY, PATH_CHANNELS, CHANNELS);
        mUriMatcher.addURI(AUTHORITY, PATH_CHANNELS_ID + "#", CHANNELS_ID);
        mUriMatcher.addURI(AUTHORITY, PATH_CATEGORIES, CATEGORIES);
        mUriMatcher.addURI(AUTHORITY, PATH_CATEGORIES_ID + "#", CATEGORIES_ID);
        Log.d("myLog", "mUriMatcher, result Uri : " + mUriMatcher.toString());
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(@NonNull Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String finalWhere;
        int count;
        switch (mUriMatcher.match(uri)) {
            case PROGRAMS:
                count = db.delete(ContractClass.Programs.TABLE_NAME_PROGRAMS, where, whereArgs);
                break;
            case PROGRAMS_ID:
                finalWhere = ContractClass.Programs.COLUMN_PROGRAM_ID + " = " + uri.getPathSegments().get(ContractClass.Programs.PROGRAMS_ID_PATH_POSITION);
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.delete(ContractClass.Programs.TABLE_NAME_PROGRAMS, finalWhere, whereArgs);
                break;
            case CHANNELS:
                count = db.delete(ContractClass.Channels.TABLE_NAME_CHANNELS, where, whereArgs);
                break;
            case CHANNELS_ID:
                finalWhere = ContractClass.Channels.COLUMN_CHANNEL_ID + " = " + uri.getPathSegments().get(ContractClass.Channels.CHANNELS_ID_PATH_POSITION);
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.delete(ContractClass.Channels.TABLE_NAME_CHANNELS, finalWhere, whereArgs);
                break;
            case CATEGORIES:
                count = db.delete(ContractClass.Categories.TABLE_NAME_CATEGORIES, where, whereArgs);
                break;
            case CATEGORIES_ID:
                finalWhere = ContractClass.Categories.COLUMN_CATEGORY_ID + " = " + uri.getPathSegments().get(ContractClass.Categories.CATEGORIES_ID_PATH_POSITION);
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.delete(ContractClass.Categories.TABLE_NAME_CATEGORIES, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("deleteUnknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) {

        switch (mUriMatcher.match(uri)) {
            case PROGRAMS:
                return ContractClass.Programs.CONTENT_TYPE;
            case PROGRAMS_ID:
                return ContractClass.Programs.CONTENT_ITEM_TYPE;
            case CHANNELS:
                return ContractClass.Channels.CONTENT_TYPE;
            case CHANNELS_ID:
                return ContractClass.Channels.CONTENT_ITEM_TYPE;
            case CATEGORIES:
                return ContractClass.Categories.CONTENT_TYPE;
            case CATEGORIES_ID:
                return ContractClass.Categories.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("getTypeUnknown URI " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        if (mUriMatcher.match(uri) != PROGRAMS &&
                mUriMatcher.match(uri) != CATEGORIES
                && mUriMatcher.match(uri) != CHANNELS) {

            throw new IllegalArgumentException("insertUnknown URI " + uri);
        }
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues mValues;
        if (values != null) {
            mValues = new ContentValues(values);
        } else {
            mValues = new ContentValues();
        }
        long rowId;
        Uri rowUri = Uri.EMPTY;
        switch (mUriMatcher.match(uri)) {
            case PROGRAMS:
                rowId = db.insert(ContractClass.Programs.TABLE_NAME_PROGRAMS,
                        ContractClass.Programs.COLUMN_PROGRAM_ID,
                        mValues);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(ContractClass.Programs.CONTENT_ID_URI_BASE, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
            case CHANNELS:
                rowId = db.insert(ContractClass.Channels.TABLE_NAME_CHANNELS,
                        ContractClass.Channels.COLUMN_CHANNEL_ID,
                        mValues);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(ContractClass.Channels.CONTENT_ID_URI_BASE, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
            case CATEGORIES:
                rowId = db.insert(ContractClass.Categories.TABLE_NAME_CATEGORIES,
                        ContractClass.Categories.COLUMN_CATEGORY_ID,
                        mValues);
                if (rowId > 0) {
                    rowUri = ContentUris.withAppendedId(ContractClass.Categories.CONTENT_ID_URI_BASE, rowId);
                    getContext().getContentResolver().notifyChange(rowUri, null);
                }
                break;
        }
        return rowUri;
    }


    @Override
    public boolean onCreate() {
        mDBHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String orderBy = null;
        switch (mUriMatcher.match(uri)) {
            case PROGRAMS:
                qb.setTables(ContractClass.Programs.TABLE_NAME_PROGRAMS);
                qb.setProjectionMap(mProgramsProjectionMap);
                break;
            case PROGRAMS_ID:
                qb.setTables(ContractClass.Programs.TABLE_NAME_PROGRAMS);
                qb.setProjectionMap(mProgramsProjectionMap);
                qb.appendWhere(ContractClass.Programs.COLUMN_PROGRAM_ID + "=" + uri.getPathSegments().get(ContractClass.Programs.PROGRAMS_ID_PATH_POSITION));
                break;
            case CHANNELS:
                qb.setTables(ContractClass.Channels.TABLE_NAME_CHANNELS);
                qb.setProjectionMap(mChannelsProjectionMap);
                orderBy = ContractClass.Channels.COLUMN_CHANNEL_TITLE + " ASC ";
                break;
            case CHANNELS_ID:
                qb.setTables(ContractClass.Channels.TABLE_NAME_CHANNELS);
                qb.setProjectionMap(mChannelsProjectionMap);
                qb.appendWhere(ContractClass.Channels.COLUMN_CHANNEL_ID + "=" + uri.getPathSegments().get(ContractClass.Channels.CHANNELS_ID_PATH_POSITION));
                break;
            case CATEGORIES:
                qb.setTables(ContractClass.Categories.TABLE_NAME_CATEGORIES);
                qb.setProjectionMap(mCategoriesProjectionMap);
                break;
            case CATEGORIES_ID:
                qb.setTables(ContractClass.Categories.TABLE_NAME_CATEGORIES);
                qb.setProjectionMap(mCategoriesProjectionMap);
                qb.appendWhere(ContractClass.Categories.COLUMN_CATEGORY_ID + "=" + uri.getPathSegments().get(ContractClass.Categories.CATEGORIES_ID_PATH_POSITION));
                break;
            default:
                throw new IllegalArgumentException("queryUnknown URI " + uri);
        }
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String where,
                      String[] whereArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int count;
        String finalWhere;
        String id;
        switch (mUriMatcher.match(uri)) {
            case PROGRAMS:
                count = db.update(ContractClass.Programs.TABLE_NAME_PROGRAMS, values, where, whereArgs);
                break;
            case PROGRAMS_ID:
                id = uri.getPathSegments().get(ContractClass.Programs.PROGRAMS_ID_PATH_POSITION);
                finalWhere = ContractClass.Programs._ID + " = " + id;
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update(ContractClass.Programs.TABLE_NAME_PROGRAMS, values, finalWhere, whereArgs);
                break;
            case CHANNELS:
                count = db.update(ContractClass.Channels.TABLE_NAME_CHANNELS, values, where, whereArgs);
                break;
            case CHANNELS_ID:
                id = uri.getPathSegments().get(ContractClass.Channels.CHANNELS_ID_PATH_POSITION);
                finalWhere = ContractClass.Channels.COLUMN_CHANNEL_ID + " = " + id;
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update(ContractClass.Channels.TABLE_NAME_CHANNELS, values, finalWhere, whereArgs);
                break;
            case CATEGORIES:
                count = db.update(ContractClass.Categories.TABLE_NAME_CATEGORIES, values, where, whereArgs);
                break;
            case CATEGORIES_ID:
                id = uri.getPathSegments().get(ContractClass.Categories.CATEGORIES_ID_PATH_POSITION);
                finalWhere = ContractClass.Categories.COLUMN_CATEGORY_ID + " = " + id;
                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }
                count = db.update(ContractClass.Categories.TABLE_NAME_CATEGORIES, values, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("updateUnknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
