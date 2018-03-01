package com.polarizedraven.transfer.trainfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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


import static android.support.constraint.ConstraintSet.CHAIN_SPREAD_INSIDE;
import static android.support.constraint.ConstraintSet.END;
import static android.support.constraint.ConstraintSet.MATCH_CONSTRAINT;
import static android.support.constraint.ConstraintSet.RIGHT;
import static android.support.constraint.ConstraintSet.LEFT;
import static android.support.constraint.ConstraintSet.START;
import static android.support.constraint.ConstraintSet.TOP;
import static android.support.constraint.ConstraintSet.BOTTOM;

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
    private static final int CONDUCTOR_BOARD_WIDTH = TRAIN_WIDTH/4;

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

        createCar(tcl,cs,true, true,5,10,3);
        createCar(tcl,cs,true, false,4,8,4);


        cs.applyTo(tcl);
        return rootView;
    }


    /**
     * Create a train along with constraints
     * @param parent - the parent constraint layout to add the car to
     * @param cs - constraint set to be applied to final view
     * @param numberCars - if true the car will include numbered cars
     * @param bindRight - if true the car will layout to the right side
     * @param conductorCar - the car where the conductor board will be drawn
     * @return
     */
    private TrainIds createCar(ConstraintLayout parent, ConstraintSet cs, boolean numberCars,
                               boolean bindRight, int conductorCar, int carNumber, int doorsPerCar) {
        int[] carIds = new int[carNumber];
        int[] tvIds = new int[carNumber];
        int[][] doorIds = new int[carNumber][doorsPerCar];

        int parentId = parent.getId();

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
            parent.addView(car);
            cs.constrainWidth(carId, TRAIN_WIDTH);
            cs.constrainHeight(carId, MATCH_CONSTRAINT);

            //first car gets a unique horizontal constraint, rest are constrained to the first
            if (i==0) {
                //horizontal positioning happens after car numbers have been generated
            } else {
                cs.centerHorizontally(carId,carIds[i-1]);
            }
        }

        if (numberCars) {
            /**
             * Draw the car numbers
             */
            for (int i = 0; i < carNumber; i++) {
                //add a car
                AutoResizeTextView tv = new AutoResizeTextView(getContext());
                tv.setText(String.valueOf(i + 1));
                //set the text size overly large to begin, the view will automatically scale it down, but not up
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
                //text should be as large as possible, must be set after setting text as resetting text resets this field
                tv.setMaxTextSize(0f);
                tv.setTextColor(getResources().getColor(R.color.textPrimary));
                int tvId = View.generateViewId();
                tv.setId(tvId);
                parent.addView(tv);
                tvIds[i] = tvId;
                tv.setGravity(Gravity.CENTER);

                cs.constrainWidth(tvId, TRAIN_WIDTH);
                //match the height of the train cars
                cs.connect(tvId, TOP, carIds[i], TOP);
                cs.connect(tvId, BOTTOM, carIds[i], BOTTOM);
                if (bindRight) {
                    //numbers hug left
                    cs.connect(tvId, RIGHT, parentId, RIGHT);
                } else {
                    //numbers hug left
                    cs.connect(tvId, LEFT, parentId, LEFT);
                }
            }

            if (bindRight) {
                //constrain the first car to the first number
                cs.connect(carIds[0], RIGHT, tvIds[0], LEFT);
            } else {
                //constrain the first car to the first number
                cs.connect(carIds[0], LEFT, tvIds[0], RIGHT);
            }

        } else {
            //No numbers
            if (bindRight) {
                //constrain the first car to the parent
                cs.connect(carIds[0], RIGHT, parentId, RIGHT);

            } else {
                //constrain the first car to the parent
                cs.connect(carIds[0], LEFT, parentId, LEFT);
            }
        }

        /**
         * Manually create our vertical chain of cars
         */
        cs.setVerticalChainStyle(carIds[0], CHAIN_SPREAD_INSIDE);
        cs.connect(carIds[0],TOP , parentId, TOP, INTER_TRAIN_MARGIN);
        for(int i = 1; i < carIds.length; ++i) {
            cs.connect(carIds[i], TOP, carIds[i - 1], BOTTOM, INTER_TRAIN_MARGIN);
            cs.connect(carIds[i - 1], BOTTOM, carIds[i], TOP, INTER_TRAIN_MARGIN);
        }
        cs.connect(carIds[carIds.length - 1], BOTTOM, parentId, BOTTOM, INTER_TRAIN_MARGIN);

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
                doorView.setBackgroundColor(getResources().getColor(R.color.lineBlue));
                parent.addView(doorView);
                //doors layout towards center always
                if (bindRight) {
                    cs.connect(doorId, LEFT, carId, LEFT);
                } else {
                    cs.connect(doorId, RIGHT, carId, RIGHT);
                }
                cs.constrainWidth(doorId, DOOR_WIDTH);
                cs.constrainHeight(doorId, MATCH_CONSTRAINT);
                cs.setVerticalWeight(doorId,1.0f);
            }

            /**
             * Create the chains that position the doors
             */
            cs.setVerticalChainStyle(doorIds[0][0],CHAIN_SPREAD_INSIDE);
            cs.connect(doorIds[car][0],TOP , carIds[car], TOP, DOOR_EDGE_MARGIN);
            for(int i = 1; i < doorIds[car].length; ++i) {
                cs.connect(doorIds[car][i], TOP, doorIds[car][i - 1], BOTTOM, INTER_DOOR_MARGIN);
                cs.connect(doorIds[car][i - 1], BOTTOM, doorIds[car][i], TOP, INTER_DOOR_MARGIN);
            }
            cs.connect(doorIds[car][doorIds[car].length - 1], BOTTOM, carIds[car], BOTTOM, DOOR_EDGE_MARGIN);
        }

        /**
         * Draw the conductor board for the car
         */
        ImageView conductorBoard = new ImageView(getContext());
        conductorBoard.setBackground(getContext().getDrawable(R.drawable.ic_stop_marker));
        int boardId = View.generateViewId();
        conductorBoard.setId(boardId);
        parent.addView(conductorBoard);
        cs.connect(boardId,BOTTOM,doorIds[conductorCar][0],BOTTOM);
        cs.connect(boardId,TOP,doorIds[conductorCar-1][doorIds[conductorCar-1].length-1],TOP);
        if(bindRight) {
            cs.connect(boardId,END,carIds[0],START,10);
        } else {
            cs.connect(boardId,START,carIds[0],END,10);
        }
        cs.constrainHeight(boardId,MATCH_CONSTRAINT);
        cs.constrainWidth(boardId,CONDUCTOR_BOARD_WIDTH);
        return new TrainIds(carIds,doorIds,tvIds);
    }
}


class TrainIds {
    public int[] carIds;
    public int[][] doorIds;
    @Nullable
    public int[] tvIds;

    public TrainIds(int[] carIds, int[][] doorIds, @Nullable int[] tvIds) {
        this.carIds = carIds;
        this.doorIds = doorIds;
        this.tvIds = tvIds;
    }
}
