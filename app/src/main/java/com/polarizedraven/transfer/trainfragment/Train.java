package com.polarizedraven.transfer.trainfragment;

/**
 * Created by aaron on 3/1/18.
 */

public class Train {
    public int numberOfCars;
    public int doorsPerCar;
    public int conductorCar;

    public Train(int numberOfCars, int doorsPerCar, int conductorCar) {
        this.numberOfCars = numberOfCars;
        this.doorsPerCar = doorsPerCar;
        this.conductorCar = conductorCar;
    }
}
