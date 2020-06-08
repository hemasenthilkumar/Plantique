package com.example.plantique;
import com.example.plantique.HomeActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FriendFragment extends Fragment {
    String s=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_friends,container,false);
        display_frnd(view);
        return view;
    }



    public void display_frnd(final View view)
    {

        String usnm = ((MyApplication) view.getContext().getApplicationContext()).getUsername();
        s = "http://192.168.1.6:5000/friends?usname="+usnm;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {

                try {
                    String th="";
                   JSONObject j = new JSONObject(new String(responseBody, "UTF-8"));
                   add_table(view, j);

                } catch (UnsupportedEncodingException | JSONException e) {
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
    private void connect(final View view, String key, String val)
    {

        String usnm = ((MyApplication) view.getContext().getApplicationContext()).getUsername();
        String s = "http://192.168.1.6:5000/follow?usname="+usnm+"&folname="+key+"&value="+val;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {

                toast(view,"Done!");
                refresh();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //post.setText("Unable to load posts!");

                toast(view,"Failure!");
            }


        });

    }

    private void add_table(View view, final JSONObject j) throws JSONException {

        TableLayout stk=(TableLayout)view.findViewById(R.id.table_main1);
        Iterator<String> keys = j.keys();
        while(keys.hasNext())
        {
            TableRow trow=new TableRow(this.getContext());
            final String key = keys.next();
            TextView t1=new TextView(this.getContext());
            t1.setText(key);
            t1.setGravity(Gravity.CENTER);
            final Button b=new Button(this.getContext());
            b.setText(j.get(key).toString().equals("0")? "Follow":"Unfollow" );
            b.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    try {
                        connect(v,key,j.get(key).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            trow.addView(t1);
            trow.addView(b);
            stk.addView(trow);

        }


    }
    public void refresh()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }
    public void toast(View view, String res)
    {
        Toast.makeText(FriendFragment.this.getContext(),res, Toast.LENGTH_LONG).show();
    }

}
