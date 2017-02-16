package com.shtainyky.tvprogram.utils;

import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;

import java.util.List;

public interface DatabaseSourceInterface {

    void deleteAllTables();

    //for working with channels
    List<ChannelItem> getAllChannel();

    List<ChannelItem> getChannelsForCategory(Integer categoryId);

    List<ChannelItem> getPreferredChannels();

    String getCategoryNameForChannel(int id);

    void insertListChannels(List<ChannelItem> channels);

    void setChannelPreferred(int channelPreferredId, int state);

    //for working with categories
    void insertListCategories(List<CategoryItem> categories);

    List<CategoryItem> getAllCategories();

    //for working with programs
    void updateListPrograms(List<ProgramItem> programs, String whereDate);

    void insertListPrograms(List<ProgramItem> programs);

    List<ProgramItem> getPrograms(int channelId, String forDate);
}