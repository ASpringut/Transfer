package com.polarizedraven.transfer;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class LineViewer extends AppCompatActivity
                        implements LoaderManager.LoaderCallbacks<Cursor>,
                                   AdapterView.OnItemClickListener {

    private static final String TAG = LineViewer.class.toString();

    private Line line;
    private ListView listView;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_viewer);
        line = (Line) getIntent().getSerializableExtra(LineSelector.LINE_KEY);
        listView = findViewById(R.id.lineList);
        listView.setOnItemClickListener(this);
        getLoaderManager().initLoader(0,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (line != null) {
            LineLoader ll = new LineLoader(this, line);
            return ll;
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, String.valueOf(cursor.getCount()));
        this.cursor = cursor;
        listView.setAdapter(new LineAdapter(this, cursor, 0, line));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listView.setAdapter(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Cursor c = (Cursor) adapterView.getItemAtPosition(position);
        int lineIndex = java.util.Arrays.asList(LineLoader.COLUMNS).indexOf(StopsDatabase.LINE_ID_COLUMN);
        int stopIndex = java.util.Arrays.asList(LineLoader.COLUMNS).indexOf(StopsDatabase.STOP_COLUMN);

        int line_id = c.getInt(lineIndex);
        String stop = c.getString(stopIndex);

        Intent myIntent = new Intent(this, StationViewer.class);
        myIntent.putExtra(StationViewer.KEY_LINE_ID, line_id);
        myIntent.putExtra(StationViewer.KEY_STOP, stop);
        startActivity(myIntent);
    }
}
