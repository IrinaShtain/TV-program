package com.shtainyky.tvprogram.model;

import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.provider.BaseProviderModel;
import com.shtainyky.tvprogram.database.MyDatabase;

@Table(database = MyDatabase.class)
public class ChannelItem extends BaseProviderModel<ChannelItem> {

    @Column
    @PrimaryKey
    public int _id;

    @Column
    public String name;

    @Column
    public String picture;

    @Column
    public int is_preferred;

    @Column
    String category_name;

    @Column
    public int category_id;


    @Override
    public Uri getDeleteUri() {
        return  MyDatabase.ChannelItem.CONTENT_URI;
    }

    @Override
    public Uri getInsertUri() {
        return  MyDatabase.ChannelItem.CONTENT_URI;
    }

    @Override
    public Uri getUpdateUri() {
        return  MyDatabase.ChannelItem.CONTENT_URI;
    }

    @Override
    public Uri getQueryUri() {
        return  MyDatabase.ChannelItem.CONTENT_URI;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getIs_preferred() {
        return is_preferred;
    }

    public void setIs_preferred(int is_preferred) {
        this.is_preferred = is_preferred;
    }

    public int getCategory() {
        return category_id;
    }

    public void setCategory(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
