package com.example.androidlearn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

public class gesture extends AppCompatActivity {

    private boolean AUTO_START = true;
    private int FLIP_INTERVAL = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setListener();
        setProperties();
    }

    private void setView(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gesture);
        ViewFlipper vf = (ViewFlipper) findViewById(R.id.flip_view);
        vf.setAutoStart(AUTO_START);
        vf.setFlipInterval(FLIP_INTERVAL);
        vf.setInAnimation(AnimationUtils.loadAnimation(gesture.this, R.anim.in_from_left));
        vf.setOutAnimation(AnimationUtils.loadAnimation(gesture.this, R.anim.out_to_right));
    }

    private void setListener(){
        final ViewFlipper vf = (ViewFlipper) findViewById(R.id.flip_view);
        vf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                vf.stopFlipping();
                return false;
            }
        });
    }

    private void setProperties(){

    }
}
