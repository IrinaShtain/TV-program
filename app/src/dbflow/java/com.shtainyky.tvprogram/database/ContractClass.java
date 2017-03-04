package com.shtainyky.tvprogram.database;

import android.net.Uri;

public final class ContractClass {

    private ContractClass() {
    }

    public static final class Programs {
        private Programs() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://com.shtainyky.tvprogram/ProgramItem");
        public static final String COLUMN_PROGRAM_ID = "_id";
        public static final String COLUMN_PROGRAM_CHANNEL_ID = "channel_id";
        public static final String COLUMN_PROGRAM_TITLE = "title";
        public static final String COLUMN_PROGRAM_TIME = "time";
        public static final String COLUMN_PROGRAM_DATE = "date";

        public static final String[] DEFAULT_PROJECTION = new String[]{
                ContractClass.Programs.COLUMN_PROGRAM_ID,
                ContractClass.Programs.COLUMN_PROGRAM_CHANNEL_ID,
                ContractClass.Programs.COLUMN_PROGRAM_TITLE,
                ContractClass.Programs.COLUMN_PROGRAM_TIME,
                ContractClass.Programs.COLUMN_PROGRAM_DATE
        };


    }

    public static final class Channels {
        private Channels() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://com.shtainyky.tvprogram/ChannelItem");
        public static final String COLUMN_CHANNEL_ID = "_id";
        public static final String COLUMN_CHANNEL_TITLE = "name";
        public static final String COLUMN_CHANNEL_IMAGE_URL = "picture";
        public static final String COLUMN_CHANNEL_CATEGORY_ID = "category_id";
        public static final String COLUMN_CHANNEL_CATEGORY_TITLE = "category_title";
        public static final String COLUMN_CHANNEL_IS_PREFERRED = "is_preferred";

        public static final String[] DEFAULT_PROJECTION = new String[]{
                ContractClass.Channels.COLUMN_CHANNEL_ID,
                ContractClass.Channels.COLUMN_CHANNEL_TITLE,
                ContractClass.Channels.COLUMN_CHANNEL_IMAGE_URL,
                ContractClass.Channels.COLUMN_CHANNEL_CATEGORY_ID,
                ContractClass.Channels.COLUMN_CHANNEL_CATEGORY_TITLE,
                ContractClass.Channels.COLUMN_CHANNEL_IS_PREFERRED
        };

    }

    public static final class Categories {
        private Categories() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://com.shtainyky.tvprogram/CategoryItem");
        public static final String COLUMN_CATEGORY_ID = "_id";
        public static final String COLUMN_CATEGORY_TITLE = "title";
        public static final String COLUMN_CATEGORY_IMAGE_URL = "picture";

        public static final String[] DEFAULT_PROJECTION = new String[]{
                ContractClass.Categories.COLUMN_CATEGORY_ID,
                ContractClass.Categories.COLUMN_CATEGORY_TITLE,
                ContractClass.Categories.COLUMN_CATEGORY_IMAGE_URL
        };


    }
}
