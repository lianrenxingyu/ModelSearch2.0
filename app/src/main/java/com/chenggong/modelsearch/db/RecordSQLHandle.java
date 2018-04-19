package com.chenggong.modelsearch.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenggong on 18-4-18.
 * <p>
 * 历史记录的数据库操作帮助类
 * 应该包含的操作
 * (1)添加记录{@link }
 * (2)判断数据库中是否已经存在记录
 * (3)返回所有的历史记录
 * (4)模糊查询,返回相关数据
 * (5)清空数据库
 */

public class RecordSQLHandle {

    private RecordSQLHelper databaseHelper;
    private SQLiteDatabase readDB;
    private SQLiteDatabase writeDB;

    public RecordSQLHandle(Context context) {
        databaseHelper = new RecordSQLHelper(context, "Records.db");
    }

    /**
     * 插入数据
     *
     * @param record
     */
    public void insert(String record) {
        if (!hasRecord(record)) {
            writeDB = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", record);
            writeDB.insert("record", null, values);
            writeDB.close();
        }
    }

    /**
     * 返回所有的历史记录
     */
    public List<String> getAllRecord() {
        List<String> recordList = new ArrayList<>();
        readDB = databaseHelper.getReadableDatabase();
        Cursor cursor = readDB.query("record", null, null, null, null, null, "id desc");
        while (cursor.moveToNext()) {
            String record = cursor.getString(cursor.getColumnIndex("name"));
            recordList.add(record);
        }
        cursor.close();
        readDB.close();
        return recordList;
    }

    /**
     * 模糊查询
     */
    public List<String> querySimilar(String record) {
        //查询字符串
        String queryStr = "select * from record where name like '%" +
                record + "%' order by id desc";

        readDB = databaseHelper.getReadableDatabase();
        List<String> simlarRecords = new ArrayList<>();
        Cursor cursor = readDB.rawQuery(queryStr, null);
        while (cursor.moveToNext()) {
            simlarRecords.add(cursor.getString(cursor.getColumnIndex("name")));
        }
        cursor.close();
        readDB.close();
        return simlarRecords;
    }

    /**
     * 清空数据库
     */
    public void clearData() {
        writeDB = databaseHelper.getWritableDatabase();
        writeDB.delete("record", null, null);
        writeDB.close();
    }

    /**
     * 是否已经含有record
     */
    public boolean hasRecord(String record) {

        boolean flag = false;
        readDB = databaseHelper.getReadableDatabase();
        Cursor cursor = readDB.query("record", new String[]{"name"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (record.equals(cursor.getString(cursor.getColumnIndex("name")))) {
                flag = true;
            }
        }
        cursor.close();
        readDB.close();
        return flag;
    }
}
