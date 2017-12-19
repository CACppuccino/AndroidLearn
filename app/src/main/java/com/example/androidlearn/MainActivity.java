package com.example.androidlearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    static String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onStart(){
        super.onStart();
        setView();
        setListener();
    }

    private void setView(){
        setContentView(R.layout.activity_main);

    }


    private void setListener(){
        Button search = findViewById(R.id.button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText bid = findViewById(R.id.textBox);
                String res = bid.getText().toString();
                String url  = "https://api.github.com/users/";
                requestForInfo(res,url);
            }
        });
    }

    private void requestForInfo(String bid, String url){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//        final TextView tv = findViewById(R.id.result);
        Log.d("url: ", url+bid);
        JsonObjectRequest bidRequest = new JsonObjectRequest(Request.Method.GET,
                url + bid, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                renewTv(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                tv.setText("failed to request");
            }
        });
        queue.add(bidRequest);
    }
    private String nullorEmp(String x){
        if (x == null || x.isEmpty()) return "";
        else return x;
    }

    private void renewTv(JSONObject obj){
        final TextView emailtv = findViewById(R.id.ufield_email),
                comptv = findViewById(R.id.ufield_comp);
        final ImageView avatar = findViewById(R.id.avatar);
        final WebView wb = findViewById(R.id.user_wbv);
        String avatarSrc = new String();
        String email = "", company = "", url = "";
        try {
            email = obj.getString("email");
            company = obj.getString("company");
            avatarSrc = obj.getString("avatar_url");
            url = "https://github.com/"+obj.getString("login");
        }catch (Exception e){
            e.printStackTrace();
        }
        // loading image with Picasso
        Picasso.with(MainActivity.this).load(avatarSrc).into(avatar);
        comptv.setText(nullorEmp(comptv.getText().toString())+nullorEmp(company));
        emailtv.setText(nullorEmp(emailtv.getText().toString())+nullorEmp(email));
        wb.loadUrl(url);
        // an example of downloading images from the web,by using 'getBitmap' self-built lib
        //        try{
//            avatar.setImageBitmap((new getBitmap(avatar).execute(avatarSrc)).get());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        Log.d("finished","fineshed*************");
    }


    private void navigateToMenu(Intent pass){
        startActivity(pass);
    }
}
