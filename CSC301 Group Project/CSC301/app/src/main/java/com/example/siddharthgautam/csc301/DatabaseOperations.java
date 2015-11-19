package com.example.siddharthgautam.csc301;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by siddharthgautam on 19/11/15.
 */
public class DatabaseOperations extends SQLiteOpenHelper {
    public static final int database_version = 1;
    public String Create_Query = "CREATE TABLE " + TableInfo.TABLE_NAME+ "("+ TableInfo.NAME+ " TEXT,"+ TableInfo.BIRTHDAY+" TEXT);";

    public DatabaseOperations(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
