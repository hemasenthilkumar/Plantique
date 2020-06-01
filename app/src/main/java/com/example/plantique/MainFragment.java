package com.example.plantique;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.plantique.adapter.PostAdapter;
import com.example.plantique.dataprovider.PostDataProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainFragment extends Fragment {
    String s="";
    private ListView listView;
    private String[] posts;
    private PostDataProvider dataProvider;
    private PostAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main,container,false);
        display_post(view);
        return view;

    }

    public void display_list(List<PostClass> map,View view)
    {
        listView = view.findViewById(R.id.postlist);
        adapter = new PostAdapter(view.getContext(), R.layout.post_layout);
        listView.setAdapter(adapter);
                for(PostClass pc:map) {
                    dataProvider = new PostDataProvider(pc.getPost_text(),pc.getName(),pc.getPost_date());
                    adapter.add(dataProvider);
                }


    }

    public void display_post(final View view)
    {

        //post=view.findViewById(R.id.posts);
        s = "http://192.168.1.6:5000/userhome?usname=hema";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(s, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //to change to JSON
                List<PostClass> lists=new ArrayList<>();
                JSONObject testV=null;

                try {
                    testV =new JSONObject(new String(responseBody));
                    for (Iterator<String> it = testV.keys(); it.hasNext(); ) {
                        String str = it.next();
                        JSONArray jsonar=testV.getJSONArray(str);
                        for(int i=0;i<jsonar.length();i++)
                        {

                            PostClass pobj=new PostClass(str,jsonar.getJSONArray(i).get(0).toString(), (String) jsonar.getJSONArray(i).get(1));
                            lists.add(pobj);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               display_list(lists,view);
                //post.setText(postMap.keySet().toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //post.setText("Unable to load posts!");
            }


        });


    }
}
