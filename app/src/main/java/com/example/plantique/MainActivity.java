package com.example.plantique;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private TextView message;
    private EditText us,pwd;
    String s="",res="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message=findViewById(R.id.Text);
    }

    public void login(View view)
    {
        us=findViewById(R.id.editText);
        pwd=findViewById(R.id.editText2);
        s="http://192.168.1.2:5000/login?us="+us.getText().toString()+"&ps="+pwd.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {

                message.setText("Login Success!");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                message.setText("Login Failure!");
            }

        });

    }


}
