package com.polarizedraven.transfer.trainfragment;

import android.opengl.Matrix;
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
import android.widget.TextView;

import com.polarizedraven.transfer.Exit;
import com.polarizedraven.transfer.ExitAnchor;
import com.polarizedraven.transfer.R;
import com.polarizedraven.transfer.view.AutoResizeTextView;

import java.util.ArrayList;

import static android.support.constraint.ConstraintSet.BOTTOM;
import static android.support.constraint.ConstraintSet.CHAIN_SPREAD_INSIDE;
import static android.support.constraint.ConstraintSet.END;
import static android.support.constraint.ConstraintSet.LEFT;
import static android.support.constraint.ConstraintSet.MATCH_CONSTRAINT;
import static android.support.constraint.ConstraintSet.RIGHT;
import static android.support.constraint.ConstraintSet.START;
import static android.support.constraint.ConstraintSet.TOP;

/**
 * Created by aaron on 2/27/18.
 */

public class TrainFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_TITLE = "title";
    private static final int LEFT_TRAINSET = 0;
    private static final int RIGHT_TRAINSET = 1;
    private static final int INTER_TRAIN_MARGIN = 5;
    private static final int INTER_DOOR_MARGIN = 5;
    private static final int DOOR_EDGE_MARGIN = 10;
    private static final int TRAIN_WIDTH = 100;
    private static final int DOOR_WIDTH = TRAIN_WIDTH*3/4;
    private static final int CONDUCTOR_BOARD_WIDTH = TRAIN_WIDTH/4;
    private ArrayList<ArrayList<TrainIds>> trains = new ArrayList<>();
    private ArrayList<TrainIds> leftTrains = new ArrayList<>();
    private ArrayList<TrainIds> rightTrains = new ArrayList<>();

    private Station station;

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
        trains.add(leftTrains);
        trains.add(rightTrains);


        View rootView = inflater.inflate(R.layout.fragment_station, container, false);
        ConstraintLayout tcl = rootView.findViewById(R.id.station_layout);
        ConstraintSet cs = new ConstraintSet();

        Station s = Station.genericStation();
        for(int i=0;i<s.trains.get(0).size();i++) {
            Train t = s.trains.get(0).get(i);
            int bindPoint = i==0?tcl.getId():leftTrains.get(i-1).conductorBoardId;
            TrainIds tid = createCar(tcl, cs, i==0, false, t.conductorCar,
                                     t.numberOfCars,t.doorsPerCar,bindPoint, i==0?LEFT:RIGHT);
            leftTrains.add(tid);
        }

        for(int i=0;i<s.trains.get(1).size();i++) {
            Train t = s.trains.get(1).get(i);
            int bindPoint = i==0?tcl.getId():rightTrains.get(i-1).conductorBoardId;
            TrainIds tid = createCar(tcl, cs, i==0, true, t.conductorCar,
                                     t.numberOfCars,t.doorsPerCar,bindPoint, i==0?RIGHT:LEFT);
            rightTrains.add(tid);
        }

        for(int i = 0; i<s.exits.size(); i++){
            Exit e = s.exits.get(i);
            addExit(e, cs, tcl);

        }

        cs.applyTo(tcl);
        return rootView;
    }

    private void addExit(Exit e, ConstraintSet cs, ConstraintLayout parent){
        String text = e.exitText;
        /**
         * Draw the left anchors for the exit
         */
        int leftStartCar = e.leftExitAnchor.startCar;
        int leftEndCar = e.leftExitAnchor.endCar;
        int leftStartDoor = e.leftExitAnchor.startDoor;
        int leftEndDoor = e.leftExitAnchor.endDoor;

        TrainIds leftAnchorTrain = leftTrains.get(leftTrains.size()-1);
        int startDoorId = leftAnchorTrain.doorIds[leftStartCar][leftStartDoor];
        int endDoorId = leftAnchorTrain.doorIds[leftEndCar][leftEndDoor];

        ImageView topBracket = new ImageView(getContext());
        int topBracketId = View.generateViewId();
        topBracket.setId(topBracketId);
        topBracket.setBackgroundColor(getResources().getColor(R.color.lineRed));
        parent.addView(topBracket);
        cs.constrainWidth(topBracketId, TRAIN_WIDTH/3);
        cs.constrainHeight(topBracketId, 10);

        ImageView bottomBracket = new ImageView(getContext());
        int bottomBracketId = View.generateViewId();
        bottomBracket.setId(bottomBracketId);
        bottomBracket.setBackgroundColor(getResources().getColor(R.color.lineRed));
        parent.addView(bottomBracket);
        cs.constrainWidth(bottomBracketId, TRAIN_WIDTH/3);
        cs.constrainHeight(bottomBracketId, 10);

        cs.connect(topBracketId,TOP,startDoorId,TOP);
        cs.connect(bottomBracketId,BOTTOM,endDoorId,BOTTOM);
        cs.connect(topBracketId,LEFT,startDoorId,RIGHT);
        cs.connect(bottomBracketId,LEFT,endDoorId,RIGHT);

        ImageView sideBracket = new ImageView(getContext());
        int sideBracketId = View.generateViewId();
        sideBracket.setId(sideBracketId);
        sideBracket.setBackgroundColor(getResources().getColor(R.color.lineRed));
        parent.addView(sideBracket);
        cs.constrainWidth(sideBracketId, 10);
        cs.constrainHeight(sideBracketId, MATCH_CONSTRAINT);

        cs.connect(sideBracketId, TOP, topBracketId, TOP);
        cs.connect(sideBracketId, RIGHT, topBracketId, RIGHT);
        cs.connect(sideBracketId, BOTTOM, bottomBracketId, BOTTOM);


        /**
         * Draw the right anchors for the exit
         */
        int rightStartCar = e.rightExitAnchor.startCar;
        int rightEndCar = e.rightExitAnchor.endCar;
        int rightStartDoor = e.rightExitAnchor.startDoor;
        int rightEndDoor = e.rightExitAnchor.endDoor;

        TrainIds rightAnchorTrain = rightTrains.get(rightTrains.size()-1);
        int rightStartDoorId = rightAnchorTrain.doorIds[rightStartCar][rightStartDoor];
        int rightEndDoorId = rightAnchorTrain.doorIds[rightEndCar][rightEndDoor];

        ImageView rightTopBracket = new ImageView(getContext());
        int rightTopBracketId = View.generateViewId();
        rightTopBracket.setId(rightTopBracketId);
        rightTopBracket.setBackgroundColor(getResources().getColor(R.color.lineRed));
        parent.addView(rightTopBracket);
        cs.constrainWidth(rightTopBracketId, TRAIN_WIDTH/3);
        cs.constrainHeight(rightTopBracketId, 10);

        ImageView rightBottomBracket = new ImageView(getContext());
        int rightBottomBracketId = View.generateViewId();
        rightBottomBracket.setId(rightBottomBracketId);
        rightBottomBracket.setBackgroundColor(getResources().getColor(R.color.lineRed));
        parent.addView(rightBottomBracket);
        cs.constrainWidth(rightBottomBracketId, TRAIN_WIDTH/3);
        cs.constrainHeight(rightBottomBracketId, 10);

        cs.connect(rightTopBracketId,TOP,rightStartDoorId,TOP);
        cs.connect(rightBottomBracketId,BOTTOM,rightEndDoorId,BOTTOM);
        cs.connect(rightTopBracketId,RIGHT,rightStartDoorId,LEFT);
        cs.connect(rightBottomBracketId,RIGHT,rightEndDoorId,LEFT);

        ImageView rightSideBracket = new ImageView(getContext());
        int rightSideBracketId = View.generateViewId();
        rightSideBracket.setId(rightSideBracketId);
        rightSideBracket.setBackgroundColor(getResources().getColor(R.color.lineRed));
        parent.addView(rightSideBracket);
        cs.constrainWidth(rightSideBracketId, 10);
        cs.constrainHeight(rightSideBracketId, MATCH_CONSTRAINT);

        cs.connect(rightSideBracketId, TOP, rightTopBracketId, TOP);
        cs.connect(rightSideBracketId, LEFT, rightTopBracketId, LEFT);
        cs.connect(rightSideBracketId, BOTTOM, rightBottomBracketId, BOTTOM);

        /**
         * Draw the exit text
         */

        TextView exitTextView = new TextView(getContext());
        int exitTextId = View.generateViewId();
        exitTextView.setId(exitTextId);
        parent.addView(exitTextView);
        exitTextView.setText(e.exitText);
        exitTextView.setGravity(Gravity.CENTER);
        cs.constrainHeight(exitTextId, TRAIN_WIDTH*2);
        cs.constrainWidth(exitTextId, MATCH_CONSTRAINT);

        cs.connect(exitTextId,LEFT, sideBracketId, RIGHT);
        cs.connect(exitTextId,RIGHT, rightSideBracketId, LEFT);
        //text centers to the left exit by default
        cs.centerVertically(exitTextId, sideBracketId);





    }

    /**
     * Create a train along with constraints
     * @param parent - the parent constraint layout to add the car to
     * @param cs - constraint set to be applied to final view
     * @param numberCars - if true the car will include numbered cars
     * @param bindRight - if true the car will layout to the right side
     * @param conductorCar - the car where the conductor board will be drawn
     * @param horizontalLayoutAnchor - the id of the view to use. layout direction based on bindRight
     * @param horizontalLayoutSide - the side of the horizontal anchor to bind to
     * @return
     */
    private TrainIds createCar(ConstraintLayout parent, ConstraintSet cs, boolean numberCars,
                               boolean bindRight, int conductorCar, int carNumber, int doorsPerCar,
                               int horizontalLayoutAnchor, int horizontalLayoutSide) {
        int[] carIds = new int[carNumber];
        int[] tvIds = new int[carNumber];
        int[][] doorIds = new int[carNumber][doorsPerCar];

        int parentId = parent.getId();

        /**
         * Draw a direction arrow (always up, never not up)
         */
        ImageView arrow = new ImageView(getContext());
        int arrowId = View.generateViewId();
        arrow.setId(arrowId);
        arrow.setBackground(getResources().getDrawable(R.drawable.arrow_up));
        parent.addView(arrow);
        cs.constrainWidth(arrowId, TRAIN_WIDTH);
        cs.constrainHeight(arrowId, TRAIN_WIDTH);
        cs.connect(arrowId,TOP,parentId,TOP);

        /**
         * Draw the cars
         */
        for(int i=0; i < carNumber; i++) {
            //add a car
            ImageView car = new ImageView(getContext());
            int carId = View.generateViewId();
            car.setId(carId);
            carIds[i] = carId;
            //car.setBackgroundColor(getResources().getColor(R.color.lineDarkGrey));
            //rotate the car based on side
            if (bindRight) {
                car.setImageDrawable(getResources().getDrawable(R.drawable.ic_carright));
            } else {
                car.setImageDrawable(getResources().getDrawable(R.drawable.ic_car));
            }
            car.setScaleType(ImageView.ScaleType.FIT_XY);
            parent.addView(car);
            cs.constrainWidth(carId, TRAIN_WIDTH);
            cs.constrainHeight(carId, MATCH_CONSTRAINT);

            //first car gets a unique horizontal constraint, rest are constrained to the first
            if (i==0) {
                cs.connect(carId,TOP, arrowId, BOTTOM);
                cs.centerHorizontally(carId,arrowId);
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
                    //numbers hug right
                    cs.connect(tvId, RIGHT, horizontalLayoutAnchor, horizontalLayoutSide);
                } else {
                    //numbers hug left
                    cs.connect(tvId, LEFT, horizontalLayoutAnchor, horizontalLayoutSide);
                }
            }

            if (bindRight) {
                //constrain the arrow to the first number
                cs.connect(arrowId, RIGHT, tvIds[0], LEFT);
            } else {
                //constrain the first car to the first number
                cs.connect(arrowId, LEFT, tvIds[0], RIGHT);
            }

        } else {
            //No numbers
            if (bindRight) {
                //constrain the first car to the parent
                cs.connect(arrowId, RIGHT, horizontalLayoutAnchor, horizontalLayoutSide);

            } else {
                //constrain the first car to the parent
                cs.connect(arrowId, LEFT, horizontalLayoutAnchor, horizontalLayoutSide);
            }
        }

        /**
         * Manually create our vertical chain of cars
         */
        cs.setVerticalChainStyle(carIds[0], CHAIN_SPREAD_INSIDE);
        cs.connect(carIds[0],TOP , arrowId, BOTTOM, INTER_TRAIN_MARGIN);
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
                if (bindRight) {
                    doorView.setImageDrawable(getResources().getDrawable(R.drawable.ic_doorright));
                } else {
                    doorView.setImageDrawable(getResources().getDrawable(R.drawable.ic_door));
                }
                doorView.setScaleType(ImageView.ScaleType.FIT_XY);
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
        return new TrainIds(carIds,doorIds,tvIds, boardId);
    }

    class TrainIds {
        public int[] carIds;
        public int[][] doorIds;
        @Nullable
        public int[] tvIds;
        public int conductorBoardId;

        public TrainIds(int[] carIds, int[][] doorIds, @Nullable int[] tvIds, int conductorBoardId) {
            this.carIds = carIds;
            this.doorIds = doorIds;
            this.tvIds = tvIds;
            this.conductorBoardId = conductorBoardId;
        }
    }
}
