package com.polarizedraven.transfer.view;

import android.graphics.Canvas;

import com.polarizedraven.transfer.Division;

/**
 * Created by aaron on 2/26/18.
 */

public class Car {

    private final Division division;
    private final int bottom;
    private final int right;
    private final int top;
    private final int left;

    public Car(int left, int top, int right, int bottom, Division division) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.division = division;
    }


    public void draw(Canvas canvas) {

    }
}
