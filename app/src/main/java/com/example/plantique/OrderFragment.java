package com.example.plantique;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.plantique.adapter.PostAdapter;
import com.example.plantique.dataprovider.PostDataProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class OrderFragment extends Fragment
{
    String s;
    TextView t,tv,qty;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order,container,false);
        display_order(view);
        return view;
    }


    public void display_order(final View view)
    {

        String usnm = ((MyApplication) view.getContext().getApplicationContext()).getUsername();
        s = "http://192.168.1.6:5000/Orders?usname="+usnm;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //to change to JSON
                JSONArray testV=null;
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                    testV =new JSONArray(new String(responseBody));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }


                try {
                    add_table(view,testV);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //post.setText("Unable to load posts!");
            }


        });


    }

    private void add_table(View view, JSONArray testV) throws JSONException {
        List<JSONObject> l;
        TableLayout stk=(TableLayout)view.findViewById(R.id.table_main);
        TableRow tbrow= new TableRow(this.getContext());
        //Table headers
        TextView tv0=new TextView(this.getContext());
        tv0.setText("Product");
        tv0.setGravity(Gravity.CENTER);
        tv0.setWidth(15);
        tv0.setBackgroundResource(R.drawable.border);
        tbrow.addView(tv0);
        TextView tv1=new TextView(this.getContext());
        tv1.setText("Amount");
        tv1.setGravity(Gravity.CENTER);
        tv1.setBackgroundResource(R.drawable.border);
        tbrow.addView(tv1);
        TextView tv2=new TextView(this.getContext());
        tv2.setText("Date");
        tv2.setGravity(Gravity.CENTER);
        tv2.setBackgroundResource(R.drawable.border);
        tbrow.addView(tv2);
       stk.setBackgroundResource(R.drawable.border);
        stk.addView(tbrow);

        for(int i=0;i<testV.length();i++)
        {
            TableRow trow=new TableRow(this.getContext());
            JSONObject j= (JSONObject) testV.get(i);
            Iterator<String> keys = j.keys();
            while(keys.hasNext())
            {
                String key = keys.next();
                TextView t1=new TextView(this.getContext());
                t1.setText(j.get(key).toString());
                t1.setGravity(Gravity.CENTER);
                t1.setBackgroundResource(R.drawable.border);
                trow.addView(t1);
            }
           stk.setBackgroundResource(R.drawable.border);
            stk.addView(trow);
        }

    }
}


