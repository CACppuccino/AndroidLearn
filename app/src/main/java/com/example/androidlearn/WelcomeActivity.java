package com.example.androidlearn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity {

    private long delay = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // turning off the title(action bar) at the top of the screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // hide the status bar

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // set the welcome view
        setContentView(R.layout.activity_welcome);
        // set a timer
        Timer timer = new Timer();
        timer.schedule(task,delay);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Intent in = new Intent().setClass(WelcomeActivity.this,
                    menu.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
            finish();
        }
    };
}
