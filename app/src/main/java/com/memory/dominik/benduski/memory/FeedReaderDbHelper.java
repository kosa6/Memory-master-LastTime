package com.memory.dominik.benduski.memory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 09.11.2017.
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper
{
    public static class FeedEntry implements BaseColumns
    {
        public static final int COLUMNS_NUMBER = 2;
        public static final String TABLE_NAME = "paths_table";
        public static final String COLUMN_NAME_PATHS = "paths";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_PATHS + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "FeedReader.db";

    public FeedReaderDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public void insertData(String value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_PATHS, value);
        long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
    }

    public void deleteData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + FeedEntry.TABLE_NAME);
        db.close();
    }

    public List getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + FeedEntry.TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        List itemIds = new ArrayList();
        while(data.moveToNext())
        {
            String itemId = data.getString(
                    data.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_PATHS));
            itemIds.add(itemId);
        }
        data.close();
        return itemIds;
    }
}
