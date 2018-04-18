package com.chenggong.modelsearch.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chenggong.modelsearch.utils.Logger;

/**
 * Created by chenggong on 18-4-18.
 */

public class RecordSQLHelper extends SQLiteOpenHelper {

    private static final String TAG = "RecordSQLHelper";
    private static int version = 1;
    private Context mContext;


    public static final String CREATE_TABLE = "create table record (" +
            "id integer primary key autoincrement," +
            "name varchar(30))";

    public RecordSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public RecordSQLHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    public RecordSQLHelper(Context context, String name) {
        super(context,name,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Logger.d(TAG, "SQLite 创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
