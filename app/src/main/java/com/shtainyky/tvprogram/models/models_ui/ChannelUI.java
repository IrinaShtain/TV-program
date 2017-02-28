package com.shtainyky.tvprogram.models.models_ui;

import android.database.Cursor;
import android.util.Log;

import com.shtainyky.tvprogram.model.ChannelItem;

import java.util.ArrayList;
import java.util.List;

import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_CATEGORY_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_ID;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_IMAGE_URL;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_IS_PREFERRED;
import static com.shtainyky.tvprogram.database.ContractClass.Channels.COLUMN_CHANNEL_TITLE;

public class ChannelUI {
    private int id;
    private String name;
    private String picture;

    private String category_name;
    private int is_preferred;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getId() {
        return id;
    }

    public int getIs_preferred() {
        return is_preferred;
    }

    public void setIs_preferred(int is_preferred) {
        this.is_preferred = is_preferred;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture()
    {
        return picture;
    }

    public void setPictureUrl(String picture_ulr) {
        this.picture = picture_ulr;
    }

    public static List<ChannelUI> getListOfChannelsForUI(Cursor cursor)
    {
        List<ChannelUI> channels = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ChannelUI channel = new ChannelUI();
                    channel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_TITLE)));
                    channel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_ID)));
                    channel.setPictureUrl(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_IMAGE_URL)));
                    channel.setIs_preferred(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_IS_PREFERRED)));
                   // String categoryName = getCategoryNameForChannel(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_CATEGORY_ID)));
                    channel.setCategory_name("categoryName");
                    channels.add(channel);
                } while (cursor.moveToNext());
            }
            cursor.close();
            Log.d("myLog", "getPreferredChannels");
        }
        return channels;
    }

}
