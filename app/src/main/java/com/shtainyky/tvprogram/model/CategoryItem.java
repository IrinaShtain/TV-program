package com.shtainyky.tvprogram.model;

public class CategoryItem {
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

    public String getImageUrl() {
        return picture;
    }

    public void setImageUrl(String image_url) {
        this.picture = image_url;
    }
}
