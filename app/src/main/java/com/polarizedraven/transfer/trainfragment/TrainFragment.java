package com.polarizedraven.transfer.trainfragment;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.polarizedraven.transfer.R;
import com.polarizedraven.transfer.view.AutoResizeTextView;

/**
 * Created by aaron on 2/27/18.
 */

public class TrainFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_TITLE = "title";
    private static final int INTER_TRAIN_MARGIN = 5;
    private static final int INTER_DOOR_MARGIN = 5;
    private static final int DOOR_EDGE_MARGIN = 10;
    private static final int TRAIN_WIDTH = 100;
    private static final int DOOR_WIDTH = TRAIN_WIDTH*3/4;
    private final int carNumber = 10;
    private final int doorsPerCar = 3;
    private final int[] carIds = new int[carNumber];
    private final int[] tvIds = new int[carNumber];
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
        ConstraintSet cs = new ConstraintSet();
        int parentId = tcl.getId();

        /**
         * Draw the cars
         */
        for(int i=0; i < carNumber; i++) {
            //add a car
            ImageView car = new ImageView(getContext());
            int carId = View.generateViewId();
            car.setId(carId);
            carIds[i] = carId;
            car.setBackgroundColor(getResources().getColor(R.color.lineDarkGrey));
            tcl.addView(car);
            cs.constrainWidth(carId, TRAIN_WIDTH);
            cs.constrainHeight(carId, ConstraintSet.MATCH_CONSTRAINT);

            //first car gets a unique horizontal constraint, rest are constrained to the first
            if (i==0) {
                //horizontal positioning happens after car numbers have been generated
            } else {
                cs.centerHorizontally(carId,carIds[i-1]);
            }
        }

        /**
         * Draw the car numbers
         */
        for(int i=0; i < carNumber; i++) {
            //add a car
            AutoResizeTextView tv = new AutoResizeTextView(getContext());
            tv.setText(String.valueOf(i+1));
            //set the text size overly large to begin, the view will automatically scale it down, but not up
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,24);
            //text should be as large as possible, must be set after setting text as resetting text resets this field
            tv.setMaxTextSize(0f);
            tv.setTextColor(getResources().getColor(R.color.textPrimary));
            int tvId = View.generateViewId();
            tv.setId(tvId);
            tcl.addView(tv);
            tvIds[i] = tvId;
            tv.setGravity(Gravity.CENTER);

            cs.constrainWidth(tvId, TRAIN_WIDTH);
            //match the height of the train cars
            cs.connect(tvId,ConstraintSet.TOP,carIds[i],ConstraintSet.TOP);
            cs.connect(tvId,ConstraintSet.BOTTOM,carIds[i],ConstraintSet.BOTTOM);
            //numbers hug left
            cs.connect(tvId,ConstraintSet.LEFT,parentId,ConstraintSet.LEFT);
        }

        cs.connect(carIds[0],ConstraintSet.LEFT,tvIds[0],ConstraintSet.RIGHT);





        /**
         * Manually create our vertical chain of cars
         */
        cs.setVerticalChainStyle(carIds[0],ConstraintSet.CHAIN_SPREAD_INSIDE);
        cs.connect(carIds[0],ConstraintSet.TOP , parentId, ConstraintSet.TOP, INTER_TRAIN_MARGIN);
        for(int i = 1; i < carIds.length; ++i) {
            cs.connect(carIds[i], ConstraintSet.TOP, carIds[i - 1], ConstraintSet.BOTTOM, INTER_TRAIN_MARGIN);
            cs.connect(carIds[i - 1], ConstraintSet.BOTTOM, carIds[i], ConstraintSet.TOP, INTER_TRAIN_MARGIN);
        }
        cs.connect(carIds[carIds.length - 1], ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM, INTER_TRAIN_MARGIN);

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
                cs.constrainWidth(doorId, DOOR_WIDTH);
                cs.constrainHeight(doorId, ConstraintSet.MATCH_CONSTRAINT);
                cs.setVerticalWeight(doorId,1.0f);
            }

            /**
             * Create the chains that position the doors
             */
            cs.setVerticalChainStyle(doorIds[0][0],ConstraintSet.CHAIN_SPREAD_INSIDE);
            cs.connect(doorIds[car][0],ConstraintSet.TOP , carIds[car], ConstraintSet.TOP, DOOR_EDGE_MARGIN);
            for(int i = 1; i < doorIds[car].length; ++i) {
                cs.connect(doorIds[car][i], ConstraintSet.TOP, doorIds[car][i - 1], ConstraintSet.BOTTOM, INTER_DOOR_MARGIN);
                cs.connect(doorIds[car][i - 1], ConstraintSet.BOTTOM, doorIds[car][i], ConstraintSet.TOP, INTER_DOOR_MARGIN);
            }
            cs.connect(doorIds[car][doorIds[car].length - 1], ConstraintSet.BOTTOM, carIds[car], ConstraintSet.BOTTOM, DOOR_EDGE_MARGIN);

        }


        cs.applyTo(tcl);
        return rootView;
    }

}
