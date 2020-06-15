package com.example.plantique;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private TextView message;
    private EditText us, pwd;
    String s = "", res = "";
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_main);
        message = findViewById(R.id.Text);

    }

    public void sign(View view)
    {
        redirect2();
    }

    public void login(final View view) {

        us = findViewById(R.id.editText);
        pwd = findViewById(R.id.editText2);
        final String  u=us.getText().toString();
        final String  p= pwd.getText().toString();
        s = "http://192.168.1.6:5000/login?us="+u+"&ps="+p;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                toast(view,"Login successful");
                redirect(u);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                toast(view,"Login failure");
            }


        });

    }

    public void redirect(String u)
    {
        ((MyApplication)this.getApplicationContext()).setUsername(u);
        i=new Intent(this,HomeActivity.class);
        startActivity(i);
    }

    public void redirect2()
    {
        i=new Intent(this,SignupA.class);
        startActivity(i);
    }


    public void toast(View view, String res)
    {
        Toast.makeText(MainActivity.this,res, Toast.LENGTH_LONG).show();
    }


}
