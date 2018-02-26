package com.polarizedraven.transfer.view;

import android.content.Context;
import android.graphics.Canvas;

import com.polarizedraven.transfer.loader.Division;

import java.util.ArrayList;

/**
 * Created by aaron on 2/26/18.
 */

public class Train {

    private final Division division;
    private final int carNumber;
    private final float widthToHeightRatio;
    private final float interCarLenToLen;
    private int height = 0;
    private int width = 0;
    private final ArrayList<Car> cars = new ArrayList<Car>();

    public Train(Division division, int carNumber, float widthToHeightRatio, float interCarLenToCarLen) {
        this.division = division;
        this.carNumber = carNumber;
        this.widthToHeightRatio = widthToHeightRatio;
        this.interCarLenToLen = interCarLenToCarLen;
    }

    /**
     *
     * @param height
     * @return the width that this view will take
     */
    public int setSize(int height) {
        this.height = height;
        /**
         *Clear the current cars and layout the new cars
         */
        cars.clear();

        int interCarGaps = carNumber-1;
        //total car length in arbitrary units
        float totalCarLength = carNumber + interCarGaps * interCarLenToLen;
        float drawHeightToLengthRatio = height/totalCarLength;
        int carDrawHeight = (int) (drawHeightToLengthRatio);
        int interCarDrawHeight = (int) (0.1 * drawHeightToLengthRatio);
        int carDrawWidth = (int) (carDrawHeight * widthToHeightRatio)*2;// *2 b/c need room for numbers too

        int currentHeight = 0;
        for(int i=0; i<(carNumber+interCarGaps); i++) {
            if (i % 2 == 0) {

                Car.CarType type = Car.CarType.MIDDLE;
                if (i==0) {
                    type = Car.CarType.FIRST;
                } else if (i+1 == carNumber+interCarGaps){
                    type = Car.CarType.LAST;
                }

                Car car = new Car(0,currentHeight,carDrawWidth,currentHeight+carDrawHeight, division, type, 1+i/2);
                cars.add(car);
                currentHeight += carDrawHeight;
            } else {
                //"draw" a gap between cars
                currentHeight += interCarDrawHeight;
            }
        }
        return carDrawWidth;
    }

    public void draw(Canvas canvas, Context context) {
        for (Car car: cars) {
            car.draw(canvas, context);
        }
    }
}
