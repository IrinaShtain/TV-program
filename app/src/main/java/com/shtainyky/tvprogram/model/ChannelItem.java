package com.shtainyky.tvprogram.model;

public class ChannelItem {
    private int id;
    private String name;
    private String picture;
    private int category_id;

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

    public String getPictureUrl() {
        return picture;
    }

    public void setPictureUrl(String picture_ulr) {
        this.picture = picture_ulr;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
