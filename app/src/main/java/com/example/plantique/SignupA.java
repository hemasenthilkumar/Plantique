package com.example.plantique;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SignupA extends AppCompatActivity {
    String[] type={"Home Based seller","Whole saler","Just Gonna purchase"};
    EditText us,ps,cps,em,bday;
    Integer up;
    Spinner spin;
    String s;
    List<String> info;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
    }

    public void signup(final View view)
    {

        info=new ArrayList<>();
       us=findViewById(R.id.editText3);
        ps=findViewById(R.id.editText6);
        cps=findViewById(R.id.editText7);
        em=findViewById(R.id.editText8);
        bday=findViewById(R.id.editText4);
        spin = (Spinner) findViewById(R.id.spinner);
        up=spin.getSelectedItemPosition();
        info.add(us.getText().toString());
        info.add(ps.getText().toString());
        info.add(cps.getText().toString());
        info.add(em.getText().toString());
        info.add(bday.getText().toString());
        info.add(up.toString());

        s = "http://192.168.1.5:5000/signup?info="+info;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                toast(view,response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }


        });

    }

    public void toast(View view, String res)
    {
        Toast.makeText(SignupA.this,res, Toast.LENGTH_LONG).show();
    }
}
