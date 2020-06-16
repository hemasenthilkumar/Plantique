package com.example.plantique;

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
import androidx.fragment.app.FragmentTransaction;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CartFragment extends Fragment {
    String s;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_cart,container,false);
        display_cart(view);
        return view;

    }

    private void display_cart(final View view)
    {

        String usnm = ((MyApplication) view.getContext().getApplicationContext()).getUsername();
        s = "http://192.168.1.6:5000/showCart?usname="+usnm;
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
                    if(testV.length()!=0)
                    add_table(view,testV);
                    else
                    {   Button b=view.findViewById(R.id.button7);
                        b.setVisibility(View.GONE);
                        TextView tt=view.findViewById(R.id.textView16);
                        tt.setText("Cart Empty");
                    }

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

    private void add_table(final View view, JSONArray testV) throws JSONException {
        List<JSONObject> l;
        int Total_price=0;
        String pid="";
        TableLayout stk=(TableLayout)view.findViewById(R.id.table_main);
        TableRow tbrow= new TableRow(this.getContext());
        //Table headers
        TextView tv0=new TextView(this.getContext());
        tv0.setText("Product Name");
        tv0.setGravity(Gravity.CENTER);
        tv0.setBackgroundResource(R.drawable.border);
        tbrow.addView(tv0);
        TextView tv1=new TextView(this.getContext());
        tv1.setText("Price");
        tv1.setGravity(Gravity.CENTER);
        tv1.setBackgroundResource(R.drawable.border);
        tbrow.addView(tv1);
        TextView tv2=new TextView(this.getContext());
        tv2.setText("Unique Product ID");
        tv2.setGravity(Gravity.CENTER);
        tv2.setBackgroundResource(R.drawable.border);
        tbrow.addView(tv2);
       TextView tv3=new TextView(this.getContext());
        tv3.setBackgroundResource(R.drawable.border);
       tbrow.addView(tv3);
        stk.addView(tbrow);

        for(int i=0;i<testV.length();i++)
        {
            TableRow trow=new TableRow(this.getContext());
            JSONObject j= (JSONObject) testV.get(i);
            Iterator<String> keys = j.keys();
            Total_price+= Integer.parseInt((String) j.get("price"));
            pid= j.get("id_").toString();
            while(keys.hasNext())
            {

                String key = keys.next();
                TextView t1=new TextView(this.getContext());
                t1.setText(j.get(key).toString());
                t1.setGravity(Gravity.CENTER);
                t1.setBackgroundResource(R.drawable.border);
                trow.addView(t1);
            }
            Button b1=new Button(this.getContext());
            b1.setText("Remove product");
            final String finalpid = pid;
            b1.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    removeprod(view, finalpid);
                }
            });
            trow.addView(b1);
            stk.setBackgroundResource(R.drawable.border);
            stk.addView(trow);
        }

        TextView tt=view.findViewById(R.id.textView16);
        tt.setText("Total Amount to Pay"+Integer.toString(Total_price));
        Button button=view.findViewById(R.id.button7);
        final int finalTotal_price = Total_price;
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                clear_cart(view,Integer.toString(finalTotal_price));
            }
        });


    }
    public void removeprod(final View view, String pid)
    {
        String usnm = ((MyApplication) view.getContext().getApplicationContext()).getUsername();
        s = "http://192.168.1.6:5000//removeProduct?usname="+usnm+"&pid="+pid;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this.s, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //to change to JSON
                toast(view,"Removed from cart");
                refresh();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //post.setText("Unable to load posts!");
            }


        });

    }
    public void clear_cart(final View view, String tot)
    {

        String usnm = ((MyApplication) view.getContext().getApplicationContext()).getUsername();
        s = "http://192.168.1.6:5000//Emptycart?usname="+usnm+"&tot="+tot;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this.s, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //to change to JSON
               toast(view,"Payment Succesfull");
               refresh();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //post.setText("Unable to load posts!");
            }


        });

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
        Toast.makeText(CartFragment.this.getContext(),res, Toast.LENGTH_LONG).show();
    }
}
