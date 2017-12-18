package com.example.cdc4512.pointview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.pointview.PointView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PointView view;
    private SeekBar seekBar;
    private EditText point, max;
    private Button pointSet, maxSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (PointView) findViewById(R.id.view1);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        point = (EditText) findViewById(R.id.point);
        max = (EditText) findViewById(R.id.max);
        pointSet = (Button) findViewById(R.id.pointSet);
        maxSet = (Button) findViewById(R.id.maxSet);
        view.setScores(0.1f, 0.5f);
        view.setMaxPoint(1000);
        seekBar.setMax(view.getMaxPoint());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setCurrentPointWithAnimation(500, 1000);
            }
        }, 1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                view.setCurrentPoint(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        pointSet.setOnClickListener(this);
        maxSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pointSet:
                Integer point = Integer.valueOf(this.point.getText().toString());
                if (point > view.getMaxPoint()) {
                    Toast.makeText(this,"设置值超过最大值",Toast.LENGTH_SHORT).show();
                    return;
                }
                view.setCurrentPointWithAnimation(point, 2000);
                seekBar.setProgress(point);
                break;
            case R.id.maxSet:
                view.setMaxPoint(Integer.valueOf(max.getText().toString()));
                seekBar.setMax(Integer.valueOf(max.getText().toString()));
                break;
        }
    }
}
