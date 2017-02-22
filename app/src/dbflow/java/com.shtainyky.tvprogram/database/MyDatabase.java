package com.shtainyky.tvprogram.database;

import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.provider.ContentProvider;
import com.raizlabs.android.dbflow.annotation.provider.ContentUri;
import com.raizlabs.android.dbflow.annotation.provider.TableEndpoint;

@ContentProvider(authority = MyDatabase.AUTHORITY,
        database = MyDatabase.class,
        baseContentUri = MyDatabase.BASE_CONTENT_URI)
@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String NAME = "TVProgram";

    public static final int VERSION = 1;

    public static final String AUTHORITY = "com.shtainyky.tvprogram";

    public static final String BASE_CONTENT_URI = "content://";

    public static Uri buildUri(String... paths) {
        Uri.Builder builder = Uri.parse(BASE_CONTENT_URI + AUTHORITY).buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }


    @TableEndpoint(name = ProgramItem.ENDPOINT, contentProvider = MyDatabase.class)
    public static class ProgramItem {

        public static final String ENDPOINT = "ProgramItem";

        @ContentUri(path = ENDPOINT,
                type = ContentUri.ContentType.VND_MULTIPLE + ENDPOINT)
        public static final Uri CONTENT_URI = MyDatabase.buildUri(ENDPOINT);
    }


    @TableEndpoint(name = ChannelItem.ENDPOINT, contentProvider = MyDatabase.class)
    public static class ChannelItem {

        public static final String ENDPOINT = "ChannelItem";

        @ContentUri(path = ENDPOINT,
                type = ContentUri.ContentType.VND_MULTIPLE + ENDPOINT)
        public static final Uri CONTENT_URI = MyDatabase.buildUri(ENDPOINT);
    }


    @TableEndpoint(name = CategoryItem.ENDPOINT, contentProvider = MyDatabase.class)
    public static class CategoryItem {

        public static final String ENDPOINT = "CategoryItem";

        @ContentUri(path = ENDPOINT,
                type = ContentUri.ContentType.VND_MULTIPLE + ENDPOINT)
        public static final Uri CONTENT_URI = MyDatabase.buildUri(ENDPOINT);
    }

}