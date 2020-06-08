package com.example.plantique;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AddProductFragment extends Fragment {
    String s = null;
    String[] type={"Indoor","Outdoor","Decorative"};
    String[] loc={"Vellore","Chennai","Bangalore","Hyderabad"};
    EditText name,price;
    Spinner spin,spin1;
    Integer loc1,cat;
   List<String> info;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        spin = (Spinner) view.findViewById(R.id.spinner2);
         spin1 = (Spinner) view.findViewById(R.id.spinner);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this.getContext(),android.R.layout.simple_spinner_item,type);
        ArrayAdapter aa1 = new ArrayAdapter(this.getContext(),android.R.layout.simple_spinner_item,loc);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        spin1.setAdapter(aa1);
        Button button = (Button)view.findViewById(R.id.button5);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                add_product(view);
            }
        });
        return view;
    }

    public void add_product(final View view)
    {
        String usnm = ((MyApplication) view.getContext().getApplicationContext()).getUsername();
        info=new ArrayList<>();
        spin = view.findViewById(R.id.spinner);
       loc1= spin.getSelectedItemPosition();
        spin1 = view.findViewById(R.id.spinner2);
        cat = spin1.getSelectedItemPosition();
        name=(view.findViewById(R.id.pname));
        price=(view.findViewById(R.id.editText));
        info.add(usnm);
        info.add(name.getText().toString());
        info.add(price.getText().toString());
        info.add(loc1.toString());
        info.add(cat.toString());

        s = "http://192.168.1.6:5000/addProduct?info="+info;
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
        Toast.makeText(this.getContext(),res, Toast.LENGTH_LONG).show();
    }

}
