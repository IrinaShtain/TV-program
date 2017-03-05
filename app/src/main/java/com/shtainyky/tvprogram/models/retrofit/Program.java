package com.shtainyky.tvprogram.models.retrofit;

public class Program {
    private int channel_id;
    private String date;
    private String time;
    private String title;

    public int getChannelID() {
        return channel_id;
    }

    public void setChannelID(int id) {
        this.channel_id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
