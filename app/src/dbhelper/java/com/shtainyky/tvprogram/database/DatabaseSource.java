package com.shtainyky.tvprogram.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.db.DatabaseSourceInterface;
import com.shtainyky.tvprogram.utils.QueryPreferences;

import java.util.ArrayList;
import java.util.List;

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
import static com.shtainyky.tvprogram.database.ContractClass.Programs.COLUMN_PROGRAM_TIME;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.COLUMN_PROGRAM_TITLE;

public class DatabaseSource implements DatabaseSourceInterface {

    private Context mContext;

    public DatabaseSource(Context context) {
        mContext = context;
    }

    @Override
    public void deleteAllTables() {
        mContext.getContentResolver().delete(ContractClass.Channels.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(ContractClass.Categories.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(ContractClass.Programs.CONTENT_URI, null, null);

        Toast.makeText(mContext, R.string.delete_data_db, Toast.LENGTH_SHORT).show();
    }


    //working with channels
    @Override
    public List<ChannelItem> getAllChannel() {
        List<ChannelItem> channels = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(
                ContractClass.Channels.CONTENT_URI,
                ContractClass.Channels.DEFAULT_PROJECTION,
                null,
                null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ChannelItem channel = new ChannelItem();
                    channel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_TITLE)));
                    channel.setPictureUrl(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_IMAGE_URL)));
                    channel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_ID)));
                    channel.setIs_preferred(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_IS_PREFERRED)));
                    String categoryName = getCategoryNameForChannel(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_CATEGORY_ID)));
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
                ContractClass.Channels.CONTENT_URI,
                ContractClass.Channels.DEFAULT_PROJECTION,
                COLUMN_CHANNEL_CATEGORY_ID + "=?",
                new String[]{String.valueOf(categoryId)},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ChannelItem channel = new ChannelItem();
                    channel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_TITLE)));
                    channel.setPictureUrl(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_IMAGE_URL)));
                    channel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_ID)));
                    channel.setIs_preferred(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_IS_PREFERRED)));
                    String categoryName = getCategoryNameForChannel(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_CATEGORY_ID)));
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
                ContractClass.Channels.CONTENT_URI,
                ContractClass.Channels.DEFAULT_PROJECTION,
                COLUMN_CHANNEL_IS_PREFERRED + "=?",
                new String[]{"" + 1},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ChannelItem channel = new ChannelItem();
                    channel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_TITLE)));
                    channel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_ID)));
                    channel.setPictureUrl(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_IMAGE_URL)));
                    channel.setIs_preferred(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_IS_PREFERRED)));
                    String categoryName = getCategoryNameForChannel(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_CATEGORY_ID)));
                    channel.setCategory_name(categoryName);
                    channels.add(channel);
                } while (cursor.moveToNext());
            }
            cursor.close();
            Log.d("myLog", "getPreferredChannels");
        }
        return channels;
    }

    @Override
    public String getCategoryNameForChannel(int id) {
        String titleCategory = "category";
        Cursor cursor = mContext.getContentResolver().query(
                ContractClass.Categories.CONTENT_URI,
                ContractClass.Categories.DEFAULT_PROJECTION,
                COLUMN_CATEGORY_ID + " = ? ",
                new String[]{"" + id},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            titleCategory = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_TITLE));
            cursor.close();
        }
        return titleCategory;
    }

    @Override
    public void insertListChannels(List<ChannelItem> channels) {

        for (int i = 0; i < channels.size(); i++) {
            ChannelItem channel = channels.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_CHANNEL_ID, channel.getId());
            values.put(COLUMN_CHANNEL_TITLE, channel.getName().trim());
            values.put(COLUMN_CHANNEL_IMAGE_URL, channel.getPicture());
            values.put(COLUMN_CHANNEL_CATEGORY_ID, channel.getCategory_id());
            values.put(COLUMN_CHANNEL_IS_PREFERRED, 0);
            mContext.getContentResolver().insert(ContractClass.Channels.CONTENT_URI, values);
        }
        QueryPreferences.setChannelsAreLoaded(mContext, true);
    }

    @Override
    public void setChannelPreferred(int channelPreferredId, int state) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHANNEL_IS_PREFERRED, state);
        int k = mContext.getContentResolver().update(ContractClass.Channels.CONTENT_URI,
                values,
                COLUMN_CHANNEL_ID + " = ?",
                new String[]{"" + channelPreferredId});
        Log.i("myLog", "Channels.updat " + k);
    }

    //working with categories
    @Override
    public void insertListCategories(List<CategoryItem> categories) {

        for (int i = 0; i < categories.size(); i++) {
            CategoryItem category = categories.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_ID, category.getId());
            values.put(COLUMN_CATEGORY_TITLE, category.getTitle());
            values.put(COLUMN_CATEGORY_IMAGE_URL, category.getImage_url());
            mContext.getContentResolver().insert(ContractClass.Categories.CONTENT_URI, values);
        }
        QueryPreferences.setCategoriesAreLoaded(mContext, true);
    }

    @Override
    public List<CategoryItem> getAllCategories() {
        List<CategoryItem> categories = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(
                ContractClass.Categories.CONTENT_URI,
                ContractClass.Categories.DEFAULT_PROJECTION,
                null,
                null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    CategoryItem category = new CategoryItem();
                    category.setId(cursor.getInt(cursor.getColumnIndex(ContractClass.Categories.COLUMN_CATEGORY_ID)));
                    category.setTitle(cursor.getString(cursor.getColumnIndex(ContractClass.Categories.COLUMN_CATEGORY_TITLE)));
                    category.setImage_url(cursor.getString(cursor.getColumnIndex(ContractClass.Categories.COLUMN_CATEGORY_IMAGE_URL)));
                    categories.add(category);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        Log.i("myLog", "getAllCategories =" + categories.get(0).getTitle());
        Log.i("myLog", "  ContractClass.Categories.CONTENT_URI, =" +   ContractClass.Categories.CONTENT_URI);
        return categories;
    }

    //working with programs
    @Override
    public void updateListPrograms(List<ProgramItem> programs, String whereDate) {
        Log.i("myLog", "update =" + programs.get(0).getDate());

        int k = mContext.getContentResolver().delete(ContractClass.Programs.CONTENT_URI,
                COLUMN_PROGRAM_DATE + " = ?",
                new String[]{whereDate});
        insertListPrograms(programs);
        Log.i("myLog", "delete =" + k);
    }

    @Override
    public void insertListPrograms(List<ProgramItem> programs) {
        for (int i = 0; i < programs.size(); i++) {
            ProgramItem program = programs.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_PROGRAM_TITLE, program.getTitle());
            values.put(COLUMN_PROGRAM_DATE, program.getDate());
            values.put(COLUMN_PROGRAM_TIME, program.getTime());
            values.put(COLUMN_PROGRAM_CHANNEL_ID, program.getId());
            mContext.getContentResolver().insert(ContractClass.Programs.CONTENT_URI, values);
        }
        Log.i("myLog", "size =" + programs.size());
        QueryPreferences.setProgramsAreLoaded(mContext, true);
    }

    @Override
    public List<ProgramItem> getPrograms(int channelId, String forDate) {
        List<ProgramItem> programs = new ArrayList<>();
        String[] whereArgs = new String[]{String.valueOf(channelId), forDate};
        Cursor cursor = mContext.getContentResolver().query(ContractClass.Programs.CONTENT_URI,
                ContractClass.Programs.DEFAULT_PROJECTION,
                COLUMN_PROGRAM_CHANNEL_ID + " = ? AND " + COLUMN_PROGRAM_DATE + " = ?",
                whereArgs,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int titleColIndex = cursor.getColumnIndex(COLUMN_PROGRAM_TITLE);
                int dateColIndex = cursor.getColumnIndex(COLUMN_PROGRAM_DATE);
                int timeColIndex = cursor.getColumnIndex(COLUMN_PROGRAM_TIME);
                do {
                    ProgramItem program = new ProgramItem();
                    program.setTitle(cursor.getString(titleColIndex));
                    program.setTime(cursor.getString(timeColIndex));
                    program.setDate(cursor.getString(dateColIndex));
                    programs.add(program);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return programs;
    }
}
