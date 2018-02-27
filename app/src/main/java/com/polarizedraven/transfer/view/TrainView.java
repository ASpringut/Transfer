package com.polarizedraven.transfer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.polarizedraven.transfer.R;
import com.polarizedraven.transfer.loader.Division;

import java.util.ArrayList;


/**
 * Created by aaron on 2/17/18.
 */

public class TrainView extends View {

    private final float carLength;
    private final float carWidth;
    private final int carNumber;
    private final int boardingSide;
    private final float interCarLength = 2;
    private final Division division= Division.A;

    private  ArrayList<Car> cars;

    private int height;
    private int width;
    private Train train;
    private Context context;
    private int platformWidth;

    public TrainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TrainView,
                0,
                0);
        this.context = context;
        boardingSide = a.getInteger(R.styleable.TrainView_boardingSide, 0);
        carNumber = a.getInteger(R.styleable.TrainView_numberOfCars, 0);
        carWidth = a.getFloat(R.styleable.TrainView_carWidth, 0);
        carLength = a.getFloat(R.styleable.TrainView_carLength, 0);
        train = new Train(division, carNumber, carWidth/carLength, 0.1f, 5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        train.draw(canvas, context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        //Technically trainWidth could be larger than w. This shouldnt happen except on extremely narrow screens.
        int trainWidth = train.setSize(height);
        platformWidth = trainWidth/2;
    }
}
