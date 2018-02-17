package com.polarizedraven.transfer.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.polarizedraven.transfer.StopsDatabase;

import java.text.MessageFormat;

/**
 * Created by aaron on 2/17/18.
 */

public class TerminusLoader extends AsyncTaskLoader<Cursor> {

    private static final String queryFmt = "SELECT * FROM stops WHERE stop_number = (SELECT MIN(stop_number) from stops where line_id = {0}) AND line_id = {0} UNION SELECT * FROM stops WHERE stop_number = (SELECT MAX(stop_number) from stops where line_id = {0}) AND line_id = {0} ORDER BY stop_number ASC";


    private final int line_id;

    public TerminusLoader(Context context, int line_id) {
        super(context);
        this.line_id = line_id;
        onContentChanged();
    }

    @Override
    public Cursor loadInBackground() {
        MessageFormat fmt = new MessageFormat(queryFmt);
        Object[] args = {line_id};
        String query = fmt.format(args);
        SQLiteDatabase sd = new StopsDatabase(super.getContext()).getReadableDatabase();
        Cursor cursor = sd.rawQuery(query,null);
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
