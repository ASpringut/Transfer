package com.polarizedraven.transfer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LineSelector extends AppCompatActivity implements View.OnClickListener {

    public static String LINE_KEY = "LINE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lines);

        findViewById(R.id.onetrain).setOnClickListener(this);
        findViewById(R.id.twotrain).setOnClickListener(this);
        findViewById(R.id.threetrain).setOnClickListener(this);

        findViewById(R.id.fourtrain).setOnClickListener(this);
        findViewById(R.id.fivetrain).setOnClickListener(this);
        findViewById(R.id.sixtrain).setOnClickListener(this);

        findViewById(R.id.Atrain).setOnClickListener(this);
        findViewById(R.id.Ctrain).setOnClickListener(this);
        findViewById(R.id.Etrain).setOnClickListener(this);

        findViewById(R.id.Btrain).setOnClickListener(this);
        findViewById(R.id.Dtrain).setOnClickListener(this);
        findViewById(R.id.Ftrain).setOnClickListener(this);
        findViewById(R.id.Mtrain).setOnClickListener(this);

        findViewById(R.id.shuttle).setOnClickListener(this);
        findViewById(R.id.Ltrain).setOnClickListener(this);
        findViewById(R.id.Gtrain).setOnClickListener(this);
        findViewById(R.id.seventrain).setOnClickListener(this);

        findViewById(R.id.Ntrain).setOnClickListener(this);
        findViewById(R.id.Qtrain).setOnClickListener(this);
        findViewById(R.id.Rtrain).setOnClickListener(this);
        findViewById(R.id.Wtrain).setOnClickListener(this);

        findViewById(R.id.Jtrain).setOnClickListener(this);
        findViewById(R.id.Ztrain).setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        Line line = getLineFromView(view);
        Intent intent = new Intent(this, LineViewer.class);
        intent.putExtra(LINE_KEY, LineSelector.getLineFromView(view));
        startActivity(intent);
    }


    private static Line getLineFromView(View view) {

        switch (view.getId()){
            case R.id.onetrain:
                return Line.ONE;
            case R.id.twotrain:
                return Line.TWO;
            case R.id.threetrain:
                return Line.THREE;

            case R.id.fourtrain:
                return Line.FOUR;
            case R.id.fivetrain:
                return Line.FIVE;
            case R.id.sixtrain:
                return Line.SIX;

            case R.id.seventrain:
                return Line.SEVEN;
            case R.id.shuttle:
                return Line.SHUTTLE;
            case R.id.Gtrain:
                return Line.G;
            case R.id.Ltrain:
                return Line.L;

            case R.id.Atrain:
                return Line.A;
            case R.id.Ctrain:
                return Line.C;
            case R.id.Etrain:
                return Line.E;

            case R.id.Btrain:
                return Line.B;
            case R.id.Dtrain:
                return Line.D;
            case R.id.Ftrain:
                return Line.F;
            case R.id.Mtrain:
                return Line.M;

            case R.id.Jtrain:
                return Line.J;
            case R.id.Ztrain:
                return Line.Z;

            case R.id.Ntrain:
                return Line.N;
            case R.id.Qtrain:
                return Line.Q;
            case R.id.Rtrain:
                return Line.R;
            case R.id.Wtrain:
                return Line.W;


            default:
                return Line.ONE;
        }
    }
}
