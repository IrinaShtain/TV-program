package com.shtainyky.tvprogram.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.ImageView;

import com.shtainyky.tvprogram.R;
import com.squareup.picasso.Picasso;

public class Utils {
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
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
