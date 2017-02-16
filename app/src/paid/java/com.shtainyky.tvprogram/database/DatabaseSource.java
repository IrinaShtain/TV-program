package com.shtainyky.tvprogram.database;

import android.content.Context;

import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.utils.DatabaseSourceInterface;

import java.util.List;

public class DatabaseSource implements DatabaseSourceInterface {

    private Context mContext;

    public DatabaseSource(Context context) {
        mContext = context;
    }
    @Override
    public void deleteAllTables() {

    }

    @Override
    public List<ChannelItem> getAllChannel() {
        return null;
    }

    @Override
    public List<ChannelItem> getChannelsForCategory(Integer categoryId) {
        return null;
    }

    @Override
    public List<ChannelItem> getPreferredChannels() {
        return null;
    }

    @Override
    public String getCategoryNameForChannel(int id) {
        return null;
    }

    @Override
    public void insertListChannels(List<ChannelItem> channels) {

    }

    @Override
    public void setChannelPreferred(int channelPreferredId, int state) {

    }

    @Override
    public void insertListCategories(List<CategoryItem> categories) {

    }

    @Override
    public List<CategoryItem> getAllCategories() {
        return null;
    }

    @Override
    public void updateListPrograms(List<ProgramItem> programs, String whereDate) {

    }

    @Override
    public void insertListPrograms(List<ProgramItem> programs) {

    }

    @Override
    public List<ProgramItem> getPrograms(int channelId, String forDate) {
        return null;
    }
}
