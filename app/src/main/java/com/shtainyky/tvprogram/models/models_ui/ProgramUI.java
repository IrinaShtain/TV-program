package com.shtainyky.tvprogram.models.models_ui;

public class ProgramUI {
    private int channel_id;
    private String date;
    private String time;
    private String title;

    public int getId() {
        return channel_id;
    }

    public void setId(int id) {
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
