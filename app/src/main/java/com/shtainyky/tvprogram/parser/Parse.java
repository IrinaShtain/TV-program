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

    public static List<CategoryItem> parseJSONtoListCategory(String content) {
        try {
            List<CategoryItem> categories = new ArrayList<>();
            JSONArray array = new JSONArray(content);
            CategoryItem category;
            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);

                category = new CategoryItem();
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

    public static List<ChannelItem> parseJSONtoListChannels(String content) {
        try {
            List<ChannelItem> channels = new ArrayList<>();
            JSONArray array = new JSONArray(content);
            ChannelItem channel;

            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);

                channel = new ChannelItem();
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
        return bitmap;
    }

    public static void saveImageToStorage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
                Log.d("saveImage", "saveImage: Something went wrong!");
            e.printStackTrace();
        }
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
