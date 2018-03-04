package com.polarizedraven.transfer.trainfragment;

import com.polarizedraven.transfer.Exit;
import com.polarizedraven.transfer.ExitAnchor;

import java.util.ArrayList;

/**
 * Created by aaron on 3/1/18.
 */

public class Station {

    //first list is left trains, second is right
    public ArrayList<ArrayList<Train>> trains;
    public ArrayList<Exit> exits;

    public Station(){

    }

    static Station genericStation(){

        Station station = new Station();
        station.trains = new ArrayList<>();
        station.exits = new ArrayList<>();

        ArrayList<Train> leftTrains = new ArrayList<>();
        ArrayList<Train> rightTrains = new ArrayList<>();

        Train lt1 = new Train(10,3,5);
        Train lt2 = new Train(10,3,5);
        leftTrains.add(lt1);
        rightTrains.add(lt2);
        station.trains.add(leftTrains);
        station.trains.add(rightTrains);

        Exit exit = new Exit();
        exit.exitText = "40th St. 7th Ave";
        exit.rightExitAnchor = new ExitAnchor(1,1,0,1);
        exit.leftExitAnchor = new ExitAnchor(1,1,1,1);

        station.exits.add(exit);

        Exit exit2 = new Exit();
        exit2.exitText = "41st St. 7th Ave";
        exit2.rightExitAnchor = new ExitAnchor(5,6,2,1);
        exit2.leftExitAnchor = new ExitAnchor(5,6,2,1);

        station.exits.add(exit2);

        return station;
    }
}

