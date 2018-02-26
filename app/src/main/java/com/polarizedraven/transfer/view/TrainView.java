package com.polarizedraven.transfer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.polarizedraven.transfer.Division;
import com.polarizedraven.transfer.R;

import java.util.ArrayList;


/**
 * Created by aaron on 2/17/18.
 */

public class TrainView extends View {

    private final float carLength;
    private final float carWidth;
    private final int carNumber;
    private final int boardingSide;
    private final int trainDirection;
    private final float interCarLength = 2;
    private final Division division = Division.A;
    private final float doorLength;
    private final float doorWidth;

    private  ArrayList<Car> cars;


    private int height;
    private int width;

    private Context context;

    public TrainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TrainView,
                0,
                0);
        this.context = context;
        boardingSide = a.getInteger(R.styleable.TrainView_boardingSide, 0);
        trainDirection = a.getInteger(R.styleable.TrainView_trainDirection, 0);
        carNumber = a.getInteger(R.styleable.TrainView_numberOfCars, 0);
        carWidth = a.getFloat(R.styleable.TrainView_carWidth, 0);
        carLength = a.getFloat(R.styleable.TrainView_carLength, 0);
        doorWidth = carWidth*2f;
        doorLength = carLength*2f/3f;


        cars = new ArrayList<Car>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCars(height,carNumber, carLength, interCarLength, carWidth, canvas);

    }
    private void drawCars(int height, int carNumber, float carLength, float interCarLength, float carWidth, Canvas canvas){

        int interCarGaps = carNumber -1;
        float totalCarLength = carLength * carNumber + interCarGaps * interCarLength;

        float drawHeightToLengthRatio = height/totalCarLength;

        int carDrawHeight = (int)(carLength * drawHeightToLengthRatio);
        int interCarDrawHeight = (int) (interCarLength * drawHeightToLengthRatio);
        int carDrawWidth = (int) (carWidth * drawHeightToLengthRatio);

        int currentHeight = 0;
        for(int i=0; i<(carNumber+interCarGaps); i++) {
            if (i % 2 == 0) {

                drawCar(canvas,
                        currentHeight,
                        currentHeight+carDrawHeight,
                        0,
                        carDrawWidth,
                        i/2+1,
                        i==0,
                        (i+1) == (carNumber+interCarGaps));

                currentHeight += carDrawHeight;
            } else {
                //"draw" a gap between cars
                currentHeight += interCarDrawHeight;
            }
        }
    }

    private void drawCar(Canvas canvas, int top, int bottom, int left, int right, int carNumber, boolean isFirst, boolean isLast) {
        Paint carPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        carPaint.setColor(context.getResources().getColor(R.color.lineGray));
        float carCornerRad = ((float)(right-left))/8.0f;
        int halfPos = (bottom - top)/2 + top;

        /**
         * Draw the car body
         */
        if (isFirst) {
            //round the front of the first car
            canvas.drawRoundRect(left, top, right, bottom, carCornerRad, carCornerRad, carPaint);
            canvas.drawRect(left, halfPos, right, bottom, carPaint);
        } else if (isLast) {
            // round the back of the last car
            canvas.drawRect(left, top, right, halfPos, carPaint);
            canvas.drawRoundRect(left, top, right, bottom, carCornerRad, carCornerRad, carPaint);
        } else {
            //draw a car
            canvas.drawRect(left, top, right, bottom, carPaint);
        }

        /**
         * Draw the doors on the car 3 if a-division line 4 if b-division line
         */

        Paint doorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        doorPaint.setColor(context.getResources().getColor(R.color.lineBlue));

        if (division == Division.A) {
            int doorSpacing = (top-bottom)/4;
            for (int i=0;i<3;i++) {
                Rect doorRect = new Rect(right-(int)doorWidth, (int) (bottom +(doorSpacing*(i+1)-(doorLength*0.5))),right, (int) (bottom +(doorSpacing*(i+1)+(doorLength*0.5))));
                canvas.drawRect(doorRect, doorPaint);
            }
        } else {

        }


        /**
         * Draw the text for the car number
         */
        float textSize = (bottom-top)*2.0f/4.0f;
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(context.getResources().getColor(R.color.lineDarkGrey));
        // 1/3rd of height of car
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.create((String) null,Typeface.NORMAL));
        textPaint.setTextAlign(Paint.Align.LEFT);

        String carText = String.valueOf(carNumber);

        Rect bounds = new Rect(left,top,right,bottom);
        StaticLayout sl = new StaticLayout(carText, textPaint, bounds.width(), Layout.Alignment.ALIGN_NORMAL, 1, 1, true);

        Rect b = new Rect();
        textPaint.getTextBounds(carText, (int) 0,carText.length(), b);

        float textYCoordinate =  bounds.exactCenterY() + b.height()/2.0f - b.bottom;
        float textXCoordinate = bounds.exactCenterX() - b.width()/2.0f - b.left;
        canvas.drawText(carText,textXCoordinate,textYCoordinate,textPaint);
        canvas.save();
        canvas.translate(textXCoordinate, textYCoordinate);
        //draws static layout on canvas
        //sl.draw(canvas);
        canvas.restore();
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        

    }
}
