package com.shtainyky.tvprogram.models.ui;

import android.database.Cursor;

import com.shtainyky.tvprogram.database.ContractClass;

public class CategoryUI {
    private int id;
    private String title;
    private String picture;

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

    public String getImage_url() {
        return picture;
    }

    public void setImage_url(String image_url) {
        this.picture = image_url;
    }

    public static CategoryUI getCategory(Cursor cursor) {
        CategoryUI category = new CategoryUI();
        category.setId(cursor.
                getInt(cursor.
                        getColumnIndex(ContractClass.Categories.COLUMN_CATEGORY_ID)));
        category.setTitle(cursor.
                getString(cursor.
                        getColumnIndex(ContractClass.Categories.COLUMN_CATEGORY_TITLE)));
        category.setImage_url(cursor.
                getString(cursor.
                        getColumnIndex(ContractClass.Categories.COLUMN_CATEGORY_IMAGE_URL)));
        return category;
    }
}
