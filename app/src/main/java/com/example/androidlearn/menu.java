package com.example.androidlearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu);

    }
    /*
    *  Set the listenrs of this layout components
    * */
    private void setListeners(){
        Button github_btn = findViewById(R.id.github_btn);
        github_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent ni = new Intent(menu.this, MainActivity.class);
                startActivity(ni);
            }
                      }
        );

        Button btn_toCam = findViewById(R.id.btn_toCam);
        btn_toCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(menu.this, camActivity.class));
            }
        });

        Button btn_toFlip = findViewById(R.id.btn_toFlip);
        btn_toFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(menu.this, gesture.class));
            }
        });

        Button btn_toCusCam = findViewById(R.id.btn_toCusCam);
        btn_toCusCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(menu.this,CusCamera.class));
            }
        });
    }
}
