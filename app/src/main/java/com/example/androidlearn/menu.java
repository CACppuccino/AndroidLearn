package com.example.androidlearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setListeners();
//        Intent receive = this.getIntent();
//        String letter = receive.getExtras().getString("letter");
//        TextView tv = findViewById(R.id.tview);
//        tv.setText(letter);
    }
    private void setView(){
        setContentView(R.layout.menu);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    /*
    *  Set the listenrs of this layout components
    * */
    private void setListeners(){

    }
}
