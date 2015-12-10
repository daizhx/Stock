package com.hengxuan.stock;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2015/9/2.
 */
public final class DataContract {
    public DataContract(){};

    public static final String SQL_CREATE_NEWS_ENTRYS = "CREATE TABLE " + NewsTable.TABLE_NAME + " (" + NewsTable._ID + " INTEGER PRIMARY KEY,"
            + NewsTable.COLUMN_NAME_TITLE + " TEXT NOT NULL,"
            + NewsTable.COLUMN_NAME_ABSTRACT + " TEXT,"
            + NewsTable.COLUMN_NAME_DATE + " DATE,"
            + NewsTable.COLUMN_NAME_IMAGE_URI + " TEXT,"
            + NewsTable.COLUMN_NAME_IMAGE_LOCAL_CACHE + " TEXT,"
            + NewsTable.COLUMN_NAME_NEWS_URI + " TEXT"
            + ")";
    public static final String SQL_DELETE_NEW_ENTRYS = "DROP TABLE IF EXISTS " + NewsTable.TABLE_NAME;

    public static final String SQL_CREATE_CP_ENTRYS = "CREATE TABLE " + CpMsgEntry.TABLE_NAME + " (" + CpMsgEntry._ID + " INTEGER AUTO_INCREMENT PRIMARY KEY,"
            + CpMsgEntry.COLUMN_NAME + " TEXT,"
            + CpMsgEntry.COLUMN_TIME + " DATETIME NOT NULL,"
            + CpMsgEntry.COLUMN_CONTENT + " TEXT NOT NULL"
            + ")";
    public static final String SQL_DELETE_CP_ENTRYS = "DROP TABLE IF EXISTS " + CpMsgEntry.TABLE_NAME;

    public static final String SQL_CREATE_ZXG_ENTRYS = "CREATE TABLE " + ZXGEntry.TABLE_NAME + " (" + ZXGEntry._ID + " SMALLINT AUTO_INCREMENT PRIMARY KEY,"
            + ZXGEntry.COLUMN_NAME + " VARCHAR(20) NOT NULL,"
            + ZXGEntry.COLUMN_CODE + " VARCHAR(6) NOT NULL"
            + " )";
    public static final String SQL_DELETE_ZXG_ENTRYS = "drop table if exists " + ZXGEntry.TABLE_NAME;

    public static final class NewsTable implements BaseColumns {
        public static final String TABLE_NAME = "news";

        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ABSTRACT = "abstract";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_IMAGE_URI = "image_uri";
        public static final String COLUMN_NAME_IMAGE_LOCAL_CACHE = "image_cache";
        public static final String COLUMN_NAME_NEWS_URI = "uri";

        public final String DEFAULT_SORT_ORDER = "_id desc";
    }

    public static final class CpMsgEntry implements BaseColumns{
        public static final String TABLE_NAME = "cpmsg";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_CONTENT = "content";
    }

    public static final class ZXGEntry implements BaseColumns{
        public static final String TABLE_NAME = "zxg";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CODE = "code";
    }
}
