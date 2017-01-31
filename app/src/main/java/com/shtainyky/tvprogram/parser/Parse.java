package com.shtainyky.tvprogram.parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.widget.ImageView;

import com.shtainyky.tvprogram.R;
import com.shtainyky.tvprogram.model.Category;
import com.shtainyky.tvprogram.model.Channel;
import com.shtainyky.tvprogram.model.Program;
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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class Parse {

    public static List<Program> parseJSONtoListPrograms(String content) {
        try {
            List<Program> programs = new ArrayList<>();
            JSONArray array = new JSONArray(content);
            Program program;

            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);
                program = new Program();
                program.setChannel_id(object.getInt("channel_id"));
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

    public static List<Category> parseJSONtoListCategory(String content) {
        try {
            List<Category> categories = new ArrayList<>();
            JSONArray array = new JSONArray(content);
            Category category;

            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);

                category = new Category();
                category.setId(object.getInt("id"));
                category.setTitle(object.getString("title"));
                category.setImageUrl(object.getString("picture"));

                categories.add(category);
            }
            return categories;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static List<Channel> parseJSONtoListChannels(String content) {
        try {
            List<Channel> channels = new ArrayList<>();
            JSONArray array = new JSONArray(content);
            Channel channel;

            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);

                channel = new Channel();
                channel.setId(object.getInt("id"));
                channel.setName(object.getString("name"));
                channel.setPictureUrl(object.getString("picture"));
                channel.setCategory_id(object.getInt("category_id"));

                channels.add(channel);
            }
            return channels;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
    public static Bitmap loadImageFromServer(String url) {
        Bitmap bitmap = null;
        try {
            InputStream in = (InputStream) new URL(url).getContent();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
      //  Log.d("loadImageFromServer", url);
        return bitmap;
    }
    public static void saveImageToStorage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
        //    Log.d("saveImage", "saveImage: Something went wrong!");
            e.printStackTrace();
        }
      //  Log.d("saveImage", "ooooooooooooooo!");
    }
    public static void loadImageBitmapFromStorage(Context context, String imageName, ImageView intoView) {
        File file = context.getFileStreamPath(imageName);
        Picasso.with(context)
                .load(file)
                .placeholder(R.drawable.channel_logo)
                .error(R.drawable.image_error)
                .fit()
                .into(intoView);

    }
}
