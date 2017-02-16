package com.shtainyky.tvprogram.database;

import android.net.Uri;
import android.provider.BaseColumns;

final class ContractClass {
    static final String AUTHORITY = "com.shtainyky.tvprogram";
    private static final String SCHEME = "content://";

    private ContractClass() {
    }

    static final class Programs implements BaseColumns {
        private Programs() {
        }

        static final String TABLE_NAME_PROGRAMS = "programs";
        static final String PATH_PROGRAMS = "/programs";
        static final String PATH_PROGRAMS_ID = "/programs/";
        static final int PROGRAMS_ID_PATH_POSITION = 1;
        static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_PROGRAMS);
        static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_PROGRAMS_ID);
        static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + "programs";
        static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + "programs";

        static final String COLUMN_PROGRAM_ID = "_id";
        static final String COLUMN_PROGRAM_CHANNEL_ID = "program_channel_id";
        static final String COLUMN_PROGRAM_TITLE = "program_title";
        static final String COLUMN_PROGRAM_TIME = "program_time";
        static final String COLUMN_PROGRAM_DATE = "program_date";

        static final String[] DEFAULT_PROJECTION = new String[]{
                ContractClass.Programs.COLUMN_PROGRAM_ID,
                ContractClass.Programs.COLUMN_PROGRAM_CHANNEL_ID,
                ContractClass.Programs.COLUMN_PROGRAM_TITLE,
                ContractClass.Programs.COLUMN_PROGRAM_TIME,
                ContractClass.Programs.COLUMN_PROGRAM_DATE
        };


    }

    static final class Channels implements BaseColumns {
        private Channels() {
        }

        static final String TABLE_NAME_CHANNELS = "channels";
        static final String PATH_CHANNELS = "/channels";
        static final String PATH_CHANNELS_ID = "/channels/";
        static final int CHANNELS_ID_PATH_POSITION = 1;
        static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_CHANNELS);
        static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_CHANNELS_ID);
        static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PATH_CHANNELS;
        static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + PATH_CHANNELS;

        static final String COLUMN_CHANNEL_ID = "_id";
        static final String COLUMN_CHANNEL_TITLE = "channel_title";
        static final String COLUMN_CHANNEL_IMAGE_URL = "channel_image_url";
        static final String COLUMN_CHANNEL_CATEGORY_ID = "category_id";
        static final String COLUMN_CHANNEL_IS_PREFERRED = "is_preferred";

        static final String[] DEFAULT_PROJECTION = new String[]{
                ContractClass.Channels.COLUMN_CHANNEL_ID,
                ContractClass.Channels.COLUMN_CHANNEL_TITLE,
                ContractClass.Channels.COLUMN_CHANNEL_IMAGE_URL,
                ContractClass.Channels.COLUMN_CHANNEL_CATEGORY_ID,
                ContractClass.Channels.COLUMN_CHANNEL_IS_PREFERRED
        };

    }

    static final class Categories implements BaseColumns {
        private Categories() {
        }

        static final String TABLE_NAME_CATEGORIES = "categories";
        static final String PATH_CATEGORIES = "/categories";
        static final String PATH_CATEGORIES_ID = "/categories/";
        static final int CATEGORIES_ID_PATH_POSITION = 1;
        static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_CATEGORIES);
        static final Uri CONTENT_ID_URI_BASE = Uri.parse(SCHEME + AUTHORITY + PATH_CATEGORIES_ID);
        static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + "categories";
        static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + "categories";

        static final String COLUMN_CATEGORY_ID = "_id";
        static final String COLUMN_CATEGORY_TITLE = "category_title";
        static final String COLUMN_CATEGORY_IMAGE_URL = "category_image_url";

        static final String[] DEFAULT_PROJECTION = new String[]{
                ContractClass.Categories.COLUMN_CATEGORY_ID,
                ContractClass.Categories.COLUMN_CATEGORY_TITLE,
                ContractClass.Categories.COLUMN_CATEGORY_IMAGE_URL
        };


    }
}
