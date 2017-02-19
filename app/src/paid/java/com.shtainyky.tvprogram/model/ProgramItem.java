package com.shtainyky.tvprogram.model;

import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.provider.BaseProviderModel;
import com.shtainyky.tvprogram.database.MyDatabase;

@Table(database = MyDatabase.class)
public class ProgramItem extends BaseProviderModel<ProgramItem> {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String title;

    @Column
    public String date;

    @Column
    public String time;

    @Column
    public int channel_id;


    @Override
    public Uri getDeleteUri() {
        return MyDatabase.ProgramItem.CONTENT_URI;
    }

    @Override
    public Uri getInsertUri() {
        return MyDatabase.ProgramItem.CONTENT_URI;
    }

    @Override
    public Uri getUpdateUri() {
        return MyDatabase.ProgramItem.CONTENT_URI;
    }

    @Override
    public Uri getQueryUri() {
        return MyDatabase.ProgramItem.CONTENT_URI;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getChannel() {
        return channel_id;
    }

    public void setChannel(int channel_id) {
        this.channel_id = channel_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
