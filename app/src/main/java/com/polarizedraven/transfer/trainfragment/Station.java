package com.polarizedraven.transfer.trainfragment;

import java.util.ArrayList;

/**
 * Created by aaron on 3/1/18.
 */

public class Station {

    public ArrayList<Train> leftTrains;
    public ArrayList<Train> rightTrains;

    public Station(){

    }

    static Station genericStation(){
        Station station = new Station();
        Train lt1 = new Train(10,3,5);
        Train lt2 = new Train(8,3,4);
        Train lt3 = new Train(8,4,4);
        station.leftTrains = new ArrayList<>();
        station.rightTrains = new ArrayList<>();
        station.leftTrains.add(lt1);
        station.rightTrains.add(lt2);
        station.leftTrains.add(lt3);
        return station;
    }
}

