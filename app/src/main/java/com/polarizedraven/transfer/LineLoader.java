package com.polarizedraven.transfer;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by aaron on 2/15/18.
 */

public class LineLoader extends AsyncTaskLoader<Cursor> {

    private static final String TAG = LineLoader.class.toString();

    //Cursor adapters require a rowid
    public static final String[] COLUMNS = {StopsDatabase.ROW_ID, StopsDatabase.STOP_COLUMN, StopsDatabase.LINE_COLUMN};
    private static final String SELECTION = StopsDatabase.LINE_COLUMN + " = ?";

    private final Line line;

    public LineLoader (Context context, Line line){
        super(context);
        this.line = line;
        onContentChanged();
    }

    @Override
    public Cursor loadInBackground() {
        String[] selectionArgs = {line.toString()};
        StopsDatabase sd = new StopsDatabase(super.getContext());
        Cursor cursor = sd.getReadableDatabase().query(StopsDatabase.STOP_TABLE,
                COLUMNS,
                SELECTION,
                selectionArgs,
                null,
                null,
                StopsDatabase.STOPNUMBER_COLUMN,
                null);

        return cursor;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
