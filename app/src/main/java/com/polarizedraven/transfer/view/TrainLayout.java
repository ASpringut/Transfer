package com.polarizedraven.transfer.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;

/**
 * Created by aaron on 2/27/18.
 */

public class TrainLayout extends ConstraintLayout {


    public TrainLayout(Context context) {
        super(context);
        ImageView view = new ImageView(context);
        this.addView(view);
    }
}
