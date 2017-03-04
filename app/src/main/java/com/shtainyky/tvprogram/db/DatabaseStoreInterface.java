package com.shtainyky.tvprogram.db;

import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.shtainyky.tvprogram.models.models_retrofit.Category;
import com.shtainyky.tvprogram.models.models_retrofit.Channel;
import com.shtainyky.tvprogram.models.models_retrofit.Program;

import java.util.List;

public interface DatabaseStoreInterface {

    void deleteAllTables();

    //for working with channels

    String getCategoryNameForChannel(String id);

    void insertListChannels(List<Channel> channels);//

    void setChannelPreferred(int channelPreferredId, int state);

    //for working with categories
    void insertListCategories(List<Category> categories);//


    //for working with programs
    void updateListPrograms(List<Program> programs, String whereDate);

    void insertListPrograms(List<Program> programs);//


}