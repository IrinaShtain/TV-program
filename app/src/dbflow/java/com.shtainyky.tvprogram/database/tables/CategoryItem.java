package com.shtainyky.tvprogram.database.tables;

import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.provider.BaseProviderModel;
import com.shtainyky.tvprogram.database.MyDatabase;


@Table(database = MyDatabase.class)
public class CategoryItem extends BaseProviderModel<CategoryItem> {
    @Column
    @PrimaryKey
    public int _id;

    @Column
    public String title;

    @Column
    public String picture;


    @Override
    public Uri getDeleteUri() {
        return MyDatabase.CategoryItem.CONTENT_URI;
    }

    @Override
    public Uri getInsertUri() {
        return MyDatabase.CategoryItem.CONTENT_URI;
    }

    @Override
    public Uri getUpdateUri() {
        return MyDatabase.CategoryItem.CONTENT_URI;
    }

    @Override
    public Uri getQueryUri() {
        return MyDatabase.CategoryItem.CONTENT_URI;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return picture;
    }

    public void setImage_url(String image_url) {
        this.picture = image_url;
    }
}
