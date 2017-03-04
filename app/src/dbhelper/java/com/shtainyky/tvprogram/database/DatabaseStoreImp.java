package com.shtainyky.tvprogram.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.db.DatabaseStoreInterface;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.models.models_retrofit.Category;
import com.shtainyky.tvprogram.models.models_retrofit.Channel;
import com.shtainyky.tvprogram.models.models_retrofit.Program;
import com.shtainyky.tvprogram.utils.QueryPreferences;

import java.util.ArrayList;
import java.util.List;

import static com.shtainyky.tvprogram.database.ContractClass.Categories.COLUMN_CATEGORY_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Categories.COLUMN_CATEGORY_IMAGE_URL;
import static com.shtainyky.tvprogram.database.ContractClass.Categories.COLUMN_CATEGORY_TITLE;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_CATEGORY_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_CATEGORY_TITLE;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_IMAGE_URL;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_IS_PREFERRED;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_TITLE;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.COLUMN_PROGRAM_CHANNEL_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.COLUMN_PROGRAM_DATE;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.COLUMN_PROGRAM_TIME;
import static com.shtainyky.tvprogram.database.ContractClass.Programs.COLUMN_PROGRAM_TITLE;

public class DatabaseStoreImp implements DatabaseStoreInterface {

    private Context mContext;

    public DatabaseStoreImp(Context context) {
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
    public String getCategoryNameForChannel(String id) {
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
    public void insertListChannels(List<Channel> channels) {
        for (int i = 0; i < channels.size(); i++) {
            Channel channel = channels.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_CHANNEL_ID, channel.getId());
            values.put(COLUMN_CHANNEL_TITLE, channel.getName().trim());
            values.put(COLUMN_CHANNEL_IMAGE_URL, channel.getPicture());
            values.put(COLUMN_CHANNEL_CATEGORY_ID, channel.getCategory_id());
            values.put(COLUMN_CHANNEL_CATEGORY_TITLE, getCategoryNameForChannel(String.valueOf(channel.getCategory_id())));
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
    public void insertListCategories(List<Category> categories) {

        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_ID, category.getId());
            values.put(COLUMN_CATEGORY_TITLE, category.getTitle());
            values.put(COLUMN_CATEGORY_IMAGE_URL, category.getImage_url());
            mContext.getContentResolver().insert(ContractClass.Categories.CONTENT_URI, values);
        }
        QueryPreferences.setCategoriesAreLoaded(mContext, true);
    }



    //working with programs
    @Override
    public void updateListPrograms(List<Program> programs, String whereDate) {
        Log.i("myLog", "update =" + programs.get(0).getDate());

        int k = mContext.getContentResolver().delete(ContractClass.Programs.CONTENT_URI,
                COLUMN_PROGRAM_DATE + " = ?",
                new String[]{whereDate});
        insertListPrograms(programs);
        Log.i("myLog", "delete =" + k);
    }

    @Override
    public void insertListPrograms(List<Program> programs) {
        for (int i = 0; i < programs.size(); i++) {
            Program program = programs.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_PROGRAM_TITLE, program.getTitle());
            values.put(COLUMN_PROGRAM_DATE, program.getDate());
            values.put(COLUMN_PROGRAM_TIME, program.getTime());
            values.put(COLUMN_PROGRAM_CHANNEL_ID, program.getChannelID());
            mContext.getContentResolver().insert(ContractClass.Programs.CONTENT_URI, values);
        }
        Log.i("myLog", "size =" + programs.size());
        QueryPreferences.setProgramsAreLoaded(mContext, true);
    }


}
