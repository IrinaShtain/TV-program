package com.shtainyky.tvprogram.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.shtainyky.tvprogram.db.DatabaseSourceInterface;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.CategoryItem_Table;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ChannelItem_Table;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.model.ProgramItem_Table;

import com.shtainyky.tvprogram.utils.QueryPreferences;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSource implements DatabaseSourceInterface {

    private Context mContext;
    private CategoryItem mCategoryItem;
    private ChannelItem mChannelItem;
    private ProgramItem mProgramItem;

    public DatabaseSource(Context context) {
        mContext = context;
        mCategoryItem = new CategoryItem();
        mChannelItem = new ChannelItem();
        mProgramItem = new ProgramItem();
    }

    @Override
    public void deleteAllTables() {
        mContext.getContentResolver().delete(mCategoryItem.getDeleteUri(), null, null);
        mContext.getContentResolver().delete(mChannelItem.getDeleteUri(), null, null);
        mContext.getContentResolver().delete(mProgramItem.getDeleteUri(), null, null);
    }
    //for working with channels
    @Override
    public List<ChannelItem> getAllChannel() {
        List<ChannelItem> channels = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(
                mChannelItem.getQueryUri(),
                null,
                null,
                null,
                ChannelItem_Table.name + " ASC ");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ChannelItem channel = new ChannelItem();
                    channel.setName(cursor.getString(1));
                    channel.setPicture(cursor.getString(2));
                    channel.setId(cursor.getInt(0));
                    channel.setIs_preferred(cursor.getInt(3));
                    String categoryName = getCategoryNameForChannel(cursor.getInt(5));
                    channel.setCategory_name(categoryName);
                    channels.add(channel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        Log.d("myLog", "getAllChannel" + channels.get(0).getName());
        return channels;
    }

    @Override
    public List<ChannelItem> getChannelsForCategory(Integer categoryId) {
        List<ChannelItem> channels = new ArrayList<>();
        Log.d("myLog", "getChannelsForCategory" + categoryId);
        Cursor cursor = mContext.getContentResolver().query(
                mChannelItem.getQueryUri(),
                null,
                ChannelItem_Table.category_id + "=?",
                new String[]{String.valueOf(categoryId)},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ChannelItem channel = new ChannelItem();
                    channel.setName(cursor.getString(1));
                    channel.setPicture(cursor.getString(2));
                    channel.setId(cursor.getInt(0));
                    channel.setIs_preferred(cursor.getInt(3));
                    String categoryName = getCategoryNameForChannel(cursor.getInt(5));
                    channel.setCategory_name(categoryName);
                    channels.add(channel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        Log.d("myLog", "getChannelsForCategory");
        return channels;
    }

    @Override
    public List<ChannelItem> getPreferredChannels() {
        List<ChannelItem> channels = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(
                mChannelItem.getQueryUri(),
                null,
                ChannelItem_Table.is_preferred + "=?",
                new String[]{"" + 1},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ChannelItem channel = new ChannelItem();
                    channel.setName(cursor.getString(1));
                    channel.setPicture(cursor.getString(2));
                    channel.setId(cursor.getInt(0));
                    channel.setIs_preferred(cursor.getInt(3));
                    String categoryName = getCategoryNameForChannel(cursor.getInt(5));
                    channel.setCategory_name(categoryName);
                    channels.add(channel);
                } while (cursor.moveToNext());
            }
            cursor.close();
            Log.d("myLog", "getPreferredChannels" + channels.size());
        }
        return channels;
    }

    @Override
    public String getCategoryNameForChannel(int id) {
        String titleCategory = "category";
        Cursor cursor = mContext.getContentResolver().query(
                mCategoryItem.getQueryUri(),
                null,
                CategoryItem_Table.id + " = ? ",
                new String[]{"" + id},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            titleCategory = cursor.getString(1);
            cursor.close();
        }
        return titleCategory;
    }

    @Override
    public void insertListChannels(List<ChannelItem> channels) {
       if (channels == null) return;
        for (int i = 0; i < channels.size(); i++) {
            ChannelItem channel = channels.get(i);
            ContentValues values = new ContentValues();
            values.put(String.valueOf(ChannelItem_Table.id), channel.getId());
            values.put(String.valueOf(ChannelItem_Table.name), channel.getName().trim());
            values.put(String.valueOf(ChannelItem_Table.picture), channel.getPicture());
            values.put(String.valueOf(ChannelItem_Table.category_id), channel.getCategory());
            values.put(String.valueOf(ChannelItem_Table.is_preferred), 0);
            mContext.getContentResolver().insert(mChannelItem.getInsertUri(), values);
        }
        QueryPreferences.setChannelLoaded(mContext, true);
        Log.i("myLog", "is_preferred =" + channels.get(0).is_preferred);
    }

    @Override
    public void setChannelPreferred(int channelPreferredId, int state) {
        ContentValues values = new ContentValues();
        values.put(String.valueOf(ChannelItem_Table.is_preferred), state);
        int k = mContext.getContentResolver().update(mChannelItem.getQueryUri(),
                values,
                ChannelItem_Table.id+ " = ?",
                new String[]{"" + channelPreferredId});
        Log.i("myLog", "Channels.updat " + k);
    }
//for working with categories
    @Override
    public void insertListCategories(final List<CategoryItem> categories) {
        if (categories == null) return;
        for (int i = 0; i < categories.size(); i++) {
            CategoryItem category = categories.get(i);
            ContentValues values = new ContentValues();
            values.put(String.valueOf(CategoryItem_Table.id), category.getId());
            values.put(String.valueOf(CategoryItem_Table.title), category.getTitle());
            values.put(String.valueOf(CategoryItem_Table.picture), category.getImage_url());
            mContext.getContentResolver().insert(mCategoryItem.getInsertUri(), values);
        }
        QueryPreferences.setCategoryLoaded(mContext, true);
    }

    @Override
    public List<CategoryItem> getAllCategories() {
        return SQLite.select().
                from(CategoryItem.class).
                queryList();
    }
//for working with programs
    @Override
    public void updateListPrograms(List<ProgramItem> programs, String whereDate) {
        mContext.getContentResolver().delete(mProgramItem.getDeleteUri(),
                ProgramItem_Table.date + " = ?",
                new String[]{whereDate});
        insertListPrograms(programs);
    }

    @Override
    public void insertListPrograms( List<ProgramItem> programs) {
        for (int i = 0; i < programs.size(); i++) {
            ProgramItem program = programs.get(i);
            ContentValues values = new ContentValues();
            values.put(String.valueOf(ProgramItem_Table.title), program.getTitle());
            values.put(String.valueOf(ProgramItem_Table.date), program.getDate());
            values.put(String.valueOf(ProgramItem_Table.time), program.getTime());
            values.put(String.valueOf(ProgramItem_Table.channel_id), program.getChannel());
            mContext.getContentResolver().insert(mProgramItem.getInsertUri(), values);
        }
        Log.i("myLog", "size =" + programs.size());
        QueryPreferences.setProgramLoaded(mContext, true);
    }

    @Override
    public List<ProgramItem> getPrograms(int channelId, String forDate) {

        List<ProgramItem> programs = new ArrayList<>();
        String[] whereArgs = new String[]{String.valueOf(channelId), forDate};
        Cursor cursor = mContext.getContentResolver().query(mProgramItem.getQueryUri(),
                null,
                ProgramItem_Table.channel_id + " = ? AND " + ProgramItem_Table.date + " = ?",
                whereArgs,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ProgramItem program = new ProgramItem();
                    program.setTitle(cursor.getString(1));
                    program.setTime(cursor.getString(3));
                    program.setDate(cursor.getString(2));
                    programs.add(program);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return programs;
    }

}
