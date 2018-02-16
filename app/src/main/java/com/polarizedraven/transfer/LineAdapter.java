package com.polarizedraven.transfer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by aaron on 2/15/18.
 */

public class LineAdapter extends CursorAdapter {

    private final LayoutInflater li;
    private final Line line;

    public LineAdapter(Context context, Cursor c, int flags, Line l) {
        super(context, c, flags);
        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        line = l;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return li.inflate(R.layout.station_layout,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView stationNameView = view.findViewById(R.id.stationName);
        ImageView lineImage = view.findViewById(R.id.lineImage);
        int stopIndex = cursor.getColumnIndex(StopsDatabase.STOP_COLUMN);
        String stationName = cursor.getString(stopIndex);
        stationNameView.setText(stationName);

        if (cursor.isFirst()) {
            lineImage.setImageDrawable(context.getDrawable(R.drawable.ic_line_red_start));
        } else if (cursor.isLast()) {
            lineImage.setImageDrawable(context.getDrawable(R.drawable.ic_line_red_end));
        } else {
            lineImage.setImageDrawable(context.getDrawable(R.drawable.ic_line_red));
        }

        //reset the color from the line
        lineImage.setColorFilter(line.getColor(), PorterDuff.Mode.SRC_ATOP);
    }


}
