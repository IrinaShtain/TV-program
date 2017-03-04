package com.shtainyky.tvprogram.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.shtainyky.tvprogram.db.DatabaseStoreInterface;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.CategoryItem_Table;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ChannelItem_Table;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.model.ProgramItem_Table;
import com.shtainyky.tvprogram.models.retrofit.Category;
import com.shtainyky.tvprogram.models.retrofit.Channel;
import com.shtainyky.tvprogram.models.retrofit.Program;
import com.shtainyky.tvprogram.utils.QueryPreferences;

import java.util.List;

public class DatabaseStoreImp implements DatabaseStoreInterface {

    private Context mContext;
    private CategoryItem mCategoryItem;
    private ChannelItem mChannelItem;
    private ProgramItem mProgramItem;

    public DatabaseStoreImp(Context context) {
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
    public String getCategoryNameForChannel(String id) {
        String titleCategory = "category";
        Cursor cursor = mContext.getContentResolver().query(
                mCategoryItem.getQueryUri(),
                null,
                CategoryItem_Table._id + " = ? ",
                new String[]{id},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            titleCategory = cursor.getString(1);
            cursor.close();
        }
        return titleCategory;
    }

    @Override
    public void insertListChannels(List<Channel> channels) {
        if (channels == null) return;
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            ContentValues values = new ContentValues();
            values.put(String.valueOf(ChannelItem_Table._id), channel.getId());
            values.put(String.valueOf(ChannelItem_Table.name), channel.getName().trim());
            values.put(String.valueOf(ChannelItem_Table.picture), channel.getPicture());
            values.put(String.valueOf(ChannelItem_Table.category_id), channel.getCategory_id());
            values.put(String.valueOf(ChannelItem_Table.category_title), getCategoryNameForChannel(String.valueOf(channel.getCategory_id())));
            values.put(String.valueOf(ChannelItem_Table.is_preferred), 0);
            mContext.getContentResolver().insert(mChannelItem.getInsertUri(), values);
        }
        QueryPreferences.setChannelsAreLoaded(mContext, true);
        Log.i("myLog", "insertListChannels****** =");
    }

    @Override
    public void setChannelPreferred(int channelPreferredId, int state) {
        ContentValues values = new ContentValues();
        values.put(String.valueOf(ChannelItem_Table.is_preferred), state);
        int k = mContext.getContentResolver().update(mChannelItem.getQueryUri(),
                values,
                ChannelItem_Table._id + " = ?",
                new String[]{"" + channelPreferredId});
        Log.i("myLog", "Channels.updat " + k);
    }

    //for working with categories
    @Override
    public void insertListCategories(final List<Category> categories) {
        if (categories == null) return;
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            ContentValues values = new ContentValues();
            values.put(String.valueOf(CategoryItem_Table._id), category.getId());
            values.put(String.valueOf(CategoryItem_Table.title), category.getTitle());
            values.put(String.valueOf(CategoryItem_Table.picture), category.getImage_url());
            mContext.getContentResolver().insert(mCategoryItem.getInsertUri(), values);
        }
        QueryPreferences.setCategoriesAreLoaded(mContext, true);
        Log.i("myLog", "  mCategoryItem.getInsertUri(), =" + mCategoryItem.getInsertUri());
    }


    //for working with programs
    @Override
    public void updateListPrograms(List<Program> programs, String whereDate) {
        mContext.getContentResolver().delete(mProgramItem.getDeleteUri(),
                ProgramItem_Table.date + " = ?",
                new String[]{whereDate});
        insertListPrograms(programs);
    }

    @Override
    public void insertListPrograms(List<Program> programs) {
        for (int i = 0; i < programs.size(); i++) {
            Program program = programs.get(i);
            ContentValues values = new ContentValues();
            values.put(String.valueOf(ProgramItem_Table.title), program.getTitle());
            values.put(String.valueOf(ProgramItem_Table.date), program.getDate());
            values.put(String.valueOf(ProgramItem_Table.time), program.getTime());
            values.put(String.valueOf(ProgramItem_Table.channel_id), program.getChannelID());
            mContext.getContentResolver().insert(mProgramItem.getInsertUri(), values);
        }
        Log.i("myLog", "size =" + programs.size());
        QueryPreferences.setProgramsAreLoaded(mContext, true);
    }

}
