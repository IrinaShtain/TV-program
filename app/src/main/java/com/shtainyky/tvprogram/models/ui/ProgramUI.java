package com.shtainyky.tvprogram.models.ui;

import android.database.Cursor;

import com.shtainyky.tvprogram.database.ContractClass;

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

    public static ProgramUI getProgram(Cursor cursor) {
        ProgramUI program = new ProgramUI();
        program.setTitle(cursor.
                getString(cursor.
                        getColumnIndex(ContractClass.Programs.COLUMN_PROGRAM_TITLE)));
        program.setTime(cursor.
                getString(cursor.
                        getColumnIndex(ContractClass.Programs.COLUMN_PROGRAM_TIME)));
        program.setDate(cursor.
                getString(cursor.
                        getColumnIndex(ContractClass.Programs.COLUMN_PROGRAM_DATE)));
        return program;
    }
}
