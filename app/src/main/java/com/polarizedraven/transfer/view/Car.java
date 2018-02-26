package com.polarizedraven.transfer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.polarizedraven.transfer.R;
import com.polarizedraven.transfer.loader.Division;

/**
 * Created by aaron on 2/26/18.
 */

public class Car {

    private final Division division;
    private final int bottom;
    private final int top;

    private final int carLeft;
    private final int carRight;
    private final int numberRight;
    private final int numberLeft;

    private final CarType type;
    private final int doorWidth;
    private final int doorHeight;
    private final int number;

    public Car(int left, int top, int right, int bottom, Division division, CarType type, int number) {
        this.top = top;
        this.bottom = bottom;
        this.division = division;
        this.type = type;
        this.number = number;
        int midpoint = right - (right-left)/2;
        carLeft = midpoint;
        carRight = right;
        numberLeft = left;
        numberRight = midpoint;
        doorWidth = (int) ((carRight-carLeft)*2f/3f);
        doorHeight = (int) (doorWidth/2f);


    }


    public void draw(Canvas canvas, Context context) {
        Paint carPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        carPaint.setColor(context.getResources().getColor(R.color.lineGray));
        float carCornerRad = ((float)(carRight-carLeft))/10.0f;
        int verticalCenter = (bottom - top)/2 + top;
        int horizontalCenter = (carRight-carLeft)/2 + carLeft;

        /**
         * Draw the car body
         */
        if (type == CarType.FIRST) {
            //round the front of the first car
            canvas.drawRoundRect(carLeft,top,horizontalCenter,verticalCenter,carCornerRad,carCornerRad,carPaint);
            canvas.drawRect(horizontalCenter-carCornerRad, top, carRight, verticalCenter-carCornerRad, carPaint);
            canvas.drawRect(carLeft, verticalCenter-carCornerRad, carRight, bottom, carPaint);

        } else if (type == CarType.LAST) {
            // round the back of the last car
            canvas.drawRoundRect(carLeft,verticalCenter,horizontalCenter,bottom,carCornerRad,carCornerRad,carPaint);
            canvas.drawRect(carLeft, top,carRight,verticalCenter+carCornerRad, carPaint);
            canvas.drawRect(horizontalCenter-carCornerRad,verticalCenter-carCornerRad,carRight,bottom,carPaint);
        } else {
            //draw a car
            canvas.drawRect(carLeft, top, carRight, bottom, carPaint);
        }

        /**
         * Draw the doors on the car/ 3 if a-division line 4 if b-division line
         */
        int numberOfDoors = division==Division.A?3:4;

        Paint doorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        doorPaint.setColor(context.getResources().getColor(R.color.linePurple));

        float doorHeight = (bottom-top)/(float) (numberOfDoors);//+1 to leave room for gaps
        float gapHeight = doorHeight/5.0f; //distribute the last height of doors among the gaps
        int doorDrawTop = (int) (top + gapHeight/2);
        int doorDrawBottom = (int) (bottom - gapHeight/2);
        float doorDrawHeight = (doorDrawBottom-doorDrawTop)/(float) numberOfDoors;


        for (int i=0; i<numberOfDoors; i++) {
                int l = carRight - doorWidth;
                int doorTop = (int) (i*doorDrawHeight + doorDrawTop + (gapHeight/2));
                int doorBottom = (int) ((i+1)*doorDrawHeight + doorDrawTop - (gapHeight/2));

                Rect doorRect = new Rect(l, doorTop, l + doorWidth, doorBottom);
                canvas.drawRect(doorRect, doorPaint);

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

        String carText = String.valueOf(number);

        Rect bounds = new Rect(numberLeft,top,numberRight,bottom);

        Rect b = new Rect();
        textPaint.getTextBounds(carText, (int) 0,carText.length(), b);

        float textYCoordinate =  bounds.exactCenterY() + b.height()/2.0f - b.bottom;
        float textXCoordinate = bounds.exactCenterX() - b.width()/2.0f - b.left;
        canvas.drawText(carText,textXCoordinate,textYCoordinate,textPaint);
    }

    public enum CarType {
        FIRST,LAST, MIDDLE;
    }

}
