package com.shtainyky.tvprogram.model;

public class CategoryItem {
    private int _id;
    private String title;
    private String picture;

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
