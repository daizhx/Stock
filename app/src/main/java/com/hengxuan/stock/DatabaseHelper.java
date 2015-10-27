package com.hengxuan.stock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import com.hengxuan.stock.DataContract;
import com.hengxuan.stock.utils.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	
	private static final String DATABASE_NAME = "my.db";
    private static final int DATABASE_VERSION = 1;
    
	public DatabaseHelper(Context c){
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
        Log.i("create db tables!");
        db.execSQL(DataContract.SQL_CREATE_NEWS_ENTRYS);
        db.execSQL(DataContract.SQL_CREATE_CP_ENTRYS);
        db.execSQL(DataContract.SQL_CREATE_ZXG_ENTRYS);
	}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("delete tables and call onCreate(db) for upgrade!");
        db.execSQL(DataContract.SQL_DELETE_NEW_ENTRYS);
        db.execSQL(DataContract.SQL_DELETE_CP_ENTRYS);
        db.execSQL(DataContract.SQL_DELETE_ZXG_ENTRYS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
