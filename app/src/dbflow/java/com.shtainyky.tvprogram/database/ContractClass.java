package com.shtainyky.tvprogram.database;

public final class ContractClass {

    private ContractClass() {
    }

    static final class Programs {
        private Programs() {
        }
        static final Uri CONTENT_URI = "content://com.shtainyky.tvprogram/ProgramItem";
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

    static final class Channels{
        private Channels() {
        }
        static final Uri CONTENT_URI = "content://com.shtainyky.tvprogram/ChannelItem";
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

    public static final class Categories{
        private Categories() {
        }
        static final Uri CONTENT_URI = "content://com.shtainyky.tvprogram/CategoryItem";
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
