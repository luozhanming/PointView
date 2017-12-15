package com.example.cdc4512.pointview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pointview.PointView;


public class MainActivity extends AppCompatActivity {

    private PointView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (PointView) findViewById(R.id.view1);
        view.setScores(0.1f,0.5f);
        view.setMaxPoint(1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setCurrentPointWithAnimation(500,1000);
            }
        },1000);
    }
}
