package com.polarizedraven.transfer;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by aaron on 2/15/18.
 */

public class StopsDatabase extends SQLiteAssetHelper {

    public static final String STOP_TABLE = "stops";
    public static final String ROW_ID = "rowid _id";
    public static final String STOP_COLUMN = "stop_name";
    public static final String LINE_ID_COLUMN = "line_id";
    public static final String STOP_NUMBER_COLUMN = "stop_number";
    public static final String DIRECTION1 = "direction1";
    public static final String DIRECTION2 = "direction2";
    public static final String LAYOUT1 = "layout1";
    public static final String LAYOUT2 = "layout2";

    private static final String DATABASE_NAME = "subway1.db";
    private static final int DATABASE_VERSION = 1;

    public StopsDatabase (Context context){
        super(context, StopsDatabase.DATABASE_NAME, null, StopsDatabase.DATABASE_VERSION);
    }

}
