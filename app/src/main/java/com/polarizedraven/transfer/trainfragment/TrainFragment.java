package com.polarizedraven.transfer.trainfragment;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.polarizedraven.transfer.R;

/**
 * Created by aaron on 2/27/18.
 */

public class TrainFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_TITLE = "title";
    private final int carNumber = 10;
    private final int doorsPerCar = 3;
    private final int[] carIds = new int[carNumber];
    private final int[][] doorIds = new int[carNumber][doorsPerCar];

    public TrainFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TrainFragment newInstance(String title) {
        TrainFragment fragment = new TrainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_station, container, false);
        ConstraintLayout tcl = rootView.findViewById(R.id.station_layout);


        int parentId = tcl.getId();
        ConstraintSet cs = new ConstraintSet();

        for(int i=0; i < carNumber; i++) {
                //add a car
                ImageView car = new ImageView(getContext());
                int carId = View.generateViewId();
                car.setId(carId);
                carIds[i] = carId;
                car.setBackgroundColor(getResources().getColor(R.color.lineDarkGrey));
                tcl.addView(car);
                cs.centerHorizontally(carId, parentId);
                cs.constrainWidth(carId, 100);
                cs.constrainHeight(carId, ConstraintSet.MATCH_CONSTRAINT);
        }

        /**
         * Manually create our vertical chain of cars
         */

        cs.setVerticalChainStyle(carIds[0],ConstraintSet.CHAIN_SPREAD_INSIDE);
        cs.connect(carIds[0],ConstraintSet.TOP , parentId, ConstraintSet.TOP, 5);
        for(int i = 1; i < carIds.length; ++i) {
            cs.connect(carIds[i], ConstraintSet.TOP, carIds[i - 1], ConstraintSet.BOTTOM, 5);
            cs.connect(carIds[i - 1], ConstraintSet.BOTTOM, carIds[i], ConstraintSet.TOP, 5);
        }
        cs.connect(carIds[carIds.length - 1], ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM, 5);

        /**
         * Add doors to the cars
         */
        for (int car=0; car<carNumber; car++) {
            int carId = carIds[car];
            for (int door = 0; door < doorsPerCar; door++) {

                ImageView doorView = new ImageView(getContext());
                int doorId = View.generateViewId();
                doorView.setId(doorId);
                doorIds[car][door] = doorId;
                doorView.setBackgroundColor(getResources().getColor(R.color.lineLime));
                tcl.addView(doorView);
                cs.connect(doorId,ConstraintSet.RIGHT, carId,ConstraintSet.RIGHT);
                cs.constrainWidth(doorId, 100);
                cs.constrainHeight(doorId, ConstraintSet.MATCH_CONSTRAINT);
            }

            cs.setVerticalChainStyle(doorIds[0][0],ConstraintSet.CHAIN_SPREAD_INSIDE);
            cs.connect(doorIds[car][0],ConstraintSet.TOP , carIds[car], ConstraintSet.TOP, 5);
            for(int i = 1; i < doorIds[car].length; ++i) {
                cs.connect(doorIds[car][i], ConstraintSet.TOP, doorIds[car][i - 1], ConstraintSet.BOTTOM, 5);
                cs.connect(doorIds[car][i - 1], ConstraintSet.BOTTOM, doorIds[car][i], ConstraintSet.TOP, 5);
            }
            cs.connect(doorIds[car][doorIds[car].length - 1], ConstraintSet.BOTTOM, carIds[car], ConstraintSet.BOTTOM, 5);

        }
        /**
         * Create the chains that position the doors
         */

        cs.applyTo(tcl);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ConstraintLayout cl = getView().findViewById(R.id.station_layout);
        ConstraintSet cs = new ConstraintSet();
        cs.clone(cl);
        cl.requestLayout();
    }
}