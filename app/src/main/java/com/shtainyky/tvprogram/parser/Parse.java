package com.shtainyky.tvprogram.parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.CategoryItem;
import com.shtainyky.tvprogram.model.ChannelItem;
import com.shtainyky.tvprogram.model.ProgramItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Parse {

    public static List<ProgramItem> parseJSONtoListPrograms(String content) {
        try {
            List<ProgramItem> programs = new ArrayList<>();
            JSONArray array = new JSONArray(content);
            ProgramItem program;
            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);
                program = new ProgramItem();
                program.setId(object.getInt("channel_id"));
                program.setDate(object.getString("date"));
                program.setTime(object.getString("time"));
                program.setTitle(object.getString("title"));

                programs.add(program);
            }
            return programs;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }



    public static void loadImageFromServerWithPicasso(Context context, String uriString, ImageView intoImageView)
    {
        Uri uri = Uri.parse(uriString);
        Picasso.with(context)
                .load(uri)
                .placeholder(R.drawable.channel_logo)
                .error(R.drawable.image_error)
                .fit()
                .into(intoImageView);
    }
}
