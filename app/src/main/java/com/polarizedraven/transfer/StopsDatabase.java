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

    private static final String DATABASE_NAME = "subway.db";
    private static final int DATABASE_VERSION = 1;

    public StopsDatabase (Context context){
        super(context, StopsDatabase.DATABASE_NAME, null, StopsDatabase.DATABASE_VERSION);
    }

}
