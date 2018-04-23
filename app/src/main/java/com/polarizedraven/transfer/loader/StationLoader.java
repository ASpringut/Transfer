package com.polarizedraven.transfer.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import com.polarizedraven.transfer.StopsDatabase;

/**
 * Created by aaron on 2/16/18.
 */

public class StationLoader extends AsyncTaskLoader<Cursor> {
    private static final String TAG = StationLoader.class.toString();

    //Cursor adapters require a rowid
    public static final String[] COLUMNS = {StopsDatabase.ROW_ID,
                                            StopsDatabase.STOP_COLUMN,
                                            StopsDatabase.LINE_ID_COLUMN,
                                            StopsDatabase.DIRECTION1,
                                            StopsDatabase.DIRECTION2,
                                            StopsDatabase.LAYOUT1,
                                            StopsDatabase.LAYOUT2};

    private static final String SELECTION = String.format("%s = ? AND %s = ?", StopsDatabase.LINE_ID_COLUMN, StopsDatabase.STOP_COLUMN);

    private final int line_id;
    private final String stop;

    public StationLoader (Context context, int line_id, String stop){
        super(context);
        this.line_id = line_id;
        this.stop = stop;
        onContentChanged();
    }

    @Override
    public Cursor loadInBackground() {
        String[] selectionArgs = {String.valueOf(line_id), stop};
        StopsDatabase sd = new StopsDatabase(super.getContext());
        Cursor cursor = sd.getReadableDatabase().query(StopsDatabase.STOP_TABLE,
                COLUMNS,
                SELECTION,
                selectionArgs,
                null,
                null,
                StopsDatabase.STOP_NUMBER_COLUMN,
                String.valueOf(1));

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
