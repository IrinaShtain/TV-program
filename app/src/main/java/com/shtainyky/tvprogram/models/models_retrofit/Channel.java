package com.shtainyky.tvprogram.models.models_retrofit;

public class Channel {
    private int id;
    private String name;
    private String picture;
    private int category_id;


    public int getId() {
        return id;
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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
