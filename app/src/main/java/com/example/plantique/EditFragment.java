package com.example.plantique;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class EditFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_edit,container,false);
        display_profile(view);

        return view;
    }

    public class edit_role extends DialogFragment {

//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            LayoutInflater inflater = getActivity().getLayoutInflater();
//            View view = inflater.inflate(R.layout.edit_role_activity, null);
//            builder.setView(view);
//            return builder.create();
//        }

    }

    private void display_profile(final View view)
    {

        String usnm = ((MyApplication) view.getContext().getApplicationContext()).getUsername();
       String s = "http://192.168.1.6:5000/edit?usname="+usnm;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //to change to JSON
                JSONObject testV=null;
                String response = null;
                try {
                    response = new String(responseBody, "UTF-8");
                    testV =new JSONObject(new String(responseBody));
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

                try {
                    add_Table(view,testV);
                } catch (JSONException ex) {
        ex.printStackTrace();
        }

        }

@Override
public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
        //post.setText("Unable to load posts!");
        }


        });
        }

    private void add_Table(View view, JSONObject testV) throws JSONException
    {
               TextView follows=view.findViewById(R.id.textView19);
               TextView followedby=view.findViewById(R.id.textView20);
               TextView role=view.findViewById(R.id.textView21);
               TextView doj=view.findViewById(R.id.textView22);

              follows.setText(testV.get("Follows").toString());
               followedby.setText(testV.get("Followed by").toString());
               role.setText(testV.get("Role").toString());
               doj.setText(testV.get("Date").toString());

    }




}
