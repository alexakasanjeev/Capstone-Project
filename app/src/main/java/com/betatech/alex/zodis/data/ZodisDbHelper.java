package com.betatech.alex.zodis.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenovo on 11/29/2017.
 */

public class ZodisDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "zodis.db";
    private static final int DATABASE_VERSION = 1;


    ZodisDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_ROOT_TABLE = "CREATE TABLE " + ZodisContract.RootEntry.TABLE_NAME + " (" +
                ZodisContract.RootEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ZodisContract.RootEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ZodisContract.RootEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                ZodisContract.RootEntry.COLUMN_LEVEL + " TEXT NOT NULL," +
                ZodisContract.RootEntry.COLUMN_LESSON + " INTEGER NOT NULL," +
                ZodisContract.RootEntry.COLUMN_STATUS + " INTEGER DEFAULT 0," +
                ZodisContract.RootEntry.COLUMN_FAVOURITE + " INTEGER DEFAULT 0);";

        final String SQL_CREATE_DERIVED_TABLE = "CREATE TABLE " + ZodisContract.DerivedEntry.TABLE_NAME + " (" +
                ZodisContract.DerivedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ZodisContract.DerivedEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ZodisContract.DerivedEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                ZodisContract.DerivedEntry.COLUMN_ROOT_NAME + " TEXT NOT NULL);";

        final String SQL_CREATE_LEVEL_TABLE = "CREATE TABLE " + ZodisContract.LevelEntry.TABLE_NAME + " (" +
                ZodisContract.LevelEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ZodisContract.LevelEntry.COLUMN_LEVEL_NAME + " TEXT NOT NULL, " +
                ZodisContract.LevelEntry.COLUMN_LESSON_ID + " INTEGER NOT NULL," +
                ZodisContract.LevelEntry.COLUMN_LEVEL_STATUS + " INTEGER DEFAULT 0);";

        db.execSQL(SQL_CREATE_ROOT_TABLE);
        db.execSQL(SQL_CREATE_DERIVED_TABLE);
        db.execSQL(SQL_CREATE_LEVEL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ZodisContract.DerivedEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ZodisContract.RootEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ZodisContract.LevelEntry.TABLE_NAME);
        onCreate(db);
    }
}
