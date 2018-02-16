package com.polarizedraven.transfer;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by aaron on 2/16/18.
 */

public class StationLoader extends AsyncTaskLoader<Cursor> {
    private static final String TAG = StationLoader.class.toString();

    //Cursor adapters require a rowid
    public static final String[] COLUMNS = {StopsDatabase.ROW_ID, StopsDatabase.STOP_COLUMN, StopsDatabase.LINE_COLUMN};
    private static final String SELECTION = String.format("%s = ? AND %s = ?", StopsDatabase.LINE_COLUMN, StopsDatabase.STOP_COLUMN);

    private final Line line;
    private final String stop;

    public StationLoader (Context context, Line line, String stop){
        super(context);
        this.line = line;
        this.stop = stop;
        onContentChanged();
    }

    @Override
    public Cursor loadInBackground() {
        String[] selectionArgs = {line.toString(), stop};
        StopsDatabase sd = new StopsDatabase(super.getContext());
        Cursor cursor = sd.getReadableDatabase().query(StopsDatabase.STOP_TABLE,
                COLUMNS,
                SELECTION,
                selectionArgs,
                null,
                null,
                StopsDatabase.STOPNUMBER_COLUMN,
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
