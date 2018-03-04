package com.polarizedraven.transfer;

/**
 * Created by aaron on 3/3/18.
 */

public class ExitAnchor {
    public int startCar;
    public int endCar;
    public int startDoor;
    public int endDoor;

    public ExitAnchor(int startCar, int endCar, int startDoor, int endDoor) {
        this.startCar = startCar;
        this.endCar = endCar;
        this.startDoor = startDoor;
        this.endDoor = endDoor;
    }
}
